package org.tigr.seq.display.prefs;

import java.io.*;
import java.util.*;

/**
 *
 * This class represents the non-color preferences for an object that
 * displays basecalls.
 *
 * <p>
 * Copyright &copy; 2001 The Institute for Genomic Research (TIGR).
 * <p>
 * All rights reserved.
 * 
 * <pre>
 * $RCSfile: BasecallDisplayerPreferences.java,v $
 * $Revision: 1.11 $
 * $Date: 2003/09/29 17:54:18 $
 * $Author: mcovarr $
 * </pre>
 * 
 *
 * @author Miguel Covarrubias
 * @version 1.0 */
public class BasecallDisplayerPreferences implements IBasecallDisplayerPreferences, Serializable {

    /**
     * Describe variable <code>horizontalMag</code> here.
     *
     *
     */
    private double horizontalMag = 1.0;

    /**
     * Describe variable <code>positionsPeriod</code> here.
     *
     *
     */
    private int positionsPeriod = 10;

    /**
     * Describe variable <code>overviewHoriziontalMagnification</code> here.
     *
     *
     */
    private double overviewHoriziontalMagnification = 1 / 75.0;

    private int overviewPositionsPeriod = 500;

    private boolean globalNumbering = false;

    /**
     * Describe constant <code>serialVersionUID</code> here.
     *
     *
     */
    private static final long serialVersionUID = 0;

    /**
     * This has to be transient or it will get serialized.  Believe me,
     * we don't we that. */
    private transient List<IBasecallDisplayerPreferencesChangeListener> listeners = new ArrayList<IBasecallDisplayerPreferencesChangeListener>();

    /**
     * Default constructor, should come up with some sensible default
     * values.  */
    public BasecallDisplayerPreferences() {
    }

    /**
     * Describe <code>addBasecallDisplayerPreferencesChangeListener</code> method here.
     *
     *
     * @param listener an <code>IBasecallDisplayerPreferencesChangeListener</code> value
     * 
     */
    public void addBasecallDisplayerPreferencesChangeListener(IBasecallDisplayerPreferencesChangeListener listener) {
        this.listeners.add(listener);
    }

    /**
     * Describe <code>removeBasecallDisplayerPreferencesChangeListener</code> method here.
     *
     *
     * @param listener an <code>IBasecallDisplayerPreferencesChangeListener</code> value
     * 
     */
    public void removeBasecallDisplayerPreferencesChangeListener(IBasecallDisplayerPreferencesChangeListener listener) {
        this.listeners.add(listener);
    }

    /**
     * Describe <code>getHorizontalMagnification</code> method here.
     *
     *
     * @return a <code>double</code> value
     *
     */
    public double getHorizontalMagnification() {
        return this.horizontalMag;
    }

    /**
     * Describe <code>setHorizontalMagnification</code> method here.
     *
     *
     * @param mag a <code>double</code> value
     * 
     */
    public void setHorizontalMagnification(double mag) {
        double oldmag = this.horizontalMag;
        this.horizontalMag = mag;
        IBasecallDisplayerPreferencesChangeListener listener;
        Iterator iter = this.listeners.iterator();
        while (iter.hasNext()) {
            listener = (IBasecallDisplayerPreferencesChangeListener) iter.next();
            listener.horizontalMagnificationChange(this.horizontalMag, oldmag);
        }
    }

    /**
     * Describe <code>getPositionsPeriod</code> method here.
     *
     *
     * @return an <code>int</code> value
     *
     */
    public int getPositionsPeriod() {
        return this.positionsPeriod;
    }

    /**
     * Describe <code>setPositionsPeriod</code> method here.
     *
     *
     * @param newPeriod an <code>int</code> value
     * 
     */
    public void setPositionsPeriod(int newPeriod) {
        int oldPeriod = this.positionsPeriod;
        this.positionsPeriod = newPeriod;
        IBasecallDisplayerPreferencesChangeListener listener;
        Iterator iter = this.listeners.iterator();
        while (iter.hasNext()) {
            listener = (IBasecallDisplayerPreferencesChangeListener) iter.next();
            listener.positionsPeriodChange(newPeriod, oldPeriod);
        }
    }

    public double getOverviewHorizontalMagnification() {
        return this.overviewHoriziontalMagnification;
    }

    public void setOverviewHorizontalMagnification(double pNewMag) {
        double oldMag = this.overviewHoriziontalMagnification;
        this.overviewHoriziontalMagnification = pNewMag;
        IBasecallDisplayerPreferencesChangeListener listener;
        Iterator iter = this.listeners.iterator();
        while (iter.hasNext()) {
            listener = (IBasecallDisplayerPreferencesChangeListener) iter.next();
            listener.overviewHorizontalMagnificationChange(pNewMag, oldMag);
        }
    }

    /**
     * Describe <code>getOverviewPositionsPeriod</code> method here.
     *
     *
     * @return an <code>int</code> value
     *
     */
    public int getOverviewPositionsPeriod() {
        return this.overviewPositionsPeriod;
    }

    /**
     * Describe <code>setOverviewPositionsPeriod</code> method here.
     *
     *
     * @param newPeriod an <code>int</code> value
     * 
     */
    public void setOverviewPositionsPeriod(int pNewPeriod) {
        int oldPeriod = this.overviewPositionsPeriod;
        this.overviewPositionsPeriod = pNewPeriod;
        IBasecallDisplayerPreferencesChangeListener listener;
        Iterator iter = this.listeners.iterator();
        while (iter.hasNext()) {
            listener = (IBasecallDisplayerPreferencesChangeListener) iter.next();
            listener.overviewPositionsPeriodChange(pNewPeriod, oldPeriod);
        }
    }

    public boolean isGlobalNumbering() {
        return this.globalNumbering;
    }

    public void setIsGlobalNumbering(boolean pGlobal) {
        boolean oldNumbering = this.globalNumbering;
        if (oldNumbering != pGlobal) {
            this.globalNumbering = pGlobal;
            IBasecallDisplayerPreferencesChangeListener listener;
            Iterator iter = this.listeners.iterator();
            while (iter.hasNext()) {
                listener = (IBasecallDisplayerPreferencesChangeListener) iter.next();
                listener.globalNumberingChange(this.globalNumbering, oldNumbering);
            }
        }
    }
}
