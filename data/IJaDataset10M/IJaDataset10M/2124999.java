package com.daffodilwoods.daffodildb.server.serversystem;

public interface SaveModeConstants {

    public static final int QUERY = 0;

    public static final int METHOD = 1;

    public static final int PREPARED = 0;

    public static final int NON_PREPARED = 1;

    public static final int COMMIT = 0;

    public static final int ROLLBACK = 1;

    public static final int SETSAVEPOINT = 2;

    public static final int RELEASESAVEPOINT = 3;

    public static final int COMMITSAVEPOINT = 4;

    public static final int ROLLBACKSAVEPOINT = 5;

    public static final int SETCOMMITON = 6;

    public static final int SETCOMMITOFF = 7;

    public static final int CLOSE = 8;

    public static final int STARTCONNECTION = 9;

    public static final String XML_FILENAME = "transaction.xml";

    public static final String LOG_FILEPREFIX = "log_";
}
