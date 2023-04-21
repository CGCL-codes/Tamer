package com.rhythm.commons.reflections;

import com.rhythm.base.Copyable;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Michael J. Lee @ Synergy Energy Holdings, LLC
 */
public class ReflectorTest {

    public ReflectorTest() {
    }

    @Test(expected = NoSuchMethodException.class)
    public void testGetMethodWithArgumentsOfWrongType() throws NoSuchMethodException {
        Reflector.getMethod(new Mock("", 0), "setValue", Double.class);
    }

    @Test
    public void testGetMethodWithArguments() throws NoSuchMethodException {
        Reflector.getMethod(new Mock("", 0), "setValue", int.class);
    }

    @Test(expected = NoSuchMethodException.class)
    public void testGetMethodWithArgumentsButNotGivenAny() throws NoSuchMethodException {
        Reflector.getMethod(new Mock("", 0), "setValue");
    }

    @Test(expected = NoSuchMethodException.class)
    public void testGetMethodWhenOneDoesntExist() throws NoSuchMethodException {
        Reflector.getMethod(new Mock("", 0), "getName1");
    }

    @Test
    public void testGetMethod() throws NoSuchMethodException {
        assertNotNull(Reflector.getMethod(new Mock("", 0), "getName"));
    }

    @Test
    public void testHasGetter() {
        Mock mock = new Mock("Hello World", 1);
        assertTrue(Reflector.hasGetter(mock, "name"));
        assertTrue(Reflector.hasGetter(mock, "value"));
        assertFalse(Reflector.hasGetter(mock, "getName"));
        assertFalse(Reflector.hasGetter(mock, "getValue"));
    }

    @Test
    public void testGet() {
        Mock mock = new Mock("Hello World", 1);
        assertEquals("Hello World", Reflector.get(mock, "name"));
        assertEquals(1, Reflector.get(mock, "value"));
    }

    private class Mock {

        private String name;

        private int value;

        public Mock(String name, int value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public String getName(String nameToAppend) {
            return name + nameToAppend;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public int getValue(int valueToAdd) {
            return (value + valueToAdd);
        }

        public void setValue(int value) {
            this.value = value;
        }
    }
}
