package com.youda.core.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程相关的Utils函数集合.
 * 
 * @author calvin
 */
public class ThreadUtils {

    /**
	 * sleep等待,单位毫秒,忽略InterruptedException.
	 */
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
        }
    }

    /**
	 * 按照ExecutorService JavaDoc示例代码编写的Graceful Shutdown方法.
	 * 先使用shutdown尝试执行所有任务.
	 * 超时后调用shutdownNow取消在workQueue中Pending的任务,并中断所有阻塞函数.
	 * 另对在shutdown时线程本身被调用中断做了处理.
	 */
    public static void gracefulShutdown(ExecutorService pool, int timeout, TimeUnit timeUnit) {
        pool.shutdown();
        try {
            if (!pool.awaitTermination(timeout, timeUnit)) {
                pool.shutdownNow();
                if (!pool.awaitTermination(timeout, timeUnit)) {
                    System.err.println("Pool did not terminate");
                }
            }
        } catch (InterruptedException ie) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    /**
	 * 直接调用shutdownNow的方法.
	 */
    public static void normalShutdown(ExecutorService pool, int timeout, TimeUnit timeUnit) {
        try {
            pool.shutdownNow();
            if (!pool.awaitTermination(timeout, timeUnit)) {
                System.err.println("Pool did not terminate");
            }
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }

    /**
	 * 自定义ThreadPool,可定制线程池的名称.
	 */
    public static class CustomizableThreadFactory implements ThreadFactory {

        private final String namePrefix;

        private final AtomicInteger threadNumber = new AtomicInteger(1);

        public CustomizableThreadFactory(String poolName) {
            namePrefix = poolName + "-";
        }

        public Thread newThread(Runnable runable) {
            return new Thread(runable, namePrefix + threadNumber.getAndIncrement());
        }
    }
}
