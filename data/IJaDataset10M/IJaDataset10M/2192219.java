package com.nonesole.persistence.connectionpool.parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import com.nonesole.persistence.connectionpool.ConnectionsConstant;
import com.nonesole.persistence.exception.OperationsException;
import com.nonesole.persistence.main.FrameConfigConstruction;
import com.nonesole.persistence.main.FrameEnvService;
import com.nonesole.persistence.tools.ReflectToolkit;
import com.nonesole.persistence.tools.XMLHandler;

/**
 * Parse configuration of connection pool from xml files
 * @author JACK LEE
 * @version 1.0 - build in 2009-07-01
 */
public class ConnectionPoolConfigParser {

    private ConnectionPoolConfigParser() {
    }

    private static final String FILE_PATH;

    private static Map<String, ConnectionPoolProperties> CONNECTION_POOL_MAP;

    static {
        FILE_PATH = FrameEnvService.getInstance().getProperty(FrameConfigConstruction.DEFAULT_DB_CONFIG);
        try {
            CONNECTION_POOL_MAP = new HashMap<String, ConnectionPoolProperties>();
            parse();
        } catch (Exception e) {
            new OperationsException(ConnectionPoolConfigParser.class.toString() + " report : \n" + e.getMessage(), e.getCause());
        }
    }

    /**
	 * Parse xml files
	 * */
    private static final void parse() throws Exception {
        Document doc = XMLHandler.getInstance().loadFile(FILE_PATH);
        Node root = doc.getDocumentElement();
        List<Node> l = XMLHandler.getInstance().getNodes(root, ConnectionsConstant.CONNECTION_POOL_CONTAINER_NAME);
        if (null == l) throw new Exception("Config file has no config information.");
        for (Node n : l) {
            CONNECTION_POOL_MAP.put(n.getAttributes().getNamedItem(ConnectionsConstant.CONNECTION_POOL_NAME).getNodeValue(), (parseParameter(n)));
        }
    }

    /**
	 * Parse xml files and get DriverProperties object
	 * @param n Node 
	 * @return DriverProperties 
	 * */
    private static final ConnectionPoolProperties parseParameter(Node n) throws Exception {
        Map<String, Object> m = new HashMap<String, Object>();
        ConnectionPoolParserUtil.parseParameter(n, m, ConnectionsConstant.PARAMETER);
        ConnectionPoolProperties dp = (ConnectionPoolProperties) ReflectToolkit.parseMapToObject(m, ConnectionPoolProperties.class);
        dp.setPoolConfigName(n.getAttributes().getNamedItem(ConnectionsConstant.CONNECTION_POOL_NAME).getNodeValue());
        return dp;
    }

    /**
	 * Get connection pool 
	 * @param poolName-it must be as same as names in xml files
	 * @return ConnectionPoolProperties object
	 * */
    public static final ConnectionPoolProperties getConnectionPool(String poolName) {
        if (null == CONNECTION_POOL_MAP) return null;
        return CONNECTION_POOL_MAP.get(poolName);
    }
}
