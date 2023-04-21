package org.localstorm.cpu;

import java.util.Random;

public class Main {

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        CpuMonitor cpu = new CpuMonitor(200);
        cpu.start();
        Thread.currentThread().setName("Goo");
        Random rnd = new Random();
        while (true) {
            for (int i = 0; i < 2; i++) {
                Thread t1 = new Thread(new MyRunnable(rnd.nextInt(15) + 1, 50));
                t1.start();
            }
            Thread.sleep(50000);
        }
    }

    private static class MyRunnable implements Runnable {

        int sleepSec;

        int maxRun;

        public MyRunnable(int sec, int maxRun) {
            sleepSec = sec;
            this.maxRun = maxRun;
        }

        @Override
        public void run() {
            doInfWork(sleepSec, sleepSec * 2, maxRun);
        }
    }

    private static void doInfWork(int sleepSec, int workSec, int maxRun) {
        long runStart = System.currentTimeMillis();
        while (System.currentTimeMillis() - runStart < maxRun * 1000) {
            long workStart = System.currentTimeMillis();
            long count = 0;
            while ((System.currentTimeMillis() - workStart) < (workSec * 1000)) {
                count++;
            }
            try {
                Thread.sleep(sleepSec * 1000);
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}
