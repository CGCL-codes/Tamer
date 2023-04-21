package net.sourceforge.jsxe.dom;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Notation;
import org.w3c.dom.ls.LSSerializer;
import org.w3c.dom.ls.LSSerializerFilter;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMLocator;
import org.w3c.dom.DOMError;
import org.w3c.dom.DOMErrorHandler;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

/**
 * An implementation of the DOM3 LSSerializer interface. This class supports
 * everything that is supported by the DOMSerializerConfiguration class. Clients
 *  can check if a feature is supported by calling canSetParameter() on the
 * appropriate DOMSerializerConfiguration object.
 *
 * @author <a href="mailto:IanLewis at member dot fsf dot org">Ian Lewis</a>
 * @version $Id: DOMSerializer.java 849 2006-04-18 21:45:20Z ian_lewis $
 */
public class DOMSerializer implements LSSerializer {

    /**
     * Creates a default DOMSerializer using the default options.
     */
    public DOMSerializer() {
        config = new DOMSerializerConfiguration();
        m_newLine = System.getProperty("line.separator");
    }

    /**
     * Creates a DOMSerializer that uses the configuration specified.
     * @param config The configuration to be used by this DOMSerializer object
     */
    public DOMSerializer(DOMSerializerConfiguration config) {
        this.config = config;
        m_newLine = System.getProperty("line.separator");
    }

    public DOMConfiguration getDomConfig() {
        return config;
    }

    public LSSerializerFilter getFilter() {
        return m_filter;
    }

    public String getNewLine() {
        return m_newLine;
    }

    public void setFilter(LSSerializerFilter filter) {
        m_filter = filter;
    }

    public void setNewLine(String newLine) {
        m_newLine = newLine;
    }

    public boolean write(Node nodeArg, LSOutput destination) {
        if (m_filter == null || m_filter.acceptNode(nodeArg) == 1) {
            Writer writer = destination.getCharacterStream();
            String encoding = null;
            if (writer == null) {
                OutputStream out = destination.getByteStream();
                if (out != null) {
                    try {
                        writer = new OutputStreamWriter(out, destination.getEncoding());
                        encoding = destination.getEncoding();
                    } catch (UnsupportedEncodingException uee) {
                        DefaultDOMLocator loc = new DefaultDOMLocator(nodeArg, 1, 1, 0, null);
                        try {
                            throwError(loc, "unsupported-encoding", DOMError.SEVERITY_FATAL_ERROR, uee);
                        } catch (DOMSerializerException e) {
                        }
                        return false;
                    }
                } else {
                    String id = destination.getSystemId();
                    if (id != null) {
                        try {
                            URL uri = new URL(id);
                            URLConnection con = uri.openConnection();
                            try {
                                con.setDoOutput(true);
                                con.setUseCaches(true);
                            } catch (IllegalStateException ise) {
                            }
                            con.connect();
                            writer = new OutputStreamWriter(con.getOutputStream(), destination.getEncoding());
                        } catch (MalformedURLException mue) {
                            DefaultDOMLocator loc = new DefaultDOMLocator(nodeArg, 1, 1, 0, null);
                            try {
                                throwError(loc, "bad-uri", DOMError.SEVERITY_FATAL_ERROR, mue);
                            } catch (DOMSerializerException e) {
                            }
                            return false;
                        } catch (IOException ioe) {
                            DefaultDOMLocator loc = new DefaultDOMLocator(nodeArg, 1, 1, 0, null);
                            try {
                                throwError(loc, "io-error", DOMError.SEVERITY_FATAL_ERROR, ioe);
                            } catch (DOMSerializerException e) {
                            }
                            return false;
                        }
                    } else {
                        DefaultDOMLocator loc = new DefaultDOMLocator(nodeArg, 1, 1, 0, null);
                        try {
                            throwError(loc, "no-output-specified", DOMError.SEVERITY_FATAL_ERROR, null);
                        } catch (DOMSerializerException e) {
                        }
                        return false;
                    }
                }
            }
            BufferedWriter bufWriter = new BufferedWriter(writer, IO_BUFFER_SIZE);
            try {
                serializeNode(bufWriter, nodeArg, encoding);
                bufWriter.close();
                return true;
            } catch (IOException ioe) {
                Object rawHandler = config.getParameter(DOMSerializerConfiguration.ERROR_HANDLER);
                if (rawHandler != null) {
                    DOMErrorHandler handler = (DOMErrorHandler) rawHandler;
                    DefaultDOMLocator loc = new DefaultDOMLocator(nodeArg, 1, 1, 0, null);
                    DOMSerializerError error = new DOMSerializerError(loc, ioe, DOMError.SEVERITY_FATAL_ERROR, "io-error");
                    handler.handleError(error);
                }
            } catch (DOMSerializerException dse) {
                Object rawHandler = config.getParameter(DOMSerializerConfiguration.ERROR_HANDLER);
                if (rawHandler != null) {
                    DOMErrorHandler handler = (DOMErrorHandler) rawHandler;
                    DOMError error = dse.getError();
                    handler.handleError(error);
                }
            }
        }
        return false;
    }

