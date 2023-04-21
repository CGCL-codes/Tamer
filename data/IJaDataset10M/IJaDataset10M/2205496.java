package orproject.backend.services;

import java.math.BigDecimal;
import java.math.MathContext;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import orproject.backend.services.helpers.TestHelper;
import orproject.helpers.DSGleichungsSystem;
import orproject.helpers.DSTableauSystem;
import orproject.internalInterface.IDSMethodCalculator;
import static org.junit.Assert.*;

/**
 * Tests setzt die Lösung der Aufgabe 1 auf Seite 162 im OR-Buch um
 *
 * @author BG
 */
public class DSMethodTestSample1 {

    private static IDSMethodCalculator calc;

    private static TestHelper helper;

    public DSMethodTestSample1() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        calc = new DSMethodCalculator();
        helper = new TestHelper();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        calc = null;
        helper = null;
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testGetNextSimplexSample1Step1() {
        DSGleichungsSystem startSimplex = new DSGleichungsSystem();
        BigDecimal werte[][] = new BigDecimal[3][4];
        werte[0][0] = new BigDecimal(5);
        werte[0][1] = new BigDecimal(3);
        werte[0][2] = new BigDecimal(4);
        werte[0][3] = new BigDecimal(100);
        werte[1][0] = new BigDecimal(3);
        werte[1][1] = new BigDecimal(4);
        werte[1][2] = new BigDecimal(3);
        werte[1][3] = new BigDecimal(50);
        werte[2][0] = new BigDecimal(-5);
        werte[2][1] = new BigDecimal(-6);
        werte[2][2] = new BigDecimal(-7);
        werte[2][3] = new BigDecimal(0);
        startSimplex = helper.fillGS(werte);
        DSGleichungsSystem expectedSimplex = new DSGleichungsSystem();
        BigDecimal werte1[][] = new BigDecimal[3][4];
        werte1[0][0] = new BigDecimal(1);
        werte1[0][1] = new BigDecimal(-7).divide(new BigDecimal(3), MathContext.DECIMAL128);
        werte1[0][2] = new BigDecimal(-4).divide(new BigDecimal(3), MathContext.DECIMAL128);
        werte1[0][3] = new BigDecimal(100).divide(new BigDecimal(3), MathContext.DECIMAL128);
        werte1[1][0] = new BigDecimal(1);
        werte1[1][1] = new BigDecimal(4).divide(new BigDecimal(3), MathContext.DECIMAL128);
        werte1[1][2] = new BigDecimal(1).divide(new BigDecimal(3), MathContext.DECIMAL128);
        werte1[1][3] = new BigDecimal(50).divide(new BigDecimal(3), MathContext.DECIMAL128);
        werte1[2][0] = new BigDecimal(2);
        werte1[2][1] = new BigDecimal(10).divide(new BigDecimal(3), MathContext.DECIMAL128);
        werte1[2][2] = new BigDecimal(7).divide(new BigDecimal(3), MathContext.DECIMAL128);
        werte1[2][3] = new BigDecimal(350).divide(new BigDecimal(3), MathContext.DECIMAL128);
        expectedSimplex = helper.fillGS(werte1);
        DSGleichungsSystem endSimplex = new DSGleichungsSystem();
        endSimplex = calc.getNextSimplex(startSimplex);
        assertTrue(helper.compareSimplex(endSimplex, expectedSimplex));
    }

