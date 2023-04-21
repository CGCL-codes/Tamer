package de.ueller.gps.data;

import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordStore;
import de.ueller.midlet.gps.Logger;

public class Configuration {

    private static Logger logger;

    public static final int VERSION = 1;

    public static final byte CFGBIT_BACKLIGHT_ON = 1;

    public static final byte CFGBIT_BACKLIGHT_MIDP2 = 2;

    public static final byte CFGBIT_BACKLIGHT_NOKIA = 3;

    public static final byte CFGBIT_BACKLIGHT_NOKIAFLASH = 4;

    public static final byte CFGBIT_GPS_AUTORECONNECT = 5;

    public static final byte CFGBIT_BACKLIGHT_SIEMENS = 6;

    public static final byte CFGBIT_SKIPP_SPLASHSCREEN = 7;

    public static final byte CFGBIT_DEFAULTVALUESAPPLIED = 8;

    /**
	 * These are the database record ids for each configuration option	 * 
	 */
    private static final int RECORD_ID_BT_URL = 1;

    private static final int RECORD_ID_CFGBITS = 3;

    private static final int RECORD_ID_LOG_DEBUG_URL = 1;

    private static final int RECORD_ID_LOG_DEBUG_ENABLE = 2;

    private static final int RECORD_ID_LOG_DEBUG_SEVERITY = 3;

    private static final int RECORD_ID_BT_KEEPALIVE = 4;

    private static final int RECORD_ID_GPS_RECONNECT = 5;

    private static final int RECORD_ID_CONFIG_VERSION = 6;

    private String btUrl;

    /** This URL is used to store logs of raw data received from the GPS receiver*/
    private String rawDebugLogUrl;

    private boolean rawDebugLogEnable;

    private boolean rawGpsLogEnable;

    private long cfgBits = 0;

    private long cfgBitsDefault = 0;

    private int debugSeverity;

    private boolean btKeepAlive = false;

    private boolean btAutoRecon = false;

    private byte[] empty = new byte[0];

    public Configuration() {
        logger = Logger.getInstance(Configuration.class, Logger.DEBUG);
        read();
    }

    private void read() {
        RecordStore database;
        try {
            database = RecordStore.openRecordStore("Receiver", true);
            if (database == null) {
                logger.info("No database loaded at the moment");
                return;
            }
            logger.debug("Config database opened");
            cfgBits = readLong(database, RECORD_ID_CFGBITS);
            if (!getCfgBitState(CFGBIT_DEFAULTVALUESAPPLIED)) {
                cfgBits = 1L << CFGBIT_DEFAULTVALUESAPPLIED | getDefaultDeviceBacklightMethodMask();
            }
            int configVersionStored = readInt(database, RECORD_ID_CONFIG_VERSION);
            if (configVersionStored < 1) {
                cfgBits |= CFGBIT_BACKLIGHT_ON;
                setBtAutoRecon(true);
                logger.info("Default config for version 1+ set.");
            }
            setCfgBits(cfgBits, true);
            write(VERSION, RECORD_ID_CONFIG_VERSION);
            btUrl = readString(database, RECORD_ID_BT_URL);
            rawDebugLogUrl = readString(database, RECORD_ID_LOG_DEBUG_URL);
            rawDebugLogEnable = readInt(database, RECORD_ID_LOG_DEBUG_ENABLE) != 0;
            debugSeverity = readInt(database, RECORD_ID_LOG_DEBUG_SEVERITY);
            btKeepAlive = readInt(database, RECORD_ID_BT_KEEPALIVE) != 0;
            btAutoRecon = readInt(database, RECORD_ID_GPS_RECONNECT) != 0;
            database.closeRecordStore();
        } catch (Exception e) {
            logger.exception("Problems with reading our configuration: ", e);
        }
    }

