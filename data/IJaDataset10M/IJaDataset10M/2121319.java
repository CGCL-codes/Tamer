package test.com.gestioni.adoc;

import junit.framework.Test;
import junit.framework.TestSuite;
import test.com.gestioni.adoc.aps.system.services.repository.documento.TestRepositoryDocumentoDAO;
import test.com.gestioni.adoc.aps.system.services.repository.documento.TestRepositoryDocumentoManager;
import test.com.gestioni.adoc.aps.system.services.repository.documento.TestRepositorySharedDocumentoManager;
import test.com.gestioni.adoc.aps.system.services.repository.documento.personale.TestRepositoryDocumentoPersonaleManager;
import test.com.gestioni.adoc.aps.system.services.repository.fascicolo.TestRepositoryFascicoloManager;
import test.com.gestioni.adoc.aps.system.services.repository.titolario.TestRepositoryTitolarioManager;

public class AdocAllTest_JcrRepository {

    public static Test suite() {
        TestSuite suite = new TestSuite("ADOC JACKRABBIT ALL TEST - Solo i test che vanno " + "ad utilizzare direttamente jackrabbit");
        suite.addTestSuite(TestRepositoryTitolarioManager.class);
        suite.addTestSuite(TestRepositoryFascicoloManager.class);
        suite.addTestSuite(TestRepositoryDocumentoPersonaleManager.class);
        suite.addTestSuite(TestRepositoryDocumentoManager.class);
        suite.addTestSuite(TestRepositorySharedDocumentoManager.class);
        suite.addTestSuite(TestRepositoryDocumentoDAO.class);
        return suite;
    }
}
