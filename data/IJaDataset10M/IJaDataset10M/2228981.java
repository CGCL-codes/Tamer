package org.qtitools.qti.node.content.xhtml.presentation;

import org.qtitools.qti.node.XmlObject;
import org.qtitools.qti.node.content.basic.AbstractSimpleInline;
import org.qtitools.qti.node.content.basic.SimpleInline;

/**
 * i
 * 
 * @author Jonathon Hare
 *
 */
public class I extends AbstractSimpleInline implements SimpleInline {

    private static final long serialVersionUID = 1L;

    /** Name of this class in xml schema. */
    public static String CLASS_TAG = "i";

    /**
	 * Constructs object.
	 *
	 * @param parent parent of constructed object
	 */
    public I(XmlObject parent) {
        super(parent);
    }

    @Override
    public String getClassTag() {
        return CLASS_TAG;
    }
}