    public String writeToString(Node nodeArg) throws DOMException {
        StringWriter writer = new StringWriter();
        try {
            serializeNode(writer, nodeArg);
            writer.flush();
        } catch (DOMSerializerException dse) {
            throw new DOMException(DOMException.INVALID_STATE_ERR, dse.getMessage());
        }
        return writer.toString();
    }

    public boolean writeToURI(Node nodeArg, java.lang.String uri) {
        return write(nodeArg, new DOMOutput(uri, "UTF-8"));
    }

    private static final int IO_BUFFER_SIZE = 32768;

    private static class DOMSerializerError implements DOMError {

        public DOMSerializerError(DOMLocator locator, Exception e, short s, String type) {
            m_exception = e;
            m_location = locator;
            m_severity = s;
            m_type = type;
        }

        public DOMLocator getLocation() {
            return m_location;
        }

        public String getMessage() {
            return m_exception.getMessage();
        }

        public Object getRelatedData() {
            return m_location.getRelatedNode();
        }

        public Object getRelatedException() {
            return m_exception;
        }

        public short getSeverity() {
            return m_severity;
        }

        public String getType() {
            return m_type;
        }

        private Exception m_exception;

        private DOMLocator m_location;

        private short m_severity;

        private String m_type;
    }

    private void serializeNode(Writer writer, Node node) throws DOMSerializerException {
        serializeNode(writer, node, null);
    }

    /**
     * Serializes the node to the writer specified
     */
    private void serializeNode(Writer writer, Node node, String encoding) throws DOMSerializerException {
        rSerializeNode(writer, node, encoding, "", 1, 1, 0);
    }

