package org.pwr.odwa.server.metadata;

/**
 * Stores internal information of a measure.
 *
 * This class is an image of 'measure' tag from metadata XML,
 * giving a complete information set about a single measure
 * in a data warehouse project.
 * <p>
 * The information includes:
 * <ul>
 * <li>table - name a the given facts table </li>
 * <li>field - name of a column in the given facts table</li>
 * <li>format - printf-like format string to print value of this measure properly</li>
 * <li>function - name of aggregate function of this measure</li>
 * </ul>
 * <p>
 * This component extends {@link Meta} class inheriting basic meta
 * element properties from it (unique ID, name and description).
 */
public class Measure extends Meta {

    protected String m_table;

    protected String m_field;

    protected String m_format;

    protected String m_function;

    public void setTable(String table) {
        m_table = table;
    }

    public void setField(String field) {
        m_field = field;
    }

    public void setFormat(String format) {
        m_format = format;
    }

    public void setFunction(String function) {
        m_function = function;
    }

    public String getTable() {
        return m_table;
    }

    public String getField() {
        return m_field;
    }

    public String getFormat() {
        return m_format;
    }

    public String getFunction() {
        return m_function;
    }

    public String getUniqueName(Metadata meta) {
        return "[Measures]." + super.getUniqueName(meta);
    }
}
