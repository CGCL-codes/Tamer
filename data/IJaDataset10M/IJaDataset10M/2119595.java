package uk.ac.shef.wit.simmetrics.tokenisers;

import junit.framework.TestCase;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA. User: Administrator Date: 23-Nov-2006 Time:
 * 12:04:10 To change this template use File | Settings | File Templates.
 */
public class TokeniserQGram3Test extends TestCase {

    /**
	 * internal tokeniser.
	 */
    private InterfaceTokeniser tokeniser = null;

    /**
	 * main constructor setting the name of the test case.
	 * 
	 * @param s
	 *            The name of the test
	 */
    public TokeniserQGram3Test(String s) {
        super(s);
    }

    /**
	 * Sets up the test fixture.
	 * 
	 * Called before every test case method.
	 */
    protected void setUp() {
        tokeniser = new TokeniserQGram3();
    }

    /**
	 * Tears down the test fixture.
	 * 
	 * Called after every test case method.
	 */
    protected void tearDown() {
    }

    /**
	 * Tests emptying the cart.
	 */
    public void testTokeniseToArrayList() {
        ArrayList results = tokeniser.tokenizeToArrayList("12345678");
        assertEquals(6, results.size());
        assertEquals("123", results.get(0));
        assertEquals("234", results.get(1));
        assertEquals("345", results.get(2));
        assertEquals("456", results.get(3));
        assertEquals("567", results.get(4));
        assertEquals("678", results.get(5));
    }
}
