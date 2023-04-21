package jmri.jmrix.rps;

import java.util.Vector;

/**
 * Distributes Readings and the Measurements calculated from them.
 * <P>
 * @author	Bob Jacobsen  Copyright (C) 2006, 2008
 *
 * @version	$Revision: 1.6 $
 */
public class Distributor {

    /**
     * Request being informed when a new Reading 
     * is available.
     */
    public void addReadingListener(ReadingListener l) {
        if (!readingListeners.contains(l)) {
            readingListeners.addElement(l);
        }
    }

    /**
     * Request to no longer be informed when new Readings arrive.
     */
    public void removeReadingListener(ReadingListener l) {
        if (readingListeners.contains(l)) {
            readingListeners.removeElement(l);
        }
    }

    /**
     * Invoked when a new Reading is created
     */
    @SuppressWarnings("unchecked")
    public void submitReading(Reading s) {
        Vector<ReadingListener> v;
        synchronized (this) {
            v = (Vector<ReadingListener>) readingListeners.clone();
        }
        if (log.isDebugEnabled()) log.debug("notify " + v.size() + " ReadingListeners about item ");
        int cnt = v.size();
        for (int i = 0; i < cnt; i++) {
            ReadingListener client = v.elementAt(i);
            javax.swing.SwingUtilities.invokeLater(new ForwardReading(s, client));
        }
    }

    /**
     * Request being informed when a new Measurement 
     * is available.
     */
    public void addMeasurementListener(MeasurementListener l) {
        if (!measurementListeners.contains(l)) {
            measurementListeners.addElement(l);
        }
    }

    /**
     * Request to no longer be informed when new Measurements arrive.
     */
    public void removeMeasurementListener(MeasurementListener l) {
        if (measurementListeners.contains(l)) {
            measurementListeners.removeElement(l);
        }
    }

    /**
     * Invoked when a new Measurement is created
     */
    @SuppressWarnings("unchecked")
    public void submitMeasurement(Measurement s) {
        Vector<MeasurementListener> v;
        synchronized (this) {
            v = (Vector<MeasurementListener>) measurementListeners.clone();
        }
        if (log.isDebugEnabled()) log.debug("notify " + v.size() + " MeasurementListeners about item ");
        int cnt = v.size();
        for (int i = 0; i < cnt; i++) {
            MeasurementListener client = v.elementAt(i);
            javax.swing.SwingUtilities.invokeLater(new ForwardMeasurement(s, client));
        }
    }

    private static volatile Distributor instance = null;

    public static Distributor instance() {
        if (instance == null) instance = new Distributor();
        return instance;
    }

    private final Vector<ReadingListener> readingListeners = new Vector<ReadingListener>();

    private final Vector<MeasurementListener> measurementListeners = new Vector<MeasurementListener>();

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(Distributor.class.getName());

    /**
     * Forward the Reading from the Swing thread
     */
    static class ForwardReading implements Runnable {

        Reading s;

        ReadingListener client;

        ForwardReading(Reading s, ReadingListener client) {
            this.s = s;
            this.client = client;
        }

        public void run() {
            client.notify(s);
        }
    }

    /**
     * Forward the Measurement from the Swing thread
     */
    static class ForwardMeasurement implements Runnable {

        Measurement s;

        MeasurementListener client;

        ForwardMeasurement(Measurement s, MeasurementListener client) {
            this.s = s;
            this.client = client;
        }

        public void run() {
            client.notify(s);
        }
    }
}
