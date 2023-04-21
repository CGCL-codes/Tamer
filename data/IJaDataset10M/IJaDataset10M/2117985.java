package org.jpos.iso.filter;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import org.jpos.core.Configurable;
import org.jpos.core.Configuration;
import org.jpos.core.ConfigurationException;
import org.jpos.core.NodeConfigurable;
import org.jpos.iso.ISOChannel;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOFilter;
import org.jpos.iso.ISOMsg;
import org.jpos.tpl.ConnectionPool;
import org.jpos.util.JepUtil;
import org.jpos.util.LogEvent;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * SQLFilter useful to execute sql statement.
 * @author <a href="mailto:tzymail@163.com">Zhiyu Tang</a>
 * @version $Revision: 1745 $ $Date: 2003-10-13 07:04:20 -0400 (Mon, 13 Oct 2003) $
 */
public class SQLFilter implements ISOFilter, Configurable, NodeConfigurable {

    ConnectionPool connect = null;

    Configuration cfg;

    String SQLUpdate[];

    String condition[];

    JepUtil jeputil;

    int condNum;

    public SQLFilter() {
        super();
        condNum = 0;
        SQLUpdate = null;
        condition = null;
        jeputil = new JepUtil();
    }

    /**
    * @param cfg
    * <ul>
    *  <li>ConnectionPool - connectionpool
    * </ul>
    */
    public void setConfiguration(Configuration cfg) throws ConfigurationException {
        this.cfg = cfg;
        try {
            this.connect = ConnectionPool.getConnectionPool(cfg.get("ConnectionPool", null));
        } catch (Exception e) {
            throw new ConfigurationException(e);
        }
    }

    public void setConfiguration(Node node) throws ConfigurationException {
        NodeList nodes = node.getChildNodes();
        condNum = nodes.getLength();
        int i, j = 0;
        for (i = 0; i < condNum; i++) {
            if (nodes.item(i).getNodeName().equals("SQL")) j++;
        }
        condition = new String[j];
        SQLUpdate = new String[j];
        j = 0;
        for (i = 0; i < condNum; i++) {
            if (nodes.item(i).getNodeName().equals("SQL")) {
                condition[j] = nodes.item(i).getAttributes().getNamedItem("switch").getNodeValue();
                SQLUpdate[j] = nodes.item(i).getAttributes().getNamedItem("statement").getNodeValue();
                j++;
            }
        }
        condNum = j;
    }

    private void SQLStatement(ISOMsg m, LogEvent evt) throws ISOException {
        Connection engine = null;
        try {
            engine = connect.getConnection();
            Statement s = engine.createStatement();
            for (int i = 0; i < condNum; i++) {
                if (jeputil.getResultBoolean(m, condition[i])) s.executeUpdate(jeputil.replaceMacro(m, SQLUpdate[i]));
            }
            connect.free(engine);
        } catch (SQLException ex) {
            throw new ISOException(ex);
        }
    }

    public ISOMsg filter(ISOChannel channel, ISOMsg m, LogEvent evt) throws VetoException {
        if (SQLUpdate != null) {
            try {
                SQLStatement(m, evt);
            } catch (Exception e) {
                evt.addMessage(e);
                throw new VetoException(e);
            }
        }
        return m;
    }
}
