package org.codehaus.janino.util;

import java.io.*;
import org.codehaus.janino.util.resource.*;

/**
 * A {@link ClassLoader} that uses a {@link org.codehaus.janino.util.resource.ResourceFinder}
 * to find ".class" files.
 */
public class ResourceFinderClassLoader extends ClassLoader {

    private final ResourceFinder resourceFinder;

    public ResourceFinderClassLoader(ResourceFinder resourceFinder, ClassLoader parent) {
        super(parent);
        this.resourceFinder = resourceFinder;
    }

    public ResourceFinder getResourceFinder() {
        return this.resourceFinder;
    }

    /**
     * @throws ClassNotFoundException
     */
    protected Class findClass(String className) throws ClassNotFoundException {
        Resource classFileResource = this.resourceFinder.findResource(ClassFile.getClassFileResourceName(className));
        if (classFileResource == null) throw new ClassNotFoundException(className);
        InputStream is;
        try {
            is = classFileResource.open();
        } catch (IOException ex) {
            throw new RuntimeException("Opening class file resource \"" + classFileResource.getFileName() + "\": " + ex.getMessage());
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[4096];
            for (; ; ) {
                int bytesRead = is.read(buffer);
                if (bytesRead == -1) break;
                baos.write(buffer, 0, bytesRead);
            }
        } catch (IOException ex) {
            throw new ClassNotFoundException("Reading class file from \"" + classFileResource + "\"", ex);
        } finally {
            try {
                is.close();
            } catch (IOException ex) {
            }
        }
        byte[] ba = baos.toByteArray();
        Class clazz = super.defineClass(null, ba, 0, ba.length);
        if (!clazz.getName().equals(className)) {
            throw new ClassNotFoundException(className);
        }
        return clazz;
    }
}
