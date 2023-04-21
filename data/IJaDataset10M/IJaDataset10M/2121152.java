package org.progeeks.meta.xml;

import java.io.*;
import java.util.*;

/**
 *  <p>Extends the functionality of the IndentPrintWriter to keep
 *  track of tag state and provide attribute support, etc..  Also,
 *  all output is encoded as appropriate for its context.<p>
 *
 *  <p>It is important to note that this class only keeps rudimentary
 *  state and cannot (is not designed to) validate the XML on the way
 *  out.  It is a helper for programmatically generating declarative
 *  XML in a way that's often easier then constructing a full DOM
 *  hierarchy.</p>
 *
 *  @version   $Revision: 1.16 $
 *  @author    Paul Speed
 */
public class XmlPrintWriter extends IndentPrintWriter {

    private List tags = new LinkedList();

    private static final int CTX_BODY = 0;

    private static final int CTX_TAG = 1;

    private static final int CTX_COMMENT = 2;

    private static final int CTX_DATA = 3;

    private static final int CTX_PROCESSING_INSTRUCTION = 4;

    private static final int CTX_DOCTYPE = 5;

    private static final int CTX_DOCTYPE_BLOCK = 6;

    private static final int CTX_MARKUP_DECLARATION = 7;

    /**
     *  Holds the current context mode to keep track of whether
     *  or not we are in a tag, a comment, a CDATA block, or just
     *  normal body text.
     */
    private int context = CTX_BODY;

    /**
     *  The name of the currently open tag or null if there
     *  is no open tag.
     */
    private String openTag = null;

    /**
     *  True if attributes have been written for the current
     *  tag.
     */
    private boolean hasAttributes = false;

    /**
     *  Set to true if the current tag is in single tag
     *  form, ie: <tag />  This is necessary for auto-closing
     *  the tag.
     */
    private boolean singleTag = false;

    /**
     *  Holds a set of attributes that will be automatically applied
     *  whenever the next tag is open.  This allows attributes to be
     *  set before calling some external handler that will actually
     *  write the XML.
     */
    private Map nextAttributes = new LinkedHashMap();

    public XmlPrintWriter(Writer out) {
        super(out);
    }

    /**
     *  Prints the tag start and sets up the indent for nesting.
     */
    public void pushTag(String tag) {
        closeBlock();
        if (!isNewLine()) endLine();
        tags.add(0, tag);
        printBypass("<" + tag);
        context = CTX_TAG;
        openTag = tag;
        printPushedAttributes();
    }

    /**
     *  Prints the specified tag in single-element form, ie: &lt;tag /&gt;
     *  This is different that the normal pushTag() because if another tag
     *  or any text is written then the printed tag will automatically
     *  be closed with the &/gt; instead of nesting the tags or text.
     */
    public void printTag(String tag) {
        closeBlock();
        if (!isNewLine()) endLine();
        printBypass("<" + tag);
        context = CTX_TAG;
        singleTag = true;
        openTag = tag;
        printPushedAttributes();
    }

    /**
     *  Prints the specified processing instruction and opens it for
     *  additional writing with printAttribute() or free form printing.
     *  A processing instruction is of the form:
     *  &lt?target data ?&gt;
     *  A standard processing instruction is the XML or xml target.
     *  &lt?xml version='1.0' encoding='utf-8'?&gt;
     *  To facilitate this kind of construct the printAttribute method
     *  will use single quotes when adding to a processing instruction.
     */
    public void printProcessingInstruction(String target) {
        closeBlock();
        if (!isNewLine()) endLine();
        printBypass("<?" + target + " ");
        context = CTX_PROCESSING_INSTRUCTION;
        pushIndent();
    }

    /**
     *  Closes the current processing instruction block.  In most
     *  cases this is called automatically when a new state is encountered
     *  but it may be necessary in some cases to close it manually.
     */
    public void closeProcessingInstruction() {
        if (context != CTX_PROCESSING_INSTRUCTION) return;
        printlnBypass("?>");
        context = CTX_BODY;
        hasAttributes = false;
        popIndent();
    }

    /**
     *  Writes the specified attribute to the current tag if it is still
     *  open.  Otherwise it will throw an IllegalStateException.  The name
     *  will be verified for XML attribute name restrictions and the
     *  value will be encoded as necessary for attribute values.
     *
     *  @param name The name of the attribute.
     *  @param value The value to be written.  If the value is null then
     *               the attribute will not be written.
     */
    public void printAttribute(String name, String value) {
        if (value == null) return;
        if (context != CTX_TAG && context != CTX_PROCESSING_INSTRUCTION) {
            throw new IllegalStateException("No open tags to receive attributes.");
        }
        value = encodeXml(value, true);
        if (!hasAttributes && context != CTX_PROCESSING_INSTRUCTION) printBypass(" ");
        if (context == CTX_TAG) printBypass(name + "=\"" + value + "\" "); else printBypass(name + "='" + value + "' ");
        hasAttributes = true;
    }

