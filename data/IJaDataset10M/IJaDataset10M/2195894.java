package org.spbu.pldoctoolkit.graph.diagram.infproduct.edit.parts;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.Graphics;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ComponentEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.draw2d.ui.figures.PolylineConnectionEx;
import org.eclipse.gmf.runtime.notation.View;
import org.spbu.pldoctoolkit.graph.diagram.infproduct.edit.policies.GenericDocumentPartGroupsItemSemanticEditPolicy;

/**
 * @generated
 */
public class GenericDocumentPartGroupsEditPart extends ConnectionNodeEditPart {

    /**
	 * @generated
	 */
    public static final int VISUAL_ID = 3002;

    /**
	 * @generated
	 */
    public GenericDocumentPartGroupsEditPart(View view) {
        super(view);
    }

    /**
	 * @generated NOT
	 */
    protected void createDefaultEditPolicies() {
        super.createDefaultEditPolicies();
        installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE, new GenericDocumentPartGroupsItemSemanticEditPolicy());
        installEditPolicy(EditPolicy.COMPONENT_ROLE, new ComponentEditPolicy() {

            @Override
            protected Command createDeleteSemanticCommand(GroupRequest deleteRequest) {
                return UnexecutableCommand.INSTANCE;
            }

            @Override
            protected Command createDeleteViewCommand(GroupRequest deleteRequest) {
                return UnexecutableCommand.INSTANCE;
            }
        });
    }

    /**
	 * Creates figure for this edit part.
	 * 
	 * Body of this method does not depend on settings in generation model
	 * so you may safely remove <i>generated</i> tag and modify it.
	 * 
	 * @generated
	 */
    protected Connection createConnectionFigure() {
        return new InfElemRefGroupConnectionFigure();
    }

    /**
	 * @generated
	 */
    public class InfElemRefGroupConnectionFigure extends PolylineConnectionEx {

        /**
		 * @generated
		 */
        public InfElemRefGroupConnectionFigure() {
            this.setFill(true);
            this.setFillXOR(false);
            this.setOutline(true);
            this.setOutlineXOR(false);
            this.setLineWidth(1);
            this.setLineStyle(Graphics.LINE_DASH);
        }
    }
}
