package com.mycila.plugin.spi.aop;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class BridgeClassLoader extends ClassLoader {

    private final String[] packages;

    private final ClassLoader delegate;

    public BridgeClassLoader(ClassLoader parent, ClassLoader delegate, String... packages) {
        super(parent);
        this.packages = packages;
        this.delegate = delegate;
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        for (String p : packages) {
            if (name.startsWith(p)) {
                try {
                    Class<?> clazz = delegate.loadClass(name);
                    if (resolve) resolveClass(clazz);
                    return clazz;
                } catch (Exception e) {
                }
                return super.loadClass(name, resolve);
            }
        }
        return super.loadClass(name, resolve);
    }
}
