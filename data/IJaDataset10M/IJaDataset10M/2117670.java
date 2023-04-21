package org.ms150hams.trackem.network;

import org.ms150hams.trackem.model.*;
import org.ms150hams.trackem.util.*;
import org.ms150hams.trackem.util.configdb.ConfigDB;
import org.ms150hams.trackem.network.aprs.*;
import org.ms150hams.trackem.network.ax25.*;
import org.ms150hams.trackem.network.ip.*;
import org.ms150hams.trackem.network.opentrac.*;
import org.ms150hams.trackem.network.tnc.*;
import org.ms150hams.trackem.prefs.*;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.*;
import java.util.*;
import java.util.logging.Logger;
import gnu.io.*;
import org.ms150hams.trackem.db.*;

public class TrackemNetwork implements EventHandler, DatabaseClient {

    private static final int PRIMARY = 1;

    private static final int SECONDARY = 2;

    private static final Logger logger = Logger.getLogger("org.ms150hams.trackem.network");

    public static final String BatchIDKey = "batch";

    Station thisStation;

    Neighbor[] neighbors;

    private ExpiringQueue eventsHeardRecently;

    private int mode;

    private OpenTracProtocolHandler tncOTProtocol;

    private APRSMicEDecoder tncAPRSMicE;

    private APRSObjectEncoder tncAPRSObject;

    private SerialPort tncConnection;

    private KISSProtocol tncKiss;

    private AX25Protocol tncAX25;

    private int setID = 1;

    private HashMap eventSets = new HashMap();

    private Set eventsToTransmit = new HashSet();

    private Set eventsForNextTime = new HashSet();

    private DispatchThread transmitThread;

    private RetransmitThread confirmationThread;

    private db database;

    private TrackemNetwork() {
        ExpirationDelegate d = new ExpirationDelegate() {

            public void objectDidExpire(ExpiringQueue queue, Object element, Date expirationDate) {
                queue.remove(element);
            }
        };
        eventsHeardRecently = new ExpiringQueue(d, 300000);
    }

    public TrackemNetwork(Station station, NetworkPrefs prefs, db theDB) throws NetworkSetupFailureException {
        this();
        database = theDB;
        thisStation = station;
        logger.info("Configuring network for station " + thisStation);
        if (prefs.isTncAttached() && prefs.getSerial() != null && prefs.getSerial().getPort() != null) {
            configureSerialPort(prefs.getSerial());
            try {
                configureTNC(prefs.getCallsign(), tncConnection.getInputStream(), tncConnection.getOutputStream());
            } catch (IOException e) {
                throw new NetworkSetupFailureException(e);
            }
        }
        configure();
    }

    private void configure() throws NetworkSetupFailureException {
        configureTopology();
        startDoingStuff();
    }

    private void configureSerialPort(SerialPortConfig serialPort) throws NetworkSetupFailureException {
        logger.info("Configuring TNC Serial Port");
        try {
            tncConnection = (SerialPort) CommPortIdentifier.getPortIdentifier(serialPort.getPort()).open("Trackem", CommPortIdentifier.PORT_SERIAL);
        } catch (PortInUseException e) {
            throw new NetworkSetupFailureException(e);
        } catch (NoSuchPortException e) {
            throw new NetworkSetupFailureException(e);
        }
        try {
            tncConnection.setSerialPortParams(serialPort.getBaud(), serialPort.getDataBits(), serialPort.getStopBits(), serialPort.getParity());
        } catch (UnsupportedCommOperationException e) {
            throw new NetworkSetupFailureException(e);
        }
    }

