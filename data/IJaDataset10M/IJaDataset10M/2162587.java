package org.nightlabs.jfire.testsuite;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import org.nightlabs.ModuleException;
import org.nightlabs.jfire.servermanager.JFireServerManager;
import org.nightlabs.jfire.servermanager.JFireServerManagerUtil;

/**
 * EAR descriptor for JFireTestSuite.
 * 
 * @author Alexander Bieber <!-- alex [AT] nightlabs [DOT] de -->
 * @author marco schulze - marco at nightlabs dot de
 */
public class JFireTestSuiteEAR {

    private static final Logger logger = Logger.getLogger(JFireTestSuiteEAR.class);

    public static final String MODULE_NAME = "JFireTestSuite";

    protected JFireTestSuiteEAR() {
    }

    public static File getEARDir() throws ModuleException {
        JFireServerManager jFireServerManager;
        try {
            jFireServerManager = JFireServerManagerUtil.getJFireServerManager();
        } catch (Exception e) {
            throw new ModuleException("Could not get JFireServerManager!", e);
        }
        try {
            File earDir = new File(new File(jFireServerManager.getJFireServerConfigModule().getJ2ee().getJ2eeDeployBaseDirectory()), MODULE_NAME + ".ear");
            return earDir;
        } finally {
            jFireServerManager.close();
        }
    }

    private static Properties jfireTestSuiteProperties;

    private static Properties readJFireTestSuitePropertiesFile(File file) throws IOException {
        if (logger.isDebugEnabled()) logger.debug("readJFireTestSuitePropertiesFile: file=" + file.getAbsolutePath());
        Properties properties = new Properties();
        FileInputStream in = new FileInputStream(file);
        try {
            properties.load(in);
        } finally {
            in.close();
        }
        if (logger.isTraceEnabled()) {
            for (Map.Entry<?, ?> me : properties.entrySet()) logger.trace("readJFireTestSuitePropertiesFile: " + me.getKey() + '=' + me.getValue());
        }
        return properties;
    }

    private static List<File> getIncludeFilesFromProperties(Properties properties) {
        List<File> includeFiles = new LinkedList<File>();
        Properties includeProps = getProperties(properties, "include.");
        SortedSet<String> includePropKeys = new TreeSet<String>();
        for (Object key : includeProps.keySet()) includePropKeys.add((String) key);
        for (String includePropKey : includePropKeys) {
            String includeValue = includeProps.getProperty(includePropKey);
            String includeFileName = includeValue;
            for (Map.Entry<?, ?> me : System.getProperties().entrySet()) {
                String systemPropertyKey = (String) me.getKey();
                String systemPropertyValue = (String) me.getValue();
                includeFileName = includeFileName.replaceAll(Pattern.quote("${" + systemPropertyKey + "}"), systemPropertyValue);
            }
            if (logger.isDebugEnabled()) logger.debug("getIncludeFilesFromProperties: includeKey=include." + includePropKey + " includeValue=" + includeValue + " includeFileName=" + includeFileName);
            includeFiles.add(new File(includeFileName));
        }
        return includeFiles;
    }

    private static void readJFireTestSuitePropertiesRecursively(Properties jfireTestSuiteProperties, File propertiesFile, Set<File> includeFilesProcessed) throws IOException {
        Properties props = readJFireTestSuitePropertiesFile(propertiesFile);
        jfireTestSuiteProperties.putAll(props);
        for (File includeFile : getIncludeFilesFromProperties(props)) {
            if (!includeFile.exists()) {
                logger.info("readJFireTestSuitePropertiesRecursively: includeFile \"" + includeFile.getAbsolutePath() + "\" defined in propertiesFile \"" + propertiesFile.getAbsolutePath() + "\" does not exist!");
                continue;
            }
            if (!includeFilesProcessed.add(includeFile)) {
                logger.warn("readJFireTestSuitePropertiesRecursively: includeFile \"" + includeFile.getAbsolutePath() + "\" defined in propertiesFile \"" + propertiesFile.getAbsolutePath() + "\" has already been processed! Skipping in order to prevent circular include loops!");
                continue;
            }
            readJFireTestSuitePropertiesRecursively(jfireTestSuiteProperties, includeFile, includeFilesProcessed);
        }
    }

    /**
	 * Get the main properties of {@link JFireTestSuite}.
	 * They are located in the file jfireTestSuite.properties in the ear directory.
	 */
    public static Properties getJFireTestSuiteProperties() throws ModuleException, IOException {
        if (jfireTestSuiteProperties == null) {
            synchronized (JFireTestSuiteEAR.class) {
                if (jfireTestSuiteProperties == null) {
                    Properties newJFireTestSuiteProps = new Properties();
                    Set<File> includeFilesProcessed = new HashSet<File>();
                    readJFireTestSuitePropertiesRecursively(newJFireTestSuiteProps, new File(JFireTestSuiteEAR.getEARDir(), "jfireTestSuite.properties"), includeFilesProcessed);
                    jfireTestSuiteProperties = newJFireTestSuiteProps;
                }
            }
            if (logger.isTraceEnabled()) {
                for (Map.Entry<?, ?> me : jfireTestSuiteProperties.entrySet()) logger.trace("getJFireTestSuiteProperties: " + me.getKey() + '=' + me.getValue());
            }
        }
        return jfireTestSuiteProperties;
    }

    public static Collection<Matcher> getPropertyKeyMatches(Properties properties, Pattern pattern) {
        Collection<Matcher> matches = new ArrayList<Matcher>();
        for (Iterator<?> iter = properties.keySet().iterator(); iter.hasNext(); ) {
            String key = (String) iter.next();
            Matcher m = pattern.matcher(key);
            if (m.matches()) matches.add(m);
        }
        return matches;
    }

    public static Properties getProperties(Properties properties, String keyPrefix) {
        Properties newProperties = new Properties();
        Collection<Matcher> matches = getPropertyKeyMatches(properties, Pattern.compile("^" + Pattern.quote(keyPrefix) + "(.*)$"));
        for (Matcher m : matches) newProperties.put(m.group(1), properties.get(m.group(0)));
        return newProperties;
    }
}
