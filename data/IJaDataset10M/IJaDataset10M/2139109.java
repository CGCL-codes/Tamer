package com.googlecode.xmlzen.utils;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.googlecode.xmlzen.XmlZenException;

/**
 * File manipulation utilities
 * 
 * <p>Mostly used for reading Files into Strings, finding Files in class path<p> 
 * 
 * @author Tomas Varaneckas &lt;tomas.varaneckas@gmail.com&gt;
 * @version $Id: FileUtils.java 48 2009-09-27 18:45:52Z tomas.varaneckas $
 */
public abstract class FileUtils {

    /**
     * Logger
     */
    private static final Log log = LogFactory.getLog(FileUtils.class);

    /**
	 * File buffer - 2048 bytes.
	 */
    private static final int BUFFER = 2048;

    /**
	 * Reads a {@link File} and returns the contents as {@link String}
	 * 
	 * @param file File to read
	 * @param charset Charset of this File
	 * @return File contents as String
	 */
    public static String readFile(final File file, final String charset) {
        InputStream in = null;
        try {
            if (!file.isFile()) {
                return null;
            }
            in = new FileInputStream(file);
            final BufferedInputStream buffIn = new BufferedInputStream(in);
            final byte[] buffer = new byte[(int) Math.min(file.length(), BUFFER)];
            int read;
            final StringBuilder result = new StringBuilder();
            while ((read = buffIn.read(buffer)) != -1) {
                result.append(new String(buffer, 0, read, charset));
            }
            return result.toString();
        } catch (final Exception e) {
            throw new XmlZenException("Failed reading file: " + file, e);
        } finally {
            close(in);
        }
    }

    /**
	 * Reads a {@link File} and returns it's contents as String. Uses the 
	 * default system charset.
	 * 
	 * @see #readFile(File, String)
	 * @param file File to read
	 * @return File contents as String
	 */
    public static String readFile(final File file) {
        return readFile(file, Charset.defaultCharset().name());
    }

    /**
	 * Closes a {@link Closeable}, for instance a {@link FileInputStream}.
	 * <p>
	 * Null-safe, Supresses the IOException.</p>
	 * 
	 * @param closeable A {@link Closeable} object to be closed
	 */
    public static void close(final Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (final IOException e) {
            log.debug("Failed closing " + closeable + ". Ignoring exception.");
        }
    }

    /**
	 * Gets a {@link File} from a String that represents file path which is 
	 * relative to current Class Path
	 * 
	 * @param path Path to File
	 * @param classLoader {@link ClassLoader} that should be able to see the 
	 *         file
	 * @return {@link File} object or null if nothing is found
	 */
    public static File getClassPathFile(final String path, final ClassLoader classLoader) {
        final URL url = classLoader.getResource(path);
        if (url == null) {
            return null;
        }
        return new File(url.getFile());
    }

    /**
	 * Gets a {@link File} form a String that represents file path which is
	 * relative to current Class Path. Uses system's default ClassLoader.
	 * 
	 * @see #getClassPathFile(String, ClassLoader)
	 * @param path Path to File
	 * @return {@link File} object or null if nothing is found
	 */
    public static File getClassPathFile(final String path) {
        return getClassPathFile(path, ClassLoader.getSystemClassLoader());
    }
}
