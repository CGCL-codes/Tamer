package org.hip.kernel.stext;

/**
 * This class creates output escaped html code by serializing a StructuredText.
 * 
 * @see org.hip.kernel.stext.StructuredText
 * @author: Benno Luthiger
 */
public class EscapedHTMLSerializer extends AbstractHTMLSerializer {

    /**
	 * Constructor for EscapedHTMLSerializer.
	 */
    public EscapedHTMLSerializer() {
        super();
    }

    /**
	 * Emits a start tag which is output escaped.
	 * 
	 * @param inContent java.lang.String
	 */
    protected synchronized void emitHTMLStartTag(String inContent) {
        emit("&lt;" + inContent + "&gt;");
    }

    /**
	 * Emits an end tag which is output escaped.
	 * 
	 * @param inContent java.lang.String
	 */
    protected synchronized void emitHTMLEndTag(String inContent) {
        emit("&lt;/" + inContent + "&gt;");
    }

    protected StringBuffer getConverted(String inRawString) {
        return InlineStructuredText2HTML.getSingleton().convertToEscapedHTML(inRawString);
    }
}
