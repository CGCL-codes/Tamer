package org.magiclabs.mosaic.testdomain.test2;

import junit.framework.TestCase;
import org.magiclabs.mosaic.MosaicFactory;

public class TestSens2 extends TestCase {

    final MosaicFactory factory = new MosaicFactory.StandardFactory();

    public void testSens1() {
        Person2 person = this.factory.create(Person2.class);
        assertFalse(person.isDirty());
        person.setName("Andrea");
        assertTrue(person.isDirty());
        assertEquals("Andrea", person.getName());
    }

    public interface Person2 extends DomainObject {

        public void setName(String name);

        String getName();

        public void setLastName(String name);

        String getLastName();
    }
}
