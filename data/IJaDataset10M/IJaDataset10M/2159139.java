package com.thoughthole.util.hoarder.core;

import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.ResultSet;
import com.thoughthole.util.hoarder.workflow.WorkflowTextResource;
import com.thoughthole.util.hoarder.workflow.WorkflowBinaryResource;
import com.thoughthole.util.andalay.Access;
import com.thoughthole.util.andalay.ExecutionEngine;
import com.thoughthole.util.andalay.Query;
import org.apache.log4j.Logger;

/**
 * @author Jason I. Oh
 */
public class HoarderIdBroker implements Serializable {

    private static Logger logger = Logger.getLogger(HoarderIdBroker.class);

    static final long serialVersionUID = 4667507339070245253L;

    private static HoarderIdBroker INSTANCE;

    private static List resourceAttribute = new ArrayList();

    private static List resourceOrderBy = new ArrayList();

    private static Access textAccess = null;

    private static Access binaryAccess = null;

    private static ExecutionEngine exEn = null;

    private static HoarderIdBrokerTranslator translator = null;

    private HoarderIdBroker() {
        exEn = ExecutionEngine.getEngine();
    }

    public static synchronized HoarderIdBroker getInstance() {
        if (INSTANCE == null) INSTANCE = new HoarderIdBroker();
        return INSTANCE;
    }

    static {
        translator = HoarderIdBrokerTranslator.getInstance();
        textAccess = new Access(HoarderContentManager.getProps().getProperty("hoarder.resource.text.db.table"));
        binaryAccess = new Access(HoarderContentManager.getProps().getProperty("hoarder.resource.binary.db.table"));
        resourceAttribute.add("resourceId");
        resourceOrderBy.add("resourceId ASC");
    }

    public static Long getId(Object obj) {
        int counter = 0;
        Long index = null;
        Long id = null;
        ResultSet rs = null;
        Statement stmt = null;
        if (obj instanceof WorkflowTextResource) {
            Connection conn = ExecutionEngine.getConnection();
            try {
                stmt = conn.createStatement();
                rs = stmt.executeQuery(((Query) textAccess.select(resourceAttribute, null, resourceOrderBy)).getQuery());
                while (rs.next()) {
                    if (counter == 0) {
                        index = new Long(rs.getString(1));
                        counter++;
                    }
                    if (!new Long(rs.getString(1)).equals(index)) {
                        return index;
                    } else {
                        index = new Long(index.longValue() + 1);
                    }
                }
                return index;
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                ExecutionEngine.returnConnection(conn);
                try {
                    stmt.close();
                } catch (Exception ignore) {
                }
            }
            logger.info("Assigned id: " + id + " to text resource");
        } else if (obj instanceof WorkflowBinaryResource) {
            id = translator.translate(exEn.execute(binaryAccess.select(resourceAttribute, null, resourceOrderBy)));
            logger.info("Assigned id: " + id + " to binary resource");
        } else {
            logger.error("unknown Object type: " + obj);
            return null;
        }
        return id;
    }
}
