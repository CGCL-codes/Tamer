package net.sf.jukebox.conf;

import java.net.URL;
import java.util.Map;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.Set;

/**
 * This object holds the configuration in a way similar to {@code TreeMap}, but
 * provides a convenient way to load and extract it.
 *
 * @author Copyright &copy; <a href="mailto:vt@freehold.crocodile.org">Vadim Tkachenko</a> 1998-2008
 * @author Significant influence from <a href="http://java.apache.org/"
 * target=_top>Java-Apache Project</a> in general and code written by <a
 * href="mailto:stefano@apache.org">Stefano Mazzocchi</a> in particular.
 */
public class TextConfiguration implements Configuration {

    /**
     * Configuration key to value mapping.
     */
    private final Map<String, Object> theConfiguration = new TreeMap<String, Object>();

    /**
     * Default configuration, if any. If the value for the requested
     * configuration keyword is not found, the result is sought from the default
     * configuration. Note that multiple levels of default configurations are
     * available.
     *
     * @serial Default configuration, if any.
     */
    protected Configuration defaultConf = null;

    /**
     * The URL this configuration has been originated from.
     */
    private URL baseURL;

    /**
     * Create an empty configuration with no values and no defaults.
     */
    public TextConfiguration() {
    }

    /**
     * Create an empty configuration with a given set of defaults.
     *
     * @param defaultConf Default configuration.
     */
    public TextConfiguration(Configuration defaultConf) {
        this.defaultConf = defaultConf;
    }

    /**
     * Create an empty configuration with a given set of defaults, based on a
     * given URL.
     *
     * @param defaultConf Default configuration.
     * @param baseURL URL to base on. This value is declarative, it is never
     * used by the configuration itself, however, it is used by the {@link
     * ConfigurationWatcher ConfigurationWatcher} and {@link
     * ConfigurationChangeListener ConfigurationChangeListener}.
     */
    public TextConfiguration(Configuration defaultConf, URL baseURL) {
        this.defaultConf = defaultConf;
        this.baseURL = baseURL;
    }

    /**
     * Set the default configuration.
     *
     * @param conf The default configuration. Becomes new top-priority default,
     * shifting the rest of the default configuration chain to the back of this
     * object's default chain.
     * @exception IllegalArgumentException if the value of the new default
     * configuration is {@code null}. To disable the defaults, use
     * {@link #clearDefaults clearDefaults()}.
     */
    public void setDefaultConfiguration(Configuration conf) {
        Configuration oldDefault = this.defaultConf;
        this.defaultConf = conf;
        if (oldDefault == null) {
            return;
        }
        Configuration tail = this.defaultConf;
        Configuration cursor = tail.getDefaultConfiguration();
        while (cursor != null) {
            tail = cursor;
            cursor = tail.getDefaultConfiguration();
        }
        tail.setDefaultConfiguration(oldDefault);
    }

    /**
     * Clear all the default configurations.
     */
    public void clearDefaults() {
        defaultConf = null;
    }

    /**
     * Get the configuration value as a {@code String}.
     *
     * @param key Keyword to look up.
     * @return Value as a {@code String}, regardless of intended type,
     * {@code null} if not found.
     * @exception NoSuchElementException if the value is not present in the
     * configuration.
     */
    public String getString(String key) {
        return parseString(key, get(key));
    }

    /**
     * Get the configuration value as a {@code Boolean}.
     *
     * @param key Keyword to look up.
     * @return Value as a {@code boolean}, {@code false} if not found.
     * @exception NoSuchElementException if the value is not present in the
     * configuration.
     */
    public boolean getBoolean(String key) {
        return parseBoolean(getString(key));
    }

    /**
     * Get the configuration value as an {@code int}.
     *
     * @param key Keyword to look up.
     * @return Value as an {@code int}, {@code 0} if not found.
     * @exception NoSuchElementException if the value is not present in the
     * configuration.
     */
    public int getInteger(String key) {
        return Integer.parseInt(getString(key));
    }

    /**
     * Get the configuration value as a {@code long}.
     *
     * @param key Keyword to look up.
     * @return Value as a {@code long}, {@code 0} if not found.
     * @exception NoSuchElementException if the value is not present in the
     * configuration.
     */
    public long getLong(String key) {
        return Long.parseLong(getString(key));
    }

    /**
     * Get the configuration value as a {@code double}.
     *
     * @param key Keyword to look up.
     * @return Value as a {@code double}, {@code 0} if not found.
     * @exception NoSuchElementException if the value is not present in the
     * configuration.
     */
    public double getDouble(String key) {
        return Double.parseDouble(getString(key));
    }

    /**
     * Get the configuration value as a {@code Collection}.
     *
     * @param key Keyword to look up.
     * @return The list of the values found. In case there were no values,
     * return an empty list, <strong>not {@code null}</strong>!
     */
    public List<String> getList(String key) {
        return getList(key, new LinkedList<String>());
    }

