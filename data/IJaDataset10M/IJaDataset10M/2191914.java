package org.qtitools.qti.node.item.response.processing;

import org.qtitools.qti.exception.QTIProcessingInterrupt;
import org.qtitools.qti.node.XmlObject;

/**
 * The exit response rule terminates response processing immediately (for this invocation).
 * 
 * @author Jonathon Hare
 *
 */
public class ExitResponse extends ResponseRule {

    private static final long serialVersionUID = 1L;

    /** Name of this class in xml schema. */
    public static final String CLASS_TAG = "exitResponse";

    /**
	 * Constructs object.
	 *
	 * @param parent parent of constructed object
	 */
    public ExitResponse(XmlObject parent) {
        super(parent);
    }

    @Override
    public void evaluate() throws QTIProcessingInterrupt {
        throw new QTIProcessingInterrupt();
    }

    @Override
    public String getClassTag() {
        return CLASS_TAG;
    }
}
