package net.sf.jade4spring.internal;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.util.ExtendedProperties;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map.Entry;
import org.springframework.core.io.ClassPathResource;

/**
 * 
 * @author xiaobo
 * @author tarjei
 */
public class Jade4SpringUtils {

    public static final String CLASSPATH_IDENTIFIER = "classpath:";

    /**
     * 
     * Read properties file from the path supplied. The method is able to load
     * both from file system and the classpath. To indicate that it should read
     * the file from classpath, use classpath:/ as prefix.
     * 
     * @param propertiesFile
     * @return a populated instance of {@link jade.util.ExtendedProperties}
     * @throws IOException
     */
    private static ExtendedProperties readPropertiesFromClassPathOrFile(final String propertiesFile) throws IOException {
        InputStream in = null;
        try {
            in = readPropertiesFile(propertiesFile);
            final ExtendedProperties properties = new ExtendedProperties();
            properties.load(in);
            return properties;
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }

    private static InputStream readPropertiesFile(final String propertiesFile) throws IOException {
        InputStream in;
        if (propertiesFile.startsWith(CLASSPATH_IDENTIFIER)) {
            ClassPathResource classPathResource = new ClassPathResource(getPropertiesFileName(propertiesFile));
            in = classPathResource.getInputStream();
        } else {
            in = new FileInputStream(propertiesFile);
        }
        return in;
    }

    private static String getPropertiesFileName(final String propertiesFile) {
        return propertiesFile.substring(CLASSPATH_IDENTIFIER.length());
    }

    /**
     * Create a boot profile for JADE.
     * 
     * @return BootProfileImpl.
     * @throws Exception
     *             Thrown if the properties can not be loaded.
     */
    public static ProfileImpl createBootProfileFromPropertiesFileOrClassPathResource(final String propertiesFile) throws IOException {
        ExtendedProperties properties = readPropertiesFromClassPathOrFile(propertiesFile);
        ProfileImpl profile = addAllPropertiesToProfile(properties);
        return profile;
    }

    private static ProfileImpl addAllPropertiesToProfile(ExtendedProperties properties) {
        ProfileImpl profile = new ProfileImpl();
        for (Entry<Object, Object> entry : properties.entrySet()) {
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            profile.setParameter(key, value);
        }
        profile.setParameter(Profile.MTPS, null);
        return profile;
    }
}
