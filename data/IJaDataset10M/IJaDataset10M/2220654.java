package uk.ac.ed.ph.jqtiplus.group.item.response.processing;

import uk.ac.ed.ph.jqtiplus.group.AbstractNodeGroup;
import uk.ac.ed.ph.jqtiplus.node.item.response.processing.ResponseCondition;
import uk.ac.ed.ph.jqtiplus.node.item.response.processing.ResponseElse;

/**
 * Group of responseElse child.
 * 
 * @author Jonathon Hare
 */
public class ResponseElseGroup extends AbstractNodeGroup {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs group.
     *
     * @param parent parent of created group
     */
    public ResponseElseGroup(ResponseCondition parent) {
        super(parent, ResponseElse.CLASS_TAG, false);
    }

    /**
     * Gets child.
     *
     * @return child
     * @see #setResponseElse
     */
    public ResponseElse getResponseElse() {
        return (ResponseElse) getChild();
    }

    /**
     * Sets new child.
     *
     * @param responseElse new child
     * @see #getResponseElse
     */
    public void setResponseElse(ResponseElse responseElse) {
        setChild(responseElse);
    }

    /**
     * Creates child with given QTI class name.
     * <p>
     * Parameter classTag is needed only if group can contain children with different QTI class names.
     * @param classTag QTI class name (this parameter is ignored)
     * @return created child
     */
    public ResponseElse create(String classTag) {
        return new ResponseElse((ResponseCondition) getParent());
    }
}
