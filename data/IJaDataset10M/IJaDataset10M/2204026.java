package mipt.data.store.table;

import mipt.data.DataFactory;
import mipt.data.store.DataID;
import mipt.data.store.PersistentData;
import mipt.data.store.impl.DefaultPersistentData;
import mipt.data.store.impl.DefaultSwizzlingData;
import mipt.data.store.impl.StatelessPersistentData;

/**
 * Default implementation of PersistentDataFactory using DataStorage and/or DataLoader.
 * It does not use DataTableSettings so it creates:
 *  1) DefaultPersistentData if storage is NOT null and loader is null.
 *  2) DefaultSwizzlingData (with contents if setCreateSwizzlingDataContents is called*)
 *    if storage is NOT null and loader is NOT null.
 *  3) StatelessPersistentData if storage is null and loader is NOT null
 *     (however you may often want to set storage yourself!).
 * Storage and loader CAN'T BE NULL SIMULTANEOUSLY.
 * In cases 1 and 2* DataFactory is used to create contents.
 * Note: getDataType is not abstract but should be overridden if type information can't be
 *   extracted from DataID (that is sent to create*Data).
 */
public class DefaultPersistentDataFactory extends PersistentDataFactory {

    protected DataStorage storage;

    protected DataLoader loader;

    private boolean createSwizzlingDataContents = false;

    /**
	 * 
	 */
    public DefaultPersistentDataFactory() {
    }

    /**
	 * 
	 */
    public DefaultPersistentDataFactory(DataStorage storage, DataLoader loader, DataFactory dataFactory) {
        super(dataFactory);
        setStorage(storage);
        setLoader(loader);
    }

    /**
	 * 
	 * @param newStorage mipt.data.store.DataStorage
	 */
    public void setStorage(DataStorage newStorage) {
        storage = newStorage;
    }

    /**
	 * @param loader
	 */
    public void setLoader(DataLoader loader) {
        this.loader = loader;
    }

    /**
	 * 
	 * @return mipt.data.store.DataStorage
	 */
    public final DataStorage getStorage() {
        return storage;
    }

    /**
	 * @return loader
	 */
    public final DataLoader getLoader() {
        return loader;
    }

    public final boolean shouldCreateSwizzlingDataContents() {
        return createSwizzlingDataContents;
    }

    /**
	 * Call this method with true to fill SwizzlinData with contents during its creation.
	 * @param createSwizzlingDataContents (false by default)
	 */
    public void setCreateSwizzlingDataContents(boolean createSwizzlingDataContents) {
        this.createSwizzlingDataContents = createSwizzlingDataContents;
    }

    /**
	 * @see mipt.data.store.table.PersistentDataFactory#createData(mipt.data.store.DataID, java.lang.Object[], java.lang.String)
	 */
    public PersistentData createData(DataID id, Object[] fields, String name) {
        if (storage != null) {
            if (loader != null) return new DefaultSwizzlingData(id, storage, loader, shouldCreateSwizzlingDataContents() ? createContents(id, fields) : null, name); else return new DefaultPersistentData(id, storage, createContents(id, fields));
        } else {
            return new StatelessPersistentData(id, null, loader, name);
        }
    }

    /**
	 * Assumes that either storage or loader is not null.
	 */
    public EntityMetaData getMetaData() {
        DataTable table = storage;
        if (table == null) table = (DataTable) loader;
        return table.getMetaData();
    }
}
