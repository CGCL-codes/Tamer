package sk.bielyvlk.gps;

import java.util.Calendar;
import java.util.Date;
import sk.bielyvlk.bluetooth.BtSpp;
import sk.bielyvlk.bluetooth.BtSppListener;
import sk.bielyvlk.vlkui.Debug;
import sk.bielyvlk.vlkui.SettingsAction;
import sk.bielyvlk.vlkui.VlkText;

public class NmeaGps extends GpsListener implements BtSppListener, SettingsAction {

    public static final int FIXNO = 0;

    public static final int FIX2D = 0;

    public static final int FIX3D = 0;

    public static final int KMH = 10000;

    public static final int MS = 36000;

    public static final int MPH = 16093;

    public static final int KT = 18520;

    public long parseTimeGGA = 0;

    public long parseTimeRMC = 0;

    public long parseTimeGSV = 0;

    public long parseTimeGSA = 0;

    public long parseTimeStart = 0;

    private StringBuffer s = new StringBuffer();

    private String[] data = new String[25];

    private BtSpp gpsDevice = null;

    public String toString() {
        return "NmeaGPS";
    }

    public NmeaGps(BtSpp btSpp) {
        btSpp.init(this);
        this.gpsDevice = btSpp;
    }

    public String btSppDataInput(byte b) {
        if (s.length() > 0) {
            s.append((char) b);
            if (b == '*') {
                parseGpSentence(s.toString());
                s = new StringBuffer();
            }
        } else {
            if (b == '$') {
                s.append((char) b);
            }
        }
        return null;
    }

    public void parseGpSentence(String s) {
        int i = 0;
        int pos = 0;
        int nextPos = 0;
        try {
            while (true) {
                nextPos = s.indexOf(",", pos);
                if (nextPos < 0) {
                    nextPos = s.indexOf("*", pos);
                    if (nextPos < 0) {
                        break;
                    }
                }
                data[i++] = s.substring(pos, nextPos);
                pos = nextPos + 1;
                if (i > 25) {
                    Debug.debug("GPS to big nmea cmd");
                    break;
                }
            }
            if (data[0].startsWith("$GPGGA")) {
                if (data[7].length() > 0) {
                    sat = Integer.parseInt(data[7]);
                }
                if ((data[9].length() > 0) && (data[2].length() > 0) && (data[4].length() > 0)) {
                    coords.setAlt(data[9]);
                    coords.setLat(data[2], data[3]);
                    coords.setLon(data[4], data[5]);
                    coords.setStartup(false);
                }
                parseTimeGGA = System.currentTimeMillis() - parseTimeStart;
            } else if (data[0].startsWith("$GPRMC")) {
                try {
                    int raw = Integer.parseInt(data[1].substring(0, 6));
                    getCalendar().setTime(new Date((long) (raw % 100 + ((raw / 100) % 100) * 60 + (raw / 10000) * 3600) * 1000));
                    raw = Integer.parseInt(data[9]);
                    getCalendar().set(Calendar.YEAR, raw % 100 + 2000);
                    getCalendar().set(Calendar.MONTH, (raw / 100) % 100 - 1);
                    getCalendar().set(Calendar.DAY_OF_MONTH, raw / 10000 + getCalendar().get(Calendar.DAY_OF_MONTH) - 1);
                } catch (Exception e) {
                    Debug.debug("" + e);
                }
                if ((data[7].length() > 0) && (data[8].length() > 0)) {
                    spd = (int) (Double.parseDouble(data[7]) * 18520);
                    crs = (int) (Double.parseDouble(data[8]) * Deg.DEG);
                }
                parseTimeRMC = System.currentTimeMillis() - parseTimeStart;
            } else if (data[0].startsWith("$GPGSV")) {
                satView = parseInt(data[3]);
                if (satView > satelites.length) {
                    satelites = new GpsSat[satView];
                    for (i = 0; i < satelites.length; i++) {
                        satelites[i] = new GpsSat();
                    }
                }
                int tmi = (parseInt(data[2]) - 1) * 4;
                int tmk = 4;
                while (tmk < 19 && tmi++ < satelites.length) {
                    satelites[tmi - 1].set(parseInt(data[tmk++]), parseInt(data[tmk++]), parseInt(data[tmk++]), parseInt(data[tmk++]));
                }
                parseTimeGSV = System.currentTimeMillis() - parseTimeStart;
            } else if (data[0].startsWith("$GPGSA")) {
                fix = parseInt(data[2]);
                int tmi, tmj, tmk;
                for (tmi = 3; tmi < 15; tmi++) {
                    tmk = parseInt(data[tmi]);
                    tmj = 0;
                    while (tmj < satView) {
                        satelites[tmj++].setState(tmk);
                    }
                }
                tmj = 0;
                while (tmj < satView) {
                    satelites[tmj++].moveState();
                }
                hAccuracy = (data[16].length() > 0) ? (0.6 * Double.parseDouble(data[16]) * 10) : -1;
                parseTimeGSA = System.currentTimeMillis() - parseTimeStart;
            }
        } catch (Exception e) {
            Debug.debug(s);
            Debug.debug("" + e);
        }
    }

    public static int parseInt(String data) {
        try {
            return (Integer.parseInt(data));
        } catch (Exception e) {
            return (0);
        }
    }

    public void exit() {
        super.exit();
        if (gpsDevice != null) gpsDevice.exit();
    }

    public static String dateTimeToStr(Calendar cal) {
        return VlkText.intToStr(cal.get(Calendar.YEAR), 4) + "-" + VlkText.intToStr(cal.get(Calendar.MONTH) + 1, 2) + "-" + VlkText.intToStr(cal.get(Calendar.DAY_OF_MONTH), 2) + "T" + VlkText.intToStr(cal.get(Calendar.HOUR_OF_DAY), 2) + ":" + VlkText.intToStr(cal.get(Calendar.MINUTE), 2) + ":" + VlkText.intToStr(cal.get(Calendar.SECOND), 2) + "Z";
    }

    public static String dateTimeToStrShort(Calendar cal) {
        return VlkText.intToStr(cal.get(Calendar.YEAR), 4) + VlkText.intToStr(cal.get(Calendar.MONTH) + 1, 2) + VlkText.intToStr(cal.get(Calendar.DAY_OF_MONTH), 2) + "_" + VlkText.intToStr(cal.get(Calendar.HOUR_OF_DAY), 2) + VlkText.intToStr(cal.get(Calendar.MINUTE), 2);
    }

    public boolean getAutoConnect() {
        return gpsDevice.getAutoConnect();
    }

    public boolean isConnected() {
        return gpsDevice.isConnected();
    }

    public void setAutoConnect(boolean autoConnect) {
        gpsDevice.setAutoConnect(autoConnect);
    }

    public void resetConnection() {
        gpsDevice.resetConnection();
    }

    public void setUrl(String url) {
        gpsDevice.setUrl(url);
    }

    public String getName() {
        return "bluetooth.conf";
    }

    public int getType() {
        return NMEA_BT;
    }

    public void setDefaultSettings() {
    }
}
