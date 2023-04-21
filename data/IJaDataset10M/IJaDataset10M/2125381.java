package org.jabber.xdb.module;

import org.jabber.xdb.*;
import org.jabber.jabberbeans.serverside.*;
import org.jabber.jabberbeans.Extension.*;
import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;
import java.io.*;
import java.sql.*;
import java.util.Enumeration;
import java.util.List;

public class IQRoster implements JabberModule {

    /**
		 * Handles the jabber:iq:roster namespace.
		 *
		 * @param XDB packet received from Jabber.
		 * @return XDBResponse used to build xdb response packet.
		 * @author: Matthew D. Diez
		 *
		 */
    public XDBResponse handlePacket(XDB packet) {
        XDBResponse response = new XDBResponse();
        String sSQL;
        Connection con = Configuration.getConnection();
        if (packet.getType().equals("set")) {
            Extension xdbe = new XDBExtension();
            Enumeration extensions;
            extensions = packet.Extensions();
            xdbe = (Extension) extensions.nextElement();
            Configuration.log("***** HANDLING PACKET *****");
            if (xdbe != null) {
                SAXBuilder builder = new SAXBuilder();
                try {
                    Configuration.log("**** Entering roster management **** ");
                    org.jdom.Document doc = builder.build(new StringReader(xdbe.toString()));
                    Namespace localNS = Namespace.getNamespace("jabber:iq:roster");
                    org.jdom.Element queryElement = doc.getRootElement();
                    List itemsList = queryElement.getChildren("item", localNS);
                    int iSizeOfItemsList = itemsList.size();
                    Configuration.log("Beginning user subscription removal");
                    sSQL = "DELETE FROM ROSTERUSERS WHERE USERNAME = '" + packet.getToAddress() + "';";
                    try {
                        Statement deleteStatement = con.createStatement();
                        deleteStatement.executeUpdate(sSQL);
                        deleteStatement.close();
                    } catch (SQLException se) {
                        Configuration.log("jabber:iq:roster: Unable to delete record");
                        response.setErrorMessage(true);
                        response.setErrorText("jabber:iq:roster: Unable to delete record");
                        response.setErrorCode("503");
                        return response;
                    }
                    for (int iFig = 0; iFig < iSizeOfItemsList; iFig++) {
                        boolean bUpdate = false;
                        Configuration.log("*** Handling list item Number " + iFig);
                        org.jdom.Element listItem = (org.jdom.Element) itemsList.get(iFig);
                        String sJID = listItem.getAttributeValue("jid");
                        String sName = listItem.getAttributeValue("name");
                        String sSubscription = listItem.getAttributeValue("subscription");
                        String sAsk = listItem.getAttributeValue("ask");
                        Configuration.log("Checking for NULL JID!");
                        if (sJID == null) {
                            Configuration.log("Null JID! Inconceivable!");
                        }
                        Configuration.log("Cleaning out ROSTERGROUPS");
                        sSQL = "DELETE FROM ROSTERGROUPS WHERE USERNAME = '" + packet.getToAddress() + "' AND JID ='" + sJID + "';";
                        try {
                            Statement deleteStatement = con.createStatement();
                            deleteStatement.executeUpdate(sSQL);
                            deleteStatement.close();
                        } catch (SQLException se) {
                            Configuration.log("jabber:iq:roster: Unable to update record");
                            response.setErrorMessage(true);
                            response.setErrorText("jabber:iq:roster: Unable to update record");
                            response.setErrorCode("503");
                            return response;
                        }
                        Configuration.log("Checking to see if user exists in ROSTERUSERS");
                        sSQL = "SELECT * FROM ROSTERUSERS WHERE USERNAME = '" + packet.getToAddress() + "'  AND JID = '" + sJID + "';";
                        try {
                            Statement selectStatement = con.createStatement();
                            ResultSet rosterUsers = selectStatement.executeQuery(sSQL);
                            while (rosterUsers.next()) {
                                bUpdate = true;
                            }
                            selectStatement.close();
                        } catch (SQLException se) {
                            Configuration.log("jabber:iq:roster: Unable to retrieve record");
                            response.setErrorMessage(true);
                            response.setErrorText("jabber:iq:roster: Unable to retrieve record");
                            response.setErrorCode("503");
                            return response;
                        }
                        if (bUpdate == true) {
                            StringBuffer sBuf = new StringBuffer("");
                            boolean bMultiple = false;
                            sBuf.append("UPDATE ROSTERUSERS SET ");
                            Configuration.log("1 Subscription is !!! " + sSubscription);
                            if (sSubscription != null) {
                                char cSub = '-';
                                sBuf.append("SUBSCRIPTION = '");
                                if (sSubscription.equals("none")) cSub = 'N';
                                if (sSubscription.equals("to")) cSub = 'T';
                                if (sSubscription.equals("from")) cSub = 'F';
                                if (sSubscription.equals("both")) cSub = 'B';
                                sBuf.append(cSub);
                                sBuf.append("' ");
                                bMultiple = true;
                            }
                            Configuration.log("2 sAsk is !!! " + sSubscription);
                            if (sAsk != null) {
                                char cAsk = '-';
                                if (sAsk.equals("subscribe")) cAsk = 'S';
                                if (sAsk.equals("unsubscribe")) cAsk = 'U';
                                if (bMultiple == true) sBuf.append(",");
                                sBuf.append("ASK = '");
                                sBuf.append(cAsk);
                                sBuf.append("' ");
                                bMultiple = true;
                            }
                            Configuration.log("3 sName is !!! " + sSubscription);
                            if (sName != null) {
                                if (bMultiple == true) sBuf.append(", ");
                                sBuf.append("NICK = '");
                                sBuf.append(sName);
                                sBuf.append("' ");
                            }
                            sBuf.append("WHERE USERNAME = '");
                            sBuf.append(packet.getToAddress());
                            sBuf.append("' AND JID = '");
                            sBuf.append(sJID);
                            sBuf.append("';");
                            sSQL = sBuf.toString();
                            try {
                                Configuration.log("Doing updating" + sSubscription);
                                Statement updateStatement = con.createStatement();
                                updateStatement.executeUpdate(sSQL);
                                Configuration.log("Wayy Updated " + sSubscription);
                                updateStatement.close();
                            } catch (SQLException se) {
                                Configuration.log("SQLException in IQRosterHandler: " + se);
                                return response;
                            }
                        } else if (bUpdate == false) {
                            StringBuffer sBuf = new StringBuffer("");
                            sBuf.append("INSERT INTO ROSTERUSERS (USERNAME, JID, NICK, SUBSCRIPTION, ASK) VALUES (");
                            sBuf.append("'");
                            sBuf.append(packet.getToAddress());
                            sBuf.append("'");
                            sBuf.append(",");
                            sBuf.append("'");
                            sBuf.append(sJID);
                            sBuf.append("'");
                            sBuf.append(",");
                            sBuf.append("'");
                            if (sName != null) sBuf.append(sName);
                            sBuf.append("'");
                            sBuf.append(",");
                            sBuf.append("'");
                            char cSub = '-';
                            if (sSubscription != null) {
                                if (sSubscription.equals("none")) cSub = 'N';
                                if (sSubscription.equals("to")) cSub = 'T';
                                if (sSubscription.equals("from")) cSub = 'F';
                                if (sSubscription.equals("both")) cSub = 'B';
                            }
                            sBuf.append(cSub);
                            sBuf.append("'");
                            sBuf.append(",");
                            sBuf.append("'");
                            char cAsk = '-';
                            if (sAsk != null) {
                                if (sAsk.equals("subscribe")) cAsk = 'S';
                                if (sAsk.equals("unsubscribe")) cAsk = 'U';
                            }
                            sBuf.append(cAsk);
                            sBuf.append("'");
                            sBuf.append(");");
                            Configuration.log("Check 2.9");
                            sSQL = sBuf.toString();
                            try {
                                Statement insertStatement = con.createStatement();
                                insertStatement.executeUpdate(sSQL);
                                insertStatement.close();
                            } catch (SQLException se) {
                                Configuration.log("jabber:iq:roster: Unable to update record");
                                response.setErrorMessage(true);
                                response.setErrorText("jabber:iq:roster: Unable to update record");
                                response.setErrorCode("503");
                                return response;
                            }
                        }
                        List groupsList = listItem.getChildren("group", localNS);
                        int iSizeOfGroupsList = groupsList.size();
                        for (int iKiwi = 0; iKiwi < iSizeOfGroupsList; iKiwi++) {
                            Configuration.log("loop + " + iKiwi);
                            org.jdom.Element groupItem = (org.jdom.Element) groupsList.get(iKiwi);
                            sSQL = "INSERT INTO ROSTERGROUPS (USERNAME, JID, GRP) VALUES ('" + packet.getToAddress() + "','" + sJID + "','" + groupItem.getText() + "');";
                            try {
                                Configuration.log("SQL being run is" + sSQL);
                                Statement insertStatement = con.createStatement();
                                insertStatement.executeUpdate(sSQL);
                                insertStatement.close();
                            } catch (SQLException se) {
                                Configuration.log("jabber:iq:roster: Unable to update record");
                                response.setErrorMessage(true);
                                response.setErrorText("jabber:iq:roster: Unable to update record");
                                response.setErrorCode("503");
                                return response;
                            }
                        }
                    }
                } catch (Exception e) {
                    Configuration.log("***** Exception: " + e);
                    return response;
                }
            }
        }
        if (packet.getType().equals("get")) {
            XDBExtension xdbe = new XDBExtension();
            sSQL = ("SELECT * FROM ROSTERUSERS WHERE USERNAME = '" + packet.getToAddress() + "';");
            try {
                Configuration.log("SQL being run is: " + sSQL);
                Statement selectStatement = con.createStatement();
                ResultSet rosterUsers = selectStatement.executeQuery(sSQL);
                xdbe.appendTagData("<query xmlns='jabber:iq:roster'>");
                while (rosterUsers.next() && (rosterUsers != null)) {
                    xdbe.appendTagData("<item ");
                    String sJID = rosterUsers.getString("jid");
                    xdbe.appendTagData("jid='" + sJID + "' ");
                    String sName = rosterUsers.getString("nick");
                    if (sName != null) xdbe.appendTagData(" name='" + sName + "'");
                    String sSubscription = rosterUsers.getString("subscription");
                    if (sSubscription.equals("-")) {
                    } else {
                        xdbe.appendTagData(" subscription='");
                        if (sSubscription.equals("N")) xdbe.appendTagData("none");
                        if (sSubscription.equals("T")) xdbe.appendTagData("to");
                        if (sSubscription.equals("F")) xdbe.appendTagData("from");
                        if (sSubscription.equals("B")) xdbe.appendTagData("both");
                        xdbe.appendTagData("' ");
                    }
                    String sAsk = rosterUsers.getString("ask");
                    if (sAsk.equals("-")) {
                    } else {
                        xdbe.appendTagData(" ask='");
                        if (sAsk.equals("S")) xdbe.appendTagData("subscribe");
                        if (sAsk.equals("U")) xdbe.appendTagData("unsubscribe");
                        xdbe.appendTagData("' ");
                    }
                    xdbe.appendTagData(">");
                    sSQL = "SELECT * FROM ROSTERGROUPS WHERE USERNAME = '" + packet.getToAddress() + "' AND JID = '" + sJID + "';";
                    Connection groupConnection = Configuration.getConnection();
                    Statement groupStatement = groupConnection.createStatement();
                    ResultSet groupResultSet = groupStatement.executeQuery(sSQL);
                    while (groupResultSet.next()) {
                        String sGroup = groupResultSet.getString("grp");
                        xdbe.appendTagData("<group>");
                        xdbe.appendTagData(sGroup);
                        xdbe.appendTagData("</group>");
                    }
                    groupStatement.close();
                    Configuration.freeConnection(groupConnection);
                    xdbe.appendTagData("</item>");
                }
                xdbe.appendTagData("</query>");
                selectStatement.close();
                response.setExtension(xdbe);
            } catch (SQLException se) {
                Configuration.log("jabber:iq:roster: Unable to retrieve record");
                response.setErrorMessage(true);
                response.setErrorText("jabber:iq:roster: Unable to retrieve record");
                response.setErrorCode("503");
                return response;
            }
        }
        Configuration.freeConnection(con);
        return response;
    }
}