    /**
     * Designed to be called recursively and maintain the state of the
     * serialization.
     */
    private void rSerializeNode(Writer writer, Node node, String encoding, String currentIndent, int line, int column, int offset) throws DOMSerializerException {
        boolean formatting = config.getFeature(DOMSerializerConfiguration.FORMAT_XML);
        String str = "";
        if (m_filter == null || m_filter.acceptNode(node) == 1) {
            switch(node.getNodeType()) {
                case Node.DOCUMENT_NODE:
                    if (config.getFeature(DOMSerializerConfiguration.XML_DECLARATION)) {
                        String header = "<?xml version=\"1.0\"";
                        String realEncoding = (String) config.getParameter(DOMSerializerConfiguration.XML_ENCODING);
                        if (realEncoding == null) {
                            realEncoding = encoding;
                        }
                        if (realEncoding != null) header += " encoding=\"" + realEncoding + "\"";
                        header += "?>";
                        doWrite(writer, header, node, line, column, offset);
                        offset += header.length();
                        column += header.length();
                        if (!formatting) {
                            column = 0;
                            line += 1;
                            doWrite(writer, m_newLine, node, line, column, offset);
                            offset += m_newLine.length();
                        }
                    }
                    NodeList nodes = node.getChildNodes();
                    if (nodes != null) {
                        for (int i = 0; i < nodes.getLength(); i++) {
                            rSerializeNode(writer, nodes.item(i), encoding, currentIndent, line, column, offset);
                        }
                    }
                    break;
                case Node.ELEMENT_NODE:
                    String nodeName = node.getLocalName();
                    String nodePrefix = node.getPrefix();
                    if (nodeName == null) {
                        nodeName = node.getNodeName();
                    }
                    if (formatting) {
                        column = 0;
                        str = m_newLine + currentIndent;
                        doWrite(writer, str, node, line, column, offset);
                        column += currentIndent.length();
                        offset += str.length();
                    }
                    if (config.getFeature(DOMSerializerConfiguration.NAMESPACES) && nodePrefix != null) {
                        str = "<" + nodePrefix + ":" + nodeName;
                    } else {
                        str = "<" + nodeName;
                    }
                    doWrite(writer, str, node, line, column, offset);
                    column += str.length();
                    offset += str.length();
                    NamedNodeMap attr = node.getAttributes();
                    for (int i = 0; i < attr.getLength(); i++) {
                        Attr currentAttr = (Attr) attr.item(i);
                        boolean writeAttr = false;
                        if (config.getFeature(DOMSerializerConfiguration.DISCARD_DEFAULT_CONTENT)) {
                            if (currentAttr.getSpecified()) {
                                writeAttr = true;
                            }
                        } else {
                            writeAttr = true;
                        }
                        if (writeAttr) {
                            str = " " + currentAttr.getNodeName() + "=\"" + normalizeCharacters(currentAttr.getNodeValue()) + "\"";
                            doWrite(writer, str, node, line, column, offset);
                            column += str.length();
                            offset += str.length();
                        }
                    }
                    NodeList children = node.getChildNodes();
                    if (children != null) {
                        boolean elementEmpty = false;
                        if (children.getLength() <= 0) {
                            elementEmpty = true;
                        } else {
                            if (!config.getFeature(DOMSerializerConfiguration.WS_IN_ELEMENT_CONTENT)) {
                                boolean hasWSOnlyElements = true;
                                for (int i = 0; i < children.getLength(); i++) {
                                    hasWSOnlyElements = hasWSOnlyElements && children.item(i).getNodeType() == Node.TEXT_NODE && children.item(i).getNodeValue().trim().equals("");
                                }
                                elementEmpty = formatting && hasWSOnlyElements;
                            }
                        }
                        if (!elementEmpty) {
                            str = ">";
                            doWrite(writer, str, node, line, column, offset);
                            column += str.length();
                            offset += str.length();
                            String indentUnit = "";
                            if (formatting) {
                                if (config.getFeature(DOMSerializerConfiguration.SOFT_TABS)) {
                                    Integer indentSize = (Integer) config.getParameter("indent");
                                    if (indentSize != null) {
                                        int size = indentSize.intValue();
                                        StringBuffer buf = new StringBuffer();
                                        for (int i = 0; i < size; i++) {
                                            buf.append(" ");
                                        }
                                        indentUnit = buf.toString();
                                    }
                                } else {
                                    indentUnit = "\t";
                                }
                            }
                            for (int i = 0; i < children.getLength(); i++) {
                                rSerializeNode(writer, children.item(i), encoding, currentIndent + indentUnit, line, column, offset);
                            }
                            if (formatting) {
                                boolean allText = true;
                                for (int i = 0; i < children.getLength(); i++) {
                                    if (!(children.item(i).getNodeType() == Node.TEXT_NODE) && !(children.item(i).getNodeType() == Node.CDATA_SECTION_NODE)) {
                                        allText = false;
                                        break;
                                    }
                                }
                                if (!allText) {
                                    column = 0;
                                    str = m_newLine + currentIndent;
                                    doWrite(writer, str, node, line, column, offset);
                                    column += currentIndent.length();
                                    offset += str.length();
                                }
                            }
                            if (config.getFeature(DOMSerializerConfiguration.NAMESPACES) && nodePrefix != null) {
                                str = "</" + nodePrefix + ":" + nodeName + ">";
                            } else {
                                str = "</" + nodeName + ">";
                            }
                            doWrite(writer, str, node, line, column, offset);
                            column += str.length();
                            offset += str.length();
                        } else {
                            str = "/>";
                            doWrite(writer, str, node, line, column, offset);
                            column += str.length();
                            offset += str.length();
                        }
                    }
                    break;
                case Node.TEXT_NODE:
                    String text = node.getNodeValue();
                    if (!text.equals("")) {
                        if (formatting) {
                            if (text.trim().equals("")) {
                                return;
                            }
                        }
                        for (int i = 0; i < text.length(); i++) {
                            str = text.substring(i, i + 1);
                            if (str.equals("&")) {
                                str = "&amp;";
                            }
                            if (str.equals(">")) {
                                str = "&gt;";
                            }
                            if (str.equals("<")) {
                                str = "&lt;";
                            }
                            if (str.equals("\'")) {
                                str = "&apos;";
                            }
                            if (str.equals("\"")) {
                                str = "&quot;";
                            }
                            if (str.equals(m_newLine)) {
                                line++;
                                column = 0;
                                doWrite(writer, m_newLine, node, line, column, offset);
                                offset += m_newLine.length();
                            } else {
                                doWrite(writer, str, node, line, column, offset);
                                column += str.length();
                                offset += str.length();
                            }
                        }
                    }
                    break;
                case Node.CDATA_SECTION_NODE:
                    if (config.getFeature(DOMSerializerConfiguration.CDATA_SECTIONS)) {
                        str = "<![CDATA[";
                        doWrite(writer, str, node, line, column, offset);
                        column += str.length();
                        offset += str.length();
                        String cdata = node.getNodeValue();
                        for (int i = 0; i < cdata.length(); i++) {
                            str = cdata.substring(i, i + 1);
                            if (str.equals("]") && i + 3 < cdata.length() && cdata.substring(i, i + 3).equals("]]>")) {
                                DefaultDOMLocator loc = new DefaultDOMLocator(node, line, column, offset, null);
                                if (config.getFeature(DOMSerializerConfiguration.SPLIT_CDATA)) {
                                    i += 2;
                                    str = "]]]]>";
                                    str += "<![CDATA[>";
                                    throwError(loc, "cdata-sections-splitted", DOMError.SEVERITY_WARNING, null);
                                } else {
                                    throwError(loc, "invalid-data-in-cdata-section", DOMError.SEVERITY_FATAL_ERROR, null);
                                }
                            }
                            if (str.equals(m_newLine)) {
                                line++;
                                column = 0;
                                doWrite(writer, m_newLine, node, line, column, offset);
                                offset += m_newLine.length();
                            } else {
                                doWrite(writer, str, node, line, column, offset);
                                column += str.length();
                                offset += str.length();
                            }
                        }
                        str = "]]>";
                        doWrite(writer, str, node, line, column, offset);
                        column += str.length();
                        offset += str.length();
                    } else {
                        Node textNode = node.getOwnerDocument().createTextNode(node.getNodeValue());
                        rSerializeNode(writer, textNode, encoding, currentIndent, line, column, offset);
                    }
                    break;
                case Node.COMMENT_NODE:
                    if (config.getFeature("comments")) {
                        if (formatting) {
                            column = 0;
                            str = m_newLine + currentIndent;
                            doWrite(writer, str, node, line, column, offset);
                            column += currentIndent.length();
                            offset += str.length();
                        }
                        str = "<!--" + node.getNodeValue() + "-->";
                        doWrite(writer, str, node, line, column, offset);
                        column += str.length();
                        offset += str.length();
                    }
                    break;
                case Node.PROCESSING_INSTRUCTION_NODE:
                    if (formatting) {
                        column = 0;
                        str = m_newLine + currentIndent;
                        doWrite(writer, str, node, line, column, offset);
                        column += currentIndent.length();
                        offset += str.length();
                    }
                    str = "<?" + node.getNodeName() + " " + node.getNodeValue() + "?>";
                    doWrite(writer, str, node, line, column, offset);
                    column += str.length();
                    offset += str.length();
                    break;
                case Node.ENTITY_REFERENCE_NODE:
                    str = "&" + node.getNodeName() + ";";
                    doWrite(writer, str, node, line, column, offset);
                    column += str.length();
                    offset += str.length();
                    break;
                case Node.DOCUMENT_TYPE_NODE:
                    DocumentType docType = (DocumentType) node;
                    if (formatting) {
                        column = 0;
                        str = m_newLine + currentIndent;
                        doWrite(writer, str, node, line, column, offset);
                        column += currentIndent.length();
                        offset += str.length();
                    }
                    str = "<!DOCTYPE " + docType.getName();
                    doWrite(writer, str, node, line, column, offset);
                    column += str.length();
                    offset += str.length();
                    if (docType.getPublicId() != null) {
                        str = " PUBLIC \"" + docType.getPublicId() + "\" ";
                        doWrite(writer, str, node, line, column, offset);
                        column += str.length();
                        offset += str.length();
                    }
                    if (docType.getSystemId() != null) {
                        if (docType.getPublicId() == null) {
                            str = " SYSTEM ";
                        } else {
                            str = "";
                        }
                        str += "\"" + docType.getSystemId() + "\"";
                        doWrite(writer, str, node, line, column, offset);
                        column += str.length();
                        offset += str.length();
                    }
                    String internalSubset = docType.getInternalSubset();
                    if (internalSubset != null && !internalSubset.equals("")) {
                        str = " [ " + internalSubset + " ]";
                        doWrite(writer, str, node, line, column, offset);
                        column += str.length();
                        offset += str.length();
                    }
                    str = ">";
                    doWrite(writer, str, node, line, column, offset);
                    column += str.length();
                    offset += str.length();
                    if (!formatting) {
                        column = 0;
                        str = m_newLine + currentIndent;
                        doWrite(writer, str, node, line, column, offset);
                        column += currentIndent.length();
                        offset += str.length();
                    }
                    break;
            }
        }
    }

