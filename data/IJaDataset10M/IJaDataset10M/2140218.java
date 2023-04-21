package se.mdh.mrtc.saveccm.diagram.edit.policies;

import java.util.Iterator;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gmf.runtime.emf.type.core.commands.DestroyElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;
import se.mdh.mrtc.saveccm.SaveccmPackage;
import se.mdh.mrtc.saveccm.diagram.edit.commands.CombinedIn3CreateCommand;
import se.mdh.mrtc.saveccm.diagram.edit.commands.CombinedOut3CreateCommand;
import se.mdh.mrtc.saveccm.diagram.edit.commands.DataIn3CreateCommand;
import se.mdh.mrtc.saveccm.diagram.edit.commands.DataOut3CreateCommand;
import se.mdh.mrtc.saveccm.diagram.edit.commands.TriggerIn3CreateCommand;
import se.mdh.mrtc.saveccm.diagram.edit.commands.TriggerOut3CreateCommand;
import se.mdh.mrtc.saveccm.diagram.edit.parts.CombinedIn3EditPart;
import se.mdh.mrtc.saveccm.diagram.edit.parts.CombinedOut3EditPart;
import se.mdh.mrtc.saveccm.diagram.edit.parts.DataIn3EditPart;
import se.mdh.mrtc.saveccm.diagram.edit.parts.DataOut3EditPart;
import se.mdh.mrtc.saveccm.diagram.edit.parts.TriggerIn3EditPart;
import se.mdh.mrtc.saveccm.diagram.edit.parts.TriggerOut3EditPart;
import se.mdh.mrtc.saveccm.diagram.part.SaveccmVisualIDRegistry;
import se.mdh.mrtc.saveccm.diagram.providers.SaveccmElementTypes;

/**
 * @generated
 */
public class SwitchItemSemanticEditPolicy extends SaveccmBaseItemSemanticEditPolicy {

    /**
	 * @generated
	 */
    protected Command getCreateCommand(CreateElementRequest req) {
        if (SaveccmElementTypes.TriggerIn_2009 == req.getElementType()) {
            if (req.getContainmentFeature() == null) {
                req.setContainmentFeature(SaveccmPackage.eINSTANCE.getElement_Offer());
            }
            return getGEFWrapper(new TriggerIn3CreateCommand(req));
        }
        if (SaveccmElementTypes.TriggerOut_2010 == req.getElementType()) {
            if (req.getContainmentFeature() == null) {
                req.setContainmentFeature(SaveccmPackage.eINSTANCE.getElement_Offer());
            }
            return getGEFWrapper(new TriggerOut3CreateCommand(req));
        }
        if (SaveccmElementTypes.DataIn_2011 == req.getElementType()) {
            if (req.getContainmentFeature() == null) {
                req.setContainmentFeature(SaveccmPackage.eINSTANCE.getElement_Offer());
            }
            return getGEFWrapper(new DataIn3CreateCommand(req));
        }
        if (SaveccmElementTypes.DataOut_2012 == req.getElementType()) {
            if (req.getContainmentFeature() == null) {
                req.setContainmentFeature(SaveccmPackage.eINSTANCE.getElement_Offer());
            }
            return getGEFWrapper(new DataOut3CreateCommand(req));
        }
        if (SaveccmElementTypes.CombinedIn_2013 == req.getElementType()) {
            if (req.getContainmentFeature() == null) {
                req.setContainmentFeature(SaveccmPackage.eINSTANCE.getElement_Offer());
            }
            return getGEFWrapper(new CombinedIn3CreateCommand(req));
        }
        if (SaveccmElementTypes.CombinedOut_2014 == req.getElementType()) {
            if (req.getContainmentFeature() == null) {
                req.setContainmentFeature(SaveccmPackage.eINSTANCE.getElement_Offer());
            }
            return getGEFWrapper(new CombinedOut3CreateCommand(req));
        }
        return super.getCreateCommand(req);
    }

    /**
	 * @generated
	 */
    protected Command getDestroyElementCommand(DestroyElementRequest req) {
        CompoundCommand cc = getDestroyEdgesCommand();
        addDestroyChildNodesCommand(cc);
        addDestroyShortcutsCommand(cc);
        View view = (View) getHost().getModel();
        if (view.getEAnnotation("Shortcut") != null) {
            req.setElementToDestroy(view);
        }
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
            switch(SaveccmVisualIDRegistry.getVisualID(node)) {
                case TriggerIn3EditPart.VISUAL_ID:
                    cmd.add(getDestroyElementCommand(node));
                    break;
                case TriggerOut3EditPart.VISUAL_ID:
                    cmd.add(getDestroyElementCommand(node));
                    break;
                case DataIn3EditPart.VISUAL_ID:
                    cmd.add(getDestroyElementCommand(node));
                    break;
                case DataOut3EditPart.VISUAL_ID:
                    cmd.add(getDestroyElementCommand(node));
                    break;
                case CombinedIn3EditPart.VISUAL_ID:
                    cmd.add(getDestroyElementCommand(node));
                    break;
                case CombinedOut3EditPart.VISUAL_ID:
                    cmd.add(getDestroyElementCommand(node));
                    break;
            }
        }
    }
}
