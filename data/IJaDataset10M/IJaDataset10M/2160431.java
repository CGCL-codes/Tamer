package org.sti2.elly.terms;

import junit.framework.JUnit4TestAdapter;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sti2.elly.ObjectTests;
import org.sti2.elly.api.factory.ITermFactory;
import org.sti2.elly.api.terms.IIndividual;

public class IndividualTest extends TestCase {

    private static ITermFactory TERM = TermFactory.getInstance();

    private static final String I_SYMBOL = "const";

    private static final String I_SYMBOL_MORE = "consu";

    private static final String I_SYMBOL_EVENMORE = "consuu";

    private IIndividual iTerm = null;

    private IIndividual iTerm_equal = null;

    private IIndividual iTerm_more = null;

    private IIndividual iTerm_evenmore = null;

    @Before
    protected void setUp() {
        iTerm = TERM.createIndividual(I_SYMBOL);
        iTerm_equal = TERM.createIndividual(I_SYMBOL);
        iTerm_more = TERM.createIndividual(I_SYMBOL_MORE);
        iTerm_evenmore = TERM.createIndividual(I_SYMBOL_EVENMORE);
    }

    @After
    protected void tearDown() {
        iTerm = iTerm_equal = iTerm_evenmore = iTerm_more = null;
    }

    @Test
    public void testGetValue() {
        assertEquals(I_SYMBOL, iTerm.getValue());
    }

    @Test
    public void testConstructor() {
        try {
            TERM.createIndividual(null);
            fail("Passing null-String should cause Exception");
        } catch (IllegalArgumentException e) {
        }
        try {
            TERM.createIndividual("");
            fail("Passing empty String should cause Exception");
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void testEquals() {
        ObjectTests.runTestEquals(iTerm, iTerm_equal, iTerm_more);
    }

    @Test
    public void testToString() {
        assertEquals(I_SYMBOL, iTerm.toString());
    }

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(IndividualTest.class);
    }
}
