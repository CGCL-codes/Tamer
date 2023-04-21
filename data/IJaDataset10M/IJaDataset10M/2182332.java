package org.ujac.util.io;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Title:       ClassPathResourceLoader<br>
 * Description: A resource loader which loads resources from the given class loader.<br>
 * 
 * @author lauerc
 */
public class ClassPathResourceLoader implements ResourceLoader {

    /** The I/O buffer size. */
    public static final int IO_BUFFER_SIZE = 2048;

    /** The class loader from which to load the resources. */
    private ClassLoader classLoader = null;

    /** The path prefix to prepend prepend before the location. */
    private String pathPrefix = null;

    /**
   * Constructs a ClassPathResourceLoader instance with specific attributes.
   * @param classLoader The class loader.
   * @exception IOException If the given rootDirectory does not exist or is no directory. 
   */
    public ClassPathResourceLoader(ClassLoader classLoader) throws IOException {
        if (classLoader == null) {
            throw new IOException("The class loader mustn't be null.");
        }
        this.classLoader = classLoader;
    }

    /**
   * Constructs a ClassPathResourceLoader instance with specific attributes.
   * @param classLoader The class loader.
   * @param pathPrefix The path prefix to prepend before the location.
   * @exception IOException If the given rootDirectory does not exist or is no directory. 
   */
    public ClassPathResourceLoader(ClassLoader classLoader, String pathPrefix) throws IOException {
        this(classLoader);
        this.pathPrefix = pathPrefix;
    }

    /**
   * Loads a binary resource from the class loader.
   * @param location The location of the resource.
   * @return The loaded resource.
   * @exception IOException In case the loading of the resource failed.
   */
    public byte[] loadResource(String location) throws IOException {
        if ((location == null) || (location.length() == 0)) {
            throw new IOException("The given resource location must not be null and non empty.");
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        String path = location;
        if (pathPrefix != null) {
            path = pathPrefix + "/" + location;
        }
        InputStream cpis = classLoader.getResourceAsStream(path);
        BufferedInputStream is = null;
        try {
            is = new BufferedInputStream(cpis, IO_BUFFER_SIZE);
            byte[] buffer = new byte[IO_BUFFER_SIZE];
            int numRead = is.read(buffer, 0, IO_BUFFER_SIZE);
            while (numRead > 0) {
                bos.write(buffer, 0, numRead);
                numRead = is.read(buffer, 0, IO_BUFFER_SIZE);
            }
        } finally {
            if (is != null) {
                is.close();
            }
            if (cpis != null) {
                cpis.close();
            }
        }
        return bos.toByteArray();
    }

    /**
   * Checks whether or not the desired resource exists. 
   * @param location The location of the resource.
   * @return true in case the given resource could be located, else false.
   */
    public boolean resourceExists(String location) {
        if ((location == null) || (location.length() == 0)) {
            return false;
        }
        String path = location;
        if (pathPrefix != null) {
            path = pathPrefix + "/" + location;
        }
        InputStream cpis = classLoader.getResourceAsStream(path);
        if (cpis == null) {
            return false;
        }
        try {
            cpis.close();
        } catch (IOException ex) {
        }
        return true;
    }
}
