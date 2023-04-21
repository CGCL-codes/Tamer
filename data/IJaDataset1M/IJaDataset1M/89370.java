package net.sourceforge.dynamicgmf.diagram.part;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import net.sourceforge.dynamicgmf.diagram.edit.parts.EClassEditPart;
import net.sourceforge.dynamicgmf.diagram.edit.parts.EContainmentEditPart;
import net.sourceforge.dynamicgmf.diagram.edit.parts.EReferenceEditPart;
import net.sourceforge.dynamicgmf.diagram.edit.parts.RootEditPart;
import net.sourceforge.dynamicgmf.diagram.providers.EcoreModelElementTypes;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @generated
 */
public class EcoreModelDiagramUpdater {

    /**
	 * @generated NOT
	 */
    public static List<EcoreModelNodeDescriptor> getSemanticChildren(View view) {
        switch(EcoreModelVisualIDRegistry.getVisualID(view)) {
            case RootEditPart.VISUAL_ID:
                return getRoot_SemanticChildren(view);
        }
        return Collections.EMPTY_LIST;
    }

    /**
	 * @generated NOT
	 */
    public static List<EcoreModelNodeDescriptor> getRoot_SemanticChildren(View view) {
        if (!view.isSetElement()) {
            return Collections.EMPTY_LIST;
        }
        EObject modelElement = view.getElement();
        EClass rootClass = modelElement.eClass();
        List<EcoreModelNodeDescriptor> result = new LinkedList<EcoreModelNodeDescriptor>();
        {
            for (EReference eContainment : rootClass.getEAllContainments()) {
                if (eContainment.isMany()) {
                    EList childElements = (EList) modelElement.eGet(eContainment);
                    for (Iterator iter = childElements.iterator(); iter.hasNext(); ) {
                        EObject childElement = (EObject) iter.next();
                        int visualID = EcoreModelVisualIDRegistry.getNodeVisualID(view, childElement);
                        if (visualID == EClassEditPart.VISUAL_ID) {
                            result.add(new EcoreModelNodeDescriptor(childElement, visualID));
                        }
                    }
                } else {
                    EObject childElement = (EObject) modelElement.eGet(eContainment);
                    int visualID = EcoreModelVisualIDRegistry.getNodeVisualID(view, childElement);
                    if (visualID == EClassEditPart.VISUAL_ID) {
                        result.add(new EcoreModelNodeDescriptor(childElement, visualID));
                    }
                }
            }
        }
        Resource resource = modelElement.eResource();
        for (Iterator<EObject> semanticIterator = getPhantomNodesIterator(resource); semanticIterator.hasNext(); ) {
            EObject childElement = semanticIterator.next();
            if (childElement == modelElement) {
                continue;
            }
            if (childElement.eContainer() == modelElement) {
                continue;
            }
            if (childElement.eClass().getName().equals("DocumentRoot")) {
                continue;
            }
            if (childElement.eContainer() != null && childElement.eContainer().eClass().getName().equals("DocumentRoot")) {
                continue;
            }
            if (EcoreModelVisualIDRegistry.getNodeVisualID(view, childElement) == EClassEditPart.VISUAL_ID) {
                result.add(new EcoreModelNodeDescriptor(childElement, EClassEditPart.VISUAL_ID));
                continue;
            }
        }
        return result;
    }

    /**
	 * @generated NOT
	 */
    private static Iterator<EObject> getPhantomNodesIterator(Resource resource) {
        return resource.getAllContents();
    }

    /**
	 * @generated NOT
	 */
    public static List<EcoreModelLinkDescriptor> getContainedLinks(View view) {
        switch(EcoreModelVisualIDRegistry.getVisualID(view)) {
            case RootEditPart.VISUAL_ID:
                return getRoot_ContainedLinks(view);
            case EClassEditPart.VISUAL_ID:
                return getEClass_ContainedLinks(view);
        }
        return Collections.EMPTY_LIST;
    }

