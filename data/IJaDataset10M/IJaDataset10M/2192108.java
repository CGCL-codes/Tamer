package net.taylor.uml2.classdiagram.edit.policies;

import net.taylor.uml2.classdiagram.providers.UMLElementTypes;
import net.taylor.uml2.classdiagram.util.ModelUtil;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gmf.runtime.emf.type.core.commands.CreateRelationshipCommand;
import org.eclipse.gmf.runtime.emf.type.core.commands.DestroyElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLPackage;

/**
 * @generated
 */
public class UseCaseItemSemanticEditPolicy extends UMLBaseItemSemanticEditPolicy {

    /**
	 * @generated
	 */
    protected Command getDestroyElementCommand(DestroyElementRequest req) {
        return getMSLWrapper(new DestroyElementCommand(req) {

            protected EObject getElementToDestroy() {
                View view = (View) getHost().getModel();
                EAnnotation annotation = view.getEAnnotation("Shortcut");
                if (annotation != null) {
                    return view;
                }
                return super.getElementToDestroy();
            }
        });
    }

    /**
	 * @generated
	 */
    protected Command getCreateRelationshipCommand(CreateRelationshipRequest req) {
        if (UMLElementTypes.Generalization_3001 == req.getElementType()) {
            return req.getTarget() == null ? getCreateStartOutgoingGeneralization3001Command(req) : getCreateCompleteIncomingGeneralization3001Command(req);
        }
        if (UMLElementTypes.Association_3002 == req.getElementType()) {
            return req.getTarget() == null ? getCreateStartOutgoingAssociation3002Command(req) : getCreateCompleteIncomingAssociation3002Command(req);
        }
        if (UMLElementTypes.Association_3003 == req.getElementType()) {
            return req.getTarget() == null ? getCreateStartOutgoingAssociation3003Command(req) : getCreateCompleteIncomingAssociation3003Command(req);
        }
        if (UMLElementTypes.Association_3004 == req.getElementType()) {
            return req.getTarget() == null ? getCreateStartOutgoingAssociation3004Command(req) : getCreateCompleteIncomingAssociation3004Command(req);
        }
        if (UMLElementTypes.Association_3005 == req.getElementType()) {
            return req.getTarget() == null ? getCreateStartOutgoingAssociation3005Command(req) : getCreateCompleteIncomingAssociation3005Command(req);
        }
        if (UMLElementTypes.Association_3006 == req.getElementType()) {
            return req.getTarget() == null ? getCreateStartOutgoingAssociation3006Command(req) : getCreateCompleteIncomingAssociation3006Command(req);
        }
        if (UMLElementTypes.Dependency_3007 == req.getElementType()) {
            return req.getTarget() == null ? getCreateStartOutgoingDependency3007Command(req) : getCreateCompleteIncomingDependency3007Command(req);
        }
        return super.getCreateRelationshipCommand(req);
    }

    /**
	 * @generated
	 */
    protected Command getCreateStartOutgoingGeneralization3001Command(CreateRelationshipRequest req) {
        return new Command() {
        };
    }

    /**
	 * @generated
	 */
    protected Command getCreateCompleteIncomingGeneralization3001Command(CreateRelationshipRequest req) {
        if (!(req.getSource() instanceof Classifier)) {
            return UnexecutableCommand.INSTANCE;
        }
        final Classifier element = (Classifier) req.getSource();
        if (req.getContainmentFeature() == null) {
            req.setContainmentFeature(UMLPackage.eINSTANCE.getClassifier_Generalization());
        }
        return getMSLWrapper(new CreateIncomingGeneralization3001Command(req) {

            /**
			 * @generated
			 */
            protected EObject getElementToEdit() {
                return element;
            }
        });
    }

    /**
	 * @generated
	 */
    private static class CreateIncomingGeneralization3001Command extends CreateRelationshipCommand {

        /**
		 * @generated
		 */
        public CreateIncomingGeneralization3001Command(CreateRelationshipRequest req) {
            super(req);
        }

