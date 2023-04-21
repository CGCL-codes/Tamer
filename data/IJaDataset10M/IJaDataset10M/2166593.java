package org.ozoneDB.collections;

import java.util.Collection;
import java.util.TreeSet;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import org.ozoneDB.OzonePersonalMetaData;

/**
 * @author <a href="mailto:ozoneATmekenkampD0Tcom">Leo Mekenkamp (mind the anti-sp@m)</a>
 */
public class NodeTreeSetTest extends OzoneCollectionTest {

    /** Creates a new instance of FullTreeSetTest */
    public NodeTreeSetTest(String name) {
        super(name);
    }

    public static void main(String[] args) throws Exception {
        TestRunner testRunner = new TestRunner();
        testRunner.run(NodeTreeSetTest.class);
    }

    protected Collection createRef() {
        return new TreeSet();
    }

    protected Collection createCmp() throws Exception {
        return NodeTreeSetImplFactory.getDefault().create();
    }

    protected Collection createCmp(String name) throws Exception {
        OzonePersonalMetaData meta = new OzonePersonalMetaData();
        meta.setName(name);
        return NodeTreeSetImplFactory.getDefault().create(meta, null);
    }
}
