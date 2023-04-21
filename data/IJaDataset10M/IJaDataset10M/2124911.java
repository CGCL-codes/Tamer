package hu.schmidtsoft.map.gps.impl;

import hu.schmidtsoft.map.gps.IGPSEventListener;
import hu.schmidtsoft.map.model.MCoordinate;
import hu.schmidtsoft.map.util.UtilLog;
import hu.schmidtsoft.map.util.UtilNumber;
import hu.schmidtsoft.map.util.UtilString;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

/**
 * A gps source implementation that connects to a GPSD server
 * 
 * @author rizsi
 * 
 */
public class GpsdGPS extends AbstractGPS implements Runnable {

    public static final int defaultPort = 2947;

    int port;

    public GpsdGPS(int port) {
        super();
        this.port = port;
    }

    Socket s;

    Thread th;

    @Override
    void myStart() {
        log("Starting GPSD gps client");
        try {
            th = new Thread(this);
            th.start();
        } catch (Exception e) {
            UtilLog.stackTrace(e);
        }
    }

    public void stop() {
        exit = true;
        th.interrupt();
    }

    boolean exit = false;

    boolean exited = false;

    public void run() {
        try {
            while (!exit) {
                try {
                    StringBuilder bld = new StringBuilder();
                    log("GPSD Connecting to port: " + port);
                    Socket s = new Socket("localhost", port);
                    try {
                        log("GPSD port open: " + port);
                        OutputStream os = s.getOutputStream();
                        byte[] buffer = new byte[] { (byte) 'w', (byte) '=', (byte) '1', (byte) '\n' };
                        os.write(buffer, 0, 4);
                        os.flush();
                        InputStream is = s.getInputStream();
                        while (!exit) {
                            int b = is.read();
                            if (b <= 0) {
                                throw new Exception("TCP port closed");
                            }
                            char c = (char) b;
                            if (c == '\r' || c == '\n') {
                                if (bld.length() > 0) {
                                    processString(bld.toString());
                                    bld = new StringBuilder();
                                }
                            } else {
                                bld.append(c);
                            }
                        }
                    } finally {
                        s.close();
                        log("GPSG receiver port closed");
                    }
                } catch (Exception e) {
                    error("GPSD receiver error: " + e.getMessage());
                    if (!exit) {
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e1) {
                        }
                    }
                }
            }
        } finally {
            exited = true;
        }
    }

    void error(String s) {
    }

    void log(String s) {
    }

    private void processString(String str) {
        try {
            if (str.startsWith("GPSD")) {
                char ch = str.charAt(5);
                switch(ch) {
                    case 'O':
                        {
                            List<String> subs = UtilString.tokenize(str.substring("GPSD,O=".length()), " ");
                            if ("?".equals(subs.get(0))) {
                                return;
                            } else if (subs.size() == 15) {
                                @SuppressWarnings("unused") String tag = subs.get(0);
                                String timestr = subs.get(1);
                                @SuppressWarnings("unused") String ept = subs.get(2);
                                String lat = subs.get(3);
                                String lon = subs.get(4);
                                String alt = subs.get(5);
                                @SuppressWarnings("unused") String eph = subs.get(6);
                                double xC = UtilNumber.parseDouble(lon);
                                double yC = UtilNumber.parseDouble(lat);
                                double height = UtilNumber.parseDouble(alt, 0);
                                double time = UtilNumber.parseDouble(timestr, 0);
                                long timeUTC = (long) (time * 1000);
                                MCoordinate coo = new MCoordinate(xC, yC, height);
                                position(coo, timeUTC, 0);
                            }
                            break;
                        }
                }
            } else {
            }
        } catch (Exception e) {
            error(e.getMessage());
        }
    }
}
