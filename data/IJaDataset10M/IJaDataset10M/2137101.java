package desktoputils;

import java.io.*;
import java.net.*;
import java.awt.*;

public class deskTopUtils {

    public static void WriteHTMFile(String htm, String HTMFilename, boolean startBrowser) throws Exception {
        try {
            File f = new File(HTMFilename);
            if (f.exists()) f.delete();
            PrintWriter HTMFileout = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(HTMFilename), "UTF8")));
            HTMFileout.print(htm);
            HTMFileout.close();
            System.out.println("START BROWSER" + startBrowser);
            if (startBrowser) launchBrowser(HTMFilename);
        } catch (Exception e) {
            throw new gqtiexcept.XMLException("Cannot write " + HTMFilename + "/n" + e.getMessage());
        }
    }

    public static void launchBrowser(String HTMFilename) throws java.io.IOException {
        File f = new File(HTMFilename);
        if (f.exists()) {
            URI uri = null;
            uri = f.toURI();
            Desktop.getDesktop().browse(uri);
        }
    }
}
