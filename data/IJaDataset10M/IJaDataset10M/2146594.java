package com.subshell.persistence.mapper;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.subshell.persistence.exception.InitializationException;
import com.subshell.persistence.exception.PersistenceMapperNotFoundException;

/** A persistence mapper configuration constructed from properties. */
public class PropertyPersistenceMapperConfiguration implements PersistenceMapperConfiguration {

    private static final Log log = LogFactory.getLog(PropertyPersistenceMapperConfiguration.class);

    /** Holds pairs of persistent objects class and persistence mapper class. */
    private Map<Class<?>, Class<?>> mapperClasses = new HashMap<Class<?>, Class<?>>();

    /**
	 * Constructs a new persistence mapper configuration from the specified properties.
	 * 
	 * @throws InitializationException when constructing the configuration fails
	 */
    public PropertyPersistenceMapperConfiguration(Properties props) {
        try {
            loadPersistenceMappers(props);
        } catch (ClassNotFoundException e) {
            throw new InitializationException(e);
        }
    }

    /**
	 * Constructs a new persistence mapper configuration from the specified file.
	 * 
	 * @throws InitializationException when constructing the configuration fails
	 * @throws IOException when loading the configuration fails
	 */
    public PropertyPersistenceMapperConfiguration(File file) throws IOException {
        try {
            loadPersistenceMappers(file);
        } catch (ClassNotFoundException e) {
            throw new InitializationException(e);
        }
    }

    /**
	 * Loads persistence mappers from the specified file.
	 * 
	 * @throws InitializationException when loading the persistence mappers fails
	 * @throws IOException when loading the file fails
	 * @throws ClassNotFoundException when a class could not be found
	 */
    private void loadPersistenceMappers(File file) throws IOException, ClassNotFoundException {
        InputStream in = null;
        Properties props = new Properties();
        try {
            in = new BufferedInputStream(new FileInputStream(file));
            props.load(in);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    log.warn("", e);
                }
            }
        }
        loadPersistenceMappers(props);
    }

    /**
	 * Loads persistence mappers from the specified properties.
	 *
	 * @throws InitializationException when loading the persistence mappers fails
	 * @throws ClassNotFoundException when a class could not be found
	 */
    private void loadPersistenceMappers(Properties props) throws ClassNotFoundException {
        for (Enumeration e = props.propertyNames(); e.hasMoreElements(); ) {
            String objectClassName = (String) e.nextElement();
            String mapperClassName = props.getProperty(objectClassName);
            addPersistenceMapper(objectClassName, mapperClassName);
        }
    }

    /**
	 * Adds a persistence mapper to the set of known persistence mappers.
	 *
	 * @param objectClassName class name for which to add the persistence mapper
	 * @param mapperClassName class name of the persistence mapper
	 * 
	 * @throws IllegalArgumentException when the specified class names are invalid
	 */
    private void addPersistenceMapper(String objectClassName, String mapperClassName) throws ClassNotFoundException {
        Class<?> objectClass = Class.forName(objectClassName);
        Class<?> mapperClass = Class.forName(mapperClassName);
        Class<?> currentMapperClass = mapperClasses.get(objectClass);
        if (currentMapperClass != null) {
            throw new IllegalArgumentException("can't register persistence mapper " + mapperClassName + " for objects class " + objectClassName + ": " + "persistence mapper " + currentMapperClass.getClass().getName() + " already registered for this objects class");
        }
        if (!PersistenceMapper.class.isAssignableFrom(mapperClass)) {
            throw new IllegalArgumentException("persistence mapper class " + mapperClassName + " for objects class " + objectClassName + " " + "is not a PersistenceMapper");
        }
        if (PersistenceMapper.class.equals(mapperClass)) {
            throw new IllegalArgumentException("persistence mapper class " + mapperClassName + " for objects class " + objectClassName + " " + "must not be " + PersistenceMapper.class.getName() + " itself");
        }
        if (mapperClass.isInterface()) {
            throw new IllegalArgumentException("persistence mapper class " + mapperClassName + " for objects class " + objectClassName + " " + "must not be an interface");
        }
        if (Modifier.isAbstract(mapperClass.getModifiers())) {
            throw new IllegalArgumentException("persistence mapper class " + mapperClassName + " for objects class " + objectClassName + " " + "must not be abstract");
        }
        if (!Modifier.isPublic(mapperClass.getModifiers())) {
            throw new IllegalArgumentException("persistence mapper class " + mapperClassName + " for objects class " + objectClassName + " " + "must be public");
        }
        mapperClasses.put(objectClass, mapperClass);
    }

    /**
	 * Returns the PersistenceMapper class configured for the specified class.
	 *
	 * @throws PersistenceMapperNotFoundException if no mapper is found
	 */
    public <T> Class<PersistenceMapper<T>> getMapperForClass(Class<T> clazz) {
        @SuppressWarnings("unchecked") Class<PersistenceMapper<T>> mapper = (Class<PersistenceMapper<T>>) mapperClasses.get(clazz);
        if (mapper == null) {
            throw new PersistenceMapperNotFoundException("no persistence mapper found for " + "objects class " + clazz.getName());
        }
        return mapper;
    }
}
