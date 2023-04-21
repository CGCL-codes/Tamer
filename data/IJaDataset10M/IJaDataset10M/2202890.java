package org.jdom.output;

import java.io.*;
import java.util.*;
import javax.xml.transform.Result;
import org.jdom.*;

/**
 * Outputs a JDOM document as a stream of bytes. The outputter can manage many
 * styles of document formatting, from untouched to pretty printed. The default
 * is to output the document content exactly as created, but this can be changed
 * by setting a new Format object. For pretty-print output, use
 * <code>{@link Format#getPrettyFormat()}</code>. For whitespace-normalized
 * output, use <code>{@link Format#getCompactFormat()}</code>.
 * <p>
 * There are <code>{@link #output output(...)}</code> methods to print any of
 * the standard JDOM classes, including Document and Element, to either a Writer
 * or an OutputStream. <b>Warning</b>: When outputting to a Writer, make sure
 * the writer's encoding matches the encoding setting in the Format object. This
 * ensures the encoding in which the content is written (controlled by the
 * Writer configuration) matches the encoding placed in the document's XML
 * declaration (controlled by the XMLOutputter). Because a Writer cannot be
 * queried for its encoding, the information must be passed to the Format
 * manually in its constructor or via the
 * <code>{@link Format#setEncoding}</code> method. The default encoding is
 * UTF-8.
 * <p>
 * The methods <code>{@link #outputString outputString(...)}</code> are for
 * convenience only; for top performance you should call one of the <code>{@link
 * #output output(...)}</code> methods and pass in your own Writer or
 * OutputStream if possible.
 * <p>
 * XML declarations are always printed on their own line followed by a line
 * seperator (this doesn't change the semantics of the document). To omit
 * printing of the declaration use
 * <code>{@link Format#setOmitDeclaration}</code>. To omit printing of the
 * encoding in the declaration use <code>{@link Format#setOmitEncoding}</code>.
 * Unfortunatly there is currently no way to know the original encoding of the
 * document.
 * <p>
 * Empty elements are by default printed as &lt;empty/&gt;, but this can be
 * configured with <code>{@link Format#setExpandEmptyElements}</code> to cause
 * them to be expanded to &lt;empty&gt;&lt;/empty&gt;.
 *
 * @version $Revision: 1.116 $, $Date: 2007/11/10 05:29:01 $
 * @author  Brett McLaughlin
 * @author  Jason Hunter
 * @author  Jason Reid
 * @author  Wolfgang Werner
 * @author  Elliotte Rusty Harold
 * @author  David &amp; Will (from Post Tool Design)
 * @author  Dan Schaffer
 * @author  Alex Chaffee
 * @author  Bradley S. Huffman
 */
public class XMLOutputter implements Cloneable {

    private static final String CVS_ID = "@(#) $RCSfile: XMLOutputter.java,v $ $Revision: 1.116 $ $Date: 2007/11/10 05:29:01 $ $Name: jdom_1_1 $";

    private Format userFormat = Format.getRawFormat();

    protected static final Format preserveFormat = Format.getRawFormat();

    protected Format currentFormat = userFormat;

    /** Whether output escaping is enabled for the being processed
      * Element - default is <code>true</code> */
    private boolean escapeOutput = true;

    /**
     * This will create an <code>XMLOutputter</code> with the default
     * {@link Format} matching {@link Format#getRawFormat}.
     */
    public XMLOutputter() {
    }

    /**
     * This will create an <code>XMLOutputter</code> with the specified
     * format characteristics.  Note the format object is cloned internally
     * before use.
     */
    public XMLOutputter(Format format) {
        userFormat = (Format) format.clone();
        currentFormat = userFormat;
    }

    /**
     * This will create an <code>XMLOutputter</code> with all the
     * options as set in the given <code>XMLOutputter</code>.  Note
     * that <code>XMLOutputter two = (XMLOutputter)one.clone();</code>
     * would work equally well.
     *
     * @param that the XMLOutputter to clone
     */
    public XMLOutputter(XMLOutputter that) {
        this.userFormat = (Format) that.userFormat.clone();
        currentFormat = userFormat;
    }

    /**
     * Sets the new format logic for the outputter.  Note the Format
     * object is cloned internally before use.
     *
     * @param newFormat the format to use for output
     */
    public void setFormat(Format newFormat) {
        this.userFormat = (Format) newFormat.clone();
        this.currentFormat = userFormat;
    }

    /**
     * Returns the current format in use by the outputter.  Note the
     * Format object returned is a clone of the one used internally.
     */
    public Format getFormat() {
        return (Format) userFormat.clone();
    }

