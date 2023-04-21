package de.grogra.xl.property;

import java.util.HashMap;

public class RuntimeModelFactory {

    private static RuntimeModelFactory factory;

    public static synchronized RuntimeModelFactory getInstance() {
        if (factory == null) {
            factory = new RuntimeModelFactory();
        }
        return factory;
    }

    public static synchronized void setInstance(RuntimeModelFactory factory) {
        RuntimeModelFactory.factory = factory;
    }

    private final HashMap<String, RuntimeModel> models = new HashMap<String, RuntimeModel>();

    public synchronized boolean defineModel(String classFileConstant, RuntimeModel model) {
        models.put(classFileConstant, model);
        return true;
    }

    public final synchronized RuntimeModel modelForName(String name, ClassLoader loader) {
        RuntimeModel m;
        if ((m = models.get(name)) == null) {
            m = modelForNameImpl(name, loader);
            if (m == null) {
                throw new NoClassDefFoundError(name);
            }
            models.put(name, m);
        }
        return m;
    }

    protected RuntimeModel modelForNameImpl(String classFileConstant, ClassLoader loader) {
        try {
            int i = classFileConstant.indexOf(':');
            RuntimeModel m = (RuntimeModel) Class.forName((i < 0) ? classFileConstant : classFileConstant.substring(0, i), true, loader).newInstance();
            m.initialize((i < 0) ? null : classFileConstant.substring(i + 1));
            return m;
        } catch (Exception e) {
            return null;
        }
    }
}
