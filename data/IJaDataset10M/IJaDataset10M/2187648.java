package org.placelab.spotter;

import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import org.placelab.collections.Iterator;
import org.placelab.collections.LinkedList;
import org.placelab.core.BeaconMeasurement;
import org.placelab.core.BeaconReading;
import org.placelab.core.BluetoothReading;
import org.placelab.core.Measurement;

public class ListenerBluetoothGPSSpotter extends BluetoothGPSSpotter implements SpotterListener {

    private static long SMALL_AMOUNT_OF_TIME = 500;

    private long LARGE_AMOUNT_OF_TIME;

    private static long TOO_OLD_TO_CHECK = 20000;

    private LinkedList candidatesToSearch;

    private LinkedList candidatesSearched;

    private class CandidatePackage {

        public long timestamp;

        public BluetoothReading reading;

        public CandidatePackage(long timestamp, BluetoothReading reading) {
            this.timestamp = timestamp;
            this.reading = reading;
        }
    }

    public ListenerBluetoothGPSSpotter(BluetoothSpotter bt) {
        LARGE_AMOUNT_OF_TIME = 30 * 1000;
        bt.addListener(this);
    }

    public void open() {
        super.open();
        candidatesToSearch = new LinkedList();
        candidatesSearched = new LinkedList();
    }

    public void close() {
        super.close();
        candidatesToSearch = null;
        candidatesSearched = null;
    }

    public void gotMeasurement(Spotter sp, Measurement m) {
        if (serviceUrl != null) return;
        boolean doNotify = false;
        BeaconReading[] teeth = ((BeaconMeasurement) m).getReadings();
        synchronized (candidatesToSearch) {
            for (int i = 0; i < teeth.length; i++) {
                if (!(teeth[i] instanceof BluetoothReading)) {
                    continue;
                }
                BluetoothReading tooth = (BluetoothReading) teeth[i];
                if (tooth.getHumanReadableName().toLowerCase().indexOf("gps") >= 0) {
                    boolean found = false;
                    Iterator it = candidatesSearched.iterator();
                    while (it.hasNext()) {
                        CandidatePackage cp = (CandidatePackage) it.next();
                        if (cp.reading.getId().equals(tooth.getId())) {
                            cp.timestamp = m.getTimestamp();
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        found = false;
                        it = candidatesToSearch.iterator();
                        while (it.hasNext()) {
                            CandidatePackage cp = (CandidatePackage) it.next();
                            if (cp.reading.getId().equals(tooth.getId())) {
                                cp.timestamp = m.getTimestamp();
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            CandidatePackage cp = new CandidatePackage(m.getTimestamp(), tooth);
                            candidatesToSearch.add(cp);
                        }
                    }
                }
            }
            if (candidatesToSearch.size() > 0) doNotify = true;
        }
        if (doNotify) {
            synchronized (this) {
                this.notify();
            }
        }
    }

    public void spotterExceptionThrown(Spotter sp, SpotterException se) {
    }

    protected void findGPS() {
        LinkedList notChecked = new LinkedList();
        synchronized (candidatesToSearch) {
            Iterator i = candidatesToSearch.iterator();
            while (i.hasNext()) {
                notChecked.add(i.next());
            }
        }
        Iterator i = notChecked.iterator();
        while (i.hasNext()) {
            CandidatePackage cp = (CandidatePackage) i.next();
            if ((System.currentTimeMillis() - cp.timestamp) > TOO_OLD_TO_CHECK) {
                synchronized (candidatesToSearch) {
                    candidatesToSearch.remove(cp);
                }
                continue;
            } else {
                synchronized (candidatesToSearch) {
                    candidatesToSearch.remove(cp);
                    candidatesSearched.add(cp);
                }
            }
            System.out.println("trying to connect to " + cp.reading.getHumanReadableName());
            String fabUrl = "btspp://" + cp.reading.getId() + ":1" + ";authenticate=false" + ";encrypt=false";
            try {
                state = "Attempting to connect to " + cp.reading.getHumanReadableName() + " " + cp.reading.getId();
                System.out.println("state: " + state);
                conn = (StreamConnection) Connector.open(fabUrl);
                is = conn.openDataInputStream();
            } catch (Exception e) {
                state = "Failed connection attempt to " + cp.reading.getHumanReadableName();
                System.out.println(state);
                serviceUrl = null;
                continue;
            }
            serviceUrl = fabUrl;
            state = "Connected " + cp.reading.getHumanReadableName();
            break;
        }
        if (serviceUrl == null) {
            try {
                state = "Sleeping until suitable devices are found";
                long currentMillis = 0;
                while (currentMillis < LARGE_AMOUNT_OF_TIME) {
                    synchronized (candidatesToSearch) {
                        if (candidatesToSearch.size() > 0) break;
                    }
                    if (scanThread.isDone()) {
                        break;
                    }
                    synchronized (this) {
                        this.wait(SMALL_AMOUNT_OF_TIME);
                    }
                    currentMillis += SMALL_AMOUNT_OF_TIME;
                }
                synchronized (candidatesToSearch) {
                    if (candidatesToSearch.size() == 0) {
                        this.candidatesToSearch.addAll(candidatesSearched);
                        this.candidatesSearched.clear();
                    }
                }
                state = "Trying Again";
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
    }
}
