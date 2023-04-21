package net.sourceforge.processdash.process.ui;

import java.awt.Toolkit;
import java.net.URL;
import java.net.URLConnection;
import net.sourceforge.processdash.net.http.WebServer;
import net.sourceforge.processdash.ui.Browser;
import net.sourceforge.processdash.util.FileUtils;
import net.sourceforge.processdash.util.HTMLUtils;
import net.sourceforge.processdash.util.HTTPUtils;

public class TriggerURI {

    /**
     * The name of a parameter that will be present on the query string of a
     * triggering request.
     */
    public static final String IS_TRIGGERING = "isTriggering";

    /**
     * Text that an HTML response document can include to indicate that a
     * trigger request performed all necessary work, and does not need to be
     * displayed to the user.
     */
    public static final String NULL_DOCUMENT_MARKER = "<!-- null document -->";

    /**
     * Header that an HTTP response can include to indicate that a trigger
     * request performed all necessary work, and does not need to be displayed
     * to the user.
     */
    public static final String NULL_DOCUMENT_HEADER = "X-Trigger-Null-Document";

    public static boolean isTrigger(String uri) {
        return uri != null && (uri.contains("?trigger") || uri.contains("&trigger"));
    }

    public static boolean isOptionalTrigger(String uri) {
        return uri != null && (uri.contains("?trigger=optional") || uri.contains("&trigger=optional"));
    }

    public static boolean isMandatoryTrigger(String uri) {
        return isTrigger(uri) && !isOptionalTrigger(uri);
    }

    public static void handle(String uri) {
        if (isTrigger(uri)) new Thread(new TriggerLauncher(uri)).start(); else Browser.launch(uri);
    }

    private static class TriggerLauncher implements Runnable {

        private String href;

        public TriggerLauncher(String href) {
            this.href = href;
        }

        public void run() {
            try {
                String uri = href;
                if (uri.startsWith("http://localhost")) uri = new URL(uri).getFile();
                if (!uri.startsWith("/")) uri = "/" + uri;
                int pos = uri.indexOf('#');
                if (pos != -1) uri = uri.substring(0, pos);
                uri = HTMLUtils.appendQuery(uri, IS_TRIGGERING);
                URL u = new URL(WebServer.DASHBOARD_PROTOCOL + ":" + uri);
                URLConnection conn = u.openConnection();
                byte[] rawData = FileUtils.slurpContents(conn.getInputStream(), true);
                String charset = HTTPUtils.getCharset(conn.getContentType());
                String result = new String(rawData, charset);
                if (result.indexOf(TriggerURI.NULL_DOCUMENT_MARKER) == -1 && conn.getHeaderField(NULL_DOCUMENT_HEADER) == null) Browser.launch(href);
            } catch (Exception e) {
                e.printStackTrace();
                Toolkit.getDefaultToolkit().beep();
            }
        }
    }
}