    private void configureTNC(String callsign, InputStream fromTNC, OutputStream toTNC) {
        logger.info("Configuring TNC");
        tncKiss = new KISSProtocol(fromTNC, toTNC);
        tncAX25 = new AX25Protocol();
        tncAX25.setMyCall(callsign);
        tncKiss.setUpperLayer(tncAX25);
        tncAX25.setLowerLayer(tncKiss);
        tncOTProtocol = new OpenTracProtocolHandler(database);
        tncAPRSMicE = new APRSMicEDecoder();
        tncAPRSObject = new APRSObjectEncoder();
        tncOTProtocol.setLowerLayer(tncAX25);
        tncOTProtocol.setEventHandler(this);
        tncAX25.setUpperLayer(tncOTProtocol, 0x77);
        tncAX25.setUpperLayer(tncAPRSMicE, 0xF0);
        tncAPRSObject.setLowerLayer(tncAX25);
    }

    private void configureTopology() throws NetworkSetupFailureException {
        try {
            logger.info("Configuring RF Topology");
            Statement s = ConfigDB.getStatement();
            s = s.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = s.executeQuery("SELECT * FROM NetworkTopology WHERE stationID='" + thisStation.getIdentifier() + "'");
            rs.last();
            int num = rs.getRow();
            neighbors = new Neighbor[num];
            rs.beforeFirst();
            int pos = 0;
            while (rs.next()) {
                Neighbor n = new Neighbor(rs.getString("neighborCallsign"), Station.stationForIdentifier(rs.getString("neighbor")));
                neighbors[pos++] = n;
                logger.config(thisStation.toString() + "'s Neighbor " + pos + " is " + n.station + " " + n.callsign);
            }
            rs.close();
            s.close();
        } catch (SQLException e) {
            throw new NetworkSetupFailureException(e);
        }
    }

    private void startDoingStuff() {
        logger.info("Configuration Completed.  Running.");
        if (tncKiss != null) {
            tncKiss.startReceive();
            transmitThread = this.new DispatchThread();
            transmitThread.start();
            confirmationThread = this.new RetransmitThread();
            confirmationThread.start();
        }
    }

    public TrackemNetwork(NetworkPrefs prefs, InputStream fromTNC, OutputStream toTNC) throws NetworkSetupFailureException {
        this();
        configureTNC(prefs.getCallsign(), fromTNC, toTNC);
        configureTopology();
        startDoingStuff();
    }

    private class DispatchThread extends Thread {

        public DispatchThread() {
            super(thisStation.toString() + "-Dispatch");
        }

