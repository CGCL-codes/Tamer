package test.org.nakedobjects.object.reflect;

import junit.framework.TestCase;
import org.nakedobjects.noa.adapter.NakedObjectLoader;
import org.nakedobjects.noa.persist.NakedObjectPersistor;
import org.nakedobjects.noa.reflect.NakedObjectReflector;
import org.nakedobjects.nof.core.conf.PropertiesConfiguration;
import org.nakedobjects.nof.core.context.NakedObjectsContext;
import org.nakedobjects.nof.core.context.StaticContext;
import org.nakedobjects.nof.core.util.NakedObjectConfiguration;
import org.nakedobjects.testing.TestObjectLoader;
import org.nakedobjects.testing.TestPersistor;
import org.nakedobjects.testing.TestSession;
import org.nakedobjects.testing.TestSpecificationLoader;

public class NakedObjectsContextTest extends TestCase {

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(NakedObjectsContextTest.class);
    }

    private NakedObjectConfiguration configuration;

    private NakedObjectPersistor persistor;

    private NakedObjectReflector specificationLoader;

    private NakedObjectLoader objectLoader;

    private TestSession session;

    protected void setUp() throws Exception {
        configuration = new PropertiesConfiguration();
        NakedObjectsContext.reset();
        NakedObjectsContext.setConfiguration(configuration);
        NakedObjectsContext instance = StaticContext.createInstance();
        specificationLoader = new TestSpecificationLoader();
        instance.setReflector(specificationLoader);
        persistor = new TestPersistor();
        instance.setObjectPersistor(persistor);
        objectLoader = new TestObjectLoader();
        instance.setObjectLoader(objectLoader);
        session = new TestSession();
        instance.startSession(session);
    }

    public void testConfiguration() {
        assertEquals(configuration, NakedObjectsContext.getConfiguration());
    }

    public void testObjectPersistor() {
        assertEquals(persistor, NakedObjectsContext.getObjectPersistor());
    }

    public void testObjectLoader() {
        assertEquals(objectLoader, NakedObjectsContext.getObjectLoader());
    }

    public void testSpecificationLoader() {
        assertEquals(specificationLoader, NakedObjectsContext.getReflector());
    }

    public void testSession() {
        assertEquals(session, NakedObjectsContext.getSession());
    }
}
