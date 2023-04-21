package com.aaron.concurrency.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.aaron.concurrency.LiftOff;

/**
 * @author zhanglong
 *
 */
public class FixedThreadPool {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        ExecutorService exec = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 10; i++) {
            exec.execute(new LiftOff());
        }
        exec.shutdown();
    }
}
