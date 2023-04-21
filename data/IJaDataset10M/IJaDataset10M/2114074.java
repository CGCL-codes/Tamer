package org.subrecord.impl;

import java.io.File;
import java.io.Serializable;
import java.util.Map;
import org.subrecord.RecordManager;
import org.subrecord.exception.StorageException;
import org.subrecord.util.SerializationHelper;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;

/**
 * Oracle's BerkeleyDB JE implementation. Records are kept inside a dedicated
 * database. Each record is located under a composite key with serialized byte
 * stream as a value
 * 
 * @author przemek
 * 
 */
public class BdbRecordManager implements RecordManager {

    private Database database;

    private Environment environment;

    private String dataRoot;

    public BdbRecordManager(String dataRoot) {
        this.dataRoot = dataRoot;
    }

    @Override
    public void start() throws Exception {
        EnvironmentConfig envConfig = new EnvironmentConfig();
        envConfig.setAllowCreate(true);
        File f = new File(dataRoot);
        f.mkdirs();
        environment = new Environment(f, envConfig);
        DatabaseConfig dbConfig = new DatabaseConfig();
        dbConfig.setAllowCreate(true);
        database = environment.openDatabase(null, "subrecord", dbConfig);
    }

    @Override
    public void stop() throws Exception {
        if (database != null) {
            database.close();
        }
        if (environment != null) {
            environment.close();
        }
    }

    @Override
    public Map<String, Serializable> getRecord(Serializable domain, Serializable table, Serializable key) throws Exception {
        DatabaseEntry akey = new DatabaseEntry(SerializationHelper.serialize(domain, table, key));
        DatabaseEntry data = new DatabaseEntry();
        OperationStatus status = database.get(null, akey, data, LockMode.DEFAULT);
        if (status != OperationStatus.SUCCESS) {
            throw new StorageException("DB status=" + status);
        }
        return (Map<String, Serializable>) SerializationHelper.deserialize(data.getData());
    }

    @Override
    public boolean putRecord(Serializable domain, Serializable table, Serializable key, Map<String, Serializable> record) throws Exception {
        DatabaseEntry akey = new DatabaseEntry(SerializationHelper.serialize(domain, table, key));
        DatabaseEntry data = new DatabaseEntry(SerializationHelper.serialize(record));
        OperationStatus status = database.put(null, akey, data);
        return status == OperationStatus.SUCCESS;
    }

    @Override
    public boolean removeRecord(Serializable domain, Serializable table, Serializable key) throws Exception {
        DatabaseEntry akey = new DatabaseEntry(SerializationHelper.serialize(domain, table, key));
        database.removeSequence(null, akey);
        return true;
    }
}
