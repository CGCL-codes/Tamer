package org.nakedobjects.system;

import org.nakedobjects.application.system.SystemClock;
import org.nakedobjects.application.valueholder.Date;
import org.nakedobjects.application.valueholder.DateTime;
import org.nakedobjects.application.valueholder.Time;
import org.nakedobjects.event.ObjectViewingMechanismListener;
import org.nakedobjects.object.NakedObjectLoader;
import org.nakedobjects.object.NakedObjectPersistor;
import org.nakedobjects.object.NakedObjectSpecificationLoader;
import org.nakedobjects.object.NakedObjects;
import org.nakedobjects.object.fixture.Fixture;
import org.nakedobjects.object.help.HelpManagerAssist;
import org.nakedobjects.object.help.HelpPeerFactory;
import org.nakedobjects.object.help.SimpleHelpManager;
import org.nakedobjects.object.loader.IdentityAdapterHashMap;
import org.nakedobjects.object.loader.ObjectLoaderImpl;
import org.nakedobjects.object.loader.PojoAdapterHashMap;
import org.nakedobjects.object.persistence.objectstore.NakedObjectStore;
import org.nakedobjects.object.reflect.ReflectionPeerFactory;
import org.nakedobjects.object.repository.NakedObjectsClient;
import org.nakedobjects.object.transaction.TransactionPeerFactory;
import org.nakedobjects.reflector.java.JavaBusinessObjectContainer;
import org.nakedobjects.reflector.java.JavaObjectFactory;
import org.nakedobjects.reflector.java.control.SimpleSession;
import org.nakedobjects.reflector.java.fixture.JavaFixture;
import org.nakedobjects.reflector.java.fixture.JavaFixtureBuilder;
import org.nakedobjects.reflector.java.reflect.JavaAdapterFactory;
import org.nakedobjects.reflector.java.reflect.JavaSpecificationLoader;
import org.nakedobjects.utility.AboutNakedObjects;
import org.nakedobjects.utility.SplashWindow;
import org.nakedobjects.utility.configuration.PropertiesConfiguration;
import org.nakedobjects.utility.configuration.PropertiesFileLoader;
import org.nakedobjects.viewer.skylark.SkylarkViewer;
import org.nakedobjects.viewer.skylark.ViewUpdateNotifier;
import java.util.Locale;
import java.util.StringTokenizer;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Utility class to start a server, using the default configuration file: client.properties.
 */
public abstract class AbstractSystem {

    private static final String DEFAULT_CONFIG = "nakedobjects.properties";

    private static final String SHOW_EXPLORATION_OPTIONS = "viewer.lightweight.show-exploration";

    private static final Logger LOG = Logger.getLogger(AbstractSystem.class);

    protected PropertiesConfiguration configuration;

    private NakedObjectsClient nakedObjects;

    protected void init() {
        LogManager.getRootLogger().setLevel(Level.OFF);
        configuration = new PropertiesConfiguration(new PropertiesFileLoader(DEFAULT_CONFIG, true));
        if (configuration.getString(SHOW_EXPLORATION_OPTIONS) == null) {
            configuration.add(SHOW_EXPLORATION_OPTIONS, "yes");
        }
        PropertyConfigurator.configure(configuration.getProperties("log4j"));
        nakedObjects = createRepository();
        nakedObjects.setConfiguration(configuration);
        AboutNakedObjects.logVersion();
        SplashWindow splash = null;
        boolean noSplash = configuration.getBoolean("nakedobjects.nosplash", false);
        if (!noSplash) {
            splash = new SplashWindow();
        }
        try {
            setUpLocale();
            NakedObjectPersistor objectManager = createPersistor();
            nakedObjects.setObjectPersistor(objectManager);
            NakedObjectSpecificationLoader specificationLoader = createReflector();
            nakedObjects.setSpecificationLoader(specificationLoader);
            NakedObjectLoader objectLoader = createLoader();
            nakedObjects.setObjectLoader(objectLoader);
            nakedObjects.setSession(new SimpleSession());
            nakedObjects.init();
            setupFixtures();
            displayUserInterface();
        } catch (Exception e) {
            LOG.error("startup problem", e);
        } finally {
            if (splash != null) {
                splash.toFront();
                splash.removeAfterDelay(6);
            }
        }
    }