    /**
	 * @generated NOT
	 */
    public static List<EcoreModelLinkDescriptor> getIncomingLinks(View view) {
        switch(EcoreModelVisualIDRegistry.getVisualID(view)) {
            case EClassEditPart.VISUAL_ID:
                return getEClass_IncomingLinks(view);
        }
        return Collections.EMPTY_LIST;
    }

    /**
	 * @generated NOT
	 */
    public static List<EcoreModelLinkDescriptor> getOutgoingLinks(View view) {
        switch(EcoreModelVisualIDRegistry.getVisualID(view)) {
            case EClassEditPart.VISUAL_ID:
                return getEClass_OutgoingLinks(view);
        }
        return Collections.EMPTY_LIST;
    }

    /**
	 * @generated NOT
	 */
    public static List<EcoreModelLinkDescriptor> getRoot_ContainedLinks(View view) {
        return Collections.EMPTY_LIST;
    }

    public static List<EcoreModelLinkDescriptor> getEClass_ContainedLinks(View view) {
        EObject modelElement = view.getElement();
        List<EcoreModelLinkDescriptor> result = new LinkedList<EcoreModelLinkDescriptor>();
        result.addAll(getOutgoingFeatureModelFacetLinks(modelElement));
        return result;
    }

    public static List<EcoreModelLinkDescriptor> getEClass_IncomingLinks(View view) {
        EObject modelElement = view.getElement();
        Map<EObject, Collection<Setting>> crossReferences = EcoreUtil.CrossReferencer.find(view.eResource().getResourceSet().getResources());
        List<EcoreModelLinkDescriptor> result = new LinkedList<EcoreModelLinkDescriptor>();
        result.addAll(getIncomingFeatureModelFacetLinks(modelElement, crossReferences));
        return result;
    }

    public static List<EcoreModelLinkDescriptor> getEClass_OutgoingLinks(View view) {
        EObject modelElement = view.getElement();
        List<EcoreModelLinkDescriptor> result = new LinkedList<EcoreModelLinkDescriptor>();
        result.addAll(getOutgoingFeatureModelFacetLinks(modelElement));
        return result;
    }

    private static Collection<EcoreModelLinkDescriptor> getIncomingFeatureModelFacetLinks(EObject target, Map<EObject, Collection<Setting>> crossReferences) {
        Collection<EcoreModelLinkDescriptor> result = new LinkedList<EcoreModelLinkDescriptor>();
        EClass eClass = target.eClass();
        Collection<Setting> settings = crossReferences.get(target);
        for (EStructuralFeature.Setting setting : settings) {
            EReference eReference = (EReference) setting.getEStructuralFeature();
            if (eClass.getEAllReferences().contains(eReference)) {
                addLinkDescriptor(result, setting.getEObject(), target, eReference);
            }
        }
        return result;
    }

    private static Collection<EcoreModelLinkDescriptor> getOutgoingFeatureModelFacetLinks(EObject source) {
        Collection<EcoreModelLinkDescriptor> result = new LinkedList<EcoreModelLinkDescriptor>();
        EClass eClass = source.eClass();
        for (EReference eReference : eClass.getEAllReferences()) {
            if (eReference.isMany()) {
                for (EObject target : (Collection<EObject>) source.eGet(eReference)) {
                    addLinkDescriptor(result, source, target, eReference);
                }
            } else {
                EObject target = (EObject) source.eGet(eReference);
                addLinkDescriptor(result, source, target, eReference);
            }
        }
        return result;
    }

    private static void addLinkDescriptor(Collection<EcoreModelLinkDescriptor> result, EObject source, EObject target, EReference eReference) {
        if (eReference.isContainment()) {
            result.add(new EcoreModelLinkDescriptor(source, target, EcoreModelElementTypes.getElementType(eReference), EContainmentEditPart.VISUAL_ID));
        } else {
            result.add(new EcoreModelLinkDescriptor(source, target, EcoreModelElementTypes.getElementType(eReference), EReferenceEditPart.VISUAL_ID));
        }
    }
}
