package eu.bseboy.tvrss.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import eu.bseboy.tvrss.config.Configuration;

public class DownloadStreamFactory {

    private static void debug(String message) {
        System.out.println(message);
    }

    private static void error(String message) {
        System.err.println(message);
    }

    private static OutputStream createFileOutputStream(String location) throws IOException {
        File dir = new File(location);
        File tempFile = File.createTempFile("download_", ".torrent", dir);
        debug("Created output file : " + tempFile.getAbsolutePath());
        FileOutputStream fos = new FileOutputStream(tempFile);
        return fos;
    }

    private static OutputStream createOutputStream(String location) throws IOException {
        OutputStream os = null;
        os = createFileOutputStream(location);
        return os;
    }

    public static OutputStream[] createDownloadOutputStreams(Configuration conf) throws IOException {
        OutputStream[] osArray = null;
        int numLocs = 0;
        Iterator<String> locs = conf.getDownloadLocations();
        while (locs.hasNext()) {
            numLocs++;
            locs.next();
        }
        osArray = new OutputStream[numLocs];
        numLocs = 0;
        locs = conf.getDownloadLocations();
        while (locs.hasNext()) {
            osArray[numLocs] = createOutputStream(locs.next());
            numLocs++;
        }
        return osArray;
    }
}
