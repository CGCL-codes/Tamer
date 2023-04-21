package fbench.dom;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Hashtable;
import java.util.Stack;

/**
 * Writes XML to text.
 * 
 * @author DG
 * 
 */
public class XmlWriter {

    private Writer writer = null;

    private Stack<String> elementStack = new Stack<String>();

    private Hashtable<String, String> attributes = new Hashtable<String, String>();

    private boolean startElementOpen = false;

    private boolean currentElementEmpty = true;

    private boolean cursorOnNewLine = true;

    private boolean needNewLine = false;

    private String indent = "  ";

    private String newLine = System.getProperty("line.separator");

    private String xmlVersion = "1.0";

    private String encoding = "utf-8";

    private int line = 0;

    private int column = 0;

    private int characters = 0;

    public XmlWriter(String path) throws IOException {
        this.writer = new BufferedWriter(new FileWriter(new File(path)));
    }

    public XmlWriter(Writer writer) {
        this.writer = writer;
    }

    protected Writer getWriter() {
        return writer;
    }

    protected void setWriter(Writer writer) {
        this.writer = writer;
    }

    public XmlWriter writeDocType(String name, String pubid, String sysid, String subset) throws XmlException {
        try {
            if (needNewLine && !stringNullOrEmpty(this.newLine)) {
                write(this.newLine);
                this.cursorOnNewLine = true;
                this.needNewLine = false;
            }
            validateName(name);
            write("<!DOCTYPE ");
            write(name);
            if (pubid != null) {
                write(" PUBLIC \"");
                write(pubid);
                write("\" \"");
                write(sysid);
                write("\"");
            } else if (sysid != null) {
                write(" SYSTEM \"");
                write(sysid);
                write("\"");
            }
            if (subset != null) {
                write("[");
                write(subset);
                write("]");
            }
            write(">");
            this.needNewLine = true;
        } catch (IOException e) {
            throw new XmlException("Error writing doctype.", e);
        }
        return this;
    }

    public XmlWriter writeStartDocument() throws XmlException {
        try {
            write("<?xml version=\"");
            write(this.xmlVersion);
            write("\" encoding=\"");
            write(this.encoding);
            write("\" ?>");
            this.needNewLine = true;
        } catch (IOException e) {
            throw new XmlException("Error writing start document.", e);
        }
        return this;
    }

    public XmlWriter writeEndDocument() throws XmlException {
        while (!this.elementStack.empty()) this.writeEndElement();
        return this;
    }

    public XmlWriter writeComment(String comment) throws XmlException {
        try {
            moveBeginElement();
            write("<!--");
            write(comment);
            write("-->");
            this.needNewLine = true;
        } catch (IOException e) {
            throw new XmlException("Error writing comment.", e);
        }
        return this;
    }

    public XmlWriter writeStartElement(String name) throws XmlException {
        try {
            validateName(name);
            moveBeginElement();
            write("<");
            write(name);
            this.elementStack.add(name);
            this.currentElementEmpty = true;
            this.cursorOnNewLine = false;
            this.needNewLine = true;
            this.startElementOpen = true;
        } catch (IOException e) {
            throw new XmlException("Error writing start element " + name, e);
        }
        return this;
    }

    public XmlWriter writeEndElement() throws XmlException {
        if (this.elementStack.empty()) throw new XmlException("Cannot close element.");
        String name = elementStack.pop();
        try {
            if (!stringNullOrEmpty(name)) {
                if (this.currentElementEmpty) {
                    closeStartElement();
                } else {
                    if (needNewLine && !stringNullOrEmpty(this.newLine)) {
                        write(this.newLine);
                        this.needNewLine = false;
                        this.cursorOnNewLine = true;
                    }
                    writeIndent();
                    write("</");
                    write(name);
                    write(">");
                }
                this.cursorOnNewLine = false;
                this.needNewLine = true;
                this.currentElementEmpty = false;
            }
        } catch (IOException e) {
            throw new XmlException("Error writing end element " + name, e);
        }
        return this;
    }

    public XmlWriter writeString(String text) throws XmlException {
        try {
            this.currentElementEmpty = false;
            this.needNewLine = false;
            closeStartElement();
            write(replaceChars(text));
        } catch (IOException e) {
            throw new XmlException("Error writing text.", e);
        }
        return this;
    }

    public XmlWriter writeAttributeString(String name, String value) throws XmlException {
        if (this.startElementOpen) {
            validateName(name);
            this.attributes.put(name, replaceChars(value));
        } else {
            throw new XmlException("Cannot write attributes after closing the start element.");
        }
        return this;
    }

    public XmlWriter writeRaw(String text) throws XmlException {
        try {
            this.currentElementEmpty = false;
            this.needNewLine = false;
            closeStartElement();
            write(text);
        } catch (IOException e) {
            throw new XmlException("Error writing text.", e);
        }
        return this;
    }

    public void close() throws XmlException {
        try {
            writeEndDocument();
            writer.close();
        } catch (IOException e) {
            throw new XmlException("Error closing writer.", e);
        }
    }

