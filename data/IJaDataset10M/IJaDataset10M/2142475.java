package org.hydra.beans.abstracts;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hydra.utils.Constants;
import org.hydra.utils.abstracts.ALogger;

public abstract class APropertyLoader extends ALogger {

    /**
	 * Looks up a resource named 'name' in the classpath. The resource must map
	 * to a file with .properties extention. The name is assumed to be absolute
	 * and can use either "/" or "." for package segment separation with an
	 * optional leading "/" and optional ".properties" suffix. Thus, the
	 * following names refer to the same resource:
	 * 
	 * <pre>
	 * some.pkg.Resource
	 * some.pkg.Resource.properties
	 * some/pkg/Resource
	 * some/pkg/Resource.properties
	 * /some/pkg/Resource
	 * /some/pkg/Resource.properties
	 * </pre>
	 * 
	 * @param name
	 *            classpath resource name [may not be null]
	 * @param loader
	 *            classloader through which to load the resource [null is
	 *            equivalent to the application loader]
	 * 
	 * @return resource converted to java.util.Properties [may be null if the
	 *         resource was not found and THROW_ON_LOAD_FAILURE is false]
	 * @throws IllegalArgumentException
	 *             if the resource was not found and THROW_ON_LOAD_FAILURE is
	 *             true
	 */
    private static Properties loadProperties(String name, ClassLoader loader) {
        if (name == null) throw new IllegalArgumentException("null input: name");
        if (name.startsWith("/")) name = name.substring(1);
        if (name.endsWith(SUFFIX)) name = name.substring(0, name.length() - SUFFIX.length());
        Properties result = null;
        InputStream in = null;
        try {
            if (loader == null) loader = ClassLoader.getSystemClassLoader();
            if (LOAD_AS_RESOURCE_BUNDLE) {
                name = name.replace('/', '.');
                final ResourceBundle rb = ResourceBundle.getBundle(name, Locale.getDefault(), loader);
                result = new Properties();
                for (Enumeration<?> keys = rb.getKeys(); keys.hasMoreElements(); ) {
                    final String key = (String) keys.nextElement();
                    final String value = rb.getString(key);
                    result.put(key, value);
                }
            } else {
                name = name.replace('.', '/');
                if (!name.endsWith(SUFFIX)) name = name.concat(SUFFIX);
                in = loader.getResourceAsStream(name);
                if (in != null) {
                    result = parsePropertyFile(in);
                }
            }
        } catch (Exception e) {
            result = null;
        } finally {
            if (in != null) try {
                in.close();
            } catch (Throwable ignore) {
            }
        }
        if (THROW_ON_LOAD_FAILURE && (result == null)) {
            throw new IllegalArgumentException("could not load [" + name + "]" + " as " + (LOAD_AS_RESOURCE_BUNDLE ? "a resource bundle" : "a classloader resource"));
        }
        return result;
    }

    public static Properties parsePropertyFile(String filePath) {
        if (filePath == null) return (null);
        File file = new File(filePath);
        if (!file.exists()) return (null);
        Properties result = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            result = parsePropertyFile(fis);
        } catch (Exception e) {
            _log.error(e.getMessage());
        }
        return result;
    }

    public static Properties parsePropertyFile(InputStream in) throws UnsupportedEncodingException, IOException {
        Properties result = new Properties();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, Constants._utf_8));
        String line = null;
        String curKey = null;
        String curValue = null;
        int NOT_FOUND = -1;
        int found = NOT_FOUND;
        while ((line = reader.readLine()) != null) {
            if (line.trim().isEmpty() || line.matches("^[#]+.*")) continue;
            if (!line.matches("^[\\s]+.*")) {
                found = line.indexOf("=");
                if (found != NOT_FOUND) {
                    savePreviousKeyValue(result, curKey, curValue);
                    curKey = line.substring(0, found - 1).trim();
                    curValue = line.substring(found + 1, line.length()).trim();
                } else {
                    curValue += "\n";
                    curValue += line;
                }
            } else {
                curValue += " " + line.substring(1).trim();
            }
        }
        savePreviousKeyValue(result, curKey, curValue);
        return result;
    }

    private static void savePreviousKeyValue(Properties inProp, String inKey, String inValue) {
        if ((inKey != null) && (inValue != null)) {
            inProp.setProperty(inKey, inValue);
            _log.debug(String.format("KEY(%s) and VALUE(%s) found", inKey, inValue));
        }
    }

    /**
	 * A convenience overload of {@link #loadProperties(String, ClassLoader)}
	 * that uses the current thread's context classloader.
	 */
    public static Properties loadProperties(final String name) {
        _log.debug("Loading property file " + name);
        return loadProperties(name, Thread.currentThread().getContextClassLoader());
    }

    private static final Log _log = LogFactory.getLog(org.hydra.executors.Executor.class);

    private static final boolean THROW_ON_LOAD_FAILURE = true;

    private static final boolean LOAD_AS_RESOURCE_BUNDLE = false;

    public static final String SUFFIX = ".properties";
}
