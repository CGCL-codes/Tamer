package org.spbu.pldoctoolkit.graph.diagram.productline.providers;

import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.spbu.pldoctoolkit.graph.DrlPackage;
import org.spbu.pldoctoolkit.graph.diagram.productline.part.DrlModelDiagramEditorPlugin;

/**
 * @generated
 */
public class DrlModelElementTypes extends ElementInitializers {

    /**
	 * @generated
	 */
    private DrlModelElementTypes() {
    }

    /**
	 * @generated
	 */
    private static Map elements;

    /**
	 * @generated
	 */
    private static ImageRegistry imageRegistry;

    /**
	 * @generated
	 */
    private static Set KNOWN_ELEMENT_TYPES;

    /**
	 * @generated
	 */
    public static final IElementType ProductLine_79 = getElementType("org.spbu.pldoctoolkit.graph.diagram.productline.ProductLine_79");

    /**
	 * @generated
	 */
    public static final IElementType ProductLine_1001 = getElementType("org.spbu.pldoctoolkit.graph.diagram.productline.ProductLine_1001");

    /**
	 * @generated
	 */
    public static final IElementType Node_2001 = getElementType("org.spbu.pldoctoolkit.graph.diagram.productline.Node_2001");

    /**
	 * @generated
	 */
    public static final IElementType Node_2002 = getElementType("org.spbu.pldoctoolkit.graph.diagram.productline.Node_2002");

    /**
	 * @generated
	 */
    public static final IElementType Product_2003 = getElementType("org.spbu.pldoctoolkit.graph.diagram.productline.Product_2003");

    /**
	 * @generated
	 */
    public static final IElementType Node_2004 = getElementType("org.spbu.pldoctoolkit.graph.diagram.productline.Node_2004");

    /**
	 * @generated
	 */
    public static final IElementType InfProduct_2005 = getElementType("org.spbu.pldoctoolkit.graph.diagram.productline.InfProduct_2005");

    /**
	 * @generated
	 */
    public static final IElementType ProductDocumentationFinalInfProducts_3001 = getElementType("org.spbu.pldoctoolkit.graph.diagram.productline.ProductInfProductLink_3001");

    /**
	 * @generated
	 */
    private static ImageRegistry getImageRegistry() {
        if (imageRegistry == null) {
            imageRegistry = new ImageRegistry();
        }
        return imageRegistry;
    }

    /**
	 * @generated
	 */
    private static String getImageRegistryKey(ENamedElement element) {
        return element.getName();
    }

    /**
	 * @generated
	 */
    private static ImageDescriptor getProvidedImageDescriptor(ENamedElement element) {
        if (element instanceof EStructuralFeature) {
            EStructuralFeature feature = ((EStructuralFeature) element);
            EClass eContainingClass = feature.getEContainingClass();
            EClassifier eType = feature.getEType();
            if (eContainingClass != null && !eContainingClass.isAbstract()) {
                element = eContainingClass;
            } else if (eType instanceof EClass && !((EClass) eType).isAbstract()) {
                element = eType;
            }
        }
        if (element instanceof EClass) {
            EClass eClass = (EClass) element;
            if (!eClass.isAbstract()) {
                return DrlModelDiagramEditorPlugin.getInstance().getItemImageDescriptor(eClass.getEPackage().getEFactoryInstance().create(eClass));
            }
        }
        return null;
    }

    /**
	 * @generated
	 */
    public static ImageDescriptor getImageDescriptor(ENamedElement element) {
        String key = getImageRegistryKey(element);
        ImageDescriptor imageDescriptor = getImageRegistry().getDescriptor(key);
        if (imageDescriptor == null) {
            imageDescriptor = getProvidedImageDescriptor(element);
            if (imageDescriptor == null) {
                imageDescriptor = ImageDescriptor.getMissingImageDescriptor();
            }
            getImageRegistry().put(key, imageDescriptor);
        }
        return imageDescriptor;
    }

    /**
	 * @generated
	 */
    public static Image getImage(ENamedElement element) {
        String key = getImageRegistryKey(element);
        Image image = getImageRegistry().get(key);
        if (image == null) {
            ImageDescriptor imageDescriptor = getProvidedImageDescriptor(element);
            if (imageDescriptor == null) {
                imageDescriptor = ImageDescriptor.getMissingImageDescriptor();
            }
            getImageRegistry().put(key, imageDescriptor);
            image = getImageRegistry().get(key);
        }
        return image;
    }

    /**
	 * @generated
	 */
    public static ImageDescriptor getImageDescriptor(IAdaptable hint) {
        ENamedElement element = getElement(hint);
        if (element == null) {
            return null;
        }
        return getImageDescriptor(element);
    }

    /**
	 * @generated
	 */
    public static Image getImage(IAdaptable hint) {
        ENamedElement element = getElement(hint);
        if (element == null) {
            return null;
        }
        return getImage(element);
    }

    /**
	 * Returns 'type' of the ecore object associated with the hint.
	 * 
	 * @generated
	 */
    public static ENamedElement getElement(IAdaptable hint) {
        Object type = hint.getAdapter(IElementType.class);
        if (elements == null) {
            elements = new IdentityHashMap();
            elements.put(ProductLine_79, DrlPackage.eINSTANCE.getProductLine());
            elements.put(ProductLine_1001, DrlPackage.eINSTANCE.getProductLine());
            elements.put(Product_2003, DrlPackage.eINSTANCE.getProduct());
            elements.put(InfProduct_2005, DrlPackage.eINSTANCE.getInfProduct());
            elements.put(ProductDocumentationFinalInfProducts_3001, DrlPackage.eINSTANCE.getProductDocumentation_FinalInfProducts());
        }
        return (ENamedElement) elements.get(type);
    }

    /**
	 * @generated
	 */
    private static IElementType getElementType(String id) {
        return ElementTypeRegistry.getInstance().getType(id);
    }

    /**
	 * @generated
	 */
    public static boolean isKnownElementType(IElementType elementType) {
        if (KNOWN_ELEMENT_TYPES == null) {
            KNOWN_ELEMENT_TYPES = new HashSet();
            KNOWN_ELEMENT_TYPES.add(ProductLine_79);
            KNOWN_ELEMENT_TYPES.add(ProductLine_1001);
            KNOWN_ELEMENT_TYPES.add(Node_2001);
            KNOWN_ELEMENT_TYPES.add(Node_2002);
            KNOWN_ELEMENT_TYPES.add(Product_2003);
            KNOWN_ELEMENT_TYPES.add(Node_2004);
            KNOWN_ELEMENT_TYPES.add(InfProduct_2005);
            KNOWN_ELEMENT_TYPES.add(ProductDocumentationFinalInfProducts_3001);
        }
        return KNOWN_ELEMENT_TYPES.contains(elementType);
    }
}