    /**
     * This will print the <code>Document</code> to the given output stream.
     * The characters are printed using the encoding specified in the
     * constructor, or a default of UTF-8.
     *
     * @param doc <code>Document</code> to format.
     * @param out <code>OutputStream</code> to use.
     * @throws IOException - if there's any problem writing.
     */
    public void output(Document doc, OutputStream out) throws IOException {
        Writer writer = makeWriter(out);
        output(doc, writer);
    }

    /**
     * Print out the <code>{@link DocType}</code>.
     *
     * @param doctype <code>DocType</code> to output.
     * @param out <code>OutputStream</code> to use.
     */
    public void output(DocType doctype, OutputStream out) throws IOException {
        Writer writer = makeWriter(out);
        output(doctype, writer);
    }

    /**
     * Print out an <code>{@link Element}</code>, including
     * its <code>{@link Attribute}</code>s, and all
     * contained (child) elements, etc.
     *
     * @param element <code>Element</code> to output.
     * @param out <code>Writer</code> to use.
     */
    public void output(Element element, OutputStream out) throws IOException {
        Writer writer = makeWriter(out);
        output(element, writer);
    }

    /**
     * This will handle printing out an <code>{@link
     * Element}</code>'s content only, not including its tag, and
     * attributes.  This can be useful for printing the content of an
     * element that contains HTML, like "&lt;description&gt;JDOM is
     * &lt;b&gt;fun&gt;!&lt;/description&gt;".
     *
     * @param element <code>Element</code> to output.
     * @param out <code>OutputStream</code> to use.
     */
    public void outputElementContent(Element element, OutputStream out) throws IOException {
        Writer writer = makeWriter(out);
        outputElementContent(element, writer);
    }

    /**
     * This will handle printing out a list of nodes.
     * This can be useful for printing the content of an element that
     * contains HTML, like "&lt;description&gt;JDOM is
     * &lt;b&gt;fun&gt;!&lt;/description&gt;".
     *
     * @param list <code>List</code> of nodes.
     * @param out <code>OutputStream</code> to use.
     */
    public void output(List list, OutputStream out) throws IOException {
        Writer writer = makeWriter(out);
        output(list, writer);
    }

    /**
     * Print out a <code>{@link CDATA}</code> node.
     *
     * @param cdata <code>CDATA</code> to output.
     * @param out <code>OutputStream</code> to use.
     */
    public void output(CDATA cdata, OutputStream out) throws IOException {
        Writer writer = makeWriter(out);
        output(cdata, writer);
    }

    /**
     * Print out a <code>{@link Text}</code> node.  Perfoms
     * the necessary entity escaping and whitespace stripping.
     *
     * @param text <code>Text</code> to output.
     * @param out <code>OutputStream</code> to use.
     */
    public void output(Text text, OutputStream out) throws IOException {
        Writer writer = makeWriter(out);
        output(text, writer);
    }

    /**
     * Print out a <code>{@link Comment}</code>.
     *
     * @param comment <code>Comment</code> to output.
     * @param out <code>OutputStream</code> to use.
     */
    public void output(Comment comment, OutputStream out) throws IOException {
        Writer writer = makeWriter(out);
        output(comment, writer);
    }

    /**
     * Print out a <code>{@link ProcessingInstruction}</code>.
     *
     * @param pi <code>ProcessingInstruction</code> to output.
     * @param out <code>OutputStream</code> to use.
     */
    public void output(ProcessingInstruction pi, OutputStream out) throws IOException {
        Writer writer = makeWriter(out);
        output(pi, writer);
    }

    /**
     * Print out a <code>{@link EntityRef}</code>.
     *
     * @param entity <code>EntityRef</code> to output.
     * @param out <code>OutputStream</code> to use.
     */
    public void output(EntityRef entity, OutputStream out) throws IOException {
        Writer writer = makeWriter(out);
        output(entity, writer);
    }

    /**
     * Get an OutputStreamWriter, using prefered encoding
     * (see {@link Format#setEncoding}).
     */
    private Writer makeWriter(OutputStream out) throws java.io.UnsupportedEncodingException {
        return makeWriter(out, userFormat.encoding);
    }

    /**
     * Get an OutputStreamWriter, use specified encoding.
     */
    private static Writer makeWriter(OutputStream out, String enc) throws java.io.UnsupportedEncodingException {
        if ("UTF-8".equals(enc)) {
            enc = "UTF8";
        }
        Writer writer = new BufferedWriter((new OutputStreamWriter(new BufferedOutputStream(out), enc)));
        return writer;
    }

