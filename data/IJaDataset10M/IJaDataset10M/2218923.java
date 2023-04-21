package org.gbif.portal.util.file;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Simple implementation of a class to treat tab-delimited files as a data set.
 *
 * @author Donald Hobern
 */
public class DelimitedFileReader {

    /**
   * Logger
   */
    protected Log logger = LogFactory.getLog(DelimitedFileReader.class);

    /**
   * Reader for the input stream
   */
    protected BufferedReader reader;

    /**
   * String used to separate columns (tab, comma, etc.)
   */
    protected String sep;

    /**
   * String to be stripped from both ends of column text where present (quotes)
   */
    protected String strip;

    /**
   * Map constructed from first row if it is a header row, mapping column names
   * to positions
   */
    protected Map<String, Integer> columnMap;

    /**
   * Columns for current row (row incremented by next() method)
   */
    protected List<String> columns;

    /**
   * Row number for current row
   */
    protected int rowNumber = 0;

    /**
   * Disable default constructor
   */
    private DelimitedFileReader() {
    }

    /**
   * Open input stream and initialise reader (including header columns if present)
   *
   * @param is        Stream to read
   * @param sep       Separator string
   * @param strip     Optional quote string
   * @param headerRow true if first row contains column headings
   */
    public DelimitedFileReader(InputStream is, String sep, String strip, boolean headerRow) {
        try {
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            this.sep = sep;
            this.strip = strip;
            if (headerRow) {
                columnMap = new HashMap<String, Integer>();
                next();
                for (int i = 0; i < columns.size(); i++) {
                    columnMap.put(columns.get(i), i);
                }
            }
        } catch (Exception e) {
            logger.error("Exception creating DelimitedFileReader", e);
        }
    }

    /**
   * Read next row of column values
   *
   * @return true if row found
   */
    public boolean next() {
        columns = read();
        if (columns != null) {
            rowNumber++;
        }
        return columns != null;
    }

    /**
   * Get column value by (zero-based) position
   *
   * @param column Column index
   * @return Content of column (may be zero-length string)
   */
    public String get(int column) {
        return columns != null && columns.size() > column && !"\\N".equals(columns.get(column)) ? columns.get(column) : null;
    }

    /**
   * Get column value by column name - only works if header row read by
   * constructor
   *
   * @param columnName Column name
   * @return Content of column (may be zero-length string)
   */
    public String get(String columnName) {
        String value = null;
        if (columnMap != null) {
            Integer column = columnMap.get(columnName);
            if (column != null) {
                value = get(column);
            }
        }
        return value;
    }

    /**
   * Returns number of columns in present row
   *
   * @return number of columns
   */
    public int getColumnCount() {
        return columns == null ? 0 : columns.size();
    }

    /**
   * Returns the number of the present row
   *
   * @return row number
   */
    public int getRowNumber() {
        return rowNumber;
    }

    /**
   * Get column names found by constructor if header row present
   *
   * @return set of column headers (unordered)
   */
    public Set<String> getColumnHeaders() {
        return columnMap == null ? null : columnMap.keySet();
    }

    /**
   * Read row of column values, delimited by separator string and remove
   * quote strings if present
   *
   * @return list of column values for new row, null if no more rows
   */
    private List<String> read() {
        List<String> tokens = null;
        try {
            String line = reader.readLine();
            if (line != null) {
                tokens = new ArrayList<String>();
                int i1 = 0;
                int i2 = line.indexOf(sep, i1);
                while (i1 >= 0 && i1 < line.length()) {
                    int tokenStart = i1;
                    int tokenEnd = i2;
                    if (tokenEnd < tokenStart) {
                        tokenEnd = line.length();
                    }
                    if (strip != null && tokenEnd - tokenStart >= 2 * strip.length() && line.startsWith(strip, tokenStart) && line.startsWith(strip, tokenEnd - strip.length())) {
                        tokenStart += strip.length();
                        tokenEnd -= strip.length();
                    }
                    tokens.add(line.substring(tokenStart, tokenEnd));
                    i1 = i2;
                    if (i1 >= 0) {
                        i1 += sep.length();
                        i2 = line.indexOf(sep, i1);
                    }
                }
            }
        } catch (Exception e) {
        }
        return tokens;
    }
}
