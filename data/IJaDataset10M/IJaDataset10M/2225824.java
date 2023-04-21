package org.parallelj.designer.validation.constraints;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.parallelj.model.Pipeline;
import org.parallelj.model.Procedure;

/**
 * Constraint: Checks if Procedure has at least one outgoing links except the
 * Procedures from Pipeline.
 * 
 */
public class ProcedureOutgoingLinkConstraint extends AbstractModelConstraint {

    @Override
    public IStatus validate(IValidationContext ctx) {
        EObject eObject = ctx.getTarget();
        if (eObject instanceof Procedure) {
            Procedure procedure = (Procedure) eObject;
            if (!(procedure.eContainer() instanceof Pipeline) && (procedure.getOutputLinks() == null || procedure.getOutputLinks().size() == 0)) {
                return ctx.createFailureStatus(eObject.eClass().getName(), procedure.getName());
            }
        }
        return ctx.createSuccessStatus();
    }
}
