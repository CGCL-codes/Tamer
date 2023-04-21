package hub.sam.mof.simulator.editor.diagram.edit.policies;

import hub.sam.mof.simulator.editor.diagram.edit.commands.EAnnotationCreateCommand;
import hub.sam.mof.simulator.editor.diagram.edit.commands.EClassCreateCommand;
import hub.sam.mof.simulator.editor.diagram.edit.commands.EDataTypeCreateCommand;
import hub.sam.mof.simulator.editor.diagram.edit.commands.EEnumCreateCommand;
import hub.sam.mof.simulator.editor.diagram.edit.commands.EPackageCreateCommand;
import hub.sam.mof.simulator.editor.diagram.edit.commands.MActivityCreateCommand;
import hub.sam.mof.simulator.editor.diagram.edit.commands.MClassCreateCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.emf.commands.core.commands.DuplicateEObjectsCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DuplicateElementsRequest;

/**
 * @generated
 */
public class EPackageItemSemanticEditPolicy extends M3ActionsBaseItemSemanticEditPolicy {

    /**
	 * @generated
	 */
    protected Command getCreateCommand(CreateElementRequest req) {
        if (hub.sam.mof.simulator.editor.diagram.providers.M3ActionsElementTypes.EClass_1001 == req.getElementType()) {
            if (req.getContainmentFeature() == null) {
                req.setContainmentFeature(org.eclipse.emf.ecore.EcorePackage.eINSTANCE.getEPackage_EClassifiers());
            }
            return getGEFWrapper(new EClassCreateCommand(req));
        }
        if (hub.sam.mof.simulator.editor.diagram.providers.M3ActionsElementTypes.EPackage_1002 == req.getElementType()) {
            if (req.getContainmentFeature() == null) {
                req.setContainmentFeature(org.eclipse.emf.ecore.EcorePackage.eINSTANCE.getEPackage_ESubpackages());
            }
            return getGEFWrapper(new EPackageCreateCommand(req));
        }
        if (hub.sam.mof.simulator.editor.diagram.providers.M3ActionsElementTypes.EAnnotation_1003 == req.getElementType()) {
            if (req.getContainmentFeature() == null) {
                req.setContainmentFeature(org.eclipse.emf.ecore.EcorePackage.eINSTANCE.getEModelElement_EAnnotations());
            }
            return getGEFWrapper(new EAnnotationCreateCommand(req));
        }
        if (hub.sam.mof.simulator.editor.diagram.providers.M3ActionsElementTypes.EDataType_1004 == req.getElementType()) {
            if (req.getContainmentFeature() == null) {
                req.setContainmentFeature(org.eclipse.emf.ecore.EcorePackage.eINSTANCE.getEPackage_EClassifiers());
            }
            return getGEFWrapper(new EDataTypeCreateCommand(req));
        }
        if (hub.sam.mof.simulator.editor.diagram.providers.M3ActionsElementTypes.EEnum_1005 == req.getElementType()) {
            if (req.getContainmentFeature() == null) {
                req.setContainmentFeature(org.eclipse.emf.ecore.EcorePackage.eINSTANCE.getEPackage_EClassifiers());
            }
            return getGEFWrapper(new EEnumCreateCommand(req));
        }
        if (hub.sam.mof.simulator.editor.diagram.providers.M3ActionsElementTypes.MClass_1006 == req.getElementType()) {
            if (req.getContainmentFeature() == null) {
                req.setContainmentFeature(org.eclipse.emf.ecore.EcorePackage.eINSTANCE.getEPackage_EClassifiers());
            }
            return getGEFWrapper(new MClassCreateCommand(req));
        }
        if (hub.sam.mof.simulator.editor.diagram.providers.M3ActionsElementTypes.MActivity_1007 == req.getElementType()) {
            if (req.getContainmentFeature() == null) {
                req.setContainmentFeature(org.eclipse.emf.ecore.EcorePackage.eINSTANCE.getEPackage_EClassifiers());
            }
            return getGEFWrapper(new MActivityCreateCommand(req));
        }
        return super.getCreateCommand(req);
    }

    /**
	 * @generated
	 */
    protected Command getDuplicateCommand(DuplicateElementsRequest req) {
        TransactionalEditingDomain editingDomain = ((IGraphicalEditPart) getHost()).getEditingDomain();
        return getGEFWrapper(new DuplicateAnythingCommand(editingDomain, req));
    }

    /**
	 * @generated
	 */
    private static class DuplicateAnythingCommand extends DuplicateEObjectsCommand {

        /**
		 * @generated
		 */
        public DuplicateAnythingCommand(TransactionalEditingDomain editingDomain, DuplicateElementsRequest req) {
            super(editingDomain, req.getLabel(), req.getElementsToBeDuplicated(), req.getAllDuplicatedElementsMap());
        }
    }
}
