package atg.util.service.identifiant;

import static atg.util.service.identifiant.ATGCritereValue.OPERATION_EGAL;
import junit.framework.TestCase;
import atg.service.constante.AtgConstantes;
import atg.service.constante.AtgConstantesWF;
import atg.service.log.AtgLogManager;
import atg.test.EntiteBidon;

/**
 * Classe de test de ATGCritereValue
 * @author Reizz
 *
 */
public class TestATGCritereValue extends TestCase {

    public TestATGCritereValue(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
        atg.service.constante.AtgConstantesWF.setPropFileName("constantes_test_atg");
        atg.service.constante.AtgConstantesWF.init(new atg.service.constante.AtgConstantesWF());
        AtgLogManager.setPathFileProperties(AtgConstantesWF.getValue(AtgConstantes.ATG_LOG_LOGGING_PATH_FILE_NAME) + AtgConstantes.ATG_LOG_DEFAULT_FILE_LOGGING);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testATGCritereValueStringStringString() {
        ATGCritereValue crit = new ATGCritereValue("LIBELLE", OPERATION_EGAL, "VALEUR_DE_TEST");
        assertEquals("LIBELLE", crit.getKey());
        assertEquals(OPERATION_EGAL, crit.getOperation());
        assertEquals("VALEUR_DE_TEST", crit.getStringValue());
    }

    public void testATGCritereValueClassStringStringString() {
        ATGCritereValue crit = new ATGCritereValue(EntiteBidon.class, "libelle", OPERATION_EGAL, "VALEUR_DE_TEST");
        assertEquals(EntiteBidon.class, crit.getClasse());
        assertEquals("libelle", crit.getAttribut());
        assertEquals(OPERATION_EGAL, crit.getOperation());
        assertEquals("VALEUR_DE_TEST", crit.getStringValue());
    }

    public void testATGCritereValueClassStringStringObject() {
        ATGCritereValue crit = new ATGCritereValue(EntiteBidon.class, "code", OPERATION_EGAL, new Integer(10));
        assertEquals(EntiteBidon.class, crit.getClasse());
        assertEquals("code", crit.getAttribut());
        assertEquals(OPERATION_EGAL, crit.getOperation());
        assertEquals(new Integer(10), crit.getValue());
    }

    public void testGetKey() {
        ATGCritereValue crit = new ATGCritereValue("LIBELLE", OPERATION_EGAL, "VALEUR_DE_TEST");
        assertEquals("LIBELLE", crit.getKey());
    }

    public void testGetOperation() {
        ATGCritereValue crit = new ATGCritereValue("LIBELLE", OPERATION_EGAL, "VALEUR_DE_TEST");
        assertEquals(OPERATION_EGAL, crit.getOperation());
    }

    public void testGetStringValue() {
        ATGCritereValue crit = new ATGCritereValue("LIBELLE", OPERATION_EGAL, "VALEUR_DE_TEST");
        assertEquals("VALEUR_DE_TEST", crit.getStringValue());
    }

    public void testGetValue() {
        ATGCritereValue crit = new ATGCritereValue("LIBELLE", OPERATION_EGAL, new Integer(10));
        assertEquals(new Integer(10), crit.getValue());
    }

    public void testGetAttribut() {
        ATGCritereValue crit = new ATGCritereValue(EntiteBidon.class, "libelle", OPERATION_EGAL, "VALEUR_DE_TEST");
        assertEquals("libelle", crit.getAttribut());
    }

    public void testGetClasse() {
        ATGCritereValue crit = new ATGCritereValue(EntiteBidon.class, "libelle", OPERATION_EGAL, "VALEUR_DE_TEST");
        assertEquals(EntiteBidon.class, crit.getClasse());
    }

    public void testCompareTo() {
        ATGCritereValue crit = new ATGCritereValue("LIBELLE", OPERATION_EGAL, "VALEUR_DE_TEST");
        try {
            crit.compareTo(null);
        } catch (NullPointerException e) {
            assertTrue("comparaison avec null", true);
        }
        String bidon = "objet quelconque";
        assertEquals("comparaison avec un object quelconque", -1, crit.compareTo(bidon));
        ATGCritereValue crit2 = new ATGCritereValue("AAAA", OPERATION_EGAL, "VALEUR_DE_TEST");
        assertTrue("ccomparaison avec un critere inferieur en cle", crit.compareTo(crit2) > 0);
        ATGCritereValue crit3 = new ATGCritereValue("ZZZZ", OPERATION_EGAL, "VALEUR_DE_TEST");
        assertTrue("comparaison avec un critere superieur en cle", crit.compareTo(crit3) < 0);
        ATGCritereValue crit4 = new ATGCritereValue("AAAA", OPERATION_EGAL, "VALEUR_DE_TEST");
        assertTrue("comparaison avec un critere inferieur en valeur", crit.compareTo(crit4) > 0);
        ATGCritereValue crit5 = new ATGCritereValue("ZZZZ", OPERATION_EGAL, "VALEUR_DE_TEST");
        assertTrue("comparaison avec un critere superieur en valeur", crit.compareTo(crit5) < 0);
        ATGCritereValue crit6 = new ATGCritereValue("LIBELLE", OPERATION_EGAL, "VALEUR_DE_TEST");
        assertEquals("comparaison avec un critere identique", 0, crit.compareTo(crit6));
        ATGCritereValue crit7 = new ATGCritereValue(EntiteBidon.class, "code", OPERATION_EGAL, new Integer(10));
        ATGCritereValue crit8 = new ATGCritereValue(EntiteBidon.class, "code", OPERATION_EGAL, new Integer(9));
        assertTrue("comparaison avec un critere inferieur en valeur", crit7.compareTo(crit8) > 0);
    }
}
