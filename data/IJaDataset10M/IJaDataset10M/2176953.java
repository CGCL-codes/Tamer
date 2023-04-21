package org.objectstyle.cayenne.dba.oracle;

import org.objectstyle.cayenne.dba.DbAdapter;
import org.objectstyle.cayenne.dba.JdbcActionBuilder;
import org.objectstyle.cayenne.map.EntityResolver;
import org.objectstyle.cayenne.query.BatchQuery;
import org.objectstyle.cayenne.query.ProcedureQuery;
import org.objectstyle.cayenne.query.SQLAction;
import org.objectstyle.cayenne.query.SelectQuery;

/**
 * @since 1.2
 * @author Andrus Adamchik
 */
class OracleActionBuilder extends JdbcActionBuilder {

    OracleActionBuilder(DbAdapter adapter, EntityResolver resolver) {
        super(adapter, resolver);
    }

    public SQLAction batchAction(BatchQuery query) {
        if (OracleAdapter.isSupportsOracleLOB() && OracleAdapter.updatesLOBColumns(query)) {
            return new OracleLOBBatchAction(query, getAdapter());
        } else {
            boolean useOptimisticLock = query.isUsingOptimisticLocking();
            boolean runningAsBatch = !useOptimisticLock && adapter.supportsBatchUpdates();
            OracleBatchAction action = new OracleBatchAction(query, getAdapter(), getEntityResolver());
            action.setBatch(runningAsBatch);
            return action;
        }
    }

    public SQLAction procedureAction(ProcedureQuery query) {
        return new OracleProcedureAction(query, getAdapter(), getEntityResolver());
    }

    public SQLAction objectSelectAction(SelectQuery query) {
        return new OracleSelectAction(query, getAdapter(), getEntityResolver());
    }
}