    private void write(String s) throws IOException {
        int index = s.indexOf('\n');
        int lastIndex = -1;
        int length = s.length();
        while (index > -1) {
            this.line++;
            lastIndex = index;
            if (index < length) index = s.indexOf('\n', index + 1); else index = -1;
        }
        if (lastIndex > -1) this.column = length - lastIndex - 1; else this.column += length;
        this.characters += length;
        this.writer.write(s);
    }

    /**
	 * Closes any open elements and moves to a position where a new element begins.
	 * @throws IOException
	 */
    protected void moveBeginElement() throws IOException {
        this.currentElementEmpty = false;
        closeStartElement();
        if (needNewLine && !stringNullOrEmpty(this.newLine)) {
            write(this.newLine);
            this.cursorOnNewLine = true;
            this.needNewLine = false;
        }
        writeIndent();
    }

    /**
	 * Closes a start element.
	 * 
	 * @throws IOException
	 */
    private void closeStartElement() throws IOException {
        if (this.startElementOpen) {
            writeAttributes();
            if (this.currentElementEmpty) write(" />"); else write(">");
            this.startElementOpen = false;
        }
    }

    /**
	 * @throws IOException
	 */
    private void writeIndent() throws IOException {
        if (cursorOnNewLine && !stringNullOrEmpty(this.indent)) {
            for (int i = this.elementStack.size() - 1; i >= 0; i--) write(this.indent);
            cursorOnNewLine = false;
        }
    }

    private void writeAttributes() throws IOException {
        for (String name : attributes.keySet()) {
            String value = attributes.get(name);
            write(" ");
            write(name);
            write("=\"");
            write(replaceChars(value));
            write("\"");
        }
        this.attributes.clear();
    }

    /**
	 * @param indent
	 *            The string used to indent elements.
	 */
    public void setIndent(String indent) {
        this.indent = indent;
    }

    /**
	 * @return the The string used to indent elements.
	 */
    public String getIndent() {
        return indent;
    }

    /**
	 * @param indent
	 *            The string used to create a new line.
	 */
    public void setNewLine(String newLine) {
        this.newLine = newLine;
    }

    /**
	 * @return The string used to create a new line.
	 */
    public String getNewLine() {
        return newLine;
    }

    /**
	 * @param xmlVersion
	 *            the xmlVersion to set
	 */
    public void setXmlVersion(String xmlVersion) {
        this.xmlVersion = xmlVersion;
    }

    /**
	 * @return the xmlVersion
	 */
    public String getXmlVersion() {
        return xmlVersion;
    }

    /**
	 * @param encoding
	 *            the encoding to set
	 */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    /**
	 * @return the encoding
	 */
    public String getEncoding() {
        return encoding;
    }

    /**
	 * @return the current line
	 */
    public int getLine() {
        return line;
    }

    /**
	 * @return the current column
	 */
    public int getColumn() {
        return column;
    }

    /**
	 * @return the length of text written.
	 */
    public int getLength() {
        return characters;
    }

    /**
	 * @param name
	 * @throws XmlException
	 */
    private static void validateName(String name) throws XmlException {
        if (!isValidName(name)) throw new XmlException("The specified name '" + name + "' is invalid.");
    }

    private static boolean isValidName(String name) {
        if (stringNullOrEmpty(name)) return false;
        return !(name.contains("<") || name.contains(">") || name.startsWith("?"));
    }

    public static String replaceChars(String text) {
        String s = new String(text);
        s = s.replaceAll("&", "&#38;");
        s = s.replaceAll("\n", "&#10;");
        s = s.replaceAll("\"", "&#34;");
        s = s.replaceAll(">", "&#62;");
        s = s.replaceAll("<", "&#60;");
        s = s.replaceAll("'", "&#39;");
        return s;
    }

    private static boolean stringNullOrEmpty(String s) {
        return s == null || s.length() == 0;
    }

    public static void main(String[] args) {
        try {
            XmlWriter writer = new XmlWriter("test.xml");
            writer.writeStartDocument();
            writer.writeComment("This is a basic test for the XmlWriter class.");
            writer.writeStartElement("Root");
            writer.writeStartElement("EmptyElement1");
            writer.writeEndElement();
            writer.writeStartElement("EmptyElement2");
            writer.writeEndElement();
            writer.writeStartElement("Child1");
            writer.writeString("Text");
            writer.writeEndElement();
            writer.writeStartElement("Child2");
            writer.writeString("Text");
            writer.writeEndElement();
            writer.writeStartElement("Child3");
            writer.writeStartElement("Child4");
            writer.writeStartElement("Child5");
            writer.writeString("Text");
            writer.writeEndElement();
            writer.writeEndElement();
            writer.writeEndElement();
            writer.writeStartElement("AttributeElement1");
            writer.writeAttributeString("Att1", "Value1");
            writer.writeAttributeString("Att2", "Value2");
            writer.writeEndElement();
            writer.writeStartElement("AttributeElement2");
            writer.writeAttributeString("Att1", "Value1");
            writer.writeAttributeString("Att2", "Value2");
            writer.writeString("Text");
            writer.writeEndElement();
            writer.writeEndElement();
            writer.writeEndDocument();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlException e) {
            e.printStackTrace();
        }
    }
}
