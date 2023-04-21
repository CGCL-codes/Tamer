package net.sf.portecle.gui;

import static net.sf.portecle.FPortecle.RB;
import java.awt.Component;
import java.awt.Desktop;
import java.net.URI;
import java.net.URLEncoder;
import java.text.MessageFormat;
import javax.swing.JOptionPane;

/**
 * Desktop utilities.
 */
public final class DesktopUtil {

    /** Desktop */
    private static final Desktop DESKTOP = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;

    /** Not needed. */
    private DesktopUtil() {
    }

    /**
	 * Open URI in system default browser.
	 * 
	 * @param parentComponent
	 * @param uri URI to open
	 * @see Desktop#browse(URI)
	 */
    public static void browse(Component parentComponent, URI uri) {
        if (DESKTOP != null) {
            try {
                DESKTOP.browse(uri);
                return;
            } catch (Exception e) {
            }
        }
        JOptionPane.showMessageDialog(parentComponent, MessageFormat.format(RB.getString("FPortecle.NoLaunchBrowser.message"), uri), RB.getString("FPortecle.Title"), JOptionPane.INFORMATION_MESSAGE);
    }

    /**
	 * Open mail compose window in system default mail client.
	 * 
	 * @param parentComponent
	 * @param address E-mail address to mail to
	 * @see Desktop#mail(URI)
	 */
    public static void mail(Component parentComponent, String address) {
        if (DESKTOP != null) {
            try {
                DESKTOP.mail(new URI("mailto:" + URLEncoder.encode(address, "ISO-8859-1")));
                return;
            } catch (Exception e) {
            }
        }
        JOptionPane.showMessageDialog(parentComponent, MessageFormat.format(RB.getString("FPortecle.NoLaunchEmail.message"), address), RB.getString("FPortecle.Title"), JOptionPane.INFORMATION_MESSAGE);
    }
}
