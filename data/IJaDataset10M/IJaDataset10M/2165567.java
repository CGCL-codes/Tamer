package de.banh.bibo.model;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Für die Tests der implementation des Bibomanager einfach diese Klasse ableiten
 * und die abstrakten Funktionen implementieren
 * @author Thomas
 *
 */
public abstract class BiboManagerTest<O extends Manager> {

    /**
	 * Diese Klasse liefert ein Objekt der zu testenden Klasse
	 * @return das Objekt
	 * @author Thomas
	 */
    public abstract O generateTestObject();

    /**
	 * Diese Methode muss überschrieben werden, falls die Implementation des Objekts in irgendeinerweise explizit freigegeben werden muss
	 * @param testObject das Objekt, das getestet wurde
	 * @author Thomas
	 */
    public void freeTestObject(O testObject) {
    }

    private O testObjekt;

    /**
	 * @throws java.lang.Exception
	 */
    @Before
    public void setUp() throws Exception {
        testObjekt = generateTestObject();
    }

    /**
	 * @throws java.lang.Exception
	 */
    @After
    public void tearDown() throws Exception {
        freeTestObject(testObjekt);
        testObjekt = null;
    }

    /**
	 * Test method for {@link de.banh.bibo.model.Manager#getMediumManager()}.
	 */
    @Test
    public void testGetMediumManager() {
        if (testObjekt.getMediumManager() == null) {
            fail("Keinen MediumManager erhalten.");
        }
    }

    /**
	 * Test method for {@link de.banh.bibo.model.Manager#getExemplarManager()}.
	 */
    @Test
    public void testGetExemplarManager() {
        if (testObjekt.getExemplarManager() == null) {
            fail("Keinen ExemplarManager erhalten");
        }
    }

    /**
	 * Test method for {@link de.banh.bibo.model.Manager#getStandortManager()}.
	 */
    @Test
    public void testGetStandortManager() {
        if (testObjekt.getStandortManager() == null) {
            fail("Keinen StandortManager erhalten");
        }
    }

    /**
	 * Test method for {@link de.banh.bibo.model.Manager#getVerlagManager()}.
	 */
    @Test
    public void testGetVerlagManager() {
        if (testObjekt.getVerlagManager() == null) {
            fail("Keinen VerlagManager erhalten");
        }
    }

    /**
	 * Test method for {@link de.banh.bibo.model.Manager#getMedientypManager()}.
	 */
    @Test
    public void testGetMedientypManager() {
        if (testObjekt.getMedientypManager() == null) {
            fail("Keinen MedientypManager erhalten");
        }
    }

    /**
	 * Test method for {@link de.banh.bibo.model.Manager#getSperrgrundManager()}.
	 */
    @Test
    public void testGetSperrgrundManager() {
        if (testObjekt.getSperrgrundManager() == null) {
            fail("Keinen SperrgrundManager erhalten");
        }
    }

    /**
	 * Test method for {@link de.banh.bibo.model.Manager#getMedienzustandManager()}.
	 */
    @Test
    public void testGetExemplarzustandManager() {
        if (testObjekt.getExemplarzustandManager() == null) {
            fail("Keinen MedienzustandManager erhalten");
        }
    }
}
