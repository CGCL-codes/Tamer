package ar.edu.unlp.info.diseyappweb.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTestsCompania {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for ar.edu.unlp.info.diseyappweb.test");
        suite.addTestSuite(ProductoTestCase.class);
        suite.addTestSuite(UsuarioTestCase.class);
        suite.addTestSuite(MateriaPrimaTestCase.class);
        suite.addTestSuite(LlamadoLicitacionTestCase.class);
        suite.addTestSuite(LicitacionTestCase.class);
        suite.addTestSuite(PedidoTestCase.class);
        suite.addTestSuite(OrdenTrabajoTestCase.class);
        return suite;
    }
}
