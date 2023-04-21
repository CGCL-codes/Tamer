package concurrent.latch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RaceLatchDemo {

    private static final int PLAY_AMOUNT = 10;

    public static void main(String[] args) {
        CountDownLatch begin = new CountDownLatch(1);
        CountDownLatch end = new CountDownLatch(PLAY_AMOUNT);
        Player[] plays = new Player[PLAY_AMOUNT];
        for (int i = 0; i < PLAY_AMOUNT; i++) {
            plays[i] = new Player(i + 1, begin, end);
        }
        ExecutorService exe = Executors.newFixedThreadPool(PLAY_AMOUNT);
        for (Player p : plays) {
            exe.execute(p);
        }
        System.out.println("比赛开始");
        begin.countDown();
        try {
            end.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println("比赛结束");
        }
        exe.shutdown();
    }
}

class Player implements Runnable {

    private int id;

    private CountDownLatch begin;

    private CountDownLatch end;

    public Player(int id, CountDownLatch begin, CountDownLatch end) {
        super();
        this.id = id;
        this.begin = begin;
        this.end = end;
    }

    public void run() {
        try {
            begin.await();
            Thread.sleep((long) (Math.random() * 100));
            System.out.println("Play " + id + " has arrived. ");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            end.countDown();
        }
    }
}
