package net.sf.joafip.classloader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MyClassLoader extends ClassLoader {

    public MyClassLoader() {
        super();
    }

    public MyClassLoader(final ClassLoader parent) {
        super(parent);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        System.out.println("\nfindClass " + name + "\n");
        return super.findClass(name);
    }

    @Override
    protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        System.out.println("\nloadClass " + name + "\n");
        final Class<?> defineClass;
        final Class<?> loadClass = super.loadClass(name, resolve);
        if (name.startsWith("java")) {
            defineClass = loadClass;
        } else {
            final String resourceName = "/" + name.replace('.', '/') + ".class";
            InputStream inputStream = loadClass.getResourceAsStream(resourceName);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int v;
            try {
                while ((v = inputStream.read()) != -1) {
                    byteArrayOutputStream.write(v);
                }
                inputStream.close();
            } catch (IOException exception) {
                throw new ClassNotFoundException(exception.getMessage());
            }
            byte[] byteCode = byteArrayOutputStream.toByteArray();
            defineClass = defineClass(name, byteCode, 0, byteCode.length);
        }
        return defineClass;
    }
}
