package org.avaje.ebean;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import org.avaje.ebean.server.transaction.TransactionEvent;
import org.avaje.ebean.util.BindParams;
import org.avaje.ebean.util.BindParams.Param;

/**
 * For making calls to stored procedures. Refer to the Ebean execute() method.
 * <p>
 * Note that UpdateSql is designed for general DML sql and CallableSql is
 * designed for use with stored procedures. Also note that when using this in
 * batch mode the out parameters are not read.
 * </p>
 * <p>
 * Example 1:
 * </p>
 * 
 * <pre class="code">
 * String sql = &quot;{call sp_order_mod(?,?)}&quot;;
 * 
 * CallableSql cs = new CallableSql();
 * cs.setSql(sql);
 * cs.setParameter(1, &quot;turbo&quot;);
 * cs.registerOut(2, Types.INTEGER);
 * 
 * Ebean.execute(cs);
 * 
 * // read the out parameter
 * Integer returnValue = (Integer) cs.getObject(2);
 * </pre>
 * 
 * <p>
 * Example 2:<br>
 * Includes batch mode, table modification information and label. Note that the
 * label is really only to help people reading the transaction logs to identify
 * the procedure called etc.
 * </p>
 * 
 * <pre class="code">
 * String sql = &quot;{call sp_insert_order(?,?)}&quot;;
 * 
 * CallableSql cs = new CallableSql();
 * cs.setSql(sql);
 * 
 * // the label appears in the transaction log
 * cs.setLabel(&quot;Silly Example&quot;);
 * 
 * // Inform Ebean this stored procedure inserts into the
 * // oe_order table and inserts + updates the oe_order_detail table.
 * // this is used to invalidate objects in the cache
 * cs.addModification(&quot;oe_order&quot;, true, false, false);
 * cs.addModification(&quot;oe_order_detail&quot;, true, true, false);
 * 
 * Transaction t = Ebean.startTransaction();
 * 
 * // execute using JDBC batching 10 statements at a time
 * t.setBatchMode(true);
 * t.setBatchSize(10);
 * try {
 * 	cs.setParameter(1, &quot;Was&quot;);
 * 	cs.setParameter(2, &quot;Banana&quot;);
 * 	Ebean.execute(cs);
 * 
 * 	cs.setParameter(1, &quot;Here&quot;);
 * 	cs.setParameter(2, &quot;Kumera&quot;);
 * 	Ebean.execute(cs);
 * 
 * 	cs.setParameter(1, &quot;More&quot;);
 * 	cs.setParameter(2, &quot;Apple&quot;);
 * 	Ebean.execute(cs);
 * 
 * 	//Ebean.externalModification(&quot;oe_order&quot;,true,false,false);
 * 	//Ebean.externalModification(&quot;oe_order_detail&quot;,true,true,false);
 * 	Ebean.commitTransaction();
 * 
 * } finally {
 * 	Ebean.endTransaction();
 * }
 * </pre>
 * 
 * @see org.avaje.ebean.SqlUpdate
 * @see org.avaje.ebean.Ebean#execute(CallableSql)
 */
public class CallableSql implements Serializable {

    static final long serialVersionUID = 8984272253185424701L;

    /**
	 * The callable sql.
	 */
    String sql;

    /**
	 * To display in the transaction log to help identify the procedure.
	 */
    String label;

    int timeout;

    /**
	 * The parameters to be bound and read later.
	 */
    ArrayList<Object> paramList = new ArrayList<Object>();

    /**
	 * Holds the table modification information. On commit this information is
	 * used to manage the cache etc.
	 */
    TransactionEvent transactionEvent = new TransactionEvent();

    BindParams bindParameters = new BindParams();

    /**
	 * Create with callable sql.
	 */
    public CallableSql(String sql) {
        this.sql = sql;
    }

    /**
	 * Create the CallableSql.
	 */
    public CallableSql() {
    }

