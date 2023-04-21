package com.nonesole.persistence.connectionpool.parser;

import java.util.Map;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.nonesole.persistence.connectionpool.ConnectionsConstant;

/**
 * Tools of connection pool parser
 * @author JACK LEE
 * @version 1.0 - build in 2009-07-20 
 */
public class ConnectionPoolParserUtil {

    /**
	 * Only for connection pool parser��
	 * @param n Node object which include key,value property
	 * @param m result of parsing
	 * @param parameterFlag flag of Node
	 * */
    public static final void parseParameter(Node n, Map<String, Object> m, String parameterFlag) throws Exception {
        if (null == n || null == m || null == parameterFlag) {
            throw new NullPointerException("Parameter is null,so parsing parameter is failed.");
        }
        NodeList l = n.getChildNodes();
        for (int i = 1; i < l.getLength(); i++) {
            if ("parameter".equals(l.item(i).getNodeName())) parseParameter(l.item(i), m, ConnectionsConstant.PARAMETER_NAME, ConnectionsConstant.PARAMETER_VALUE);
        }
    }

    /**
	 * Only for connection pool parser��
	 * @param n - Node object which include key,value property
	 * @param m - result of parsing
	 * @param keyFlag - flag of Node object and key flag of map
	 * @param valueFlag - flag of Node object and value flag of map
	 * */
    private static final void parseParameter(Node n, Map<String, Object> m, String keyFlag, String valueFlag) throws Exception {
        if (null == n || null == m || null == keyFlag || null == valueFlag) throw new NullPointerException("Parameter is null,so parsing parameter is failed.");
        NodeList l = n.getChildNodes();
        String nodeName = null;
        int keyCount = 0;
        int valueCount = 0;
        String key = null;
        String value = null;
        for (int i = 0; i < l.getLength(); i++) {
            nodeName = l.item(i).getNodeName();
            if (keyFlag.equals(nodeName)) {
                key = l.item(i).getTextContent();
                keyCount++;
            } else if (valueFlag.equals(nodeName)) {
                value = l.item(i).getTextContent();
                valueCount++;
            }
        }
        if (keyCount != 1 && valueCount != 1) {
            throw new Exception("Searching parameter is failed." + "Cause the number of parameter is wrong.");
        } else if (null == key || null == value) {
            throw new NullPointerException("Parameter is null,so parse is failed.");
        }
        m.put(key, value);
    }
}
