package com.ixora.rms.agents.sqlserver;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import com.ixora.rms.CounterId;
import com.ixora.rms.EntityId;
import com.ixora.rms.agents.AgentExecutionContext;
import com.ixora.rms.agents.impl.Counter;
import com.ixora.rms.agents.impl.Entity;
import com.ixora.rms.agents.utils.SQLBasedEntity;
import com.ixora.rms.data.CounterValueDouble;

/**
 * SqlPerfInfo
 */
public class SqlPerfInfo extends SQLBasedEntity {

    private static final long serialVersionUID = 1531604518354399404L;

    /** Statement called within a loop to retrieve counters for an entity */
    private CallableStatement statGetCounters;

    private class SqlPerfEntity extends Entity {

        private static final long serialVersionUID = -7485282545338200597L;

        private String objectName;

        private String instanceName;

        private class SqlCounter extends Counter {

            private static final long serialVersionUID = -4316428992053831325L;

            public SqlCounter(String name) {
                super(name, name + ".desc");
            }
        }

        /**
		 * Creates a SQL entity under another parent SQL entity
		 * 
		 * @param parent
		 * @param id
		 * @param objectName
		 * @param instanceName
		 * @param ctxt
		 * @throws Throwable
		 */
        public SqlPerfEntity(SqlPerfEntity parent, EntityId id, String objectName, String instanceName, AgentExecutionContext ctxt) throws Throwable {
            this(id, objectName, instanceName, ctxt);
            parent.addChildEntity(this);
        }

        /**
		 * Creates a SQL entity under the root
		 * 
		 * @param id
		 * @param objectName
		 * @param instanceName
		 * @param ctxt
		 */
        public SqlPerfEntity(EntityId id, String objectName, String instanceName, AgentExecutionContext ctxt) {
            super(id, ctxt);
            this.objectName = objectName;
            this.instanceName = instanceName;
            try {
                statGetCounters.setString(1, objectName);
                statGetCounters.setString(2, instanceName);
                ResultSet rs = statGetCounters.executeQuery();
                while (rs.next()) {
                    SqlCounter c = new SqlCounter(rs.getString("counter_name").trim());
                    addCounter(c);
                }
                rs.close();
            } catch (SQLException e) {
                fContext.error(e);
            }
        }

        /**
		 * @see com.ixora.rms.agents.impl.EntityForest#testForChildren()
		 */
        protected boolean testForChildren() throws Throwable {
            return fChildrenEntities.size() > 0;
        }

        /**
		 * @see com.ixora.rms.agents.impl.Entity#retrieveCounterValues()
		 */
        protected void retrieveCounterValues() throws Throwable {
            if (!isConnected()) return;
            String strIn = new String();
            for (Iterator<Counter> it = fCounters.values().iterator(); it.hasNext(); ) {
                SqlCounter c = (SqlCounter) it.next();
                if (c.isEnabled()) {
                    if (strIn.length() != 0) strIn += ",";
                    strIn += "'" + c.getName() + "'";
                }
            }
            if (strIn.length() > 0) {
                Statement stat = getConnection(0).createStatement();
                ResultSet rs = stat.executeQuery("select counter_name, cntr_value " + "from sysperfinfo where object_name='" + objectName + "' " + "and instance_name='" + instanceName + "' " + "and counter_name in(" + strIn + ")");
                while (rs.next()) {
                    SqlCounter c = (SqlCounter) getCounter(new CounterId(rs.getString("counter_name").trim()));
                    if (c != null) c.dataReceived(new CounterValueDouble(rs.getInt("cntr_value")));
                }
                rs.close();
                stat.close();
            }
        }
    }

    /**
	 * Uses 2 database connections
	 */
    public SqlPerfInfo(AgentExecutionContext ctxt) {
        super(ctxt, 2);
    }

    /**
	 * Initializes/refreshes entity tree
	 * 
	 * @see com.ixora.rms.agents.impl.Entity#updateChildrenEntities(recursive)
	 */
    public void updateChildrenEntities(boolean recursive) throws Throwable {
        if (!isConnected()) return;
        try {
            statGetCounters = getConnection(1).prepareCall("select distinct counter_name from sysperfinfo where object_name=? and instance_name=?");
            String objectName, lastObjectName = "";
            String instanceName, lastInstanceName = "";
            SqlPerfEntity lastEntity = null;
            Statement stat = getConnection(0).createStatement();
            ResultSet rs = stat.executeQuery("select distinct object_name, instance_name " + "from sysperfinfo group by object_name, instance_name");
            while (rs.next()) {
                objectName = rs.getString("object_name").trim();
                instanceName = rs.getString("instance_name").trim();
                if (!objectName.equals(lastObjectName)) {
                    if (objectName.length() > 0) lastEntity = new SqlPerfEntity(new EntityId(getId(), objectName), objectName, instanceName, fContext); else lastEntity = new SqlPerfEntity(new EntityId(getId(), lastObjectName), lastObjectName, lastInstanceName, fContext);
                    addChildEntity(lastEntity);
                } else {
                    new SqlPerfEntity(lastEntity, new EntityId(lastEntity.getId(), instanceName), objectName, instanceName, fContext);
                }
                lastObjectName = objectName;
                lastInstanceName = instanceName;
            }
            rs.close();
            stat.close();
        } catch (Exception e) {
            fContext.error(e);
        }
    }
}
