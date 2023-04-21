package de.hu_berlin.sam.mmunit.diagram.part;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;

/**
 * This registry is used to determine which type of visual object should be
 * created for the corresponding Diagram, Node, ChildNode or Link represented
 * by a domain model object.
 * 
 * @generated
 */
public class MMUnitVisualIDRegistry {

    /**
	 * @generated
	 */
    private static final String DEBUG_KEY = "de.hu_berlin.sam.mmunit.diagram/debug/visualID";

    /**
	 * @generated
	 */
    public static int getVisualID(View view) {
        if (view instanceof Diagram) {
            if (de.hu_berlin.sam.mmunit.diagram.edit.parts.TestModelEditPart.MODEL_ID.equals(view.getType())) {
                return de.hu_berlin.sam.mmunit.diagram.edit.parts.TestModelEditPart.VISUAL_ID;
            } else {
                return -1;
            }
        }
        return de.hu_berlin.sam.mmunit.diagram.part.MMUnitVisualIDRegistry.getVisualID(view.getType());
    }

    /**
	 * @generated
	 */
    public static String getModelID(View view) {
        View diagram = view.getDiagram();
        while (view != diagram) {
            EAnnotation annotation = view.getEAnnotation("Shortcut");
            if (annotation != null) {
                return (String) annotation.getDetails().get("modelID");
            }
            view = (View) view.eContainer();
        }
        return diagram != null ? diagram.getType() : null;
    }

    /**
	 * @generated
	 */
    public static int getVisualID(String type) {
        try {
            return Integer.parseInt(type);
        } catch (NumberFormatException e) {
            if (Boolean.TRUE.toString().equalsIgnoreCase(Platform.getDebugOption(DEBUG_KEY))) {
                de.hu_berlin.sam.mmunit.diagram.part.MMUnitDiagramEditorPlugin.getInstance().logError("Unable to parse view type as a visualID number: " + type);
            }
        }
        return -1;
    }

    /**
	 * @generated
	 */
    public static String getType(int visualID) {
        return String.valueOf(visualID);
    }

    /**
	 * @generated
	 */
    public static int getDiagramVisualID(EObject domainElement) {
        if (domainElement == null) {
            return -1;
        }
        if (de.hu_berlin.sam.mmunit.MMUnitPackage.eINSTANCE.getTestModel().isSuperTypeOf(domainElement.eClass()) && isDiagram((de.hu_berlin.sam.mmunit.TestModel) domainElement)) {
            return de.hu_berlin.sam.mmunit.diagram.edit.parts.TestModelEditPart.VISUAL_ID;
        }
        return -1;
    }

    /**
	 * @generated
	 */
    public static int getNodeVisualID(View containerView, EObject domainElement) {
        if (domainElement == null) {
            return -1;
        }
        String containerModelID = de.hu_berlin.sam.mmunit.diagram.part.MMUnitVisualIDRegistry.getModelID(containerView);
        if (!de.hu_berlin.sam.mmunit.diagram.edit.parts.TestModelEditPart.MODEL_ID.equals(containerModelID)) {
            return -1;
        }
        int containerVisualID;
        if (de.hu_berlin.sam.mmunit.diagram.edit.parts.TestModelEditPart.MODEL_ID.equals(containerModelID)) {
            containerVisualID = de.hu_berlin.sam.mmunit.diagram.part.MMUnitVisualIDRegistry.getVisualID(containerView);
        } else {
            if (containerView instanceof Diagram) {
                containerVisualID = de.hu_berlin.sam.mmunit.diagram.edit.parts.TestModelEditPart.VISUAL_ID;
            } else {
                return -1;
            }
        }
        switch(containerVisualID) {
            case de.hu_berlin.sam.mmunit.diagram.edit.parts.InstanceAttributeCompartmentEditPart.VISUAL_ID:
                if (de.hu_berlin.sam.mmunit.MMUnitPackage.eINSTANCE.getAttribute().isSuperTypeOf(domainElement.eClass())) {
                    return de.hu_berlin.sam.mmunit.diagram.edit.parts.AttributeEditPart.VISUAL_ID;
                }
                break;
            case de.hu_berlin.sam.mmunit.diagram.edit.parts.TestModelEditPart.VISUAL_ID:
                if (de.hu_berlin.sam.mmunit.MMUnitPackage.eINSTANCE.getInstance().isSuperTypeOf(domainElement.eClass())) {
                    return de.hu_berlin.sam.mmunit.diagram.edit.parts.InstanceEditPart.VISUAL_ID;
                }
                if (de.hu_berlin.sam.mmunit.MMUnitPackage.eINSTANCE.getConjunction().isSuperTypeOf(domainElement.eClass())) {
                    return de.hu_berlin.sam.mmunit.diagram.edit.parts.ConjunctionEditPart.VISUAL_ID;
                }
                break;
        }
        return -1;
    }

