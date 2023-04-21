package com.nex.content.xtm;

import java.util.*;
import java.sql.*;
import com.nex.content.SimpleGrove;
import com.nex.log.Logger;

/**
 * 20010616 jp2:  updated to allow for resource-based occurrences
 */
public class XTMSession {

    /** displayed in MainFrame */
    private String currentTmID = null;

    private XTMTopicMap currentTM = null;

    private XTMTopic currentTopic = null;

    private ArrayList openTopicMaps = null;

    /** displayed in MainFrame */
    private String currentTopicID = null;

    private ArrayList openTopics = null;

    private String currentAssociationID = null;

    /** user ID */
    private String userID = null;

    private HsqlCommander commander = null;

    private XTMElementBuilder builder = null;

    private String tabName = null;

    private Vector workingVec = null;

    /**
   * A host here is only needed for Server implementations
   */
    private com.nex.content.NEXSessionHandler host = null;

    private SimpleGrove grove = null;

    /**
   * MODIFY THIS FOR TRUE GENERIC FILE://
   */
    private String baseURI = "d:/projects/Nexist/data/";

    public XTMSession(String user, HsqlCommander c, com.nex.content.NEXSessionHandler ho, String addr) {
        this.userID = user;
        this.commander = c;
        this.host = ho;
        this.builder = new XTMElementBuilder(commander);
        if (addr != null) baseURI = addr;
        this.grove = new SimpleGrove(builder, baseURI);
    }

    /**
   * Assumes we have chosen an existing TM
   */
    public void setSelectedTopicMap(String tmID) {
        this.currentTM = new XTMTopicMap(tmID, this.tabName, null, false, this.commander);
        this.currentTmID = tmID;
        Logger.cat.debug("XTMSession setSelectedTopicMap " + tmID + " " + this.currentTmID + " " + tabName + " " + commander + " " + (currentTM == null));
    }

    public String getTopicSource() {
        return currentTopic.toString();
    }

    /**
   * One of the first things done when a Client logs in
   */
    public void setTabName(String name) {
        System.out.println("XTMSession setting tabName " + name);
        this.tabName = name;
    }

    public void deleteAssociation() {
        currentTM.deleteAssociation(this.currentAssociationID);
    }

    public void deleteTopicMap() {
        System.out.println("Deleting " + currentTmID);
        if (currentTM != null) {
            currentTM.deleteSelf();
            currentTM = null;
            currentTmID = null;
        }
    }

    /**
   * Can't delete if a topic is not selected
   * and, if it's selected, it's currentTopic
   */
    public void deleteTopic() {
        currentTM.deleteTopic(currentTopic.getID());
        currentTopic = null;
    }

    public void deleteOccurrence(String id) {
        currentTM.deleteOccurrence(id, currentTopic.getID());
        setSelectedTopic(currentTopic.getID());
    }

    public void updateOccurrence(String id, String body) {
        XTMOccurrence occur = new XTMOccurrence(id, currentTmID, currentTopic.getID(), true, commander);
        XTMResourceRef ref = occur.getResourceRef();
        String xref = null;
        if (ref == null) {
            xref = grove.storeOccurrence(body);
            ref = new XTMResourceRef(builder.getNextIDNumber(), xref, currentTmID, null, null, null, id, null, null, false, commander);
            occur.setResourceRef(ref);
            occur.insertSelf();
        } else {
            xref = ref.getHREF();
            grove.updateOccurrence(xref, body);
        }
    }

    public Vector getAssociatedTopics() {
        return currentTM.getAssociatedTopics(currentTmID, currentTopic.getID());
    }

    /**
   * Hard-wired parent-child association
   */
    public Vector getChildren(String topicID) {
        Vector result = new Vector();
        ResultSet rs = commander.execute("SELECT CHILDID FROM  TOPIC WHERE TMID='" + currentTmID + "' AND TOPICID='" + topicID + "'");
        Object obj = null;
        try {
            while (rs.next()) {
                obj = commander.getObject(rs, "CHILDID");
                if (obj != null) {
                    result.addElement((String) obj);
                    result.addElement("CHILD");
                }
            }
        } catch (SQLException sxx) {
            System.out.println("XTMSession getChildren error " + sxx.getMessage());
        }
        return result;
    }

