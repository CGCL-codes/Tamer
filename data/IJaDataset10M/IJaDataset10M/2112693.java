package ch.epfl.lsr.adhoc.runtime;

public class FrancThread implements FrancThreadInterface, Runnable {

    private FrancThreadInterface peer;

    public FrancThread() {
        this(null);
    }

    public FrancThread(Runnable target) {
        init(target);
    }

    private synchronized void init(Runnable target) {
        Runnable realTarget = (target == null) ? this : target;
        peer = FrancSystem.instance().createPeerThread(realTarget);
    }

    public void start() {
        peer.start();
    }

    public void setDaemon(boolean on) {
        peer.setDaemon(on);
    }

    public static void sleep(long millis) throws java.lang.InterruptedException {
        Thread.sleep(millis);
    }

    public boolean isAlive() {
        return peer.isAlive();
    }

    public void interrupt() {
        peer.interrupt();
    }

    public void run() {
    }
}
