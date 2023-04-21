package mecca.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author Shaiful Nizam Tajul
 * @version 1.01
 */
public class Logger {

    private Calendar cal = Calendar.getInstance();

    private DateFormat df = new SimpleDateFormat("MMM dd, yyyy h:mm:ss a");

    private String className;

    public Logger(String className) {
        this.className = className;
    }

    /**
 * This method is used for printing messages out onto the system console.
 */
    public void setMessage(String msg) {
        String date = df.format(cal.getTime());
        System.out.println("[" + date + "]" + className + ": " + msg);
    }
}
