package com.ms.xml.dso;

import java.util.*;
import java.applet.*;
import java.awt.*;
import java.net.*;
import java.io.*;
import com.ms.xml.parser.*;
import com.ms.xml.om.*;
import com.ms.xml.util.Name;
import com.ms.xml.util.XMLOutputStream;
import com.ms.xml.util.StringInputStream;

/**
 * An XMLDSO is an applet that can be used in an APPLET tag to
 * load XML files and provide that data to for data binding.
 * See IE 4.0 documentation for more information on data binding.
 *
 * @version 1.0, 8/14/97
 * @see Document
 * @see Element
 *
 * Joachim Feise (dav-exp@ics.uci.edu), 25 March 1999:
 * Commented out all COM- and JavaScript-related stuff to be able to compile the code
 * with JDK 1.2
 * Also fixed parameter count mismatch in calls to ElementFactory.createElement()
 */
public class XMLDSO extends Applet {

    /**
     * Construct a new XMLDSO object with an empty document.
     */
    public XMLDSO() {
        document = new Document();
        inlineXML = false;
        loaded = false;
    }

    /**
     * The init method looks for a URL PARAM and loads this
     * url if specified.  Otherwise it looks for inline XML
     * from inside the APPLET tag that created this XMLDSO
     * object.  It does this using the ID parameter of the
     * APPLET tag to lookup the actual applet object in
     * the page, using JSObject, an then getting the altHtml
     * member of this object.   Note: it is ok to not have
     * a URL or any inline XML.
     */
    public void init() {
        super.init();
        String arg = getParameter("URL");
        if (arg != null && arg.length() > 0) {
            load(arg);
        } else {
            loaded = true;
            arg = getParameter("ID");
            if (arg != null && arg.length() > 0) {
                Object appl = null;
                if (appl != null) {
                } else {
                    setError("Error finding <APPLET> with ID=" + arg);
                }
            } else {
                setError("No PARAM with NAME=URL was found, " + "so perhaps you specified the XML inside the APPET tag " + "but in order for this to work you must also specify " + "<PARAM NAME=\"APPLET\" VALUE=\"xmldso\">" + " where the value matches the ID of your APPLET tag.");
            }
        }
        updateSchema();
    }

    /**
     * This method is called whenever the document is changed,
     * that is, from the load and setroot methods.  You can
     * also call this method if you have manually changed the
     * document or the SCHEMA <PARAM>.
     */
    public void updateSchema() {
        schema = new Document();
        String arg = getParameter("SCHEMA");
        if (arg != null && arg.length() > 0) {
            schema = parseXML(arg);
            schemaRoot = schema.getRoot();
        } else {
            if (document != null && document.getRoot() != null) {
                Element root = document.getRoot();
                SchemaNode n = new SchemaNode(schema, schema, root.getTagName());
                generateSchema(root, n);
                ElementEnumeration iter = new ElementEnumeration(schema.getRoot(), XMLRowsetProvider.nameROWSET, Element.ELEMENT);
                schemaRoot = (Element) iter.nextElement();
            }
        }
        notifyListeners();
    }

    public Document parseXML(String xml) {
        Document _document = new Document();
        if (xml == null) return _document;
        try {
            _document.load(new StringInputStream(xml));
        } catch (Exception e) {
            setError("Caught exception parsing given XML.  " + e.toString());
        }
        return _document;
    }

    /**
     * Parse and Process the given update gram
     */
    public void handleUpdateString(String xml) {
        Document d = parseXML(xml);
        handleUpdate(d.getRoot());
    }

    /**
     * Process the given update gram
     */
    public void handleUpdate(Element root) {
        if (document == null) {
            document = new Document();
            document.addChild(root, null);
            updateSchema();
            return;
        }
        try {
            Element docRoot = document.getRoot();
            for (ElementEnumeration en = new ElementEnumeration(root); en.hasMoreElements(); ) {
                Element action = (Element) en.nextElement();
                updateTree(docRoot, action);
            }
        } catch (Exception e) {
            setError("HandleUpdate error: " + e.toString());
        }
    }

