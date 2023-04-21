package vista.editores;

import junit.framework.*;
import javax.swing.JDialog;
import org.omg.uml.foundation.core.GeneralizableElement;
import vista.VistaEdicion;

/**
 *
 * @author Juan Timoteo Ponce Ortiz
 */
public class EditorEGeneralizableTest extends TestCase {

    public EditorEGeneralizableTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(EditorEGeneralizableTest.class);
        return suite;
    }

    /**
     * Prueba del metodo updateModelo , de la clase vista.editores.EditorEGeneralizable.
     */
    public void testUpdateModelo() {
        System.out.println("updateModelo");
        EditorEGeneralizable instance = null;
        instance.updateModelo();
        fail("El caso de prueba es un prototipo.");
    }

    /**
     * Prueba del metodo addListener , de la clase vista.editores.EditorEGeneralizable.
     */
    public void testAddListener() {
        System.out.println("addListener");
        EditorListener listener = null;
        EditorEGeneralizable instance = null;
        instance.addListener(listener);
        fail("El caso de prueba es un prototipo.");
    }

    /**
     * Prueba del metodo setModelo , de la clase vista.editores.EditorEGeneralizable.
     */
    public void testSetModelo() {
        System.out.println("setModelo");
        Object modelo = null;
        EditorEGeneralizable instance = null;
        instance.setModelo(modelo);
        fail("El caso de prueba es un prototipo.");
    }

    /**
     * Prueba del metodo updateVista , de la clase vista.editores.EditorEGeneralizable.
     */
    public void testUpdateVista() {
        System.out.println("updateVista");
        EditorEGeneralizable instance = null;
        instance.updateVista();
        fail("El caso de prueba es un prototipo.");
    }

    /**
     * Prueba del metodo getTitulo , de la clase vista.editores.EditorEGeneralizable.
     */
    public void testGetTitulo() {
        System.out.println("getTitulo");
        EditorEGeneralizable instance = null;
        String expResult = "";
        String result = instance.getTitulo();
        assertEquals(expResult, result);
        fail("El caso de prueba es un prototipo.");
    }
}
