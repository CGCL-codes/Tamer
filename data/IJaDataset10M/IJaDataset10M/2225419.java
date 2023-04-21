package astcentric.structure.validation;

import astcentric.structure.basic.FailureType;
import astcentric.structure.basic.Node;
import astcentric.structure.basic.NodeTool;
import astcentric.structure.basic.SimpleValidationFailure;
import astcentric.structure.basic.ValidationResult;

class NoValueNoChildrenValidator extends NoValueValidator {

    static final ExtendedNodeValidator NO_VALUE_NO_CHILDREN_VALIDATOR = new NoValueNoChildrenValidator();

    @Override
    protected ValidationResult validate(Node node, StandardValidationContext context) {
        if (NodeTool.countChildren(node) > 0) {
            return new SimpleValidationFailure(node, FailureType.CHILDREN, "No children expected");
        }
        return super.validate(node, context);
    }
}
