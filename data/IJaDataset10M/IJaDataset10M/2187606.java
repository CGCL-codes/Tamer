package net.sourceforge.juploader.upload.server;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;
import net.sourceforge.juploader.upload.ClientHttpRequest;

/**
 *
 * @author proktor
 */
public class YourFileLink extends BasicServer {

    private final int LINK = 0;

    private final int DELETE = 1;

    private final int MAX_FILESIZE = 52428800;

    public YourFileLink() {
        final ResourceBundle bundle = java.util.ResourceBundle.getBundle("net/sourceforge/juploader/upload/server/Bundle");
        fieldNames = new String[] { bundle.getString("YFLLink"), bundle.getString("YFLDelete") };
    }

    public String[] uploadFile() {
        links = new String[fieldNames.length];
        try {
            InputStream serverInput = new ClientHttpRequest(new URL("http://www.yourfilelink.com/upload.php")).post(new Object[] { "attached", new File(fileName), "agree", "yes", "action", "upload_process" });
            if (!createLinks(serverInput)) {
            }
        } catch (Exception e) {
            net.sourceforge.juploader.app.Error.showConnectionError();
        }
        return links;
    }

    private boolean createLinks(InputStream stream) {
        Scanner in = new Scanner(stream);
        int i = 0;
        while (in.hasNext()) {
            String line = in.nextLine();
            if (line.endsWith("onclick=\"this.select()\"><br>")) {
                if (i < 2) {
                    links[i++] = line.trim();
                }
            }
        }
        links[LINK] = parseLink(links[LINK]);
        links[DELETE] = parseLink(links[DELETE]);
        if (links[0].startsWith("http://www.yourfilelink.com/get") && links[1].startsWith("http://www.yourfilelink.com/delete")) {
            return true;
        } else {
            return false;
        }
    }

    private String parseLink(String line) {
        return line.substring(line.indexOf("value=") + 7, line.lastIndexOf("\" onclick"));
    }

    public Servers getServer() {
        return Servers.YOURFILELINK;
    }

    public boolean isAcceptable(String fileName) {
        if (new File(fileName).length() <= MAX_FILESIZE && fileName.indexOf('.') > -1) {
            return true;
        } else {
            return false;
        }
    }
}
