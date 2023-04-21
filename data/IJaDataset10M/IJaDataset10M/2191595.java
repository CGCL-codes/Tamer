package it.unisannio.rcost.callgraphanalyzer.diagram.providers;

import it.unisannio.rcost.callgraphanalyzer.CallGraphPackage;
import it.unisannio.rcost.callgraphanalyzer.diagram.part.CallGraphDiagramEditorPlugin;
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

/**
 * @generated
 */
public class CallGraphElementTypes extends ElementInitializers {

    /**
	 * @generated
	 */
    private CallGraphElementTypes() {
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
    public static final IElementType Graph_79 = getElementType("CallGraphAnalyzer.gmf.diagram.Graph_79");

    /**
	 * @generated
	 */
    public static final IElementType Package_1001 = getElementType("CallGraphAnalyzer.gmf.diagram.Package_1001");

    /**
	 * @generated
	 */
    public static final IElementType Aspect_1002 = getElementType("CallGraphAnalyzer.gmf.diagram.Aspect_1002");

    /**
	 * @generated
	 */
    public static final IElementType Class_1003 = getElementType("CallGraphAnalyzer.gmf.diagram.Class_1003");

    /**
	 * @generated
	 */
    public static final IElementType Interface_1004 = getElementType("CallGraphAnalyzer.gmf.diagram.Interface_1004");

    /**
	 * @generated
	 */
    public static final IElementType Advice_1005 = getElementType("CallGraphAnalyzer.gmf.diagram.Advice_1005");

    /**
	 * @generated
	 */
    public static final IElementType Method_1006 = getElementType("CallGraphAnalyzer.gmf.diagram.Method_1006");

    /**
	 * @generated
	 */
    public static final IElementType Field_1007 = getElementType("CallGraphAnalyzer.gmf.diagram.Field_1007");

    /**
	 * @generated
	 */
    public static final IElementType Pointcut_1008 = getElementType("CallGraphAnalyzer.gmf.diagram.Pointcut_1008");

    /**
	 * @generated
	 */
    public static final IElementType Aspect_2001 = getElementType("CallGraphAnalyzer.gmf.diagram.Aspect_2001");

    /**
	 * @generated
	 */
    public static final IElementType Pointcut_2002 = getElementType("CallGraphAnalyzer.gmf.diagram.Pointcut_2002");

    /**
	 * @generated
	 */
    public static final IElementType Advice_2003 = getElementType("CallGraphAnalyzer.gmf.diagram.Advice_2003");

    /**
	 * @generated
	 */
    public static final IElementType Method_2004 = getElementType("CallGraphAnalyzer.gmf.diagram.Method_2004");

    /**
	 * @generated
	 */
    public static final IElementType Field_2005 = getElementType("CallGraphAnalyzer.gmf.diagram.Field_2005");

    /**
	 * @generated
	 */
    public static final IElementType Interface_2006 = getElementType("CallGraphAnalyzer.gmf.diagram.Interface_2006");

    /**
	 * @generated
	 */
    public static final IElementType Class_2007 = getElementType("CallGraphAnalyzer.gmf.diagram.Class_2007");

    /**
	 * @generated
	 */
    public static final IElementType Aspect_2008 = getElementType("CallGraphAnalyzer.gmf.diagram.Aspect_2008");

    /**
	 * @generated
	 */
    public static final IElementType Class_2009 = getElementType("CallGraphAnalyzer.gmf.diagram.Class_2009");

    /**
	 * @generated
	 */
    public static final IElementType Interface_2010 = getElementType("CallGraphAnalyzer.gmf.diagram.Interface_2010");

    /**
	 * @generated
	 */
    public static final IElementType Package_2011 = getElementType("CallGraphAnalyzer.gmf.diagram.Package_2011");

    /**
	 * @generated
	 */
    public static final IElementType ExplicitCall_3001 = getElementType("CallGraphAnalyzer.gmf.diagram.ExplicitCall_3001");

    /**
	 * @generated
	 */
    public static final IElementType Dependence_3002 = getElementType("CallGraphAnalyzer.gmf.diagram.Dependence_3002");

    /**
	 * @generated
	 */
    public static final IElementType Association_3003 = getElementType("CallGraphAnalyzer.gmf.diagram.Association_3003");

    /**
	 * @generated
	 */
    public static final IElementType Extension_3004 = getElementType("CallGraphAnalyzer.gmf.diagram.Extension_3004");

    /**
	 * @generated
	 */
    public static final IElementType ImplicitCall_3005 = getElementType("CallGraphAnalyzer.gmf.diagram.ImplicitCall_3005");

    /**
	 * @generated
	 */
    public static final IElementType Implementation_3006 = getElementType("CallGraphAnalyzer.gmf.diagram.Implementation_3006");

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
                return CallGraphDiagramEditorPlugin.getInstance().getItemImageDescriptor(eClass.getEPackage().getEFactoryInstance().create(eClass));
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
            elements.put(Graph_79, CallGraphPackage.eINSTANCE.getGraph());
            elements.put(Package_1001, CallGraphPackage.eINSTANCE.getPackage());
            elements.put(Aspect_1002, CallGraphPackage.eINSTANCE.getAspect());
            elements.put(Class_1003, CallGraphPackage.eINSTANCE.getClass_());
            elements.put(Interface_1004, CallGraphPackage.eINSTANCE.getInterface());
            elements.put(Advice_1005, CallGraphPackage.eINSTANCE.getAdvice());
            elements.put(Method_1006, CallGraphPackage.eINSTANCE.getMethod());
            elements.put(Field_1007, CallGraphPackage.eINSTANCE.getField());
            elements.put(Pointcut_1008, CallGraphPackage.eINSTANCE.getPointcut());
            elements.put(Aspect_2001, CallGraphPackage.eINSTANCE.getAspect());
            elements.put(Pointcut_2002, CallGraphPackage.eINSTANCE.getPointcut());
            elements.put(Advice_2003, CallGraphPackage.eINSTANCE.getAdvice());
            elements.put(Method_2004, CallGraphPackage.eINSTANCE.getMethod());
            elements.put(Field_2005, CallGraphPackage.eINSTANCE.getField());
            elements.put(Interface_2006, CallGraphPackage.eINSTANCE.getInterface());
            elements.put(Class_2007, CallGraphPackage.eINSTANCE.getClass_());
            elements.put(Aspect_2008, CallGraphPackage.eINSTANCE.getAspect());
            elements.put(Class_2009, CallGraphPackage.eINSTANCE.getClass_());
            elements.put(Interface_2010, CallGraphPackage.eINSTANCE.getInterface());
            elements.put(Package_2011, CallGraphPackage.eINSTANCE.getPackage());
            elements.put(ExplicitCall_3001, CallGraphPackage.eINSTANCE.getExplicitCall());
            elements.put(Dependence_3002, CallGraphPackage.eINSTANCE.getDependence());
            elements.put(Association_3003, CallGraphPackage.eINSTANCE.getAssociation());
            elements.put(Extension_3004, CallGraphPackage.eINSTANCE.getExtension());
            elements.put(ImplicitCall_3005, CallGraphPackage.eINSTANCE.getImplicitCall());
            elements.put(Implementation_3006, CallGraphPackage.eINSTANCE.getImplementation());
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
            KNOWN_ELEMENT_TYPES.add(Graph_79);
            KNOWN_ELEMENT_TYPES.add(Package_1001);
            KNOWN_ELEMENT_TYPES.add(Aspect_1002);
            KNOWN_ELEMENT_TYPES.add(Class_1003);
            KNOWN_ELEMENT_TYPES.add(Interface_1004);
            KNOWN_ELEMENT_TYPES.add(Advice_1005);
            KNOWN_ELEMENT_TYPES.add(Method_1006);
            KNOWN_ELEMENT_TYPES.add(Field_1007);
            KNOWN_ELEMENT_TYPES.add(Pointcut_1008);
            KNOWN_ELEMENT_TYPES.add(Aspect_2001);
            KNOWN_ELEMENT_TYPES.add(Pointcut_2002);
            KNOWN_ELEMENT_TYPES.add(Advice_2003);
            KNOWN_ELEMENT_TYPES.add(Method_2004);
            KNOWN_ELEMENT_TYPES.add(Field_2005);
            KNOWN_ELEMENT_TYPES.add(Interface_2006);
            KNOWN_ELEMENT_TYPES.add(Class_2007);
            KNOWN_ELEMENT_TYPES.add(Aspect_2008);
            KNOWN_ELEMENT_TYPES.add(Class_2009);
            KNOWN_ELEMENT_TYPES.add(Interface_2010);
            KNOWN_ELEMENT_TYPES.add(Package_2011);
            KNOWN_ELEMENT_TYPES.add(ExplicitCall_3001);
            KNOWN_ELEMENT_TYPES.add(Dependence_3002);
            KNOWN_ELEMENT_TYPES.add(Association_3003);
            KNOWN_ELEMENT_TYPES.add(Extension_3004);
            KNOWN_ELEMENT_TYPES.add(ImplicitCall_3005);
            KNOWN_ELEMENT_TYPES.add(Implementation_3006);
        }
        return KNOWN_ELEMENT_TYPES.contains(elementType);
    }
}
