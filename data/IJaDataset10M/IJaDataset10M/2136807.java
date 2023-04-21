package javabook.ch09;

class ThreadTest extends Thread {

    public void run() {
        try {
            for (int i = 0; i < 10; i++) {
                Thread.sleep(200);
                System.out.println("�˱⽬�� �ڹ� �ؼ�: " + i);
            }
        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }
}

class ThreadfromThread {

    public static void main(String args[]) {
        ThreadTest t = new ThreadTest();
        t.start();
    }
}
