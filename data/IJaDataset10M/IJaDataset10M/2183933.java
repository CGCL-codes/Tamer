package org.itracker.model;

import static org.junit.Assert.assertNotNull;
import static org.itracker.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ProjectScriptTest {

    private ProjectScript pro;

    @Test
    public void testToString() {
        assertNotNull("toString", pro.toString());
    }

    @Test
    public void testFieldPriorityComparator() {
        ProjectScript entityA = new ProjectScript();
        ProjectScript entityB = new ProjectScript();
        entityA.setFieldId(0);
        entityB.setFieldId(1);
        entityA.setPriority(0);
        entityB.setPriority(0);
        assertEntityComparator("field priority comparator", ProjectScript.FIELD_PRIORITY_COMPARATOR, entityA, entityB);
        assertEntityComparator("field priority comparator", ProjectScript.FIELD_PRIORITY_COMPARATOR, entityA, null);
        entityA.setFieldId(entityB.getFieldId());
        entityB.setPriority(entityA.getPriority() + 1);
        assertEntityComparator("field priority comparator", ProjectScript.FIELD_PRIORITY_COMPARATOR, entityA, entityB);
        assertEntityComparator("field priority comparator", ProjectScript.FIELD_PRIORITY_COMPARATOR, entityA, null);
        entityA.setPriority(entityB.getPriority());
        assertEntityComparatorEquals("field priority comparator", ProjectScript.FIELD_PRIORITY_COMPARATOR, entityA, entityB);
        assertEntityComparatorEquals("field priority comparator", ProjectScript.FIELD_PRIORITY_COMPARATOR, entityA, entityA);
    }

    @Before
    public void setUp() throws Exception {
        pro = new ProjectScript();
    }

    @After
    public void tearDown() throws Exception {
        pro = null;
    }
}
