package com.mindbright.terminal;

public abstract class TerminalInputChaff implements TerminalInputListener, Runnable {

    private Thread chaffThread;

    private volatile boolean chaffActive;

    private volatile long chaffLastKeyTime;

    private int[] lastKeyBuf;

    private int lastKeyROffs;

    private int lastKeyWOffs;

    public void startChaff() {
        if (!chaffActive) {
            chaffActive = true;
            lastKeyBuf = new int[4];
            lastKeyROffs = 0;
            lastKeyWOffs = 0;
            chaffThread = new Thread(this, "SSH2TerminalAdapterImpl.chaff");
            chaffThread.setDaemon(true);
            chaffThread.start();
        }
    }

    public void stopChaff() {
        if (chaffActive) {
            chaffActive = false;
            synchronized (chaffThread) {
                chaffThread.notify();
            }
            chaffThread = null;
        }
    }

    public void typedChar(char c) {
        if (chaffThread == null) {
            sendTypedChar((int) c);
        } else {
            synchronized (this) {
                lastKeyBuf[lastKeyWOffs++] = (int) c;
                lastKeyWOffs &= 0x03;
            }
            dispenseChaff();
        }
    }

    public void run() {
        long now;
        int wait;
        while (chaffActive) {
            try {
                synchronized (chaffThread) {
                    chaffThread.wait();
                }
                wait = (int) (System.currentTimeMillis() ^ (new Object()).hashCode()) & 0x1ff;
                do {
                    int lastKeyChar = -1;
                    synchronized (this) {
                        if (lastKeyWOffs != lastKeyROffs) {
                            lastKeyChar = lastKeyBuf[lastKeyROffs++];
                            lastKeyROffs &= 0x03;
                        }
                    }
                    if (lastKeyChar >= 0) {
                        sendTypedChar(lastKeyChar);
                    } else {
                        sendFakeChar();
                    }
                    Thread.sleep(30);
                    now = System.currentTimeMillis();
                } while (chaffActive && (now - chaffLastKeyTime) < (1500 + wait));
            } catch (InterruptedException e) {
            }
        }
    }

    public void dispenseChaff() {
        if (chaffThread != null) {
            long now = System.currentTimeMillis();
            if ((now - chaffLastKeyTime) > 1000) {
                chaffLastKeyTime = now;
                synchronized (chaffThread) {
                    chaffThread.notify();
                }
            }
        }
    }

    protected abstract void sendTypedChar(int c);

    protected abstract void sendFakeChar();
}
