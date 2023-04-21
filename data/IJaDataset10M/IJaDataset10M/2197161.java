package matrixCore;

import java.io.Serializable;

public class ArrayListStorageFactory implements DataStorageFactory, Serializable {

    @Override
    public MatrixDataStorage getStorage() {
        return new ArrayListStorage();
    }
}
