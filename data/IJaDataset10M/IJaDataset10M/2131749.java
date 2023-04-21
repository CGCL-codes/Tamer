package org.nakedobjects.plugins.nosql;

import java.util.Iterator;
import java.util.List;
import org.nakedobjects.runtime.persistence.objectstore.transaction.PersistenceCommand;

public interface NoSqlDataDatabase {

    StateReader getInstance(String key, String specificationName);

    void close();

    void open();

    boolean containsData();

    void addService(String name, String key);

    String getService(String name);

    boolean hasInstances(String specificationName);

    Iterator<StateReader> instancesOf(String specificationName);

    long nextSerialNumberBatch(int batchSize);

    void write(List<PersistenceCommand> commands);
}