    /**
     *  Adds an attribute to the set of attributes that will be automatically
     *  applied to the next rendered tag.
     *  When the next tag is started either through pushTag or printTag these
     *  accumulated attributes are rendered immediately and the attribute cache
     *  is flushed.
     *  This is useful in cases where tag rendering is going to be delegated to
     *  some secondary class but the caller still requires some attributes to be
     *  set.  This is a general enough mechanism that it is convenient to put it
     *  here.  Places that it comes up often are XmlPropertyRenderers that nest
     *  to other renderers but have to specify ObjectXmlReader directives for
     *  the nested object.
     */
    public void pushAttribute(String name, String value) {
        if (value == null) return;
        nextAttributes.put(name, value);
    }

    /**
     *  Clears all pushed attributes.  This can be used to verify
     *  that all pushed attributes have been processed after calling
     *  out to an external tag.  It's a safety measure for the type
     *  of caller that would get the best use out of pushed attributes.
     *  It is not required in many cases since the tag printing will
     *  automatically clear the attributes.
     */
    public void clearPushedAttributes() {
        nextAttributes.clear();
    }

    /**
     *  Iterates over the accumulated pushed attributes and prints them.
     *  This is called by the various tag start methods.
     */
    protected void printPushedAttributes() {
        for (Iterator i = nextAttributes.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry entry = (Map.Entry) i.next();
            printAttribute((String) entry.getKey(), (String) entry.getValue());
        }
        nextAttributes.clear();
    }

    /**
     *  Prints the closing element for the current tag and pops the indent.
     */
    public String popTag() {
        if (tags.size() == 0) throw new IllegalStateException("Tag stack is empty.");
        String tag = (String) tags.remove(0);
        if (context == CTX_TAG && openTag == tag) {
            singleTag = true;
            closeBlock();
        } else {
            closeBlock();
            popIndent();
            printlnBypass("</" + tag + ">");
        }
        return (tag);
    }

    /**
     *  Prints the closing elements for the stacked tags until the
     *  specified tag is reached and popped.  If the tag is never
     *  found than an illegal state exception is thrown.
     */
    public void popTag(String tag) {
        while (!tag.equals(popTag())) ;
    }

    /**
     *  Prints the XML DOCTYPE declaration.  The publicId and systemLiteral
     *  paramaters are optional but work together to define the external
     *  entity declaration.  If the publicId and systemLiteral are
     *  specified then a PUBLIC external reference is generated.
     *  If only the systemLiteral is specified then a SYSTEM reference
     *  is generated.
     *  <pre>
     *  Example:
     *     startDocType( "root", null, null );
     *  Generates:
     *     &lt;!DOCTYPE root&gt;
     *
     *  Example:
     *     startDocType( "root", "foo.dtd", null );
     *  Generates:
     *     &lt;!DOCTYPE root
     *         SYSTEM "foo.dtd" &gt;
     *
     *  Example:
     *     startDocType( "root", "foo.dtd", "foo" );
     *  Generates:
     *     &lt;!DOCTYPE root
     *         PUBLIC "foo"
     *         "foo.dtd" &gt;
     *  </pre>
     */
    public void startDocType(String root, String systemLiteral, String publicId) {
        closeBlock();
        if (!isNewLine()) endLine();
        tags.add(0, "!DOCTYPE");
        printBypass("<!DOCTYPE " + root);
        pushIndent();
        if (systemLiteral != null) {
            endLine();
            if (publicId != null) {
                printBypass(" PUBLIC \"" + publicId + "\"");
                endLine();
                printBypass("\"" + publicId + "\"");
            } else {
                printBypass(" SYSTEM \"" + systemLiteral + "\"");
            }
        }
        context = CTX_DOCTYPE;
    }

    /**
     *  Closes the open DOCTYPE element.  In most cases this will be done
     *  automatically when a state change is detected.  The notable exception
     *  to this is when there are nested markup declarations that are unclosed.
     *  In this case it is necessary to either close the last markup declaration
     *  or just close the doctype block.  The latter of which is the easier
     *  choice and more appropriate.
     */
    public void closeDocType() {
        if (context == CTX_MARKUP_DECLARATION) closeMarkupDeclaration();
        if (context != CTX_DOCTYPE && context != CTX_DOCTYPE_BLOCK) throw new IllegalStateException("DOCTYPE element not open.");
        popIndent();
        String tag = (String) tags.remove(0);
        if (context == CTX_DOCTYPE_BLOCK) printBypass("]");
        printlnBypass(">");
        context = CTX_BODY;
    }

