package com.ldodds.ot.property;

/**
 * Abstract implementation of the {@link PropertyMapper} interface. 
 * <p>
 * Provides support for mapping to a simple label using either the 
 * full property URI, or based on the namespace URI.
 * </p>
 * @author Leigh Dodds
 */
public abstract class AbstractPropertyMapper implements PropertyMapper {

    public static final String DEFAULT_SEPARATOR = "_";

    public String map(String propertyURI) {
        String key = mapURI(propertyURI);
        if (key == null) {
            String[] parts = splitURI(propertyURI);
            String prefix = mapBaseURI(parts[0]);
            if (prefix == null) {
                return null;
            }
            key = prefix + getSeparator() + parts[1];
        }
        return key;
    }

    /**
	 * Map whole URI
	 * @param the property URI
	 * @return the mapped URI or null
	 */
    protected abstract String mapURI(String propertyURI);

    /**
	 * Map based on base URI
	 * @param baseURI the base URI
	 * @return the mapped URI or null
	 */
    protected abstract String mapBaseURI(String baseURI);

    /**
	 * Splits a URI into a base URI and a suffix
	 * @param propertyURI
	 */
    private String[] splitURI(String propertyURI) {
        if (propertyURI.indexOf("#") > -1) {
            String[] parts = new String[2];
            int index = propertyURI.lastIndexOf("#");
            parts[0] = propertyURI.substring(0, index + 1);
            parts[1] = propertyURI.substring(index + 1);
            return parts;
        }
        int index = propertyURI.lastIndexOf("/");
        String[] parts = new String[2];
        parts[0] = propertyURI.substring(0, index + 1);
        parts[1] = propertyURI.substring(index + 1);
        return parts;
    }

    protected String getSeparator() {
        return DEFAULT_SEPARATOR;
    }
}