    public Vector getAllAssociations() {
        return currentTM.getAllAssociations();
    }

    /**
   * Add a child to the current topic
   */
    public void addChild(String childID) {
        newTopic(childID);
        currentTopic.addChild(childID);
    }

    /**
   * Add a child to given topic
   * If parentID == "", then use currentTopic
   * By using this method, we don't need to keep swapping currentTopic
   */
    public void addChild(String parentID, String childID) {
        newTopic(childID);
        String pID = parentID;
        if (pID.equals("")) pID = currentTopic.getID();
        ResultSet rs = commander.execute("INSERT INTO TOPIC (TMID, TOPICID, CHILDID) VALUES('" + currentTmID + "', '" + pID + "', '" + childID + "')");
    }

    public void addChildWithOccurrence(String parentID, String childID, String body) {
        addChild(parentID, childID);
        addTextOccurrence(childID, childID, body, "", "", "", "");
    }

    /**
   * Deleting a child is not easy, given the way a session works.
   * User clicks on a topic and it automatically becomes currentTopic
   * Now, how to select which to delete without makint it currentTopic?
   * MIGHT CONSIDER HOLDING SHIFT KEY.  First one selected is currentTM,
   * second one selected becomes topicID.
   */
    public void deleteChild(String topicID) {
    }

    public ArrayList getTopicIDs() {
        return currentTM.getTopicIDs(currentTmID);
    }

    public Vector getIssueIDs() {
        Vector v = new Vector();
        Object obj = null;
        ResultSet rs = commander.execute("SELECT TMID FROM IBIS");
        try {
            while (rs.next()) {
                obj = commander.getObject(rs, "TMID");
                if (obj != null) v.addElement(obj);
            }
        } catch (SQLException sqx) {
            System.out.println("Get Issues " + sqx.getMessage());
        }
        return v;
    }

    public Vector getTopicMapIDs(String tabID) {
        Vector v = new Vector();
        Object obj = null;
        ResultSet rs = null;
        if (tabName.equals("XTM")) rs = commander.execute("SELECT TOPICMAPID FROM TOPICMAP"); else rs = commander.execute("SELECT TOPICMAPID FROM TOPICMAP WHERE TABID='" + tabName + "'");
        try {
            while (rs.next()) {
                obj = commander.getObject(rs, "TOPICMAPID");
                if (obj != null) v.addElement(obj);
            }
        } catch (SQLException sqr) {
            System.out.println("Get TopicMapIDs " + sqr.getMessage());
        }
        return v;
    }

    public XTMTopic getTopic(String tmID, String topID) {
        return currentTM.getTopic(topID);
    }

    public void setSelectedTopic(XTMTopic topic) {
        currentTopic = topic;
    }

    /**
   * Note: we are just fetching it, not caching for the time being
   */
    public void setSelectedTopic(String topicID) {
        System.out.println("XTMSession setting selected topic " + topicID + " " + this.currentTmID);
        Logger.cat.debug("XTMSession setSelectedTopic 1 " + topicID + " " + this.currentTmID + " " + (currentTM == null));
        if (this.currentTmID == null) this.currentTmID = this.currentTM.getID();
        Logger.cat.debug("XTMSession setSelectedTopic 2 " + topicID + " " + this.currentTmID);
        currentTopic = new XTMTopic(topicID, this.currentTmID, true, this.commander);
    }

    public String getCurrentTmID() {
        return this.currentTmID;
    }

    public String getCurrentTopicID() {
        if (currentTopic == null) return null;
        return this.currentTopic.getID();
    }

    /**
   * Does not set currentTopic
   */
    public XTMTopic newTopic(String id) {
        XTMTopic top = new XTMTopic(id, currentTmID, false, commander);
        top.insertSelf();
        return top;
    }

