package webwatcher;

import java.util.*;

/**
 * @author   Sverre H. Huseby
 *           &lt;<A HREF="mailto:shh@thathost.com">shh@thathost.com</A>&gt;
 * @version  $Id: WebPageCollectionUpdater.java 16 2010-08-22 22:14:44Z miraculix0815 $
 */
public final class WebPageCollectionUpdater extends Thread {

    private WebPageCollection collection;

    private long sleepWhen;

    private long sleepTime;

    private long interval;

    private final Vector<WebPage> extraPages = new Vector<WebPage>();

    private boolean finish;

    private long millisLeft() {
        long ret;
        ret = sleepTime - (System.currentTimeMillis() - sleepWhen);
        if (ret < 0L) ret = 0L;
        return ret;
    }

    public WebPageCollectionUpdater(WebPageCollection collection, int first, int rest) {
        this.collection = collection;
        sleepTime = first * 60L * 1000L;
        interval = rest * 60L * 1000L;
        sleepWhen = System.currentTimeMillis();
        finish = false;
    }

    public int minutesLeft() {
        return (int) (millisLeft() / (60L * 1000L));
    }

    public synchronized void refreshPage(WebPage page) {
        extraPages.addElement(page);
        notify();
    }

    public void pleaseStop() {
        finish = true;
    }

    public void run() {
        int q, n;
        WebPage[] wpa;
        while (!finish) {
            while (sleepTime > 0L) {
                sleepWhen = System.currentTimeMillis();
                synchronized (this) {
                    if (extraPages.size() == 0) try {
                        wait(sleepTime);
                    } catch (InterruptedException ex) {
                    }
                }
                if ((n = extraPages.size()) > 0) {
                    synchronized (extraPages) {
                        wpa = new WebPage[extraPages.size()];
                        extraPages.copyInto(wpa);
                        extraPages.removeAllElements();
                    }
                    for (q = 0; q < wpa.length; q++) wpa[q].refresh();
                }
                sleepTime = millisLeft();
            }
            collection.refresh();
            sleepTime = interval;
        }
    }
}
