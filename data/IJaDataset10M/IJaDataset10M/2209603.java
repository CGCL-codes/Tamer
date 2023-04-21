package gov.lanl.translate;

import gov.lanl.Utility.*;
import gov.lanl.Web.COAS;
import gov.lanl.Web.Content;
import org.omg.DsObservationAccess.*;
import org.apache.xml.serialize.OutputFormat;
import nu.xom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.util.*;

/**
 * Assemble comma delimited table into column arrays
 * Assumes labels are in first row and values are in subsequent rows
 * Also supports conversion of file into XML Document row by row with configurable XML types, cf. toDom()
 * This is useful for importing an Excel spread sheet into a XML DOM tree, which can then be automatically
 *   processed
 */
public class CSVTable {

    /**
     *   Flag for reading CSV table indicating that the first line has the variable names
     */
    private boolean first;

    /**
     * XML Document handling classes
     */
    private static DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();

    private static DocumentBuilder docBuilder = null;

    private static Document document = null;

    private static gov.lanl.Web.DOMErrors domErrors = new gov.lanl.Web.DOMErrors();

    private String xsl;

    private static org.apache.log4j.Logger cat = org.apache.log4j.Logger.getLogger(CSVTable.class.getName());

    /**
     * Labels are in the first row, hold them in columns
     */
    private Vector columns;

    /**
     * Hashtable holds vectors attached to each column
     */
    private Hashtable cols = new Hashtable();

    /**
     * Hashtable of fields for type specification
     */
    private static Hashtable types = new Hashtable();

    /**
     * Default Code value
     */
    private static String Default = "OBS";

    /**
     * Properties file containing definitions of XML types
     */
    private static ConfigProperties props;

    /**
     * Default properties file (with .properties suffix)
     */
    private String propFile = "/csv";

    private BufferedReader in;

    private int lineNo = 0;

    /**
     * Default constructor
     */
    public CSVTable() {
    }

    /**
     * Construct CVSTable from file containing comma delimited entries specifying the properties file
     */
    public CSVTable(String file, String propFile) {
        if (propFile != null) this.propFile = propFile;
        setProperties(this.propFile);
        readFile(file);
    }

    /**
     * Constructor to use default properties file
     */
    public CSVTable(String file) {
        this(file, null);
    }

    /**
     * set the line to begin parsing at.
     * @param lineNo
     */
    public void setLineNo(int lineNo) {
        this.lineNo = lineNo;
    }

    /** get the current lnineNo red
      *
      */
    public int getLineNo() {
        return lineNo;
    }

    /**
     * set the property file for the table
     */
    public void setProperties(String propFile) {
        this.propFile = propFile;
        try {
            props = new ConfigProperties(this.propFile);
        } catch (Exception e) {
            cat.error(e);
        }
        init();
    }

    /**
     * Open file and parse it
     */
    public void readFile(String file) {
        try {
            InputStream is = gov.lanl.Utility.IOHelper.getInputStream(file);
            in = new BufferedReader(new InputStreamReader(is));
            String line = null;
            first = true;
            while (((line = in.readLine()) != null)) {
                quotedParseLine(line);
            }
        } catch (IOException e) {
            cat.error(e, e);
        }
    }

    /**
     *  Incrementally read file file
     * @param file to read
     * @param lines number of lines to read at this time
     * @return boolean if there are more lines to read.
     */
    public boolean readFile(String file, int lines) {
        try {
            if (in == null) {
                InputStream is = gov.lanl.Utility.IOHelper.getInputStream(file);
                in = new BufferedReader(new InputStreamReader(is));
                first = true;
            }
            String line = null;
            int end = lineNo + lines;
            cols = new Hashtable();
            if (columns != null) {
                for (int j = 0; j < columns.size(); j++) {
                    cols.put(columns.elementAt(j), new Vector());
                }
            }
            for (int i = lineNo; i < end; i++) {
                line = in.readLine();
                if (line != null) parseLine(line);
                lineNo++;
            }
            cat.debug("readFile: " + file + "," + lines + " " + lineNo);
            cat.debug("readFile: columns=" + cols.size());
            if (line != null) return true;
        } catch (IOException io) {
            cat.error(io, io);
        }
        return false;
    }