    /**
   * Sets currentTopic
   */
    public XTMTopic newTopic(String id, String baseName, String psi, Vector instanceOf) {
        currentTopic = new XTMTopic(id, currentTmID, false, commander);
        System.out.println("XTMSession New Topic " + id + " " + tabName);
        if (baseName != null && !baseName.equals("")) {
            XTMBaseName bn = new XTMBaseName(builder.getNextIDNumber(), currentTmID, id, null, false, commander);
            bn.setBaseNameString(baseName);
            currentTopic.addBaseName(bn);
        }
        if (psi != null && !psi.equals("")) {
            XTMSubjectIdentity sid = new XTMSubjectIdentity(builder.getNextIDNumber(), currentTmID, id, false, commander);
            XTMSubjectIndicatorRef sir = new XTMSubjectIndicatorRef(builder.getNextIDNumber(), psi, currentTmID, null, null, sid.getID(), null, null, null, null, false, commander);
            sid.addSubjectIndicatorRef(sir);
            currentTopic.setSubjectIdentity(sid);
        }
        if (instanceOf != null) {
            String ioID = null;
            for (int i = 0; i < instanceOf.size(); i++) {
                ioID = (String) instanceOf.elementAt(i);
                System.out.println("Session adding instanceOf " + currentTopic.getID() + " " + ioID);
                XTMInstanceOf iox = new XTMInstanceOf(builder.getNextIDNumber(), currentTmID, null, id, null, false, commander);
                XTMTopicRef iotr = new XTMTopicRef(builder.getNextIDNumber(), "#" + ioID, currentTmID, null, null, null, null, iox.getID(), null, null, false, commander);
                iox.setTopicRef(iotr);
                currentTopic.addInstanceOf(iox);
            }
        }
        currentTopic.insertSelf();
        return currentTopic;
    }

    /**
   * New TM becomes currentTM
   */
    public void newTopicMap(String id) {
        Logger.cat.debug("XTMSession new TopicMap " + id);
        this.currentTmID = id;
        System.out.println("XTMSession New Topic Map " + id + " " + tabName);
        this.currentTM = new XTMTopicMap(id, tabName, null, true, commander);
    }

    public String exportTM() {
        return currentTM.toString();
    }

    public String getNextID() {
        return builder.getNextIDNumber();
    }

    /**
   * Restricted to TopicRefs for now
   */
    public void newAssociation(String assocID, String memberA, String memberB, String roleA, String roleB, String instanceOf, String scope) {
        XTMAssociation assoc = new XTMAssociation(assocID, currentTmID, false, commander);
        XTMMember memb = null;
        XTMTopicRef tr = null;
        String newIDA = getNextID();
        String newIDB = getNextID();
        if (!memberA.equals("")) {
            memb = new XTMMember(newIDA, currentTmID, assocID, false, commander);
            tr = new XTMTopicRef(newIDB, "#" + memberA, currentTmID, null, null, newIDA, null, null, null, null, false, commander);
            memb.addTopicRef(tr);
            if (!roleA.equals("")) {
                XTMRoleSpec rsa = new XTMRoleSpec(currentTmID, getNextID(), newIDA, false, commander);
                XTMTopicRef tra = new XTMTopicRef(getNextID(), "#" + roleA, currentTmID, null, rsa.getID(), null, null, null, null, null, false, commander);
                rsa.setTopicRef(tra);
                memb.setRoleSpec(rsa);
            }
            assoc.addMember(memb);
        }
        if (!memberB.equals("")) {
            newIDA = getNextID();
            newIDB = getNextID();
            memb = new XTMMember(newIDA, currentTmID, assocID, false, commander);
            tr = new XTMTopicRef(newIDB, "#" + memberB, currentTmID, null, null, newIDA, null, null, null, null, false, commander);
            memb.addTopicRef(tr);
            if (!roleB.equals("")) {
                XTMRoleSpec rsb = new XTMRoleSpec(currentTmID, getNextID(), newIDA, false, commander);
                XTMTopicRef trb = new XTMTopicRef(getNextID(), "#" + roleB, currentTmID, null, rsb.getID(), null, null, null, null, null, false, commander);
                rsb.setTopicRef(trb);
                memb.setRoleSpec(rsb);
            }
            assoc.addMember(memb);
        }
        if (!instanceOf.equals("")) {
            XTMInstanceOf ino = new XTMInstanceOf(getNextID(), currentTmID, null, null, assocID, false, commander);
            XTMTopicRef itr = new XTMTopicRef(getNextID(), "#" + instanceOf, currentTmID, null, null, null, null, ino.getID(), null, null, false, commander);
            ino.setTopicRef(itr);
            assoc.setInstanceOf(ino);
        }
        if (!scope.equals("")) {
            XTMScope sco = new XTMScope(getNextID(), currentTmID, null, assocID, null, false, commander);
            XTMTopicRef str = new XTMTopicRef(getNextID(), "#" + scope, currentTmID, null, null, null, sco.getID(), null, null, null, false, commander);
            sco.addTopicRef(str);
            assoc.setScope(sco);
        }
        assoc.insertSelf();
    }