    boolean updateTree(Element node1, Element node2) {
        int type = node2.getType();
        if (type == Element.COMMENT || type == Element.PI || type == Element.WHITESPACE) return true;
        if (type == Element.PCDATA || type == Element.ENTITYREF) {
            if (node1.getText().equals(node2.getText())) return true; else return false;
        }
        String action = (String) node2.getAttribute("UPDATE-ACTION");
        if (action != null && (action.equalsIgnoreCase("APPEND") || action.equalsIgnoreCase("INSERT"))) {
            {
                node2.removeAttribute("UPDATE-ACTION");
                boolean append = action.equalsIgnoreCase("APPEND");
                if (append) node1.addChild(node2, null); else {
                    node1.addChild(node2, 0, 0);
                }
                notifyNewRow(node1, append);
                return true;
            }
        }
        int num = node2.numElements();
        if (num > 0) {
            int row = 0;
            for (ElementEnumeration en = new ElementEnumeration(node1, node2.getTagName(), Element.ELEMENT); en.hasMoreElements(); ) {
                row++;
                Element element = (Element) en.nextElement();
                Vector actions = new Vector();
                boolean match = false;
                for (ElementEnumeration en2 = new ElementEnumeration(node2); en2.hasMoreElements(); ) {
                    Element pattern = (Element) en2.nextElement();
                    String cmd = (String) pattern.getAttribute("UPDATE-ACTION");
                    if (cmd == null || cmd.equalsIgnoreCase("APPEND") || cmd.equalsIgnoreCase("INSERT") || cmd.equalsIgnoreCase("REMOVE")) {
                        match = updateTree(element, pattern);
                        if (!match) break;
                    } else {
                        actions.addElement(pattern);
                    }
                }
                if (!match) continue;
                if (action != null && action.equalsIgnoreCase("REMOVE")) {
                    node1.removeChild(element);
                    notifyRemovedRow(node1, row);
                    return true;
                }
                for (Enumeration an = actions.elements(); an.hasMoreElements(); ) {
                    Element actElement = (Element) an.nextElement();
                    String cmd = (String) actElement.getAttribute("UPDATE-ACTION");
                    actElement.removeAttribute("UPDATE-ACTION");
                    if (cmd.equalsIgnoreCase("REPLACE")) {
                        ElementEnumeration ee = new ElementEnumeration(element, actElement.getTagName(), Element.ELEMENT);
                        Element oldChild = (Element) ee.nextElement();
                        element.addChild(actElement, oldChild);
                        if (oldChild != null) element.removeChild(oldChild);
                        element.setParent(node1);
                        notifyCellChanged(row, element, actElement);
                    }
                }
                return true;
            }
        }
        return false;
    }

    int getColumn(Element _schema, Element element) {
        int col = 0;
        String tagname = element.getTagName().toString();
        for (Enumeration en = _schema.getElements(); en.hasMoreElements(); ) {
            col++;
            Element e = (Element) en.nextElement();
            String n1 = e.getAttribute("NAME").toString();
            if (n1.equals(tagname)) return col;
        }
        setError(tagname + " not found in schema.");
        return 0;
    }

    void removeProvider(XMLRowsetProvider p) {
        if (p != null) providers.removeElement(p);
    }

    void addProvider(XMLRowsetProvider p) {
        if (p != null) {
            providers.addElement(p);
        }
    }

    /**
     * Notify all DSO's that are bound to this row that the given cell has
     * been modified.
     */
    void notifyCellChanged(int row, Element rowElement, Element modifiedElement) {
        long count = 0;
        for (Enumeration en = providers.elements(); en.hasMoreElements(); ) {
            XMLRowsetProvider provider = (XMLRowsetProvider) en.nextElement();
            Element node = rowElement;
            while (node != null) {
                XMLRowsetProvider child = provider.findChildProvider(node);
                if (child != null) {
                    count++;
                }
                node = node.getParent();
            }
        }
    }

    void notifyNewRow(Element node, boolean append) {
        for (Enumeration en = providers.elements(); en.hasMoreElements(); ) {
            XMLRowsetProvider provider = (XMLRowsetProvider) en.nextElement();
            XMLRowsetProvider child = provider.findChildProvider(node);
            if (child != null) {
            }
        }
    }

    void notifyRemovedRow(Element node, int row) {
        for (Enumeration en = providers.elements(); en.hasMoreElements(); ) {
            XMLRowsetProvider provider = (XMLRowsetProvider) en.nextElement();
            XMLRowsetProvider child = provider.findChildProvider(node);
            if (child != null) {
            }
        }
    }

    /**
     * Notify current DataSouceListener that data has changed.
     */
    public void notifyListeners() {
        if (myDSL != null) {
            try {
            } catch (Exception e) {
                setError("Error notifying data members changed: " + e.toString());
            }
        }
    }

    /**
     * Return the loaded document.
     */
    public Object getDocument() {
        return document;
    }

    /**
     * This method is called to set the root of the document
     * for this XMLDSO object.  This is useful when you want to
     * link multiple DSO's together.
     */
    public void setRoot(Element e) {
        document = new Document();
        document.addChild(e, null);
        updateSchema();
    }

