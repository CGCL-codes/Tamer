package example.thread;

public class ThreadLocalExample {

    private static ThreadLocal tlinstance = new ThreadLocal();

    public static synchronized void setThreadInstance(Object me) {
        tlinstance.set(me);
    }

    public static synchronized ThreadLocalExample getThreadInstance() {
        Object o = tlinstance.get();
        if (o == null) {
            o = new ThreadLocalExample();
            tlinstance.set(o);
        }
        return (ThreadLocalExample) o;
    }
}
