package org.imogene.model.validation.constraints;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.validation.IValidationContext;
import org.imogene.model.core.Project;
import org.imogene.model.core.Role;

public class DuplicateRoleName extends AbstractMedanyModelConstraint {

    public IStatus validate(IValidationContext ctx) {
        List<String> roleNames = new Vector<String>();
        try {
            if (ctx.getTarget() instanceof Project) {
                Project project = (Project) ctx.getTarget();
                for (Iterator<Role> itr = project.getRoles().iterator(); itr.hasNext(); ) {
                    Role role = itr.next();
                    if (role.getName() != null) {
                        if (roleNames.contains(role.getName())) {
                            return ctx.createFailureStatus(new Object[] { getErrorMessage(role.getName()) });
                        } else {
                            roleNames.add(role.getName());
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return ctx.createSuccessStatus();
        }
        return ctx.createSuccessStatus();
    }

    /** return the error message */
    private String getErrorMessage(String roleName) {
        return new String("Two roles have the same name \"" + roleName + "\".");
    }
}
