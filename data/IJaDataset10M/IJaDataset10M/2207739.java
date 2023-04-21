package org.codescale.eDependency.diagram.edit.parts;

import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @generated
 */
public class WorkspaceEditPart extends DiagramEditPart {

    /**
     * @generated
     */
    public static final String MODEL_ID = "EDependency";

    /**
     * @generated
     */
    public static final int VISUAL_ID = 79;

    /**
     * @generated
     */
    public WorkspaceEditPart(View view) {
        super(view);
    }

    /**
     * @generated
     */
    protected void createDefaultEditPolicies() {
        super.createDefaultEditPolicies();
        installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE, new org.codescale.eDependency.diagram.edit.policies.WorkspaceItemSemanticEditPolicy());
        installEditPolicy(EditPolicyRoles.CANONICAL_ROLE, new org.codescale.eDependency.diagram.edit.policies.WorkspaceCanonicalEditPolicy());
    }
}
