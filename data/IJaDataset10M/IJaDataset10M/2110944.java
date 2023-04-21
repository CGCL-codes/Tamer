package net.sf.doolin.gui.tabular.csv;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.doolin.gui.tabular.io.TabularWriter;
import net.sf.doolin.gui.tabular.model.TabularColumn;
import net.sf.doolin.gui.tabular.model.TabularModel;
import org.apache.commons.lang.StringUtils;

/**
 * Writer for CSV format.
 * 
 * @author Damien Coraboeuf
 * @param <T>
 *            Type of object per row
 */
public class CSVWriter<T> implements TabularWriter<T> {

    private String encoding = "UTF-8";

    private String separator = ",";

    private static final Map<Class<?>, CSVAdapter> defaultAdapters = new HashMap<Class<?>, CSVAdapter>();

    static {
        defaultAdapters.put(boolean.class, new PrimitiveCSVAdapter());
        defaultAdapters.put(byte.class, new PrimitiveCSVAdapter());
        defaultAdapters.put(short.class, new PrimitiveCSVAdapter());
        defaultAdapters.put(int.class, new PrimitiveCSVAdapter());
        defaultAdapters.put(long.class, new PrimitiveCSVAdapter());
        defaultAdapters.put(float.class, new PrimitiveCSVAdapter());
        defaultAdapters.put(double.class, new PrimitiveCSVAdapter());
        defaultAdapters.put(BigDecimal.class, new PrimitiveCSVAdapter());
        defaultAdapters.put(Boolean.class, new PrimitiveCSVAdapter());
        defaultAdapters.put(Byte.class, new PrimitiveCSVAdapter());
        defaultAdapters.put(Short.class, new PrimitiveCSVAdapter());
        defaultAdapters.put(Integer.class, new PrimitiveCSVAdapter());
        defaultAdapters.put(Long.class, new PrimitiveCSVAdapter());
        defaultAdapters.put(Float.class, new PrimitiveCSVAdapter());
        defaultAdapters.put(Double.class, new PrimitiveCSVAdapter());
        defaultAdapters.put(String.class, new StringCSVAdapter());
    }

    private CSVAdapter defaultAdapter = new StringCSVAdapter();

    private Map<Class<?>, CSVAdapter> adapters = new HashMap<Class<?>, CSVAdapter>();

    /**
	 * Returns the <code>defaultAdapter</code> property.
	 * 
	 * @return <code>defaultAdapter</code> property.
	 */
    public CSVAdapter getDefaultAdapter() {
        return this.defaultAdapter;
    }

    /**
	 * Returns the <code>encoding</code> property. It defaults to UTF-8.
	 * 
	 * @return <code>encoding</code> property.
	 */
    public String getEncoding() {
        return this.encoding;
    }

    /**
	 * Returns the <code>separator</code> property. It defaults to ",".
	 * 
	 * @return <code>separator</code> property.
	 */
    public String getSeparator() {
        return this.separator;
    }

    /**
	 * Sets the <code>defaultAdapter</code> property.
	 * 
	 * @param defaultAdapter
	 *            <code>defaultAdapter</code> property.
	 */
    public void setDefaultAdapter(CSVAdapter defaultAdapter) {
        this.defaultAdapter = defaultAdapter;
    }

    /**
	 * Sets the <code>encoding</code> property. It defaults to UTF-8.
	 * 
	 * @param encoding
	 *            <code>encoding</code> property.
	 */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    /**
	 * Sets the <code>separator</code> property. It defaults to ",".
	 * 
	 * @param separator
	 *            <code>separator</code> property.
	 */
    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public void write(OutputStream output, TabularModel<T> model) throws IOException {
        PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(output, this.encoding)));
        try {
            List<String> line = new ArrayList<String>();
            int columnCount = model.getColumnCount();
            for (int i = 0; i < columnCount; i++) {
                TabularColumn<T> column = model.getColumn(i);
                String name = column.getTitle();
                line.add(name);
            }
            writeLine(writer, line);
            model.reset();
            T item;
            while ((item = model.next()) != null) {
                line = new ArrayList<String>();
                for (int col = 0; col < columnCount; col++) {
                    TabularColumn<T> column = model.getColumn(col);
                    Object value = column.getValue(item);
                    String csvValue = getCSVValue(value);
                    line.add(csvValue);
                }
                writeLine(writer, line);
            }
        } finally {
            writer.flush();
        }
    }

    /**
	 * Adapts a value for CSV
	 * 
	 * @param value
	 *            Value to adapt
	 * @return Adapted value
	 */
    protected String getCSVValue(Object value) {
        if (value == null) {
            return "";
        } else {
            CSVAdapter adapter;
            Class<?> type = value.getClass();
            adapter = this.adapters.get(type);
            if (adapter == null) {
                adapter = defaultAdapters.get(type);
            }
            if (adapter == null) {
                adapter = this.defaultAdapter;
            }
            String string = adapter.getCSVValue(value);
            return string;
        }
    }

    /**
	 * Writes a line in the CSV destination.
	 * 
	 * @param writer
	 *            Writer to write to
	 * @param line
	 *            Line to write
	 */
    protected void writeLine(PrintWriter writer, List<String> line) {
        String wholeLine = StringUtils.join(line.iterator(), this.separator);
        writer.println(wholeLine);
    }
}
