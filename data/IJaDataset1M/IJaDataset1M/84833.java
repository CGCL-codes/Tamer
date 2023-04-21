package akme.mobile.location;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.location.Criteria;
import javax.microedition.location.Location;
import javax.microedition.location.LocationException;
import javax.microedition.location.LocationListener;
import javax.microedition.location.LocationProvider;
import net.rim.device.api.io.SocketConnectionEnhanced;
import net.rim.device.api.system.CDMAInfo;
import net.rim.device.api.system.GPRSInfo;
import net.rim.device.api.system.IDENInfo;
import net.rim.device.api.system.RadioInfo;
import akme.mobile.io.IoUtil;
import akme.mobile.io.XmlReader;
import akme.mobile.io.XmlTokenizer;
import akme.mobile.rim.NetworkUtil;
import akme.mobile.util.Logger;
import akme.mobile.util.NumberUtil;

/**
 * Helper for the J2ME Location API.
 * 
The different types of location fixes are:

Cellsite, which is the least accurate but the fastest location mode, uses cellsite towers that attempt to provide triangulated GPS information. 
Assisted, which is more accurate than the Cellsite fix but not as fast, uses the network in an assisted search. 
Autonomous, which yields the most accurate fix but provides the first fix the slowest, uses the on-board GPS chip.

http://www.blackberry.com/knowledgecenterpublic/livelink.exe/fetch/2000/348583/800332/800703/How_To_-_Define_criteria_to_retrieve_a_GPS_fix.html?nodeid=1123135&vernum=0
For rough but quick Cellsite fixes you need CostAllowed(true), PowerConsumption(LOW), and no horizontal/vertical accuracy requirement.
For Cell Assisted GPS fixes use CostAllowed(true), PowerConsumption(MEDIUM or NO_REQUIREMENT), and no horizontal/vertical accuracy requirement. 
For Autonomous GPS only, require horizontal/vertical accuracy, CostAllowed(false), PowerConsumption(NO_REQUIREMENT).
In practice the best would be cellsite first to get a general idea, then either Assisted or Autonomous GPS thereafter.

There is a hiddeen Google Location API by cellID: http://www.devx.com/wireless/Article/40524/1954?pf=true
 * 
 * @author Keith Mashinter
 */
public class AkmeLocationHelper {

    /** Remember instances to easily clean them later. */
    private static final Hashtable INSTANCE_MAP = new Hashtable();

    protected static Logger log = Logger.open(AkmeLocationHelper.class);

    public static final int DEFAULT_TIMING = -1;

    public static final int ACCURACY_TYPE_CELLULAR = 1;

    public static final int ACCURACY_TYPE_ASSISTED = 2;

    public static final int ACCURACY_TYPE_SATELLITE = 3;

    protected static final String[] PROVIDER_STATE_ARY = { "", "AVAILABLE", "TEMPORARILY_UNAVAILABLE", "OUT_OF_SERVICE" };

    private static final Hashtable CELL_LOOKUP_MAP = new Hashtable();

    static {
        CELL_LOOKUP_MAP.put("opencellid", "key=3fd381d6305f101ff0e4556b71170b6c");
        CELL_LOOKUP_MAP.put("celldb", "username=kmashint&hash=f8ca36c15098baf46a8a3eddcc89a79a");
    }

    private static Criteria getCriteria(int accuracyType, int preferredResponseSecs) {
        Criteria cri = new Criteria();
        if (accuracyType == ACCURACY_TYPE_CELLULAR) {
            cri.setCostAllowed(true);
            cri.setPreferredPowerConsumption(Criteria.POWER_USAGE_LOW);
        } else if (accuracyType == ACCURACY_TYPE_ASSISTED) {
            cri.setCostAllowed(true);
            cri.setPreferredPowerConsumption(Criteria.POWER_USAGE_HIGH);
        } else {
            cri.setCostAllowed(false);
            cri.setPreferredPowerConsumption(Criteria.POWER_USAGE_MEDIUM);
        }
        cri.setPreferredResponseTime(preferredResponseSecs);
        return cri;
    }

    /**
	 * Prepare an internal LocationProvider with the given general accuracy.  
	 */
    public static AkmeLocationHelper open(int accuracyType, int timeoutSecs) {
        return open(getCriteria(accuracyType, timeoutSecs));
    }

    private static AkmeLocationHelper open(Criteria criteria) {
        LocationProvider pro = null;
        try {
            pro = LocationProvider.getInstance(criteria);
        } catch (Exception ex) {
            ;
        }
        AkmeLocationHelper result = new AkmeLocationHelper(pro);
        INSTANCE_MAP.put(result, result);
        return result;
    }