    /**
     * This will print the <code>Document</code> to the given Writer.
     *
     * <p>
     * Warning: using your own Writer may cause the outputter's
     * preferred character encoding to be ignored.  If you use
     * encodings other than UTF-8, we recommend using the method that
     * takes an OutputStream instead.
     * </p>
     *
     * @param doc <code>Document</code> to format.
     * @param out <code>Writer</code> to use.
     * @throws IOException - if there's any problem writing.
     */
    public void output(Document doc, Writer out) throws IOException {
        printDeclaration(out, doc, userFormat.encoding);
        List content = doc.getContent();
        int size = content.size();
        for (int i = 0; i < size; i++) {
            Object obj = content.get(i);
            if (obj instanceof Element) {
                printElement(out, doc.getRootElement(), 0, createNamespaceStack());
            } else if (obj instanceof Comment) {
                printComment(out, (Comment) obj);
            } else if (obj instanceof ProcessingInstruction) {
                printProcessingInstruction(out, (ProcessingInstruction) obj);
            } else if (obj instanceof DocType) {
                printDocType(out, doc.getDocType());
                out.write(currentFormat.lineSeparator);
            } else {
            }
            newline(out);
            indent(out, 0);
        }
        out.write(currentFormat.lineSeparator);
        out.flush();
    }

    /**
     * Print out the <code>{@link DocType}</code>.
     *
     * @param doctype <code>DocType</code> to output.
     * @param out <code>Writer</code> to use.
     */
    public void output(DocType doctype, Writer out) throws IOException {
        printDocType(out, doctype);
        out.flush();
    }

    /**
     * Print out an <code>{@link Element}</code>, including
     * its <code>{@link Attribute}</code>s, and all
     * contained (child) elements, etc.
     *
     * @param element <code>Element</code> to output.
     * @param out <code>Writer</code> to use.
     */
    public void output(Element element, Writer out) throws IOException {
        printElement(out, element, 0, createNamespaceStack());
        out.flush();
    }

    /**
     * This will handle printing out an <code>{@link
     * Element}</code>'s content only, not including its tag, and
     * attributes.  This can be useful for printing the content of an
     * element that contains HTML, like "&lt;description&gt;JDOM is
     * &lt;b&gt;fun&gt;!&lt;/description&gt;".
     *
     * @param element <code>Element</code> to output.
     * @param out <code>Writer</code> to use.
     */
    public void outputElementContent(Element element, Writer out) throws IOException {
        List content = element.getContent();
        printContentRange(out, content, 0, content.size(), 0, createNamespaceStack());
        out.flush();
    }

    /**
     * This will handle printing out a list of nodes.
     * This can be useful for printing the content of an element that
     * contains HTML, like "&lt;description&gt;JDOM is
     * &lt;b&gt;fun&gt;!&lt;/description&gt;".
     *
     * @param list <code>List</code> of nodes.
     * @param out <code>Writer</code> to use.
     */
    public void output(List list, Writer out) throws IOException {
        printContentRange(out, list, 0, list.size(), 0, createNamespaceStack());
        out.flush();
    }

    /**
     * Print out a <code>{@link CDATA}</code> node.
     *
     * @param cdata <code>CDATA</code> to output.
     * @param out <code>Writer</code> to use.
     */
    public void output(CDATA cdata, Writer out) throws IOException {
        printCDATA(out, cdata);
        out.flush();
    }

    /**
     * Print out a <code>{@link Text}</code> node.  Perfoms
     * the necessary entity escaping and whitespace stripping.
     *
     * @param text <code>Text</code> to output.
     * @param out <code>Writer</code> to use.
     */
    public void output(Text text, Writer out) throws IOException {
        printText(out, text);
        out.flush();
    }

    /**
     * Print out a <code>{@link Comment}</code>.
     *
     * @param comment <code>Comment</code> to output.
     * @param out <code>Writer</code> to use.
     */
    public void output(Comment comment, Writer out) throws IOException {
        printComment(out, comment);
        out.flush();
    }

    /**
     * Print out a <code>{@link ProcessingInstruction}</code>.
     *
     * @param pi <code>ProcessingInstruction</code> to output.
     * @param out <code>Writer</code> to use.
     */
    public void output(ProcessingInstruction pi, Writer out) throws IOException {
        boolean currentEscapingPolicy = currentFormat.ignoreTrAXEscapingPIs;
        currentFormat.setIgnoreTrAXEscapingPIs(true);
        printProcessingInstruction(out, pi);
        currentFormat.setIgnoreTrAXEscapingPIs(currentEscapingPolicy);
        out.flush();
    }

    /**
     * Print out a <code>{@link EntityRef}</code>.
     *
     * @param entity <code>EntityRef</code> to output.
     * @param out <code>Writer</code> to use.
     */
    public void output(EntityRef entity, Writer out) throws IOException {
        printEntityRef(out, entity);
        out.flush();
    }

    /**
     * Return a string representing a document.  Uses an internal
     * StringWriter. Warning: a String is Unicode, which may not match
     * the outputter's specified encoding.
     *
     * @param doc <code>Document</code> to format.
     */
    public String outputString(Document doc) {
        StringWriter out = new StringWriter();
        try {
            output(doc, out);
        } catch (IOException e) {
        }
        return out.toString();
    }

