package com.siberhus.commons.properties;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;
import org.apache.commons.lang.ArrayUtils;
import com.siberhus.commons.converter.TypeConvertUtils;
import com.siberhus.commons.io.LineReader;

/**
 * 
 * @author hussachai
 *
 */
@SuppressWarnings("unchecked")
public class LineDelimProperties extends AbstractSimpleProperties {

    private static final long serialVersionUID = 1L;

    public LineDelimProperties() {
    }

    private LineDelimProperties(LineDelimProperties props) {
        super.putAll(props);
        setFile(props.getFile());
        setEncoding(props.getEncoding());
        setInterpolatable(props.isInterpolatable());
    }

    @SuppressWarnings("unchecked")
    public synchronized void load(Reader reader) throws IOException {
        super.clear();
        LineReader lr = new LineReader();
        lr.load(reader);
        for (String line : lr.getLines()) {
            int eqIdx = line.indexOf('=');
            String key = null;
            String value = null;
            Object oldValue = null;
            String newValue[] = null;
            if (eqIdx == -1) {
                key = line;
            } else {
                key = line.substring(0, eqIdx);
                value = line.substring(eqIdx + 1, line.length());
            }
            key = key.trim();
            if (value != null) {
                value = value.trim();
            }
            oldValue = super.get(key);
            if (oldValue != null) {
                if (oldValue instanceof String[]) {
                    newValue = (String[]) ArrayUtils.add((String[]) oldValue, value);
                } else {
                    newValue = new String[] { oldValue.toString(), value };
                }
                super.put(key, newValue);
            } else {
                super.put(key, value);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public synchronized void save(Writer writer, String comments) throws IOException {
        BufferedWriter bWriter = null;
        if (writer instanceof BufferedWriter) {
            bWriter = (BufferedWriter) writer;
        } else {
            bWriter = new BufferedWriter(writer);
        }
        if (comments != null) {
            bWriter.append("#" + comments);
        }
        bWriter.newLine();
        for (Map.Entry<Object, Object> entry : super.entrySet()) {
            String key = (String) entry.getKey();
            Object values = entry.getValue();
            if (values instanceof String[]) {
                for (String value : (String[]) values) {
                    bWriter.append(key);
                    bWriter.append("=");
                    bWriter.append(value);
                    bWriter.newLine();
                }
            } else {
                bWriter.append(key);
                bWriter.append("=");
                bWriter.append(values.toString());
                bWriter.newLine();
            }
        }
        bWriter.flush();
    }

    public synchronized String setProperty(String key, String value) {
        String oldValue = getProperty(key);
        super.put(key, value);
        return oldValue;
    }

    @SuppressWarnings("unchecked")
    public synchronized String[] setProperty(String key, String[] values) {
        String oldValues[] = getPropertyAsArray(key);
        super.put(key, values);
        return oldValues;
    }

    @SuppressWarnings("unchecked")
    public synchronized String[] addPropertyValue(String key, String value) {
        String oldValues[] = getPropertyAsArray(key);
        String values[] = null;
        if (oldValues != null) {
            values = oldValues.clone();
        } else {
            values = new String[0];
        }
        values = (String[]) ArrayUtils.add(values, value);
        super.put(key, values);
        return oldValues;
    }

    @SuppressWarnings("unchecked")
    public String getProperty(String key) {
        Object values = super.get(key);
        if (values != null) {
            String value = null;
            if (values instanceof String[]) {
                value = ((String[]) values)[0];
            } else {
                value = values.toString();
            }
            if (isInterpolatable()) {
                return interpolate(value);
            } else {
                return value;
            }
        }
        return null;
    }

    public String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        if (value != null) {
            return value;
        }
        return defaultValue;
    }

    @SuppressWarnings("unchecked")
    public String[] getPropertyAsArray(String key) {
        Object valueObj = super.get(key);
        if (valueObj != null) {
            if (valueObj instanceof String[]) {
                String values[] = (String[]) valueObj;
                if (isInterpolatable()) {
                    for (int i = 0; i < values.length; i++) {
                        values[i] = interpolate(values[i]);
                    }
                }
                return values;
            } else {
                String value = valueObj.toString();
                if (isInterpolatable()) {
                    value = interpolate(value);
                }
                return new String[] { value };
            }
        }
        return null;
    }

    public String[] getPropertyAsArray(String key, String[] defaultValues) {
        String values[] = getPropertyAsArray(key);
        if (values != null) {
            return values;
        }
        return defaultValues;
    }

    @SuppressWarnings("unchecked")
    public <T> T getProperty(Class<T> type, String key, T defaultValue) {
        String value = getProperty(key);
        if (value != null) {
            return (T) TypeConvertUtils.convert(value, type, defaultValue);
        }
        return defaultValue;
    }

    public <T> T getProperty(Class<T> type, String key) {
        String value = getProperty(key);
        return TypeConvertUtils.convert(value, type);
    }

    @SuppressWarnings("unchecked")
    public <T> T[] getPropertyAsArray(Class<T> type, String key) {
        String[] values = getPropertyAsArray(key);
        if (values != null) {
            return TypeConvertUtils.convert(values, type);
        }
        return null;
    }

    public LineDelimProperties duplicate() {
        return new LineDelimProperties(this);
    }

    public static void main(String[] args) throws IOException {
        LineDelimProperties p = new LineDelimProperties();
        p.load(new File("resources/thailang.txt"), "UTF-8");
        System.out.println(p.getProperty("cat"));
        System.out.println(p.getPropertyAsArray("cat"));
        System.out.println("novalue=" + p.getProperty("novalue"));
        System.out.println("notexist=" + p.getProperty("notexist"));
        p.setProperty("hussachai", "smart");
        p.setProperty("greeting", new String[] { "1", "2", "3" });
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream("resources/thailang2.txt"), "UTF-8");
        p.save(writer, "");
        writer.close();
        System.out.println(p.duplicate());
    }
}
