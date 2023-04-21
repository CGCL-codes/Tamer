package org.nakedobjects.xat.integ.junit4;

import org.nakedobjects.noa.persist.NakedObjectPersistor;
import org.nakedobjects.noa.reflect.ContainerInjector;
import org.nakedobjects.nof.boot.system.NakedObjectsSystem;
import org.nakedobjects.nof.core.conf.Configuration;
import org.nakedobjects.nof.core.context.NakedObjectsContext;
import org.nakedobjects.nof.core.context.StaticContext;
import org.nakedobjects.nof.core.image.java.AwtTemplateImageLoaderInstaller;
import org.nakedobjects.nof.core.security.PasswordFileAuthenticatorInstaller;
import org.nakedobjects.nof.core.system.ConfigurationLoader;
import org.nakedobjects.nof.core.system.InstanceFactory;
import org.nakedobjects.nof.core.util.NakedObjectConfiguration;
import org.nakedobjects.nof.persist.objectstore.inmemory.InMemoryPersistorInstaller;
import org.nakedobjects.nof.reflect.javax.JavaNotifyingDomainObjectContainer;
import org.nakedobjects.nof.reflect.javax.JavaNotifyingReflectorInstaller;
import org.nakedobjects.nos.client.xat.*;
import org.nakedobjects.testlib.TestViewer;
import org.nakedobjects.testlib.annotations.DocumentUsing;
import org.nakedobjects.testlib.documentor.Documentor;
import org.nakedobjects.xat.documentor.DocumentorFactory;
import org.nakedobjects.xat.documentor.mem.InMemoryDocumentor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import org.jmock.Mockery;
import org.junit.internal.runners.InitializationError;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.internal.runners.MethodRoadie;
import org.junit.internal.runners.TestMethod;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;

/**
 * Copied from JMock, and with the same support.
 * 
 */
public class NakedObjectsTestRunner extends JUnit4ClassRunner {

    private final Field mockeryField;

    private Documentor documentor;

    private final NakedObjectConfiguration configuration;

    private final DocumentProxyInteractionsListener documentProxyInteractionsListener;

    private final AnnotatedClassFixtureInstaller fixturesInstaller;

    private final AnnotatedClassServicesInstaller servicesInstaller;

    public NakedObjectsTestRunner(final Class<?> testClass) throws InitializationError {
        super(testClass);
        final ConfigurationLoader configurationLoader = new XatConfigurationLoader();
        configuration = configurationLoader.load();
        final Documentor requested = getDocumentor(testClass);
        documentProxyInteractionsListener = new DocumentProxyInteractionsListener(requested);
        documentor = documentProxyInteractionsListener;
        fixturesInstaller = new AnnotatedClassFixtureInstaller();
        try {
            fixturesInstaller.addFixturesAnnotatedOn(getTestClass().getJavaClass());
        } catch (InstantiationException e) {
            throw new InitializationError(e);
        } catch (IllegalAccessException e) {
            throw new InitializationError(e);
        }
        servicesInstaller = new AnnotatedClassServicesInstaller();
        try {
            servicesInstaller.addServicesAnnotatedOn(getTestClass().getJavaClass());
        } catch (InstantiationException e) {
            throw new InitializationError(e);
        } catch (IllegalAccessException e) {
            throw new InitializationError(e);
        }
        servicesInstaller.addService(documentor);
        mockeryField = findFieldAndMakeAccessible(testClass, Mockery.class);
    }

