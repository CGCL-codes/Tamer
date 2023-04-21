package package1;

import junit.framework.TestCase;

public class ClassCTest extends TestCase {

    public void testGetOne() {
        ClassA classA = new ClassA();
        assertEquals(1, classC.getOne());
    }

    public void testGetTwo() {
        ClassA classA = new ClassA();
        assertEquals(2, classC.getTwo());
    }
}
