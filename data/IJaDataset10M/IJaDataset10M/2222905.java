package org.langkiss.logging;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.langkiss.util.StringUtil;

/**
 *
 * @author Tom Wiedenhoeft
 */
public class PropertiesReader {

    private Logger logger;

    /** Creates a new instance of TestProperties */
    public PropertiesReader() {
        logger = Logger.getLogger(this.getClass().getName());
    }

    public void readPropertiesFileIntoSystem(String file) throws Exception {
        Properties props = this.getProperties(file);
        if (props != null) {
            Enumeration e = props.keys();
            while (e.hasMoreElements()) {
                String key = (String) e.nextElement();
                String value = props.getProperty(key);
                logger.log(Level.FINEST, "Adding found property to System Properties: key = ''{0}'', value = '' {1}''.", new Object[] { key, value });
                System.setProperty(key.trim(), value.trim());
            }
        }
    }

    public Properties getProperties(String file) throws IOException {
        File f = new File(file);
        Properties props = new Properties();
        BufferedInputStream in = null;
        logger.log(Level.FINEST, "Reading properties from file: {0}", file);
        try {
            in = new BufferedInputStream(new FileInputStream(file));
            props.load(in);
            in.close();
            in = null;
        } catch (IOException e) {
            logger.log(Level.WARNING, "Failed to read file " + file, e);
            throw e;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
        return props;
    }

    /**
     * Replaces the Java System Properties in a String.
     * Replaces more the one Java Properties and all occurances of them.
     * Example:  "${user.home}${file.separator}tmp${file.separator}${java.version}"
     *
     * @param s The String where the replacement should take place.
     * Example: ${user.home}/tmp
     * @return The String with all replaced Java Properties.
     * Example: c:/Documents and Settings/myaccount/tmp
     */
    public String replaceSystemProperties(String s) {
        String patternString = "(\\$\\{)(.+?)(\\})";
        Pattern pattern = Pattern.compile(patternString);
        logger.log(Level.FINE, "Try to find a Java System Property with pattern = ''{0}'' in String ''{1}''.", new Object[] { patternString, s });
        Matcher matcher = pattern.matcher(s);
        while (matcher.find()) {
            String systemPropertyKey = matcher.group(2);
            String systemPropertyValue = System.getProperty(systemPropertyKey);
            if (systemPropertyValue == null) {
                logger.log(Level.FINER, "Could not find a Java System Property for key = ''{0}''.", systemPropertyKey);
            } else {
                logger.log(Level.FINER, "Could find a Java System Property for key = ''{0}''. Found value = ''{1}''.", new Object[] { systemPropertyKey, systemPropertyValue });
                StringUtil sUtil = new StringUtil();
                systemPropertyValue = sUtil.replaceBackSlashesByForwardSlashes(systemPropertyValue);
                String systemPropertyKeyPatternString = "(\\$\\{)(" + systemPropertyKey + ")(\\})";
                s = s.replaceAll(systemPropertyKeyPatternString, systemPropertyValue);
                matcher = pattern.matcher(s);
            }
        }
        return s;
    }

    /**
     * Find the value for a key in a properties file.
     * 
     * @param file The properties file
     * @param key The key to search the value for. Is a regular expression.
     * @return The value for the key
     * @throws Exception
     */
    public String getProperty(String file, String key) throws Exception {
        Properties props = this.getProperties(file);
        String value = null;
        if (props != null) {
            logger.log(Level.FINEST, "Compiling pattern ''{0}''...", key);
            Pattern p = Pattern.compile(key);
            Enumeration keyEnum = props.keys();
            while (keyEnum.hasMoreElements()) {
                String propertyKey = (String) keyEnum.nextElement();
                logger.log(Level.FINEST, "Matching pattern ''{0}'' for key ''{1}''...", new Object[] { key, propertyKey });
                Matcher m = p.matcher(propertyKey);
                while (m.find()) {
                    value = props.getProperty(propertyKey);
                    logger.log(Level.FINEST, "Yes, found key ''{0}''.", propertyKey);
                    return value;
                }
            }
        }
        logger.log(Level.FINEST, "Nothing found for key (regular expression) = ''{0}''.", key);
        return value;
    }

    /**
     * Time dependend find of a property in a properties file.
     * The method tries to find a value for a key. The key is a regular expression.
     * The found value is matched against an expected value. If the found value
     * does not match the expected one the method keeps finding but not longer than
     * the maximum wait time (milliseconds). The internal retry time is 1000 milliseconds.
     * <br><br>
     * java.io.IOExceptions are catched and ignored. Why? Sometimes another process
     * blocks parts of the properties file. This causes an IOException.
     * 
     * @param file The properties file as absolut path
     * @param key The key of the property (a regular expression)
     * @param expectedValue The expected value
     * @param maximumWaitTime The maximum time in milliseconds the method tries to find
     * the expected value for the key.
     * @return The value of the property
     * @throws Exception
     */
    public synchronized String waitForProperty(String file, String key, String expectedValue, long maximumWaitTime) throws Exception {
        logger.log(Level.FINEST, "Starting to wait for key ''{0}'' with expected value ''{1}'' in file ''{2}'' with a maximum waiting time of ''{3}''.", new Object[] { key, expectedValue, file, maximumWaitTime });
        long startTime = System.currentTimeMillis();
        String foundString = null;
        while (foundString == null) {
            try {
                foundString = this.getProperty(file, key);
            } catch (IOException e) {
                logger.log(Level.FINE, "Ignoring the exception ''{0}'' - while getting key (regular expression) ''{1}'' in file ''{2}''.", new Object[] { e.getMessage(), key, file });
            } catch (Exception e) {
                logger.log(Level.FINE, "Exception ''{0}'' - while getting key (regular expression) ''{1}'' in file ''{2}''.", new Object[] { e.getMessage(), key, file });
                throw e;
            }
            if (foundString != null) {
                logger.log(Level.FINEST, "Comparing found value ''{0}'' with expected value ''{1}''...", new Object[] { foundString, expectedValue });
                if (foundString.equals(expectedValue)) {
                    logger.finest("Yes, both are equal. Breaking loop...");
                    break;
                } else {
                    logger.log(Level.FINEST, "Found the value ''{0}'' but does not match with expected value {1}'' for key ''{2}'' in file ''{3}''.", new Object[] { foundString, expectedValue, key, file });
                    foundString = null;
                }
            }
            long currentTime = System.currentTimeMillis();
            long lapTime = currentTime - startTime;
            if (lapTime > maximumWaitTime) {
                logger.log(Level.FINEST, "Lap time ''{0}'' greater than maximum wait time ''{1}''.", new Object[] { lapTime, maximumWaitTime });
                break;
            }
            try {
                logger.finest("Waiting for 500 milliseconds...");
                this.wait(1000);
            } catch (InterruptedException ie) {
                logger.log(Level.WARNING, "Ignoring exceptione " + ie.getMessage(), ie);
            }
        }
        logger.log(Level.FINEST, "Returning found value ''{0}'' for key ''{1}''...", new Object[] { foundString, key });
        return foundString;
    }

    /**
     * Removes a System Property
     * @param key like 'user.home'
     */
    public void removeSystemPropertyStartingWith(String key) {
        this.logger.log(Level.FINER, "Setting back the System Properties for ''{0}...", key);
        Iterator it = System.getProperties().keySet().iterator();
        List keys = new ArrayList();
        while (it.hasNext()) {
            String k = (String) it.next();
            if (k.startsWith(key)) {
                keys.add(k);
                continue;
            }
            if (k.startsWith(key)) {
                keys.add(k);
            }
        }
        it = keys.iterator();
        while (it.hasNext()) {
            String k = (String) it.next();
            System.setProperty(k, "");
            this.logger.log(Level.FINER, "Remove System Properties for ''{0}''.", k);
        }
    }
}