    /**
     * Return a string representing a DocType. Warning: a String is
     * Unicode, which may not match the outputter's specified
     * encoding.
     *
     * @param doctype <code>DocType</code> to format.
     */
    public String outputString(DocType doctype) {
        StringWriter out = new StringWriter();
        try {
            output(doctype, out);
        } catch (IOException e) {
        }
        return out.toString();
    }

    /**
     * Return a string representing an element. Warning: a String is
     * Unicode, which may not match the outputter's specified
     * encoding.
     *
     * @param element <code>Element</code> to format.
     */
    public String outputString(Element element) {
        StringWriter out = new StringWriter();
        try {
            output(element, out);
        } catch (IOException e) {
        }
        return out.toString();
    }

    /**
     * Return a string representing a list of nodes.  The list is
     * assumed to contain legal JDOM nodes.
     *
     * @param list <code>List</code> to format.
     */
    public String outputString(List list) {
        StringWriter out = new StringWriter();
        try {
            output(list, out);
        } catch (IOException e) {
        }
        return out.toString();
    }

    /**
     * Return a string representing a CDATA node. Warning: a String is
     * Unicode, which may not match the outputter's specified
     * encoding.
     *
     * @param cdata <code>CDATA</code> to format.
     */
    public String outputString(CDATA cdata) {
        StringWriter out = new StringWriter();
        try {
            output(cdata, out);
        } catch (IOException e) {
        }
        return out.toString();
    }

    /**
     * Return a string representing a Text node. Warning: a String is
     * Unicode, which may not match the outputter's specified
     * encoding.
     *
     * @param text <code>Text</code> to format.
     */
    public String outputString(Text text) {
        StringWriter out = new StringWriter();
        try {
            output(text, out);
        } catch (IOException e) {
        }
        return out.toString();
    }

    /**
     * Return a string representing a comment. Warning: a String is
     * Unicode, which may not match the outputter's specified
     * encoding.
     *
     * @param comment <code>Comment</code> to format.
     */
    public String outputString(Comment comment) {
        StringWriter out = new StringWriter();
        try {
            output(comment, out);
        } catch (IOException e) {
        }
        return out.toString();
    }

    /**
     * Return a string representing a PI. Warning: a String is
     * Unicode, which may not match the outputter's specified
     * encoding.
     *
     * @param pi <code>ProcessingInstruction</code> to format.
     */
    public String outputString(ProcessingInstruction pi) {
        StringWriter out = new StringWriter();
        try {
            output(pi, out);
        } catch (IOException e) {
        }
        return out.toString();
    }

    /**
     * Return a string representing an entity. Warning: a String is
     * Unicode, which may not match the outputter's specified
     * encoding.
     *
     * @param entity <code>EntityRef</code> to format.
     */
    public String outputString(EntityRef entity) {
        StringWriter out = new StringWriter();
        try {
            output(entity, out);
        } catch (IOException e) {
        }
        return out.toString();
    }

    /**
     * This will handle printing of the declaration.
     * Assumes XML version 1.0 since we don't directly know.
     *
     * @param doc <code>Document</code> whose declaration to write.
     * @param out <code>Writer</code> to use.
     * @param encoding The encoding to add to the declaration
     */
    protected void printDeclaration(Writer out, Document doc, String encoding) throws IOException {
        if (!userFormat.omitDeclaration) {
            out.write("<?xml version=\"1.0\"");
            if (!userFormat.omitEncoding) {
                out.write(" encoding=\"" + encoding + "\"");
            }
            out.write("?>");
            out.write(currentFormat.lineSeparator);
        }
    }

    /**
     * This handle printing the DOCTYPE declaration if one exists.
     *
     * @param docType <code>Document</code> whose declaration to write.
     * @param out <code>Writer</code> to use.
     */
    protected void printDocType(Writer out, DocType docType) throws IOException {
        String publicID = docType.getPublicID();
        String systemID = docType.getSystemID();
        String internalSubset = docType.getInternalSubset();
        boolean hasPublic = false;
        out.write("<!DOCTYPE ");
        out.write(docType.getElementName());
        if (publicID != null) {
            out.write(" PUBLIC \"");
            out.write(publicID);
            out.write("\"");
            hasPublic = true;
        }
        if (systemID != null) {
            if (!hasPublic) {
                out.write(" SYSTEM");
            }
            out.write(" \"");
            out.write(systemID);
            out.write("\"");
        }
        if ((internalSubset != null) && (!internalSubset.equals(""))) {
            out.write(" [");
            out.write(currentFormat.lineSeparator);
            out.write(docType.getInternalSubset());
            out.write("]");
        }
        out.write(">");
    }

