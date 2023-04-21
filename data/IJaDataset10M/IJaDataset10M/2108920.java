package ar.com.khronos.core.test;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

/**
 * Clase base para todos los tests que necesitan del contexto de Spring para
 * inyeccion de dependencias.
 *
 * @author <a href="mailto:tezequiel@gmail.com">Ezequiel Turovetzky</a>
 */
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class })
@ContextConfiguration(locations = { "classpath:ar/com/khronos/core/spring/spring-application-context.xml" })
public abstract class KhronosSpringTestCase extends KhronosTestCase {

    /**
     * Creates a new object of this class.
     */
    public KhronosSpringTestCase() {
    }
}
