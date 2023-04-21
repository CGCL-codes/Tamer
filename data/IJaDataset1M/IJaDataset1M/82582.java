package org.jxpfw.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.josef.util.CDebug;
import org.josef.util.CReflection;
import org.josef.util.csv.CsvUtil;

/**
 * Abstract utility class to aid in the creation of csv output.
 * <br>csv output format normally consists of:<br>
 * - an optional header row that describes all columns.<br>
 * - one or more data rows consisting of several columns.<br>
 * For both the header row and the data rows it is true that every individual
 * column is separated with a column separator like a comma.<br>
 * Note for designers:<br>
 * Concrete subclasses normally will want to override the {@link #start()} and
 * {@link #stop()} methods and most importantly the {@link #writeRow(String)}
 * method. A concrete subclass that writes csv output to a file for example may
 * want to open and close the file upon start() and (stop) respectively.<br>
 * On another note: Users of jxpfw should normally be allowed to simply use one
 * of the high-level methods like writeObjectsToCsv(...) to create csv output
 * (without calling neither start() nor stop()). For low-level construction of
 * csv output, for example when the input is not available as objects, the low
 * level start() and stop() methods should be overridden.
 * @author Kees Schotanus
 * @version 1.0 $Revision: 1.14 $
 */
public abstract class AbstractCsvWriter {

    /**
     * The separator that separates the different columns.
     */
    private String columnSeparator = ",";

    /**
     * The separator that separates the different rows.
     */
    private String rowSeparator = System.getProperty("line.separator");

    /**
     * Determines whether a header row is created or not.
     */
    private boolean outputHeader = true;

    /**
     * Creates this CsvCreator with a comma as a separator.
     */
    public AbstractCsvWriter() {
    }

    /**
     * Creates this Csv Writer using the supplied columnSeparator as a separator
     * between the different columns.
     * @param columnSeparator The separator that separates the different
     *  columns.
     *  <br>Normally you will want to supply a separator consisting of a single
     *  character. Longer separators may work but you are on your own here.
     * @throws NullPointerException When the supplied columnSeparator is null.
     */
    public AbstractCsvWriter(final String columnSeparator) {
        CDebug.checkParameterNotNull(columnSeparator, "columnSeparator");
        this.columnSeparator = columnSeparator;
    }

    /**
     * Gets the currently used column separator.
     * @return The currently used column separator.
     */
    public String getColumnSeparator() {
        return columnSeparator;
    }

    /**
     * Sets the separator that will be used to separate the individual columns.
     * @param columnSeparator The separator that will be used to separate the
     *  individual columns.
     *  <br>You will normally want to use a comma or a semicolon here. A
     *  separator normally consists of a single character. Longer separators may
     *  work but you are on your own!
     * @throws NullPointerException When the supplied columnSeparator is null.
     */
    public void setColumnSeparator(final String columnSeparator) {
        CDebug.checkParameterNotNull(columnSeparator, "columnSeparator");
        this.columnSeparator = columnSeparator;
    }

    /**
     * Gets the currently used row separator.
     * @return The currently used row separator.
     */
    public String getRowSeparator() {
        return rowSeparator;
    }

    /**
     * Sets the separator that will be used to separate the individual rows.
     * @param rowSeparator The separator that will be used to separate the rows.
     *  <br>You will normally want to use the line separator used by the current
     *  operating system (which is the default).<br>
     *  Note: No check is made whether this separator differs from the column
     *  separator or the column separator is part of this separator but I can
     *  guarantee trouble when this is the case.
     * @throws NullPointerException When the supplied rowSeparator is null.
     */
    public void setRowSeparator(final String rowSeparator) {
        CDebug.checkParameterNotNull(rowSeparator, "rowSeparator");
        this.rowSeparator = rowSeparator;
    }

    /**
     * Prevents output of a header row.
     * <br>By default the first row is the header row. Call this method for raw
     * data output.
     */
    public void setNoHeader() {
        this.outputHeader = false;
    }

