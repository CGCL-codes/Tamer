package javaframework.datalayer.database;

import java.sql.SQLException;
import java.sql.Savepoint;

/**
 * Declaration of members used to execute an SQL modification query (INSERT, UPDATE or DELETE).
 * 
 * <br/><br/>
 *
 * <b><u>Dependencies</u></b><br/>
 * Base
 * <br/><br/>
 * 
 *
 * <b><u>Design notes</u></b><br/>
 * <b>· Creation time:</b>11/02/2012<br/>
 * <b>· Revisions:</b> -<br/><br/>
 * <b><u>State</u></b><br/>
 * <b>· Debugged:</b> Yes<br/>
 * <b>· Structural tests:</b> -<br/>
 * <b>· Functional tests:</b> -<br/>
 *
 * @author Francisco Pérez R. de V. (franjfw{@literal @}yahoo.es)
 * @version JavaFramework.0.1.0.en
 * @version InterfaceSQLModificationStatement.0.0.1
 * @since JavaFramework.0.1.0.en
 * @see <a href=””></a>
 *
 */
public interface InterfaceSQLModificationStatement extends InterfaceSQLStatement {

    /**
	 * Commits current transaction.
	 * @throws SQLException			If a database access exception occurs.
	 */
    public void consolidateTransaction() throws SQLException;

    /**
	 * Marks the state of the database as a savepoint.
	 * 
	 * @param savepointName			Name of the savepoint.
	 * @return						A <code>Savepoint</code> that represents the current state of the database.
	 * @throws SQLException			If a database access exception occurs.
	 */
    public Savepoint createSavepoint(final String savepointName) throws SQLException;

    /**
	 * Reverts modifications made to the database since the specified savepoint.
	 * @param savepoint				<code>Savepoint</code> that represents the state to which return.
	 * @throws SQLException			If a database access exception occurs.
	 */
    public void revertTransaction(final Savepoint savepoint) throws SQLException;

    /**
	 * Sets the transactional mode. By default transactional mode is deactivated ("auto commit" mode).
	 * In this mode, each single statement is automatically commited after its execution.
	 * 
	 * @param activated				<code>true</code> to activate transactional mode and allow to take
	 *								savepoints, commit or rollback,
	 * @throws SQLException			If a database access exception occurs.
	 */
    public void setTransactionalMode(final boolean activated) throws SQLException;

    /**
	 * Executes the specified modification query (insert, update or delete), returning the number 
	 * of affected rows.
	 * 
	 * @param queryParameters	The values of the parameters (if any) typed in the query as '?' characters.
	 *							If there are no parameter in the query pass <code>null</code>.
	 * @return
	 * @throws SQLException 
	 */
    public int execute(final QueryParameter... queryParameters) throws SQLException;
}
