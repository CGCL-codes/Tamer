package org.jazzteam.edu.lang.threads.synhronized;

/**
 * 
 * 
 * @author ADLeR
 * @version $Rev: $
 */
public class Main {

    /**
	 * @param args
	 * @version 1
	 */
    public static void main(String[] args) {
        new Thread() {

            @Override
            public void run() {
                writeString();
                writeString2();
            }
        }.start();
        new Thread() {

            @Override
            public void run() {
                writeString();
            }
        }.start();
    }

    public static synchronized void writeString() {
        for (int i = 0; i < 50; i++) {
            System.out.print(i + " ");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println();
    }

    public static void writeString2() {
        for (int i = 0; i < 50; i++) {
            System.out.print(i + " ");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println();
    }
}