        public void run() {
            int nWaits = 0;
            int maxWaits = 0;
            int maxEvents = 15;
            while (true) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                }
                synchronized (this) {
                    if (eventsToTransmit.size() < maxEvents) {
                        if (nWaits++ < maxWaits) continue;
                        nWaits = 0;
                    } else nWaits = 0;
                    if (eventsToTransmit.size() == 0) {
                        eventsToTransmit.addAll(eventsForNextTime);
                        eventsForNextTime.clear();
                        continue;
                    }
                }
                Collection events = new ArrayList();
                int thisSetID = setID++;
                Integer setInteger = new Integer(thisSetID);
                boolean setIDUsed = false;
                synchronized (this) {
                    events.addAll(eventsToTransmit);
                    for (int i = 0; i < neighbors.length; i++) {
                        if (neighbors[i].mustDigiNextSet) {
                            neighbors[i].setsToDigi.add(setInteger);
                            neighbors[i].isDigiPreviousSet = true;
                            setIDUsed = true;
                        }
                    }
                    eventsToTransmit.clear();
                    eventsToTransmit.addAll(eventsForNextTime);
                    eventsForNextTime.clear();
                }
                Hashtable ht = new Hashtable();
                if (setIDUsed) {
                    ht.put(BatchIDKey, setInteger);
                } else {
                    setID--;
                }
                logger.fine(thisStation.toString() + " transmitting " + events.size() + " events as set " + ht.get(BatchIDKey) + ": " + events);
                Set eventsTransmitted = new HashSet(events);
                if (tncOTProtocol != null) tncOTProtocol.transmitEvents(events, ht);
                eventsTransmitted.removeAll(events);
                if (setIDUsed) {
                    eventSets.put(setInteger, eventsTransmitted);
                }
                synchronized (this) {
                    if (events.size() == 0) {
                        for (int i = 0; i < neighbors.length; i++) {
                            if (neighbors[i].mustDigiNextSet) {
                                neighbors[i].mustDigiNextSet = false;
                            }
                        }
                    } else {
                        eventsToTransmit.addAll(events);
                    }
                }
            }
        }
    }

    private class RetransmitThread extends Thread {

        Random rand = new Random();

        public void run() {
            while (true) {
                long toSleep = (long) (20000 + (rand.nextFloat() * 10000));
                logger.finest(thisStation.toString() + " Waiting " + toSleep + "ms");
                try {
                    Thread.sleep(toSleep);
                } catch (InterruptedException e) {
                }
                synchronized (this) {
                    for (int i = 0; i < neighbors.length; i++) {
                        int nSets = neighbors[i].setsToDigi.size();
                        if (neighbors[i].isDigiPreviousSet) {
                            nSets--;
                            neighbors[i].isDigiPreviousSet = false;
                        }
                        if (nSets > 0) logger.finest(thisStation.toString() + " needs " + neighbors[i].station + " to digi " + nSets + " sets");
                        if (nSets == 0) continue;
                        if (System.currentTimeMillis() - neighbors[i].lastHeard.getTime() > 35000 && neighbors[i].retryCount > 5) {
                            logger.info("Haven't heard from neighbor " + neighbors[i].station + " in 35 seconds and 5 retries.");
                            continue;
                        }
                        int nToPick = (int) Math.ceil(Math.log(nSets) / Math.log(2));
                        if (nToPick <= 0) nToPick = 1;
                        int n = 0;
                        Iterator it = neighbors[i].setsToDigi.iterator();
                        while (it.hasNext() && n++ < nToPick) {
                            Integer pick = (Integer) it.next();
                            DigipeatConfirmationRequest query = new DigipeatConfirmationRequest(thisStation, new Date(), neighbors[i].station, pick.intValue());
                            eventsToTransmit.add(query);
                            logger.finer(thisStation.toString() + " Requesting confirmation from " + neighbors[i].station + " of " + pick.intValue() + " with " + query);
                        }
                        neighbors[i].retryCount++;
                    }
                }
            }
        }
    }

    public void databaseReceivedEvent(Event evt, Database db) {
        logger.finer(thisStation.toString() + " Originating event " + evt);
        if (mode == PRIMARY) {
            synchronized (this) {
                for (int i = 0; i < neighbors.length; i++) {
                    logger.finer(thisStation.toString() + " expecting " + neighbors[i].station + " (" + i + ") to digi " + evt);
                    neighbors[i].needsDigi();
                }
                eventsToTransmit.add(evt);
                eventsHeardRecently.add(evt);
                ArrayList list = new ArrayList();
                list.add(evt);
                Hashtable protoInfo = new Hashtable();
                tncOTProtocol.transmitEvents(list, protoInfo);
            }
        } else {
            ArrayList list = new ArrayList();
            list.add(evt);
            tncOTProtocol.transmitEvents(list, new Hashtable());
        }
    }

    public void receiveEvent(Event evt, Hashtable protoInfo) {
        if (mode == SECONDARY) {
            logger.warning("Secondary mode client received event.  Dropping.");
            return;
        }
        Neighbor n = null;
        String theCall = (String) protoInfo.get(AX25Protocol.SourceCallsignKey);
        if (theCall != null) {
            for (int i = 0; i < neighbors.length; i++) {
                if (theCall.equals(neighbors[i].callsign)) {
                    n = neighbors[i];
                    n.lastHeard = new Date();
                    break;
                }
            }
        }
        if (evt instanceof DigipeatConfirmation) {
            DigipeatConfirmation dc = (DigipeatConfirmation) evt;
            if (!dc.getOriginatingStation().equals(thisStation)) return;
            int setID = dc.getSetID();
            n.retryCount = 0;
            logger.finer(thisStation.toString() + " got confirmation from " + n.station + " of set " + setID);
            n.setsToDigi.remove(new Integer(setID));
            return;
        } else if (evt instanceof DigipeatFailure) {
            if (!((DigipeatFailure) evt).getOriginatingStation().equals(thisStation)) return;
            n.retryCount = 0;
            int setID = ((DigipeatFailure) evt).getSetID();
            logger.info(thisStation.toString() + " got digipeat failure from " + n.station + " of set " + setID);
            synchronized (this) {
                n.setsToDigi.remove(new Integer(setID));
                n.needsDigi();
                logger.finer("Set " + setID + " consists of " + eventSets.get(new Integer(((DigipeatFailure) evt).getSetID())) + " " + eventSets);
                eventsToTransmit.addAll((Collection) eventSets.get(new Integer(((DigipeatFailure) evt).getSetID())));
            }
            return;
        } else if (evt instanceof DigipeatConfirmationRequest) {
            if (!((DigipeatConfirmationRequest) evt).getDigipeatingStation().equals(thisStation)) return;
            Integer bat = new Integer(((DigipeatConfirmationRequest) evt).getSetID());
            Event e = null;
            if (n.digiSets.get(bat) != null) {
                e = new DigipeatConfirmation(thisStation, new Date(), n.station, bat.intValue());
            } else {
                e = new DigipeatFailure(thisStation, new Date(), n.station, bat.intValue());
            }
            logger.info(thisStation.toString() + " replying to " + evt + " with " + e);
            synchronized (this) {
                eventsForNextTime.add(e);
            }
            return;
        }
        if (!eventsHeardRecently.contains(evt)) {
            logger.fine(thisStation.toString() + " Received event " + evt + " from network");
            Integer batch = (Integer) protoInfo.get(BatchIDKey);
            Event forNextTime = null;
            if (batch != null && n != null) {
                int b = batch.intValue();
                boolean existed = n.addEventToDigiSet(b, evt);
                if (!existed) {
                    Event e = new DigipeatConfirmation(thisStation, new Date(), n.station, b);
                    logger.finer(thisStation.toString() + " generating event " + e);
                    forNextTime = e;
                }
            }
            if (theCall != null) {
                synchronized (this) {
                    boolean someoneNeedsToDigi = false;
                    for (int i = 0; i < neighbors.length; i++) {
                        String origCall = (String) protoInfo.get(AX25Protocol.SourceCallsignKey);
                        if (!neighbors[i].station.equals(evt.getStation()) && !origCall.equals(neighbors[i].callsign)) {
                            logger.finer(thisStation.toString() + " expecting " + neighbors[i].station + " to digi " + evt);
                            neighbors[i].needsDigi();
                            someoneNeedsToDigi = true;
                        }
                    }
                    if (someoneNeedsToDigi) {
                        eventsToTransmit.add(evt);
                    }
                    if (forNextTime != null) eventsToTransmit.add(forNextTime);
                }
            }
            eventsHeardRecently.add(evt);
            if (database != null) {
                database.eventReceived(evt, this);
            }
        }
    }
}

class Neighbor {

    public String callsign;

    public Station station;

    public boolean isDead = false;

    public boolean mustDigiNextSet = false;

    public boolean isDigiPreviousSet = false;

    public Collection setsToDigi = new LinkedList();

    public Hashtable digiSets = new Hashtable();

    public int retryCount = 0;

    public Date lastHeard = new Date();

    public Date lastDigi = new Date();

    public Neighbor(String call, Station stn) {
        callsign = call;
        station = stn;
    }

    public synchronized void needsDigi() {
        mustDigiNextSet = true;
    }

    public synchronized boolean addEventToDigiSet(int set, Event evt) {
        boolean ret = true;
        Set s = (Set) digiSets.get(new Integer(set));
        if (s == null) {
            s = new HashSet();
            digiSets.put(new Integer(set), s);
            ret = false;
        }
        s.add(evt);
        return ret;
    }
}
