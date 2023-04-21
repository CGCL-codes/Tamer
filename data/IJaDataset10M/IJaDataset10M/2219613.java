package javax.media.j3d;

class InputDeviceBlockingThread extends Thread {

    private static final int WAIT = 0;

    private static final int NOTIFY = 1;

    private static final int STOP = 2;

    private InputDevice device;

    private volatile boolean running = true;

    private volatile boolean stop = false;

    private boolean waiting = false;

    private boolean ready = false;

    private static int numInstances = 0;

    private int instanceNum = -1;

    InputDeviceBlockingThread(ThreadGroup threadGroup, InputDevice device) {
        super(threadGroup, "");
        setName("J3D-InputDeviceBlockingThread-" + getInstanceNum());
        this.device = device;
    }

    private synchronized int newInstanceNum() {
        return (++numInstances);
    }

    private int getInstanceNum() {
        if (instanceNum == -1) instanceNum = newInstanceNum();
        return instanceNum;
    }

    public void run() {
        while (running) {
            while (!stop) {
                device.pollAndProcessInput();
                Thread.yield();
            }
            runMonitor(WAIT);
        }
    }

    void sleep() {
        stop = true;
    }

    void restart() {
        stop = false;
        runMonitor(NOTIFY);
    }

    void finish() {
        stop = true;
        runMonitor(STOP);
    }

    synchronized void runMonitor(int action) {
        switch(action) {
            case WAIT:
                while (running && !ready) {
                    waiting = true;
                    try {
                        wait();
                    } catch (InterruptedException e) {
                    }
                    waiting = false;
                }
                ready = false;
                break;
            case NOTIFY:
                ready = true;
                if (waiting) {
                    notify();
                }
                break;
            case STOP:
                running = false;
                if (waiting) {
                    notify();
                }
                break;
        }
    }
}
