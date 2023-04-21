package org.jomper.test.observer;

public class ObservedThreadTest {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        Thread t = new Thread(new ObservedThread(new DefaultThreadObserver()));
        t.start();
    }
}
