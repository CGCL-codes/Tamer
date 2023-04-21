package concept.misc;

class MyClass implements Runnable {

    static ThreadLocal<String> threadLocalString = new ThreadLocal<String>() {

        protected String initialValue() {
            return Thread.currentThread().getName();
        }
    };

    @Override
    public void run() {
        System.out.println(threadLocalString.get());
    }
}

public class ThreadLocalLab {

    public static void main(String... strings) {
        MyClass myClass = new MyClass();
        Thread t1 = new Thread(myClass, "Thread-A");
        Thread t2 = new Thread(myClass, "Thread-B");
        Thread t3 = new Thread(myClass, "Thread-C");
        t1.start();
        t2.start();
        t3.start();
    }
}
