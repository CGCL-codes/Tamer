package org.dbe.sdl.util;

import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.apache.log4j.Logger;

/**
 * Utilty class for reading jar files.
 */
public class SdlJarReaderUtil {

    /** for deployment logging purposes */
    private static final Logger logger = Logger.getLogger(SdlJarReaderUtil.class.getName());

    /** jar file being read */
    private JarFile mJarFile;

    /**
     * Constructor.
     * @param aUrl points to a jar resource.
     * @throws IOException
     */
    public SdlJarReaderUtil(URL aUrl) throws IOException {
        logger.debug("SdlJarReaderUtil() " + aUrl);
        mJarFile = new JarFile(aUrl.getFile());
    }

    /**
     * Return the JarEntry with the given name.
     * @param aName
     * @return named entry or null if none is found
     */
    public JarEntry getEntry(String aName) {
        for (Enumeration enums = mJarFile.entries(); enums.hasMoreElements(); ) {
            JarEntry jarEntry = (JarEntry) enums.nextElement();
            if (aName.equals(jarEntry.getName())) {
                return jarEntry;
            }
        }
        return null;
    }

    /**
     * Collection of JarEntry names acceptable by the filter.
     * @param aFilter select entries based on filename
     * @return matching jar entry file names
     */
    public Collection getEntryNames(FilenameFilter aFilter) {
        System.out.println("getEntryNames() with filter");
        List matches = new ArrayList();
        for (Enumeration enums = mJarFile.entries(); enums.hasMoreElements(); ) {
            JarEntry jarEntry = (JarEntry) enums.nextElement();
            if (aFilter.accept(null, jarEntry.getName())) {
                System.out.println("adding file to filter " + jarEntry.getName());
                matches.add(jarEntry.getName());
            }
        }
        return matches;
    }

    /**
     * Return collection of JarEntry objects acceptable by the filter.
     * @param aFilter select entries based on filename
     * @return match jar etnry objects
     */
    public Collection getEntries(FilenameFilter aFilter) {
        logger.debug("getEntries() ");
        List matches = new ArrayList();
        for (Enumeration enums = mJarFile.entries(); enums.hasMoreElements(); ) {
            JarEntry jarEntry = (JarEntry) enums.nextElement();
            if (aFilter.accept(null, jarEntry.getName())) {
                logger.info("filter accepted file " + jarEntry.getName());
                matches.add(jarEntry);
            }
        }
        return matches;
    }

    /**
     * Access input stream to jar entry
     * @param aEntry jar entry to read
     * @return input stream to jar entry
     * @throws IOException
     */
    public InputStream getInputStream(JarEntry aEntry) throws IOException {
        return mJarFile.getInputStream(aEntry);
    }

    /**
     * Release any open resources.
     */
    public void close() {
        SdlCloser.close(mJarFile);
    }
}
