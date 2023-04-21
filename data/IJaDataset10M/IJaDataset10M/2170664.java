package com.volantis.mcs.wbsax;

import java.io.IOException;
import java.io.Writer;
import java.io.PrintWriter;

/**
 * A filter for WBSAX events which generates a textual description of the 
 * events (including sizing information) being filtered.
 * <p>
 * This class is "transparent", i.e. it has passes the events to it's contained 
 * {@link WBSAXContentHandler} exactly as they arrived, so it may be inserted 
 * into existing WBSAX pipelines to "collect" events for debugging purposes.
 * <p>
 * This was originally written in about an hour on a deadline day and is a bit
 * ugly, but is reasonably functional. Could use a bit more work to make the
 * output a bit more readable.
 * <p>
 * This has now been extended to add sizing information, for all but Opaque
 * element starts and values. This is useful for checking rendering sizes. 
 */
public class WBSAXDisassembler extends WBSAXFilterHandler {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    private static final byte UNPRINTABLE_REPLACEMENT_BYTE = '*';

    /** Where we are outputting our dissasembly information to */
    private Writer out;

    /** Cached string table if it was incomplete at start document time */
    private StringTable stringTable;

    /** Calulated size of the document, so far */
    private int size;

    /** Calculated size of the current event, so far */
    private int increment;

    /**
     * Create a WBSAX disassember which generates output to System.out
     *  
     * @param handler
     */
    public WBSAXDisassembler(WBSAXContentHandler handler) {
        this(handler, new PrintWriter(System.out));
    }

    /**
     * Create a WBSAX disassember which generates output to the destination
     * of your choice.
     *  
     * @param handler
     */
    public WBSAXDisassembler(WBSAXContentHandler handler, Writer out) {
        super(handler, new NullReferenceResolver());
        this.out = out;
    }

    public void startDocument(VersionCode version, PublicIdCode publicId, Codec codec, StringTable stringTable, StringFactory strings) throws WBSAXException {
        print("startDoc(code)", "version=" + toByte(version) + ",publicId=" + publicId.getInteger() + "(" + publicId.getDtd() + ")");
        this.stringTable = stringTable;
        printStringTableStart(stringTable);
        increment(1);
        increment(publicId.getBytes().length);
        increment(codec.getCharset().getBytes().length);
        printSize();
        super.startDocument(version, publicId, codec, stringTable, strings);
    }

    public void startDocument(VersionCode version, StringReference publicId, Codec codec, StringTable stringTable, StringFactory strings) throws WBSAXException {
        print("startDoc(literal)", "version=" + toByte(version) + ", publicId=" + publicId.resolveLogicalIndex() + "(" + publicId.resolveString() + ")");
        this.stringTable = stringTable;
        printStringTableStart(stringTable);
        increment(1);
        increment(publicId.resolvePhysicalIndex().getBytes().length);
        increment(codec.getCharset().getBytes().length);
        printSize();
        super.startDocument(version, publicId, codec, stringTable, strings);
    }

    /**
     * Print the sting table during start document, if possible.
     * 
     * @param stringTable
     * @throws WBSAXException
     */
    private void printStringTableStart(StringTable stringTable) throws WBSAXException {
        if (stringTable != null) {
            if (stringTable.isComplete()) {
                printStringTableImpl(stringTable);
            } else {
                print(null, "(stringTable incomplete, will check again at end)");
                this.stringTable = stringTable;
            }
        } else {
            print(null, "(string table is null)");
        }
    }

    /**
     * Print the string table during end document, if possible.
     * <p>
     * This uses the string table cached during {@link #printStringTableStart}.
     * 
     * @throws WBSAXException
     */
    private void printStringTableEnd() throws WBSAXException {
        if (stringTable != null) {
            if (stringTable.isComplete()) {
                printStringTableImpl(stringTable);
            } else {
                print(null, "ERROR: stringTable is incomplete at end!");
            }
        } else {
            print(null, "(string table was null or complete at start)");
        }
    }

    /**
     * Helper method to print a string table which is non-null and complete.
     * 
     * @param stringTable
     * @throws WBSAXException
     */
    private void printStringTableImpl(StringTable stringTable) throws WBSAXException {
        if (stringTable.size() == 0) {
            print(null, "stringtable present but empty");
            increment(1);
        } else {
            byte[] content = stringTable.getContent();
            int length = content.length;
            byte[] printable = new byte[content.length];
            System.arraycopy(content, 0, printable, 0, content.length);
            for (int i = 0; i < printable.length; i++) {
                char c = (char) printable[i];
                if (c < 32 || c > 127) {
                    printable[i] = UNPRINTABLE_REPLACEMENT_BYTE;
                }
            }
            print(null, "stringtable size=" + length + " content='" + new String(printable) + "'");
            increment(stringTable.length().getBytes().length);
            increment(length);
        }
    }