    /**
   * properID() allows user to just send "" if they don't have an ID
   */
    String properID(String id) {
        if (id.equals("")) return builder.getNextIDNumber();
        return id;
    }

    /**
   * Adds text based occurrence to selected Topic
   */
    public void addTextOccurrence(String topicID, String id, String text, String instanceOf, String scope, String basename, String annotates) {
        System.out.println("Adding text occurrence " + topicID + " " + id + " " + text);
        String tID = topicID;
        if (tID.equals("")) tID = currentTopic.getID();
        newTextOccurrence(tID, id, text, instanceOf, scope, basename, annotates);
    }

    /**
   * Adds resource based occurrence to selected Topic
   */
    public void addResourceOccurrence(String topicID, String id, String instanceOf, String scope, String basename, String uri) {
        String tID = topicID;
        if (tID.equals("")) tID = currentTopic.getID();
        newResourceOccurrence(tID, id, instanceOf, scope, basename, uri);
    }

    /**
   * Adds text occurrence to currentTopic
   */
    void newTextOccurrence(String topID, String id, String text, String instanceOf, String scope, String basename, String annotates) {
        System.out.println("XTMSession new occurrence " + tabName + " " + id + " " + currentTopic);
        String oid = properID(id);
        XTMOccurrence occ = new XTMOccurrence(oid, currentTmID, topID, false, commander);
        String uri = grove.storeOccurrence(text);
        if (uri == null) return;
        System.out.println("XTMSession newOccurrence " + uri);
        XTMResourceRef ref = new XTMResourceRef(builder.getNextIDNumber(), uri, currentTmID, null, null, null, oid, null, null, false, commander);
        occ.setResourceRef(ref);
        if (!instanceOf.equals("")) {
            XTMInstanceOf xio = new XTMInstanceOf(builder.getNextIDNumber(), currentTmID, occ.getID(), null, null, false, commander);
            XTMTopicRef iotr = new XTMTopicRef(builder.getNextIDNumber(), "#" + instanceOf, currentTmID, null, null, null, null, xio.getID(), null, null, false, commander);
            xio.setTopicRef(iotr);
            occ.setInstanceOf(xio);
        }
        if (!scope.equals("")) {
            XTMScope xsc = new XTMScope(builder.getNextIDNumber(), currentTmID, null, null, oid, false, commander);
            XTMTopicRef iotr = new XTMTopicRef(builder.getNextIDNumber(), "#" + scope, currentTmID, null, null, null, xsc.getID(), null, null, null, false, commander);
            xsc.addTopicRef(iotr);
            occ.setScope(xsc);
        }
        if (!basename.equals("")) {
            XTMBaseName xbn = new XTMBaseName(builder.getNextIDNumber(), currentTmID, null, oid, false, commander);
            xbn.setBaseNameString(basename);
            occ.setBaseName(xbn);
        }
        occ.insertSelf();
    }

