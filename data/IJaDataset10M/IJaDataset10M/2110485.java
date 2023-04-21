package net.sf.chineseutils.web.metadata;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.lang.StringUtils;

/**
 * @author <a href="mailto:luhuiguo@gmail.com">Lu,Huiguo</a>
 * @version $Id: Metadata.java 19 2006-08-13 15:32:50Z fantasy4u $
 */
public class Metadata implements CreativeCommons, DublinCore, HttpHeaders, Office {

    /** Used to format DC dates for the DATE metadata field */
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private static final Map<String, String> NAMES_IDX = new HashMap<String, String>();

    private static String[] normalized = null;

    static {
        Field[] fields = Metadata.class.getFields();
        for (int i = 0; i < fields.length; i++) {
            int mods = fields[i].getModifiers();
            if (Modifier.isFinal(mods) && Modifier.isPublic(mods) && Modifier.isStatic(mods) && fields[i].getType().equals(String.class)) {
                try {
                    String val = (String) fields[i].get(null);
                    NAMES_IDX.put(normalize(val), val);
                } catch (Exception e) {
                }
            }
        }
        normalized = (String[]) NAMES_IDX.keySet().toArray(new String[NAMES_IDX.size()]);
    }

    /** A map of all metadata attributes */
    private Map<String, Object> metadata = null;

    /** Constructs a new, empty metadata. */
    public Metadata() {
        metadata = new HashMap<String, Object>();
    }

    /**
	 */
    public boolean isMultiValued(String name) {
        return getValues(name).length > 1;
    }

    /**
	 * Returns an array of the names contained in the metadata.
	 */
    public String[] names() {
        Iterator<String> iter = metadata.keySet().iterator();
        List<String> names = new ArrayList<String>();
        while (iter.hasNext()) {
            names.add(getNormalizedName((String) iter.next()));
        }
        return (String[]) names.toArray(new String[names.size()]);
    }

    /**
	 * Get the value associated to a metadata name. If many values are
	 * assiociated to the specified name, then the first one is returned.
	 * 
	 * @param name
	 *            of the metadata.
	 * @return the value associated to the specified metadata name.
	 */
    public String get(String name) {
        Object values = metadata.get(getNormalizedName(name));
        if ((values != null) && (values instanceof List)) {
            return (String) ((List) values).get(0);
        } else {
            return (String) values;
        }
    }

    /**
	 * Get the values associated to a metadata name.
	 * 
	 * @param name
	 *            of the metadata.
	 * @return the values associated to a metadata name.
	 */
    public String[] getValues(String name) {
        Object values = metadata.get(getNormalizedName(name));
        if (values != null) {
            if (values instanceof List<?>) {
                List<?> list = (List<?>) values;
                return (String[]) list.toArray(new String[list.size()]);
            } else {
                return new String[] { (String) values };
            }
        }
        return new String[0];
    }

    /**
	 * Add a metadata name/value mapping. Add the specified value to the list of
	 * values associated to the specified metadata name.
	 * 
	 * @param name
	 *            the metadata name.
	 * @param value
	 *            the metadata value.
	 */
    @SuppressWarnings("unchecked")
    public void add(String name, String value) {
        String normalized = getNormalizedName(name);
        Object values = metadata.get(normalized);
        if (values != null) {
            if (values instanceof String) {
                List<Object> list = new ArrayList<Object>();
                list.add(values);
                list.add(value);
                metadata.put(normalized, list);
            } else if (values instanceof List<?>) {
                ((List<String>) values).add(value);
            }
        } else {
            metadata.put(normalized, value);
        }
    }

    public void setAll(Properties properties) {
        Enumeration names = properties.propertyNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            set(name, properties.getProperty(name));
        }
    }

    /**
	 * Set metadata name/value. Associate the specified value to the specified
	 * metadata name. If some previous values were associated to this name, they
	 * are removed.
	 * 
	 * @param name
	 *            the metadata name.
	 * @param value
	 *            the metadata value.
	 */
    public void set(String name, String value) {
        remove(name);
        add(name, value);
    }

    /**
	 * Remove a metadata and all its associated values.
	 */
    public void remove(String name) {
        metadata.remove(getNormalizedName(name));
    }

    /**
	 * Returns the number of metadata names in this metadata.
	 */
    public int size() {
        return metadata.size();
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        Metadata other = null;
        try {
            other = (Metadata) o;
        } catch (ClassCastException cce) {
            return false;
        }
        if (other.size() != size()) {
            return false;
        }
        String[] names = names();
        for (int i = 0; i < names.length; i++) {
            String[] otherValues = other.getValues(names[i]);
            String[] thisValues = getValues(names[i]);
            if (otherValues.length != thisValues.length) {
                return false;
            }
            for (int j = 0; j < otherValues.length; j++) {
                if (!otherValues[j].equals(thisValues[j])) {
                    return false;
                }
            }
        }
        return true;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        String[] names = names();
        for (int i = 0; i < names.length; i++) {
            String[] values = getValues(names[i]);
            for (int j = 0; j < values.length; j++) {
                buf.append(names[i]).append("=").append(values[j]).append(" ");
            }
        }
        return buf.toString();
    }

    /**
	 * Get the normalized name of metadata attribute name. This method tries to
	 * find a well-known metadata name (one of the metadata names defined in
	 * this class) that matches the specified name. The matching is error
	 * tolerent. For instance,
	 * <ul>
	 * <li>content-type gives Content-Type</li>
	 * <li>CoNtEntType gives Content-Type</li>
	 * <li>ConTnTtYpe gives Content-Type</li>
	 * </ul>
	 * If no matching with a well-known metadata name is found, then the
	 * original name is returned.
	 */
    public static String getNormalizedName(String name) {
        String searched = normalize(name);
        String value = (String) NAMES_IDX.get(searched);
        if ((value == null) && (normalized != null)) {
            int threshold = searched.length() / 3;
            for (int i = 0; i < normalized.length && value == null; i++) {
                if (StringUtils.getLevenshteinDistance(searched, normalized[i]) < threshold) {
                    value = (String) NAMES_IDX.get(normalized[i]);
                }
            }
        }
        return (value != null) ? value : name;
    }

    private static final String normalize(String str) {
        char c;
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            c = str.charAt(i);
            if (Character.isLetter(c)) {
                buf.append(Character.toLowerCase(c));
            }
        }
        return buf.toString();
    }
}
