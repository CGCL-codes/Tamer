package se.sics.cooja.dialogs;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Timer;
import se.sics.cooja.plugins.LogListener;

/**
 * Help class to avoid EventQueue flooding by aggregating several update events.
 * 
 * To be used by plugins et. al. that receive updates at a high rate 
 * (such as new Log Output messages), and must handle them from the Event thread.
 * 
 * @author Fredrik Osterlind, Niclas Finne
 *
 * @param <A> Event whose update event are aggregated
 * @see LogListener
 */
public abstract class UpdateAggregator<A> {

    private static final int DEFAULT_MAX_PENDING = 256;

    private int maxPending;

    private ArrayList<A> pending;

    private Timer t;

    /**
   * @param interval Max interval (ms)
   */
    public UpdateAggregator(int interval) {
        this(interval, DEFAULT_MAX_PENDING);
    }

    /**
   * @param delay Max interval (ms)
   * @param maxEvents Max pending events (default 256)
   */
    public UpdateAggregator(int interval, int maxEvents) {
        this.maxPending = maxEvents;
        pending = new ArrayList<A>();
        t = new Timer(interval, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                consume.run();
            }
        });
        t.setInitialDelay(0);
        t.setCoalesce(true);
        t.setRepeats(true);
    }

    /**
   * Consumer: called from event queue
   */
    private Runnable consume = new Runnable() {

        public void run() {
            if (pending.isEmpty()) {
                return;
            }
            ;
            List<A> q = getPending();
            if (q != null) {
                handle(q);
            }
            synchronized (UpdateAggregator.this) {
                UpdateAggregator.this.notifyAll();
            }
        }
    };

    /**
   * @param l All events since last update
   */
    protected abstract void handle(List<A> l);

    private synchronized List<A> getPending() {
        ArrayList<A> tmp = pending;
        pending = new ArrayList<A>();
        return tmp;
    }

    /**
   * @param a Add new event (any thread). May block.
   */
    public synchronized void add(A a) {
        try {
            while (pending.size() > maxPending) {
                EventQueue.invokeLater(consume);
                wait(t.getDelay());
            }
        } catch (InterruptedException e) {
        }
        pending.add(a);
    }

    public void start() {
        t.start();
    }

    public void stop() {
        t.stop();
    }
}
