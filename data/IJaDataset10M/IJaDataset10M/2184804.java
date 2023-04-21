package rice.persistence.testing;

import java.util.*;
import rice.*;
import rice.persistence.*;

/**
 * This class is a class which tests the PersistentStorage class
 * in the rice.persistence package.
 */
public class PersistentStorageTest extends MemoryStorageTest {

    /**
   * Builds a MemoryStorageTest
   */
    public PersistentStorageTest(boolean store) {
        super(store);
        storage = new PersistentStorage(".", 20000000);
    }

    public static void main(String[] args) {
        boolean store = true;
        if (args.length > 0) {
            store = !args[0].equals("-nostore");
        }
        PersistentStorageTest test = new PersistentStorageTest(store);
        test.start();
    }
}
