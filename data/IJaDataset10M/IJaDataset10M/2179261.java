package it.unisannio.rcost.callgraphanalyzer.diagram.part;

import it.unisannio.rcost.callgraphanalyzer.CallGraphPackage;
import it.unisannio.rcost.callgraphanalyzer.Graph;
import it.unisannio.rcost.callgraphanalyzer.diagram.edit.parts.Advice2EditPart;
import it.unisannio.rcost.callgraphanalyzer.diagram.edit.parts.AdviceEditPart;
import it.unisannio.rcost.callgraphanalyzer.diagram.edit.parts.Aspect2EditPart;
import it.unisannio.rcost.callgraphanalyzer.diagram.edit.parts.AspectBodyCompartment2EditPart;
import it.unisannio.rcost.callgraphanalyzer.diagram.edit.parts.AspectBodyCompartment3EditPart;
import it.unisannio.rcost.callgraphanalyzer.diagram.edit.parts.AspectBodyCompartmentEditPart;
import it.unisannio.rcost.callgraphanalyzer.diagram.edit.parts.AspectEditPart;
import it.unisannio.rcost.callgraphanalyzer.diagram.edit.parts.AspectName2EditPart;
import it.unisannio.rcost.callgraphanalyzer.diagram.edit.parts.AspectName3EditPart;
import it.unisannio.rcost.callgraphanalyzer.diagram.edit.parts.AspectNameEditPart;
import it.unisannio.rcost.callgraphanalyzer.diagram.edit.parts.AssociationEditPart;
import it.unisannio.rcost.callgraphanalyzer.diagram.edit.parts.Class2EditPart;
import it.unisannio.rcost.callgraphanalyzer.diagram.edit.parts.ClassBodyCompartment2EditPart;
import it.unisannio.rcost.callgraphanalyzer.diagram.edit.parts.ClassBodyCompartment3EditPart;
import it.unisannio.rcost.callgraphanalyzer.diagram.edit.parts.ClassBodyCompartmentEditPart;
import it.unisannio.rcost.callgraphanalyzer.diagram.edit.parts.ClassEditPart;
import it.unisannio.rcost.callgraphanalyzer.diagram.edit.parts.ClassName2EditPart;
import it.unisannio.rcost.callgraphanalyzer.diagram.edit.parts.ClassName3EditPart;
import it.unisannio.rcost.callgraphanalyzer.diagram.edit.parts.ClassNameEditPart;
import it.unisannio.rcost.callgraphanalyzer.diagram.edit.parts.DependenceEditPart;
import it.unisannio.rcost.callgraphanalyzer.diagram.edit.parts.ExplicitCallEditPart;
import it.unisannio.rcost.callgraphanalyzer.diagram.edit.parts.ExplicitCallGroupIdEditPart;
import it.unisannio.rcost.callgraphanalyzer.diagram.edit.parts.ExtensionEditPart;
import it.unisannio.rcost.callgraphanalyzer.diagram.edit.parts.Field2EditPart;
import it.unisannio.rcost.callgraphanalyzer.diagram.edit.parts.FieldEditPart;
import it.unisannio.rcost.callgraphanalyzer.diagram.edit.parts.GraphEditPart;
import it.unisannio.rcost.callgraphanalyzer.diagram.edit.parts.ImplementationEditPart;
import it.unisannio.rcost.callgraphanalyzer.diagram.edit.parts.ImplicitCallEditPart;
import it.unisannio.rcost.callgraphanalyzer.diagram.edit.parts.Interface2EditPart;
import it.unisannio.rcost.callgraphanalyzer.diagram.edit.parts.Interface3EditPart;
import it.unisannio.rcost.callgraphanalyzer.diagram.edit.parts.InterfaceBodyCompartment2EditPart;
import it.unisannio.rcost.callgraphanalyzer.diagram.edit.parts.InterfaceBodyCompartment3EditPart;
import it.unisannio.rcost.callgraphanalyzer.diagram.edit.parts.InterfaceBodyCompartmentEditPart;
import it.unisannio.rcost.callgraphanalyzer.diagram.edit.parts.InterfaceEditPart;
import it.unisannio.rcost.callgraphanalyzer.diagram.edit.parts.InterfaceName2EditPart;
import it.unisannio.rcost.callgraphanalyzer.diagram.edit.parts.InterfaceName3EditPart;
import it.unisannio.rcost.callgraphanalyzer.diagram.edit.parts.InterfaceNameEditPart;
import it.unisannio.rcost.callgraphanalyzer.diagram.edit.parts.Method2EditPart;
import it.unisannio.rcost.callgraphanalyzer.diagram.edit.parts.Method3EditPart;
import it.unisannio.rcost.callgraphanalyzer.diagram.edit.parts.MethodEditPart;
import it.unisannio.rcost.callgraphanalyzer.diagram.edit.parts.Package2EditPart;
import it.unisannio.rcost.callgraphanalyzer.diagram.edit.parts.Package3EditPart;
import it.unisannio.rcost.callgraphanalyzer.diagram.edit.parts.PackageBodyCompartment2EditPart;
import it.unisannio.rcost.callgraphanalyzer.diagram.edit.parts.PackageBodyCompartmentEditPart;
import it.unisannio.rcost.callgraphanalyzer.diagram.edit.parts.PackageEditPart;
import it.unisannio.rcost.callgraphanalyzer.diagram.edit.parts.PackageName2EditPart;
import it.unisannio.rcost.callgraphanalyzer.diagram.edit.parts.PackageNameEditPart;
import it.unisannio.rcost.callgraphanalyzer.diagram.edit.parts.Pointcut2EditPart;
import it.unisannio.rcost.callgraphanalyzer.diagram.edit.parts.PointcutEditPart;
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
public class CallGraphVisualIDRegistry {