    /**
	 * @generated
	 */
    public static boolean canCreateNode(View containerView, int nodeVisualID) {
        String containerModelID = de.hu_berlin.sam.mmunit.diagram.part.MMUnitVisualIDRegistry.getModelID(containerView);
        if (!de.hu_berlin.sam.mmunit.diagram.edit.parts.TestModelEditPart.MODEL_ID.equals(containerModelID)) {
            return false;
        }
        int containerVisualID;
        if (de.hu_berlin.sam.mmunit.diagram.edit.parts.TestModelEditPart.MODEL_ID.equals(containerModelID)) {
            containerVisualID = de.hu_berlin.sam.mmunit.diagram.part.MMUnitVisualIDRegistry.getVisualID(containerView);
        } else {
            if (containerView instanceof Diagram) {
                containerVisualID = de.hu_berlin.sam.mmunit.diagram.edit.parts.TestModelEditPart.VISUAL_ID;
            } else {
                return false;
            }
        }
        switch(containerVisualID) {
            case de.hu_berlin.sam.mmunit.diagram.edit.parts.InstanceEditPart.VISUAL_ID:
                if (de.hu_berlin.sam.mmunit.diagram.edit.parts.InstanceObjectNameClassNameEditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (de.hu_berlin.sam.mmunit.diagram.edit.parts.InstanceAttributeCompartmentEditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                break;
            case de.hu_berlin.sam.mmunit.diagram.edit.parts.AttributeEditPart.VISUAL_ID:
                if (de.hu_berlin.sam.mmunit.diagram.edit.parts.AttributeNameValueEditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                break;
            case de.hu_berlin.sam.mmunit.diagram.edit.parts.InstanceAttributeCompartmentEditPart.VISUAL_ID:
                if (de.hu_berlin.sam.mmunit.diagram.edit.parts.AttributeEditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                break;
            case de.hu_berlin.sam.mmunit.diagram.edit.parts.TestModelEditPart.VISUAL_ID:
                if (de.hu_berlin.sam.mmunit.diagram.edit.parts.InstanceEditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (de.hu_berlin.sam.mmunit.diagram.edit.parts.ConjunctionEditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                break;
            case de.hu_berlin.sam.mmunit.diagram.edit.parts.ReferenceEditPart.VISUAL_ID:
                if (de.hu_berlin.sam.mmunit.diagram.edit.parts.ReferenceEnd2NameEditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (de.hu_berlin.sam.mmunit.diagram.edit.parts.ReferenceEnd1NameEditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                break;
        }
        return false;
    }

    /**
	 * @generated
	 */
    public static int getLinkWithClassVisualID(EObject domainElement) {
        if (domainElement == null) {
            return -1;
        }
        if (de.hu_berlin.sam.mmunit.MMUnitPackage.eINSTANCE.getReference().isSuperTypeOf(domainElement.eClass())) {
            return de.hu_berlin.sam.mmunit.diagram.edit.parts.ReferenceEditPart.VISUAL_ID;
        }
        return -1;
    }

    /**
	 * User can change implementation of this method to handle some specific
	 * situations not covered by default logic.
	 * 
	 * @generated
	 */
    private static boolean isDiagram(de.hu_berlin.sam.mmunit.TestModel element) {
        return true;
    }
}
