package myqueueserver.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author Nikos Siatras
 */
public class ServerLog {

    private static SimpleDateFormat fDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    private static Calendar fCalendar = Calendar.getInstance();

    public ServerLog() {
    }

    public static void WriteToLog(String event, LogMessageType type) {
        event = fDateFormat.format(fCalendar.getTime()) + "\t" + event;
        switch(type) {
            case Error:
                event = "[Error]\t" + event;
                System.err.println(event);
                break;
            case Warning:
                event = "[Warning]\t" + event;
                System.out.println(event);
                break;
            case Information:
                event = "[Information]\t" + event;
                System.out.println(event);
                break;
        }
    }
}
