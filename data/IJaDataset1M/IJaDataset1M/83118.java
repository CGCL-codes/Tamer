package com.nex.content.xtm;

import java.util.ArrayList;
import java.util.Iterator;
import java.sql.*;
import com.nex.util.iNexConstants;

public class XTMAssociation implements Cloneable, iNexConstants {

    protected String id = null;

    protected XTMInstanceOf instanceOf = null;

    protected XTMScope scope = null;

    protected ArrayList memberList = null;

    protected boolean isDirty = false;

    protected String tmID = null;

    protected HsqlCommander commander = null;

    /**
   *
   */
    public XTMAssociation(String id, String tmID, boolean isRestore, HsqlCommander com) {
        this.commander = com;
        this.id = id;
        this.tmID = tmID;
        if (isRestore) restoreSelf();
    }

    public XTMAssociation() {
    }

    public XTMAssociation(String id) {
        this.id = id;
    }

    public void setID(String id) {
        this.id = id;
        isDirty = true;
    }

    public String getID() {
        return this.id;
    }

    public XTMInstanceOf getInstanceOf() {
        return this.instanceOf;
    }

    public void setInstanceOf(XTMInstanceOf al) {
        this.instanceOf = al;
        isDirty = true;
    }

    public XTMScope getScope() {
        return this.scope;
    }

    public void setScope(XTMScope al) {
        this.scope = al;
        isDirty = true;
    }

    public void addMember(XTMMember mo) {
        if (memberList == null) memberList = new ArrayList();
        memberList.add(mo);
        isDirty = true;
    }

    /**
   * Handle lists
   * Allows for external processing
   */
    public ArrayList getMemberList() {
        return this.memberList;
    }

    public void setMemberList(ArrayList al) {
        this.memberList = al;
        isDirty = true;
    }

    /**
   * Serialize
   */
    public String toString() {
        StringBuffer buf = new StringBuffer("<association ");
        if (id != null) buf.append("id=\"" + id + "\"");
        buf.append(">");
        if (instanceOf != null) buf.append("\n " + instanceOf.toString());
        if (scope != null) buf.append("\n " + scope.toString());
        if (memberList != null) {
            Iterator itr = memberList.iterator();
            while (itr.hasNext()) {
                buf.append("\n " + ((XTMMember) itr.next()).toString());
            }
        }
        buf.append("\n</association>");
        return buf.toString();
    }

    void restoreSelf() {
        System.out.println("Restoring Association " + id);
        try {
            String query = "SELECT MEMBERID FROM MEMBER WHERE TMID='" + tmID + "' AND ASSOCIATIONID='" + id + "'";
            ResultSet rs = commander.execute(query);
            System.out.println(query + rs);
            Object obj = null;
            while (rs.next()) {
                obj = commander.getObject(rs, "MEMBERID");
                if (obj != null) addMember(new XTMMember((String) obj, tmID, id, true, commander));
            }
            query = "SELECT INSTANCEOFID FROM INSTANCEOF WHERE TMID='" + tmID + "' AND ASSOCIATIONID='" + id + "'";
            rs = commander.execute(query);
            System.out.println(query + rs);
            rs.next();
            obj = commander.getObject(rs, "INSTANCEOFID");
            if (obj != null) setInstanceOf(new XTMInstanceOf((String) obj, tmID, id, null, id, true, commander));
            query = "SELECT SCOPEID FROM SCOPE WHERE TMID='" + tmID + "' AND ASSOCIATIONID='" + id + "'";
            rs = commander.execute(query);
            System.out.println(query + rs);
            rs.next();
            obj = commander.getObject(rs, "SCOPEID");
            if (obj != null) setScope(new XTMScope((String) obj, tmID, null, id, null, true, commander));
        } catch (SQLException sqlx) {
            System.out.println("Getting topic " + sqlx.getMessage());
        }
        isDirty = false;
    }

    public void insertSelf() {
        System.out.println("Inserting Association " + id);
        ResultSet rs = null;
        rs = commander.execute("INSERT INTO ASSOCIATION (TMID, ASSOCIATIONID) VALUES('" + tmID + "', '" + id + "')");
        if (instanceOf != null) instanceOf.insertSelf();
        if (scope != null) scope.insertSelf();
        Iterator itr = null;
        if (memberList != null) {
            itr = memberList.iterator();
            while (itr.hasNext()) {
                ((XTMMember) itr.next()).insertSelf();
            }
        }
        XTMChangeRegistry.notify(tmID, id, ISASSOCIATION);
    }

    public void updateSelf() {
        if (isDirty) {
            if (instanceOf != null) instanceOf.updateSelf();
            if (scope != null) scope.updateSelf();
            if (memberList != null) {
                if (memberList != null) {
                    Iterator itr = memberList.iterator();
                    while (itr.hasNext()) {
                        ((XTMMember) itr.next()).updateSelf();
                    }
                }
            }
        }
        isDirty = false;
        XTMChangeRegistry.notify(tmID, id, ISASSOCIATION);
    }

    public void deleteSelf() {
        String query = "DELETE FROM ASSOCIATION WHERE TMID='" + tmID + "' AND ASSOCIATIONID='" + id + "'";
        System.out.println(query);
        ResultSet rs = commander.execute(query);
        if (instanceOf != null) instanceOf.deleteSelf();
        if (scope != null) scope.deleteSelf();
        if (memberList != null) {
            if (memberList != null) {
                Iterator itr = memberList.iterator();
                while (itr.hasNext()) {
                    ((XTMMember) itr.next()).deleteSelf();
                }
            }
        }
    }
}