    public static void close(AkmeLocationHelper helper) {
        if (helper != null) {
            helper.stopTracking();
            if (helper.provider != null) {
                helper.provider = null;
            }
            INSTANCE_MAP.remove(helper);
        }
    }

    public static void closeAll() {
        synchronized (INSTANCE_MAP) {
            for (Enumeration en = INSTANCE_MAP.keys(); en.hasMoreElements(); ) {
                close((AkmeLocationHelper) en.nextElement());
            }
        }
    }

    /**
	 * Get a one-shot location.
	 * This doesn't seem to work as a call to LocationProvide.getInstance(...).getLocation(timeout)
	 * so instead try startTrackingOnceOnly that starts a listener and stops after the first reading.
	 */
    public static Location getLocation(int accuracyType, int timeoutSecs) throws LocationException {
        Location loc = null;
        Criteria cri = getCriteria(accuracyType, timeoutSecs);
        LocationProvider pro = LocationProvider.getInstance(cri);
        if (pro.getState() == LocationProvider.AVAILABLE) {
            try {
                loc = pro.getLocation(timeoutSecs);
            } catch (InterruptedException ex) {
                ;
            }
        }
        cri = null;
        pro = null;
        return loc;
    }

    public static Location getLastKnownLocation() {
        return LocationProvider.getLastKnownLocation();
    }

    /** Remember the location provider for later use. */
    private LocationProvider provider;

    /**
	 * Hide factory constructor.
	 */
    private AkmeLocationHelper(LocationProvider pro) {
        this.provider = pro;
    }

    /**
	 * Get the internal LocationProvider. 
	 */
    public LocationProvider getLocationProvider() {
        return provider;
    }

    /**
	 * Start tracking the location with the given timings.
	 * If the onceOnly 
	 * 
	 * @param interval between readings in seconds.
	 * @param timeout waiting for a reading in seconds.
	 * @param maxAge of a useful reading in seconds.
	 */
    public void startTracking(LocationListener listener, int intervalSecs, int timeoutSecs, int maxAgeSecs) {
        if (provider == null) return;
        provider.setLocationListener(listener, intervalSecs, timeoutSecs, maxAgeSecs);
    }

    /**
	 * Start tracking the location with the given timings.
	 */
    public void stopTracking() {
        if (provider == null) return;
        provider.setLocationListener(null, DEFAULT_TIMING, DEFAULT_TIMING, DEFAULT_TIMING);
    }

    /**
	 * Get the provider state.
	 */
    public int getProviderState() {
        return (provider != null) ? provider.getState() : LocationProvider.TEMPORARILY_UNAVAILABLE;
    }

    /**
	 * Get the provider state.
	 */
    public String getProviderStateString() {
        int providerState = (provider != null) ? provider.getState() : LocationProvider.TEMPORARILY_UNAVAILABLE;
        return providerState >= 0 && providerState < PROVIDER_STATE_ARY.length ? PROVIDER_STATE_ARY[providerState] : null;
    }

    /**
	 * Get an int[] of information about the cell network.
	 * For GPRS, this is [state, CellID, LAC, MNC, MCC].  
	 */
    public static int[] getCellNetworkInfo() {
        int[] result = new int[5];
        int netType = RadioInfo.getNetworkType();
        int wafType = RadioInfo.getActiveWAFs();
        if (netType == RadioInfo.NETWORK_GPRS || (wafType & RadioInfo.NETWORK_GPRS) != 0) {
            final GPRSInfo.GPRSCellInfo info = GPRSInfo.getCellInfo();
            final int gstate = GPRSInfo.getGPRSState();
            result[0] = GPRSInfo.getGPRSState();
            result[1] = info.getCellId();
            result[2] = info.getLAC();
            result[3] = info.getMNC();
            result[4] = info.getMCC();
            log.debug("GPRS IMEI " + GPRSInfo.imeiToString(GPRSInfo.getIMEI(), false) + " state " + (gstate == GPRSInfo.GPRS_STATE_READY ? "READY" : gstate == GPRSInfo.GPRS_STATE_IDLE ? "IDLE" : "STANDBY") + " zone " + GPRSInfo.getZoneName());
            log.debug("GPRS cellId " + info.getCellId() + " lac " + info.getLAC() + " mnc " + info.getMNC() + " mcc " + info.getMCC());
        } else if (netType == RadioInfo.NETWORK_CDMA) {
            final CDMAInfo.CDMACellInfo info = CDMAInfo.getCellInfo();
            log.debug("CDMA ESN " + CDMAInfo.getESN() + " CurrentSID " + CDMAInfo.getCurrentSID() + " HomeSID " + CDMAInfo.getHomeSID());
            log.debug("CDMA SystemID " + info.getSID() + " NetworkID " + info.getNID() + " BillingID " + info.getBID());
        } else if (netType == RadioInfo.NETWORK_IDEN) {
            final IDENInfo.IDENCellInfo info = IDENInfo.getCellInfo();
            final int istate = IDENInfo.getSQELevel();
            final String iname = IDENInfo.getHomeNetworkName();
            log.debug("IDEN IMEI " + IDENInfo.imeiToString(IDENInfo.getIMEI()) + " state " + istate + " name " + iname);
            log.debug("IDEN cellId " + info.getCellId());
        } else if (netType == RadioInfo.NETWORK_802_11) {
            log.debug("WLAN ");
        }
        return result;
    }

