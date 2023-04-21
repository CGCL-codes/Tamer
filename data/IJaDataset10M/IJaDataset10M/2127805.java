package org.jxul.swing;

import java.util.Hashtable;
import org.w3c.dom.Document;

/**
 * A Xul Doc / peerMap encapsulation.
 * @deprecated
 */
public class XulDom extends Thread {

    private Document xulDoc;

    private Hashtable peerMap;

    public final void setXulDoc(Document doc) {
        this.xulDoc = doc;
    }

    public final void setPeerMap(Hashtable peerMap) {
        this.peerMap = peerMap;
    }

    public final Document getXulDoc() {
        return xulDoc;
    }

    public final Hashtable getPeerMap() {
        return peerMap;
    }
}
