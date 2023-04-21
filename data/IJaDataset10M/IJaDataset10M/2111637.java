package org.simplextensions.properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Utility class used to read all properties files with defined name and merge
 * properties values according to <b>priority</b> property (By default
 * <b>priority</b>=0).
 * 
 * 
 * 
 * @author Tomasz Krzyzak, <a
 *         href="mailto:tomasz.krzyzak@gmail.com">tomasz.krzyzak@gmail.com</a>
 * @since 2010-05-10 15:00:59
 */
public class PropertiesReader {

    private final String defaultPropertiesFileName;

    private final String propertiesFileName;

    private static final Logger log = LoggerFactory.getLogger(PropertiesReader.class);

    public PropertiesReader(String propertiesFileName) {
        this.propertiesFileName = propertiesFileName;
        this.defaultPropertiesFileName = propertiesFileName.replace(".properties", ".default.properties");
        try {
            scanProperties(Thread.currentThread().getContextClassLoader().getResources(propertiesFileName));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void scanProperties(Enumeration<URL> resources) throws Exception {
        while (resources.hasMoreElements()) {
            URL nextElement = resources.nextElement();
            if ("file".equals(nextElement.getProtocol())) {
                Properties defaults = new ReferencingProperties();
                Properties properties = new ReferencingProperties(defaults);
                File file = new File(nextElement.getFile());
                File defaultFile = new File(nextElement.getFile().replace(propertiesFileName, defaultPropertiesFileName));
                readProperties(defaults, defaultFile);
                readProperties(properties, file);
                processProperties(properties);
            } else if ("jar".equals(nextElement.getProtocol())) {
                String file = nextElement.getFile();
                String[] split = file.split("!");
                split[1] = split[1].substring(1);
                Map<String, Properties[]> propertiesMap = new HashMap<String, Properties[]>();
                JarFile jarFile = new JarFile(new File(new URI(split[0])));
                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry je = entries.nextElement();
                    if (!je.isDirectory()) {
                        if (je.getName().endsWith(defaultPropertiesFileName)) {
                            InputStream inputStream = jarFile.getInputStream(je);
                            Properties defaults = new ReferencingProperties();
                            defaults.load(inputStream);
                            String replace = je.getName().replace(defaultPropertiesFileName, "");
                            Properties[] properties2 = propertiesMap.get(replace);
                            if (properties2 == null) {
                                properties2 = new Properties[2];
                                propertiesMap.put(replace, properties2);
                            }
                            properties2[1] = defaults;
                        } else if (je.getName().endsWith(propertiesFileName)) {
                            InputStream inputStream = jarFile.getInputStream(je);
                            Properties properties = new ReferencingProperties();
                            properties.load(inputStream);
                            String replace = je.getName().replace(propertiesFileName, "");
                            Properties[] properties2 = propertiesMap.get(replace);
                            if (properties2 == null) {
                                properties2 = new Properties[2];
                                propertiesMap.put(replace, properties2);
                            }
                            properties2[0] = properties;
                        }
                    }
                }
                for (Properties[] ps : propertiesMap.values()) {
                    if (ps[0] != null) {
                        Properties properties = new ReferencingProperties(ps[1] != null ? ps[1] : new Properties());
                        for (Object o : ps[0].keySet()) {
                            properties.put(o, ps[0].get(o));
                        }
                        processProperties(properties);
                    }
                }
            }
        }
    }

    private Properties properties = new Properties();

    public String getProperty(String key) {
        return this.properties.getProperty(key);
    }

    private void processProperties(Properties properties) {
        Integer newPriority = new Integer(properties.getProperty("priority", "0"));
        Integer oldPriority = new Integer(this.properties.getProperty("priority", "-1"));
        Enumeration<?> propertyNames = properties.propertyNames();
        while (propertyNames.hasMoreElements()) {
            Object propertyName = propertyNames.nextElement();
            if (!"priority".equals(propertyName)) {
                String newValue = properties.getProperty((String) propertyName);
                String oldValue = this.properties.getProperty((String) propertyName);
                newValue = mergePropertyValues((String) propertyName, newValue, oldValue, newPriority, oldPriority);
                this.properties.setProperty((String) propertyName, newValue);
            }
        }
        this.properties.setProperty("priority", newPriority.toString());
    }

    /**
	 * merging of two properties according to priority. merging result in
	 * choosing value with higher priority.
	 * 
	 * @param propertyName
	 * @param newValue
	 * @param oldValue
	 * @param newPriority
	 * @param oldPriority
	 * @return
	 */
    public String mergePropertyValues(String propertyName, String newValue, String oldValue, Integer newPriority, Integer oldPriority) {
        return newPriority.compareTo(oldPriority) > 0 ? newValue : oldValue;
    }

    private void readProperties(Properties properties, File file) {
        if (!file.exists()) {
            return;
        }
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            properties.load(fileInputStream);
        } catch (Exception e) {
            log.error("", e);
        } finally {
            try {
                fileInputStream.close();
            } catch (Exception e) {
                log.error("", e);
            }
        }
    }
}
