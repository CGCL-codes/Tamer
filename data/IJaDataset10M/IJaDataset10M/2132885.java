package triptracker.testing;

import static triptracker.core.Protocol.*;
import java.io.File;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.Enumeration;
import java.util.SortedSet;
import java.util.TreeSet;
import triptracker.client.gps.core.NMEASentence;
import triptracker.client.gps.core.NMEATalkerID;
import triptracker.core.Coordinate;
import triptracker.core.Sexagesimal;

public class Test {

    public static void main(String[] args) {
        findPropFile();
    }

    private static void findPropFile() {
        String s = "c:\\docume~1\\ur-qua~1\\mydocu~1\\eclips~1\\triptr~2\\release\\instme~1\\tripgps\\.install4j\\i4jruntime.jar;c:\\docume~1\\ur-qua~1\\mydocu~1\\eclips~1\\triptr~2\\release\\instme~1\\tripgps\\.\\.;c:\\docume~1\\ur-qua~1\\mydocu~1\\eclips~1\\triptr~2\\release\\instme~1\\tripgps\\.\\tripgps.jar;c:\\docume~1\\ur-qua~1\\mydocu~1\\eclips~1\\triptr~2\\release\\instme~1\\tripgps\\.\\libraries\\windows\\javacomm\\comm.jar;c:\\docume~1\\ur-qua~1\\mydocu~1\\eclips~1\\triptr~2\\release\\instme~1\\tripgps\\.\\libraries\\windows\\javacomm;";
        StreamTokenizer streamtokenizer = new StreamTokenizer(new StringReader(s));
        streamtokenizer.whitespaceChars(File.pathSeparatorChar, File.pathSeparatorChar);
        streamtokenizer.wordChars(File.separatorChar, File.separatorChar);
        streamtokenizer.ordinaryChar(46);
        streamtokenizer.wordChars(46, 46);
        try {
            while (streamtokenizer.nextToken() != -1) {
                int i = -1;
                if (streamtokenizer.ttype == -3 && (i = streamtokenizer.sval.indexOf("comm.jar")) != -1) {
                    String s1 = new String(streamtokenizer.sval);
                    File file = new File(s1);
                    if (file.exists()) {
                        String s2 = s1.substring(0, i);
                        if (s2 != null) s2 = s2 + "." + File.separator + "javax.comm.properties"; else s2 = "." + File.separator + "javax.comm.properties";
                        File file1 = new File(s2);
                        if (file1.exists()) System.out.println("property file found: " + s2); else System.out.println("property file not found");
                    }
                }
            }
        } catch (IOException _ex) {
        }
        System.out.println("property file not found");
    }

    private static void oldieDecNmeaDecTest() {
        double[] degs = { 5.600775, 5.60017222222222, 5.61073888888889, 5.61023888888889 };
        for (double d : degs) {
            System.out.println("Sexagesimal.decOldToNmea(" + d + ") = " + Coordinate.decOldToNmea(d));
            System.out.println("Coordinate.nmeaToDec(" + Coordinate.decOldToNmea(d) + ") = " + Coordinate.nmeaToDec(Coordinate.decOldToNmea(d)));
        }
    }

    public static void decSexDecTest() {
        double[] degs = { 5.600775, 5.60017222222222, 5.61073888888889, 5.61023888888889 };
        for (double d : degs) {
            System.out.println("Sexagesimal.decToSex(" + d + ") = " + Sexagesimal.decToSex(d));
            System.out.println("Coordinate.sexToDec(" + Sexagesimal.decToSex(d) + ") = " + Coordinate.sexToDec(Sexagesimal.decToSex(d)));
        }
    }

    public static void nmeaDecNmea() {
        double[] degs = { 00536.0062, 00535.9866 };
        for (double d : degs) {
            System.out.println("Coordinate.nmeaToDec(" + d + ") = " + Coordinate.nmeaToDec(d));
            System.out.println("Coordinate.decToNmea(" + Coordinate.nmeaToDec(d) + ") = " + Coordinate.decToNmea(Coordinate.nmeaToDec(d)));
        }
    }