    public void clear() {
        document = new Document();
        updateSchema();
    }

    /**
     * Reload the document using the given URL.  Relative URL's
     * are resolved using the HTML document as a base URL.
     */
    public void load(String arg) {
        URL base = getDocumentBase();
        loaded = false;
        try {
            if (base == null) {
                url = new URL(arg);
            } else {
                url = new URL(base, arg);
            }
            document.load(url.toString());
            loaded = true;
        } catch (Exception e) {
            setError("Error loading XML document '" + arg + "'.  " + e.toString());
        }
        if (loaded && schema != null) {
            updateSchema();
        }
    }

    public void asyncLoad(String arg, String callback) {
        document = null;
        try {
            URL _url = new URL(getDocumentBase(), arg);
            document = new Document();
            Object win = null;
            XMLParserThread t = new XMLParserThread(_url, win, callback);
            t.start();
        } catch (Exception e) {
            setError("Error loading document '" + arg + "'.  " + e.toString());
        }
    }

    /**
     * Return the XML for the loaded document as a big string.
     * Pass in the formatting style. 0=default, 1=pretty, 2=compact.
     */
    public Object getXML(int style) {
        if (document != null) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                XMLOutputStream s = new XMLOutputStream(out);
                s.setOutputStyle(style);
                document.save(s);
            } catch (Exception e) {
                setError(e.toString());
            }
            return out.toString();
        }
        return null;
    }

    /**
     * Return string containing last error encountered by the XMLDSO.
     */
    public String getError() {
        return error;
    }

    /**
     * Save the schema to the given file.
     */
    public void saveSchema(String filename) {
        if (schema != null) {
            try {
                FileOutputStream out = new FileOutputStream(filename);
                schema.save(new XMLOutputStream(out));
            } catch (Exception e) {
                setError(e.toString());
            }
        }
    }

    /**
     * Return the schema as a string.
     */
    public Object getSchema(int style) {
        if (schema != null) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                XMLOutputStream xout = new XMLOutputStream(out);
                xout.setOutputStyle(style);
                schema.save(xout);
            } catch (Exception e) {
                setError(e.toString());
            }
            return out.toString();
        }
        return null;
    }

    /**
     * Save the XML document to the given file.
     */
    public void save(String filename) {
        if (document != null) {
            try {
                FileOutputStream out = new FileOutputStream(filename);
                document.save(document.createOutputStream(out));
            } catch (Exception e) {
                setError(e.toString());
            }
        }
    }

    private void setError(String e) {
        error = e;
        repaint();
    }

    void generateSchema(Element source, SchemaNode n) {
        for (ElementEnumeration en = new ElementEnumeration(source); en.hasMoreElements(); ) {
            Element e = (Element) en.nextElement();
            int t = e.getType();
            Name name = e.getTagName();
            if (t == Element.ELEMENT) {
                SchemaNode sn = n.setRow(name);
                generateSchema(e, sn);
            }
        }
        if (n.e == null) {
            n.createElement(false);
        }
    }

    /**
     * When the APPLET containing this XMLDSO is given a non-zero bounds
     * this paint methods displays diagnostic information that helps the
     * page author debug the page.
     */
    public void paint(Graphics g) {
        Dimension d = getSize();
        if (d.width > 0 && d.height > 0) {
            if (error == null) {
                g.setColor(Color.green);
            } else {
                g.setColor(Color.red);
            }
            g.fillRect(0, 0, d.width, d.height);
            String text = error;
            if (text == null) {
                if (url != null) {
                    text = "Successfully loaded XML from \"" + url.toString() + "\"";
                } else if (inlineXML) {
                    text = "Successfully loaded inline XML";
                } else if (document.getRoot() != null) {
                    text = "Successfully loaded document.";
                } else {
                    text = "Empty";
                }
            }
            g.setColor(Color.black);
            drawText(g, text, 5, 5, d.width - 10, true, 0);
        }
    }

    /**
     * Draw a text string within the given bounds.
     */
    private int drawText(Graphics g, String text, int x, int y, int max, boolean skipWhiteSpace, int length) {
        if (text == null || text.length() == 0) return y;
        if (max < 5) return y;
        int i = 0;
        int len;
        int w = 0;
        if (length == 0) len = text.length(); else len = length;
        while (i < len && skipWhiteSpace && isWhiteSpace(text.charAt(i))) i++;
        FontMetrics fm = g.getFontMetrics();
        int j = i;
        int k = i;
        while (i < len) {
            char ch = text.charAt(i);
            int cw = fm.charWidth(ch);
            w += cw;
            if (w > max || ch == '\n') {
                if (ch == '\n' && text.charAt(i - 1) == 0x0D) j = i - 1; else if (k == j) j = i;
                String sub = text.substring(k, j);
                g.drawString(sub, x, y + fm.getMaxAscent());
                y += fm.getHeight();
                if (ch == '\n' && text.charAt(i - 1) == 0x0D) j = i + 1;
                while (skipWhiteSpace && j < len && isWhiteSpace(text.charAt(j))) j++;
                i = j;
                k = j;
                w = 0;
            } else {
                if (skipWhiteSpace && isWhiteSpace(ch)) {
                    j = i;
                }
                i++;
            }
        }
        String remaining = text.substring(k);
        g.drawString(remaining, x, y + fm.getMaxAscent());
        return y;
    }

    private boolean isWhiteSpace(char ch) {
        return Character.isWhitespace(ch) || ch == 13;
    }

    private Object myDSL;

    private Document document;

    private Document schema;

    private Element schemaRoot;

    private String error;

    private URL url;

    private boolean loaded;

    private boolean inlineXML;

    static Vector providers = new Vector();
}