    @Test
    public void testGetResultSample1() {
        DSTableauSystem startTableau = new DSTableauSystem();
        DSGleichungsSystem ersterSimplex = new DSGleichungsSystem();
        BigDecimal werte[][] = new BigDecimal[3][4];
        werte[0][0] = new BigDecimal(5);
        werte[0][1] = new BigDecimal(3);
        werte[0][2] = new BigDecimal(4);
        werte[0][3] = new BigDecimal(100);
        werte[1][0] = new BigDecimal(3);
        werte[1][1] = new BigDecimal(4);
        werte[1][2] = new BigDecimal(3);
        werte[1][3] = new BigDecimal(50);
        werte[2][0] = new BigDecimal(-5);
        werte[2][1] = new BigDecimal(-6);
        werte[2][2] = new BigDecimal(-7);
        werte[2][3] = new BigDecimal(0);
        ersterSimplex = helper.fillGS(werte);
        DSGleichungsSystem zweiterSimplex = new DSGleichungsSystem();
        BigDecimal werte1[][] = new BigDecimal[3][4];
        werte1[0][0] = new BigDecimal(1);
        werte1[0][1] = new BigDecimal(-7).divide(new BigDecimal(3), MathContext.DECIMAL128);
        werte1[0][2] = new BigDecimal(-4).divide(new BigDecimal(3), MathContext.DECIMAL128);
        werte1[0][3] = new BigDecimal(100).divide(new BigDecimal(3), MathContext.DECIMAL128);
        werte1[1][0] = new BigDecimal(1);
        werte1[1][1] = new BigDecimal(4).divide(new BigDecimal(3), MathContext.DECIMAL128);
        werte1[1][2] = new BigDecimal(1).divide(new BigDecimal(3), MathContext.DECIMAL128);
        werte1[1][3] = new BigDecimal(50).divide(new BigDecimal(3), MathContext.DECIMAL128);
        werte1[2][0] = new BigDecimal(2);
        werte1[2][1] = new BigDecimal(10).divide(new BigDecimal(3), MathContext.DECIMAL128);
        werte1[2][2] = new BigDecimal(7).divide(new BigDecimal(3), MathContext.DECIMAL128);
        werte1[2][3] = new BigDecimal(350).divide(new BigDecimal(3), MathContext.DECIMAL128);
        zweiterSimplex = helper.fillGS(werte1);
        startTableau.add(ersterSimplex);
        startTableau.add(zweiterSimplex);
        DSTableauSystem endTableau = new DSTableauSystem();
        endTableau = calc.getResult(ersterSimplex);
        assertTrue(helper.compareTableaus(startTableau, endTableau));
    }

    /**
    * einfacher Test um die compareSimplex-Methode zu testen
    *
    */
    @Test
    public void testMethodCompareSimplex() {
        DSGleichungsSystem startSimplex = new DSGleichungsSystem();
        BigDecimal werte[][] = new BigDecimal[3][4];
        werte[0][0] = new BigDecimal(5);
        werte[0][1] = new BigDecimal(3);
        werte[0][2] = new BigDecimal(4);
        werte[0][3] = new BigDecimal(100);
        werte[1][0] = new BigDecimal(3);
        werte[1][1] = new BigDecimal(4);
        werte[1][2] = new BigDecimal(3);
        werte[1][3] = new BigDecimal(50);
        werte[2][0] = new BigDecimal(-5);
        werte[2][1] = new BigDecimal(-6);
        werte[2][2] = new BigDecimal(-7);
        werte[2][3] = new BigDecimal(0);
        startSimplex = helper.fillGS(werte);
        DSGleichungsSystem expectedSimplex = new DSGleichungsSystem();
        BigDecimal werte1[][] = new BigDecimal[3][4];
        werte1[0][0] = new BigDecimal(5);
        werte1[0][1] = new BigDecimal(3);
        werte1[0][2] = new BigDecimal(4);
        werte1[0][3] = new BigDecimal(100);
        werte1[1][0] = new BigDecimal(3);
        werte1[1][1] = new BigDecimal(4);
        werte1[1][2] = new BigDecimal(3);
        werte1[1][3] = new BigDecimal(50);
        werte1[2][0] = new BigDecimal(-5);
        werte1[2][1] = new BigDecimal(-6);
        werte1[2][2] = new BigDecimal(-7);
        werte1[2][3] = new BigDecimal(0);
        expectedSimplex = helper.fillGS(werte1);
        assertTrue(helper.compareSimplex(startSimplex, expectedSimplex));
    }
}
