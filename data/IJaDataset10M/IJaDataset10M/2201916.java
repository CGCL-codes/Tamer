package cz.vse.gebz.diagram.edit.policies;

import java.util.Iterator;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gmf.runtime.emf.type.core.commands.DestroyElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;
import cz.vse.gebz.diagram.edit.parts.NumerickyAtribalniVyrokEditPart;
import cz.vse.gebz.diagram.edit.parts.NumerickyAtributNumerickyAtributCompartment3EditPart;
import cz.vse.gebz.diagram.part.BzVisualIDRegistry;

/**
 * @generated
 */
public class NumerickyAtribut3ItemSemanticEditPolicy extends BzBaseItemSemanticEditPolicy {

    /**
	 * @generated
	 */
    protected Command getDestroyElementCommand(DestroyElementRequest req) {
        CompoundCommand cc = getDestroyEdgesCommand();
        addDestroyChildNodesCommand(cc);
        addDestroyShortcutsCommand(cc);
        cc.add(getGEFWrapper(new DestroyElementCommand(req)));
        return cc.unwrap();
    }

    /**
	 * @generated
	 */
    protected void addDestroyChildNodesCommand(CompoundCommand cmd) {
        View view = (View) getHost().getModel();
        EAnnotation annotation = view.getEAnnotation("Shortcut");
        if (annotation != null) {
            return;
        }
        for (Iterator it = view.getChildren().iterator(); it.hasNext(); ) {
            Node node = (Node) it.next();
            switch(BzVisualIDRegistry.getVisualID(node)) {
                case NumerickyAtributNumerickyAtributCompartment3EditPart.VISUAL_ID:
                    for (Iterator cit = node.getChildren().iterator(); cit.hasNext(); ) {
                        Node cnode = (Node) cit.next();
                        switch(BzVisualIDRegistry.getVisualID(cnode)) {
                            case NumerickyAtribalniVyrokEditPart.VISUAL_ID:
                                cmd.add(getDestroyElementCommand(cnode));
                                break;
                        }
                    }
                    break;
            }
        }
    }
}
