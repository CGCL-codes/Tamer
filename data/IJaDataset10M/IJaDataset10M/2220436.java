package fildiv.jremcntl.server.core.env;

import java.util.Hashtable;
import java.util.Map;
import fildiv.jremcntl.common.util.log.Logger;
import fildiv.jremcntl.server.core.ext.Environment;
import fildiv.jremcntl.server.core.ext.JRemExtension;
import fildiv.jremcntl.server.core.ext.ExtensionFactory;
import fildiv.jremcntl.server.core.props.Property;
import fildiv.jremcntl.server.core.props.PropertyMapper;

public class ExtensionLoader {

    private JRemEnv env;

    private ExtensionClassLoader extClsLoader;

    private Map factories;

    protected ExtensionLoader() {
        this.env = JRemEnv.getInstance();
        extClsLoader = new ExtensionClassLoader();
        factories = new Hashtable();
    }

    protected void addRepository(String repo) {
        extClsLoader.addRepository(repo);
    }

    protected ExtensionFactory getCachedExtensionFactory(String className) {
        return (ExtensionFactory) factories.get(className);
    }

    protected void addExtensionFactoryToCache(ExtensionFactory factory, String className) {
        factories.put(className, factory);
    }

    public JRemExtension getExtension(String className) {
        ExtensionFactory fact = getCachedExtensionFactory(className);
        if (fact != null) return createExtension(fact);
        fact = createExtensionFactory(className);
        if (fact == null) {
            env.getLogger().error("Unable to create factory with class name: " + className);
            return null;
        }
        return createExtension(fact);
    }

    private ExtensionFactory createExtensionFactory(String className) {
        Logger log = env.getLogger();
        ExtensionFactory fact;
        try {
            Class fc = extClsLoader.loadClass(className);
            fact = (ExtensionFactory) fc.newInstance();
            registerExtensionProperties(fact);
            addExtensionFactoryToCache(fact, className);
        } catch (ClassNotFoundException e) {
            log.error(e);
            return null;
        } catch (InstantiationException e) {
            log.error(e);
            return null;
        } catch (IllegalAccessException e) {
            log.error(e);
            return null;
        }
        return fact;
    }

    private JRemExtension createExtension(ExtensionFactory ef) {
        JRemExtension ext = ef.createExtension(createSafeEnv());
        return ext;
    }

    private void registerExtensionProperties(ExtensionFactory extFact) {
        Property properties[] = extFact.getProperties();
        if (properties == null) return;
        PropertyMapper pm = env.getPropertiesMapper();
        for (int index = 0; index < properties.length; ++index) {
            Property p = properties[index];
            pm.registerProperty(extFact.getClass(), p);
        }
    }

    protected Environment createSafeEnv() {
        return new ExtensionEnv();
    }
}