    /**
	 * @generated
	 */
    private static final String DEBUG_KEY = CallGraphDiagramEditorPlugin.getInstance().getBundle().getSymbolicName() + "/debug/visualID";

    /**
	 * @generated
	 */
    public static int getVisualID(View view) {
        if (view instanceof Diagram) {
            if (GraphEditPart.MODEL_ID.equals(view.getType())) {
                return GraphEditPart.VISUAL_ID;
            } else {
                return -1;
            }
        }
        return it.unisannio.rcost.callgraphanalyzer.diagram.part.CallGraphVisualIDRegistry.getVisualID(view.getType());
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
                CallGraphDiagramEditorPlugin.getInstance().logError("Unable to parse view type as a visualID number: " + type);
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
        if (CallGraphPackage.eINSTANCE.getGraph().isSuperTypeOf(domainElement.eClass()) && isDiagram((Graph) domainElement)) {
            return GraphEditPart.VISUAL_ID;
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
        String containerModelID = it.unisannio.rcost.callgraphanalyzer.diagram.part.CallGraphVisualIDRegistry.getModelID(containerView);
        if (!GraphEditPart.MODEL_ID.equals(containerModelID)) {
            return -1;
        }
        int containerVisualID;
        if (GraphEditPart.MODEL_ID.equals(containerModelID)) {
            containerVisualID = it.unisannio.rcost.callgraphanalyzer.diagram.part.CallGraphVisualIDRegistry.getVisualID(containerView);
        } else {
            if (containerView instanceof Diagram) {
                containerVisualID = GraphEditPart.VISUAL_ID;
            } else {
                return -1;
            }
        }
        switch(containerVisualID) {
            case PackageBodyCompartmentEditPart.VISUAL_ID:
                if (CallGraphPackage.eINSTANCE.getAspect().isSuperTypeOf(domainElement.eClass())) {
                    return Aspect2EditPart.VISUAL_ID;
                }
                if (CallGraphPackage.eINSTANCE.getClass_().isSuperTypeOf(domainElement.eClass())) {
                    return Package2EditPart.VISUAL_ID;
                }
                if (CallGraphPackage.eINSTANCE.getInterface().isSuperTypeOf(domainElement.eClass())) {
                    return Interface3EditPart.VISUAL_ID;
                }
                if (CallGraphPackage.eINSTANCE.getPackage().isSuperTypeOf(domainElement.eClass())) {
                    return Package3EditPart.VISUAL_ID;
                }
                break;
            case AspectBodyCompartmentEditPart.VISUAL_ID:
                if (CallGraphPackage.eINSTANCE.getPointcut().isSuperTypeOf(domainElement.eClass())) {
                    return Pointcut2EditPart.VISUAL_ID;
                }
                if (CallGraphPackage.eINSTANCE.getAdvice().isSuperTypeOf(domainElement.eClass())) {
                    return Advice2EditPart.VISUAL_ID;
                }
                if (CallGraphPackage.eINSTANCE.getMethod().isSuperTypeOf(domainElement.eClass())) {
                    return Method2EditPart.VISUAL_ID;
                }
                if (CallGraphPackage.eINSTANCE.getField().isSuperTypeOf(domainElement.eClass())) {
                    return Field2EditPart.VISUAL_ID;
                }
                if (CallGraphPackage.eINSTANCE.getInterface().isSuperTypeOf(domainElement.eClass())) {
                    return Class2EditPart.VISUAL_ID;
                }
                if (CallGraphPackage.eINSTANCE.getClass_().isSuperTypeOf(domainElement.eClass())) {
                    return Interface2EditPart.VISUAL_ID;
                }
                if (CallGraphPackage.eINSTANCE.getAspect().isSuperTypeOf(domainElement.eClass())) {
                    return Method3EditPart.VISUAL_ID;
                }
                break;
            case ClassBodyCompartmentEditPart.VISUAL_ID:
                if (CallGraphPackage.eINSTANCE.getMethod().isSuperTypeOf(domainElement.eClass())) {
                    return Method2EditPart.VISUAL_ID;
                }
                if (CallGraphPackage.eINSTANCE.getField().isSuperTypeOf(domainElement.eClass())) {
                    return Field2EditPart.VISUAL_ID;
                }
                if (CallGraphPackage.eINSTANCE.getClass_().isSuperTypeOf(domainElement.eClass())) {
                    return Interface2EditPart.VISUAL_ID;
                }
                if (CallGraphPackage.eINSTANCE.getInterface().isSuperTypeOf(domainElement.eClass())) {
                    return Class2EditPart.VISUAL_ID;
                }
                if (CallGraphPackage.eINSTANCE.getAspect().isSuperTypeOf(domainElement.eClass())) {
                    return Method3EditPart.VISUAL_ID;
                }
                break;
            case InterfaceBodyCompartmentEditPart.VISUAL_ID:
                if (CallGraphPackage.eINSTANCE.getMethod().isSuperTypeOf(domainElement.eClass())) {
                    return Method2EditPart.VISUAL_ID;
                }
                if (CallGraphPackage.eINSTANCE.getField().isSuperTypeOf(domainElement.eClass())) {
                    return Field2EditPart.VISUAL_ID;
                }
                if (CallGraphPackage.eINSTANCE.getClass_().isSuperTypeOf(domainElement.eClass())) {
                    return Interface2EditPart.VISUAL_ID;
                }
                if (CallGraphPackage.eINSTANCE.getInterface().isSuperTypeOf(domainElement.eClass())) {
                    return Class2EditPart.VISUAL_ID;
                }
                if (CallGraphPackage.eINSTANCE.getAspect().isSuperTypeOf(domainElement.eClass())) {
                    return Method3EditPart.VISUAL_ID;
                }
                break;
            case PackageBodyCompartment2EditPart.VISUAL_ID:
                if (CallGraphPackage.eINSTANCE.getPointcut().isSuperTypeOf(domainElement.eClass())) {
                    return Pointcut2EditPart.VISUAL_ID;
                }
                if (CallGraphPackage.eINSTANCE.getAdvice().isSuperTypeOf(domainElement.eClass())) {
                    return Advice2EditPart.VISUAL_ID;
                }
                if (CallGraphPackage.eINSTANCE.getMethod().isSuperTypeOf(domainElement.eClass())) {
                    return Method2EditPart.VISUAL_ID;
                }
                if (CallGraphPackage.eINSTANCE.getField().isSuperTypeOf(domainElement.eClass())) {
                    return Field2EditPart.VISUAL_ID;
                }
                if (CallGraphPackage.eINSTANCE.getInterface().isSuperTypeOf(domainElement.eClass())) {
                    return Class2EditPart.VISUAL_ID;
                }
                if (CallGraphPackage.eINSTANCE.getClass_().isSuperTypeOf(domainElement.eClass())) {
                    return Interface2EditPart.VISUAL_ID;
                }
                if (CallGraphPackage.eINSTANCE.getAspect().isSuperTypeOf(domainElement.eClass())) {
                    return Method3EditPart.VISUAL_ID;
                }
                break;
            case AspectBodyCompartment2EditPart.VISUAL_ID:
                if (CallGraphPackage.eINSTANCE.getMethod().isSuperTypeOf(domainElement.eClass())) {
                    return Method2EditPart.VISUAL_ID;
                }
                if (CallGraphPackage.eINSTANCE.getField().isSuperTypeOf(domainElement.eClass())) {
                    return Field2EditPart.VISUAL_ID;
                }
                if (CallGraphPackage.eINSTANCE.getClass_().isSuperTypeOf(domainElement.eClass())) {
                    return Interface2EditPart.VISUAL_ID;
                }
                if (CallGraphPackage.eINSTANCE.getInterface().isSuperTypeOf(domainElement.eClass())) {
                    return Class2EditPart.VISUAL_ID;
                }
                if (CallGraphPackage.eINSTANCE.getAspect().isSuperTypeOf(domainElement.eClass())) {
                    return Method3EditPart.VISUAL_ID;
                }
                break;
            case ClassBodyCompartment2EditPart.VISUAL_ID:
                if (CallGraphPackage.eINSTANCE.getMethod().isSuperTypeOf(domainElement.eClass())) {
                    return Method2EditPart.VISUAL_ID;
                }
                if (CallGraphPackage.eINSTANCE.getField().isSuperTypeOf(domainElement.eClass())) {
                    return Field2EditPart.VISUAL_ID;
                }
                if (CallGraphPackage.eINSTANCE.getClass_().isSuperTypeOf(domainElement.eClass())) {
                    return Interface2EditPart.VISUAL_ID;
                }
                if (CallGraphPackage.eINSTANCE.getInterface().isSuperTypeOf(domainElement.eClass())) {
                    return Class2EditPart.VISUAL_ID;
                }
                if (CallGraphPackage.eINSTANCE.getAspect().isSuperTypeOf(domainElement.eClass())) {
                    return Method3EditPart.VISUAL_ID;
                }
                break;
            case InterfaceBodyCompartment2EditPart.VISUAL_ID:
                if (CallGraphPackage.eINSTANCE.getAspect().isSuperTypeOf(domainElement.eClass())) {
                    return Aspect2EditPart.VISUAL_ID;
                }
                if (CallGraphPackage.eINSTANCE.getClass_().isSuperTypeOf(domainElement.eClass())) {
                    return Package2EditPart.VISUAL_ID;
                }
                if (CallGraphPackage.eINSTANCE.getInterface().isSuperTypeOf(domainElement.eClass())) {
                    return Interface3EditPart.VISUAL_ID;
                }
                if (CallGraphPackage.eINSTANCE.getPackage().isSuperTypeOf(domainElement.eClass())) {
                    return Package3EditPart.VISUAL_ID;
                }
                break;
            case AspectBodyCompartment3EditPart.VISUAL_ID:
                if (CallGraphPackage.eINSTANCE.getPointcut().isSuperTypeOf(domainElement.eClass())) {
                    return Pointcut2EditPart.VISUAL_ID;
                }
                if (CallGraphPackage.eINSTANCE.getAdvice().isSuperTypeOf(domainElement.eClass())) {
                    return Advice2EditPart.VISUAL_ID;
                }
                if (CallGraphPackage.eINSTANCE.getMethod().isSuperTypeOf(domainElement.eClass())) {
                    return Method2EditPart.VISUAL_ID;
                }
                if (CallGraphPackage.eINSTANCE.getField().isSuperTypeOf(domainElement.eClass())) {
                    return Field2EditPart.VISUAL_ID;
                }
                if (CallGraphPackage.eINSTANCE.getInterface().isSuperTypeOf(domainElement.eClass())) {
                    return Class2EditPart.VISUAL_ID;
                }
                if (CallGraphPackage.eINSTANCE.getClass_().isSuperTypeOf(domainElement.eClass())) {
                    return Interface2EditPart.VISUAL_ID;
                }
                if (CallGraphPackage.eINSTANCE.getAspect().isSuperTypeOf(domainElement.eClass())) {
                    return Method3EditPart.VISUAL_ID;
                }
                break;
            case ClassBodyCompartment3EditPart.VISUAL_ID:
                if (CallGraphPackage.eINSTANCE.getMethod().isSuperTypeOf(domainElement.eClass())) {
                    return Method2EditPart.VISUAL_ID;
                }
                if (CallGraphPackage.eINSTANCE.getField().isSuperTypeOf(domainElement.eClass())) {
                    return Field2EditPart.VISUAL_ID;
                }
                if (CallGraphPackage.eINSTANCE.getClass_().isSuperTypeOf(domainElement.eClass())) {
                    return Interface2EditPart.VISUAL_ID;
                }
                if (CallGraphPackage.eINSTANCE.getInterface().isSuperTypeOf(domainElement.eClass())) {
                    return Class2EditPart.VISUAL_ID;
                }
                if (CallGraphPackage.eINSTANCE.getAspect().isSuperTypeOf(domainElement.eClass())) {
                    return Method3EditPart.VISUAL_ID;
                }
                break;
            case InterfaceBodyCompartment3EditPart.VISUAL_ID:
                if (CallGraphPackage.eINSTANCE.getMethod().isSuperTypeOf(domainElement.eClass())) {
                    return Method2EditPart.VISUAL_ID;
                }
                if (CallGraphPackage.eINSTANCE.getField().isSuperTypeOf(domainElement.eClass())) {
                    return Field2EditPart.VISUAL_ID;
                }
                if (CallGraphPackage.eINSTANCE.getClass_().isSuperTypeOf(domainElement.eClass())) {
                    return Interface2EditPart.VISUAL_ID;
                }
                if (CallGraphPackage.eINSTANCE.getInterface().isSuperTypeOf(domainElement.eClass())) {
                    return Class2EditPart.VISUAL_ID;
                }
                if (CallGraphPackage.eINSTANCE.getAspect().isSuperTypeOf(domainElement.eClass())) {
                    return Method3EditPart.VISUAL_ID;
                }
                break;
            case GraphEditPart.VISUAL_ID:
                if (CallGraphPackage.eINSTANCE.getPackage().isSuperTypeOf(domainElement.eClass())) {
                    return PackageEditPart.VISUAL_ID;
                }
                if (CallGraphPackage.eINSTANCE.getAspect().isSuperTypeOf(domainElement.eClass())) {
                    return AspectEditPart.VISUAL_ID;
                }
                if (CallGraphPackage.eINSTANCE.getClass_().isSuperTypeOf(domainElement.eClass())) {
                    return ClassEditPart.VISUAL_ID;
                }
                if (CallGraphPackage.eINSTANCE.getInterface().isSuperTypeOf(domainElement.eClass())) {
                    return InterfaceEditPart.VISUAL_ID;
                }
                if (CallGraphPackage.eINSTANCE.getAdvice().isSuperTypeOf(domainElement.eClass())) {
                    return AdviceEditPart.VISUAL_ID;
                }
                if (CallGraphPackage.eINSTANCE.getMethod().isSuperTypeOf(domainElement.eClass())) {
                    return MethodEditPart.VISUAL_ID;
                }
                if (CallGraphPackage.eINSTANCE.getField().isSuperTypeOf(domainElement.eClass())) {
                    return FieldEditPart.VISUAL_ID;
                }
                if (CallGraphPackage.eINSTANCE.getPointcut().isSuperTypeOf(domainElement.eClass())) {
                    return PointcutEditPart.VISUAL_ID;
                }
                break;
        }
        return -1;
    }

    /**
	 * @generated
	 */
    public static boolean canCreateNode(View containerView, int nodeVisualID) {
        String containerModelID = it.unisannio.rcost.callgraphanalyzer.diagram.part.CallGraphVisualIDRegistry.getModelID(containerView);
        if (!GraphEditPart.MODEL_ID.equals(containerModelID)) {
            return false;
        }
        int containerVisualID;
        if (GraphEditPart.MODEL_ID.equals(containerModelID)) {
            containerVisualID = it.unisannio.rcost.callgraphanalyzer.diagram.part.CallGraphVisualIDRegistry.getVisualID(containerView);
        } else {
            if (containerView instanceof Diagram) {
                containerVisualID = GraphEditPart.VISUAL_ID;
            } else {
                return false;
            }
        }
        switch(containerVisualID) {
            case PackageEditPart.VISUAL_ID:
                if (PackageNameEditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (PackageBodyCompartmentEditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                break;
            case AspectEditPart.VISUAL_ID:
                if (AspectNameEditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (AspectBodyCompartment3EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                break;
            case ClassEditPart.VISUAL_ID:
                if (ClassNameEditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (ClassBodyCompartment3EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                break;
            case InterfaceEditPart.VISUAL_ID:
                if (InterfaceNameEditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (InterfaceBodyCompartment3EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                break;
            case Aspect2EditPart.VISUAL_ID:
                if (AspectName2EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (AspectBodyCompartmentEditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                break;
            case Class2EditPart.VISUAL_ID:
                if (InterfaceName2EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (ClassBodyCompartmentEditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                break;
            case Interface2EditPart.VISUAL_ID:
                if (ClassName2EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (InterfaceBodyCompartmentEditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                break;
            case Method3EditPart.VISUAL_ID:
                if (AspectName3EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (PackageBodyCompartment2EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                break;
            case Package2EditPart.VISUAL_ID:
                if (ClassName3EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (AspectBodyCompartment2EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                break;
            case Interface3EditPart.VISUAL_ID:
                if (InterfaceName3EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (ClassBodyCompartment2EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                break;
            case Package3EditPart.VISUAL_ID:
                if (PackageName2EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (InterfaceBodyCompartment2EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                break;
            case PackageBodyCompartmentEditPart.VISUAL_ID:
                if (Aspect2EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (Package2EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (Interface3EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (Package3EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                break;
            case AspectBodyCompartmentEditPart.VISUAL_ID:
                if (Pointcut2EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (Advice2EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (Method2EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (Field2EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (Class2EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (Interface2EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (Method3EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                break;
            case ClassBodyCompartmentEditPart.VISUAL_ID:
                if (Method2EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (Field2EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (Interface2EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (Class2EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (Method3EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                break;
            case InterfaceBodyCompartmentEditPart.VISUAL_ID:
                if (Method2EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (Field2EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (Interface2EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (Class2EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (Method3EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                break;
            case PackageBodyCompartment2EditPart.VISUAL_ID:
                if (Pointcut2EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (Advice2EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (Method2EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (Field2EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (Class2EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (Interface2EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (Method3EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                break;
            case AspectBodyCompartment2EditPart.VISUAL_ID:
                if (Method2EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (Field2EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (Interface2EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (Class2EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (Method3EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                break;
            case ClassBodyCompartment2EditPart.VISUAL_ID:
                if (Method2EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (Field2EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (Interface2EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (Class2EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (Method3EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                break;
            case InterfaceBodyCompartment2EditPart.VISUAL_ID:
                if (Aspect2EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (Package2EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (Interface3EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (Package3EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                break;
            case AspectBodyCompartment3EditPart.VISUAL_ID:
                if (Pointcut2EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (Advice2EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (Method2EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (Field2EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (Class2EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (Interface2EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (Method3EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                break;
            case ClassBodyCompartment3EditPart.VISUAL_ID:
                if (Method2EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (Field2EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (Interface2EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (Class2EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (Method3EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                break;
            case InterfaceBodyCompartment3EditPart.VISUAL_ID:
                if (Method2EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (Field2EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (Interface2EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (Class2EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (Method3EditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                break;
            case GraphEditPart.VISUAL_ID:
                if (PackageEditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (AspectEditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (ClassEditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (InterfaceEditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (AdviceEditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (MethodEditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (FieldEditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                if (PointcutEditPart.VISUAL_ID == nodeVisualID) {
                    return true;
                }
                break;
            case ExplicitCallEditPart.VISUAL_ID:
                if (ExplicitCallGroupIdEditPart.VISUAL_ID == nodeVisualID) {
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
        if (CallGraphPackage.eINSTANCE.getExplicitCall().isSuperTypeOf(domainElement.eClass())) {
            return ExplicitCallEditPart.VISUAL_ID;
        }
        if (CallGraphPackage.eINSTANCE.getDependence().isSuperTypeOf(domainElement.eClass())) {
            return DependenceEditPart.VISUAL_ID;
        }
        if (CallGraphPackage.eINSTANCE.getAssociation().isSuperTypeOf(domainElement.eClass())) {
            return AssociationEditPart.VISUAL_ID;
        }
        if (CallGraphPackage.eINSTANCE.getExtension().isSuperTypeOf(domainElement.eClass())) {
            return ExtensionEditPart.VISUAL_ID;
        }
        if (CallGraphPackage.eINSTANCE.getImplicitCall().isSuperTypeOf(domainElement.eClass())) {
            return ImplicitCallEditPart.VISUAL_ID;
        }
        if (CallGraphPackage.eINSTANCE.getImplementation().isSuperTypeOf(domainElement.eClass())) {
            return ImplementationEditPart.VISUAL_ID;
        }
        return -1;
    }

    /**
	 * User can change implementation of this method to handle some specific
	 * situations not covered by default logic.
	 * 
	 * @generated
	 */
    private static boolean isDiagram(Graph element) {
        return true;
    }
}
