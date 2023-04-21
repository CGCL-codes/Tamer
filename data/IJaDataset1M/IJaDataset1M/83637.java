package org.avaje.ebean.server.core;

import java.util.ArrayList;
import org.avaje.ebean.CallableSql;
import org.avaje.ebean.ServerConfiguration;
import org.avaje.ebean.SqlUpdate;
import org.avaje.ebean.server.transaction.TransactionEvent;
import org.avaje.ebean.util.BindParams;

/**
 * Access to the methods hidden by public API protection.
 */
public class ProtectedMethod {

    private static ProtectedMethodAPI pa;

    /**
     * Set the implementation.
     */
    public static void setPublicAccess(ProtectedMethodAPI publicAccess) {
        pa = publicAccess;
    }

    /**
     * Return the BindParams for a UpdateSql.
     */
    public static BindParams getBindParams(SqlUpdate updSql) {
        return pa.getBindParams(updSql);
    }

    /**
     * Return the BindParams for a CallableSql.
     */
    public static BindParams getBindParams(CallableSql callSql) {
        return pa.getBindParams(callSql);
    }

    /**
     * Return the TransactionEvent for a CallableSql.
     */
    public static TransactionEvent getTransactionEvent(CallableSql callSql) {
        return pa.getTransactionEvent(callSql);
    }

    /**
     * Return the classes (entities, embeddable, finders, scalar types etc). 
     */
    public static ArrayList<Class<?>> getClasses(ServerConfiguration serverConfig) {
        return pa.getClasses(serverConfig);
    }
}
