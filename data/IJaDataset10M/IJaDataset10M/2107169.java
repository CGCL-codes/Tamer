package com.apelon.dts.setup;

import com.apelon.apelonserver.client.ServerConnection;
import com.apelon.apelonserver.client.ServerConnectionJDBC;
import com.apelon.apelonserver.client.ServerConnectionSecureSocket;
import com.apelon.apelonserver.client.ServerConnectionSocket;
import com.apelon.common.sql.ConnectionParams;
import com.apelon.common.util.db.ParseConfig;
import com.apelon.common.util.db.dtd.DTD;
import java.util.Map;

/**
 * Creates connection based on the connection properties file.
 * <p/>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Apelon, Inc.</p>
 *
 * @author Abhay Sinha
 * @version DTS 3.4.0
 * @since 3.4.0
 */
public class DTSConnectionMgr {

    /**
   * Static method to create connection. This will be use in Tests where connection is done
   * once for all tests.
   * @return
   * @throws Exception
   */
    public static ServerConnection createConnection() throws Exception {
        ServerConnection sc;
        String username;
        String password;
        String hostname;
        String port;
        String scType = System.getProperty("sc.type", "jdbc");
        if (scType.equalsIgnoreCase("jdbc")) {
            String connFilePath = System.getProperty("connection.file", null);
            if (connFilePath == null) {
                throw new Exception("Property connection.file should be set to target-connection.xml");
            }
            ConnectionParams cps = parseConnection(connFilePath);
            sc = new ServerConnectionJDBC(cps);
        } else if (scType.equalsIgnoreCase("securesocket")) {
            username = System.getProperty("sc.user", "dtsjunit");
            password = System.getProperty("sc.pass", "dtsjunit");
            hostname = System.getProperty("sc.host", "localhost");
            port = System.getProperty("sc.port", "6666");
            sc = new ServerConnectionSecureSocket(hostname, Integer.parseInt(port), username, password);
        } else if (scType.equalsIgnoreCase("socket")) {
            hostname = System.getProperty("sc.host", "localhost");
            port = System.getProperty("sc.port", "6666");
            sc = new ServerConnectionSocket(hostname, Integer.parseInt(port));
        } else {
            throw new Exception("Invalid Server Connection Type :" + scType);
        }
        return sc;
    }

    private static ConnectionParams parseConnection(String connFile) {
        ConnectionParams cps = null;
        try {
            ParseConfig connParseConfig = new ParseConfig(DTD.CONNECTION_URL, DTD.class, DTD.CONNECTION_FILE);
            Map connParamsMap = connParseConfig.parseForConnection(connFile);
            cps = (ConnectionParams) connParamsMap.get(new Integer(ParseConfig.TARGET_INDEX));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cps;
    }
}