    public static void oldDecToNewTest() {
        double[] degs = { 5.600775, 5.60017222222222, 5.61073888888889, 5.61023888888889 };
        for (double d : degs) {
            System.out.println("fixOldDec(" + d + ") = " + fixOldDec(d));
            System.out.println("ratio of oldDeg / newDeg = " + (d / fixOldDec(d)));
        }
    }

    public static double fixOldDec(final double degrees) {
        return Coordinate.nmeaOldToDec(Coordinate.decToNmea(degrees));
    }

    public static double fixOldDec2(final double degrees) {
        final double frac = (100 / 60);
        return degrees * frac;
    }

    public static void toDecTest() {
        double[] degs = { 00536.0062, 00535.9866 };
        for (double d : degs) {
            System.out.println("Coordinate.nmeaToDec(" + d + ") = " + Coordinate.nmeaToDec(d));
        }
    }

    public static void javaPropTest() {
        System.out.println("Java Properties");
        System.out.println("----------");
        SortedSet<String> props = new TreeSet<String>();
        for (Enumeration e = System.getProperties().propertyNames(); e.hasMoreElements(); ) {
            String prop = e.nextElement().toString();
            props.add(prop + "=" + System.getProperty(prop));
        }
        for (String prop : props) {
            System.out.println(prop);
        }
    }

    public static void coordToStringTest() {
        Coordinate coord = new Coordinate(58.97713055555554, 5.61849722222222);
        System.out.println(coord);
    }

    public static void nmeaTest() {
        NMEASentence nmea = new NMEASentence();
        NMEATalkerID[] ids = NMEATalkerID.values();
        System.out.println(ids[0].name());
        long time = System.currentTimeMillis();
        for (int i = 0; i < 50; i++) {
            nmea.parseSentence("$GPRMC,105648,A,5856.3026,N,00541.6098,E," + "0.0,0.0,170106,1.3,W,A*0A" + NEWLINE);
            System.out.println(nmea.getTalkerID() + " " + nmea.getSentenceID());
            nmea.parseSentence("$GPRMB,A,,,,,,,,,,,,A,A*0B" + NEWLINE);
            nmea.parseSentence("$GPRMC,105648,A,5856.3026,N,00541.6098,E," + "0.0,0.0,170106,1.3,W,A*0A" + NEWLINE);
            nmea.parseSentence("$GPGGA,105648,5856.3026,N,00541.6098,E," + "1,05,3.8,28.2,M,44.0,M,,*74" + NEWLINE);
            nmea.parseSentence("$GPGSA,A,3,03,,,13,15,,,21,23,,,," + "6.5,3.8,4.7*3E" + NEWLINE);
            nmea.parseSentence("$GPGSV,3,1,11,03,13,262,39,06,46,090,00," + "10,24,046,00,13,13,325,37*7E" + NEWLINE);
            nmea.parseSentence("$GPGLL,5856.3026,N,00541.6098,E,105648,A,A*47" + NEWLINE);
            nmea.parseSentence("$GPBOD,,T,,M,,*47" + NEWLINE);
            nmea.parseSentence("$GPVTG,0.0,T,1.3,M,0.0,N,0.0,K*4C" + NEWLINE);
            nmea.parseSentence("$PGRME,26.2,M,45.0,M,52.0,M*1E" + NEWLINE);
            nmea.parseSentence("$PGRMZ,93,f,3*21" + NEWLINE);
            nmea.parseSentence("$PGRMM,WGS 84*06" + NEWLINE);
            nmea.parseSentence("$GPRTE,1,1,c,*37" + NEWLINE);
        }
        long curTime = System.currentTimeMillis();
        System.out.println("Duration: " + (curTime - time) + " ms");
    }
}
