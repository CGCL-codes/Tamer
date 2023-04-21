package com.yovn.labs;

/**
 * @author yovn
 * 
 */
public class KeyBoardMonitor {

    SystemKBListener _listener;

    private Thread eventT;

    private volatile boolean stopE;

    static {
        System.loadLibrary(KBMonitorConstants.LIBRARY_NAME);
    }

    private KeyBoardMonitor() {
    }

    private static final KeyBoardMonitor instance = new KeyBoardMonitor();

    public static KeyBoardMonitor getInstance() {
        return instance;
    }

    public int enableMonitor() {
        int ret = enableMonitorN();
        if (ret == KBMonitorConstants.KB_HOOKED) {
            eventT = new Thread() {

                public void run() {
                    while (!stopE) {
                        int readE = getNextEvent();
                        if (readE == -1) {
                            System.err.println("event pipe error!!!!");
                            break;
                        }
                        if (readE == 0) {
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                            }
                            continue;
                        }
                        int key = ((readE & 0x00ffff00) >> 8) & 0xffff;
                        boolean down = (readE & 1) == 1 ? true : false;
                        if (_listener != null) {
                            if (down) {
                                _listener.keyDown(key);
                            } else {
                                _listener.keyUp(key);
                            }
                        }
                    }
                }
            };
            stopE = false;
            eventT.start();
        }
        return ret;
    }

    private native int getNextEvent();

    private native int enableMonitorN();

    public void setSystemKBListener(SystemKBListener listener) {
        _listener = listener;
    }

    public int disableMonitor() {
        int ret = disableMonitorN();
        if (ret == KBMonitorConstants.KB_UNHOOK) {
            stopE = true;
            if (eventT != null) {
                eventT.interrupt();
                eventT = null;
            }
        }
        return ret;
    }

    private native int disableMonitorN();

    public static void main(String[] args) throws InterruptedException {
        KeyBoardMonitor kl = KeyBoardMonitor.getInstance();
        MyListener ml = new MyListener();
        kl.setSystemKBListener(ml);
        System.out.println("now enable monitor :" + kl.enableMonitor());
        Object obj = new Object();
        synchronized (obj) {
            obj.wait(60000);
        }
        System.err.println("now disabled monitor:" + kl.disableMonitor());
    }
}
