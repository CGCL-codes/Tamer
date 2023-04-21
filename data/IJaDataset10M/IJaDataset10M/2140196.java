package de.fhg.igd.gps;

import java.io.Reader;
import java.util.LinkedList;
import de.fhg.igd.event.TrustedListener;
import de.fhg.igd.util.Listener;
import de.fhg.igd.util.WhatIs;

/**
 * @author Jan Peters <jpeters@igd.fhg.de>
 */
public interface GPSService {

    /**
     * The {@link WhatIs} entry key for this service.
     */
    public static final String WHATIS = "GPS_SERVICE";

    public TrustedListener addListener(Listener listener);

    public GPSData getCurrentGpsData();

    public void setHistorySize(int size);

    public int getHistorySize();

    public void setHistoryStepDistance(long distance);

    public void setHistoryStepTime(long time);

    public void resetHistory();

    public LinkedList history();

    public void setDefaultGpsData(GPSData data);

    public void setDefaultWGS84(String str);

    public void initGpsParser(Reader reader);

    public void initGpsParser(String file);

    public void startGpsParser();

    public void stopGpsParser();

    public void updateGpsData(GPSData data);
}
