package ch.bbv.utilities;

import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * The property utilities class provides convenience methods to increase the
 * usefulness of properties.
 * @author Marcel Baumann
 * @version $Revision: 1.12 $
 */
public final class PropertyUtilities {

    /**
   * private constructor for a static class.
   */
    private PropertyUtilities() {
    }

    /**
   * Converts a properties configuration into a regular Java properties object.
   * All variables are expanded.
   * @param propertiesConfiguration properties configuration to convert
   * @return the converted properties object
   */
    public static Properties convert(PropertiesConfiguration propertiesConfiguration) {
        Properties properties = new Properties();
        for (Iterator i = propertiesConfiguration.getKeys(); i.hasNext(); ) {
            String key = (String) i.next();
            String value = propertiesConfiguration.getString(key).toString();
            properties.setProperty(key, value);
        }
        return properties;
    }

    /**
   * Returns the value of the attribute in the first element with the given
   * identifier value.
   * @param configuration configuration containing the attribute to retrieve
   * @param path path containing the elements to look for
   * @param attributeId attribute identifying the identifier value
   * @param id identifier value looked for
   * @param attribute the attribute to retrieve in the element with the given
   *          identifier value
   * @return the requested attribute value if found otherwise null
   * @pre (configuration != null) && (path != null) && (attributeId != null) &&
   *      (id != null) && (attribute != null)
   */
    public static String getAttribute(CompositeConfiguration configuration, String path, String attributeId, String id, String attribute) {
        assert (configuration != null) && (path != null) && (attributeId != null) && (id != null) && (attribute != null);
        String value = null;
        StringBuilder buffer = new StringBuilder();
        buffer.append(path);
        for (int i = 0; ; i++) {
            String name = getAttributeAt(configuration, buffer, i, attributeId);
            if (name == null) {
                break;
            }
            if (id.equals(name)) {
                value = getAttributeAt(configuration, buffer, i, attribute);
                break;
            }
        }
        return value;
    }

    /**
   * Returns the requested attribute at the given index.
   * @param configuration configuration containing the attribue
   * @param path path to the list of elements
   * @param index index of the elements
   * @param attribute attribute to retrieve
   * @return the value of the attribute if found otherwise null
   * @pre (configuration != null) && (path != null) && (index >= 0) &&
   *      (attribute != null)
   */
    public static String getAttributeAt(CompositeConfiguration configuration, StringBuilder path, int index, String attribute) {
        assert (configuration != null) && (path != null) && (index >= 0) && (attribute != null);
        int length = path.length();
        path.append("(").append(index).append(")[@").append(attribute).append("]");
        String value = configuration.getString(path.toString());
        path.setLength(length);
        return value;
    }

    /**
   * Returns the mapping for the type to the target language.
   * @param configuration configuration containing the attribute
   * @param typename to map to a language construct (qualified name)
   * @param language language to map to. A cartridge can define multiple
   *          mappings of datatypes and model types to target programming
   *          language
   * @param dialect of language (e.g. "mssqlserver" for "sql" language)
   * @param info to provide about the type (e.g. "type", "default", "null")
   * @param concrete flag indicating if the implementation class should be
   *          retrieve or not
   * @return the requested info about the type
   * @pre (typename != null) && (language != null) && (info != null)
   */
    public static String getInfoOfType(CompositeConfiguration configuration, String typename, String language, String dialect, String info, boolean concrete) {
        StringBuilder buffer = new StringBuilder();
        Object collection = configuration.getProperty("datatypes[@language]");
        if (collection instanceof Collection) {
            int size = ((Collection) collection).size();
            StringBuilder path = new StringBuilder();
            path.append("datatypes");
            int matchingDatatypesIndex = -1;
            for (int i = 0; i < size; i++) {
                String attribute = getAttributeAt(configuration, path, i, "language");
                if (language.equalsIgnoreCase(attribute)) {
                    String dialectAttribute = getAttributeAt(configuration, path, i, "dialect");
                    if (dialectAttribute == null) {
                        matchingDatatypesIndex = i;
                    } else if (dialect.equalsIgnoreCase(dialectAttribute)) {
                        matchingDatatypesIndex = i;
                        break;
                    }
                }
            }
            if (matchingDatatypesIndex > -1) {
                buffer.append("datatypes(").append(matchingDatatypesIndex).append(")");
            }
        } else {
            String attribute = (String) configuration.getProperty("datatypes[@language]");
            if (language.equalsIgnoreCase(attribute)) {
                buffer.append("datatypes");
            }
        }
        String result = null;
        if (buffer.length() > 0) {
            int length = buffer.length();
            buffer.append(".datatype");
            for (int i = 0; ; i++) {
                String name = getAttributeAt(configuration, buffer, i, "name");
                if (name == null) {
                    break;
                }
                if (typename.equals(name)) {
                    result = getAttributeAt(configuration, buffer, i, info);
                    break;
                }
            }
            if (result == null) {
                buffer.setLength(length);
                buffer.append(".class");
                for (int i = 0; ; i++) {
                    String name = getAttributeAt(configuration, buffer, i, "name");
                    if (name == null) {
                        break;
                    }
                    if (typename.equals(name)) {
                        result = getAttributeAt(configuration, buffer, i, info);
                        break;
                    }
                }
            }
        }
        return result;
    }
}