    /**
     * This will handle printing of comments.
     *
     * @param comment <code>Comment</code> to write.
     * @param out <code>Writer</code> to use.
     */
    protected void printComment(Writer out, Comment comment) throws IOException {
        out.write("<!--");
        out.write(comment.getText());
        out.write("-->");
    }

    /**
     * This will handle printing of processing instructions.
     *
     * @param pi <code>ProcessingInstruction</code> to write.
     * @param out <code>Writer</code> to use.
     */
    protected void printProcessingInstruction(Writer out, ProcessingInstruction pi) throws IOException {
        String target = pi.getTarget();
        boolean piProcessed = false;
        if (currentFormat.ignoreTrAXEscapingPIs == false) {
            if (target.equals(Result.PI_DISABLE_OUTPUT_ESCAPING)) {
                escapeOutput = false;
                piProcessed = true;
            } else if (target.equals(Result.PI_ENABLE_OUTPUT_ESCAPING)) {
                escapeOutput = true;
                piProcessed = true;
            }
        }
        if (piProcessed == false) {
            String rawData = pi.getData();
            if (!"".equals(rawData)) {
                out.write("<?");
                out.write(target);
                out.write(" ");
                out.write(rawData);
                out.write("?>");
            } else {
                out.write("<?");
                out.write(target);
                out.write("?>");
            }
        }
    }

    /**
     * This will handle printing a <code>{@link EntityRef}</code>.
     * Only the entity reference such as <code>&amp;entity;</code>
     * will be printed. However, subclasses are free to override
     * this method to print the contents of the entity instead.
     *
     * @param entity <code>EntityRef</code> to output.
     * @param out <code>Writer</code> to use.  */
    protected void printEntityRef(Writer out, EntityRef entity) throws IOException {
        out.write("&");
        out.write(entity.getName());
        out.write(";");
    }

    /**
     * This will handle printing of <code>{@link CDATA}</code> text.
     *
     * @param cdata <code>CDATA</code> to output.
     * @param out <code>Writer</code> to use.
     */
    protected void printCDATA(Writer out, CDATA cdata) throws IOException {
        String str = (currentFormat.mode == Format.TextMode.NORMALIZE) ? cdata.getTextNormalize() : ((currentFormat.mode == Format.TextMode.TRIM) ? cdata.getText().trim() : cdata.getText());
        out.write("<![CDATA[");
        out.write(str);
        out.write("]]>");
    }

    /**
     * This will handle printing of <code>{@link Text}</code> strings.
     *
     * @param text <code>Text</code> to write.
     * @param out <code>Writer</code> to use.
     */
    protected void printText(Writer out, Text text) throws IOException {
        String str = (currentFormat.mode == Format.TextMode.NORMALIZE) ? text.getTextNormalize() : ((currentFormat.mode == Format.TextMode.TRIM) ? text.getText().trim() : text.getText());
        out.write(escapeElementEntities(str));
    }

    /**
     * This will handle printing a string.  Escapes the element entities,
     * trims interior whitespace, etc. if necessary.
     */
    private void printString(Writer out, String str) throws IOException {
        if (currentFormat.mode == Format.TextMode.NORMALIZE) {
            str = Text.normalizeString(str);
        } else if (currentFormat.mode == Format.TextMode.TRIM) {
            str = str.trim();
        }
        out.write(escapeElementEntities(str));
    }

    /**
     * This will handle printing of a <code>{@link Element}</code>,
     * its <code>{@link Attribute}</code>s, and all contained (child)
     * elements, etc.
     *
     * @param element <code>Element</code> to output.
     * @param out <code>Writer</code> to use.
     * @param level <code>int</code> level of indention.
     * @param namespaces <code>List</code> stack of Namespaces in scope.
     */
    protected void printElement(Writer out, Element element, int level, NamespaceStack namespaces) throws IOException {
        List attributes = element.getAttributes();
        List content = element.getContent();
        String space = null;
        if (attributes != null) {
            space = element.getAttributeValue("space", Namespace.XML_NAMESPACE);
        }
        Format previousFormat = currentFormat;
        if ("default".equals(space)) {
            currentFormat = userFormat;
        } else if ("preserve".equals(space)) {
            currentFormat = preserveFormat;
        }
        out.write("<");
        printQualifiedName(out, element);
        int previouslyDeclaredNamespaces = namespaces.size();
        printElementNamespace(out, element, namespaces);
        printAdditionalNamespaces(out, element, namespaces);
        if (attributes != null) printAttributes(out, attributes, element, namespaces);
        int start = skipLeadingWhite(content, 0);
        int size = content.size();
        if (start >= size) {
            if (currentFormat.expandEmptyElements) {
                out.write("></");
                printQualifiedName(out, element);
                out.write(">");
            } else {
                out.write(" />");
            }
        } else {
            out.write(">");
            if (nextNonText(content, start) < size) {
                newline(out);
                printContentRange(out, content, start, size, level + 1, namespaces);
                newline(out);
                indent(out, level);
            } else {
                printTextRange(out, content, start, size);
            }
            out.write("</");
            printQualifiedName(out, element);
            out.write(">");
        }
        while (namespaces.size() > previouslyDeclaredNamespaces) {
            namespaces.pop();
        }
        currentFormat = previousFormat;
    }

