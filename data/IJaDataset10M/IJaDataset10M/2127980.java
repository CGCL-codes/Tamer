package org.parallelj.designer.validation.constraints;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.parallelj.designer.validation.tools.StringTool;
import org.parallelj.model.Condition;
import org.parallelj.model.Procedure;

/**
 * Constraint: A Procedure (or a subclass) or a Condition cannot be named as
 * "end" nor as "begin".
 * 
 */
public class ElementNameConstraint extends AbstractModelConstraint {

    @Override
    public IStatus validate(IValidationContext ctx) {
        EObject target = ctx.getTarget();
        if (target instanceof Procedure) {
            Procedure procedure = (Procedure) target;
            if (!isNameValid(procedure.getName())) {
                return ctx.createFailureStatus(procedure.eClass().getName());
            }
        } else if (target.eClass().getName().equals("Condition")) {
            Condition condition = (Condition) target;
            if (!isNameValid(condition.getName())) {
                return ctx.createFailureStatus(condition.eClass().getName());
            }
        }
        return ctx.createSuccessStatus();
    }

    private boolean isNameValid(String name) {
        if (!StringTool.isEmpty(name) && ("begin".equalsIgnoreCase(name) || "end".equalsIgnoreCase(name))) {
            return false;
        }
        return true;
    }
}
