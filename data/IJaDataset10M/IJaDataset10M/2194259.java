package com.google.code.sapwcrawler.download.cycle;

import java.net.URL;
import java.util.concurrent.atomic.AtomicInteger;
import com.google.code.sapwcrawler.download.procedure.DownloadProc;

public class DownloadCommand implements Runnable {

    private URL url;

    private DownloadProc proc;

    private Object notifyOn;

    private AtomicInteger counter;

    public void setURL(URL v) {
        this.url = v;
    }

    public void setDownloadProc(DownloadProc v) {
        this.proc = v;
    }

    public void setNotifyOn(Object v) {
        this.notifyOn = v;
    }

    public void setCounter(AtomicInteger v) {
        this.counter = v;
    }

    public void run() {
        try {
            proc.download(url);
            if (notifyOn != null) {
                synchronized (notifyOn) {
                    notifyOn.notify();
                }
            }
        } catch (Exception e) {
        } finally {
            if (counter != null) counter.decrementAndGet();
        }
    }
}
