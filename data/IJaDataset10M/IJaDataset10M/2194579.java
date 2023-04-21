package drjava.smyle.tests;

import java.util.*;
import junit.framework.*;
import drjava.smyle.core.*;
import drjava.smyle.testtypes.*;

public class MemoryBTreeTest extends BTreeTestBase {

    public MemoryBTreeTest(String name) {
        super(name);
    }

    protected BTree<String, String> createTree() {
        return new MemoryBTree<String, String>(m, comparator);
    }
}
