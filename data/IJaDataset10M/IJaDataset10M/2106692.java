package org.zoolu.sip.dialog;

import java.util.Vector;
import org.zoolu.sip.address.*;
import org.zoolu.sip.message.*;
import org.zoolu.sip.header.*;
import org.zoolu.sip.provider.*;

/** Class DialogInfo maintains a complete information status of a generic SIP dialog.
* It has the following attributes:
* <ul>
* <li>sip-provider</li>
* <li>call-id</li>
* <li>local and remote URLs</li>
* <li>local and remote contact URLs</li>
* <li>local and remote cseqs</li>
* <li>local and remote tags</li> 
* <li>dialog-id</li>
* <li>m_vnameAddressRouteSet set</li>
* </ul>
*/
public class DialogInfo {

    /** Local name */
    NameAddress m_nameAddressLocal;

    /** Remote name */
    NameAddress m_nameAddressRemote;

    /** Local contact nameAddressLocal */
    NameAddress m_nameAddressLocalContact;

    /** Remote contact nameAddressLocal */
    NameAddress m_nameAddressRemoteContact;

    /** Call-id */
    String m_strCallID;

    /** Local tag */
    String m_strLocalTag;

    /** Remote tag */
    String m_strRemoteTag;

    /** Local CSeq number */
    long m_lLocalCSEQ;

    /** Remote CSeq number */
    long m_lRemoteCSEQ;

    /** Route set (Vector of NameAddresses) */
    Vector<NameAddress> m_vnameAddressRouteSet;

    /** Creates a new empty DialogInfo */
    public DialogInfo() {
        m_nameAddressLocal = null;
        m_nameAddressRemote = null;
        m_nameAddressLocalContact = null;
        m_nameAddressRemoteContact = null;
        m_strCallID = null;
        m_strLocalTag = null;
        m_strRemoteTag = null;
        m_lLocalCSEQ = -1;
        m_lRemoteCSEQ = -1;
        m_vnameAddressRouteSet = null;
    }

    /** Sets the local name */
    public void setLocalName(NameAddress nameAddressLocal) {
        m_nameAddressLocal = nameAddressLocal;
    }

    /** Gets the local name */
    public NameAddress getLocalName() {
        return m_nameAddressLocal;
    }

    /** Sets the remote name */
    public void setRemoteName(NameAddress url) {
        m_nameAddressRemote = url;
    }

    /** Gets the remote name */
    public NameAddress getRemoteName() {
        return m_nameAddressRemote;
    }

    /** Sets the local contact nameAddressLocal */
    public void setLocalContact(NameAddress name_address) {
        m_nameAddressLocalContact = name_address;
    }

    /** Gets the local contact nameAddressLocal */
    public NameAddress getLocalContact() {
        return m_nameAddressLocalContact;
    }

    /** Sets the remote contact nameAddressLocal */
    public void setRemoteContact(NameAddress name_address) {
        m_nameAddressRemoteContact = name_address;
    }

    /** Gets the remote contact nameAddressLocal */
    public NameAddress getRemoteContact() {
        return m_nameAddressRemoteContact;
    }

    /** Sets the call-id */
    public void setCallID(String id) {
        m_strCallID = id;
    }

    /** Gets the call-id */
    public String getCallID() {
        return m_strCallID;
    }

    /** Sets the local tag */
    public void setLocalTag(String tag) {
        m_strLocalTag = tag;
    }

    /** Gets the local tag */
    public String getLocalTag() {
        return m_strLocalTag;
    }

    public void setRemoteTag(String tag) {
        m_strRemoteTag = tag;
    }

    /** Gets the remote tag */
    public String getRemoteTag() {
        return m_strRemoteTag;
    }

    /** Sets the local CSeq number */
    public void setLocalCSeq(long cseq) {
        m_lLocalCSEQ = cseq;
    }

    /** Increments the local CSeq number */
    public void incLocalCSeq() {
        m_lLocalCSEQ++;
    }

    /** Gets the local CSeq number */
    public long getLocalCSeq() {
        return m_lLocalCSEQ;
    }

    /** Sets the remote CSeq number */
    public void setRemoteCSeq(long cseq) {
        m_lRemoteCSEQ = cseq;
    }

    /** Increments the remote CSeq number */
    public void incRemoteCSeq() {
        m_lRemoteCSEQ++;
    }

    /** Gets the remote CSeq number */
    public long getRemoteCSeq() {
        return m_lRemoteCSEQ;
    }

    /** Sets the m_vnameAddressRouteSet set */
    public void setRoute(Vector<NameAddress> vnameAddressRouteSet) {
        m_vnameAddressRouteSet = vnameAddressRouteSet;
    }

    /** Gets the m_vnameAddressRouteSet set */
    public Vector<NameAddress> getRoute() {
        return m_vnameAddressRouteSet;
    }
}
