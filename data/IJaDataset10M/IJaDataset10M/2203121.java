package org.ximtec.igesture.storage;

import java.io.File;
import org.ximtec.igesture.configuration.Configuration;
import org.ximtec.igesture.core.GestureSet;
import org.ximtec.igesture.core.TestSet;

public class StorageEngineConverter {

    public void convert(File source, File target) {
        StorageEngine sourceEngine = StorageManager.createStorageEngine(source);
        StorageEngine targetEngine = StorageManager.createStorageEngine(target);
        for (GestureSet gestureSet : sourceEngine.load(GestureSet.class)) {
            targetEngine.store(gestureSet);
        }
        for (TestSet gestureSet : sourceEngine.load(TestSet.class)) {
            targetEngine.store(gestureSet);
        }
        for (Configuration gestureSet : sourceEngine.load(Configuration.class)) {
            targetEngine.store(gestureSet);
        }
        sourceEngine.dispose();
        targetEngine.commit();
        targetEngine.dispose();
    }
}
