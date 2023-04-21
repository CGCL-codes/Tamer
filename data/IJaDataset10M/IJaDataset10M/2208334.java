package framework.aspect;

import framework.IOStream.FileCache;
import framework.aspect.annotations.Managed;
import framework.log.Log;
import java.io.InputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.HashMap;

public class AspectClassLoader extends ClassLoader {

    private HashMap<String, Class<?>> classes = new HashMap<String, Class<?>>();

    private ProtectionDomain protectionDomain = getClass().getProtectionDomain();

    private FileCache cache = new FileCache(false);

    private ClassFileTransformer transformer;

    @Override
    public synchronized Class<?> loadClass(String className, boolean resolve) throws ClassNotFoundException {
        Class<?> clazz;
        clazz = classes.get(className);
        if (clazz == null) clazz = findLoadedClass(className);
        if (clazz != null) return clazz;
        clazz = super.loadClass(className, resolve);
        for (int i = 0; i < AspectClassLoaderExclude.paths.length; i++) if (className.startsWith(AspectClassLoaderExclude.paths[i])) {
            classes.put(className, clazz);
            return clazz;
        }
        String clsFile = className.replace('.', '/') + ".class";
        InputStream in1 = getResourceAsStream(clsFile);
        if (in1 == null) throw new ClassNotFoundException();
        byte[] classBytes = null;
        classBytes = cache.getBinaryResource(clsFile, in1);
        if (classBytes == null) throw new ClassNotFoundException("Cannot resolve " + className);
        try {
            if (transformer != null && clazz.isAnnotationPresent(Managed.class)) {
                byte[] transformedBytes = transformer.transform(this, className, clazz, protectionDomain, classBytes);
                if (transformedBytes.length > 0) classBytes = transformedBytes;
            }
        } catch (IllegalClassFormatException exception) {
            Log.out("Transformation failed for class " + className);
            Log.out(exception);
        }
        try {
            clazz = defineClass(className, classBytes, 0, classBytes.length, protectionDomain);
            if (resolve) resolveClass(clazz);
        } catch (SecurityException e) {
            clazz = super.loadClass(className, resolve);
        }
        if (clazz == null) throw new ClassNotFoundException();
        classes.put(className, clazz);
        return clazz;
    }

    public void setTransformer(ClassFileTransformer transformer) {
        this.transformer = transformer;
    }
}
