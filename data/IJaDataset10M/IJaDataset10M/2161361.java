package ch.ethz.dcg.spamato.db.utils;

import ch.ethz.dcg.thread.*;

public class DBThread extends TaskThread {

    private SQLUtils sqlUtils;

    public DBThread(ThreadPool threadPool, String connectionURL) {
        super(threadPool);
        sqlUtils = SQLUtils.createSQLUtils(connectionURL, true);
    }

    public SQLUtils getSQLUtils() {
        return sqlUtils;
    }
}
