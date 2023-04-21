package de.debeka;

public class ThreadTest {

    public static void main(String[] args) {
        Thread t1, t2, t3;
        MyThread hund = new MyThread("Wau");
        MyThread katze = new MyThread("Miau");
        MyThread maus = new MyThread("Piep");
        t1 = new Thread(hund);
        t2 = new Thread(katze);
        t3 = new Thread(maus);
        t1.start();
        t2.start();
        t3.start();
    }
}
