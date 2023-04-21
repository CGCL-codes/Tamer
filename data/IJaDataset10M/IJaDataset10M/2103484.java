package org.cumt;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import junit.framework.TestCase;

/**
 * I usually forget to synchronize message files :-D
 * @author <a href="cdescalzi2001@yahoo.com.ar">Carlos Descalzi</a>
 */
public class BundleTestCase extends TestCase {

    private final Log log = LogFactory.getLog(getClass());

    public void testNoMissingMessages() {
        String firstFile = null;
        Set<?> firstKeys = null;
        for (Map.Entry<String, Set<?>> entry : getAllBundleKeys().entrySet()) {
            if (firstKeys == null) {
                firstFile = entry.getKey();
                firstKeys = entry.getValue();
            } else {
                assertTrue("Checking " + entry.getKey() + " against " + firstFile, firstKeys.equals(entry.getValue()));
            }
        }
    }

    /**
	 * Scan classpath for all messages*.properties and returns a map of file names and their keys
	 * @return
	 */
    private Map<String, Set<?>> getAllBundleKeys() {
        File[] files = new File(getClass().getResource("/").getFile()).listFiles(new FileFilter() {

            public boolean accept(File pathname) {
                return pathname.isFile() && pathname.getName().matches("messages[^\\.]*\\.properties");
            }
        });
        Map<String, Set<?>> keysByFile = new HashMap<String, Set<?>>();
        for (File file : files) {
            log.info("Adding :" + file.getName());
            keysByFile.put(file.getName(), getMessageKeys(file));
        }
        return keysByFile;
    }

    /**
	 * Read property file keys
	 * @param file
	 * @return
	 */
    @SuppressWarnings("unchecked")
    private Set getMessageKeys(File file) {
        Properties properties = new Properties();
        try {
            InputStream input = new FileInputStream(file);
            properties.load(input);
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
        return new HashSet(properties.keySet());
    }
}
