package org.sun.dbe.tools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.net.URL;
import org.dbe.servent.tools.ObjectSerializer;

/**
 * Helper to write and read Objects
 * 
 * @author bob
 */
public class ObjectStream implements ObjectSerializer {

    public class ObjectInputStreamWithClassLoader extends ObjectInputStream {

        @Override
        protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
            String className = desc.getName();
            Class<?> class1 = null;
            try {
                class1 = super.resolveClass(desc);
            } catch (ClassNotFoundException cnfex) {
                class1 = null;
            } catch (NoClassDefFoundError ncdfer) {
                class1 = null;
            }
            if (class1 == null && classLoader != null) {
                class1 = classLoader.loadClass(className);
            }
            if (class1 == null) {
                throw new ClassNotFoundException(className);
            }
            return class1;
        }

        private ClassLoader classLoader;

        public ObjectInputStreamWithClassLoader(InputStream stream, ClassLoader classLoader) throws IOException {
            super(stream);
            this.classLoader = classLoader;
        }
    }

    private ClassLoader classLoader;

    public ObjectStream(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    /**
	 * @see org.dbe.servent.tools.ObjectSerializer#getObject(byte[])
	 */
    public Object getObject(byte[] buffer) throws IOException, ClassNotFoundException {
        return getObject(buffer, 0, buffer.length);
    }

    /**
	 * @see org.dbe.servent.tools.ObjectSerializer#getBytes(java.lang.Object)
	 */
    public byte[] getBytes(Object obj) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);
        return baos.toByteArray();
    }

    /**
	 * @see org.dbe.servent.tools.ObjectSerializer#getObject(byte[], int, int)
	 */
    public Object getObject(byte[] buffer, int begin, int length) throws IOException, ClassNotFoundException {
        Object obj = null;
        ObjectInputStream oos = new ObjectInputStreamWithClassLoader(new ByteArrayInputStream(buffer, begin, length), classLoader);
        ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
        ClassLoader classClassLoader = getClass().getClassLoader();
        dumpClassLoader(currentClassLoader);
        ClassLoader parentOfCurrentClassLoader = currentClassLoader.getParent();
        if (parentOfCurrentClassLoader != null) {
            dumpClassLoader(parentOfCurrentClassLoader);
        }
        obj = oos.readObject();
        if (obj == null) {
        }
        if (obj != null) {
            Class cl = obj.getClass();
            ClassLoader which = cl.getClassLoader();
        }
        return obj;
    }

    private void doTest() {
    }

    private void dumpClassLoader(ClassLoader currentClassLoader) {
        if (1 == 1) return;
        if (currentClassLoader instanceof java.net.URLClassLoader) {
            URL[] urls = ((java.net.URLClassLoader) currentClassLoader).getURLs();
            if (urls == null) {
                System.out.println("The urls of the current classloader is a null set");
            } else {
                System.out.println("There are " + urls.length + " urls");
                for (int i = 0; i < urls.length; i++) {
                    System.out.println(urls[i]);
                }
            }
        }
    }
}
