package pierre.reports;

import java.util.ArrayList;
import pierre.model.*;

public abstract class AbstractTupleRenderer {

    protected ArrayList tuples;

    protected ArrayList excludedAttributes;

    protected ReportFileFormat reportFileFormat;

    protected String delimiter;

    protected FileFormatUtility fileFormatUtility;

    public AbstractTupleRenderer() {
        tuples = new ArrayList();
        excludedAttributes = new ArrayList();
        reportFileFormat = ReportFileFormat.HTML_FORMAT;
        delimiter = "\t";
        fileFormatUtility = new FileFormatUtility();
    }

    public void setReportFileFormat(ReportFileFormat reportFileFormat) {
        this.reportFileFormat = reportFileFormat;
        if (reportFileFormat == ReportFileFormat.TEXT_FORMAT) {
            delimiter = "\t";
        } else if (reportFileFormat == ReportFileFormat.CSV_FORMAT) {
            delimiter = "|";
        }
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public void renderAsXML() {
        reportFileFormat = ReportFileFormat.XML_FORMAT;
    }

    public void renderAsCSV() {
        reportFileFormat = ReportFileFormat.CSV_FORMAT;
        delimiter = "|";
    }

    protected String[] getVisibleAttributeNames(Tuple tuple) {
        String[] allAttributeNames = tuple.getAttributeNames();
        ArrayList visibleAttributeNames = new ArrayList();
        for (int i = 0; i < allAttributeNames.length; i++) {
            if (isAttributeIncluded(allAttributeNames[i]) == true) {
                visibleAttributeNames.add(allAttributeNames[i]);
            }
        }
        String[] results = (String[]) visibleAttributeNames.toArray(new String[0]);
        return results;
    }

    protected String[] getVisibleValues(Tuple tuple) {
        String[] attributeNames = tuple.getAttributeNames();
        Object[] allValues = tuple.getValues();
        ArrayList visibleAttributeValues = new ArrayList();
        for (int i = 0; i < attributeNames.length; i++) {
            if (isAttributeIncluded(attributeNames[i]) == true) {
                visibleAttributeValues.add(renderValue(allValues[i]));
            }
        }
        String[] results = (String[]) visibleAttributeValues.toArray(new String[0]);
        return results;
    }

    protected String renderValue(Object value) {
        if (value instanceof LinkObject) {
            LinkObject linkObject = (LinkObject) value;
            return linkObject.renderLink(reportFileFormat);
        } else {
            return String.valueOf(value);
        }
    }

    public void addTuple(Tuple tuple) {
        tuples.add(tuple);
    }

    public void excludeAttribute(String attributeName) {
        if (excludedAttributes.indexOf(attributeName) == -1) {
            excludedAttributes.add(attributeName);
        }
    }

    protected boolean isAttributeIncluded(String attributeName) {
        int index = excludedAttributes.indexOf(attributeName);
        if (index == -1) {
            return true;
        } else {
            return false;
        }
    }

    protected String writeXMLTuples() {
        StringBuffer buffer = new StringBuffer();
        int numberOfTuples = tuples.size();
        for (int i = 0; i < numberOfTuples; i++) {
            Tuple currentTuple = (Tuple) tuples.get(i);
            writeXMLTuple(buffer, currentTuple);
        }
        return buffer.toString();
    }

    protected void writeXMLTuple(StringBuffer buffer, Tuple tuple) {
        String recordClassName = tuple.getRecordClassName();
        fileFormatUtility.writeXMLBegin(buffer, recordClassName);
        String[] attributeNames = tuple.getAttributeNames();
        for (int i = 0; i < attributeNames.length; i++) {
            Object value = tuple.getValue(attributeNames[i]);
            fileFormatUtility.writeXMLField(buffer, attributeNames[i], String.valueOf(value));
        }
        fileFormatUtility.writeXMLEnd(buffer, recordClassName);
    }
}
