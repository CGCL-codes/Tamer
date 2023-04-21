package org.hip.kernel.stext;

import org.hip.kernel.sys.VObject;

/**
 * Abstract class with generalized functionality of StructuredTextParagraphs.
 * 
 * @see org.hip.kernel.stext.StructuredTextParagraph
 * @author: Benno Luthiger
 */
public abstract class AbstractStructuredTextParagraph extends VObject {

    private String rawParagraph;

    /**
	 * Constructor for AbstractStructuredTextParagraph.
	 */
    public AbstractStructuredTextParagraph(String inRawParagraph) {
        super();
        rawParagraph = inRawParagraph;
    }

    /**
	 * Returns the raw string of this paragraph.
	 * 
	 * @return java.lang.String
	 */
    public String getRawString() {
        return rawParagraph;
    }

    /**
	 * Returns the type of this paragraph
	 * 
	 * @return int
	 */
    abstract int getParagraphType();

    /**
	 * Checks if this paragraph's type is equal to the specified object's paragraph type.
	 * 
	 * @param inObject java.lang.Object
	 * @return boolean
	 */
    public boolean equalsType(Object inObject) {
        if (inObject == null) return false;
        if (inObject instanceof StructuredTextParagraph) {
            return ((StructuredTextParagraph) inObject).getParagraphType() == getParagraphType();
        } else {
            return false;
        }
    }
}