    /**
     * initialize types definitions.  The type may control the XSL style sheet for further translation
     */
    private void init() {
        String typ = props.getProperty("Types", "");
        cat.debug("init: types=" + typ);
        StringTokenizer st = new StringTokenizer(typ, ",");
        String[] typs = new String[st.countTokens()];
        int i = 0;
        while (st.hasMoreTokens()) {
            typs[i++] = st.nextToken();
        }
        if (typs.length > 1) Default = typs[typs.length - 1];
        String value = "start";
        for (int k = 0; k < typs.length; k++) {
            i = 1;
            value = "start";
            while (value != null && !value.equals("")) {
                value = props.getProperty(typs[k] + "." + i++);
                cat.debug(typs[k] + " has value " + value);
                ;
                if (value != null && !value.equals("")) types.put(value, typs[k]);
            }
        }
    }

    /**
     *  Main program to test the transformations
     */
    public static void main(String[] argv) {
        props = new ConfigProperties();
        props.setProperties("csv.cfg", argv);
        String input = props.getProperty("input");
        String xslFile = props.getProperty("xslFile", "test.xsl");
        CSVTable csvTable = new CSVTable();
        csvTable.setXsl(xslFile);
        csvTable.setProperties("csv");
        String lineNo = props.getProperty("lineNo", "0");
        csvTable.setLineNo(Integer.parseInt(lineNo));
        COAS coas = new COAS(props.getProperty("coas", "coas"));
        int i = 0;
        boolean read = true;
        cat.debug("input: " + input);
        while (read) {
            i++;
            read = csvTable.readFile(input, 100);
            cat.debug("File: " + input + " read to line: " + lineNo + 100);
            csvTable.write("csvtest" + i + ".xml");
            Document doc = new Document(csvTable.translate());
            DomDoc2ObsData obs = new DomDoc2ObsData();
            obs.setDomDoc(doc.getRootElement());
            ObservationDataStruct[] d = obs.getObsDataSeq();
            coas.store(d, coas.getCredentials());
            Content c = new Content();
            c.setCode("ObsContainer");
            Content[] cont = new Content[d.length];
            if (d != null) {
                for (int j = 0; j < d.length; j++) {
                    cont[j] = new Content(d[j]);
                }
                c.addContent(cont);
                c.write("csvobs.xml");
            }
            csvTable.writeTranslate("csvcoas" + i + ".xml");
        }
    }

    /**
     * Read a line, parse it and put elements in appropriate container
     */
    void parseLine(String line) {
        HL7Tokenizer tmp_st = new HL7Tokenizer(line, ",");
        if (tmp_st.countTokens() == 0) return;
        int i = -1;
        if (columns == null) columns = new Vector();
        Vector row = new Vector();
        while (tmp_st.hasMoreTokens()) {
            String tmp_tok = tmp_st.nextToken();
            if (tmp_tok.startsWith("\"")) {
                StringBuffer sb = new StringBuffer(tmp_tok.substring(1));
                String s = "";
                while (tmp_st.hasMoreTokens()) {
                    String t_tok = tmp_st.nextToken();
                    sb.append(",");
                    s = t_tok;
                    if (t_tok.endsWith("\"")) s = t_tok.substring(0, t_tok.length() - 1);
                    sb.append(s);
                    if (t_tok.endsWith("\"")) break;
                }
                tmp_tok = sb.toString();
            }
            i++;
            if (first) {
                tmp_tok = tmp_tok.trim();
                columns.addElement(tmp_tok);
                cols.put(tmp_tok, new Vector());
            } else if (i < cols.size()) {
                Vector v = new Vector();
                v.addElement(columns.elementAt(i));
                v.addElement(tmp_tok.trim());
                row.addElement(v);
            }
        }
        addRow(row);
        first = false;
    }

