package uk.ac.ed.ph.jqtiplus.group;

import uk.ac.ed.ph.jqtiplus.control.JQTIController;
import uk.ac.ed.ph.jqtiplus.control.ToRemove;
import uk.ac.ed.ph.jqtiplus.group.expression.ExpressionGroup;
import uk.ac.ed.ph.jqtiplus.group.test.TestPartGroup;
import uk.ac.ed.ph.jqtiplus.node.XmlNode;
import uk.ac.ed.ph.jqtiplus.validation.Validatable;
import java.io.Serializable;
import java.util.List;
import org.w3c.dom.Element;

/**
 * Container for one node type.
 * <p>
 * For example: {@link TestPartGroup} (group for testParts), {@link ExpressionGroup} (group for expressions).
 * 
 * @author Jiri Kajaba
 */
public interface NodeGroup extends Validatable, Serializable {

    /**
     * Gets parent node of group.
     *
     * @return parent node of group
     */
    XmlNode getParent();

    /**
     * Gets name of group.
     * <p>
     * Name of group is typically QTI class name of its children (if it is same for all children).
     * <p>
     * For example: name of {@link TestPartGroup} is testPart.
     * <p>
     * If group can contain children with different QTI class name, name is display name of abstract parent.
     * <p>
     * For example: name of {@link ExpressionGroup} is expression (expression is not QTI class name for any node).
     *
     * @return name of group
     */
    String getName();

    /**
     * Returns a full (but somewhat pseudo) XPath 2.0 expression that can be used to navigate to this NodeGroup.
     * <p>
     * This is intended for debugging and information purposes rather than anything else. 
     * A fictitious XPath 2.0 function is used to select the abstract QTI class represented by this NodeGroup.
     */
    String computeXPath();

    /**
     * Returns true if group can contain children with different QTI class name; false otherwise.
     *
     * @return true if group can contain children with different QTI class name; false otherwise
     */
    boolean isGeneral();

    /**
     * Returns list of all possible QTI class names (all possible children in this group).
     * <p>
     * This list will not change in time (it contains every possible QTI class name).
     * <p>
     * For example: SectionPartNodegroups returns assessmentSection and assessmentItemRef.
     *
     * @return list of all possible QTI class names (all possible children in this group)
     * @see #getCurrentSupportedClasses
     */
    List<String> getAllSupportedClasses();

    @ToRemove
    List<XmlNode> getChildren();

    /**
     * Gets required minimum number of children or null.
     *
     * @return required minimum number of children or null
     */
    Integer getMinimum();

    /**
     * Gets allowed maximum number of children or null.
     *
     * @return allowed maximum number of children or null
     */
    Integer getMaximum();

    /**
     * Loads children from given source node (DOM).
     * @param JQTIController TODO
     * @param sourceElement source node (DOM)
     */
    void load(JQTIController jqtiController, Element sourceElement);

    /**
     * Creates child with given QTI class name.
     * <p>
     * Parameter classTag is needed only if group can contain children with different QTI class names (otherwise it is ignored).
     * @param classTag QTI class name
     * @return created child
     */
    XmlNode create(String classTag);

    /**
     * Prints all children into string.
     *
     * @param depth depth indent (0 = no indent)
     * @param printDefaultAttributes whether print attribute's default values
     * @return xml string of all children
     */
    String toXmlString(int depth, boolean printDefaultAttributes);
}
