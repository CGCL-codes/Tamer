package org.parallelj.designer.validation.constraints;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.parallelj.designer.validation.tools.StringTool;
import org.parallelj.model.Element;

/**
 * Constraint: Element name should comprise alpha-numeric pattern and underscore.
 * 
 */
public class ElementNamePatternConstraint extends AbstractModelConstraint {

    @Override
    public IStatus validate(IValidationContext ctx) {
        EObject target = ctx.getTarget();
        if (target instanceof Element && !target.eClass().getName().equals("Program")) {
            Element element = (Element) target;
            if (!StringTool.isNameQualified(element.getName(), false)) {
                return ctx.createFailureStatus(element.eClass().getName());
            }
        }
        return ctx.createSuccessStatus();
    }
}
