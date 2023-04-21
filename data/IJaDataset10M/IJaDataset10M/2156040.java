package org.mitre.mrald.webservices;

import java.io.StringWriter;
import java.rmi.RemoteException;
import java.sql.ResultSet;
import javax.sql.rowset.WebRowSet;
import org.mitre.mrald.util.Config;
import org.mitre.mrald.util.MiscUtils;
import org.mitre.mrald.util.MraldConnection;
import org.mitre.mrald.util.RowSetFactory;

public class HandleBroadcastQuery {

    public String handleBroadcastQuery(String datasource, String query) throws Exception {
        if (!query.toLowerCase().trim().startsWith("select ")) {
            throw new RemoteException(Config.getProperty("BaseUrl") + ": You may only perform queries through this interface.  Received: " + query);
        }
        MraldConnection conn = new MraldConnection(datasource);
        StringBuffer logInfo = new StringBuffer();
        long startTime = MiscUtils.logQuery("remote MRALD", datasource, query, logInfo);
        ResultSet rs = conn.executeQuery(MiscUtils.clearSemiColon(query));
        MiscUtils.logQueryRun(startTime, logInfo);
        WebRowSet wrs = RowSetFactory.createWebRowSet();
        StringWriter xmlString = new StringWriter();
        wrs.writeXml(rs, xmlString);
        return xmlString.toString();
    }
}
