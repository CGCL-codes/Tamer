package vtc;

import java.util.concurrent.*;
import java.io.*;

abstract class VtcUnit implements Runnable {

    protected VtcArrayBlockingQueue inQueues_[];

    protected VtcArrayBlockingQueue outQueues_[];

    protected String id_;

    private VtcUnit() {
        ;
    }

    VtcUnit(String id, VtcArrayBlockingQueue inQueues[], VtcArrayBlockingQueue outQueues[]) {
        id_ = id;
        inQueues_ = inQueues;
        outQueues_ = outQueues;
    }

    public abstract void process() throws InterruptedException;

    public synchronized void run() {
        try {
            while (!Thread.interrupted()) {
                this.process();
            }
        } catch (InterruptedException e) {
            System.out.print("VtcUnit threat interrupted");
        }
        System.out.print("Exiting VtcUnit");
        ;
    }
}