    /**
     * Get the String corresponding to the given key.
     *
     * @param key Key Key to look up.
     * @param defaultValue Default value.
     * @return String corresponding to the given key.
     * @exception IllegalArgumentException when the object contained here is
     * neither String nor List.
     */
    public String getString(String key, String defaultValue) {
        Object found = null;
        try {
            found = get(key);
        } catch (ClassCastException ccex) {
            duplicateKeyword(key);
        } catch (NoSuchElementException ex) {
            return (defaultConf != null) ? defaultConf.getString(key, defaultValue) : defaultValue;
        }
        if (found == null) {
            throw new IllegalArgumentException("Configuration value found is null, can't be parsed as Boolean");
        }
        return parseString(key, found);
    }

    /**
     * Figure out how to parse the object so it can be returned as string.
     *
     * @param key Key The lookup key, to provide proper diagnostics in case of a
     * problem.
     * @param found Object retrieved from the configuration.
     *
     * @return Parsed string.
     */
    private String parseString(String key, Object found) {
        if (found instanceof String) {
            return (String) found;
        }
        if (found instanceof List) {
            StringBuffer result = new StringBuffer();
            List source = (List) found;
            for (Iterator i = source.iterator(); i.hasNext(); ) {
                String next = i.next().toString();
                if (result.length() != 0) {
                    result.append(",");
                }
                result.append(next);
            }
            return result.toString();
        }
        throw new IllegalArgumentException(key + " expects to see String or List, found " + found.getClass().getName());
    }

    /**
     * Get the List corresponding to the given key.
     *
     * @param key Key The key to look for.
     * @param defaultValue DefaultValue The default returned in the case there's
     * no configuration value for the given key.
     * @return The list of the values found. In case there were no values,
     * return an empty list, <strong>not {@code null}</strong>!
     */
    public List getList(String key, List defaultValue) {
        if (defaultValue == null) {
            defaultValue = new LinkedList();
        }
        Object found = theConfiguration.get(key);
        if (found == null && defaultConf != null) {
            return defaultConf.getList(key, defaultValue);
        }
        if (found == null) {
            return defaultValue;
        }
        if (found instanceof List) {
            return (List) found;
        }
        List<Object> dummy = new LinkedList<Object>();
        if (found instanceof String) {
            for (StringTokenizer st = new StringTokenizer((String) found, ",; "); st.hasMoreTokens(); ) {
                String token = st.nextToken();
                dummy.add(token);
            }
        } else {
            dummy.add(found);
        }
        return dummy;
    }

    /**
     * Get the boolean corresponding to the given key.
     *
     * @param key Key Key to look up.
     * @param defaultValue Default value.
     * @return String corresponding to the given key.
     * @exception IllegalArgumentException when the object contained here is
     * neither String nor Boolean.
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        Object found = null;
        try {
            found = get(key);
        } catch (ClassCastException ccex) {
            duplicateKeyword(key);
        } catch (NoSuchElementException ex) {
            return (defaultConf != null) ? defaultConf.getBoolean(key, defaultValue) : defaultValue;
        }
        if (found == null) {
            throw new IllegalArgumentException("Configuration value found is null, can't be parsed as Boolean");
        }
        return parseBoolean(found.toString());
    }

    /**
     * Get the integer corresponding to the given key.
     *
     * @param key Key Key to look up.
     * @param defaultValue Default value.
     * @return Integer corresponding to the given key.
     * @exception IllegalArgumentException when the object contained here is not
     * Integer.
     */
    public int getInteger(String key, int defaultValue) {
        Object found = null;
        try {
            found = get(key);
        } catch (ClassCastException ccex) {
            duplicateKeyword(key);
        } catch (NoSuchElementException ex) {
            return (defaultConf != null) ? defaultConf.getInteger(key, defaultValue) : defaultValue;
        }
        if (found == null) {
            throw new IllegalArgumentException("Configuration value found is null, can't be parsed as Integer");
        }
        return Integer.parseInt(found.toString());
    }

    /**
     * Get the long corresponding to the given key.
     *
     * @param key Key Key to look up.
     * @param defaultValue Default value.
     * @return Long corresponding to the given key.
     * @exception IllegalArgumentException when the object contained here is not
     * Long.
     */
    public long getLong(String key, long defaultValue) {
        Object found = null;
        try {
            found = get(key);
        } catch (ClassCastException ccex) {
            duplicateKeyword(key);
        } catch (NoSuchElementException ex) {
            return (defaultConf != null) ? defaultConf.getLong(key, defaultValue) : defaultValue;
        }
        if (found == null) {
            throw new IllegalArgumentException("Configuration value found is null, can't be parsed as Long");
        }
        return Long.parseLong(found.toString());
    }

    /**
     * Get the double corresponding to the given key.
     *
     * @param key Key Key to look up.
     * @param defaultValue Default value.
     * @return Double corresponding to the given key.
     * @exception IllegalArgumentException when the object contained here is not
     * Double.
     */
    public double getDouble(String key, double defaultValue) {
        Object found = null;
        try {
            found = get(key);
        } catch (ClassCastException ccex) {
            duplicateKeyword(key);
        } catch (NoSuchElementException ex) {
            return (defaultConf != null) ? defaultConf.getDouble(key, defaultValue) : defaultValue;
        }
        if (found == null) {
            throw new IllegalArgumentException("Configuration value found is null, can't be parsed as Double");
        }
        return Double.parseDouble(found.toString());
    }

