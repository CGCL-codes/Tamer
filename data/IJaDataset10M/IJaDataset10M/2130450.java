package uk.ac.ed.ph.jqtiplus.node;

import uk.ac.ed.ph.jqtiplus.attribute.value.IdentifierAttribute;
import uk.ac.ed.ph.jqtiplus.group.NodeGroup;
import uk.ac.ed.ph.jqtiplus.group.NodeGroupList;
import uk.ac.ed.ph.jqtiplus.node.item.AssessmentItem;
import uk.ac.ed.ph.jqtiplus.node.result.AssessmentResult;
import uk.ac.ed.ph.jqtiplus.node.test.AssessmentTest;
import uk.ac.ed.ph.jqtiplus.node.test.BranchRule;
import uk.ac.ed.ph.jqtiplus.types.Identifier;
import uk.ac.ed.ph.jqtiplus.validation.AttributeValidationError;
import uk.ac.ed.ph.jqtiplus.validation.ValidationResult;

/**
 * Parent of all xml objects.
 * 
 * @author Jiri Kajaba
 * @author Jonathon Hare
 */
public abstract class AbstractObject extends AbstractNode implements XmlObject {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs object.
     *
     * @param parent parent of constructed object (can be null for root objects)
     */
    public AbstractObject(XmlObject parent) {
        super(parent);
    }

    @Override
    public XmlObject getParent() {
        return (XmlObject) super.getParent();
    }

    public AssessmentTest getParentTest() {
        XmlNode root = getParentRoot();
        if (root instanceof AssessmentTest) return (AssessmentTest) root;
        return null;
    }

    public AssessmentItem getParentItem() {
        XmlNode root = getParentRoot();
        if (root instanceof AssessmentItem) return (AssessmentItem) root;
        return null;
    }

    public AssessmentResult getParentResult() {
        XmlNode root = getParentRoot();
        if (root instanceof AssessmentResult) return (AssessmentResult) root;
        return null;
    }

    /** Helper method to validate a unique identifier (definition) attribute */
    protected void validateUniqueIdentifier(ValidationResult result, IdentifierAttribute identifierAttribute, Identifier identifier) {
        if (identifier != null) {
            if (getParentTest() != null && BranchRule.isSpecial(identifier.toString())) {
                result.add(new AttributeValidationError(identifierAttribute, "Cannot uses this special target as identifier: " + identifierAttribute));
            }
            if (!validateUniqueIdentifier(getParentRoot(), identifier)) {
                result.add(new AttributeValidationError(identifierAttribute, "Duplicate identifier: " + identifierAttribute));
            }
        }
    }

    private boolean validateUniqueIdentifier(XmlNode parent, Object identifier) {
        if (parent != this && parent instanceof UniqueNode) {
            Object parentIdentifier = ((UniqueNode<?>) parent).getIdentifier();
            if (identifier.equals(parentIdentifier)) {
                return false;
            }
        }
        NodeGroupList groups = parent.getNodeGroups();
        for (int i = 0; i < groups.size(); i++) {
            NodeGroup group = groups.get(i);
            for (XmlNode child : group.getChildren()) if (!validateUniqueIdentifier(child, identifier)) {
                return false;
            }
        }
        return true;
    }
}