    private void write(String s, int idx) {
        RecordStore database;
        try {
            database = RecordStore.openRecordStore("Receiver", true);
            byte[] data;
            if (s == null) {
                data = "!null!".getBytes();
            } else {
                data = s.getBytes();
            }
            while (database.getNumRecords() < idx) {
                database.addRecord(empty, 0, empty.length);
            }
            database.setRecord(idx, data, 0, data.length);
            database.closeRecordStore();
            logger.info("wrote " + s + " to " + idx);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void write(int i, int idx) {
        write("" + i, idx);
    }

    private void write(long i, int idx) {
        write("" + i, idx);
    }

    public String readString(RecordStore database, int idx) {
        try {
            String ret;
            byte[] data;
            try {
                data = database.getRecord(idx);
            } catch (InvalidRecordIDException irie) {
                return null;
            }
            if (data == null) {
                ret = null;
            } else {
                ret = new String(data);
                if (ret.equalsIgnoreCase("!null!")) ret = null;
            }
            logger.info("Read from config database " + idx + ": " + ret);
            return ret;
        } catch (Exception e) {
            logger.exception("Failed to read string from config database", e);
            return null;
        }
    }

    public int readInt(RecordStore database, int idx) {
        try {
            String tmp = readString(database, idx);
            logger.info("Read from config database " + idx + ": " + tmp);
            if (tmp == null) {
                return 0;
            } else {
                return Integer.parseInt(tmp);
            }
        } catch (Exception e) {
            logger.exception("Failed to read int from config database", e);
            return 0;
        }
    }

    public long readLong(RecordStore database, int idx) {
        try {
            String tmp = readString(database, idx);
            logger.info("Read from config database " + idx + ": " + tmp);
            if (tmp == null) {
                return 0;
            } else {
                return Long.parseLong(tmp);
            }
        } catch (Exception e) {
            logger.exception("Failed to read Long from config database", e);
            return 0;
        }
    }

    public boolean getDebugRawLoggerEnable() {
        return rawDebugLogEnable;
    }

    public String getDebugRawLoggerUrl() {
        return rawDebugLogUrl;
    }

    public void setDebugRawLoggerUrl(String url) {
        rawDebugLogUrl = url;
        write(rawDebugLogUrl, RECORD_ID_LOG_DEBUG_URL);
    }

    public void setDebugSeverityInfo(boolean enabled) {
        if (enabled) {
            debugSeverity |= 0x01;
        } else {
            debugSeverity &= ~0x01;
        }
        write(debugSeverity, RECORD_ID_LOG_DEBUG_SEVERITY);
    }

    public boolean getDebugSeverityInfo() {
        return ((debugSeverity & 0x01) > 0);
    }

    public void setDebugSeverityDebug(boolean enabled) {
        if (enabled) {
            debugSeverity |= 0x02;
        } else {
            debugSeverity &= ~0x02;
        }
        write(debugSeverity, RECORD_ID_LOG_DEBUG_SEVERITY);
    }

    public boolean getDebugSeverityDebug() {
        return ((debugSeverity & 0x02) > 0);
    }

    public void setDebugSeverityTrace(boolean enabled) {
        if (enabled) {
            debugSeverity |= 0x04;
        } else {
            debugSeverity &= ~0x04;
        }
        write(debugSeverity, RECORD_ID_LOG_DEBUG_SEVERITY);
    }

    public boolean getDebugSeverityTrace() {
        return ((debugSeverity & 0x04) > 0);
    }

    public void setDebugRawLoggerEnable(boolean enabled) {
        rawDebugLogEnable = enabled;
        if (rawDebugLogEnable) write(1, RECORD_ID_LOG_DEBUG_ENABLE); else write(0, RECORD_ID_LOG_DEBUG_ENABLE);
    }

    public boolean getGpsRawLoggerEnable() {
        return rawGpsLogEnable;
    }

    public String getBtUrl() {
        return btUrl;
    }

    public void setBtUrl(String btUrl) {
        this.btUrl = btUrl;
        write(btUrl, 1);
    }

    public boolean getBtKeepAlive() {
        return btKeepAlive;
    }

    public void setBtKeepAlive(boolean keepAlive) {
        write(keepAlive ? 1 : 0, RECORD_ID_BT_KEEPALIVE);
        this.btKeepAlive = keepAlive;
    }

    public boolean getBtAutoRecon() {
        return btAutoRecon;
    }

    public void setBtAutoRecon(boolean autoRecon) {
        write(autoRecon ? 1 : 0, RECORD_ID_GPS_RECONNECT);
        this.btAutoRecon = autoRecon;
    }

    public boolean getCfgBitState(byte bit, boolean getDefault) {
        if (getDefault) {
            return ((this.cfgBitsDefault & (1L << bit)) != 0);
        } else {
            return ((this.cfgBits & (1L << bit)) != 0);
        }
    }

    public boolean getCfgBitState(byte bit) {
        return getCfgBitState(bit, false);
    }

    public void setCfgBitState(byte bit, boolean state, boolean setAsDefault) {
        this.cfgBits |= (1L << bit);
        if (!state) {
            this.cfgBits ^= (1L << bit);
        }
        if (setAsDefault) {
            this.cfgBitsDefault |= (1L << bit);
            if (!state) {
                this.cfgBitsDefault ^= (1L << bit);
            }
            write(cfgBitsDefault, RECORD_ID_CFGBITS);
        }
    }

    private void setCfgBits(long cfgBits, boolean setAsDefault) {
        this.cfgBits = cfgBits;
        if (setAsDefault) {
            this.cfgBitsDefault = cfgBits;
            write(cfgBitsDefault, RECORD_ID_CFGBITS);
        }
    }

    private long getDefaultDeviceBacklightMethodMask() {
        String phoneModel = null;
        try {
            phoneModel = System.getProperty("microedition.platform");
        } catch (RuntimeException re) {
            return 0;
        } catch (Exception e) {
            return 0;
        }
        if (phoneModel != null) {
            if (phoneModel.startsWith("Nokia") || phoneModel.startsWith("SonyEricssonC") || phoneModel.startsWith("SonyEricssonK550")) {
                return 1L << CFGBIT_BACKLIGHT_NOKIA;
            } else if (phoneModel.startsWith("SonyEricssonK750") || phoneModel.startsWith("SonyEricssonW800")) {
                return 1L << CFGBIT_BACKLIGHT_NOKIAFLASH;
            } else if (phoneModel.endsWith("(NSG)") || phoneModel.startsWith("SIE")) {
                return 1 << CFGBIT_BACKLIGHT_SIEMENS;
            }
        }
        return 0;
    }
}
