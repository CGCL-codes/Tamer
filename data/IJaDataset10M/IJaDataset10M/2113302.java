package odm.diagram.edit.policies;

import odm.OWL;
import odm.OdmPackage;
import odm.diagram.providers.OdmElementTypes;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.emf.commands.core.commands.DuplicateEObjectsCommand;
import org.eclipse.gmf.runtime.emf.type.core.commands.CreateElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DuplicateElementsRequest;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @generated
 */
public class OWLItemSemanticEditPolicy extends OdmBaseItemSemanticEditPolicy {

    /**
	 * @generated
	 */
    protected Command getCreateCommand(CreateElementRequest req) {
        if (OdmElementTypes.OWLClass_2001 == req.getElementType()) {
            if (req.getContainmentFeature() == null) {
                req.setContainmentFeature(OdmPackage.eINSTANCE.getOWL_OWLClass());
            }
            return getMSLWrapper(new CreateOWLClass_2001Command(req));
        }
        if (OdmElementTypes.DataProperty_2002 == req.getElementType()) {
            if (req.getContainmentFeature() == null) {
                req.setContainmentFeature(OdmPackage.eINSTANCE.getOWL_DataProperty());
            }
            return getMSLWrapper(new CreateDataProperty_2002Command(req));
        }
        if (OdmElementTypes.ObjectProperty_2003 == req.getElementType()) {
            if (req.getContainmentFeature() == null) {
                req.setContainmentFeature(OdmPackage.eINSTANCE.getOWL_ObjectProperty());
            }
            return getMSLWrapper(new CreateObjectProperty_2003Command(req));
        }
        if (OdmElementTypes.MainOntology_2004 == req.getElementType()) {
            OWL container = (OWL) (req.getContainer() instanceof View ? ((View) req.getContainer()).getElement() : req.getContainer());
            if (container.getMainOntology() != null) {
                return super.getCreateCommand(req);
            }
            if (req.getContainmentFeature() == null) {
                req.setContainmentFeature(OdmPackage.eINSTANCE.getOWL_MainOntology());
            }
            return getMSLWrapper(new CreateMainOntology_2004Command(req));
        }
        if (OdmElementTypes.OtherOntology_2005 == req.getElementType()) {
            if (req.getContainmentFeature() == null) {
                req.setContainmentFeature(OdmPackage.eINSTANCE.getOWL_OtherOntology());
            }
            return getMSLWrapper(new CreateOtherOntology_2005Command(req));
        }
        if (OdmElementTypes.DataRange_2006 == req.getElementType()) {
            if (req.getContainmentFeature() == null) {
                req.setContainmentFeature(OdmPackage.eINSTANCE.getOWL_DataRange());
            }
            return getMSLWrapper(new CreateDataRange_2006Command(req));
        }
        if (OdmElementTypes.Annotation_2007 == req.getElementType()) {
            if (req.getContainmentFeature() == null) {
                req.setContainmentFeature(OdmPackage.eINSTANCE.getOWL_Annonation());
            }
            return getMSLWrapper(new CreateAnnotation_2007Command(req));
        }
        if (OdmElementTypes.Individual_2008 == req.getElementType()) {
            if (req.getContainmentFeature() == null) {
                req.setContainmentFeature(OdmPackage.eINSTANCE.getOWL_Individual());
            }
            return getMSLWrapper(new CreateIndividual_2008Command(req));
        }
        if (OdmElementTypes.Constant_2009 == req.getElementType()) {
            if (req.getContainmentFeature() == null) {
                req.setContainmentFeature(OdmPackage.eINSTANCE.getOWL_Constant());
            }
            return getMSLWrapper(new CreateConstant_2009Command(req));
        }
        return super.getCreateCommand(req);
    }

    /**
	 * @generated
	 */
    private static class CreateOWLClass_2001Command extends CreateElementCommand {

        /**
		 * @generated
		 */
        public CreateOWLClass_2001Command(CreateElementRequest req) {
            super(req);
        }

        /**
		 * @generated
		 */
        protected EClass getEClassToEdit() {
            return OdmPackage.eINSTANCE.getOWL();
        }

        ;

        /**
		 * @generated
		 */
        protected EObject getElementToEdit() {
            EObject container = ((CreateElementRequest) getRequest()).getContainer();
            if (container instanceof View) {
                container = ((View) container).getElement();
            }
            return container;
        }
    }

    /**
	 * @generated
	 */
    private static class CreateDataProperty_2002Command extends CreateElementCommand {

        /**
		 * @generated
		 */
        public CreateDataProperty_2002Command(CreateElementRequest req) {
            super(req);
        }

        /**
		 * @generated
		 */
        protected EClass getEClassToEdit() {
            return OdmPackage.eINSTANCE.getOWL();
        }

        ;

        /**
		 * @generated
		 */
        protected EObject getElementToEdit() {
            EObject container = ((CreateElementRequest) getRequest()).getContainer();
            if (container instanceof View) {
                container = ((View) container).getElement();
            }
            return container;
        }
    }