    /**
     *  Starts a markup declaration such as ENTITY, ATTLIST and so on.
     *  Text within a markup declaration is free-form through this API because
     *  of the myriad of markup declaration types and syntaxes.  They will
     *  be properly nested within an open DOCTYPE.
     */
    public void startMarkupDeclaration(String markup) {
        switch(context) {
            case CTX_DOCTYPE:
                printlnBypass(" [");
                context = CTX_DOCTYPE_BLOCK;
                break;
            case CTX_DOCTYPE_BLOCK:
                break;
            default:
                closeBlock();
                break;
        }
        printBypass("<!ENTITY ");
        pushIndent();
        context = CTX_MARKUP_DECLARATION;
    }

    /**
     *  Closes any open markup declaration.  In most cases this will
     *  be done automatically when a state change is detected.
     */
    public void closeMarkupDeclaration() {
        if (context != CTX_MARKUP_DECLARATION) throw new IllegalStateException("Markup declaration element not open.");
        printlnBypass(">");
        popIndent();
        if ("!DOCTYPE".equals(tags.get(0))) context = CTX_DOCTYPE_BLOCK; else context = CTX_BODY;
    }

    /**
     *  Returns the current tag stack depth.  Useful mostly for debugging
     *  and verification purposes.
     */
    public int getTagStackDepth() {
        return (tags.size());
    }

    /**
     *  Prints the specified string as an enclosed comment.
     */
    public void printComment(String comment) {
        startComment();
        printBypass(comment);
        closeComment();
    }

    /**
     *  Starts a comment block.
     */
    public void startComment() {
        closeBlock();
        printBypass("<!-- ");
        context = CTX_COMMENT;
        pushIndent("     ");
    }

    /**
     *  Closes the comment block.
     */
    public void closeComment() {
        if (context != CTX_COMMENT) return;
        popIndent();
        printlnBypass(" -->");
        context = CTX_BODY;
    }

    /**
     *  Starts a CDATA block.
     */
    public void startDataBlock() {
        closeBlock();
        setOverrideIndent(true);
        printBypass("<![CDATA[");
        context = CTX_DATA;
    }

    /**
     *  Closes a CDATA block.
     */
    public void closeDataBlock() {
        if (context != CTX_DATA) return;
        printBypass("]]>");
        setOverrideIndent(false);
        context = CTX_BODY;
    }

    /**
     *  Closes the current tag or the current comment block
     *  depending on what block is currently open.
     */
    protected void closeBlock() {
        switch(context) {
            default:
            case CTX_BODY:
                break;
            case CTX_TAG:
                closeTag();
                break;
            case CTX_COMMENT:
                closeComment();
                break;
            case CTX_DATA:
                closeDataBlock();
                break;
            case CTX_PROCESSING_INSTRUCTION:
                closeProcessingInstruction();
                break;
            case CTX_DOCTYPE:
            case CTX_DOCTYPE_BLOCK:
                closeDocType();
                break;
            case CTX_MARKUP_DECLARATION:
                closeMarkupDeclaration();
                break;
        }
    }

    /**
     *  Closes the current tag if open.  Otherwise this is a noop.
     *  Note this is not to be confused with popTag() which actually
     *  writes a closing tag in many cases.
     */
    protected void closeTag() {
        if (context != CTX_TAG) return;
        if (!singleTag) printBypass(">"); else if (hasAttributes) printlnBypass("/>"); else printlnBypass(" />");
        if (!singleTag) {
            pushIndent();
        }
        context = CTX_BODY;
        singleTag = false;
        hasAttributes = false;
        openTag = null;
    }

    /**
     *  Automatically finished writing any in-progess tags and then
     *  closes the file.
     */
    public void close() {
        while (tags.size() > 0) popTag();
        super.close();
    }

    /**
     *  Overridden to provide XML encoding when appropriate.
     */
    public void write(int c) {
        closeTag();
        writeEncoded(c);
    }

    public void println() {
        closeTag();
        super.println();
    }

