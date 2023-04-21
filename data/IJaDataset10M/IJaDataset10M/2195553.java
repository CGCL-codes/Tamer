package org.math.factory;

import org.math.data.storage.DoubleStorage;
import org.math.data.storage.FloatStorage;
import org.math.data.storage.IntegerStorage;
import org.math.data.storage.LongStorage;
import org.math.data.storage.ShortStorage;
import org.math.data.storage.Storage;
import org.math.data.storage.StorageType;

/**
 * package-private access. <code>DataValueFactory</code> uses it. Client code should not need
 * to invoke this factory.
 * @author Susanta Tewari <stewari@yahoo.com>
 */
class StorageFactory {

    static Storage create(StorageType storageTYpe) {
        Storage storage = null;
        switch(storageTYpe) {
            case DOUBLE:
                storage = new DoubleStorage();
                break;
            case FLOAT:
                storage = new FloatStorage();
                break;
            case INT:
                storage = new IntegerStorage();
                break;
            case LONG:
                storage = new LongStorage();
                break;
            case SHORT:
                storage = new ShortStorage();
                break;
        }
        return storage;
    }
}