        /**
		 * @generated
		 */
        protected EClass getEClassToEdit() {
            return UMLPackage.eINSTANCE.getClassifier();
        }

        ;

        /**
		 * @generated
		 */
        protected void setElementToEdit(EObject element) {
            throw new UnsupportedOperationException();
        }

        /**
		 * @generated
		 */
        protected EObject doDefaultElementCreation() {
            Generalization newElement = (Generalization) super.doDefaultElementCreation();
            if (newElement != null) {
                newElement.setGeneral((Classifier) getTarget());
            }
            return newElement;
        }
    }

    /**
	 * @generated
	 */
    protected Command getCreateStartOutgoingAssociation3002Command(CreateRelationshipRequest req) {
        return new Command() {
        };
    }

    /**
	 * @generated
	 */
    protected Command getCreateCompleteIncomingAssociation3002Command(CreateRelationshipRequest req) {
        if (!(req.getSource() instanceof Element)) {
            return UnexecutableCommand.INSTANCE;
        }
        final org.eclipse.uml2.uml.Package element = (org.eclipse.uml2.uml.Package) getRelationshipContainer(req.getSource(), UMLPackage.eINSTANCE.getPackage(), req.getElementType());
        if (element == null) {
            return UnexecutableCommand.INSTANCE;
        }
        if (req.getContainmentFeature() == null) {
            req.setContainmentFeature(UMLPackage.eINSTANCE.getPackage_PackagedElement());
        }
        return getMSLWrapper(new CreateIncomingAssociation3002Command(req) {

            /**
			 * @generated
			 */
            protected EObject getElementToEdit() {
                return element;
            }
        });
    }

    /**
	 * @generated
	 */
    private static class CreateIncomingAssociation3002Command extends CreateRelationshipCommand {

        /**
		 * @generated
		 */
        public CreateIncomingAssociation3002Command(CreateRelationshipRequest req) {
            super(req);
        }

        /**
		 * @generated
		 */
        protected EClass getEClassToEdit() {
            return UMLPackage.eINSTANCE.getPackage();
        }

        ;

        /**
		 * @generated
		 */
        protected void setElementToEdit(EObject element) {
            throw new UnsupportedOperationException();
        }

        /**
		 * @generated
		 */
        protected EObject doDefaultElementCreation() {
            Association newElement = (Association) super.doDefaultElementCreation();
            if (newElement != null) {
                newElement.getRelatedElements().add((Element) getTarget());
                newElement.getRelatedElements().add((Element) getSource());
            }
            return newElement;
        }
    }

    /**
	 * @generated
	 */
    protected Command getCreateStartOutgoingAssociation3003Command(CreateRelationshipRequest req) {
        return new Command() {
        };
    }

    /**
	 * @generated
	 */
    protected Command getCreateCompleteIncomingAssociation3003Command(CreateRelationshipRequest req) {
        if (!(req.getSource() instanceof Element)) {
            return UnexecutableCommand.INSTANCE;
        }
        final org.eclipse.uml2.uml.Package element = (org.eclipse.uml2.uml.Package) getRelationshipContainer(req.getSource(), UMLPackage.eINSTANCE.getPackage(), req.getElementType());
        if (element == null) {
            return UnexecutableCommand.INSTANCE;
        }
        if (req.getContainmentFeature() == null) {
            req.setContainmentFeature(UMLPackage.eINSTANCE.getPackage_PackagedElement());
        }
        return getMSLWrapper(new CreateIncomingAssociation3003Command(req) {

            /**
			 * @generated
			 */
            protected EObject getElementToEdit() {
                return element;
            }
        });
    }

    /**
	 * @generated
	 */
    private static class CreateIncomingAssociation3003Command extends CreateRelationshipCommand {

        /**
		 * @generated
		 */
        public CreateIncomingAssociation3003Command(CreateRelationshipRequest req) {
            super(req);
        }

        /**
		 * @generated
		 */
        protected EClass getEClassToEdit() {
            return UMLPackage.eINSTANCE.getPackage();
        }

        ;

        /**
		 * @generated
		 */
        protected void setElementToEdit(EObject element) {
            throw new UnsupportedOperationException();
        }

