package jstorm.acceptance;

import junit.framework.*;
import jstorm.test.*;
import java.util.Collection;
import java.util.Iterator;

public class CompositeKeyTest extends BaseAcceptanceTest {

    TestCompositeConcurrencyObject testComposite;

    public CompositeKeyTest(String name) {
        super(name);
    }

    public void setUp() {
        testComposite = new TestCompositeConcurrencyObject();
    }

    public void testCompositeUpdate() {
        int initialSize = getNumberOfTestCompositesInDB();
        testComposite.setAttribute1("InsertAttribute1");
        testComposite.setAttribute2("InsertAttribute2");
        testComposite.setTableId(10);
        testComposite.setSecondTableId(20);
        testComposite.insert();
        testComposite.setAttribute1("UpdatedAttribute1");
        testComposite.update();
        int newSize = getNumberOfTestCompositesInDB();
        assertEquals(initialSize + 1, newSize);
        TestCompositeConcurrencyObject constraint = new TestCompositeConcurrencyObject();
        constraint.setTableId(10);
        constraint.setSecondTableId(20);
        Iterator objects = getDatabase().doSelect(constraint).iterator();
        TestCompositeConcurrencyObject updated = (TestCompositeConcurrencyObject) objects.next();
        assertEquals("UpdatedAttribute1", updated.getAttribute1());
        assertEquals("InsertAttribute2", updated.getAttribute2());
    }

    public void testCompositeDelete() {
        int initialSize = getNumberOfTestCompositesInDB();
        testComposite.setAttribute1("InsertForDeleteAttribute1");
        testComposite.setAttribute2("InsertForDeleteAttribute2");
        testComposite.setTableId(30);
        testComposite.setSecondTableId(40);
        testComposite.insert();
        int newSize = getNumberOfTestCompositesInDB();
        assertEquals("After insert", initialSize + 1, newSize);
        getDatabase().doDelete(testComposite);
        int sizeAfterDelete = getNumberOfTestCompositesInDB();
        assertEquals("After delete", initialSize, sizeAfterDelete);
    }

    public int getNumberOfTestCompositesInDB() {
        return getDatabase().doSelect(testComposite.getMapping()).size();
    }
}
