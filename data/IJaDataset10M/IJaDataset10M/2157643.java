package com.nex.content.xtm;

import java.sql.*;

public class XTMSubjectIndicatorRef implements Cloneable {

    protected String id = null;

    protected String href = null;

    protected boolean isDirty = false;

    protected String parametersID = null;

    protected String roleSpecID = null;

    protected String subjectIdentityID = null;

    protected String instanceOfID = null;

    protected String mergeMapID = null;

    protected String memberID = null;

    protected String tmID = null;

    protected String scopeID = null;

    protected HsqlCommander commander = null;

    public XTMSubjectIndicatorRef(String id, String _href, String tmID, String parametersID, String roleSpecID, String subjectIdentityID, String instanceOfID, String mergeMapID, String memberID, String scopeID, boolean isRestore, HsqlCommander com) {
        this.commander = com;
        this.id = id;
        if (_href != null) this.href = _href.trim();
        this.parametersID = parametersID;
        this.roleSpecID = roleSpecID;
        this.subjectIdentityID = subjectIdentityID;
        this.instanceOfID = instanceOfID;
        this.mergeMapID = mergeMapID;
        this.memberID = memberID;
        this.tmID = tmID;
        this.scopeID = scopeID;
        if (isRestore) restoreSelf();
    }

    public String getHREF() {
        return this.href;
    }

    public void setHREF(String href) {
        this.href = href;
        isDirty = true;
    }

    public String getID() {
        return this.id;
    }

    /**
   * Serialize
   */
    public String toString() {
        StringBuffer buf = new StringBuffer("<subjectIndicatorRef ");
        if (id != null) buf.append("id=\"" + id + "\"");
        if (href != null) buf.append(" xlink:href=\"" + href + "\"");
        buf.append("/>");
        return buf.toString();
    }

    void restoreSelf() {
        ResultSet rs = null;
        try {
            System.out.println("Restore SubjectIndicatorRef ");
            rs = commander.execute("SELECT * FROM SUBJECTINDICATORREF WHERE TMID='" + tmID + "' AND SUBJECTINDICATORREFID='" + id + "'");
            rs.next();
            System.out.println("GetSubjectIndicatorRef " + tmID + " " + id + " " + rs);
            Object obj = commander.getObject(rs, "XLINKXREF");
            if (obj != null) this.href = (String) obj;
            obj = commander.getObject(rs, "PARAMETERSID");
            if (obj != null) this.parametersID = (String) obj;
            obj = commander.getObject(rs, "ROLESPECID");
            if (obj != null) roleSpecID = (String) obj;
            obj = commander.getObject(rs, "SUBJECTIDENTITYID");
            if (obj != null) subjectIdentityID = (String) obj;
            obj = commander.getObject(rs, "INSTANCEOFID");
            if (obj != null) instanceOfID = (String) obj;
            obj = commander.getObject(rs, "MERGEMAPID");
            if (obj != null) mergeMapID = (String) obj;
            obj = commander.getObject(rs, "MEMBERID");
            if (obj != null) memberID = (String) obj;
        } catch (SQLException sqlx) {
            System.out.println("Getting topic " + sqlx.getMessage());
        }
        isDirty = false;
    }

    public void insertSelf() {
        System.out.println("Inserting SubjectIndicatorRef " + id);
        ResultSet rs = null;
        rs = commander.execute("INSERT INTO SUBJECTINDICATORREF (TMID, SUBJECTINDICATORREFID, XLINKXREF, PARAMETERSID, SUBJECTIDENTITYID,INSTANCEOFID, ROLESPECID, MEMBERID, MERGEMAPID, SCOPEID) VALUES('" + tmID + "', '" + id + "', '" + href + "', '" + parametersID + "', '" + subjectIdentityID + "', '" + instanceOfID + "', '" + roleSpecID + "', '" + memberID + "', '" + mergeMapID + "', '" + scopeID + "')");
    }

    public void updateSelf() {
        if (isDirty) {
            String name = null;
            String val = null;
            if (memberID != null) {
                name = "MEMBERID";
                val = memberID;
            } else if (roleSpecID != null) {
                name = "ROLESPECID";
                val = roleSpecID;
            } else if (mergeMapID != null) {
                name = "MERGEMAPID";
                val = mergeMapID;
            } else if (parametersID != null) {
                name = "PARAMETERSID";
                val = parametersID;
            } else if (instanceOfID != null) {
                name = "INSTANCEOFID";
                val = instanceOfID;
            } else if (subjectIdentityID != null) {
                name = "SUBJECTIDENTITYID";
                val = subjectIdentityID;
            }
            ResultSet rs = commander.execute("UPDATE SUBJECTINDICATORREF SET XLINKXREF='" + href + "' WHERE TMID='" + tmID + "', AND SUBJECTINDICATORREFID='" + id + "', AND " + name + "='" + val + "'");
        }
        isDirty = false;
    }

    public void deleteSelf() {
        String name = null;
        String val = null;
        if (memberID != null) {
            name = "MEMBERID";
            val = memberID;
        } else if (roleSpecID != null) {
            name = "ROLESPECID";
            val = roleSpecID;
        } else if (mergeMapID != null) {
            name = "MERGEMAPID";
            val = mergeMapID;
        } else if (parametersID != null) {
            name = "PARAMETERSID";
            val = parametersID;
        } else if (instanceOfID != null) {
            name = "INSTANCEOFID";
            val = instanceOfID;
        } else if (subjectIdentityID != null) {
            name = "SUBJECTIDENTITYID";
            val = subjectIdentityID;
        }
        ResultSet rs = commander.execute("DELETE FROM SUBJECTINDICATORREF WHERE TMID='" + tmID + "' AND SUBJECTINDICATORREF='" + id + "', AND " + name + "='" + val + "'");
    }
}
