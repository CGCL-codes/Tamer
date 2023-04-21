package com.liferay.portal.kernel.annotation;

import java.sql.Connection;

/**
 * <a href="TransactionDefinition.java.html"><b><i>View Source</i></b></a>
 *
 * @author Michael Young
 *
 */
public interface TransactionDefinition {

    public static final int ISOLATION_DEFAULT = -1;

    public static final int ISOLATION_READ_COMMITTED = Connection.TRANSACTION_READ_COMMITTED;

    public static final int ISOLATION_READ_UNCOMMITTED = Connection.TRANSACTION_READ_UNCOMMITTED;

    public static final int ISOLATION_REPEATABLE_READ = Connection.TRANSACTION_REPEATABLE_READ;

    public static final int ISOLATION_SERIALIZABLE = Connection.TRANSACTION_SERIALIZABLE;

    public static final int PROPAGATION_MANDATORY = 2;

    public static final int PROPAGATION_NESTED = 6;

    public static final int PROPAGATION_NEVER = 5;

    public static final int PROPAGATION_NOT_SUPPORTED = 4;

    public static final int PROPAGATION_REQUIRED = 0;

    public static final int PROPAGATION_REQUIRES_NEW = 3;

    public static final int PROPAGATION_SUPPORTS = 1;

    public static final int TIMEOUT_DEFAULT = -1;
}
