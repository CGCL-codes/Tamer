package com.nexirius.framework.datamodel.persistence;

import com.nexirius.framework.datamodel.DataModel;
import com.nexirius.framework.datamodel.DataModelEnumeration;
import com.nexirius.framework.datamodel.DataModelVector;
import com.nexirius.util.StringVector;

/**
 * This interface is used a for storing and loading datamodels. It also holds
 * a list of PersistenceEnvironment.
 *
 * @author Marcel Baumann
 */
public interface PersistenceManager {

    public PersistenceEnvironment getEnvironment(String name) throws Exception;

    public void addEnvironment(PersistenceEnvironment e) throws Exception;

    public boolean isOpen(String environmentName, String identifier);

    /**
     * Get a model from the list of open models
     *
     * @return null if model is not open
     */
    public DataModel getOpenModel(String environmentName, String identifier);

    public DataModelEnumeration getNotSaved();

    public void store(DataModel model, String environmentName) throws Exception;

    public DataModel load(String environmentName, String identifier) throws Exception;

    public void remove(String environmentName, String identifier) throws Exception;

    public void close(String environmentName, DataModel model, boolean looseChanges) throws Exception;

    public void storeAll() throws Exception;

    public DataModelVector loadAll(String environmentName) throws Exception;

    public StringVector getEnvironments();

    void save(DataModel model, String environment) throws Exception;
}