    protected NakedObjectSpecificationLoader createReflector() {
        HelpManagerAssist helpManager = new HelpManagerAssist();
        helpManager.setDecorated(new SimpleHelpManager());
        HelpPeerFactory helpPeerFactory = new HelpPeerFactory();
        helpPeerFactory.setHelpManager(helpManager);
        ReflectionPeerFactory[] factories = new ReflectionPeerFactory[] { helpPeerFactory, new TransactionPeerFactory() };
        JavaSpecificationLoader specificationLoader = new JavaSpecificationLoader();
        specificationLoader.setReflectionPeerFactories(factories);
        return specificationLoader;
    }

    protected NakedObjectLoader createLoader() {
        SystemClock systemClock = new SystemClock();
        Date.setClock(systemClock);
        Time.setClock(systemClock);
        DateTime.setClock(systemClock);
        org.nakedobjects.application.value.Date.setClock(systemClock);
        JavaObjectFactory objectFactory = new JavaObjectFactory();
        JavaBusinessObjectContainer container = new JavaBusinessObjectContainer();
        objectFactory.setContainer(container);
        ObjectLoaderImpl objectLoader = new ObjectLoaderImpl();
        objectLoader.setObjectFactory(objectFactory);
        objectLoader.setPojoAdapterMap(new PojoAdapterHashMap());
        objectLoader.setIdentityAdapterMap(new IdentityAdapterHashMap());
        objectLoader.setAdapterFactory(new JavaAdapterFactory());
        return objectLoader;
    }

    protected abstract NakedObjectPersistor createPersistor();

    protected void setupFixtures() {
    }

    protected void shutdown() {
        nakedObjects.shutdown();
    }

    protected void installFixtures(String key) {
        JavaFixtureBuilder builder = new JavaFixtureBuilder();
        String fixtureList = configuration.getString(key);
        if (fixtureList != null) {
            StringTokenizer fixtures = new StringTokenizer(fixtureList, ",");
            while (fixtures.hasMoreTokens()) {
                String fixtureName = fixtures.nextToken().trim();
                Fixture fixture = (Fixture) StartUp.loadComponent(fixtureName, JavaFixture.class);
                builder.addFixture(fixture);
            }
            builder.installFixtures();
        }
    }

    protected NakedObjectStore installObjectStore(String key) {
        String storeName = configuration.getString(key);
        return (NakedObjectStore) StartUp.loadComponent(storeName);
    }

    protected void displayUserInterface() {
        SkylarkViewer skylark = new SkylarkViewer();
        ViewUpdateNotifier updateNotifier = new ViewUpdateNotifier();
        skylark.setUpdateNotifier(updateNotifier);
        skylark.setShutdownListener(new ObjectViewingMechanismListener() {

            public void viewerClosing() {
                shutdown();
                System.exit(0);
            }
        });
        String classList = configuration.getString("nakedobjects.classes");
        if (classList == null) {
            throw new StartupException("No classes specified");
        }
        DefaultApplicationContext context = new DefaultApplicationContext();
        StringTokenizer classes = new StringTokenizer(classList, ",");
        while (classes.hasMoreTokens()) {
            context.addClass(classes.nextToken().trim());
        }
        skylark.setApplication(context);
        skylark.setExploration(true);
        skylark.init();
    }

    protected NakedObjectsClient createRepository() {
        return new NakedObjectsClient();
    }

    private void setUpLocale() {
        String localeSpec = NakedObjects.getConfiguration().getString("locale");
        if (localeSpec != null) {
            int pos = localeSpec.indexOf('_');
            Locale locale;
            if (pos == -1) {
                locale = new Locale(localeSpec, "");
            } else {
                String language = localeSpec.substring(0, pos);
                String country = localeSpec.substring(pos + 1);
                locale = new Locale(language, country);
            }
            Locale.setDefault(locale);
            LOG.info("locale set to " + locale);
        }
        LOG.debug("locale is " + Locale.getDefault());
    }
}
