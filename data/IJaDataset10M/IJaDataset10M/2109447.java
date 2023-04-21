package bpmn.diagram.providers;

import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import bpmn.BpmnPackage;
import bpmn.diagram.expressions.BpmnAbstractExpression;
import bpmn.diagram.expressions.BpmnOCLFactory;
import bpmn.diagram.part.BpmnDiagramEditorPlugin;

/**
 * @generated
 */
public class BpmnElementTypes {

    /**
	 * @generated
	 */
    private BpmnElementTypes() {
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
            element = ((EStructuralFeature) element).getEContainingClass();
        }
        if (element instanceof EClass) {
            EClass eClass = (EClass) element;
            if (!eClass.isAbstract()) {
                return BpmnDiagramEditorPlugin.getInstance().getItemImageDescriptor(eClass.getEPackage().getEFactoryInstance().create(eClass));
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
            elements.put(BPMNDiagram_79, BpmnPackage.eINSTANCE.getBPMNDiagram());
            elements.put(ServiceTask_2001, BpmnPackage.eINSTANCE.getServiceTask());
            elements.put(ReceiveTask_2002, BpmnPackage.eINSTANCE.getReceiveTask());
            elements.put(SendTask_2003, BpmnPackage.eINSTANCE.getSendTask());
            elements.put(UserTask_2004, BpmnPackage.eINSTANCE.getUserTask());
            elements.put(ScriptTask_2005, BpmnPackage.eINSTANCE.getScriptTask());
            elements.put(ManualTask_2006, BpmnPackage.eINSTANCE.getManualTask());
            elements.put(ReferenceTask_2007, BpmnPackage.eINSTANCE.getReferenceTask());
            elements.put(Task_2008, BpmnPackage.eINSTANCE.getTask());
            elements.put(XORGateway_2009, BpmnPackage.eINSTANCE.getXORGateway());
            elements.put(ANDGateway_2010, BpmnPackage.eINSTANCE.getANDGateway());
            elements.put(NoneEvent_2011, BpmnPackage.eINSTANCE.getNoneEvent());
            elements.put(MessageEvent_2012, BpmnPackage.eINSTANCE.getMessageEvent());
            elements.put(TimerEvent_2013, BpmnPackage.eINSTANCE.getTimerEvent());
            elements.put(RuleEvent_2014, BpmnPackage.eINSTANCE.getRuleEvent());
            elements.put(LinkEvent_2015, BpmnPackage.eINSTANCE.getLinkEvent());
            elements.put(MultipleEvent_2016, BpmnPackage.eINSTANCE.getMultipleEvent());
            elements.put(ErrorEvent_2017, BpmnPackage.eINSTANCE.getErrorEvent());
            elements.put(CancelEvent_2018, BpmnPackage.eINSTANCE.getCancelEvent());
            elements.put(CompensationEvent_2019, BpmnPackage.eINSTANCE.getCompensationEvent());
            elements.put(TerminateEvent_2020, BpmnPackage.eINSTANCE.getTerminateEvent());
            elements.put(DataObject_2021, BpmnPackage.eINSTANCE.getDataObject());
            elements.put(ORGateway_2022, BpmnPackage.eINSTANCE.getORGateway());
            elements.put(Process_1001, BpmnPackage.eINSTANCE.getProcess());
            elements.put(SequenceFlow_3001, BpmnPackage.eINSTANCE.getSequenceFlow());
            elements.put(Association_3002, BpmnPackage.eINSTANCE.getAssociation());
            elements.put(MessageFlow_3003, BpmnPackage.eINSTANCE.getMessageFlow());
        }
        return (ENamedElement) elements.get(type);
    }

    /**
	 * @generated
	 */
    public static final IElementType BPMNDiagram_79 = getElementType("org.bpmn.diagram.BPMNDiagram_79");

    /**
	 * @generated
	 */
    public static final IElementType ServiceTask_2001 = getElementType("org.bpmn.diagram.ServiceTask_2001");

    /**
	 * @generated
	 */
    public static final IElementType ReceiveTask_2002 = getElementType("org.bpmn.diagram.ReceiveTask_2002");

    /**
	 * @generated
	 */
    public static final IElementType SendTask_2003 = getElementType("org.bpmn.diagram.SendTask_2003");

    /**
	 * @generated
	 */
    public static final IElementType UserTask_2004 = getElementType("org.bpmn.diagram.UserTask_2004");

    /**
	 * @generated
	 */
    public static final IElementType ScriptTask_2005 = getElementType("org.bpmn.diagram.ScriptTask_2005");

    /**
	 * @generated
	 */
    public static final IElementType ManualTask_2006 = getElementType("org.bpmn.diagram.ManualTask_2006");

    /**
	 * @generated
	 */
    public static final IElementType ReferenceTask_2007 = getElementType("org.bpmn.diagram.ReferenceTask_2007");

    /**
	 * @generated
	 */
    public static final IElementType Task_2008 = getElementType("org.bpmn.diagram.Task_2008");

    /**
	 * @generated
	 */
    public static final IElementType XORGateway_2009 = getElementType("org.bpmn.diagram.XORGateway_2009");

    /**
	 * @generated
	 */
    public static final IElementType ANDGateway_2010 = getElementType("org.bpmn.diagram.ANDGateway_2010");

    /**
	 * @generated
	 */
    public static final IElementType NoneEvent_2011 = getElementType("org.bpmn.diagram.NoneEvent_2011");

    /**
	 * @generated
	 */
    public static final IElementType MessageEvent_2012 = getElementType("org.bpmn.diagram.MessageEvent_2012");

    /**
	 * @generated
	 */
    public static final IElementType TimerEvent_2013 = getElementType("org.bpmn.diagram.TimerEvent_2013");

