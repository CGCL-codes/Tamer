package common;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import javax.swing.JEditorPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

public class HTMLEditorPane {

    private static HTMLEditorPane s_htmlEditorPane;

    private Desktop m_desktop;

    private HTMLEditorPane() {
        if (Desktop.isDesktopSupported()) {
            m_desktop = Desktop.getDesktop();
        }
    }

    public static JEditorPane getPane(String html) {
        if (s_htmlEditorPane == null) {
            s_htmlEditorPane = new HTMLEditorPane();
        }
        return s_htmlEditorPane.getEditorPane(html);
    }

    public JEditorPane getEditorPane(String html) {
        JEditorPane jep = new JEditorPane("text/html", html);
        jep.setEditable(false);
        jep.setOpaque(false);
        jep.addHyperlinkListener(new HyperlinkListener() {

            public void hyperlinkUpdate(HyperlinkEvent hle) {
                if (HyperlinkEvent.EventType.ACTIVATED.equals(hle.getEventType())) {
                    URL url = hle.getURL();
                    if (m_desktop != null) {
                        openBrowser(m_desktop, url);
                    }
                }
            }
        });
        return jep;
    }

    private void openBrowser(Desktop desktop, URL url) {
        URI uri = null;
        try {
            uri = new URI(url.toExternalForm());
            desktop.browse(uri);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (URISyntaxException use) {
            use.printStackTrace();
        }
    }
}
