package uk.ac.ed.ph.jqtiplus.group.content.xhtml.table;

import uk.ac.ed.ph.jqtiplus.group.AbstractNodeGroup;
import uk.ac.ed.ph.jqtiplus.node.XmlNode;
import uk.ac.ed.ph.jqtiplus.node.content.BodyElement;
import uk.ac.ed.ph.jqtiplus.node.content.xhtml.table.Tr;
import java.util.List;

/**
 * Group of correctResponse child.
 * 
 * @author Jonathon Hare
 */
public class TrGroup extends AbstractNodeGroup {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs group.
     *
     * @param parent parent of created group
     */
    public TrGroup(BodyElement parent) {
        super(parent, Tr.CLASS_TAG, 1, null);
    }

    /**
     * Gets list of all children.
     *
     * @return list of all children
     */
    @SuppressWarnings("unchecked")
    public List<Tr> getTrs() {
        return (List<Tr>) ((List<? extends XmlNode>) getChildren());
    }

    /**
     * Creates child with given QTI class name.
     * <p>
     * Parameter classTag is needed only if group can contain children with different QTI class names.
     * @param classTag QTI class name (this parameter is ignored)
     * @return created child
     */
    public Tr create(String classTag) {
        return new Tr((BodyElement) getParent());
    }
}
