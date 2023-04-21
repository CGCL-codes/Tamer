package com.cidero.control;

import java.util.ArrayList;
import java.util.logging.Logger;
import org.cybergarage.upnp.Action;
import org.cybergarage.upnp.Argument;
import org.cybergarage.upnp.ArgumentList;
import com.cidero.swing.text.XMLStyledDocument;

/**
 *  Debug container for generic text-based messages. 
 *  
 */
class DebugMsg extends DebugObj {

    private static final Logger logger = Logger.getLogger("com.cidero.control");

    String shortDescription;

    String longDescription;

    /**
   * Constructor
   */
    public DebugMsg(String shortDescription, String longDescription) {
        setStatus(DebugObj.STATUS_OK);
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
    }

    /**
   * Single line string represention. Used in top pane of debug window
   */
    public String toSingleLineString() {
        return getTimeString() + shortDescription;
    }

    /**
   * Full (may be multiple lines) string represention. Used in bottom
   * pane of debug window
   */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("ShortDescription: " + shortDescription + "\n");
        buf.append("LongDescription: " + longDescription + "\n");
        return buf.toString();
    }

    /** 
   *  Append a text representation of the object to an XML-capable
   *  StyledDocument class.
   *
   * @param doc          XMLStyledDocument instance (normally displayed
   *                     in JTextPane)
   * @param autoFormat   Enable XML auto-formatting. Setting this to
   *                     false disables all the special XML-sensitive logic
   */
    public void append(XMLStyledDocument doc, boolean autoFormat) {
        doc.appendString("ShortDescription: " + shortDescription + "\nLongDescription:\n" + longDescription + "\n");
    }

    static boolean enabled = true;

    public static void setEnabled(boolean flag) {
        enabled = flag;
    }

    public static boolean getEnabled() {
        return enabled;
    }

    public boolean isDisplayable() {
        return enabled;
    }
}
