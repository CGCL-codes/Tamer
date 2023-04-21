package kompilator;

import java.io.Reader;
import java.io.StringReader;
import kompilator.Graph.NodeFinal;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author dominik
 */
public class LexicalAnalizerTest {

    public LexicalAnalizerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getToken method, of class LexicalAnalizer.
     */
    @Test
    public void testGetToken() throws Exception {
        System.out.println("getToken");
        LexicalAnalizer instance = new LexicalAnalizer(new StringReader("" + "graf mostek(a b c){" + "d = and(a b)" + "e = and(b c)" + "f = or(d e)" + "return [d e f]" + "}"));
        int[] expResult = new int[] { PerlogicTokens.TT_GRAF, PerlogicTokens.TT_IDENT, PerlogicTokens.TT_BRACKET_ROUND_OPENING, PerlogicTokens.TT_IDENT, PerlogicTokens.TT_IDENT, PerlogicTokens.TT_IDENT, PerlogicTokens.TT_BRACKET_ROUND_ENDING, PerlogicTokens.TT_BRACKET_CURLY_OPENING, PerlogicTokens.TT_IDENT, PerlogicTokens.TT_ROWNA_SIE, PerlogicTokens.TT_AND, PerlogicTokens.TT_BRACKET_ROUND_OPENING, PerlogicTokens.TT_IDENT, PerlogicTokens.TT_IDENT, PerlogicTokens.TT_BRACKET_ROUND_ENDING, PerlogicTokens.TT_IDENT, PerlogicTokens.TT_ROWNA_SIE, PerlogicTokens.TT_AND, PerlogicTokens.TT_BRACKET_ROUND_OPENING, PerlogicTokens.TT_IDENT, PerlogicTokens.TT_IDENT, PerlogicTokens.TT_BRACKET_ROUND_ENDING, PerlogicTokens.TT_IDENT, PerlogicTokens.TT_ROWNA_SIE, PerlogicTokens.TT_OR, PerlogicTokens.TT_BRACKET_ROUND_OPENING, PerlogicTokens.TT_IDENT, PerlogicTokens.TT_IDENT, PerlogicTokens.TT_BRACKET_ROUND_ENDING, PerlogicTokens.TT_RETURN, PerlogicTokens.TT_BRACKET_SQUER_OPENING, PerlogicTokens.TT_IDENT, PerlogicTokens.TT_IDENT, PerlogicTokens.TT_IDENT, PerlogicTokens.TT_BRACKET_SQUER_ENDING, PerlogicTokens.TT_BRACKET_CURLY_ENDING, PerlogicTokens.TT_EOF };
        int result;
        int i = 0;
        do {
            result = instance.getToken();
            System.out.println(result);
            assertEquals(expResult[i++], result);
        } while (result != PerlogicTokens.TT_EOF);
    }

    /**
     * Test of getNode method, of class LexicalAnalizer.
     */
    @Test
    public void testGetNode() throws Exception {
        System.out.println("getNode");
        LexicalAnalizer instance = null;
        NodeFinal expResult = null;
        NodeFinal result = instance.getNode();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    Reader rd;
}
