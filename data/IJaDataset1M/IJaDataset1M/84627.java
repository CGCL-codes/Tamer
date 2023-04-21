package uk.ac.ed.ph.jqtiplus.group.content;

import uk.ac.ed.ph.jqtiplus.node.XmlNode;
import uk.ac.ed.ph.jqtiplus.node.XmlObject;
import uk.ac.ed.ph.jqtiplus.node.content.ContentType;
import uk.ac.ed.ph.jqtiplus.node.content.basic.InlineStatic;
import java.util.List;

/**
 * Group of inlineStatic children.
 * 
 * @author Jonathon Hare
 */
public class InlineStaticGroup extends AbstractContentNodeGroup {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs group.
     *
     * @param parent parent of created group
     */
    public InlineStaticGroup(XmlNode parent) {
        super(parent, InlineStatic.DISPLAY_NAME, null, null);
        getAllSupportedClasses().clear();
        for (ContentType type : ContentType.inlineStaticValues()) getAllSupportedClasses().add(type.getClassTag());
    }

    @Override
    public XmlObject getParent() {
        return (XmlObject) super.getParent();
    }

    @Override
    public boolean isGeneral() {
        return true;
    }

    /**
     * Gets child.
     *
     * @return child
     * @see #setInlineStatic
     */
    public InlineStatic getInlineStatic() {
        return (getChildren().size() != 0) ? (InlineStatic) getChildren().get(0) : null;
    }

    /**
     * Sets new child.
     *
     * @param inline new child
     * @see #getInlineStatic
     */
    public void setInlineStatic(InlineStatic inline) {
        getChildren().clear();
        getChildren().add(inline);
    }

    /**
     * Gets list of all children.
     *
     * @return list of all children
     */
    @SuppressWarnings("unchecked")
    public List<InlineStatic> getInlineStatics() {
        return (List<InlineStatic>) ((List<? extends XmlNode>) getChildren());
    }

    /**
     * Creates child with given QTI class name.
     * <p>
     * Parameter classTag is needed only if group can contain children with different QTI class names.
     * @param classTag QTI class name (this parameter is needed)
     * @return created child
     */
    public InlineStatic create(String classTag) {
        return ContentType.getInlineStaticInstance(getParent(), classTag);
    }
}