    /**
     * This will handle printing of content within a given range.
     * The range to print is specified in typical Java fashion; the
     * starting index is inclusive, while the ending index is
     * exclusive.
     *
     * @param content <code>List</code> of content to output
     * @param start index of first content node (inclusive.
     * @param end index of last content node (exclusive).
     * @param out <code>Writer</code> to use.
     * @param level <code>int</code> level of indentation.
     * @param namespaces <code>List</code> stack of Namespaces in scope.
     */
    private void printContentRange(Writer out, List content, int start, int end, int level, NamespaceStack namespaces) throws IOException {
        boolean firstNode;
        Object next;
        int first, index;
        index = start;
        while (index < end) {
            firstNode = (index == start) ? true : false;
            next = content.get(index);
            if ((next instanceof Text) || (next instanceof EntityRef)) {
                first = skipLeadingWhite(content, index);
                index = nextNonText(content, first);
                if (first < index) {
                    if (!firstNode) newline(out);
                    indent(out, level);
                    printTextRange(out, content, first, index);
                }
                continue;
            }
            if (!firstNode) {
                newline(out);
            }
            indent(out, level);
            if (next instanceof Comment) {
                printComment(out, (Comment) next);
            } else if (next instanceof Element) {
                printElement(out, (Element) next, level, namespaces);
            } else if (next instanceof ProcessingInstruction) {
                printProcessingInstruction(out, (ProcessingInstruction) next);
            } else {
            }
            index++;
        }
    }

    /**
     * This will handle printing of a sequence of <code>{@link CDATA}</code>
     * or <code>{@link Text}</code> nodes.  It is an error to have any other
     * pass this method any other type of node.
     *
     * @param content <code>List</code> of content to output
     * @param start index of first content node (inclusive).
     * @param end index of last content node (exclusive).
     * @param out <code>Writer</code> to use.
     */
    private void printTextRange(Writer out, List content, int start, int end) throws IOException {
        String previous;
        Object node;
        String next;
        previous = null;
        start = skipLeadingWhite(content, start);
        int size = content.size();
        if (start < size) {
            end = skipTrailingWhite(content, end);
            for (int i = start; i < end; i++) {
                node = content.get(i);
                if (node instanceof Text) {
                    next = ((Text) node).getText();
                } else if (node instanceof EntityRef) {
                    next = "&" + ((EntityRef) node).getValue() + ";";
                } else {
                    throw new IllegalStateException("Should see only " + "CDATA, Text, or EntityRef");
                }
                if (next == null || "".equals(next)) {
                    continue;
                }
                if (previous != null) {
                    if (currentFormat.mode == Format.TextMode.NORMALIZE || currentFormat.mode == Format.TextMode.TRIM) {
                        if ((endsWithWhite(previous)) || (startsWithWhite(next))) {
                            out.write(" ");
                        }
                    }
                }
                if (node instanceof CDATA) {
                    printCDATA(out, (CDATA) node);
                } else if (node instanceof EntityRef) {
                    printEntityRef(out, (EntityRef) node);
                } else {
                    printString(out, next);
                }
                previous = next;
            }
        }
    }

    /**
     * This will handle printing of any needed <code>{@link Namespace}</code>
     * declarations.
     *
     * @param ns <code>Namespace</code> to print definition of
     * @param out <code>Writer</code> to use.
     */
    private void printNamespace(Writer out, Namespace ns, NamespaceStack namespaces) throws IOException {
        String prefix = ns.getPrefix();
        String uri = ns.getURI();
        if (uri.equals(namespaces.getURI(prefix))) {
            return;
        }
        out.write(" xmlns");
        if (!prefix.equals("")) {
            out.write(":");
            out.write(prefix);
        }
        out.write("=\"");
        out.write(escapeAttributeEntities(uri));
        out.write("\"");
        namespaces.push(ns);
    }

    /**
     * This will handle printing of a <code>{@link Attribute}</code> list.
     *
     * @param attributes <code>List</code> of Attribute objcts
     * @param out <code>Writer</code> to use
     */
    protected void printAttributes(Writer out, List attributes, Element parent, NamespaceStack namespaces) throws IOException {
        for (int i = 0; i < attributes.size(); i++) {
            Attribute attribute = (Attribute) attributes.get(i);
            Namespace ns = attribute.getNamespace();
            if ((ns != Namespace.NO_NAMESPACE) && (ns != Namespace.XML_NAMESPACE)) {
                printNamespace(out, ns, namespaces);
            }
            out.write(" ");
            printQualifiedName(out, attribute);
            out.write("=");
            out.write("\"");
            out.write(escapeAttributeEntities(attribute.getValue()));
            out.write("\"");
        }
    }

