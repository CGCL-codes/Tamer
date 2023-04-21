package edu.washington.assist.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.TimeZone;
import javax.sound.sampled.AudioFormat;

@SuppressWarnings("serial")
public class KittyNetProperties extends Properties {

    private static final String FILENAME = "kittynet.properties";

    private static final long DEFAULT_GPS_TIME_SHIFT = 0;

    public static final long getMagicGPSShift(long reportID) {
        String propName = "run." + reportID + ".gps.time.shift";
        String str = getProperties().getProperty(propName);
        if (str == null) return DEFAULT_GPS_TIME_SHIFT; else return Long.parseLong(str);
    }

    private static KittyNetProperties props = null;

    public static final KittyNetProperties getProperties() {
        if (props == null) {
            props = new KittyNetProperties();
            try {
                System.out.println("Loading properties....");
                props.initialize();
                props.list(System.out);
                System.out.println("Loaded properties: " + props.size());
            } catch (IOException e) {
                System.err.println("Error during config file loading:");
                e.printStackTrace();
                System.exit(0);
            }
        }
        return props;
    }

    public static final AudioFormat getAudioFormat() {
        int nChannels = 1;
        boolean bBigEndian = false;
        int nSampleSizeInBits = 16;
        float fSampleRate = (float) 16000.0;
        boolean bSigned = true;
        AudioFormat format = new AudioFormat(fSampleRate, nSampleSizeInBits, nChannels, bSigned, bBigEndian);
        return format;
    }

    public static final TimeZone getTimeZone() {
        String str = getProperties().getProperty("timezone");
        if (str != null) {
            return TimeZone.getTimeZone(str);
        } else return TimeZone.getDefault();
    }

    public static final DateFormat getDateFormatLong() {
        DateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss z");
        format.setTimeZone(getTimeZone());
        return format;
    }

    public static final DateFormat getDateFormatShort() {
        DateFormat format = new SimpleDateFormat("HH:mm:ss");
        ;
        format.setTimeZone(getTimeZone());
        return format;
    }

    private void initialize() throws IOException {
        File propertiesFile = new File(FILENAME);
        InputStream in = new FileInputStream(propertiesFile);
        this.load(in);
    }

    private KittyNetProperties() {
    }
}
