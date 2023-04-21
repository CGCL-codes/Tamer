package nl.utwente.ewi.stream.network;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolManualTest {

    public ThreadPoolExecutor pool = new ThreadPoolExecutor(2, 4, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

    public ThreadPoolManualTest() {
    }

    public void run() throws InterruptedException {
        boolean flag = true;
        BlockingQueue<Runnable> queue = pool.getQueue();
        for (int i = 0; i < 1000; i++) {
            if (flag) {
                pool.execute(new Runnable() {

                    public void run() {
                        try {
                            System.out.println("start thread ");
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("thread completed");
                    }
                });
            }
            System.out.print(i + "   active threads: " + pool.getActiveCount());
            System.out.println("     queue: " + queue.size());
            if (queue.size() > 20) flag = false;
            Thread.sleep(1000);
        }
    }

    /**
	 * @param args
	 * @throws InterruptedException 
	 */
    public static void main(String[] args) throws InterruptedException {
        ThreadPoolManualTest test = new ThreadPoolManualTest();
        test.run();
    }
}
