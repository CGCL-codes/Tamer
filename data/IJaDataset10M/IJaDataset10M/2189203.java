package org.eclipse.epsilon.fptc.system.diagram.part;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.epsilon.fptc.system.System;
import org.eclipse.epsilon.fptc.system.SystemPackage;
import org.eclipse.epsilon.fptc.system.diagram.edit.parts.BlockEditPart;
import org.eclipse.epsilon.fptc.system.diagram.edit.parts.BlockFaultBehaviourTextEditPart;
import org.eclipse.epsilon.fptc.system.diagram.edit.parts.BlockNameEditPart;
import org.eclipse.epsilon.fptc.system.diagram.edit.parts.ConnectionEditPart;
import org.eclipse.epsilon.fptc.system.diagram.edit.parts.ConnectionLiteralsTextEditPart;
import org.eclipse.epsilon.fptc.system.diagram.edit.parts.SystemEditPart;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;

/**
 * This registry is used to determine which type of visual object should be
 * created for the corresponding Diagram, Node, ChildNode or Link represented
 * by a domain model object.
 * 
 * @generated
 */
public class SystemVisualIDRegistry {

    /**
	 * @generated
	 */
    private static final String DEBUG_KEY = "org.eclipse.epsilon.fptc.system.diagram/debug/visualID";

    /**
	 * @generated
	 */
    public static int getVisualID(View view) {
        if (view instanceof Diagram) {
            if (SystemEditPart.MODEL_ID.equals(view.getType())) {
                return SystemEditPart.VISUAL_ID;
            } else {
                return -1;
            }
        }
        return org.eclipse.epsilon.fptc.system.diagram.part.SystemVisualIDRegistry.getVisualID(view.getType());
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
                SystemDiagramEditorPlugin.getInstance().logError("Unable to parse view type as a visualID number: " + type);
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
        if (SystemPackage.eINSTANCE.getSystem().isSuperTypeOf(domainElement.eClass()) && isDiagram((System) domainElement)) {
            return SystemEditPart.VISUAL_ID;
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
        String containerModelID = org.eclipse.epsilon.fptc.system.diagram.part.SystemVisualIDRegistry.getModelID(containerView);
        if (!SystemEditPart.MODEL_ID.equals(containerModelID) && !"system".equals(containerModelID)) {
            return -1;
        }
        int containerVisualID;
        if (SystemEditPart.MODEL_ID.equals(containerModelID)) {
            containerVisualID = org.eclipse.epsilon.fptc.system.diagram.part.SystemVisualIDRegistry.getVisualID(containerView);
        } else {
            if (containerView instanceof Diagram) {
                containerVisualID = SystemEditPart.VISUAL_ID;
            } else {
                return -1;
            }
        }
        switch(containerVisualID) {
            case SystemEditPart.VISUAL_ID:
                if (SystemPackage.eINSTANCE.getBlock().isSuperTypeOf(domainElement.eClass())) {
                    return BlockEditPart.VISUAL_ID;
                }
                break;
        }
        return -1;
    }

    /**
	 * @generated
	 */
    public static boolean canCreateNode(View containerView, int nodeVisualID) {
        String containerModelID = org.eclipse.epsilon.fptc.system.diagram.part.SystemVisualIDRegistry.getModelID(containerView);
        if (!SystemEditPart.MODEL_ID.equals(containerModelID) && !"system".equals(containerModelID)) {
            return false;
        }
        int containerVisualID;
        if (SystemEditPart.MODEL_ID.equals(containerModelID)) {
            containerVisualID = org.eclipse.epsilon.fptc.system.diagram.part.SystemVisualIDRegistry.getVisualID(containerView);
        } else {
            if (containerView instanceof Diagram) {
                containerVisualID = SystemEditPart.VISUAL_ID;
            } else {
                return false;
            }
        }
        switch(containerVisualID) {
            case BlockEditPart.VISUAL_ID:
                if (BlockNameEditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (BlockFaultBehaviourTextEditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                break;
            case SystemEditPart.VISUAL_ID:
                if (BlockEditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                break;
            case ConnectionEditPart.VISUAL_ID:
                if (ConnectionLiteralsTextEditPart.VISUAL_ID == nodeVisualID) {
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
        if (SystemPackage.eINSTANCE.getConnection().isSuperTypeOf(domainElement.eClass())) {
            return ConnectionEditPart.VISUAL_ID;
        }
        return -1;
    }

    /**
	 * User can change implementation of this method to handle some specific
	 * situations not covered by default logic.
	 * 
	 * @generated
	 */
    private static boolean isDiagram(System element) {
        return true;
    }
}
