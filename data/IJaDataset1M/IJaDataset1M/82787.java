package net.sf.FFReport.Common;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import net.sf.JRecord.Common.RecordException;
import net.sf.JRecord.Details.AbstractFieldValue;
import net.sf.JRecord.Details.AbstractLine;
import net.sf.JRecord.Details.LayoutDetail;
import net.sf.JRecord.Details.Line;
import net.sf.JRecord.IO.AbstractLineReader;
import net.sf.JRecord.IO.LineIOProvider;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/**
 * @author Bruce Martin
 * @version $Id: FFReport
 */
public class RecordDataSource implements JRDataSource {

    public static final int ALL_RECORDS = -1331;

    private LayoutDetail layout;

    private AbstractLineReader reader;

    private AbstractLine currentLine = null;

    private AbstractLine[] lines;

    private int requiredRecord = ALL_RECORDS;

    /**
	 * create a Jasper DataSource That reads a file (and formats the
	 * record via a RecordLayout).
	 *
	 * @param recordLayout record layout used to view/format a line (or record)
	 * @param fileName name of the file to be read
	 *
	 * @throws IOException any io-error that occurs
	 */
    public RecordDataSource(final LayoutDetail recordLayout, final String fileName) throws IOException, RecordException {
        this(LineIOProvider.getInstance(), recordLayout, new FileInputStream(fileName));
    }

    /**
	 * create a Jasper DataSource That reads a file (and formats the
	 * record via a RecordLayout).
	 *
	 * @param recordLayout record layout used to view/format a line (or record)
	 * @param in stream to be read
	 *
	 * @throws IOException any IO error that occurs
	 */
    public RecordDataSource(final LayoutDetail recordLayout, final InputStream in) throws IOException, RecordException {
        this(LineIOProvider.getInstance(), recordLayout, in);
    }

    /**
	 * create a Jasper DataSource That reads a file (and formats the
	 * record via a RecordLayout).
	 *
	 * @param provider LineIOProvider that creates the appropriate
	 *        line-Readers for a specified FileStructure
	 * @param recordLayout record layout used to view/format a line (or record)
	 * @param in stream to be read
	 *
	 * @throws IOException any ioerror that occurs
	 */
    public RecordDataSource(final LineIOProvider provider, final LayoutDetail recordLayout, final InputStream in) throws IOException, RecordException {
        super();
        layout = recordLayout;
        reader = provider.getLineReader(layout.getFileStructure());
        reader.open(in, layout);
        lines = new Line[layout.getRecordCount()];
    }

    /**
	 * move to the next Record
	 *
	 * @return wether another record was read or not
	 *
	 * @throws JRException any error that occurs
	 */
    public boolean next() throws JRException {
        boolean ret = false;
        try {
            readLine();
            if (requiredRecord != ALL_RECORDS) {
                while (currentLine != null && (currentLine.getPreferredLayoutIdx() != requiredRecord)) {
                    readLine();
                }
            }
            ret = currentLine != null;
        } catch (Exception e) {
            throw new JRException(e);
        }
        return ret;
    }

    /**
	 * read one line (and store it in the lines array).
	 *
	 * @throws IOException any IO error
	 */
    private void readLine() throws IOException {
        currentLine = reader.read();
        if (currentLine != null && (currentLine.getPreferredLayoutIdx() >= 0)) {
            lines[currentLine.getPreferredLayoutIdx()] = currentLine;
        }
    }

    /**
	 * Get a fields value
	 *
	 * @param field field that is required
	 *
	 * @return fields value
	 *
	 * @throws JRException any error that occurs
	 */
    public Object getFieldValue(JRField field) throws JRException {
        String name = getRecEditName(field);
        Object ret = null;
        int pos = name.indexOf('.');
        if (pos > 0 && (pos + 1 < name.length())) {
            int idx;
            String recordName = name.substring(0, pos);
            name = name.substring(pos + 1);
            idx = layout.getRecordIndex(recordName);
            if (idx >= 0 && lines[idx] != null) {
                ret = getField(lines[idx], field, name);
            } else {
                System.out.print(" is null: " + (lines[idx] == null) + " index=" + idx + " Field=" + name);
            }
        } else {
            ret = getField(currentLine, field, name);
        }
        return ret;
    }

    /**
	 * Get the fields value
	 *
	 * @param line line to get the value from
	 * @param field Jasper field being retrieved
	 * @param fieldName field name to be retrieved
	 *
	 * @return request field value
	 */
    @SuppressWarnings({ "unchecked" })
    private Object getField(AbstractLine line, JRField field, String fieldName) {
        Object ret = "";
        try {
            AbstractFieldValue retVal = line.getFieldValue(fieldName);
            ret = null;
            if (field.getValueClass() == String.class) {
                ret = retVal.asString();
            } else if (field.getValueClass() == Long.class) {
                ret = retVal.asLong();
            } else if (field.getValueClass() == BigDecimal.class) {
                ret = retVal.asBigDecimal();
            } else if (field.getValueClass() == Double.class) {
                ret = retVal.asDouble();
            } else {
                Object[] params = { retVal.asString() };
                Class[] c = { String.class };
                ret = field.getValueClass().getConstructor(c).newInstance(params);
            }
        } catch (Exception e) {
            System.out.println("*** Error in Field: " + fieldName + " >> " + e.getLocalizedMessage());
        }
        return ret;
    }

    /**
	 * get the RecordEdit field name from the Jasper Field (JRField)
	 *
	 * @param field Jasper Field
	 *
	 * @return RecordEdit Field
	 */
    private String getRecEditName(JRField field) {
        String ret = field.getDescription();
        if (ret == null || "".equals(ret)) {
            ret = field.getName();
        }
        return ret;
    }

    /**
     * get the record to be supplied Jasper
     *
     * @return Returns the requiredRecord (i.e. record to be supplied Jasper).
     */
    public int getRequiredRecord() {
        return requiredRecord;
    }

    /**
     * set the record to be supplied Jasper
     *
     * @param newRequiredRecord The requiredRecord to set.
     */
    public void setRequiredRecord(int newRequiredRecord) {
        this.requiredRecord = newRequiredRecord;
    }
}
