package ar.com.khronos.core.test;

import java.util.Collection;
import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ar.com.khronos.core.model.Persistible;

/**
 * Clase abstracta para todos los tests de la aplicacion.
 *
 * @author <a href="mailto:tezequiel@gmail.com">Ezequiel Turovetzky</a>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners(TakeTimeTestExecutionListener.class)
public abstract class KhronosTestCase extends TestCase {

    /** Logger */
    protected static final Log logger = LogFactory.getLog(KhronosTestCase.class);

    /** Si ya fue inicializado */
    private boolean initialized;

    /**
     * Crea una nueva instancia de esta clase.
     */
    public KhronosTestCase() {
    }

    /**
     * Funciona como alternativa, a nivel de instancia,
     * a los metodos anotados con {@link BeforeClass BeforeClass}.
     * 
     * @see #initialize()
     */
    @Before
    public void beforeAllTests() {
        if (!initialized) {
            initialized = true;
            initialize();
        }
    }

    /**
     * Inicializa el entorno para ejecutar todos los tests.
     * Este metodo se ejecuta una unica vez, antes de la
     * ejecucion del primer test.
     */
    protected void initialize() {
    }

    /**
     * Asegura que un objeto persistible tiene id. Si no, se arroja un
     * AssertionError.
     */
    public static void assertHasId(Persistible p) {
        assertNotNull("No tiene id", p.getId());
    }

    /**
     * Asegura que un objeto persistible no tiene id. Si no, se arroja un
     * AssertionError.
     */
    public static void assertHasNotId(Persistible p) {
        assertNull("Tiene id", p.getId());
    }

    /**
	 * Asegura que una coleccion esta vacia. Si no, se arroja un
	 * AssertionFailedError. 
	 */
    public static void assertEmpty(Collection<?> c) {
        assertTrue("La coleccion esta vacia", c.isEmpty());
    }

    /**
	 * Asegura que una coleccion no esta vacia. Si no, se arroja un
	 * AssertionFailedError. 
	 */
    public static void assertNotEmpty(Collection<?> c) {
        assertFalse("La coleccion no esta vacia", c.isEmpty());
    }

    /**
     * Asegura que dos objetos no son iguales. Si no, se arroja un
     * AssertionFailedError
     */
    public static void assertNotEquals(Object o1, Object o2) {
        assertFalse(o1.equals(o2));
    }

    /**
     * Asegura que dos objetos no son iguales. Si no, se arroja un
     * AssertionFailedError
     */
    public static void assertNotEquals(String message, Object o1, Object o2) {
        assertFalse(message, o1.equals(o2));
    }

    /**
     * Asegura que un excepcion es de un tipo especifico. Si no, se arroja un
     * AssertionFailedError
     */
    public static void assertExceptionIs(Throwable t, Class<? extends Exception> c) {
        assertEquals("La excepcion no es del tipo " + c.getName(), t.getClass().getName(), c.getName());
    }

    /**
     * Asegura que un excepcion es de un tipo especifico. Si no, se arroja un
     * AssertionFailedError
     */
    public static void assertExceptionIsNot(Throwable t, Class<? extends Exception> c) {
        assertNotEquals("La excepcion es del tipo " + c.getName(), t.getClass(), c);
    }
}