    /**
     * Writes the supplied list of objects to csv format.
     * <br>The header names are deduced from the supplied propertyNames.<br>
     * Note: This method expects a homogeneous list of objects!
     * @param objects The list of value to convert to csv.
     * @param propertyNames The names of the properties which values will be
     *  converted to cvs.
     * @param formatter The formatter to format the supplied objects.
     *  <br>Null may be supplied to not format objects at all.
     * @throws NullPointerException When either the supplied list of objects or
     *  propertyNames is null.
     * @throws IllegalArgumentException When the supplied list of propertyNames
     *  is empty.
     * @throws Exception When the csv output could not be created.
     * @see #propertyNameToHeaderName(java.lang.String)
     */
    public void writeObjectsToCsv(final List<?> objects, final List<String> propertyNames, final Formatter formatter) throws Exception {
        CDebug.checkParameterNotNull(objects, "objects");
        CDebug.checkParameterNotEmpty(propertyNames, "propertyNames");
        final List<String> headerNames = new ArrayList<String>(propertyNames.size());
        for (final String propertyName : propertyNames) {
            headerNames.add(propertyNameToHeaderName(propertyName));
        }
        writeObjectsToCsv(objects, propertyNames, headerNames, formatter);
    }

    /**
     * Writes the supplied list of objects to csv format.
     * <br>Note: This method expects a homogeneous list of objects!
     * @param objects The list of objects to convert to csv.
     * @param propertyNames The names of the properties which values will be
     *  converted to cvs.
     * @param headerNames The header names to use for the corresponding property
     *  names.
     *  <br>Null may be supplied to suppress the output of a header.
     * @param formatter The formatter to format the supplied objects.
     *  <br>Null may be supplied to not format objects at all.
     * @throws NullPointerException When either the supplied list of objects or
     *  propertyNames is null.
     * @throws IllegalArgumentException When the supplied list of propertyNames
     *  is empty or (when headerNames is non null) it is not the same size as
     *  the supplied headerNames list.
     * @throws Exception When the csv output could not be created.
     */
    public void writeObjectsToCsv(final List<?> objects, final List<String> propertyNames, final List<String> headerNames, final Formatter formatter) throws Exception {
        CDebug.checkParameterNotNull(objects, "objects");
        CDebug.checkParameterNotEmpty(propertyNames, "propertyNames");
        if (headerNames != null && propertyNames.size() != headerNames.size()) {
            throw new IllegalArgumentException("#Header names[" + headerNames.size() + "] differs from " + "#property names[" + propertyNames.size() + "]!");
        }
        writeCsv(objects, propertyNamesToMethods(objects.iterator().next(), propertyNames), headerNames, formatter);
    }

    /**
     * Writes the supplied list of objects to csv format.
     * <br>Note: This method expects a homogeneous list of objects!
     * @param objects The list of objects to write as csv output.
     * @param getters The methods that get information from the supplied
     *  objects.
     * @param headerNames The header to use for the corresponding property.
     *  <br>Null may be supplied to suppress the output of a header row.
     * @param formatter The formatter to format the supplied objects.
     *  <br>Null may be supplied to not format objects at all.
     * @throws NullPointerException When either the supplied list of objects or
     *  getters is null.
     * @throws IllegalArgumentException When the supplied list of getters is
     *  empty or (when headerNames is non null) it is not the same size as the
     *  supplied headerNames list.
     * @throws Exception When the csv output could not be created.
     */
    public void writeCsv(final List<?> objects, final List<Method> getters, final List<String> headerNames, final Formatter formatter) throws Exception {
        CDebug.checkParameterNotNull(objects, "objects");
        CDebug.checkParameterNotNull(getters, "getters");
        if (headerNames != null) {
            CDebug.checkParameterTrue(getters.size() == headerNames.size(), "#Header names[" + headerNames.size() + "] differs from #getters[" + getters.size() + "]!");
        }
        start();
        if (headerNames != null && !headerNames.isEmpty() && outputHeader) {
            writeRow(createRow(headerNames));
        }
        for (final Object object : objects) {
            final List<String> row = new ArrayList<String>(headerNames.size());
            for (final Method getter : getters) {
                try {
                    final Object column = getter.invoke(object);
                    row.add(columnToString(column, formatter));
                } catch (final IllegalAccessException exception) {
                    row.add("Error!");
                } catch (final InvocationTargetException exception) {
                    row.add("Error!");
                }
            }
            writeRow(createRow(row));
        }
        stop();
    }

