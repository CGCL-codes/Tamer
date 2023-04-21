package Cosmo.lockTest;

import Cosmo.locks.ReadWriteLock;
import Cosmo.util.Constants;

public class ReaderThread extends Thread {

    private int readerNumber;

    private ReadWriteLock lock;

    public ReaderThread(int readNum, ReadWriteLock loc) {
        readerNumber = readNum;
        lock = loc;
        this.setPriority((readerNumber * 2) + 1);
        run();
    }

    private void outputMessage() {
        Constants.iLog.LogInfoLine("Reader" + readerNumber + "trying to aquire lock");
        Constants.iLog.LogInfoLine(this.toString());
        lock.acquireRead();
        Constants.iLog.LogInfoLine("Reader" + readerNumber + "reading data");
        lock.releaseRead();
        Constants.iLog.LogInfoLine("Reader" + readerNumber + "released lock");
    }

    public void run() {
        try {
            for (int i = 0; i < 10; ++i) {
                Thread.sleep(1000);
                Thread.yield();
                outputMessage();
            }
        } catch (InterruptedException ie) {
        }
    }
}
