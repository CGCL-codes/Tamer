package test.analiseSintatica.Regras;

import AnaliseLexicaFinal.EstruturaDeDados.Token;
import analiseSintatica.ErroSintaticoException;
import analiseSintatica.Regras.Regra;
import analiseSintatica.estruturadados.NoArvore;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Nathy
 */
public class RegraTestTest {

    public RegraTestTest() {
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
     * Test of setUp method, of class RegraTest.
     */
    @Test
    public void testSetUp() throws Exception {
        System.out.println("setUp");
        RegraTest instance = null;
        instance.setUp();
        fail("The test case is a prototype.");
    }

    /**
     * Test of tearDown method, of class RegraTest.
     */
    @Test
    public void testTearDown() throws Exception {
        System.out.println("tearDown");
        RegraTest instance = null;
        instance.tearDown();
        fail("The test case is a prototype.");
    }

    /**
     * Test of getListaPrimeiros method, of class RegraTest.
     */
    @Test
    public void testGetListaPrimeiros() {
        System.out.println("getListaPrimeiros");
        Regra regra = null;
        RegraTest instance = null;
        String expResult = "";
        String result = instance.getListaPrimeiros(regra);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    /**
     * Test of analisarTerminal method, of class RegraTest.
     */
    @Test
    public void testAnalisarTerminal() throws ErroSintaticoException {
        System.out.println("analisarTerminal");
        Regra regra = null;
        Token token = null;
        RegraTest instance = null;
        instance.analisarTerminal(regra, token);
        fail("The test case is a prototype.");
    }

    /**
     * Test of analisarCodigo method, of class RegraTest.
     */
    @Test
    public void testAnalisarCodigo() throws ErroSintaticoException {
        System.out.println("analisarCodigo");
        Regra regra = null;
        String codigo = "";
        RegraTest instance = null;
        NoArvore expResult = null;
        NoArvore result = instance.analisarCodigo(regra, codigo);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }
}