    /**
     * Creates a String representation of the supplied column, using the
     * supplied formatter to format the column.
     * @param column The column to create a String representation of.
     * @param formatter The formatter to format the supplied column.
     *  <br>
     * @return String representation of the supplied column, using the
     *  supplied formatter to format the column.
     */
    private String columnToString(final Object column, final Formatter formatter) {
        String result;
        if (column == null) {
            result = "";
        } else if (formatter == null) {
            result = column.toString();
        } else if (column instanceof java.sql.Date) {
            result = formatter.formatDate((java.sql.Date) column);
        } else if (column instanceof java.util.Date) {
            result = formatter.formatDateTime((java.util.Date) column);
        } else if (column instanceof java.sql.Timestamp) {
            result = formatter.formatDateTime((java.sql.Timestamp) column);
        } else if (column instanceof Double) {
            result = formatter.formatDecimal((Double) column);
        } else if (column instanceof Float) {
            result = formatter.formatDecimal((Float) column);
        } else if (column instanceof Integer) {
            result = formatter.formatInteger((Integer) column);
        } else {
            result = column.toString();
        }
        return result;
    }

    /**
     * Writes the supplied fields as a single row to csv output.
     * @param columns The columns to write as a csv row.
     * @throws Exception When the columns could not be written.
     */
    public void writeColumns(final String... columns) throws Exception {
        writeRow(createRow(columns));
    }

    /**
     * Initializes the creation of the csv output.
     * <br>This method is guaranteed to be called before any output is created
     * and is a great method to override to perform your own initialization.
     * @throws Exception When the implementation of a subclass throws this
     *  exception.
     */
    protected void start() throws Exception {
    }

    /**
     * Writes a single row.
     * @param row The row to write.
     *  <br>The row consists of properly separated columns and a row separator.
     * @throws Exception When the row could not be written.
     */
    public abstract void writeRow(final String row) throws Exception;

    /**
     * Counterpart of the start() method.
     * <br>This method is guaranteed to be called after any output is created
     * and is a great method to override to perform your own clean-up code.
     * @throws Exception When the implementation of a subclass throws this
     *  exception.
     */
    protected void stop() throws Exception {
    }

    /**
     * Creates a row of properly separated columns.
     * <br>The resulting row will have a row separator appended to it.
     * @param columns The columns to create the row from.
     * @return A single row created from the supplied columns.
     */
    public String createRow(final List<String> columns) {
        if (columns == null || columns.isEmpty()) {
            return "";
        }
        final StringBuilder row = new StringBuilder();
        row.append(createColumn(columns.get(0)));
        for (int i = 1; i < columns.size(); i++) {
            row.append(columnSeparator);
            row.append(createColumn(columns.get(i)));
        }
        row.append(rowSeparator);
        return row.toString();
    }

    /**
     * Creates a row of properly separated columns.
     * <br>The resulting row will have a row separator appended to it.
     * @param columns The columns to create the row from.
     * @return A single row created from the supplied columns.
     */
    public String createRow(final String... columns) {
        if (columns == null || columns.length == 0) {
            return "";
        }
        final StringBuilder row = new StringBuilder();
        row.append(createColumn(columns[0]));
        for (int i = 1; i < columns.length; i++) {
            row.append(columnSeparator);
            row.append(createColumn(columns[i]));
        }
        row.append(rowSeparator);
        return row.toString();
    }

    /**
     * Creates a single column.
     * <br>This method investigates the column's content and determines whether
     * it needs to be quoted before it is being written. This for example is
     * necessary when the field contains the separator character. For example:
     * When 3,141.60 would be written as is and a comma is used as a separator,
     * then this field would take up two columns!
     * @param column The column to write.
     * @return The massaged column that is to say that it is properly quoted.
     *  <br>When the column does not contain the column separator character it
     *  is returned unchanged.
     */
    public String createColumn(final String column) {
        return CsvUtil.quoteCsv(column, columnSeparator.charAt(0));
    }

    /**
     * Creates a header column from the supplied propertyName.
     * <br>Currently this method simply capitalizes the first character. Future
     * versions may insert a space before every capital.
     * @param propertyName The name of the property to create a header column
     *  for.
     * @return A header column name.
     */
    public String propertyNameToHeaderName(final String propertyName) {
        final String headerName = propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
        return headerName;
    }

    /**
     * For each property name in the supplied list of propertyNames, this method
     * finds the corresponding method that gets the value for the property.
     * @param object The object that contains the methods.
     * @param propertyNames The property names.
     * @return A list of methods that can access the corresponding property.
     * @throws NoSuchMethodException When a property does not have a
     *  corresponding getter.
     * @throws NullPointerException When the supplied object is null.
     */
    private List<Method> propertyNamesToMethods(final Object object, final List<String> propertyNames) throws NoSuchMethodException {
        assert object != null;
        final List<Method> methods = new ArrayList<Method>(propertyNames.size());
        for (final String propertyName : propertyNames) {
            methods.add(CReflection.propertyToGetMethod(object, propertyName));
        }
        return methods;
    }
}
