package com.nonesole.persistence.connectionpool.parser;

import java.util.ArrayList;
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
 * Parse Configuration of DataSource from xml files
 * @author JACK LEE
 * @version 1.0 - build in 2009-07-01
 */
public class DataSourceConfigParser {

    private DataSourceConfigParser() {
    }

    private static final String FILE_PATH;

    private static List<DriverProperties> DATA_SOURCE_LIST;

    static {
        FILE_PATH = FrameEnvService.getInstance().getProperty(FrameConfigConstruction.DEFAULT_DB_CONFIG);
        try {
            DATA_SOURCE_LIST = new ArrayList<DriverProperties>();
            parse();
        } catch (Exception e) {
            new OperationsException(DataSourceConfigParser.class.toString() + " report : \n" + e.getMessage(), e.getCause());
        }
    }

    /**
	 * Parse xml files
	 * */
    private static final void parse() throws Exception {
        Document doc = XMLHandler.getInstance().loadFile(FILE_PATH);
        Node root = doc.getDocumentElement();
        List<Node> l = XMLHandler.getInstance().getNodes(root, ConnectionsConstant.DATASOURCE_CONTAINER_NAME);
        if (null == l) throw new Exception("Config file has no config information.");
        DriverProperties dp = null;
        for (Node n : l) {
            dp = parseParameter(n);
            dp.setDataSourceName(n.getAttributes().getNamedItem(ConnectionsConstant.DATASOURCE_NAME).getNodeValue());
            DATA_SOURCE_LIST.add(dp);
        }
    }

    /**
	 * Get DriverProperties object from parsing xml files
	 * @param n Node
	 * @return DriverProperties
	 * */
    private static final DriverProperties parseParameter(Node n) throws Exception {
        Map<String, Object> m = new HashMap<String, Object>();
        ConnectionPoolParserUtil.parseParameter(n, m, ConnectionsConstant.PARAMETER);
        return (DriverProperties) ReflectToolkit.parseMapToObject(m, DriverProperties.class);
    }

    /**
	 * Get configuration list of DataSource
	 * @return list of DriverProperties
	 * */
    public static final List<DriverProperties> getDataSources() {
        return DATA_SOURCE_LIST;
    }
}