    /**
   * adds resource occurrence to currentTopic
   */
    void newResourceOccurrence(String topID, String id, String instanceOf, String scope, String basename, String uri) {
        System.out.println("XTMSession new occurrence " + tabName + " " + id + " " + currentTopic);
        if (uri == null) return;
        XTMOccurrence occ = new XTMOccurrence(properID(id), currentTmID, topID, false, commander);
        System.out.println("XTMSession newOccurrence " + uri);
        XTMResourceRef ref = new XTMResourceRef(builder.getNextIDNumber(), "#" + uri.trim(), currentTmID, null, null, null, occ.getID(), null, null, false, commander);
        occ.setResourceRef(ref);
        if (!instanceOf.equals("")) {
            XTMInstanceOf xio = new XTMInstanceOf(builder.getNextIDNumber(), currentTmID, occ.getID(), null, null, false, commander);
            XTMTopicRef iotr = new XTMTopicRef(builder.getNextIDNumber(), "#" + instanceOf, currentTmID, null, null, null, null, xio.getID(), null, null, false, commander);
            xio.setTopicRef(iotr);
            occ.setInstanceOf(xio);
        }
        if (!scope.equals("")) {
            XTMScope xsc = new XTMScope(builder.getNextIDNumber(), currentTmID, null, null, occ.getID(), false, commander);
            XTMTopicRef iotr = new XTMTopicRef(builder.getNextIDNumber(), "#" + scope, currentTmID, null, null, null, xsc.getID(), null, null, null, false, commander);
            xsc.addTopicRef(iotr);
            occ.setScope(xsc);
        }
        if (!basename.equals("")) {
            XTMBaseName xbn = new XTMBaseName(builder.getNextIDNumber(), currentTmID, null, occ.getID(), false, commander);
            xbn.setBaseNameString(basename);
            occ.setBaseName(xbn);
        }
        occ.insertSelf();
    }

    public Vector getOccurrences() {
        return currentTM.getOccurrences(currentTopic.getID());
    }

    public Vector getParentChildTree(String rootID) {
        workingVec = new Vector();
        getTree(rootID);
        return workingVec;
    }

    /**
   * recursive tree walker from root
   */
    void getTree(String parent) {
        workingVec.addElement(parent);
        ResultSet rs = commander.execute("SELECT CHILDID FROM  TOPIC WHERE TMID='" + currentTmID + "' AND TOPICID='" + parent + "'");
        Object obj = null;
        Vector children = new Vector();
        try {
            while (rs.next()) {
                obj = commander.getObject(rs, "CHILDID");
                if (obj != null) {
                    workingVec.addElement((String) obj);
                    children.addElement((String) obj);
                }
            }
            workingVec.addElement("|");
            for (int i = 0; i < children.size(); i++) getTree((String) children.elementAt(i));
        } catch (SQLException sxy) {
            System.out.println("XTMSession getParentChildTree error " + sxy.getMessage());
        }
    }

    /**
   * When user asks for occurrenceBody, we also send the URI for the occurrence
   * for use in possible transclusion
   */
    public String getOccurrenceBody(String topicID, String occurrenceID) {
        Logger.cat.debug("XTMSession getOccurrenceBody " + topicID + " " + occurrenceID + " ");
        if (occurrenceID == null) return null;
        XTMTopic top = currentTopic;
        System.out.println("XTMSession getting occurrence body " + topicID + " " + currentTopic);
        if (!top.getID().equals(topicID)) top = new XTMTopic(topicID, currentTmID, true, commander);
        String uri = top.getResourceRef(occurrenceID.trim());
        System.out.println("Session got URI " + uri);
        Logger.cat.debug("XTMSession got occurrence body URI " + uri);
        if (uri == null) return null;
        host.setOccurrenceURI(uri);
        return grove.fetchOccurrence(uri);
    }

    public Vector getAssociatedTopics(String tmID, String id) {
        return currentTM.getAssociatedTopics(tmID, id);
    }

    public void setSelectedAssociation(String id) {
        currentAssociationID = id;
    }

    public String getAssociationSource() {
        return currentTM.getAssociationSource(currentAssociationID);
    }

    public void importTopicMap(String map) {
        builder.importTopicMap(map, tabName);
        host.getTopicMapIDs(false);
    }
}
