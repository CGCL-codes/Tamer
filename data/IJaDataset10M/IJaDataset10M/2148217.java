package model.storage;

import model.storage.*;
import static org.junit.Assert.*;
import org.junit.*;

public class PersonTest {

    Person a, b, c, d, e, f;

    @Before
    public void setUp() {
        a = new Person("Dick Erol Roll");
        b = new Person("Roll, D.E.");
        c = new Person("D.E Roll");
        d = new Person("D. E. Roll");
        e = new Person("Biggs, J.");
        f = new Person("J Biggs");
    }

    @After
    public void tearDown() throws Exception {
        a = null;
    }

    @Test
    public void testLastName() {
        assertTrue(a.getLastName().equals("Roll"));
        assertTrue(b.getLastName().equals("Roll"));
        assertTrue(c.getLastName().equals("Roll"));
        assertTrue(d.getLastName().equals("Roll"));
    }

    @Test
    public void testFirstInitial() {
        assertTrue(a.getFirstInitial().equals("D"));
        assertTrue(b.getFirstInitial().equals("D"));
        assertTrue(c.getFirstInitial().equals("D"));
        assertTrue(d.getFirstInitial().equals("D"));
    }

    @Test
    public void testToString() {
        assertTrue(a.toString().compareTo("Dick Erol Roll") == 0);
    }

    @Test
    public void testEquals() {
        Person b = new Person("Dick Erol Roll");
        assertTrue(a.equals(b));
        assertTrue(e.equals(f));
        assertTrue(f.equals(e));
    }
}
