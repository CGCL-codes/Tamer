package com.radic;

import ade.ADEGlobals;

/**
The abstract superclass for implementing potential fields.

@author Jim Kramer
*/
public abstract class PotentialField extends FieldPoint {

    private static String prg = "PotentialField";

    private static boolean debug = false;

    /** The default update rate (in milliseconds). */
    public static int DEF_PERIOD = 100;

    /** The {@link com.interfaces.PseudoRefAccess PseudoRefAccess} object
	 * (i.e., a subclass of {@link ade.ADEServer ADEServer} that implements
	 * the <tt>PseudoRefAccess</tt> interface) from which data is obtained. */
    protected PseudoRefAccess adeServer;

    /** The tag that designates a <i>pseudoreference</i> in the {@link
	 * #adeServer} object, through which data is obtained. */
    protected String pRefTag = null;

    /** The update Thread object. */
    private Updater updater;

    /** The force vector update method, which must be implemented by subclass.
	 * It is likely that updating will consist of a Thread that periodically
	 * obtains the sensor readings necessary to recalculate the force vector. */
    protected abstract void update();

    /** The update Thread (inner) class. */
    private class Updater extends Thread {

        int period;

        boolean shouldRun = true;

        boolean shouldUpdate = false;

        /** Constructor. */
        public Updater(int p) {
            if (debug) System.out.println(prg + ": creating updater");
            period = p;
        }

        /** Thread run loop. */
        public void run() {
            if (debug) System.out.println(prg + ": starting updater...");
            while (shouldRun) {
                if (shouldUpdate) {
                    if (debug) System.out.println(prg + ": calling update...");
                    update();
                }
                try {
                    Thread.sleep(period);
                } catch (Exception ignore) {
                }
            }
            if (debug) System.out.println(prg + ": exiting updater...");
        }

        /** Stops the updating, but continues to run. */
        public void stopUpdate() {
            shouldUpdate = false;
        }

        /** Starts the updating. */
        public void startUpdate() {
            shouldUpdate = true;
        }

        /** Stops (and exits) the update thread. */
        public void halt() {
            shouldUpdate = false;
            shouldRun = false;
        }
    }

    public PotentialField() {
    }

    /** Constructor; sets update period to {@link #DEF_PERIOD}.
	 * @param pra A subclass of {@link ade.ADEServer ADEServer} that has
	 *   been cast as a {@link com.interfaces.PseudoRefAccess PseudoRefAccess}
	 *   object */
    public PotentialField(PseudoRefAccess pra) {
        this(pra, DEF_PERIOD);
    }

    /** Constructor; sets update period. */
    public PotentialField(PseudoRefAccess pra, int p) {
        adeServer = pra;
        updater = new Updater(p);
        updater.start();
    }

    /** Set the update period. */
    public void setPeriod(int p) {
        updater.period = p;
    }

    /** Starts updating.
	 * @param tag The tag that designates a <i>pseudoreference</i> in the
	 *   {@link #adeServer} through which data is obtained */
    public void setUpdateOn(String tag) {
        pRefTag = tag;
        updater.startUpdate();
    }

    /** Stops updating. */
    public void setUpdateOff() {
        updater.stopUpdate();
        pRefTag = null;
    }

    /** Exit and cleanup. */
    public void stop() {
        updater.halt();
    }

    /** Concise description. */
    public String toString() {
        StringBuilder sb = new StringBuilder(getClass().getName());
        sb.append(": period=");
        sb.append(updater.period);
        return sb.toString();
    }

    /** Formats an array of doubles for display. */
    protected String locStr(double[] loc) {
        StringBuilder sb = new StringBuilder("(");
        for (double coord : loc) {
            sb.append(ADEGlobals.roundDec(coord, 2));
            sb.append(",");
        }
        sb.setLength(sb.length() - 1);
        sb.append(")");
        return sb.toString();
    }
}