    /** 
	 * Get the QualifiedCoordinates, or null, from the OpenCellId service with the given timeout in milliseconds. 
	 * Check the returned range, say < 5000 meters, to see that's it's a reasonable response. 
	 */
    public static QualifiedLocation getQualifiedCoordinatesByOpenCellId() {
        return getQualifiedCoordinatesByOpenCellId(false);
    }

    /** 
	 * Get the QualifiedCoordinates, or null, from the OpenCellId service with the given timeout in milliseconds. 
	 * Check the returned range, say < 5000 meters, to see that's it's a reasonable response.
	 * Will optionally add them if missing. 
	 */
    public static QualifiedLocation getQualifiedCoordinatesByOpenCellId(boolean tryGoogleAndAddIfMissing) {
        QualifiedLocation result = null;
        int netType = RadioInfo.getNetworkType();
        int wafType = RadioInfo.getActiveWAFs();
        if (RadioInfo.NETWORK_GPRS == netType || (wafType & RadioInfo.NETWORK_GPRS) != 0) {
            int[] cellIdAry = getCellNetworkInfo();
            String url = "http://www.opencellid.org/cell/get?" + CELL_LOOKUP_MAP.get("opencellid") + "&mcc=" + cellIdAry[4] + "&mnc=" + cellIdAry[3] + "&lac=" + cellIdAry[2] + "&cellid=" + cellIdAry[1];
            String suffix = NetworkUtil.getConnectorSuffix();
            HttpConnection con = null;
            try {
                con = (HttpConnection) NetworkUtil.openWithTimeout(url + suffix, Connector.READ_WRITE, true);
                if (con instanceof SocketConnectionEnhanced && log.isDebugEnabled()) log.debug(con + "implements SocketConnectionEnhanced");
                int httpCode = con.getResponseCode();
                String httpMesg = con.getResponseMessage();
                log.debug("getQualifiedCoordinatesByOpenCellId() " + httpCode + " " + httpMesg);
                if (httpCode == HttpConnection.HTTP_OK) {
                    InputStreamReader ins = null;
                    try {
                        char[] buf = new char[256];
                        StringBuffer sb = new StringBuffer();
                        ins = new InputStreamReader(con.openInputStream());
                        for (int n = 0; (n = ins.read(buf)) != -1; ) sb.append(buf, 0, n);
                        String xml = sb.toString();
                        XmlTokenizer toker = new XmlTokenizer(xml);
                        toker.findTag("rsp");
                        String status = toker.getAttributeValue("stat");
                        if (log.isDebugEnabled()) log.debug("getQualifiedCoordinatesByOpenCellId() rsp " + status);
                        if ("ok".equalsIgnoreCase(status)) {
                            toker.findTag("cell");
                            double lat = NumberUtil.toDoublePrimitive(toker.getAttributeValue("lat"), 0d);
                            double lon = NumberUtil.toDoublePrimitive(toker.getAttributeValue("lon"), 0d);
                            float range = NumberUtil.toFloatPrimitive(toker.getAttributeValue("range"), Float.NaN);
                            if (range >= 50000f) range = Float.NaN;
                            result = new QualifiedLocation(lat, lon, Float.NaN, range, Float.NaN);
                            result.setTimestamp(System.currentTimeMillis());
                        }
                        if (log.isDebugEnabled()) log.debug(xml);
                    } finally {
                        IoUtil.closeQuiet(ins);
                    }
                }
            } catch (IOException ex) {
                log.error(ex);
            } finally {
                IoUtil.closeQuiet(con);
            }
        }
        if (tryGoogleAndAddIfMissing && (result == null || Float.isNaN(result.getHorizontalAccuracy()))) {
            result = getQualifiedCoordinatesByGoogleCellId();
            if (result == null) return result;
            int[] cellIdAry = getCellNetworkInfo();
            String url = "http://www.opencellid.org/measure/add?" + CELL_LOOKUP_MAP.get("opencellid") + "&cellid=" + cellIdAry[1] + "&lac=" + cellIdAry[2] + "&mnc=" + cellIdAry[3] + "&mcc=" + cellIdAry[4] + "&lat=" + result.getLatitude() + "&lon=" + result.getLongitude();
            String suffix = NetworkUtil.getConnectorSuffix();
            HttpConnection con = null;
            try {
                con = (HttpConnection) NetworkUtil.openWithTimeout(url + suffix, Connector.READ_WRITE, true);
                int httpCode = con.getResponseCode();
                String httpMesg = con.getResponseMessage();
                if (log.isDebugEnabled()) log.debug("getQualifiedCoordinatesByOpenCellId(add) " + httpCode + " " + httpMesg);
                if (httpCode == HttpConnection.HTTP_OK) {
                    InputStreamReader ins = null;
                    try {
                        ins = new InputStreamReader(con.openInputStream());
                        XmlReader toker = new XmlReader(ins);
                        toker.findTag("rsp");
                        String status = toker.getAttributeValue("stat");
                        if (log.isDebugEnabled()) log.debug("getQualifiedCoordinatesByOpenCellId(add) rsp " + status);
                    } finally {
                        IoUtil.closeQuiet(ins);
                    }
                }
            } catch (IOException ex) {
                log.error(ex);
            } finally {
                IoUtil.closeQuiet(con);
            }
        }
        return result;
    }