    @Override
    protected void invokeTestMethod(final Method method, final RunNotifier notifier) {
        final JavaNotifyingDomainObjectContainer container = new JavaNotifyingDomainObjectContainer();
        final TestViewer testViewer = new TestViewerImpl(container);
        servicesInstaller.addService(testViewer);
        final List<Object> services = servicesInstaller.getServices(false);
        final NakedObjectsSystem system = new NakedObjectsSystem();
        system.setConfiguration(configuration);
        system.disableSplash(true);
        system.setAuthenticatorInstaller(new PasswordFileAuthenticatorInstaller());
        system.setContext(StaticContext.createInstance());
        system.setReflectorInstaller(new JavaNotifyingReflectorInstaller(container));
        system.setTemplateImageLoader(new AwtTemplateImageLoaderInstaller());
        system.setFixtureInstaller(fixturesInstaller);
        final InMemoryPersistorInstaller inMemoryPersistorInstaller = new InMemoryPersistorInstaller();
        final NakedObjectPersistor objectPersistor = inMemoryPersistorInstaller.createObjectPersistor(configuration);
        objectPersistor.setServices(services);
        system.setObjectPersistor(objectPersistor);
        system.init();
        ContainerInjector containerInjector = NakedObjectsContext.getObjectPersistor();
        containerInjector.initDomainObjects(services);
        XatSession session = new XatSession();
        system.connect(session);
        final SessionFixture sessionFixture = fixturesInstaller.getSessionFixture();
        if (sessionFixture != null) {
            sessionFixture.install();
        }
        NakedObjectsContext.getObjectPersistor().startTransaction();
        testViewer.addInteractionListener(documentProxyInteractionsListener);
        final Description description = methodDescription(method);
        Object test;
        try {
            test = createTest();
            containerInjector.initDomainObject(test);
        } catch (final InvocationTargetException e) {
            notifier.testAborted(description, e.getCause());
            return;
        } catch (final Exception e) {
            notifier.testAborted(description, e);
            return;
        }
        final TestMethod testMethod = wrapMethod(method);
        documentor.beginTest(method.getName());
        try {
            new MethodRoadie(test, testMethod, notifier, description).run();
        } finally {
            documentor.endTest();
        }
        system.disconnect(session);
        system.shutdown();
        servicesInstaller.removeService(testViewer);
    }

    /**
     * Taken from JMock's runner.
     */
    @Override
    protected TestMethod wrapMethod(final Method method) {
        return new TestMethod(method, getTestClass()) {

            @Override
            public void invoke(final Object testFixture) throws IllegalAccessException, InvocationTargetException {
                super.invoke(testFixture);
                if (mockeryField != null) {
                    mockeryOf(testFixture).assertIsSatisfied();
                }
            }
        };
    }

    private Documentor getDocumentor(final Class<?> testClass) throws InitializationError {
        Documentor documentor = getDocumentorFromConfiguration(testClass.getCanonicalName());
        if (documentor == null) {
            getDocumentorFromAnnotation(testClass);
        }
        if (documentor == null) {
            documentor = new InMemoryDocumentor(this.configuration, testClass.getCanonicalName());
        }
        return documentor;
    }

    public Documentor getDocumentorFromConfiguration(String classUnderTest) {
        final String factoryName = configuration.getString(Configuration.ROOT + DocumentorFactory.DOCUMENTOR_FACTORY);
        if (factoryName != null) {
            final DocumentorFactory documentorFactory = InstanceFactory.createInstance(factoryName, DocumentorFactory.class);
            documentorFactory.init(configuration);
            return documentorFactory.newDocumentor(classUnderTest);
        }
        return null;
    }

    private Documentor getDocumentorFromAnnotation(final Class<?> testClass) throws InitializationError {
        final DocumentUsing documentUsingAnnotation = testClass.getAnnotation(DocumentUsing.class);
        if (documentUsingAnnotation == null) {
            return null;
        }
        final Class<? extends Documentor> documentorClass = documentUsingAnnotation.value();
        try {
            Constructor<? extends Documentor> constructor = documentorClass.getConstructor(new Class[] { NakedObjectConfiguration.class, String.class });
            return constructor.newInstance(this.configuration, testClass.getCanonicalName());
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
        }
        try {
            return documentorClass.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * JMock code.
     * 
     * @param test
     * @return
     */
    protected Mockery mockeryOf(final Object test) {
        if (mockeryField == null) {
            return null;
        }
        try {
            final Mockery mockery = (Mockery) mockeryField.get(test);
            if (mockery == null) {
                throw new IllegalStateException(String.format("Mockery named '%s' is null", mockeryField.getName()));
            }
            return mockery;
        } catch (final IllegalAccessException e) {
            throw new IllegalStateException(String.format("cannot get value of field %s", mockeryField.getName()), e);
        }
    }

    /**
     * Adapted from JMock code.
     */
    static Field findFieldAndMakeAccessible(final Class<?> testClass, final Class<?> clazz) throws InitializationError {
        for (Class<?> c = testClass; c != Object.class; c = c.getSuperclass()) {
            for (final Field field : c.getDeclaredFields()) {
                if (clazz.isAssignableFrom(field.getType())) {
                    field.setAccessible(true);
                    return field;
                }
            }
        }
        return null;
    }

    @Override
    protected void finalize() throws Throwable {
        documentor.close();
        super.finalize();
    }
}
