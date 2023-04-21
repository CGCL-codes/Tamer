package it.cspnet.jpa.mapping;

import it.cspnet.jpa.mapping.testsupport.Alfa;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

public class TestItemListIterator extends TestItemList {

    public void setUp() {
        super.setUp();
    }

    public void testIterator() {
        ArrayList<Identifiable> identifiablesForThisTest = new ArrayList<Identifiable>();
        identifiablesForThisTest.add(alfa4);
        identifiablesForThisTest.add(alfa5);
        identifiablesForThisTest.add(alfa4);
        Iterator<Identifiable> iterOnTestItems = identifiablesForThisTest.iterator();
        items.addAll(identifiablesForThisTest);
        Iterator<Identifiable> iterOnItemListElems = items.iterator();
        assertNotNull(iterOnItemListElems);
        while (iterOnItemListElems.hasNext()) {
            Identifiable actualTestItem = iterOnTestItems.next();
            Identifiable actualItemListElem = iterOnItemListElems.next();
            assertEquals(actualTestItem.getId(), actualItemListElem.getId());
            assertEquals(((Alfa) (actualTestItem)).getDescrizione(), ((Alfa) (actualItemListElem)).getDescrizione());
        }
    }

    public void testListIterator() {
        items.add(alfa4);
        items.add(alfa5);
        items.add(delta5);
        ListIterator<Identifiable> itemListIterator = items.listIterator();
        assertNotNull(itemListIterator);
        assertTrue(itemListIterator.next().getId() == alfa4.getId());
        assertTrue(itemListIterator.next().getId() == alfa5.getId());
        assertTrue(itemListIterator.previous().getId() == alfa5.getId());
        assertTrue(itemListIterator.previous().getId() == alfa4.getId());
    }

    public void testListIteratorIndexed() {
        items.add(alfa4);
        items.add(alfa5);
        items.add(delta5);
        ListIterator<Identifiable> itemListIterator = items.listIterator(1);
        assertNotNull(itemListIterator);
        assertFalse(itemListIterator.next().getId() == alfa4.getId());
        assertTrue(itemListIterator.next().getId() == alfa5.getId());
        assertTrue(itemListIterator.previous().getId() == alfa5.getId());
        itemListIterator.previous();
        itemListIterator.previous();
        assertEquals(alfa4.getId(), itemListIterator.next().getId());
    }
}
