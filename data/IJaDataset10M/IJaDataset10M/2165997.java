package spoon.aval.annotations.jwsValImpl;

import spoon.aval.Validator;
import spoon.aval.annotations.jwsVal.ValidEndPointInterface;
import spoon.aval.processing.ValidationPoint;
import spoon.processing.Severity;
import spoon.reflect.declaration.CtInterface;
import spoon.reflect.declaration.ModifierKind;

public class ValidEndPointInterfaceValidator implements Validator<ValidEndPointInterface> {

    public void check(ValidationPoint<ValidEndPointInterface> vp) {
        if (vp.getProgramElement() instanceof CtInterface) {
            CtInterface<?> endPointItf = (CtInterface<?>) vp.getProgramElement();
            boolean isPublic = endPointItf.hasModifier(ModifierKind.PUBLIC);
            boolean definesEndPoint = vp.getDslAnnotation().getElementValue("endPointInterface").equals("");
            boolean definesServiceName = vp.getDslAnnotation().getElementValue("serviceName").equals("");
            if (!isPublic) {
                ValidationPoint.report(Severity.ERROR, endPointItf, "An EndPointInterface must be public");
            }
            if (definesEndPoint) {
                ValidationPoint.report(Severity.ERROR, vp.getDslAnnotation(), "An EndPointInterface cannot define the attribute \'endPointInterface\'");
            }
            if (definesServiceName) {
                ValidationPoint.report(Severity.ERROR, vp.getDslAnnotation(), "An EndPointInterface cannot define the attribute \'serviceName\'");
            }
        }
    }
}
