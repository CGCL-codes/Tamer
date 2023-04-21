package huf.io;

import java.io.InputStream;
import java.io.IOException;

/**
 * Class for loading resources from inside JAR archives.
 */
public class ResourceLoader {

    /** No instantiation. */
    private ResourceLoader() {
    }

    /**
	 * Open stream to specific file inside JAR archive.
	 *
	 * @param fileName name of file inside JAR archive
	 * @param anchorClassName name of any class inside the same JAR archive as
	 *        the file being opened
	 * @return input stream for file inside JAR archive
	 * @throws IOException when class can't be instantiated
	 */
    public static InputStream openStream(String fileName, String anchorClassName) throws IOException {
        try {
            return openStream(fileName, Class.forName(anchorClassName));
        } catch (ClassNotFoundException cnfe) {
            throw new IOException("Unable to find class: " + cnfe);
        }
    }

    /**
	 * Open stream to specific file inside JAR archive.
	 *
	 * @param fileName name of file inside JAR archive
	 * @param anchorClass instance of any class inside the same JAR archive as
	 *        the file being opened
	 * @return input stream for file inside JAR archive
	 */
    public static InputStream openStream(String fileName, Class<?> anchorClass) {
        return anchorClass.getClassLoader().getResourceAsStream(fileName);
    }

    /**
	 * Open stream to specific file inside JAR archive loaded by default class loader.
	 *
	 * @param fileName name of file inside JAR archive
	 * @return input stream for file inside JAR archive
	 */
    public static InputStream openStream(String fileName) {
        return ClassLoader.getSystemClassLoader().getResourceAsStream(fileName);
    }
}
