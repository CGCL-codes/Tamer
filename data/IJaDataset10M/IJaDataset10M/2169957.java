package EJBTool.deployment.part;

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
public class EJBToolVisualIDRegistry {

    /**
	 * @generated
	 */
    private static final String DEBUG_KEY = EJBTool.deployment.part.EJBToolDiagramEditorPlugin.getInstance().getBundle().getSymbolicName() + "/debug/visualID";

    /**
	 * @generated
	 */
    public static int getVisualID(View view) {
        if (view instanceof Diagram) {
            if (EJBTool.deployment.edit.parts.DeplyomentViewEditPart.MODEL_ID.equals(view.getType())) {
                return EJBTool.deployment.edit.parts.DeplyomentViewEditPart.VISUAL_ID;
            } else {
                return -1;
            }
        }
        return EJBTool.deployment.part.EJBToolVisualIDRegistry.getVisualID(view.getType());
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
                EJBTool.deployment.part.EJBToolDiagramEditorPlugin.getInstance().logError("Unable to parse view type as a visualID number: " + type);
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
        if (EJBTool.EJBToolPackage.eINSTANCE.getRoot().isSuperTypeOf(domainElement.eClass()) && isDiagram((EJBTool.Root) domainElement)) {
            return EJBTool.deployment.edit.parts.DeplyomentViewEditPart.VISUAL_ID;
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
        String containerModelID = EJBTool.deployment.part.EJBToolVisualIDRegistry.getModelID(containerView);
        if (!EJBTool.deployment.edit.parts.DeplyomentViewEditPart.MODEL_ID.equals(containerModelID)) {
            return -1;
        }
        int containerVisualID;
        if (EJBTool.deployment.edit.parts.DeplyomentViewEditPart.MODEL_ID.equals(containerModelID)) {
            containerVisualID = EJBTool.deployment.part.EJBToolVisualIDRegistry.getVisualID(containerView);
        } else {
            if (containerView instanceof Diagram) {
                containerVisualID = EJBTool.deployment.edit.parts.DeplyomentViewEditPart.VISUAL_ID;
            } else {
                return -1;
            }
        }
        switch(containerVisualID) {
            case EJBTool.deployment.edit.parts.DeplyomentViewEditPart.VISUAL_ID:
                if (EJBTool.EJBToolPackage.eINSTANCE.getEJB().isSuperTypeOf(domainElement.eClass())) {
                    return EJBTool.deployment.edit.parts.EJBEditPart.VISUAL_ID;
                }
                if (EJBTool.EJBToolPackage.eINSTANCE.getModule().isSuperTypeOf(domainElement.eClass())) {
                    return EJBTool.deployment.edit.parts.ModuleEditPart.VISUAL_ID;
                }
                break;
        }
        return -1;
    }

    /**
	 * @generated
	 */
    public static boolean canCreateNode(View containerView, int nodeVisualID) {
        String containerModelID = EJBTool.deployment.part.EJBToolVisualIDRegistry.getModelID(containerView);
        if (!EJBTool.deployment.edit.parts.DeplyomentViewEditPart.MODEL_ID.equals(containerModelID)) {
            return false;
        }
        int containerVisualID;
        if (EJBTool.deployment.edit.parts.DeplyomentViewEditPart.MODEL_ID.equals(containerModelID)) {
            containerVisualID = EJBTool.deployment.part.EJBToolVisualIDRegistry.getVisualID(containerView);
        } else {
            if (containerView instanceof Diagram) {
                containerVisualID = EJBTool.deployment.edit.parts.DeplyomentViewEditPart.VISUAL_ID;
            } else {
                return false;
            }
        }
        switch(containerVisualID) {
            case EJBTool.deployment.edit.parts.EJBEditPart.VISUAL_ID:
                if (EJBTool.deployment.edit.parts.EJBNameEditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (EJBTool.deployment.edit.parts.EJBPersistentEditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                break;
            case EJBTool.deployment.edit.parts.ModuleEditPart.VISUAL_ID:
                if (EJBTool.deployment.edit.parts.ModuleNameEditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (EJBTool.deployment.edit.parts.ModuleDescriptionEditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                break;
            case EJBTool.deployment.edit.parts.DeplyomentViewEditPart.VISUAL_ID:
                if (EJBTool.deployment.edit.parts.EJBEditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (EJBTool.deployment.edit.parts.ModuleEditPart.VISUAL_ID == nodeVisualID) {
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
        return -1;
    }

    /**
	 * User can change implementation of this method to handle some specific
	 * situations not covered by default logic.
	 * 
	 * @generated
	 */
    private static boolean isDiagram(EJBTool.Root element) {
        return true;
    }
}
