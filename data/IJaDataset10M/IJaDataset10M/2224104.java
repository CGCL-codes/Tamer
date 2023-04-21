package com.cidero.control;

import java.util.ArrayList;
import java.util.logging.Logger;
import java.net.URL;
import java.net.MalformedURLException;
import org.cybergarage.upnp.Action;
import org.cybergarage.upnp.Argument;
import org.cybergarage.upnp.ArgumentList;
import org.cybergarage.upnp.ssdp.SSDPPacket;
import com.cidero.swing.text.XMLStyledDocument;

/**
 *  Debug container for multicast search request messages. There are a 
 *  number of types of search requests. An example of the syntax of 
 *  the HTTP request is:
 *
 *  M-SEARCH * HTTP/1.1
 *  ST: ssdp:all
 *  MX: 3
 *  MAN: "ssdp:discover"
 *  HOST: 239.255.255.250:1900
 *
 *  The 'ST' (search target) is set depending on the search type:
 *
 *  1.  Search for all Root device search request.
 *
 *        ST: ssdp:all
 *
 *  2.  Root device search request
 *
 *        ST: upnp:rootdevice
 *     
 *  3.  Device specific search request (not limited to these examples)
 *
 *        ST: urn:schemas-upnp-org:device:MediaServer:1
 *      
 *      or
 *
 *        ST: urn:schemas-upnp-org:device:MediaServer:1
 */
class DebugSearchRequest extends DebugObj {

    private static final Logger logger = Logger.getLogger("com.cidero.control");

    String ssdpPacket;

    String st;

    int mx;

    /**
   * Constructor
   */
    public DebugSearchRequest(SSDPPacket packet) {
        setStatus(DebugObj.STATUS_OK);
        ssdpPacket = packet.toString();
        st = packet.getST();
        mx = packet.getMX();
    }

    /**
   * Single line string represention. Used in top pane of debug window
   */
    public String toSingleLineString() {
        return getTimeString() + " SearchRequest: MX=" + mx + " ST=" + st;
    }

    /**
   * Full (may be multiple lines) string represention. Used in bottom
   * pane of debug window
   */
    public String toString() {
        return ssdpPacket;
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
        doc.appendString("Search Request SSDP Packet:\n\n" + ssdpPacket);
    }

    static boolean enabled = true;

    public static void setEnabled(boolean flag) {
        enabled = flag;
    }

    public static boolean getEnabled() {
        return enabled;
    }

    public boolean isDisplayable() {
        return true;
    }
}
