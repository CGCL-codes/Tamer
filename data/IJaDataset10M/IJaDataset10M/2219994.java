package br.gov.frameworkdemoiselle.internal.factory;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import java.util.Map;
import javax.enterprise.inject.spi.Annotated;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import junit.framework.Assert;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.slf4j.Logger;
import br.gov.frameworkdemoiselle.annotation.PersistenceUnit;
import br.gov.frameworkdemoiselle.exception.ConfigurationException;
import br.gov.frameworkdemoiselle.internal.configuration.EntityManagerConfig;
import br.gov.frameworkdemoiselle.stereotype.Configuration;
import br.gov.frameworkdemoiselle.util.ResourceBundle;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Persistence.class, ResourceBundle.class })
public class EntityManagerFactoryTest {

    private EntityManagerFactory factory;

    private javax.persistence.EntityManagerFactory emf;

    private Logger logger;

    private EntityManager em;

    private ResourceBundle bundle;

    @Before
    public void setUp() {
        PowerMock.mockStatic(Persistence.class);
        factory = new EntityManagerFactory();
        emf = EasyMock.createMock(javax.persistence.EntityManagerFactory.class);
        logger = EasyMock.createMock(Logger.class);
        factory.setLogger(logger);
    }

    @After
    public void tearDown() {
        factory = null;
    }

    @Test
    public void test2EMInPersistenceXMLButAnnotationDefine1MustNotThrowsException() throws Exception {
        EasyMock.expect(Persistence.createEntityManagerFactory("pu1")).andReturn(emf);
        EasyMock.expect(Persistence.createEntityManagerFactory("pu2")).andReturn(emf);
        PowerMock.replay(Persistence.class);
        em = EasyMock.createMock(EntityManager.class);
        EasyMock.expect(emf.createEntityManager()).andReturn(em);
        EasyMock.replay(em);
        EasyMock.replay(emf);
        bundle = PowerMock.createMock(ResourceBundle.class);
        PowerMock.expectPrivate(bundle, "handleGetObject", "getting-persistence-unit-from-annotation").andReturn("");
        EasyMock.expect(bundle.getString("entity-manager-was-created", "pu1")).andReturn("");
        EasyMock.replay(bundle);
        factory.setBundle(bundle);
        PersistenceUnit persistenceUnit = EasyMock.createMock(PersistenceUnit.class);
        EasyMock.expect(persistenceUnit.name()).andReturn("pu1");
        EasyMock.replay(persistenceUnit);
        Annotated annotated = EasyMock.createMock(Annotated.class);
        EasyMock.expect(annotated.getAnnotation(PersistenceUnit.class)).andReturn(persistenceUnit);
        EasyMock.replay(annotated);
        InjectionPoint injectionPoint = PowerMock.createMock(InjectionPoint.class);
        expect(injectionPoint.getAnnotated()).andReturn(annotated);
        replay(injectionPoint);
        ;
        EntityManagerFactory.ENTITY_MANAGER_RESOURCE = "META-INF/persistence-two-units.xml";
        try {
            EntityManager createdEntityManager = factory.create(injectionPoint);
            Assert.assertNotNull(createdEntityManager);
        } catch (ConfigurationException ce) {
            Assert.fail("Must not throws exception with two persistence units defined in persistence.xml");
        }
        PowerMock.verify(bundle);
    }

    @Test
    @Ignore
    public void testTwoPersistenceUnitsMustThrowsException() throws Exception {
        EasyMock.expect(Persistence.createEntityManagerFactory("pu1")).andReturn(emf);
        PowerMock.replay(Persistence.class);
        em = EasyMock.createMock(EntityManager.class);
        EasyMock.expect(emf.createEntityManager()).andReturn(em);
        EasyMock.replay(em);
        EasyMock.replay(emf);
        bundle = PowerMock.createMock(ResourceBundle.class);
        PowerMock.expectPrivate(bundle, "handleGetObject", "more-than-one-persistence-unit-defined").andReturn("");
        EasyMock.replay(bundle);
        factory.setBundle(bundle);
        EntityManagerConfig config = new EntityManagerConfig();
        config.setPersistenceUnitName(null);
        EntityManagerFactory.ENTITY_MANAGER_RESOURCE = "META-INF/persistence-two-units.xml";
        try {
            factory.create(config);
            Assert.fail("Must throws exception with two persistence units defined in persistence.xml");
        } catch (ConfigurationException ce) {
        }
        PowerMock.verify(bundle);
    }

