package fr.cantor.commore.comm;

import fr.cantor.commore.Commore;

public class ClassLoaderInterfaceFactory implements InterfaceFactory {

    private Class<? extends Interface> interfaceClass;

    private final String interfaceClassName;

    private final ClassLoader classLoader;

    public ClassLoaderInterfaceFactory(String interfaceClassName, ClassLoader loader) {
        this.interfaceClassName = interfaceClassName;
        if (loader == null) {
            this.classLoader = this.getClass().getClassLoader();
        } else {
            this.classLoader = loader;
        }
    }

    public ClassLoaderInterfaceFactory(String interfaceClassName) {
        this(interfaceClassName, null);
    }

    @SuppressWarnings("unchecked")
    private boolean loadClassIfNone() {
        if (interfaceClass != null) {
            return true;
        }
        try {
            interfaceClass = (Class<? extends Interface>) Class.forName(interfaceClassName, true, classLoader);
            return true;
        } catch (ClassNotFoundException e) {
            Commore.logger.warning("Unable to load the class " + interfaceClassName + " from this factory");
            return false;
        }
    }

    public Interface create(String interfaceClassName, Service service) {
        boolean classLoaded = loadClassIfNone();
        if (classLoaded) {
            try {
                return interfaceClass.newInstance();
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
}
