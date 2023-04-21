package net.sf.jctc.common.ldap.resolver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Collection;
import java.util.Enumeration;
import java.util.InvalidPropertiesFormatException;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

/**
 * <p>
 * Type: <strong><code>net.sf.jctc.common.ldap.resolver.ResolvingProperties</code></strong>
 * </p>
 *
 * @version $Rev$
 * @author logicfish
 * @since 0.2
 *
 */
public class ResolvingProperties extends Properties {

    /**
	 * 
	 */
    private static final long serialVersionUID = -4229075417496850844L;

    Properties properties;

    Resolver<? super Object> resolver;

    /**
	 * 
	 */
    public ResolvingProperties(Properties properties, Resolver<? super Object> resolver) {
        this.properties = properties;
        this.resolver = resolver;
    }

    public void clear() {
        properties.clear();
    }

    public boolean contains(Object value) {
        return properties.contains(value);
    }

    public boolean containsKey(Object key) {
        return properties.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return properties.containsValue(value);
    }

    public Enumeration<Object> elements() {
        return properties.elements();
    }

    public Set<Entry<Object, Object>> entrySet() {
        return properties.entrySet();
    }

    public Object get(Object key) {
        return resolver.resolve(properties.get(key));
    }

    public String getProperty(String key, String defaultValue) {
        return resolver.resolve(properties.getProperty(key, defaultValue));
    }

    public String getProperty(String key) {
        return resolver.resolve(properties.getProperty(key));
    }

    public boolean isEmpty() {
        return properties.isEmpty();
    }

    public Enumeration<Object> keys() {
        return properties.keys();
    }

    public Set<Object> keySet() {
        return properties.keySet();
    }

    public void list(PrintStream out) {
        properties.list(out);
    }

    public void list(PrintWriter out) {
        properties.list(out);
    }

    public void load(InputStream inStream) throws IOException {
        properties.load(inStream);
    }

    public void load(Reader reader) throws IOException {
        properties.load(reader);
    }

    public void loadFromXML(InputStream in) throws IOException, InvalidPropertiesFormatException {
        properties.loadFromXML(in);
    }

    public Enumeration<?> propertyNames() {
        return properties.propertyNames();
    }

    public Object put(Object key, Object value) {
        return properties.put(key, value);
    }

    public void putAll(Map<? extends Object, ? extends Object> t) {
        properties.putAll(t);
    }

    public Object remove(Object key) {
        return properties.remove(key);
    }

    public void save(OutputStream out, String comments) {
        properties.save(out, comments);
    }

    public Object setProperty(String key, String value) {
        return properties.setProperty(key, value);
    }

    public int size() {
        return properties.size();
    }

    public void store(OutputStream out, String comments) throws IOException {
        properties.store(out, comments);
    }

    public void store(Writer writer, String comments) throws IOException {
        properties.store(writer, comments);
    }

    public void storeToXML(OutputStream os, String comment, String encoding) throws IOException {
        properties.storeToXML(os, comment, encoding);
    }

    public void storeToXML(OutputStream os, String comment) throws IOException {
        properties.storeToXML(os, comment);
    }

    public Set<String> stringPropertyNames() {
        return properties.stringPropertyNames();
    }

    public Collection<Object> values() {
        return properties.values();
    }
}
