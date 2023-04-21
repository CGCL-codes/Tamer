package sk.evaku.jdirectory.test;

import static org.junit.Assert.assertEquals;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SerializationTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void tralala() {
        int a = 5;
        assertEquals(5, a);
    }

    @Test
    public void sooooBad() {
        int a = 4;
        assertEquals(5, a);
    }
}
