package Testing;

import junit.framework.*;
import Domini.categoriaTasca;
import Errors.excepcio;

/**
 * <p>Title: Projecte de PP</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author Frederic P�rez Ordeig
 * @version 1.0
 */
public class TestcategoriaTasca extends TestCase {

    categoriaTasca categoriatasca;

    public TestcategoriaTasca(String s) {
        super(s);
    }

    protected void setUp() {
        try {
            categoriatasca = new categoriaTasca("Nom tasca", 3);
        } catch (excepcio e) {
            fail();
        }
    }

    protected void tearDown() {
    }

    public void testGetImportancia() {
        assertTrue(categoriatasca.getImportancia() == 3);
    }

    public void testGetNom() {
        assertTrue(categoriatasca.getNom().equals("Nom tasca"));
    }

    public void testSetImportancia() {
        try {
            categoriatasca.setImportancia(4);
            assertTrue(categoriatasca.getImportancia() == 4);
        } catch (excepcio e) {
            fail();
        }
    }

    public void testSetNom() {
        try {
            categoriatasca.setNom("hola");
            assertTrue(categoriatasca.getNom().equals("hola"));
        } catch (excepcio e) {
            fail();
        }
    }
}
