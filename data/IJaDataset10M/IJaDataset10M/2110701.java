package com.sleepycat.db;

import com.sleepycat.db.internal.DbEnv;

public class ReplicationHandleDeadException extends DatabaseException {

    ReplicationHandleDeadException(final String s, final int errno, final DbEnv dbenv) {
        super(s, errno, dbenv);
    }
}
