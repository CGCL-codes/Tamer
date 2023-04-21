package net.sourceforge.sdm.util;

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import javax.swing.*;
import javax.swing.text.JTextComponent;
import net.sourceforge.sdm.images.ImageLoader;
import net.sourceforge.sdm.ui.JValuableLabel;

/**
 * @author cmccann
 * 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class LinkUtil {

    private static ClipboardTransfer clipboardTransfer = new ClipboardTransfer();

    static Cursor copyCursor = Toolkit.getDefaultToolkit().createCustomCursor(ImageLoader.getCopyIcon().getImage(), new Point(6, 6), "copy");

    static Cursor handCursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);

    static Cursor defaultCursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);

    private static boolean useBrowserLauncer = true;

    static String[] validPrimaryDomains = { ".com", ".net", ".org", ".us", ".info", ".biz", ".ws", ".tv", ".cc", ".de", ".jp", ".be", ".at", ".uk", ".nz", ".co" };

    public static void setUseBrowserLauncher(boolean useBrowser) {
        useBrowserLauncer = useBrowser;
        Log.out("LinkUtil: useBrowserLauncer=" + useBrowserLauncer);
    }

    /**
	 * Set different color for URL
	 */
    public static void highlightLink(JComponent label, String url) {
        if (isURL(url)) {
            label.setForeground(Color.blue);
        } else {
            label.setForeground(Color.black);
        }
    }

    /**
	 * Enabled the click function on the label
	 */
    public static void enableComponentClick(Component c) {
        c.addMouseListener(new MouseAdapter() {

            public void mouseEntered(java.awt.event.MouseEvent e) {
                mouseEnteredLabel(e);
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                mouseExitLabel(e);
            }

            public void mouseReleased(MouseEvent e) {
                mouseReleasedLabel(e);
            }
        });
    }

    /**
	 * Set Cursor type
	 */
    public static void mouseEnteredLabel(MouseEvent e) {
        Cursor rolloverCursor;
        String text = null;
        if (e.getComponent() instanceof JValuableLabel) text = ((JValuableLabel) e.getComponent()).getValue(); else if (e.getComponent() instanceof JLabel) text = ((JLabel) e.getComponent()).getText();
        if (e.getComponent() instanceof JTextComponent) text = ((JTextComponent) e.getComponent()).getText();
        if ((text != null) && (!text.equals(""))) {
            if ((isURL(text)) && (useBrowserLauncer)) {
                rolloverCursor = handCursor;
            } else {
                rolloverCursor = copyCursor;
            }
            e.getComponent().setCursor(rolloverCursor);
        }
    }

    /**
	 * Restore default Cursor
	 */
    public static void mouseExitLabel(MouseEvent e) {
        e.getComponent().setCursor(defaultCursor);
    }

    /**
	 * Copy text and provide a link if possible
	 */
    public static void mouseReleasedLabel(MouseEvent e) {
        String text = null;
        if (e.getComponent() instanceof JValuableLabel) text = ((JValuableLabel) e.getComponent()).getValue(); else if (e.getComponent() instanceof JLabel) text = ((JLabel) e.getComponent()).getText(); else if (e.getComponent() instanceof JTextComponent) text = ((JTextComponent) e.getComponent()).getText();
        Log.out("LinkUtil: mouseReleasedLable() useBrowserLauncer=" + useBrowserLauncer);
        if (text == null) {
            text = "";
        }
        if ((isURL(text)) && (useBrowserLauncer)) {
            BrowserControl.displayURL(text.trim());
        } else {
            clipboardTransfer.setClipboardContents(text.trim());
        }
    }

    /**
	 * Remove all mouse listener from the label
	 */
    public static void removeLabelClickEffects(JLabel label) {
        MouseListener[] mList = label.getMouseListeners();
        for (int i = 0; i < mList.length; i++) {
            label.removeMouseListener(mList[i]);
        }
        label.setForeground(Color.black);
    }

    /**
	 * Is it a URL
	 */
    public static boolean isURL(String siteURL) {
        try {
            new URL(siteURL);
        } catch (java.net.MalformedURLException exc) {
            return false;
        }
        return true;
    }
}
