package org.eclipse.datatools.sqltools.core.services;

/**
 * An action related service specific to control the actions
 * @author Hui Wan
 *
 */
public class ActionService {

    /**
	 * Checks whether the action is supported. The action id could be:
	 * <code>ISQLEditorActionConstants.EXPLAIN_SQL_ACTION_ID</code>
     * <code>ISQLEditorActionConstants.EXECUTE_SQL_ACTION_ID</code>
     * <code>ISQLEditorActionConstants.EXECTUE_SELECTION_SQL_ACTION_ID</code>
     * <code>ISQLEditorActionConstants.GROUP_NEW_ROUTINE_ID</code>
     * <code>ISQLEditorActionConstants.GROUP_INSERT_QUERY_ID</code>
     * <code>ISQLEditorActionConstants.RUN_ACTION_ID</code>
     * <code>ISQLEditorActionConstants.DEBUG_ACTION_ID</code>
     * <code>ISQLEditorActionConstants.CREATE_DATABASE_ACTION_ID</code>
     * <code>ISQLEditorActionConstants.RENAME_ACTION_ID</code>
	 * 
	 * @param actionId
	 *            Action id
	 * @return If support, return true, else return false
	 */
    public boolean supportsAction(String actionId) {
        return true;
    }
}
