package de.fuh.xpairtise.tests.nonjunit.network.activemq;

public class ActiveMQPerformanceTest {

    public static void main(final String[] args) {
        Thread t1 = new Thread(new Runnable() {

            public void run() {
                ActiveMQPerformanceTestServer.main(args);
            }
        });
        t1.start();
        Thread t2 = new Thread(new Runnable() {

            public void run() {
                ActiveMQPerformanceTestClient.main(args);
            }
        });
        t2.start();
    }
}
