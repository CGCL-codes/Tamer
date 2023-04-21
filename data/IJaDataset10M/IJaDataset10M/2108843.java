package com.novocode.naf.data;

/**
 * A DataConverter for a string array with varying delimiters and parameters.
 *
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Oct 7, 2004
 * @version $Id: StringArrayDataConverter.java 338 2005-06-04 19:28:23 +0000 (Sat, 04 Jun 2005) szeiger $
 */
public class StringArrayDataConverter implements IDataConverter {

    private static final StringArrayDataConverter instance = new StringArrayDataConverter(";", false, true);

    private final String separators;

    private final boolean trim;

    private final boolean excludeEmptyEntries;

    public static StringArrayDataConverter getDefaultInstance() {
        return instance;
    }

    public StringArrayDataConverter(String separators, boolean trim, boolean excludeEmptyEntries) {
        this.separators = separators;
        this.trim = trim;
        this.excludeEmptyEntries = excludeEmptyEntries;
    }

    public String toExternal(Class<?> type, Object value) throws Exception {
        if (value == null) return null;
        String[] sa = (String[]) value;
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < sa.length; i++) {
            if (i != 0) buf.append(separators.charAt(0));
            if (trim) buf.append(' ');
            buf.append(sa[i]);
        }
        return buf.toString();
    }

    public Object toInternal(Class<?> type, String value) throws Exception {
        return DataDecoder.decodeStringArray(value, separators, trim, excludeEmptyEntries);
    }
}
