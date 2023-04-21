package org.objectwiz.plugin.hibernate;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.objectwiz.core.Application;
import org.objectwiz.core.facet.persistence.PersistenceUnitProxy;
import org.objectwiz.core.facet.persistence.PersistenceUnit;
import org.objectwiz.utils.FileUtils;
import org.objectwiz.utils.MirrorProxy;
import org.objectwiz.utils.TypeUtils;

/**
 * {@link PersistenceProxy} for Hibernate.
 * 
 * @author Benoit Del Basso <benoit.delbasso at helmet.fr>
 */
public class HibernatePersistenceUnitProxy extends PersistenceUnitProxy {

    private static final Log logger = LogFactory.getLog(HibernatePersistenceUnitProxy.class);

    private static final List<String> SUPPORTED = Arrays.asList("org.hibernate.ejb.EntityManagerFactoryImpl", "org.hibernate.engine.SessionFactoryImplementor");

    @Override
    public PersistenceUnit createUnit(Application application, String name, Object implementation) throws Exception {
        HibernateConnector connector = null;
        for (String supported : SUPPORTED) {
            if (TypeUtils.isInstanceOf(supported, implementation.getClass())) {
                connector = createConnector(implementation);
            }
        }
        if (connector == null) return null;
        logger.info("Found Hibernate unit, version: " + connector.getHibernateVersion());
        return new HibernatePersistenceUnit(application, name, connector);
    }

    protected HibernateConnector createConnector(Object implementation) throws Exception {
        final ClassLoader proxyClassLoader = this.getClass().getClassLoader();
        final ClassLoader targetLoader = implementation.getClass().getClassLoader();
        ClassLoader customLoader = new ClassLoader(targetLoader) {

            @Override
            protected Class<?> findClass(String name) throws ClassNotFoundException {
                if (logger.isTraceEnabled()) {
                    logger.trace("Using custom class loader for: " + name);
                }
                if (!name.startsWith(HibernateConnector.class.getPackage().getName())) {
                    throw new ClassNotFoundException(name);
                }
                String resourceName = name.replace(".", "/") + ".class";
                InputStream in = proxyClassLoader.getResourceAsStream(resourceName);
                if (in == null) {
                    throw new RuntimeException("Unable to load class file: " + resourceName);
                }
                try {
                    byte[] bytecode = FileUtils.readInputStreamAsBytes(in);
                    return defineClass(name, bytecode, 0, bytecode.length);
                } catch (Exception e) {
                    throw new RuntimeException("Error loading bytecode for: " + name, e);
                }
            }
        };
        System.setProperty("ANTLR_USE_DIRECT_CLASS_LOADING", "true");
        Class connectorClass = customLoader.loadClass(GenericHibernateConnector.class.getName());
        Constructor constructor = connectorClass.getConstructor(new Class[] { Object.class });
        Object connector = constructor.newInstance(new Object[] { implementation });
        return new MirrorProxy<HibernateConnector>(HibernateConnector.class, connector).getProxy();
    }
}
