package de.helwich.math.symbolic.node;

import static org.junit.Assert.*;
import java.math.BigInteger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ScalarTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetHeight() {
        assertEquals(0, new Number(new BigInteger("2")).getHeight());
        assertEquals(0, new Variable("var1").getHeight());
        assertEquals(3, new Function(FunctionType.ADDITION, new Variable("a"), new Function(FunctionType.MULTIPLICATION, new Function(FunctionType.COSINUS, new Variable("b")), new Variable("c"))).getHeight());
    }
}
