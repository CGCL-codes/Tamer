package jfb.examples.gmf.school.diagram.providers;

import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import jfb.examples.gmf.school.SchoolPackage;
import jfb.examples.gmf.school.diagram.edit.parts.ClassroomEditPart;
import jfb.examples.gmf.school.diagram.edit.parts.DiagramEditPart;
import jfb.examples.gmf.school.diagram.edit.parts.SchoolEditPart;
import jfb.examples.gmf.school.diagram.edit.parts.StudentEditPart;
import jfb.examples.gmf.school.diagram.edit.parts.StudentFriendsEditPart;
import jfb.examples.gmf.school.diagram.part.SchoolDiagramEditorPlugin;
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
public class SchoolElementTypes extends ElementInitializers {

    /**
	 * @generated
	 */
    private SchoolElementTypes() {
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
    public static final IElementType Diagram_1000 = getElementType("School.diagram.Diagram_1000");

    /**
	 * @generated
	 */
    public static final IElementType School_2001 = getElementType("School.diagram.School_2001");

    /**
	 * @generated
	 */
    public static final IElementType Classroom_3001 = getElementType("School.diagram.Classroom_3001");

    /**
	 * @generated
	 */
    public static final IElementType Student_3002 = getElementType("School.diagram.Student_3002");

    /**
	 * @generated
	 */
    public static final IElementType StudentFriends_4001 = getElementType("School.diagram.StudentFriends_4001");

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
                return SchoolDiagramEditorPlugin.getInstance().getItemImageDescriptor(eClass.getEPackage().getEFactoryInstance().create(eClass));
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
            elements.put(Diagram_1000, SchoolPackage.eINSTANCE.getDiagram());
            elements.put(School_2001, SchoolPackage.eINSTANCE.getSchool());
            elements.put(Classroom_3001, SchoolPackage.eINSTANCE.getClassroom());
            elements.put(Student_3002, SchoolPackage.eINSTANCE.getStudent());
            elements.put(StudentFriends_4001, SchoolPackage.eINSTANCE.getStudent_Friends());
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
            KNOWN_ELEMENT_TYPES.add(Diagram_1000);
            KNOWN_ELEMENT_TYPES.add(School_2001);
            KNOWN_ELEMENT_TYPES.add(Classroom_3001);
            KNOWN_ELEMENT_TYPES.add(Student_3002);
            KNOWN_ELEMENT_TYPES.add(StudentFriends_4001);
        }
        return KNOWN_ELEMENT_TYPES.contains(elementType);
    }

    /**
	 * @generated
	 */
    public static IElementType getElementType(int visualID) {
        switch(visualID) {
            case DiagramEditPart.VISUAL_ID:
                return Diagram_1000;
            case SchoolEditPart.VISUAL_ID:
                return School_2001;
            case ClassroomEditPart.VISUAL_ID:
                return Classroom_3001;
            case StudentEditPart.VISUAL_ID:
                return Student_3002;
            case StudentFriendsEditPart.VISUAL_ID:
                return StudentFriends_4001;
        }
        return null;
    }
}
