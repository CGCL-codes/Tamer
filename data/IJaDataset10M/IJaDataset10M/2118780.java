package javarequirementstracer;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Like {@link Properties} but enforces the properties loaded from a file must be unique,
 * though enforcing a property must not be set twice.
 * 
 * @author Ronald Koster
 */
final class UniqueProperties extends Properties {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getInstance(UniqueProperties.class);

    /**
	 * @throws IllegalArgumentException in case a property is set twice.
	 */
    @Override
    public Object put(Object key, Object value) {
        if (containsKey(key)) {
            throw new IllegalArgumentException("Unique violation: " + key);
        }
        return super.put(key, value);
    }

    void load(final File propertiesFile) {
        LOGGER.info("$user.dir = " + System.getProperty("user.dir"));
        LOGGER.info("propertiesFilename = " + propertiesFile.getAbsolutePath());
        FileInputStream input = null;
        try {
            input = new FileInputStream(propertiesFile);
            load(input);
        } catch (FileNotFoundException fnfex) {
            LOGGER.error(fnfex);
            throw new UncheckedException(fnfex);
        } catch (IOException ioex) {
            LOGGER.error(ioex);
            throw new UncheckedException(ioex);
        } finally {
            if (input != null) {
                close(input);
            }
        }
    }

    private void close(Closeable stream) {
        try {
            stream.close();
        } catch (IOException ioex) {
            LOGGER.error(ioex);
            throw new UncheckedException(ioex);
        }
    }

    SortedMap<String, String> getAsSortedMap() {
        final SortedMap<String, String> map = new TreeMap<String, String>();
        for (Map.Entry<Object, Object> entry : entrySet()) {
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            map.put(key, value);
        }
        return map;
    }
}
