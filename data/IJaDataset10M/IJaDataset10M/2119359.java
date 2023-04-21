package com.volantis.mcs.wbsax.io;

import com.volantis.mcs.wbsax.Codec;
import com.volantis.mcs.wbsax.Extension;
import com.volantis.mcs.wbsax.PublicIdCode;
import com.volantis.mcs.wbsax.StringReference;
import com.volantis.mcs.wbsax.StringTable;
import com.volantis.mcs.wbsax.VersionCode;
import com.volantis.mcs.wbsax.WBSAXException;
import com.volantis.mcs.wbsax.WBSAXString;
import com.volantis.mcs.wbsax.StringFactory;
import java.io.Writer;
import java.io.IOException;

/**
 * WMLProducer. Turn WBSAX events into WML
 */
public class WMLProducer extends XMLProducer {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * Create a XMLProducer
     *
     * @param out the Writer to write to
     */
    public WMLProducer(Writer out, Writer encoded) {
        super(out, encoded);
    }

    /**
     * Output a string to the content of the attribute
     * 
     * @param part a reference to the string holding the attribute
     * 
     * @throws WBSAXException if an output error occurs
     */
    public void addAttributeValue(StringReference part) throws WBSAXException {
        output(part.resolveString().getString());
    }

    /**
     * Output a string to the content of the attribute
     * 
     * @param part a string holding the attribute
     * 
     * @throws WBSAXException if an output error occurs
     */
    public void addAttributeValue(WBSAXString part) throws WBSAXException {
        output(part.getString());
    }

    /**
     * Write an extended attribute value
     *
     * @param code the extension code
     * @param part the value of the attribute
     *
     * @throws WBSAXException if an output error occurs
     */
    public void addAttributeValueExtension(Extension code, WBSAXString part) throws WBSAXException {
        addContentValueExtension(code, part);
    }

    /**
     * Write an extended attribute value
     *
     * @param code the extension code
     * @param part a reference to the value of the attribute
     *
     * @throws WBSAXException if an output error occurs
     */
    public void addAttributeValueExtension(Extension code, StringReference part) throws WBSAXException {
        addContentValueExtension(code, part);
    }

    /**
     * Output an extended string to the content of the element
     *
     * @param code the extension code
     * @param part a string holding the content
     *
     * @throws WBSAXException if an output error occurs
     */
    public void addContentValueExtension(Extension code, WBSAXString part) throws WBSAXException {
        try {
            if (code == Extension.ZERO) {
                out.write("$(");
                output(part.getString());
                out.write(":e)");
            } else if (code == Extension.ONE) {
                out.write("$(");
                output(part.getString());
                out.write(":u)");
            } else if (code == Extension.TWO) {
                out.write("$(");
                output(part.getString());
                out.write(")");
            } else {
                throw new UnsupportedOperationException("Document Extension (" + String.valueOf(code.intValue()) + ") not supported");
            }
        } catch (IOException e) {
            throw new WBSAXException(e);
        }
    }

    /**
     * Output an extended string to the content of the element
     *
     * @param code the extension code
     * @param part a reference to the string holding the content
     *
     * @throws WBSAXException if an output error occurs
     */
    public void addContentValueExtension(Extension code, StringReference part) throws WBSAXException {
        try {
            if (code == Extension.ZERO) {
                out.write("$(");
                output(part.resolveString().getString());
                out.write(":e)");
            } else if (code == Extension.ONE) {
                out.write("$(");
                output(part.resolveString().getString());
                out.write(":u)");
            } else if (code == Extension.TWO) {
                out.write("$(");
                output(part.resolveString().getString());
                out.write(")");
            } else {
                throw new UnsupportedOperationException("Document Extension (" + String.valueOf(code.intValue()) + ") not supported");
            }
        } catch (IOException e) {
            throw new WBSAXException(e);
        }
    }

    /**
     * Start a new document.
     *
     * @param version the WBXML version code
     * @param publicId holds the code of the document type
     * @param charset which character set is being used
     * @param stringTable the document string table
     *
     * @throws WBSAXException if there is an error writing the header
     */
    public void startDocument(VersionCode version, PublicIdCode publicId, Codec charset, StringTable stringTable, StringFactory strings) throws WBSAXException {
        try {
            super.startDocument(version, publicId, charset, stringTable, strings);
            if (publicId.getName() != null) {
                out.write("<!DOCTYPE wml PUBLIC \"");
                output(publicId.getName());
                out.write("\" \"");
                output(publicId.getDtd());
                out.write("\">");
            }
        } catch (IOException e) {
            throw new WBSAXException(e);
        }
    }

    /**
     * Start a new document.
     *
     * @param version the WBXML version code
     * @param publicId a reference to a string holding the public id
     * @param charset which character set is being used
     * @param stringTable the document string table
     *
     * @throws WBSAXException if there is an error writing the header
     * 
     * @todo The document is only passed a PUBLIC ID, there is no DTD
     * specified. The DTD needs to be passed and handled.
     */
    public void startDocument(VersionCode version, StringReference publicId, Codec charset, StringTable stringTable, StringFactory strings) throws WBSAXException {
        try {
            super.startDocument(version, publicId, charset, stringTable, strings);
            out.write("<!DOCTYPE wml PUBLIC \"");
            output(publicId.resolveString().getString());
            out.write("\" >");
        } catch (IOException e) {
            throw new WBSAXException(e);
        }
    }

    /**
     * Output a string to the content of the element
     *
     * @param part a reference to the string holding the content
     *
     * @throws WBSAXException if an output error occurs
     */
    public void addContentValue(StringReference part) throws WBSAXException {
        output(part.resolveString().getString());
    }

    /**
     * Output a string to the content of the element
     *
     * @param part a string holding the content
     *
     * @throws WBSAXException if an output error occurs
     */
    public void addContentValue(WBSAXString part) throws WBSAXException {
        output(part.getString());
    }

    /**
     * Output a string but double any '$' characters.
     * '$' characters that get through to here are in the content of a
     * WML element and are not variables. ( If they were variables they
     * would be written via the addContentValueExtension() methods )
     * We therefore double any '$' characters so the WML device displays
     * them as single '$' characters.
     * @param str the string to write.
     */
    private void output(String str) throws WBSAXException {
        try {
            char chr;
            for (int i = 0; i < str.length(); i++) {
                chr = str.charAt(i);
                if (chr == '$') {
                    enc.write("$$");
                } else {
                    enc.write(chr);
                }
            }
        } catch (IOException e) {
            throw new WBSAXException(e);
        }
    }
}
