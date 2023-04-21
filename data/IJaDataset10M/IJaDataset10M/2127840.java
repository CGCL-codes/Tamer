package org.databene.commons.collection;

import org.junit.Test;
import static junit.framework.Assert.*;

/**
 * Tests the {@link NamedValueList}.<br/><br/>
 * Created at 09.05.2008 21:19:22
 * @since 0.5.4
 * @author Volker Bergmann
 */
public class NamedValueListTest {

    @Test
    public void testCaseIgnorantList() {
        NamedValueList<Integer> list = NamedValueList.createCaseIgnorantList();
        list.add("ONE", 1);
        assertEquals(1, list.size());
        assertEquals("one", list.getName(0));
        assertEquals(1, list.getValue(0).intValue());
        assertEquals(1, list.someValueOfName("One").intValue());
        list.add("oNE", 11);
        assertEquals(2, list.size());
        assertEquals("one", list.getName(1));
        assertEquals(11, list.getValue(1).intValue());
        assertEquals(11, list.someValueOfName("One").intValue());
    }

    @Test
    public void testCaseInsensitiveList() {
        NamedValueList<Integer> list = NamedValueList.createCaseInsensitiveList();
        list.add("ONE", 1);
        assertEquals(1, list.size());
        assertEquals("ONE", list.getName(0));
        assertEquals(1, list.getValue(0).intValue());
        int index = list.someValueOfName("One").intValue();
        assertEquals(1, index);
        list.add("oNE", 11);
        assertEquals(2, list.size());
        assertEquals("oNE", list.getName(1));
        assertEquals(11, list.getValue(1).intValue());
        assertTrue(index == 11 || index == 1);
    }

    @Test
    public void testCaseSensitiveList() {
        NamedValueList<Integer> list = NamedValueList.createCaseSensitiveList();
        list.add("ONE", 1);
        assertEquals(1, list.size());
        assertEquals("ONE", list.getName(0));
        assertEquals(1, list.getValue(0).intValue());
        assertEquals(1, list.someValueOfName("ONE").intValue());
        assertEquals(null, list.someValueOfName("One"));
        list.add("oNE", 11);
        assertEquals(2, list.size());
        assertEquals("oNE", list.getName(1));
        assertEquals(11, list.getValue(1).intValue());
        assertEquals(11, list.someValueOfName("oNE").intValue());
        assertEquals(1, list.someValueOfName("ONE").intValue());
    }
}