    private void printElementNamespace(Writer out, Element element, NamespaceStack namespaces) throws IOException {
        Namespace ns = element.getNamespace();
        if (ns == Namespace.XML_NAMESPACE) {
            return;
        }
        if (!((ns == Namespace.NO_NAMESPACE) && (namespaces.getURI("") == null))) {
            printNamespace(out, ns, namespaces);
        }
    }

    private void printAdditionalNamespaces(Writer out, Element element, NamespaceStack namespaces) throws IOException {
        List list = element.getAdditionalNamespaces();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                Namespace additional = (Namespace) list.get(i);
                printNamespace(out, additional, namespaces);
            }
        }
    }

    /**
     * This will print a newline only if indent is not null.
     *
     * @param out <code>Writer</code> to use
     */
    private void newline(Writer out) throws IOException {
        if (currentFormat.indent != null) {
            out.write(currentFormat.lineSeparator);
        }
    }

    /**
     * This will print indents only if indent is not null or the empty string.
     *
     * @param out <code>Writer</code> to use
     * @param level current indent level
     */
    private void indent(Writer out, int level) throws IOException {
        if (currentFormat.indent == null || currentFormat.indent.equals("")) {
            return;
        }
        for (int i = 0; i < level; i++) {
            out.write(currentFormat.indent);
        }
    }

    private int skipLeadingWhite(List content, int start) {
        if (start < 0) {
            start = 0;
        }
        int index = start;
        int size = content.size();
        if (currentFormat.mode == Format.TextMode.TRIM_FULL_WHITE || currentFormat.mode == Format.TextMode.NORMALIZE || currentFormat.mode == Format.TextMode.TRIM) {
            while (index < size) {
                if (!isAllWhitespace(content.get(index))) {
                    return index;
                }
                index++;
            }
        }
        return index;
    }

    private int skipTrailingWhite(List content, int start) {
        int size = content.size();
        if (start > size) {
            start = size;
        }
        int index = start;
        if (currentFormat.mode == Format.TextMode.TRIM_FULL_WHITE || currentFormat.mode == Format.TextMode.NORMALIZE || currentFormat.mode == Format.TextMode.TRIM) {
            while (index >= 0) {
                if (!isAllWhitespace(content.get(index - 1))) break;
                --index;
            }
        }
        return index;
    }

    private static int nextNonText(List content, int start) {
        if (start < 0) {
            start = 0;
        }
        int index = start;
        int size = content.size();
        while (index < size) {
            Object node = content.get(index);
            if (!((node instanceof Text) || (node instanceof EntityRef))) {
                return index;
            }
            index++;
        }
        return size;
    }

    private boolean isAllWhitespace(Object obj) {
        String str = null;
        if (obj instanceof String) {
            str = (String) obj;
        } else if (obj instanceof Text) {
            str = ((Text) obj).getText();
        } else if (obj instanceof EntityRef) {
            return false;
        } else {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if (!Verifier.isXMLWhitespace(str.charAt(i))) return false;
        }
        return true;
    }

    private boolean startsWithWhite(String str) {
        if ((str != null) && (str.length() > 0) && Verifier.isXMLWhitespace(str.charAt(0))) {
            return true;
        }
        return false;
    }

    private boolean endsWithWhite(String str) {
        if ((str != null) && (str.length() > 0) && Verifier.isXMLWhitespace(str.charAt(str.length() - 1))) {
            return true;
        }
        return false;
    }

    /**
     * This will take the pre-defined entities in XML 1.0 and
     * convert their character representation to the appropriate
     * entity reference, suitable for XML attributes.  It does not convert
     * the single quote (') because it's not necessary as the outputter
     * writes attributes surrounded by double-quotes.
     *
     * @param str <code>String</code> input to escape.
     * @return <code>String</code> with escaped content.
     */
    public String escapeAttributeEntities(String str) {
        StringBuffer buffer;
        char ch;
        String entity;
        EscapeStrategy strategy = currentFormat.escapeStrategy;
        buffer = null;
        for (int i = 0; i < str.length(); i++) {
            ch = str.charAt(i);
            switch(ch) {
                case '<':
                    entity = "&lt;";
                    break;
                case '>':
                    entity = "&gt;";
                    break;
                case '\"':
                    entity = "&quot;";
                    break;
                case '&':
                    entity = "&amp;";
                    break;
                case '\r':
                    entity = "&#xD;";
                    break;
                case '\t':
                    entity = "&#x9;";
                    break;
                case '\n':
                    entity = "&#xA;";
                    break;
                default:
                    if (strategy.shouldEscape(ch)) {
                        entity = "&#x" + Integer.toHexString(ch) + ";";
                    } else {
                        entity = null;
                    }
                    break;
            }
            if (buffer == null) {
                if (entity != null) {
                    buffer = new StringBuffer(str.length() + 20);
                    buffer.append(str.substring(0, i));
                    buffer.append(entity);
                }
            } else {
                if (entity == null) {
                    buffer.append(ch);
                } else {
                    buffer.append(entity);
                }
            }
        }
        return (buffer == null) ? str : buffer.toString();
    }

    /**
     * This will take the three pre-defined entities in XML 1.0
     * (used specifically in XML elements) and convert their character
     * representation to the appropriate entity reference, suitable for
     * XML element content.
     *
     * @param str <code>String</code> input to escape.
     * @return <code>String</code> with escaped content.
     */
    public String escapeElementEntities(String str) {
        if (escapeOutput == false) return str;
        StringBuffer buffer;
        char ch;
        String entity;
        EscapeStrategy strategy = currentFormat.escapeStrategy;
        buffer = null;
        for (int i = 0; i < str.length(); i++) {
            ch = str.charAt(i);
            switch(ch) {
                case '<':
                    entity = "&lt;";
                    break;
                case '>':
                    entity = "&gt;";
                    break;
                case '&':
                    entity = "&amp;";
                    break;
                case '\r':
                    entity = "&#xD;";
                    break;
                case '\n':
                    entity = currentFormat.lineSeparator;
                    break;
                default:
                    if (strategy.shouldEscape(ch)) {
                        entity = "&#x" + Integer.toHexString(ch) + ";";
                    } else {
                        entity = null;
                    }
                    break;
            }
            if (buffer == null) {
                if (entity != null) {
                    buffer = new StringBuffer(str.length() + 20);
                    buffer.append(str.substring(0, i));
                    buffer.append(entity);
                }
            } else {
                if (entity == null) {
                    buffer.append(ch);
                } else {
                    buffer.append(entity);
                }
            }
        }
        return (buffer == null) ? str : buffer.toString();
    }

    /**
     * Returns a copy of this XMLOutputter.
     */
    public Object clone() {
        try {
            return super.clone();
        } catch (java.lang.CloneNotSupportedException e) {
            throw new RuntimeException(e.toString());
        }
    }

    /**
     * Return a string listing of the settings for this
     * XMLOutputter instance.
     *
     * @return a string listing the settings for this XMLOutputter instance
     */
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < userFormat.lineSeparator.length(); i++) {
            char ch = userFormat.lineSeparator.charAt(i);
            switch(ch) {
                case '\r':
                    buffer.append("\\r");
                    break;
                case '\n':
                    buffer.append("\\n");
                    break;
                case '\t':
                    buffer.append("\\t");
                    break;
                default:
                    buffer.append("[" + ((int) ch) + "]");
                    break;
            }
        }
        return ("XMLOutputter[omitDeclaration = " + userFormat.omitDeclaration + ", " + "encoding = " + userFormat.encoding + ", " + "omitEncoding = " + userFormat.omitEncoding + ", " + "indent = '" + userFormat.indent + "'" + ", " + "expandEmptyElements = " + userFormat.expandEmptyElements + ", " + "lineSeparator = '" + buffer.toString() + "', " + "textMode = " + userFormat.mode + "]");
    }

    /**
     * Factory for making new NamespaceStack objects.  The NamespaceStack
     * created is actually an inner class extending the package protected
     * NamespaceStack, as a way to make NamespaceStack "friendly" toward
     * subclassers.
     */
    private NamespaceStack createNamespaceStack() {
        return new NamespaceStack();
    }

    /**
     * Our own null subclass of NamespaceStack.  This plays a little
     * trick with Java access protection.  We want subclasses of
     * XMLOutputter to be able to override protected methods that
     * declare a NamespaceStack parameter, but we don't want to
     * declare the parent NamespaceStack class as public.
     */
    protected class NamespaceStack extends org.jdom.output.NamespaceStack {
    }

    private void printQualifiedName(Writer out, Element e) throws IOException {
        if (e.getNamespace().getPrefix().length() == 0) {
            out.write(e.getName());
        } else {
            out.write(e.getNamespace().getPrefix());
            out.write(':');
            out.write(e.getName());
        }
    }

    private void printQualifiedName(Writer out, Attribute a) throws IOException {
        String prefix = a.getNamespace().getPrefix();
        if ((prefix != null) && (!prefix.equals(""))) {
            out.write(prefix);
            out.write(':');
            out.write(a.getName());
        } else {
            out.write(a.getName());
        }
    }
}
