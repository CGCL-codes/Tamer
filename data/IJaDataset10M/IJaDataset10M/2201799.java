package com.continuent.tungsten.manager.client;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Implements a client result formatter. Default result formatting is to call
 * Object.toString(). Tabular values are formatted appropriately.
 * 
 * @author <a href="mailto:robert.hodges@continuent.com">Robert Hodges</a>
 * @version 1.0
 */
public class ResultFormatter {

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    private static final String keyHeader = "Key";

    private static final String valueHeader = "Value";

    protected final Object result;

    /**
     * Creates a new formatter for a particular object.
     */
    public ResultFormatter(Object result) {
        this.result = result;
    }

    /**
     * Returns a formatted value.
     */
    public String format() {
        if (result == null) {
            return "(null)";
        } else if (result instanceof Map) {
            return format((Map<?, ?>) result);
        } else {
            return format(result);
        }
    }

    /**
     * Default object formatter using Object.toString().
     */
    protected String format(Object o) {
        return o.toString();
    }

    /**
     * Object formatter for Map instance. This sorts keys and
     * then formats the key value pairs into a nice tabular representation.
     */
    protected String format(Map<?, ?> props) {
        StringWriter writer = new StringWriter();
        PrintWriter printer = new PrintWriter(writer, true);
        Iterator<?> keyIterator = props.keySet().iterator();
        int keyLength = keyHeader.length();
        int valueLength = valueHeader.length();
        TreeMap<String, String> sortedProperties = new TreeMap<String, String>();
        while (keyIterator.hasNext()) {
            String key = keyIterator.next().toString();
            String value = props.get(key).toString();
            if (key.length() > keyLength) keyLength = key.length();
            if (value.length() > valueLength) valueLength = value.length();
            sortedProperties.put(key, value);
        }
        String format = "%-" + keyLength + "." + keyLength + "s %-" + valueLength + "." + valueLength + "s" + LINE_SEPARATOR;
        String formatHeader = "%-" + keyLength + "." + keyLength + "s %-" + valueHeader.length() + "." + valueHeader.length() + "s" + LINE_SEPARATOR;
        printer.printf(formatHeader, keyHeader, valueHeader);
        printer.printf(formatHeader, "---", "-----");
        for (String key : sortedProperties.keySet()) {
            printer.printf(format, key, sortedProperties.get(key));
        }
        printer.close();
        return writer.toString();
    }
}