    /**
	 * Return the label that is put into the transaction log.
	 */
    public final String getLabel() {
        return label;
    }

    /**
	 * Set the label that is put in the transaction log.
	 */
    public final CallableSql setLabel(String label) {
        this.label = label;
        return this;
    }

    /**
	 * Return the statement execution timeout.
	 */
    public final int getTimeout() {
        return timeout;
    }

    /**
	 * Return the callable sql.
	 */
    public final String getSql() {
        return sql;
    }

    /**
	 * Set the statement execution timeout. Zero implies unlimited time.
	 * <p>
	 * This is set to the underlying CallableStatement.
	 * </p>
	 */
    public final CallableSql setTimeout(int secs) {
        this.timeout = secs;
        return this;
    }

    /**
	 * Set the callable sql.
	 */
    public final CallableSql setSql(String sql) {
        this.sql = sql;
        return this;
    }

    /**
	 * Bind a parameter that is bound as a IN parameter.
	 * <p>
	 * position starts at value 1 (not 0) to be consistent with
	 * CallableStatement.
	 * </p>
	 * <p>
	 * This is designed so that you do not need to set params in index order.
	 * You can set/register param 2 before param 1 etc.
	 * </p>
	 * 
	 * @param position
	 *            the index position of the parameter.
	 * @param value
	 *            the value of the parameter.
	 */
    public final CallableSql bind(int position, Object value) {
        bindParameters.setParameter(position, value);
        return this;
    }

    /**
	 * Bind a positioned parameter (same as bind method).
	 * 
	 * @param position
	 *            the index position of the parameter.
	 * @param value
	 *            the value of the parameter.
	 */
    public final CallableSql setParameter(int position, Object value) {
        bindParameters.setParameter(position, value);
        return this;
    }

    /**
	 * Register an OUT parameter.
	 * <p>
	 * Note that position starts at value 1 (not 0) to be consistent with
	 * CallableStatement.
	 * </p>
	 * <p>
	 * This is designed so that you do not need to register params in index
	 * order. You can set/register param 2 before param 1 etc.
	 * </p>
	 * 
	 * @param position
	 *            the index position of the parameter (starts with 1).
	 * @param type
	 *            the jdbc type of the OUT parameter that will be read.
	 */
    public final CallableSql registerOut(int position, int type) {
        bindParameters.registerOut(position, type);
        return this;
    }

    /**
	 * Return an OUT parameter value.
	 * <p>
	 * position starts at value 1 (not 0) to be consistent with
	 * CallableStatement.
	 * </p>
	 * <p>
	 * This can only be called after the CallableSql has been executed. When run
	 * in batch mode you effectively can't use this method.
	 * </p>
	 */
    public final Object getObject(int position) {
        Param p = bindParameters.getParameter(position);
        return p.getOutValue();
    }

    /**
	 * 
	 * You can extend this object and override this method for more advanced
	 * stored procedure calls. This would be the case when ResultSets are
	 * returned etc.
	 */
    public boolean executeOverride(CallableStatement cstmt) throws SQLException {
        return false;
    }

    /**
	 * Add table modification information to the TransactionEvent.
	 * <p>
	 * This would be similar to using the
	 * <code>Ebean.externalModification()</code> method. It may be easier and
	 * make more sense to set it here with the CallableSql.
	 * </p>
	 * <p>
	 * For UpdateSql the table modification information is derived by parsing
	 * the sql to determine the table name and whether it was an insert, update
	 * or delete.
	 * </p>
	 */
    public final CallableSql addModification(String tableName, boolean inserts, boolean updates, boolean deletes) {
        transactionEvent.add(tableName, inserts, updates, deletes);
        return this;
    }

    /**
	 * Return the TransactionEvent which holds the table modification
	 * information for this CallableSql. This information is merged into the
	 * transaction after the transaction is commited.
	 */
    final TransactionEvent getTransactionEvent() {
        return transactionEvent;
    }

    final BindParams getBindParams() {
        return bindParameters;
    }
}
