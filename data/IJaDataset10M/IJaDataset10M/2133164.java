package de.KW4FT.KW_Base;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author HrabovszZ
 *
 */
public class KW_PPTest {

    /**
	 * @throws java.lang.Exception
	 */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    /**
	 * @throws java.lang.Exception
	 */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    /**
	 * @throws java.lang.Exception
	 */
    @Before
    public void setUp() throws Exception {
    }

    /**
	 * @throws java.lang.Exception
	 */
    @After
    public void tearDown() throws Exception {
    }

    /**
	 * Ziel der Pr�fung: Pr�ft, ob die bei der Instanzierung der Klasse sowohl SWN- als auch StN-notierter
	 * SW-Befehl Zerlegt wird.
	 * Test method for {@link de.KW4FT.KW_Base.KW_V_LoPP#KW_V_LoPP(java.lang.String)}.
	 */
    @Test
    public final void test_KW_PP() {
        KW_PP my_KW_PP = new KW_PP("Gib ein: \"Key_1\" = \"Wert_1\"");
        assertEquals("Key_1", my_KW_PP.Parameter_1);
        assertEquals("Wert_1", my_KW_PP.Parameter_2);
        my_KW_PP = new KW_PP("\t [ ]SW.GibEin( \"Fachlicherbezeichner\", \"Wert\"");
        assertEquals("Fachlicherbezeichner", my_KW_PP.Parameter_1);
        assertEquals("Wert", my_KW_PP.Parameter_2);
    }

    /**
	 * Test method for {@link de.KW4FT.KW_Base.KW_V_LoPP#SWN2_V_LoPP(java.lang.String)}.
	 */
    @Test
    public final void test_SWN2_PP() {
        KW_PP my_KW_PP = new KW_PP("Gib ein: \"Key_1\" = \"Wert_1\"");
        assertEquals("Key_1", my_KW_PP.Parameter_1);
        assertEquals("Wert_1", my_KW_PP.Parameter_2);
        assertEquals(true, my_KW_PP.SWN2_PP("W�hle aus: \"Objekt\" = \"Wert\""));
        assertEquals("Objekt", my_KW_PP.Parameter_1);
        assertEquals("Wert", my_KW_PP.Parameter_2);
    }

    /**
	 * assertEquals("\t[ ] SW.W�hleAus( \"Key_1\", \"Wert_1\" )", my_KW_PP.GetStN());
	 * Pr�ft ob die generaliesirung f�r die Ausgabe in SWN funktioniert.
	 * KW_V_PP interessiert sich nur f�r die Parameter. Diese werden ermittelt
	 * und in Instanz variablen vorgehalten.
	 * Test method for {@link de.KW4FT.KW_Base.KW_V_LoPP#GetSWN()}.
	 */
    @Test
    public final void test_GetSWN() {
        KW_PP my_KW_PP = new KW_PP("Gib ein: \"Key_1\" = \"Wert_1\"");
        my_KW_PP.SWN_KeyWord = "Gib ein";
        assertEquals("Gib ein: \"Key_1\" = \"Wert_1\"", my_KW_PP.GetSWN());
    }

    /**
	 * Pr�fung der Methode <code>StN2_V_PP</code>
	 * Test method for {@link de.KW4FT.KW_Base.KW_PP#StN2_PP(java.lang.String)}.
	 */
    @Test
    public final void test_StN2_PP() {
        KW_PP my_KW_PP = new KW_PP("Gib ein: \"Objekt_1\" = \"Wert_1\"");
        assertEquals(true, my_KW_PP.StN2_PP("\t [ ] SW.GibEin( \"Objekt_1\", \"Wert_1\" )"));
        assertEquals("Objekt_1", my_KW_PP.Parameter_1);
        assertEquals("Wert_1", my_KW_PP.Parameter_2);
        assertEquals(true, my_KW_PP.StN2_PP("\t [ ]SW.W�hleAus( \"Objekt_2\", \"Wert_2\" )"));
        assertEquals("Objekt_2", my_KW_PP.Parameter_1);
        assertEquals("Wert_2", my_KW_PP.Parameter_2);
    }
}
