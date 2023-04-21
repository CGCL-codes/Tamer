package ishima.logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

    private static String logFileName = "messages.log";

    public Logger() {
    }

    public static void log(String message) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(logFileName, true));
            out.write(addDate(message + '\n'));
            out.close();
        } catch (IOException e) {
            System.err.println(addDate("FATAL ERROR! Cannot log a message: '" + message + "'"));
        }
    }

    private static String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String addDate(String message) {
        return "[" + getDateTime() + "] " + message;
    }
}