class SchemaNode {

    Element e;

    Hashtable rows;

    Name name;

    Element parent;

    ElementFactory factory;

    SchemaNode(Element parent, ElementFactory factory, Name name) {
        this.parent = parent;
        this.name = name;
        this.factory = factory;
    }

    void createElement(boolean rowset) {
        if (rowset) {
            e = factory.createElement(null, Element.ELEMENT, XMLRowsetProvider.nameROWSET, null);
        } else {
            e = factory.createElement(null, Element.ELEMENT, XMLRowsetProvider.nameCOLUMN, null);
        }
        parent.addChild(e, null);
        e.setAttribute(XMLRowsetProvider.nameNAME, name.toString());
    }

    SchemaNode setRow(Name name) {
        if (e == null) {
            createElement(true);
        } else if (e.getTagName() != XMLRowsetProvider.nameROWSET) {
            parent.removeChild(e);
            createElement(true);
        }
        SchemaNode sn = getRow(name);
        if (sn == null) {
            sn = new SchemaNode(e, factory, name);
            addRow(name, sn);
        }
        return sn;
    }

    void addRow(Name _name, SchemaNode n) {
        if (rows == null) rows = new Hashtable(13);
        rows.put(_name, n);
    }

    SchemaNode getRow(Name n) {
        if (rows == null) return null;
        return (SchemaNode) rows.get(n);
    }
}

class XMLRowsetProvider {

    public XMLRowsetProvider(Element e, Element schema, ElementFactory f, XMLRowsetProvider parent) {
        root = e;
        this.schema = schema;
        this.parent = parent;
        this.factory = f;
        rowset = Name.create((String) schema.getAttribute(nameNAME));
        resetIterator();
    }

    public Element getSchema() {
        return schema;
    }

    void resetIterator() {
        iter = new ElementEnumeration(root, rowset, Element.ELEMENT);
        row = (Element) iter.nextElement();
        rowindex = 0;
    }

    public int getRowCount() {
        int result = 0;
        iter.reset();
        while (iter.nextElement() != null) result++;
        iter.reset();
        row = (Element) iter.nextElement();
        rowindex = 0;
        return result;
    }

    public int getEstimatedRows() {
        return getRowCount();
    }

    public int getColumnCount() {
        int columns = schema.getChildren().getLength();
        return columns;
    }

    public int getRWStatus(int iRow, int iColumn) {
        return 1;
    }

    public int find(int iStartRow, int iColumn, Object varSearchVal, int findFlags, int compType) {
        return 0;
    }

    public int deleteRows(int iRow, int cRows) {
        int result = 0;
        for (int i = iRow; i < iRow + cRows; i++) {
            Element _row = getRow(iRow);
            if (_row != null) {
                result++;
                root.removeChild(_row);
                removeChildProvider(_row);
            }
        }
        resetIterator();
        return result;
    }

    public int insertRows(int iRow, int cRows) {
        Name name = Name.create(schema.getAttribute("NAME").toString());
        for (int i = iRow; i < iRow + cRows; i++) {
            Element newRow = factory.createElement(null, Element.ELEMENT, name, null);
            Element previousRow = root.getChildren().getChild(i);
            root.addChild(newRow, previousRow);
        }
        resetIterator();
        return cRows;
    }

    public Object getVariant(int iRow, int iColumn, int formatType) {
        Object retVal = null;
        if (iRow == 0) {
            if (iColumn <= getColumnCount()) {
                Element e = schema.getChildren().getChild(iColumn - 1);
                String name = (String) e.getAttribute(nameNAME);
                if (name != null) {
                    if (e.getTagName() != nameCOLUMN) {
                        retVal = "^" + name + "^";
                    } else {
                        retVal = name;
                    }
                }
            }
        } else {
            getRow(iRow);
            retVal = getColumn(row, iColumn - 1);
        }
        return retVal;
    }

