package jmodnews.db.mckoi;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import jmodnews.logging.ExceptionHandler;

/**
 * A ReferenceQueue used to "clean" weak references on cached overviews.
 * 
 * @author Michael Schierl <schierlm@gmx.de>
 */
public class OverviewReferenceReferenceQueue extends ReferenceQueue implements Runnable {

    /**
	 * 
	 */
    public OverviewReferenceReferenceQueue() {
        super();
        new Thread(this, "OverviewReferenceQueue").start();
    }

    public void run() {
        try {
            while (true) {
                WeakReference wr;
                wr = (WeakReference) remove();
                OverviewImpl.removeWeakReference(wr);
            }
        } catch (InterruptedException e) {
            ExceptionHandler.handle(e);
        }
    }
}
