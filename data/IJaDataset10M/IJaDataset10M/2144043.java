package org.xmi.infoset.repository;

import junit.framework.TestCase;
import org.xmi.repository.RepositoryDriverEnumeration;

public class RepositoryTypesTest extends TestCase {

    public void testGetName() {
        assertEquals("org.xmi.repository.driver.jdbm.JDBMModelDriver", RepositoryDriverEnumeration.JDBM.getName());
    }

    public void testGetType() {
        assertEquals(RepositoryDriverEnumeration.JDBM, RepositoryDriverEnumeration.getType("org.xmi.repository.driver.jdbm.JDBMModelDriver"));
    }
}
