package com.threadtestone.thead.controller;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

@Slf4j
public class ConcurrencyTest {

    // 请求总数
    public static int clientTotal = 5000;

    // 同时并发执行的线程数
    public static int threadTotal = 200;

    public static int count = 0;

    public static void main(String[] args) throws Exception {
//        1，定义线程池
        ExecutorService executorService = Executors.newCachedThreadPool();
//        2，定义允许并发的数目
        final Semaphore semaphore = new Semaphore(threadTotal);
//        3，定义请求总数
        final CountDownLatch countDownLatch = new CountDownLatch(clientTotal);
//        4，放入请求
        for (int i = 0; i < clientTotal ; i++) {
            executorService.execute(() -> {
                try {
                    semaphore.acquire();
                    add();
                    semaphore.release();
                } catch (Exception e) {
                    log.error("error");
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        executorService.shutdown();
        System.out.println("count.......:"+count);
    }

    private static void add() {
        count++;
    }
}