    /**
     *  Overridden to provide XML encoding when appropriate.
     */
    public void write(char[] buff, int off, int len) {
        closeTag();
        switch(context) {
            case CTX_BODY:
            case CTX_COMMENT:
            case CTX_PROCESSING_INSTRUCTION:
            case CTX_DOCTYPE:
            case CTX_DOCTYPE_BLOCK:
            case CTX_MARKUP_DECLARATION:
                for (int i = 0; i < len; i++) {
                    writeEncoded(buff[off + i]);
                }
                return;
            case CTX_TAG:
            case CTX_DATA:
            default:
                break;
        }
        super.write(buff, off, len);
    }

    /**
     *  Overridden to provide XML encoding when appropriate.
     */
    public void write(String s, int off, int len) {
        closeTag();
        switch(context) {
            case CTX_BODY:
            case CTX_COMMENT:
            case CTX_PROCESSING_INSTRUCTION:
            case CTX_DOCTYPE:
            case CTX_DOCTYPE_BLOCK:
            case CTX_MARKUP_DECLARATION:
                for (int i = 0; i < len; i++) {
                    char c = s.charAt(off + i);
                    writeEncoded(c);
                }
                return;
            case CTX_TAG:
            case CTX_DATA:
            default:
                break;
        }
        super.write(s, off, len);
    }

    /**
     *  Bypasses the automatic block termination by sending the data
     *  directly to the super class.
     */
    protected void printBypass(String s) {
        super.write(s, 0, s.length());
    }

    /**
     *  Bypasses the automatic block termination by sending the data
     *  directly to the super class.
     */
    protected void printlnBypass(String s) {
        super.write(s, 0, s.length());
        super.println();
    }

    /**
     *  Returns true if the specified character should be encoded.
     */
    protected boolean isEncodable(int c) {
        if (c == '&') return (true);
        return (false);
    }

    /**
     *  Writes the specified value out as an encoded string if
     *  necessary.
     */
    protected void writeEncoded(int c) {
        if (c == '&') {
            super.write("&amp;", 0, 5);
        } else if (c == '<') {
            super.write("&lt;", 0, 4);
        } else {
            super.write(c);
        }
    }

    /**
     *  Returns an encoded safe string for the specified value.
     *  If quoted is true then it is also safe to encode quotes
     *  and <> signs.
     */
    private static String encodeXml(String value, boolean quoted) {
        value = value.replaceAll("&", "&amp;");
        if (quoted) {
            value = value.replaceAll("\"", "&quot;");
            value = value.replaceAll("'", "&apos;");
            value = value.replaceAll("<", "&lt;");
            value = value.replaceAll(">", "&gt;");
        }
        return (value);
    }

    /**
     *  Used for testing.  Generates example output to test various
     *  features of the writer.
     */
    public static void main(String[] args) throws IOException {
        OutputStreamWriter osw = new OutputStreamWriter(System.out);
        XmlPrintWriter out = new XmlPrintWriter(osw);
        out.printProcessingInstruction("xml");
        out.printAttribute("version", "1.0");
        out.print("encoding='utf-8'");
        out.startDocType("root", "foo.dtd", "test");
        out.startDocType("root", "foo2.dtd", "test2");
        out.startMarkupDeclaration("ENTITY");
        out.print("SYSTEM \"file:data/include-test1.xml\"");
        out.startMarkupDeclaration("ENTITY");
        out.print("SYSTEM \"file:data/include-test2.xml\"");
        out.closeDocType();
        out.startDocType("root", null, null);
        out.startMarkupDeclaration("ENTITY");
        out.print("SYSTEM \"file:data/include-test1.xml\"");
        out.closeDocType();
        out.pushTag("root");
        out.println();
        out.startComment();
        out.println("This is a test of a comment block.");
        out.println("This is the second line of the test.");
        out.println("This line requires encoding the & symbol and the < sign.");
        out.pushTag("firstInner");
        out.printAttribute("test", "A test attribute.");
        out.printTag("single");
        out.printTag("anotherSingle");
        out.printAttribute("foo", "Bar");
        out.printAttribute("bar", "Foo");
        out.pushTag("singleLineExample");
        out.print("Testing.");
        out.popTag();
        out.printProcessingInstruction("test");
        out.println("Some free form processing instructions.");
        out.println("To test things.");
        out.popTag();
        out.println("This is just some body text floating around in the");
        out.println("middle of nowhere.  And it has an & symbol too.");
        out.startDataBlock();
        out.println("This is just some raw data that is not XML at all.");
        out.println("This is how you would include raw data in an XML file.");
        out.pushTag("secondInner");
        out.pushTag("nestedInner");
        out.popTag("secondInner");
        out.startComment();
        out.print("We should be in the root tag now.");
        out.closeComment();
        out.close();
    }
}