        /**
		 * @generated
		 */
        protected EObject doDefaultElementCreation() {
            Association newElement = (Association) super.doDefaultElementCreation();
            if (newElement != null) {
                newElement.getRelatedElements().add((Element) getTarget());
                newElement.getRelatedElements().add((Element) getSource());
            }
            return newElement;
        }
    }

    /**
	 * @generated
	 */
    protected Command getCreateStartOutgoingAssociation3004Command(CreateRelationshipRequest req) {
        return new Command() {
        };
    }

    /**
	 * @generated
	 */
    protected Command getCreateCompleteIncomingAssociation3004Command(CreateRelationshipRequest req) {
        if (!(req.getSource() instanceof Element)) {
            return UnexecutableCommand.INSTANCE;
        }
        final org.eclipse.uml2.uml.Package element = (org.eclipse.uml2.uml.Package) getRelationshipContainer(req.getSource(), UMLPackage.eINSTANCE.getPackage(), req.getElementType());
        if (element == null) {
            return UnexecutableCommand.INSTANCE;
        }
        if (req.getContainmentFeature() == null) {
            req.setContainmentFeature(UMLPackage.eINSTANCE.getPackage_PackagedElement());
        }
        return getMSLWrapper(new CreateIncomingAssociation3004Command(req) {

            /**
			 * @generated
			 */
            protected EObject getElementToEdit() {
                return element;
            }
        });
    }

    /**
	 * @generated
	 */
    private static class CreateIncomingAssociation3004Command extends CreateRelationshipCommand {

        /**
		 * @generated
		 */
        public CreateIncomingAssociation3004Command(CreateRelationshipRequest req) {
            super(req);
        }

        /**
		 * @generated
		 */
        protected EClass getEClassToEdit() {
            return UMLPackage.eINSTANCE.getPackage();
        }

        ;

        /**
		 * @generated
		 */
        protected void setElementToEdit(EObject element) {
            throw new UnsupportedOperationException();
        }

        /**
		 * @generated
		 */
        protected EObject doDefaultElementCreation() {
            Association newElement = (Association) super.doDefaultElementCreation();
            if (newElement != null) {
                newElement.getRelatedElements().add((Element) getTarget());
                newElement.getRelatedElements().add((Element) getSource());
            }
            return newElement;
        }
    }

    /**
	 * @generated
	 */
    protected Command getCreateStartOutgoingAssociation3005Command(CreateRelationshipRequest req) {
        return new Command() {
        };
    }

    /**
	 * @generated
	 */
    protected Command getCreateCompleteIncomingAssociation3005Command(CreateRelationshipRequest req) {
        if (!(req.getSource() instanceof Element)) {
            return UnexecutableCommand.INSTANCE;
        }
        final org.eclipse.uml2.uml.Package element = (org.eclipse.uml2.uml.Package) getRelationshipContainer(req.getSource(), UMLPackage.eINSTANCE.getPackage(), req.getElementType());
        if (element == null) {
            return UnexecutableCommand.INSTANCE;
        }
        if (req.getContainmentFeature() == null) {
            req.setContainmentFeature(UMLPackage.eINSTANCE.getPackage_PackagedElement());
        }
        return getMSLWrapper(new CreateIncomingAssociation3005Command(req) {

            /**
			 * @generated
			 */
            protected EObject getElementToEdit() {
                return element;
            }
        });
    }

    /**
	 * @generated
	 */
    private static class CreateIncomingAssociation3005Command extends CreateRelationshipCommand {

        /**
		 * @generated
		 */
        public CreateIncomingAssociation3005Command(CreateRelationshipRequest req) {
            super(req);
        }

        /**
		 * @generated
		 */
        protected EClass getEClassToEdit() {
            return UMLPackage.eINSTANCE.getPackage();
        }

        ;

        /**
		 * @generated
		 */
        protected void setElementToEdit(EObject element) {
            throw new UnsupportedOperationException();
        }

