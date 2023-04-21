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

public class XDBNSList implements JabberModule {

    /**
	 * Handles all public viewable data.
	 * 
	 * @param XDB packet received from Jabber.
	 * @return XDBResponse used to build xdb response packet.
	 * @author: Matthew D. Diez
	 *
	 */
    public org.jabber.xdb.XDBResponse handlePacket(org.jabber.jabberbeans.serverside.XDB packet) {
        XDBResponse response = new XDBResponse();
        String sSQL;
        ResultSet rs;
        Connection con = Configuration.getConnection();
        String sNamespace = packet.getNamespace();
        if (packet.getType().equals("set")) {
        }
        if (packet.getType().equals("get")) {
            XDBExtension xdbe = new XDBExtension();
            sSQL = "SELECT DATA FROM USERS WHERE USERNAME = '" + packet.getToAddress() + "' AND NAMESPACE = '" + sNamespace + "';";
            try {
                Statement selectStatement = con.createStatement();
                rs = selectStatement.executeQuery(sSQL);
                while (rs.next()) {
                    String sData = rs.getString("DATA");
                    xdbe.setTagData(sData);
                    response.setExtension(xdbe);
                }
                selectStatement.close();
            } catch (SQLException se) {
                response.setErrorMessage(true);
                Configuration.log("SQLException in IQPublic" + se.toString());
                return response;
            }
        }
        Configuration.freeConnection(con);
        return response;
    }
}