    /**
     * Add a mapping from the key to the value. If there's already existing
     * value[s], the value is added into the sequence of values.
     * <p>
     * <strong>NOTE:</strong> it is not possible to distinguish between the
     * {@code List} that is a value itself and the {@code List} that is a
     * collection of other values. Caveat emptor.
     *
     * @param key Key The key.
     * @param value The value.
     */
    public void put(String key, Object value) {
        boolean split = false;
        if (value instanceof String && ((String) value).indexOf(",") != -1) {
            split = true;
            String valueAsString = (String) value;
            List<String> valueAsList = new LinkedList<String>();
            for (StringTokenizer st = new StringTokenizer(valueAsString, ","); st.hasMoreTokens(); ) {
                String element = st.nextToken().trim();
                valueAsList.add(element);
            }
            value = valueAsList;
        }
        Object found = theConfiguration.get(key);
        if (found == null) {
            theConfiguration.put(key, value);
            return;
        }
        if (found instanceof String) {
            Object first = found;
            List<Object> set = new LinkedList<Object>();
            theConfiguration.remove(key);
            set.add(first);
            theConfiguration.put(key, set);
            found = set;
        }
        if (found instanceof List) {
            List<Object> vFound = (List<Object>) found;
            if (vFound.indexOf(value) == -1) {
                if (split) {
                    for (Iterator e = ((List) value).iterator(); e.hasNext(); ) {
                        vFound.add(e.next());
                    }
                } else {
                    vFound.add(value);
                }
                return;
            }
        }
        theConfiguration.remove(key);
        List<Object> set = new LinkedList<Object>();
        set.add(found);
        set.add(value);
        theConfiguration.put(key, set);
    }

    /**
     * Parse the value into the boolean.
     * <ul>
     * <li>'on', 'true', '1' and 'yes' are recognized as true.
     * <li>'off', 'false', '0' and 'no' are recognized as false.
     * </ul>
     * Implementation note: {@code Boolean.valueOf} treats only "true" string as
     * true, every other value in this list is treated as 'false'.
     *
     * @param value Value to parse.
     * @return Boolean value of the string.
     * @exception IllegalArgumentException if the value is not boolean.
     */
    public static boolean parseBoolean(String value) {
        String source = value.toLowerCase();
        if (source.equals("on") || source.equals("true") || source.equals("1") || source.equals("yes")) {
            return true;
        }
        if (source.equals("off") || source.equals("false") || source.equals("0") || source.equals("no")) {
            return false;
        }
        throw new IllegalArgumentException("Not boolean: '" + value + "'");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        String result = "(" + theConfiguration.toString() + ")";
        if (defaultConf == null) {
            return result;
        }
        return "(Default: " + defaultConf.toString() + result + ")";
    }

    /**
     * This method <strong>never</strong> returns normally, because it's
     * supposed to throw the {@code IllegalArgumentException} with the message
     * depending on the parameter value.
     *
     * @param key Key Configuration keyword to report the problem against.
     * @exception IllegalArgumentException that contains the detailed message
     * about the problem source.
     */
    protected void duplicateKeyword(String key) {
        throw new IllegalArgumentException(getClass().getName() + "#" + Integer.toHexString(hashCode()) + ": " + "The invocation context for the configuration item " + "assumes single value, check for duplicate entries: " + key);
    }

    /**
     * {@inheritDoc}
     */
    public Object get(String key) {
        Object found = theConfiguration.get(key);
        if (found == null && !theConfiguration.containsKey(key)) {
            throw new NoSuchElementException("No configuration value stored for '" + key + "'");
        }
        return found;
    }

    /**
     * {@inheritDoc}
     */
    public Object get(String key, Object defaultValue) {
        try {
            return get(key);
        } catch (NoSuchElementException ex) {
            return defaultValue;
        }
    }

    /**
     * {@inheritDoc}
     */
    public Configuration getDefaultConfiguration() {
        return defaultConf;
    }

    /**
     * {@inheritDoc}
     */
    public URL getURL() {
        return baseURL;
    }

    /**
     * {@inheritDoc}
     */
    public synchronized URL[] getUrlChain() {
        int size = (baseURL == null) ? 0 : 1;
        Configuration conf = getDefaultConfiguration();
        while (conf != null) {
            if (conf.getURL() != null) {
                size++;
            }
            conf = conf.getDefaultConfiguration();
        }
        if (size == 0) {
            return null;
        }
        URL chain[] = new URL[size];
        int idx = 0;
        conf = this;
        do {
            if (conf.getURL() != null) {
                chain[idx++] = conf.getURL();
            }
            conf = conf.getDefaultConfiguration();
        } while (conf != null);
        return chain;
    }

    /**
     * {@inheritDoc}
     */
    public Set<String> keySet() {
        return theConfiguration.keySet();
    }
}