        /**
		 * @generated
		 */
        protected EObject doDefaultElementCreation() {
            Association newElement = (Association) super.doDefaultElementCreation();
            if (newElement != null) {
                newElement.getRelatedElements().add((Element) getTarget());
                newElement.getRelatedElements().add((Element) getSource());
            }
            return newElement;
        }
    }

    /**
	 * @generated
	 */
    protected Command getCreateStartOutgoingAssociation3006Command(CreateRelationshipRequest req) {
        return new Command() {
        };
    }

    /**
	 * @generated
	 */
    protected Command getCreateCompleteIncomingAssociation3006Command(CreateRelationshipRequest req) {
        if (!(req.getSource() instanceof Element)) {
            return UnexecutableCommand.INSTANCE;
        }
        final org.eclipse.uml2.uml.Package element = (org.eclipse.uml2.uml.Package) getRelationshipContainer(req.getSource(), UMLPackage.eINSTANCE.getPackage(), req.getElementType());
        if (element == null) {
            return UnexecutableCommand.INSTANCE;
        }
        if (req.getContainmentFeature() == null) {
            req.setContainmentFeature(UMLPackage.eINSTANCE.getPackage_PackagedElement());
        }
        return getMSLWrapper(new CreateIncomingAssociation3006Command(req) {

            /**
			 * @generated
			 */
            protected EObject getElementToEdit() {
                return element;
            }
        });
    }

    /**
	 * @generated
	 */
    private static class CreateIncomingAssociation3006Command extends CreateRelationshipCommand {

        /**
		 * @generated
		 */
        public CreateIncomingAssociation3006Command(CreateRelationshipRequest req) {
            super(req);
        }

        /**
		 * @generated
		 */
        protected EClass getEClassToEdit() {
            return UMLPackage.eINSTANCE.getPackage();
        }

        ;

        /**
		 * @generated
		 */
        protected void setElementToEdit(EObject element) {
            throw new UnsupportedOperationException();
        }

        /**
		 * @generated NOT
		 */
        protected EObject doDefaultElementCreation() {
            Type source = (Type) getSource();
            Type target = (Type) getTarget();
            Association association = ModelUtil.createUseCaseActorAssociation(source, target);
            return association;
        }
    }

    /**
	 * @generated
	 */
    protected Command getCreateStartOutgoingDependency3007Command(CreateRelationshipRequest req) {
        return new Command() {
        };
    }

    /**
	 * @generated
	 */
    protected Command getCreateCompleteIncomingDependency3007Command(CreateRelationshipRequest req) {
        if (!(req.getSource() instanceof NamedElement)) {
            return UnexecutableCommand.INSTANCE;
        }
        final org.eclipse.uml2.uml.Package element = (org.eclipse.uml2.uml.Package) getRelationshipContainer(req.getSource(), UMLPackage.eINSTANCE.getPackage(), req.getElementType());
        if (element == null) {
            return UnexecutableCommand.INSTANCE;
        }
        if (req.getContainmentFeature() == null) {
            req.setContainmentFeature(UMLPackage.eINSTANCE.getPackage_PackagedElement());
        }
        return getMSLWrapper(new CreateIncomingDependency3007Command(req) {

            /**
			 * @generated
			 */
            protected EObject getElementToEdit() {
                return element;
            }
        });
    }

    /**
	 * @generated
	 */
    private static class CreateIncomingDependency3007Command extends CreateRelationshipCommand {

        /**
		 * @generated
		 */
        public CreateIncomingDependency3007Command(CreateRelationshipRequest req) {
            super(req);
        }

        /**
		 * @generated
		 */
        protected EClass getEClassToEdit() {
            return UMLPackage.eINSTANCE.getPackage();
        }

        ;

        /**
		 * @generated
		 */
        protected void setElementToEdit(EObject element) {
            throw new UnsupportedOperationException();
        }

        /**
		 * @generated NOT
		 */
        protected EObject doDefaultElementCreation() {
            Type source = (Type) getSource();
            Type target = (Type) getTarget();
            Association association = ModelUtil.createUseCaseActorAssociation(source, target);
            return association;
        }
    }
}
