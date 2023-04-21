package spoon.aval.annotations.jwsValImpl;

import spoon.aval.Validator;
import spoon.aval.annotations.jwsVal.ValidWebOperation;
import spoon.aval.processing.ValidationPoint;
import spoon.processing.Severity;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.ModifierKind;

public class ValidWebOperationValidator implements Validator<ValidWebOperation> {

    public void check(ValidationPoint<ValidWebOperation> vp) {
        if (vp.getProgramElement() instanceof CtMethod) {
            CtMethod<?> webOp = (CtMethod<?>) vp.getProgramElement();
            if (!webOp.hasModifier(ModifierKind.PUBLIC)) {
                ValidationPoint.report(Severity.ERROR, webOp, "WebMethods should be public");
            }
        }
    }
}
