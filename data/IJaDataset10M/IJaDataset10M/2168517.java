package org.joy.db;

import com.sleepycat.db.DatabaseConfig;
import com.sleepycat.db.DatabaseType;

/**
 *
 * @author 海
 */
public class TransactionalDBConfig extends DatabaseConfig {

    /** Creates a new instance of TransactionalDBConfig */
    public TransactionalDBConfig() {
        setType(DatabaseType.BTREE);
        setAllowCreate(true);
        setTransactional(true);
    }
}
