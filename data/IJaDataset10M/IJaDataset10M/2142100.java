package com.carbonfive.flash;

import java.util.*;
import org.w3c.dom.*;
import org.apache.xerces.dom.*;
import junit.framework.*;
import flashgateway.io.ASObject;

public class CollectionDecoderTest extends TestCase {

    /**
   * This contructor provides a new CollectionDecoderTest.
   * </p>
   * @param name The String needed to build this object
   */
    public CollectionDecoderTest(String name) {
        super(name);
    }

    /**
   * Builds the test suite using introspection.
   * </p>
   * @return Test - The Test to be returned
   */
    public static Test suite() {
        TestSuite suite = new TestSuite(CollectionDecoderTest.class);
        return suite;
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public void testDecodeObject() throws Exception {
        ArrayList list = new ArrayList();
        list.add("one");
        list.add("two");
        CollectionDecoder decoder = new CollectionDecoder();
        assertTrue(decoder.decodeObject(list, List.class) instanceof ArrayList);
        assertTrue(decoder.decodeObject(list, ArrayList.class) instanceof ArrayList);
        assertTrue(decoder.decodeObject(list, Set.class) instanceof HashSet);
        assertTrue(decoder.decodeObject(list, HashSet.class) instanceof HashSet);
        assertTrue(decoder.decodeObject(list, Collection.class) instanceof ArrayList);
    }
}
