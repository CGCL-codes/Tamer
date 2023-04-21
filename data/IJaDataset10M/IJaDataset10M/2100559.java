package de.flomain.secdata.program;

public class ShutDownHook extends Thread {

    private Thread thread;

    public ShutDownHook(Thread thread) {
        this.thread = thread;
    }

    @Override
    public void run() {
        thread.interrupt();
    }
}
