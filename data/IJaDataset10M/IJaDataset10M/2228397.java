package biz.xl.test;

public class MyThread implements Runnable {

    int count = 1, number;

    public MyThread(int num) {
        number = num;
        System.out.println("Create thread " + num);
    }

    @Override
    public void run() {
        while (true) {
            System.out.println("Thread " + number + ": count " + count);
            if (++count == 6) {
                return;
            }
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            new Thread(new MyThread(i + 1)).start();
        }
    }
}
