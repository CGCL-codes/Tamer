package com.volantis.testtools.mock.libraries.java.util;

import com.volantis.testtools.mock.test.MockTestCaseAbstract;
import mock.java.util.CollectionMock;
import mock.java.util.IteratorMock;
import mock.java.util.ListMock;
import mock.java.util.MapMock;
import mock.java.util.SetMock;

/**
 * Tests for util related mock objects.
 */
public class UtilTestCase extends MockTestCaseAbstract {

    /**
     * Tests that all the mock objects can be initialised correctly.
     */
    public void testInitialisation() {
        new CollectionMock("collection", expectations);
        new ListMock("list", expectations);
        new SetMock("set", expectations);
        new IteratorMock("iterator", expectations);
        new MapMock("map", expectations);
    }

    /**
     * Ensure that clashing fuzzy methods are correctly handled.
     */
    public void testListExpectations() {
        ListMock listMock = new ListMock("listMock", expectations);
        listMock.fuzzy.remove(ListMock._getMethodIdentifier("remove(java.lang.Object)"), this).returns(false);
        assertFalse(listMock.remove(this));
        listMock.fuzzy.remove(ListMock._getMethodIdentifier("remove(int)"), new Integer(4)).returns(this);
        assertSame(this, listMock.remove(4));
    }
}
