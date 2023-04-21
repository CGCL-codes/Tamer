package net.comtor.log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author root
 */
public final class ComtorLog {

    public static void log(String fileName, String message) {
        String date = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG).format(new Date());
        message = date + "\n" + message + "\n";
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(fileName, true);
            fos.write(message.getBytes());
            fos.flush();
            fos.close();
        } catch (IOException ex) {
            Logger.getLogger(ComtorLog.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fos.close();
            } catch (IOException ex) {
                Logger.getLogger(ComtorLog.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void log(String fileName, String message, Throwable throwable) {
        message += "\n" + printStackTrace(throwable);
        log(fileName, message);
    }

    private static String printStackTrace(Throwable exception) {
        if (exception == null) {
            return "No Exception";
        }
        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        exception.printStackTrace(printWriter);
        return writer.getBuffer().toString();
    }
}
