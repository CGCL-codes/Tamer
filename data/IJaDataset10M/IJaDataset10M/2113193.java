package com.google.code.gwt.database.client.service.callback;

import com.google.code.gwt.database.client.SQLError;
import com.google.code.gwt.database.client.SQLTransaction;
import com.google.code.gwt.database.client.StatementCallback;
import com.google.code.gwt.database.rebind.DataServiceGenerator;
import com.google.gwt.core.client.JavaScriptObject;

/**
 * Used in the {@link DataServiceGenerator} to reduce generated boilerplate
 * code.
 * 
 * @author bguijt
 */
public abstract class DataServiceStatementCallback<T extends JavaScriptObject> implements StatementCallback<T> {

    /**
   * The onFailure method, once called, always signals the database to rollback
   * the transaction.
   * 
   * @return <code>true</code>
   */
    public boolean onFailure(SQLTransaction transaction, SQLError error) {
        storeError(error.getCode(), error.getMessage());
        return true;
    }

    /**
   * Stores the SQLError details from
   * {@link #onFailure(SQLTransaction, SQLError)} to the TransactionCallback in
   * charge.
   * 
   * <p>
   * Due to Java Generics' incapabilities we need to implement this method at
   * each subtype.
   * </p>
   * 
   * @param code the error code - see <a
   *          href="http://www.w3.org/TR/webdatabase/#sqlexception">W3C Web
   *          Database error codes</a>
   * @param message the error message
   */
    protected abstract void storeError(int code, String message);
}
