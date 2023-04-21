package com.ctb.diagram.part;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import com.ctb.CtbPackage;
import com.ctb.diagram.edit.parts.DefaultConnectionEditPart;
import com.ctb.diagram.edit.parts.DiagramEditPart;
import com.ctb.diagram.edit.parts.EnterpriseServerEditPart;
import com.ctb.diagram.edit.parts.MobileCellNetworkEditPart;
import com.ctb.diagram.edit.parts.MobileDeviceEditPart;
import com.ctb.diagram.edit.parts.OfficeServerEditPart;
import com.ctb.diagram.edit.parts.PhysicalConnectionEditPart;
import com.ctb.diagram.edit.parts.ShortrangeEditPart;
import com.ctb.diagram.edit.parts.SmartCardEditPart;
import com.ctb.diagram.edit.parts.WireEditPart;
import com.ctb.diagram.edit.parts.WirelessEditPart;
import com.ctb.diagram.edit.parts.WorkstationEditPart;

/**
 * This registry is used to determine which type of visual object should be
 * created for the corresponding Diagram, Node, ChildNode or Link represented
 * by a domain model object.
 * 
 * @generated
 */
public class CtbVisualIDRegistry {

    /**
	 * @generated
	 */
    private static final String DEBUG_KEY = CtbDiagramEditorPlugin.getInstance().getBundle().getSymbolicName() + "/debug/visualID";

    /**
	 * @generated
	 */
    public static int getVisualID(View view) {
        if (view instanceof Diagram) {
            if (DiagramEditPart.MODEL_ID.equals(view.getType())) {
                return DiagramEditPart.VISUAL_ID;
            } else {
                return -1;
            }
        }
        return com.ctb.diagram.part.CtbVisualIDRegistry.getVisualID(view.getType());
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
                CtbDiagramEditorPlugin.getInstance().logError("Unable to parse view type as a visualID number: " + type);
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
        if (CtbPackage.eINSTANCE.getDiagram().isSuperTypeOf(domainElement.eClass()) && isDiagram((com.ctb.Diagram) domainElement)) {
            return DiagramEditPart.VISUAL_ID;
        }
        return -1;
    }

    /**
	 * @generated
	 */
    public static int getNodeVisualID(View containerView, EObject domainElement) {
        if (domainElement == null || !DiagramEditPart.MODEL_ID.equals(com.ctb.diagram.part.CtbVisualIDRegistry.getModelID(containerView))) {
            return -1;
        }
        switch(com.ctb.diagram.part.CtbVisualIDRegistry.getVisualID(containerView)) {
            case DiagramEditPart.VISUAL_ID:
                if (CtbPackage.eINSTANCE.getEnterpriseServer().isSuperTypeOf(domainElement.eClass())) {
                    return EnterpriseServerEditPart.VISUAL_ID;
                }
                if (CtbPackage.eINSTANCE.getOfficeServer().isSuperTypeOf(domainElement.eClass())) {
                    return OfficeServerEditPart.VISUAL_ID;
                }
                if (CtbPackage.eINSTANCE.getWorkstation().isSuperTypeOf(domainElement.eClass())) {
                    return WorkstationEditPart.VISUAL_ID;
                }
                if (CtbPackage.eINSTANCE.getMobileDevice().isSuperTypeOf(domainElement.eClass())) {
                    return MobileDeviceEditPart.VISUAL_ID;
                }
                if (CtbPackage.eINSTANCE.getSmartCard().isSuperTypeOf(domainElement.eClass())) {
                    return SmartCardEditPart.VISUAL_ID;
                }
                if (CtbPackage.eINSTANCE.getWire().isSuperTypeOf(domainElement.eClass())) {
                    return WireEditPart.VISUAL_ID;
                }
                if (CtbPackage.eINSTANCE.getWireless().isSuperTypeOf(domainElement.eClass())) {
                    return WirelessEditPart.VISUAL_ID;
                }
                if (CtbPackage.eINSTANCE.getMobileCellNetwork().isSuperTypeOf(domainElement.eClass())) {
                    return MobileCellNetworkEditPart.VISUAL_ID;
                }
                if (CtbPackage.eINSTANCE.getShortrange().isSuperTypeOf(domainElement.eClass())) {
                    return ShortrangeEditPart.VISUAL_ID;
                }
                break;
        }
        return -1;
    }

    /**
	 * @generated
	 */
    public static boolean canCreateNode(View containerView, int nodeVisualID) {
        String containerModelID = com.ctb.diagram.part.CtbVisualIDRegistry.getModelID(containerView);
        if (!DiagramEditPart.MODEL_ID.equals(containerModelID)) {
            return false;
        }
        int containerVisualID;
        if (DiagramEditPart.MODEL_ID.equals(containerModelID)) {
            containerVisualID = com.ctb.diagram.part.CtbVisualIDRegistry.getVisualID(containerView);
        } else {
            if (containerView instanceof Diagram) {
                containerVisualID = DiagramEditPart.VISUAL_ID;
            } else {
                return false;
            }
        }
        switch(containerVisualID) {
            case DiagramEditPart.VISUAL_ID:
                if (EnterpriseServerEditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (OfficeServerEditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (WorkstationEditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (MobileDeviceEditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (SmartCardEditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (WireEditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (WirelessEditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (MobileCellNetworkEditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (ShortrangeEditPart.VISUAL_ID == nodeVisualID) {
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
        if (CtbPackage.eINSTANCE.getDefaultConnection().isSuperTypeOf(domainElement.eClass())) {
            return DefaultConnectionEditPart.VISUAL_ID;
        }
        if (CtbPackage.eINSTANCE.getPhysicalConnection().isSuperTypeOf(domainElement.eClass())) {
            return PhysicalConnectionEditPart.VISUAL_ID;
        }
        return -1;
    }

    /**
	 * User can change implementation of this method to handle some specific
	 * situations not covered by default logic.
	 * 
	 * @generated
	 */
    private static boolean isDiagram(com.ctb.Diagram element) {
        return true;
    }
}