    /**
	 * @generated
	 */
    private static class CreateObjectProperty_2003Command extends CreateElementCommand {

        /**
		 * @generated
		 */
        public CreateObjectProperty_2003Command(CreateElementRequest req) {
            super(req);
        }

        /**
		 * @generated
		 */
        protected EClass getEClassToEdit() {
            return OdmPackage.eINSTANCE.getOWL();
        }

        ;

        /**
		 * @generated
		 */
        protected EObject getElementToEdit() {
            EObject container = ((CreateElementRequest) getRequest()).getContainer();
            if (container instanceof View) {
                container = ((View) container).getElement();
            }
            return container;
        }
    }

    /**
	 * @generated
	 */
    private static class CreateMainOntology_2004Command extends CreateElementCommand {

        /**
		 * @generated
		 */
        public CreateMainOntology_2004Command(CreateElementRequest req) {
            super(req);
        }

        /**
		 * @generated
		 */
        protected EClass getEClassToEdit() {
            return OdmPackage.eINSTANCE.getOWL();
        }

        ;

        /**
		 * @generated
		 */
        protected EObject getElementToEdit() {
            EObject container = ((CreateElementRequest) getRequest()).getContainer();
            if (container instanceof View) {
                container = ((View) container).getElement();
            }
            return container;
        }
    }

    /**
	 * @generated
	 */
    private static class CreateOtherOntology_2005Command extends CreateElementCommand {

        /**
		 * @generated
		 */
        public CreateOtherOntology_2005Command(CreateElementRequest req) {
            super(req);
        }

        /**
		 * @generated
		 */
        protected EClass getEClassToEdit() {
            return OdmPackage.eINSTANCE.getOWL();
        }

        ;

        /**
		 * @generated
		 */
        protected EObject getElementToEdit() {
            EObject container = ((CreateElementRequest) getRequest()).getContainer();
            if (container instanceof View) {
                container = ((View) container).getElement();
            }
            return container;
        }
    }

    /**
	 * @generated
	 */
    private static class CreateDataRange_2006Command extends CreateElementCommand {

        /**
		 * @generated
		 */
        public CreateDataRange_2006Command(CreateElementRequest req) {
            super(req);
        }

        /**
		 * @generated
		 */
        protected EClass getEClassToEdit() {
            return OdmPackage.eINSTANCE.getOWL();
        }

        ;

        /**
		 * @generated
		 */
        protected EObject getElementToEdit() {
            EObject container = ((CreateElementRequest) getRequest()).getContainer();
            if (container instanceof View) {
                container = ((View) container).getElement();
            }
            return container;
        }
    }

    /**
	 * @generated
	 */
    private static class CreateAnnotation_2007Command extends CreateElementCommand {

        /**
		 * @generated
		 */
        public CreateAnnotation_2007Command(CreateElementRequest req) {
            super(req);
        }

        /**
		 * @generated
		 */
        protected EClass getEClassToEdit() {
            return OdmPackage.eINSTANCE.getOWL();
        }

        ;

        /**
		 * @generated
		 */
        protected EObject getElementToEdit() {
            EObject container = ((CreateElementRequest) getRequest()).getContainer();
            if (container instanceof View) {
                container = ((View) container).getElement();
            }
            return container;
        }
    }

    /**
	 * @generated
	 */
    private static class CreateIndividual_2008Command extends CreateElementCommand {

        /**
		 * @generated
		 */
        public CreateIndividual_2008Command(CreateElementRequest req) {
            super(req);
        }

        /**
		 * @generated
		 */
        protected EClass getEClassToEdit() {
            return OdmPackage.eINSTANCE.getOWL();
        }

        ;

        /**
		 * @generated
		 */
        protected EObject getElementToEdit() {
            EObject container = ((CreateElementRequest) getRequest()).getContainer();
            if (container instanceof View) {
                container = ((View) container).getElement();
            }
            return container;
        }
    }

    /**
	 * @generated
	 */
    private static class CreateConstant_2009Command extends CreateElementCommand {

        /**
		 * @generated
		 */
        public CreateConstant_2009Command(CreateElementRequest req) {
            super(req);
        }

        /**
		 * @generated
		 */
        protected EClass getEClassToEdit() {
            return OdmPackage.eINSTANCE.getOWL();
        }

        ;

        /**
		 * @generated
		 */
        protected EObject getElementToEdit() {
            EObject container = ((CreateElementRequest) getRequest()).getContainer();
            if (container instanceof View) {
                container = ((View) container).getElement();
            }
            return container;
        }
    }

    /**
	 * @generated
	 */
    protected Command getDuplicateCommand(DuplicateElementsRequest req) {
        TransactionalEditingDomain editingDomain = ((IGraphicalEditPart) getHost()).getEditingDomain();
        return getMSLWrapper(new DuplicateAnythingCommand(editingDomain, req));
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