    /** 
	 * Get the QualifiedCoordinates, or null, from the OpenCellId service with the given timeout in milliseconds.
	 * Check the returned range, say < 5000 meters, to see that's it's a reasonable response. 
	 */
    public static QualifiedLocation getQualifiedCoordinatesByGoogleCellId() {
        QualifiedLocation result = null;
        int netType = RadioInfo.getNetworkType();
        int wafType = RadioInfo.getActiveWAFs();
        if (RadioInfo.NETWORK_GPRS != netType && (wafType & RadioInfo.NETWORK_GPRS) == 0) {
            return result;
        }
        int[] cellIdAry = getCellNetworkInfo();
        int cellID = cellIdAry[1];
        int lac = cellIdAry[2];
        String urlStr = "http://www.google.com/glm/mmap";
        String suffix = NetworkUtil.getConnectorSuffix();
        HttpConnection con = null;
        try {
            con = (HttpConnection) NetworkUtil.openWithTimeout(urlStr + suffix, Connector.READ_WRITE, true);
            con.setRequestMethod(HttpConnection.POST);
            OutputStream ous = con.openOutputStream();
            DataOutputStream dos = new DataOutputStream(ous);
            dos.writeShort(21);
            dos.writeLong(0);
            dos.writeUTF("en");
            dos.writeUTF("Android");
            dos.writeUTF("1.0");
            dos.writeUTF("Web");
            dos.writeByte(27);
            dos.writeInt(0);
            dos.writeInt(0);
            dos.writeInt(3);
            dos.writeUTF("");
            dos.writeInt(cellID);
            dos.writeInt(lac);
            dos.writeInt(0);
            dos.writeInt(0);
            dos.writeInt(0);
            dos.writeInt(0);
            dos.flush();
            dos = null;
            int httpCode = con.getResponseCode();
            String httpMesg = con.getResponseMessage();
            if (log.isDebugEnabled()) log.debug("getQualifiedCoordinatesByGoogleCellId() " + httpCode + " " + httpMesg);
            if (httpCode == HttpConnection.HTTP_OK) {
                DataInputStream dis = new DataInputStream(con.openInputStream());
                short short1 = dis.readShort();
                byte byte1 = dis.readByte();
                int code = dis.readInt();
                if (log.isDebugEnabled()) log.debug("Google short1 " + short1 + " byte1 " + byte1 + " code " + code);
                if (code == 0) {
                    double lat = (double) dis.readInt() / 1000000d;
                    double lon = (double) dis.readInt() / 1000000d;
                    int accuracyMeters = dis.readInt();
                    int int2 = dis.readInt();
                    String str = dis.readUTF();
                    if (log.isDebugEnabled()) log.debug("Google lat " + lat + " lon " + lon + " int1 " + accuracyMeters + " int2 " + int2 + " str " + str);
                    result = new QualifiedLocation(lat, lon, Float.NaN, accuracyMeters, Float.NaN);
                    result.setTimestamp(System.currentTimeMillis());
                }
            }
        } catch (IOException ex) {
            log.error(ex);
        } finally {
            IoUtil.closeQuiet(con);
        }
        return result;
    }
}
