package prajna.semantic.stream;

import java.io.*;
import java.text.ParseException;
import java.util.*;
import java.util.zip.GZIPInputStream;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import prajna.data.DataRecord;
import prajna.semantic.DataTemplate;
import prajna.semantic.FieldDesc;

/**
 * Implementation of a DataRecordStream which parses XML into a series of
 * DataRecord objects. This class is designed to parse data into a specified
 * data template, which would be provided in a configuration file. Otherwise,
 * it attempts to guess the structure of the XML data and parse it into
 * records.
 * 
 * @author <a href="http://www.ganae.com/edswing">Edward Swing</a>
 */
public class SaxRecordStream extends DataRecordStream {

    private String path = null;

    private String curElem = null;

    private String curData = null;

    private DataRecord curRecord = null;

    private SaxRecordHandler recHandle = new SaxRecordHandler();

    private String recTag;

    private String docTag;

    private HashSet<String> templateFields = new HashSet<String>();

    private HashSet<String> unknowns = new HashSet<String>();

    private SAXParser parser;

    /**
     * This class handles the parsing of the XML content. It identifies the
     * current tag and record during the SAX parsing process. If the template
     * has been defined, this class uses the template fields to optimize which
     * tags it parses.
     */
    private class SaxRecordHandler extends DefaultHandler {

        /**
         * Called when the parser parses character data inside of an element
         * 
         * @param ch The characters.
         * @param start The start position in the character array.
         * @param length The number of characters to use from the character
         *            array.
         */
        @Override
        public void characters(char[] ch, int start, int length) {
            if (curElem != null) {
                if (curData != null) {
                    curData += new String(ch, start, length);
                } else {
                    curData = new String(ch, start, length);
                }
            }
        }

        /**
         * Called when the end tag of an element is encountered. If the end tag
         * matches the current tag being parsed, the data is added to the
         * current record. If the end tag matches the record tag, this method
         * finishes the DataRecord
         * 
         * @param uri The Namespace URI
         * @param localName The local name, or the empty string if Namespace
         *            processing is not being performed.
         * @param qName The tag for the element
         */
        @Override
        public void endElement(String uri, String localName, String qName) {
            if (qName.equals(recTag)) {
                finishRecord();
            } else if (curElem != null && curData != null) {
                addData();
            }
        }

        /**
         * Called when the start tag of an element is encountered. If the end
         * tag matches the current tag being parsed, the data is added to the
         * current record. If the end tag matches the record tag, this method
         * finishes the DataRecord.
         * 
         * @param uri The Namespace URI
         * @param localName The local name, or the empty string if Namespace
         *            processing is not being performed.
         * @param qName The tag for the element
         * @param attributes The attributes attached to the element.
         */
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) {
            if (recTag == null && docTag == null) {
                docTag = qName;
            } else if (recTag == null) {
                recTag = qName;
            }
            if (qName.equals(recTag)) {
                curRecord = new DataRecord();
            } else if (curElem == null && templateFields.contains(qName)) {
                curElem = qName;
            } else if (!templateFields.isEmpty()) {
                unknowns.add(qName);
            } else {
                curElem = qName;
            }
        }
    }

    /**
     * Create a SaxRecordStream.
     */
    public SaxRecordStream() {
        this(null);
    }

    /**
     * Create a SaxRecordStream to parse the given file or directory;
     * 
     * @param filePath path to a file or directory
     */
    public SaxRecordStream(String filePath) {
        path = filePath;
        try {
            parser = SAXParserFactory.newInstance().newSAXParser();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    /**
     * Add the data from the current element to the record. If the template has
     * been set, this method uses the template to parse the fields into the
     * DataRecord. Otherwise, it simply adds the value of the field to the
     * current record.
     */
    private void addData() {
        curData = curData.trim();
        if (curRecord != null && curData.length() > 0) {
            DataTemplate template = getTemplate();
            if (template != null) {
                if (curRecord.getName() == null && template.getNameKeyList().contains(curElem)) {
                    curRecord.setName(curData);
                }
                HashMap<String, FieldDesc<?>> descs = template.getFieldDescriptions();
                for (FieldDesc<?> desc : descs.values()) {
                    Set<String> srcFields = desc.getSourceFields();
                    if (srcFields.contains(curElem)) {
                        try {
                            desc.parseValueIntoRecord(curRecord, curData.trim());
                        } catch (ParseException exc) {
                            System.err.println("Problem parsing " + curData + " into field for " + desc.getFieldName());
                        }
                    }
                }
            } else {
                curRecord.addTextFieldValue(curElem, curData);
            }
        }
        curElem = null;
        curData = null;
    }

    /**
     * Once the record has been loaded, this method is called to fire off new
     * record. Before the record is sent to any listeners, any defaults for the
     * fields are set.
     */
    private void finishRecord() {
        for (FieldDesc<?> desc : getTemplate().getFieldDescriptions().values()) {
            if (curRecord.getFieldAsString(desc.getFieldName()) == null) {
                desc.storeDefaultIntoRecord(curRecord);
            }
        }
        fireRecordReceived(curRecord);
        curElem = null;
        curData = null;
        curRecord = null;
    }

    /**
     * Get the set of tags which this stream did not parse. This method may be
     * used to identify parts of the stream transformation which are not
     * translated properly
     * 
     * @return the unknown tags
     */
    @Override
    public Set<String> getUnknownFields() {
        return unknowns;
    }

    /**
     * Set any initialization parameters required by this stream reader.
     * 
     * @param parameters a map of initialization parameters
     */
    @Override
    public void setInitParameters(Map<String, String> parameters) {
        path = parameters.get("dataFile");
    }

    /**
     * Set the data template for this DataRecordStream. The template is used to
     * determine how the DataRecord should be configured and generated from the
     * underlying data source. If the template is not set, the DataRecordStream
     * should still generate DataRecord objects.
     * 
     * @param dataTemplate the template to set
     */
    @Override
    public void setTemplate(DataTemplate dataTemplate) {
        super.setTemplate(dataTemplate);
        recTag = dataTemplate.getRecordIdentifier();
        HashMap<String, FieldDesc<?>> descs = dataTemplate.getFieldDescriptions();
        for (FieldDesc<?> desc : descs.values()) {
            templateFields.addAll(desc.getSourceFields());
        }
        templateFields.addAll(dataTemplate.getNameKeyList());
    }

    /**
     * Start parting the file or directory.
     * 
     * @throws IOException if there is a problem accessing the data
     * @throws SAXException if the file(s) contain invalid XML
     */
    public void startParse() throws IOException, SAXException {
        File file = new File(path);
        if (file.exists() && !file.isDirectory()) {
            if (path.endsWith(".gz")) {
                GZIPInputStream in = new GZIPInputStream(new FileInputStream(file));
                parser.parse(in, recHandle);
            } else {
                parser.parse(file, recHandle);
            }
        }
    }

    /**
     * Start parsing the input stream. This method ignores any path defined,
     * and parses the content of the input stream into DataRecord objects.
     * 
     * @param in the input stream
     * @throws IOException if there is a problem accessing the data
     * @throws SAXException if the stream contains invalid XML
     */
    public void startParse(InputStream in) throws SAXException, IOException {
        parser.parse(in, recHandle);
    }
}
