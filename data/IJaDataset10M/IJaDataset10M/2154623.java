package com.aaron.concurrency;

public class MainThread {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        LiftOff launch = new LiftOff();
        launch.run();
    }
}
