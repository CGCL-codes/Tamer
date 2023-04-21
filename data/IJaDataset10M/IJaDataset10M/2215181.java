package vista.graficador.uml;

import junit.framework.*;
import org.uml.diagrammanagement.GraphEdge;
import vista.graficador.GrDiagrama;
import vista.graficador.GrLineaVisual;

/**
 *
 * @author Juan Timoteo Ponce Ortiz
 */
public class GrClaseAsociacionLnkTest extends TestCase {

    public GrClaseAsociacionLnkTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(GrClaseAsociacionLnkTest.class);
        return suite;
    }
}