    /**
	 * @generated
	 */
    public static final IElementType RuleEvent_2014 = getElementType("org.bpmn.diagram.RuleEvent_2014");

    /**
	 * @generated
	 */
    public static final IElementType LinkEvent_2015 = getElementType("org.bpmn.diagram.LinkEvent_2015");

    /**
	 * @generated
	 */
    public static final IElementType MultipleEvent_2016 = getElementType("org.bpmn.diagram.MultipleEvent_2016");

    /**
	 * @generated
	 */
    public static final IElementType ErrorEvent_2017 = getElementType("org.bpmn.diagram.ErrorEvent_2017");

    /**
	 * @generated
	 */
    public static final IElementType CancelEvent_2018 = getElementType("org.bpmn.diagram.CancelEvent_2018");

    /**
	 * @generated
	 */
    public static final IElementType CompensationEvent_2019 = getElementType("org.bpmn.diagram.CompensationEvent_2019");

    /**
	 * @generated
	 */
    public static final IElementType TerminateEvent_2020 = getElementType("org.bpmn.diagram.TerminateEvent_2020");

    /**
	 * @generated
	 */
    public static final IElementType DataObject_2021 = getElementType("org.bpmn.diagram.DataObject_2021");

    /**
	 * @generated
	 */
    public static final IElementType ORGateway_2022 = getElementType("org.bpmn.diagram.ORGateway_2022");

    /**
	 * @generated
	 */
    public static final IElementType Process_1001 = getElementType("org.bpmn.diagram.Process_1001");

    /**
	 * @generated
	 */
    public static final IElementType SequenceFlow_3001 = getElementType("org.bpmn.diagram.SequenceFlow_3001");

    /**
	 * @generated
	 */
    public static final IElementType Association_3002 = getElementType("org.bpmn.diagram.Association_3002");

    /**
	 * @generated
	 */
    public static final IElementType MessageFlow_3003 = getElementType("org.bpmn.diagram.MessageFlow_3003");

    public static final Object StartEvent_2011 = null;

    public static final Object StartEvent_2015 = null;

    public static final Object StartEvent_2014 = null;

    public static final Object StartEvent_2013 = null;

    public static final Object StartEvent_2012 = null;

    public static final Object IntermediateEvent_2016 = null;

    public static final Object IntermediateEvent_2023 = null;

    public static final Object IntermediateEvent_2022 = null;

    public static final Object IntermediateEvent_2021 = null;

    public static final Object IntermediateEvent_2020 = null;

    public static final Object IntermediateEvent_2019 = null;

    public static final Object IntermediateEvent_2018 = null;

    public static final Object IntermediateEvent_2017 = null;

    public static final Object EndEvent_2024 = null;

    public static final Object EndEvent_2030 = null;

    public static final Object EndEvent_2029 = null;

    public static final Object EndEvent_2028 = null;

    public static final Object EndEvent_2027 = null;

    public static final Object EndEvent_2026 = null;

    public static final Object EndEvent_2025 = null;

    /**
	 * @generated
	 */
    private static IElementType getElementType(String id) {
        return ElementTypeRegistry.getInstance().getType(id);
    }

    /**
	 * @generated
	 */
    private static Set KNOWN_ELEMENT_TYPES;

    /**
	 * @generated
	 */
    public static boolean isKnownElementType(IElementType elementType) {
        if (KNOWN_ELEMENT_TYPES == null) {
            KNOWN_ELEMENT_TYPES = new HashSet();
            KNOWN_ELEMENT_TYPES.add(BPMNDiagram_79);
            KNOWN_ELEMENT_TYPES.add(ServiceTask_2001);
            KNOWN_ELEMENT_TYPES.add(ReceiveTask_2002);
            KNOWN_ELEMENT_TYPES.add(SendTask_2003);
            KNOWN_ELEMENT_TYPES.add(UserTask_2004);
            KNOWN_ELEMENT_TYPES.add(ScriptTask_2005);
            KNOWN_ELEMENT_TYPES.add(ManualTask_2006);
            KNOWN_ELEMENT_TYPES.add(ReferenceTask_2007);
            KNOWN_ELEMENT_TYPES.add(Task_2008);
            KNOWN_ELEMENT_TYPES.add(XORGateway_2009);
            KNOWN_ELEMENT_TYPES.add(ANDGateway_2010);
            KNOWN_ELEMENT_TYPES.add(NoneEvent_2011);
            KNOWN_ELEMENT_TYPES.add(MessageEvent_2012);
            KNOWN_ELEMENT_TYPES.add(TimerEvent_2013);
            KNOWN_ELEMENT_TYPES.add(RuleEvent_2014);
            KNOWN_ELEMENT_TYPES.add(LinkEvent_2015);
            KNOWN_ELEMENT_TYPES.add(MultipleEvent_2016);
            KNOWN_ELEMENT_TYPES.add(ErrorEvent_2017);
            KNOWN_ELEMENT_TYPES.add(CancelEvent_2018);
            KNOWN_ELEMENT_TYPES.add(CompensationEvent_2019);
            KNOWN_ELEMENT_TYPES.add(TerminateEvent_2020);
            KNOWN_ELEMENT_TYPES.add(DataObject_2021);
            KNOWN_ELEMENT_TYPES.add(ORGateway_2022);
            KNOWN_ELEMENT_TYPES.add(Process_1001);
            KNOWN_ELEMENT_TYPES.add(SequenceFlow_3001);
            KNOWN_ELEMENT_TYPES.add(Association_3002);
            KNOWN_ELEMENT_TYPES.add(MessageFlow_3003);
        }
        return KNOWN_ELEMENT_TYPES.contains(elementType);
    }
}