    /**
         * Read a line, parse it and put elements in appropriate container
         */
    void quotedParseLine(String line) {
        HL7Tokenizer tmp_st = new HL7Tokenizer(line, "\"");
        if (tmp_st.countTokens() == 0) return;
        int i = -1;
        if (columns == null) columns = new Vector();
        Vector row = new Vector();
        while (tmp_st.hasMoreTokens()) {
            String tmp_tok = tmp_st.nextToken();
            if (!tmp_tok.equals(",")) {
                i++;
                if (first) {
                    tmp_tok = tmp_tok.trim();
                    columns.addElement(tmp_tok);
                    cols.put(tmp_tok, new Vector());
                } else if (i < cols.size()) {
                    Vector v = new Vector();
                    v.addElement(columns.elementAt(i));
                    v.addElement(tmp_tok.trim());
                    row.addElement(v);
                }
            }
        }
        addRow(row);
        first = false;
    }

    /**
     *  Add column to table for specified label
     * @param label to put the column in
     * @param column vector to add (may be empty Vector)
     */
    public void addKey(String label, Vector column) {
        if (columns == null) columns = new Vector();
        Vector v = (Vector) cols.get(label);
        if (v != null) {
            cat.error("addKey:  duplicate column name: " + label + " insert failed");
            return;
        }
        columns.addElement(label);
        cols.put(label, column);
    }

    /**
     * add a row to the table
     *  the input vector must have the same number of elements (or less) as columns and the contents must be
     *  Strings.
     * @param row vector to be added.
     */
    public void addRow(Vector row) {
        if (columns == null) columns = new Vector();
        if (row.size() <= columns.size()) {
            for (int i = 0; i < row.size(); i++) {
                Vector v = (Vector) cols.get(columns.elementAt(i));
                Vector r = (Vector) row.elementAt(i);
                int j = 0;
                if (r.size() == 2) j = 1;
                String value = (String) r.elementAt(j);
                v.addElement(value);
                cols.put(columns.elementAt(i), v);
            }
        }
    }

    /**
     * get Labels
     * @return String[]
     */
    public String[] getLabels() {
        if (columns == null) return new String[0];
        String[] labels = new String[columns.size()];
        columns.copyInto(labels);
        return labels;
    }

    /**
     * getValues for the specified column
     * @param label to specify which values
     * @return String[] of values
     */
    public String[] getValues(String label) {
        Vector v = (Vector) cols.get(label);
        String[] s = new String[v.size()];
        v.copyInto(s);
        return s;
    }

    /**
     *  print out String representation of data
     * @return String representation
     */
    public String createString() {
        String[] labels = getLabels();
        StringBuffer buff = new StringBuffer();
        for (int i = 0; i < labels.length; i++) {
            buff.append("\n\"" + labels[i] + "\": ");
            String[] values = getValues(labels[i]);
            for (int k = 0; k < values.length; k++) buff.append(values[k] + "|");
        }
        return buff.toString();
    }

    /** initializer for DOM document container
     */
    private static void initDOM() {
        try {
            if (docBuilder == null) {
                docBuilderFactory.setValidating(true);
                docBuilder = docBuilderFactory.newDocumentBuilder();
                docBuilder.setErrorHandler(domErrors);
            }
            if (document == null) {
                document = new Document(new Element("CSV"));
            }
        } catch (Exception e) {
            cat.error(e);
        }
    }