    /**
     * Performs an actual write and implements error handling.
     */
    private void doWrite(Writer writer, String str, Node wnode, int line, int column, int offset) throws DOMSerializerException {
        try {
            writer.write(str, 0, str.length());
        } catch (IOException ioe) {
            DefaultDOMLocator loc = new DefaultDOMLocator(wnode, line, column, offset, null);
            throwError(loc, "io-error", DOMError.SEVERITY_FATAL_ERROR, ioe);
        }
    }

    /**
     * Throws an error, notifying the ErrorHandler object if necessary.
     * @return the value returned by the error handler or false if the severity was SEVERITY_FATAL_ERROR
     */
    private void throwError(DOMLocator loc, String type, short severity, Exception e) throws DOMSerializerException {
        Object rawHandler = config.getParameter(DOMSerializerConfiguration.ERROR_HANDLER);
        boolean handled = false;
        if (severity == DOMError.SEVERITY_WARNING) {
            handled = true;
        }
        DOMSerializerError error = new DOMSerializerError(loc, e, severity, type);
        if (rawHandler != null) {
            DOMErrorHandler handler = (DOMErrorHandler) rawHandler;
            handled = handler.handleError(error);
        }
        if ((severity == DOMError.SEVERITY_ERROR && !handled) || severity == DOMError.SEVERITY_FATAL_ERROR) {
            throw new DOMSerializerException(error);
        }
    }

    private String normalizeCharacters(String text) {
        StringBuffer newText = new StringBuffer();
        for (int i = 0; i < text.length(); i++) {
            String str = text.substring(i, i + 1);
            if (str.equals("&")) {
                str = "&amp;";
            }
            if (str.equals(">")) {
                str = "&gt;";
            }
            if (str.equals("<")) {
                str = "&lt;";
            }
            if (str.equals("\'")) {
                str = "&apos;";
            }
            if (str.equals("\"")) {
                str = "&quot;";
            }
            newText.append(str);
        }
        return newText.toString();
    }

    private DOMSerializerConfiguration config;

    private LSSerializerFilter m_filter;

    private String m_newLine;
}
