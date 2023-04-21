package org.ximtec.igesture.storage;

import java.util.List;
import org.ximtec.igesture.core.DataObject;

public interface IStorageManager extends StorageEngine {

    void store(List<DataObject> dataObjects);

    void update(List<DataObject> dataObjects);
}