    public void endDocument() throws WBSAXException {
        print("endDoc", null);
        printStringTableEnd();
        printSize();
        try {
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.endDocument();
    }

    public void startElement(ElementNameCode name, boolean attributes, boolean content) throws WBSAXException {
        print("startElement(code)", "name=" + name.getInteger() + "(" + name.getName() + ")," + "attributes=" + attributes + ",content=" + content);
        increment(1);
        if (attributes) {
            increment(1);
        }
        if (content) {
            increment(1);
        }
        printSize();
        super.startElement(name, attributes, content);
    }

    public void startElement(StringReference name, boolean attributes, boolean content) throws WBSAXException {
        print("startElement(ref)", "ref=" + name.resolveLogicalIndex() + "(" + name.resolveString() + ")," + "attributes=" + attributes + ",content=" + content);
        increment(1);
        increment(name.resolvePhysicalIndex().getBytes().length);
        if (attributes) {
            increment(1);
        }
        if (content) {
            increment(1);
        }
        printSize();
        super.startElement(name, attributes, content);
    }

    public void startElement(OpaqueElementStart magic, boolean content) throws WBSAXException {
        print("startElement(opaque)", "name=" + magic.getName() + "," + "content=" + content);
        printSizeUnknown();
        super.startElement(magic, content);
    }

    public void endElement() throws WBSAXException {
        print("endElement", null);
        super.endElement();
    }

    public void startAttributes() throws WBSAXException {
        print("startAttrs", null);
        super.startAttributes();
    }

    public void addAttribute(AttributeStartCode start) throws WBSAXException {
        print("addAttr(code)", "start=" + start.getInteger() + ":" + start.getName() + ":" + start.getValuePrefix());
        increment(1);
        printSize();
        super.addAttribute(start);
    }

    public void addAttribute(StringReference name) throws WBSAXException {
        print("addAttr(ref)", param(name));
        printSize();
        super.addAttribute(name);
    }

    public void addAttributeValue(AttributeValueCode part) throws WBSAXException {
        print("addAttrValue", "code=" + toByte(part));
        increment(1);
        printSize();
        super.addAttributeValue(part);
    }

    public void addAttributeValue(StringReference part) throws WBSAXException {
        print("addAttrValue", param(part));
        printSize();
        super.addAttributeValue(part);
    }

    public void addAttributeValue(WBSAXString part) throws WBSAXException {
        print("addAttrValue", param(part));
        printSize();
        super.addAttributeValue(part);
    }

    public void addAttributeValueExtension(Extension code) throws WBSAXException {
        print("addAttrValue", param(code));
        printSize();
        super.addAttributeValueExtension(code);
    }

    public void addAttributeValueExtension(Extension code, StringReference value) throws WBSAXException {
        print("addAttrValue", param(code, value));
        printSize();
        super.addAttributeValueExtension(code, value);
    }

    public void addAttributeValueExtension(Extension code, WBSAXString value) throws WBSAXException {
        print("addAttrValue", param(code, value));
        printSize();
        super.addAttributeValueExtension(code, value);
    }

    public void addAttributeValueEntity(EntityCode entity) throws WBSAXException {
        print("addAttrValue", param(entity));
        printSize();
        super.addAttributeValueEntity(entity);
    }

    public void addAttributeValueOpaque(OpaqueValue part) throws WBSAXException {
        print("addAttrValue", param(part));
        printSizeUnknown();
        super.addAttributeValueOpaque(part);
    }

    public void endAttributes() throws WBSAXException {
        print("endAttrs", null);
        super.endAttributes();
    }

    public void startContent() throws WBSAXException {
        print("startContent", null);
        super.startContent();
    }

    public void addContentValue(StringReference part) throws WBSAXException {
        print("addContentValue", param(part));
        printSize();
        super.addContentValue(part);
    }

    public void addContentValue(WBSAXString part) throws WBSAXException {
        print("addContentValue", param(part));
        printSize();
        super.addContentValue(part);
    }

    public void addContentValueExtension(Extension code) throws WBSAXException {
        print("addContentValue", param(code));
        printSize();
        super.addContentValueExtension(code);
    }

    public void addContentValueExtension(Extension code, StringReference value) throws WBSAXException {
        print("addContentValue", param(code, value));
        printSize();
        super.addContentValueExtension(code, value);
    }

    public void addContentValueExtension(Extension code, WBSAXString value) throws WBSAXException {
        print("addContentValue", param(code, value));
        printSize();
        super.addContentValueExtension(code, value);
    }

    public void addContentValueEntity(EntityCode entity) throws WBSAXException {
        print("addContentValue", param(entity));
        printSize();
        super.addContentValueEntity(entity);
    }

    public void addContentValueOpaque(OpaqueValue part) throws WBSAXException {
        print("addContentValue", param(part));
        printSizeUnknown();
        super.addContentValueOpaque(part);
    }

    public void endContent() throws WBSAXException {
        print("endContent", null);
        super.endContent();
    }

    void increment(int increment) {
        this.increment += increment;
    }

    private void printSize() {
        int increment = this.increment;
        this.increment = 0;
        this.size += increment;
        print("              size:", "this:" + increment + " total:" + size);
    }

    private void printSizeUnknown() {
        if (this.increment > 0) {
            throw new IllegalStateException();
        }
        print("              size:", "this: <unknown> total:" + size);
    }

    private String param(StringReference part) throws WBSAXException {
        increment(1 + part.resolvePhysicalIndex().getBytes().length);
        return "ref='" + part.resolveString() + "'";
    }

    private String param(WBSAXString part) throws WBSAXException {
        increment(1 + part.getBytes().length);
        return "string='" + part + "'";
    }

    private String param(Extension code) {
        increment(1);
        return "ext=" + code.intValue();
    }

    private String param(Extension code, StringReference value) throws WBSAXException {
        return param(code) + ":" + param(value);
    }

    private String param(Extension code, WBSAXString value) throws WBSAXException {
        return param(code) + ":" + param(value);
    }

    private String param(EntityCode code) {
        increment(1);
        return "entity=" + code.getInteger();
    }

    private String param(OpaqueValue part) throws WBSAXException {
        return "opaque='" + part + "'";
    }

    private void print(String method, String params) {
        if (method == null) {
            method = "";
        }
        if (params == null) {
            params = "";
        }
        StringBuffer buf = new StringBuffer(method);
        buf.setLength(20);
        for (int i = method.length(); i < 20; i++) {
            buf.setCharAt(i, ' ');
        }
        buf.append(params);
        try {
            out.write(buf.toString());
            out.write('\n');
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String toByte(SingleByteInteger version) {
        return new Integer(version.getInteger()).toString();
    }
}
