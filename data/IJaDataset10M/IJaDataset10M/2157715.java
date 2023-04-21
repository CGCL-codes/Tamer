package org.musicnotation.gef.editparts.score;

import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.emf.EObjectGraphicalEditPart;
import org.musicnotation.gef.editpolicies.MobMarkComponentEditPolicy;

public abstract class MobMarkEditPart extends EObjectGraphicalEditPart {

    @Override
    protected void createEditPolicies() {
        super.createEditPolicies();
        installEditPolicy(EditPolicy.COMPONENT_ROLE, new MobMarkComponentEditPolicy());
    }
}