    @Test
    @Ignore
    public void testEntityManagerMustBeCreatedFromXMLResource() throws Exception {
        EasyMock.expect(Persistence.createEntityManagerFactory("pu1")).andReturn(emf);
        PowerMock.replay(Persistence.class);
        em = EasyMock.createMock(EntityManager.class);
        EasyMock.expect(emf.createEntityManager()).andReturn(em);
        EasyMock.replay(em);
        EasyMock.replay(emf);
        bundle = PowerMock.createMock(ResourceBundle.class);
        PowerMock.expectPrivate(bundle, "handleGetObject", "getting-persistence-unit-from-persistence").andReturn("");
        EasyMock.expect(bundle.getString("persistence-unit-name-found", "pu1")).andReturn("");
        EasyMock.expect(bundle.getString("entity-manager-was-created", "pu1")).andReturn("");
        EasyMock.replay(bundle);
        factory.setBundle(bundle);
        EntityManagerConfig config = new EntityManagerConfig();
        config.setPersistenceUnitName(null);
        EntityManagerFactory.ENTITY_MANAGER_RESOURCE = "META-INF/persistence.xml";
        EntityManager createdEntityManager = factory.create(config);
        PowerMock.verify(bundle);
        PowerMock.verify(Persistence.class);
        Assert.assertNotNull(createdEntityManager);
    }

    @Test
    public void testEntityManagerMustBeCreatedFromPropertiesResource() {
        EasyMock.expect(Persistence.createEntityManagerFactory("pu1")).andReturn(emf);
        PowerMock.replay(Persistence.class);
        em = PowerMock.createMock(EntityManager.class);
        EasyMock.expect(emf.createEntityManager()).andReturn(em);
        EasyMock.replay(em);
        EasyMock.replay(emf);
        bundle = PowerMock.createMock(ResourceBundle.class);
        EasyMock.expect(bundle.getString("getting-persistence-unit-from-properties", "demoiselle.properties")).andReturn("");
        EasyMock.expect(bundle.getString("entity-manager-was-created", "pu1")).andReturn("");
        EasyMock.replay(bundle);
        factory.setBundle(bundle);
        EntityManagerConfig config = new EntityManagerConfig();
        config.setPersistenceUnitName("pu1");
        EntityManager createdEntityManager = factory.create(config);
        PowerMock.verify(bundle);
        PowerMock.verify(Persistence.class);
        Assert.assertNotNull(createdEntityManager);
    }

    @Test
    public void testEntityManagerWasReallyCached() {
        EasyMock.expect(Persistence.createEntityManagerFactory("pu1")).andReturn(emf);
        PowerMock.replay(Persistence.class);
        em = PowerMock.createMock(EntityManager.class);
        EasyMock.expect(emf.createEntityManager()).andReturn(em);
        EasyMock.replay(em);
        EasyMock.replay(emf);
        bundle = EasyMock.createMock(ResourceBundle.class);
        EasyMock.expect(bundle.getString("getting-persistence-unit-from-properties", Configuration.DEFAULT_CONFIGURATION_FILE)).andReturn("");
        EasyMock.expect(bundle.getString("entity-manager-was-created", "pu1")).andReturn("");
        EasyMock.replay(bundle);
        factory.setBundle(bundle);
        EntityManagerConfig config = new EntityManagerConfig();
        config.setPersistenceUnitName("pu1");
        factory.create(config);
        Map<String, EntityManager> entityManagerCache = Whitebox.getInternalState(factory, "entityManagerCache");
        Assert.assertTrue(entityManagerCache.containsKey("pu1"));
        Assert.assertNotNull(entityManagerCache.get("pu1"));
        PowerMock.verify(bundle);
        PowerMock.verify(Persistence.class);
    }
}
