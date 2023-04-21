package travelconn;

import java.io.*;
import java.util.*;
import java.text.*;

/**
 *
 * @author Mat
 */
public class TravelLog {

    FileOutputStream fos = null;

    OutputStreamWriter out = null;

    String cDate = null;

    public static String newline = System.getProperty("line.separator");

    private String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private String getTime() {
        DateFormat dateFormat = new SimpleDateFormat("HH-mm-ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public TravelLog() {
        cDate = getDate();
        try {
            fos = new FileOutputStream("Log-" + cDate + ".log");
            out = new OutputStreamWriter(fos, "UTF-8");
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    @Override
    protected void finalize() throws Exception {
        out.close();
        fos.close();
    }

    public void log(String logline) throws Exception {
        logline = ": " + logline + newline;
        out.write(getTime());
        out.write(logline);
        out.flush();
    }
}
