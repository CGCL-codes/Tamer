package javax.sql;

import java.util.EventListener;

/**
 * An object that registers to be notified of events that occur on
 * PreparedStatements that are in the Statement pool.
 */
public interface StatementEventListener extends EventListener {

    /**
     * The driver calls this method on all StatementEventListeners registered on
     * the connection when it detects that a PreparedStatement is closed.
     * 
     * @param event
     *            an StatementEvent object describing the event of statement
     *            closed
     */
    public void statementClosed(StatementEvent event);

    /**
     * The driver calls this method on all StatementEventListeners registered on
     * the connection when it detects that a PreparedStatement is invalid,
     * before a SQLException is thrown
     * 
     * @param event
     *            an StatementEvent object describing the event of statement
     *            error occurred
     */
    public void statementErrorOccurred(StatementEvent event);
}