    Element getRow(int iRow) {
        if (rowindex != iRow - 1) {
            if (rowindex == iRow - 2) {
                rowindex++;
                row = (Element) iter.nextElement();
            } else {
                iter.reset();
                row = (Element) iter.nextElement();
                rowindex = 0;
                for (int i = 0; i < iRow - 1; i++) {
                    row = (Element) iter.nextElement();
                    rowindex++;
                }
            }
        }
        return row;
    }

    public Object getColumn(Element _row, int col) {
        Element se = schema.getChildren().getChild(col);
        Name name = Name.create((String) se.getAttribute(nameNAME));
        if (se.getTagName() == nameCOLUMN) {
            Element child = findChild(_row, name);
            if (child != null) {
                String text = child.getText();
                return text;
            }
            return null;
        } else {
            XMLRowsetProvider value = findChildProvider(_row);
            if (value == null) {
                value = new XMLRowsetProvider(_row, se, factory, this);
                addChildProvider(value);
            }
            return value;
        }
    }

    public XMLRowsetProvider findChildProvider(Element _row) {
        if (childProviders != null) {
            for (Enumeration en = childProviders.elements(); en.hasMoreElements(); ) {
                XMLRowsetProvider child = (XMLRowsetProvider) en.nextElement();
                if (child.root == _row) {
                    return child;
                }
            }
        }
        return null;
    }

    void addChildProvider(XMLRowsetProvider child) {
        if (childProviders == null) childProviders = new Vector();
        childProviders.addElement(child);
    }

    void removeChildProvider(Element _row) {
        XMLRowsetProvider value = findChildProvider(_row);
        if (value != null) {
            childProviders.removeElement(value);
        }
    }

    /**
     * Recurrsively search given row for first child or grand-child
     * node with matching tag name.
     */
    public Element findChild(Element _row, Name tag) {
        for (ElementEnumeration en = new ElementEnumeration(_row, null, Element.ELEMENT); en.hasMoreElements(); ) {
            Element child = (Element) en.nextElement();
            if (child.getType() == Element.ELEMENT && child.getTagName() == tag) {
                return child;
            } else if (child.numElements() > 0) {
                child = findChild(child, tag);
                if (child != null) return child;
            }
        }
        return null;
    }

    public void setVariant(int iRow, int iColumn, int formatType, Object var) {
        getRow(iRow);
        if (row == null) return;
        Element se = schema.getChildren().getChild(iColumn - 1);
        if (se.getTagName() == nameCOLUMN) {
            String attr = (String) se.getAttribute(nameNAME);
            if (attr != null) {
                Name name = Name.create(attr);
                Element child = findChild(row, name);
                if (child == null) {
                    child = factory.createElement(null, Element.ELEMENT, name, null);
                    row.addChild(child, null);
                }
                if (child != null) {
                    if (child.numElements() == 0) {
                        child.addChild(factory.createElement(null, Element.PCDATA, null, null), null);
                    }
                    if (child.numElements() == 1 && child.getChild(0).getType() == Element.PCDATA) {
                        Element pcdata = child.getChild(0);
                        String text = (String) var;
                        pcdata.setText(text);
                    }
                }
            }
        }
    }

    public String getLocale() {
        return "";
    }

    public int isAsync() {
        return 0;
    }

    public void stopTransfer() {
    }

    Element root;

    Element schema;

    Element row;

    ElementEnumeration iter;

    int rowindex;

    Name rowset;

    XMLRowsetProvider parent;

    Object listener = null;

    ElementFactory factory;

    Vector childProviders;

    static Name nameCOLUMN = Name.create("COLUMN");

    static Name nameROWSET = Name.create("ROWSET");

    static Name nameNAME = Name.create("NAME");

    static Name nameATTRIBUTE = Name.create("ATTRIBUTE");

    static Name nameVALUE = Name.create("VALUE");
}

class XMLParserThread extends Thread {

    URL url;

    Object win;

    String callback;

    XMLParserThread(URL url, Object win, String callback) {
        this.url = url;
        this.win = win;
        this.callback = callback;
    }

    public void run() {
        Object args[] = new Object[2];
        try {
            Document document = new Document();
            document.load(url);
            args[0] = "ok";
            args[1] = document;
        } catch (ParseException e) {
            args[0] = e.toString();
            args[1] = null;
        }
    }
}
