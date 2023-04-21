package org.josef.util.csv;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.josef.util.CDebug;
import org.josef.util.CodeDescription;
import org.josef.util.InputOutputUtil;

/**
 * Utility class to write csv (Comma Separated Values) data to a File.
 * @author Kees Schotanus
 * @version 1.0 $Revision: 610 $
 */
public class CsvFileWriter extends AbstractCsvWriter {

    /**
     * The file to write the csv output to.
     */
    private File file;

    /**
     * The file writer to write the csv output to.
     */
    private FileWriter fileWriter;

    /**
     * The buffered writer to write the csv output to.
     */
    private BufferedWriter bufferedWriter;

    /**
     * Constructs this CsvFileWriter that will write csv data to the supplied
     * file.
     * <br>By default a comma is used as the column separator and the platform's
     * line.separator is used to separate the rows.
     * @param file The file to read the csv data from.
     * @throws NullPointerException When the supplied file is null.
     */
    public CsvFileWriter(final File file) {
        this.file = file;
    }

    /**
     * Constructs this CsvFileWriter that will write csv data to the supplied
     * file and will use the supplied columnSeparator as a column separator.
     * <br>By default, the platform's line.separator is used to separate the
     * rows.
     * @param file The file to read the csv data from.
     * @param columnSeparator The separator that separates the different
     *  columns.
     * @throws NullPointerException When either the supplied file or
     *  columnSeparator is null.
     */
    public CsvFileWriter(final File file, final Character columnSeparator) {
        super(columnSeparator);
        CDebug.checkParameterNotNull(file, "file");
        this.file = file;
    }

    /**
     * Constructs this CsvFileWriter that will write csv data to the supplied
     * file and will use the supplied columnSeparator as a column separator and
     * the supplied rowSeparator to separate the rows.
     * @param file The file to read the csv data from.
     * @param columnSeparator The separator that separates the different
     *  columns.
     * @param rowSeparator The separator that separates the different rows.
     * @throws NullPointerException When either the supplied file,
     *  columnSeparator or rowSeparator is null.
     */
    public CsvFileWriter(final File file, final Character columnSeparator, final String rowSeparator) {
        super(columnSeparator, rowSeparator);
        CDebug.checkParameterNotNull(file, "file");
    }

    /**
     * Writes a single row to the file.
     * @param row The row to write.
     * @throws IOException When the row could not be written,
     */
    public void writeRow(final String row) throws IOException {
        bufferedWriter.write(row);
    }

    /**
     * Writes all the rows, fetched from the supplied iterator, to file.
     * @param iterator The iterator to get the csv data from.
     * @throws CsvException When the csv data could not be written.
     * @throws NullPointerException When the supplied iterator is null.
     */
    @Override
    protected void write(final Iterator<String[]> iterator) throws CsvException {
        try {
            fileWriter = new FileWriter(file);
            bufferedWriter = new BufferedWriter(fileWriter);
            while (iterator.hasNext()) {
                final String[] row = iterator.next();
                writeRow(CsvUtil.arrayToCsv((Object[]) row));
            }
            InputOutputUtil.close(bufferedWriter);
            InputOutputUtil.close(fileWriter);
        } catch (final IOException exception) {
            throw new CsvException(exception);
        }
    }

    /**
     * For test purposes only.
     * @param args Not used.
     * @throws Exception When writing the csv files fails.
     */
    public static void main(final String[] args) throws Exception {
        final List<CodeDescription> elements = new ArrayList<CodeDescription>();
        elements.add(new CodeDescription("H", "Hydr;ogen"));
        elements.add(new CodeDescription("He", "Helium"));
        elements.add(new CodeDescription("Li", "Lithium"));
        final List<String> propertyNames = new ArrayList<String>();
        propertyNames.add("code");
        propertyNames.add("description");
        final CsvFileWriter writer = new CsvFileWriter(new File("output/csv.txt"));
        writer.setColumnSeparator(';');
    }
}