    /**
     *  Return DOM Document of CSV file according to Sample:
     *
     *   <Set>
     *		<Item Code="Event">
     *			<Data Code="Sex" T="PID">M</Data>
     *			<Data Code="Smear" T="OBS">Inoculated<Data>
     *		</Item>
     *	 </Set>
     *  An <Item> is a row, and a <Data> element is a column
     *  The T attribute is the "type" defined in the csv.properties file.
     */
    public Document toDom() {
        if (document == null) initDOM();
        Element elem = new Element("Set");
        String[] labels = getLabels();
        if (cols.get(labels[0]) == null) return document;
        int n = ((Vector) cols.get(labels[0])).size();
        for (int i = 0; i < n; i++) {
            Element el = new Element("Item");
            el.addAttribute(new Attribute("Code", "Event"));
            for (int j = 0; j < labels.length; j++) {
                String label = labels[j];
                Vector v = (Vector) cols.get(label);
                String t = "";
                if (v.size() > i) t = (String) ((Vector) cols.get(label)).elementAt(i);
                if (!t.equals("")) el.appendChild(getNode(label, t));
            }
            elem.appendChild(el);
        }
        Element e = document.getRootElement();
        e.appendChild(elem);
        domErrors.sysOutErrors();
        Document doc = document;
        document = null;
        return doc;
    }

    /**
     *  Return node Element
     * @param label
     * @param value
     * @return Node
     */
    private Node getNode(String label, String value) {
        Element e = new Element("Data");
        e.addAttribute(new Attribute("Code", label));
        String t = (String) types.get(label);
        if (t == null) t = Default;
        e.addAttribute(new Attribute("T", t));
        e.appendChild(new Text(value));
        return e;
    }

    /**
     *  Serialize Content to a file as XML
     * @param file
     */
    public void write(String file) {
        try {
            OutputFormat format = new OutputFormat("xml", "UTF-8", true);
            FileOutputStream fileOut = new FileOutputStream(file);
            Serializer serial = new Serializer(fileOut);
            serial.write(toDom());
            cat.debug(file + " written");
            fileOut.close();
        } catch (Exception e) {
            cat.error("serializer failed: ", e);
        }
    }

    /**
     *  write out the translated file as XML
     */
    public void writeTranslate(String file) {
        try {
            FileOutputStream fileOut = new FileOutputStream(file);
            Serializer serial = new Serializer(fileOut);
            serial.write(new Document(translate()));
            cat.debug(file + " written");
            fileOut.close();
        } catch (Exception e) {
            cat.error("serializer failed: ", e);
        }
    }

    /**
     * Serialize Content to String
     */
    public String toString() {
        if (xsl != null && !xsl.equals("")) return translate().toXML(); else return toDom().toXML();
    }

    /**
     * Return the rows of data from the CSV file in a Vector
     * @return Vector of rows, each row element is a Vector of name, value pairs.
     */
    public Vector toRows() {
        String[] labels = getLabels();
        Vector rows = new Vector();
        if (labels.length == 0) return rows;
        int n = ((Vector) cols.get(labels[0])).size();
        for (int i = 0; i < n; i++) {
            Vector row = new Vector();
            for (int j = 0; j < labels.length; j++) {
                int k = ((Vector) cols.get(labels[j])).size();
                if (i >= k) cat.debug(Integer.toString(j) + "," + Integer.toString(k) + "," + Integer.toString(n) + "," + Integer.toString(i));
                Vector v = new Vector();
                v.addElement(labels[j]);
                v.addElement(((Vector) cols.get(labels[j])).elementAt(i));
                row.addElement(v);
            }
            rows.addElement(row);
        }
        return rows;
    }

    /**
     * Apply style sheet to translate from generic DOM document to COAS DOM document
     * @return   the translated Node
     */
    public Element translate() {
        if ((xsl == null) || (xsl.equals(""))) {
            cat.error("No XSL style sheet defined");
            return toDom().getRootElement();
        }
        return XSLTransform.applyXSL(toDom(), getXsl(), new String[0]);
    }

    /**
     * Set the XSL file for translation
     *
     *
     * @param xslfile
     *
     * @see
     */
    public void setXsl(String xslfile) {
        xsl = xslfile;
    }

    /**
     * get the Xsl file for translation
     *
     *
     * @return
     *
     * @see
     */
    public String getXsl() {
        return xsl;
    }
}
