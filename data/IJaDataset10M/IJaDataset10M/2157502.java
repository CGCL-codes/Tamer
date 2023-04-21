package jwu2.log;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author Rolf
 */
public class FileLogger implements LoggerI {

    private StreamLogger streamLogger;

    public FileLogger(String filename) {
        try {
            streamLogger = new StreamLogger(new FileOutputStream(filename, true));
        } catch (IOException e) {
            System.out.println("Error creating streamLogger for file: [" + filename + "] - " + e);
        }
    }

    public void log(String msg) {
        streamLogger.log(msg);
    }
}
