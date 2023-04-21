package uk.ac.ed.ph.jqtiplus.node.item.response.processing;

/**
 * Implementation of IF responseCondition child.
 * 
 * @author Jonathon Hare
 */
public class ResponseIf extends ResponseConditionExpressionChild {

    private static final long serialVersionUID = 1L;

    /** Name of this class in xml schema. */
    public static final String CLASS_TAG = "responseIf";

    /**
     * Constructs IF responseCondition child.
     *
     * @param parent parent of this IF responseCondition child
     */
    public ResponseIf(ResponseCondition parent) {
        super(parent);
    }

    @Override
    public String getClassTag() {
        return CLASS_TAG;
    }
}
