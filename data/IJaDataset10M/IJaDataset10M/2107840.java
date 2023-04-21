package beans;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Esteban Benítez, José Giménez, Gustavo Planás
 */
public class UsuarioTest {

    public UsuarioTest() {
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
     * Test of getIdUsuario and setIdUsuario method, of class Usuario.
     */
    @Test
    public void testGetSetIdUsuario() {
        System.out.println("getIdUsuario");
        Usuario instance = new Usuario();
        assertNull(instance.getIdUsuario());
        System.out.println("setIdUsuario");
        String test = "1";
        instance.setIdUsuario(test);
        assertEquals(test, instance.getIdUsuario());
    }

    /**
     * Test of getNombreUsuario and setNombreUsuario method, of class Usuario.
     */
    @Test
    public void testGetSetNombreUsuario() {
        System.out.println("getNombreUsuario");
        Usuario instance = new Usuario();
        assertNull(instance.getNombreUsuario());
        String test = "Administrador";
        System.out.println("setNombreUsuario");
        instance.setNombreUsuario(test);
        assertEquals(test, instance.getNombreUsuario());
    }
}
