package com.volantis.mcs.wbsax.io;

import com.volantis.mcs.wbsax.WBSAXTerminalHandler;
import com.volantis.mcs.wbsax.VersionCode;
import com.volantis.mcs.wbsax.PublicIdCode;
import com.volantis.mcs.wbsax.Codec;
import com.volantis.mcs.wbsax.StringTable;
import com.volantis.mcs.wbsax.StringFactory;
import com.volantis.mcs.wbsax.WBSAXException;
import com.volantis.mcs.wbsax.StringReference;
import com.volantis.mcs.wbsax.ElementNameCode;
import com.volantis.mcs.wbsax.OpaqueElementStart;
import com.volantis.mcs.wbsax.AttributeStartCode;
import com.volantis.mcs.wbsax.AttributeValueCode;
import com.volantis.mcs.wbsax.WBSAXString;
import com.volantis.mcs.wbsax.Extension;
import com.volantis.mcs.wbsax.EntityCode;
import com.volantis.mcs.wbsax.OpaqueValue;
import java.io.PrintWriter;
import java.io.Writer;

/**
 * A simple producer which serialises to a simple debugging string format.
 * <p>
 * This can then be used in test cases to compare against a simple string
 * of expected results.
 * <p>
 * This is useful for testing anything which generates WBSAX events, such
 * as WBSAX filters.
 * 
 * @todo this class can be used to replace 
 * {@link com.volantis.mcs.wbsax.EnumeratedWBSAXContentHandler} and
 * {@link com.volantis.mcs.wbsax.TestWBSAXContentHandler}
 * as it is easier to use.
 * 
 * @todo implement the document, extension and entity methods to make this
 *      complete.
 * 
 * @todo currently this just outputs a text mode debug output, that is, when
 *      outputting an object which has a text and code component it only 
 *      outputs the text part (eg the name of an element rather than the code).
 *      In future we might like to extend this class to allow text, binary
 *      or combined output.
 */
public class TestDebugProducer extends WBSAXTerminalHandler {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    private PrintWriter out;

    public TestDebugProducer(Writer out) {
        this.out = new PrintWriter(out);
    }

    public void startDocument(VersionCode version, PublicIdCode publicId, Codec codec, StringTable stringTable, StringFactory strings) throws WBSAXException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public void startDocument(VersionCode version, StringReference publicId, Codec codec, StringTable stringTable, StringFactory strings) throws WBSAXException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public void endDocument() throws WBSAXException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public void startElement(ElementNameCode name, boolean attributes, boolean content) throws WBSAXException {
        out.print("[se:" + "(c:" + name.getName() + ")" + "," + attributes + "," + content + "]");
    }

    public void startElement(StringReference name, boolean attributes, boolean content) throws WBSAXException {
        out.print("[se:" + reference(name) + "," + attributes + "," + content + "]");
    }

    public void startElement(OpaqueElementStart element, boolean content) throws WBSAXException {
        out.print("[se:" + "(o:" + element.getName() + ")" + "," + content + "]");
    }

    public void endElement() throws WBSAXException {
        out.print("[ee]");
    }

    public void startAttributes() throws WBSAXException {
        out.print("[sa]");
    }

    public void addAttribute(AttributeStartCode start) throws WBSAXException {
        out.print("[aa:" + "(c:" + start.getName() + "," + start.getValuePrefix() + ")" + "]");
    }

    public void addAttribute(StringReference name) throws WBSAXException {
        out.print("[aa:" + reference(name) + "]");
    }

    public void addAttributeValue(AttributeValueCode part) throws WBSAXException {
        out.print("[av:" + "(c:" + part.getValue() + ")" + "]");
    }

    public void addAttributeValue(StringReference part) throws WBSAXException {
        out.print("[av:" + reference(part) + "]");
    }

    public void addAttributeValue(WBSAXString part) throws WBSAXException {
        out.print("[av:" + string(part) + "]");
    }

    public void addAttributeValueExtension(Extension code) throws WBSAXException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public void addAttributeValueExtension(Extension code, StringReference value) throws WBSAXException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public void addAttributeValueExtension(Extension code, WBSAXString value) throws WBSAXException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public void addAttributeValueEntity(EntityCode entity) throws WBSAXException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public void addAttributeValueOpaque(OpaqueValue part) throws WBSAXException {
        out.print("[av:" + opaque(part) + "]");
    }

    public void endAttributes() throws WBSAXException {
        out.print("[ea]");
    }

    public void startContent() throws WBSAXException {
        out.print("[sc]");
    }

    public void addContentValue(StringReference part) throws WBSAXException {
        out.print("[cv:" + reference(part) + "]");
    }

    public void addContentValue(WBSAXString part) throws WBSAXException {
        out.print("[cv:" + string(part) + "]");
    }

    public void addContentValueExtension(Extension code) throws WBSAXException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public void addContentValueExtension(Extension code, StringReference value) throws WBSAXException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public void addContentValueExtension(Extension code, WBSAXString value) throws WBSAXException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public void addContentValueEntity(EntityCode entity) throws WBSAXException {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public void addContentValueOpaque(OpaqueValue part) throws WBSAXException {
        out.print("[cv:" + opaque(part) + "]");
    }

    public void endContent() throws WBSAXException {
        out.print("[ec]");
    }

    private String string(WBSAXString value) throws WBSAXException {
        return "(s:" + value.getString() + ")";
    }

    private String reference(StringReference value) throws WBSAXException {
        return "(r:" + value.resolveString().getString() + ")";
    }

    private String opaque(OpaqueValue value) throws WBSAXException {
        String s = null;
        try {
            s = value.getString();
        } catch (Exception e) {
            s = "{exception}";
        }
        return "(o:" + s + ")";
    }
}
