package se.mdh.mrtc.saveccm.diagram.providers;

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
import se.mdh.mrtc.saveccm.SaveccmPackage;
import se.mdh.mrtc.saveccm.diagram.part.SaveccmDiagramEditorPlugin;

/**
 * @generated
 */
public class SaveccmElementTypes extends ElementInitializers {

    /**
	 * @generated
	 */
    private SaveccmElementTypes() {
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
    public static final IElementType System_79 = getElementType("se.mdh.mrtc.saveccm.diagram.System_79");

    /**
	 * @generated
	 */
    public static final IElementType Delay_1001 = getElementType("se.mdh.mrtc.saveccm.diagram.Delay_1001");

    /**
	 * @generated
	 */
    public static final IElementType Switch_1002 = getElementType("se.mdh.mrtc.saveccm.diagram.Switch_1002");

    /**
	 * @generated
	 */
    public static final IElementType Clock_1003 = getElementType("se.mdh.mrtc.saveccm.diagram.Clock_1003");

    /**
	 * @generated
	 */
    public static final IElementType Composite_1004 = getElementType("se.mdh.mrtc.saveccm.diagram.Composite_1004");

    /**
	 * @generated
	 */
    public static final IElementType Assembly_1005 = getElementType("se.mdh.mrtc.saveccm.diagram.Assembly_1005");

    /**
	 * @generated
	 */
    public static final IElementType Component_1006 = getElementType("se.mdh.mrtc.saveccm.diagram.Component_1006");

    /**
	 * @generated
	 */
    public static final IElementType ConnectionComplex_1007 = getElementType("se.mdh.mrtc.saveccm.diagram.ConnectionComplex_1007");

    /**
	 * @generated
	 */
    public static final IElementType TriggerIn_1008 = getElementType("se.mdh.mrtc.saveccm.diagram.TriggerIn_1008");

    /**
	 * @generated
	 */
    public static final IElementType TriggerOut_1009 = getElementType("se.mdh.mrtc.saveccm.diagram.TriggerOut_1009");

    /**
	 * @generated
	 */
    public static final IElementType DataIn_1010 = getElementType("se.mdh.mrtc.saveccm.diagram.DataIn_1010");

    /**
	 * @generated
	 */
    public static final IElementType DataOut_1011 = getElementType("se.mdh.mrtc.saveccm.diagram.DataOut_1011");

    /**
	 * @generated
	 */
    public static final IElementType CombinedIn_1012 = getElementType("se.mdh.mrtc.saveccm.diagram.CombinedIn_1012");

    /**
	 * @generated
	 */
    public static final IElementType CombinedOut_1013 = getElementType("se.mdh.mrtc.saveccm.diagram.CombinedOut_1013");

    /**
	 * @generated
	 */
    public static final IElementType Model_1014 = getElementType("se.mdh.mrtc.saveccm.diagram.Model_1014");

    /**
	 * @generated
	 */
    public static final IElementType Attribut_1015 = getElementType("se.mdh.mrtc.saveccm.diagram.Attribut_1015");

    /**
	 * @generated
	 */
    public static final IElementType TriggerIn_2001 = getElementType("se.mdh.mrtc.saveccm.diagram.TriggerIn_2001");

    /**
	 * @generated
	 */
    public static final IElementType TriggerOut_2002 = getElementType("se.mdh.mrtc.saveccm.diagram.TriggerOut_2002");

    /**
	 * @generated
	 */
    public static final IElementType DataIn_2003 = getElementType("se.mdh.mrtc.saveccm.diagram.DataIn_2003");

    /**
	 * @generated
	 */
    public static final IElementType DataOut_2004 = getElementType("se.mdh.mrtc.saveccm.diagram.DataOut_2004");

    /**
	 * @generated
	 */
    public static final IElementType CombinedIn_2005 = getElementType("se.mdh.mrtc.saveccm.diagram.CombinedIn_2005");

    /**
	 * @generated
	 */
    public static final IElementType CombinedOut_2006 = getElementType("se.mdh.mrtc.saveccm.diagram.CombinedOut_2006");

    /**
	 * @generated
	 */
    public static final IElementType Model_2007 = getElementType("se.mdh.mrtc.saveccm.diagram.Model_2007");

    /**
	 * @generated
	 */
    public static final IElementType Attribut_2008 = getElementType("se.mdh.mrtc.saveccm.diagram.Attribut_2008");

    /**
	 * @generated
	 */
    public static final IElementType TriggerIn_2009 = getElementType("se.mdh.mrtc.saveccm.diagram.TriggerIn_2009");

    /**
	 * @generated
	 */
    public static final IElementType TriggerOut_2010 = getElementType("se.mdh.mrtc.saveccm.diagram.TriggerOut_2010");

    /**
	 * @generated
	 */
    public static final IElementType DataIn_2011 = getElementType("se.mdh.mrtc.saveccm.diagram.DataIn_2011");

    /**
	 * @generated
	 */
    public static final IElementType DataOut_2012 = getElementType("se.mdh.mrtc.saveccm.diagram.DataOut_2012");

    /**
	 * @generated
	 */
    public static final IElementType CombinedIn_2013 = getElementType("se.mdh.mrtc.saveccm.diagram.CombinedIn_2013");

    /**
	 * @generated
	 */
    public static final IElementType CombinedOut_2014 = getElementType("se.mdh.mrtc.saveccm.diagram.CombinedOut_2014");

    /**
	 * @generated
	 */
    public static final IElementType TriggerIn_2015 = getElementType("se.mdh.mrtc.saveccm.diagram.TriggerIn_2015");

    /**
	 * @generated
	 */
    public static final IElementType TriggerOut_2016 = getElementType("se.mdh.mrtc.saveccm.diagram.TriggerOut_2016");

    /**
	 * @generated
	 */
    public static final IElementType DataIn_2017 = getElementType("se.mdh.mrtc.saveccm.diagram.DataIn_2017");

    /**
	 * @generated
	 */
    public static final IElementType DataOut_2018 = getElementType("se.mdh.mrtc.saveccm.diagram.DataOut_2018");

    /**
	 * @generated
	 */
    public static final IElementType CombinedIn_2019 = getElementType("se.mdh.mrtc.saveccm.diagram.CombinedIn_2019");

    /**
	 * @generated
	 */
    public static final IElementType CombinedOut_2020 = getElementType("se.mdh.mrtc.saveccm.diagram.CombinedOut_2020");

    /**
	 * @generated
	 */
    public static final IElementType Model_2021 = getElementType("se.mdh.mrtc.saveccm.diagram.Model_2021");

    /**
	 * @generated
	 */
    public static final IElementType Attribut_2022 = getElementType("se.mdh.mrtc.saveccm.diagram.Attribut_2022");

    /**
	 * @generated
	 */
    public static final IElementType TriggerIn_2023 = getElementType("se.mdh.mrtc.saveccm.diagram.TriggerIn_2023");

    /**
	 * @generated
	 */
    public static final IElementType TriggerOut_2024 = getElementType("se.mdh.mrtc.saveccm.diagram.TriggerOut_2024");

    /**
	 * @generated
	 */
    public static final IElementType DataIn_2025 = getElementType("se.mdh.mrtc.saveccm.diagram.DataIn_2025");

    /**
	 * @generated
	 */
    public static final IElementType DataOut_2026 = getElementType("se.mdh.mrtc.saveccm.diagram.DataOut_2026");

    /**
	 * @generated
	 */
    public static final IElementType CombinedIn_2027 = getElementType("se.mdh.mrtc.saveccm.diagram.CombinedIn_2027");

    /**
	 * @generated
	 */
    public static final IElementType CombinedOut_2028 = getElementType("se.mdh.mrtc.saveccm.diagram.CombinedOut_2028");

    /**
	 * @generated
	 */
    public static final IElementType Model_2029 = getElementType("se.mdh.mrtc.saveccm.diagram.Model_2029");

    /**
	 * @generated
	 */
    public static final IElementType Attribut_2030 = getElementType("se.mdh.mrtc.saveccm.diagram.Attribut_2030");

    /**
	 * @generated
	 */
    public static final IElementType TriggerIn_2031 = getElementType("se.mdh.mrtc.saveccm.diagram.TriggerIn_2031");

    /**
	 * @generated
	 */
    public static final IElementType TriggerOut_2032 = getElementType("se.mdh.mrtc.saveccm.diagram.TriggerOut_2032");

    /**
	 * @generated
	 */
    public static final IElementType DataIn_2033 = getElementType("se.mdh.mrtc.saveccm.diagram.DataIn_2033");

    /**
	 * @generated
	 */
    public static final IElementType DataOut_2034 = getElementType("se.mdh.mrtc.saveccm.diagram.DataOut_2034");

    /**
	 * @generated
	 */
    public static final IElementType CombinedIn_2035 = getElementType("se.mdh.mrtc.saveccm.diagram.CombinedIn_2035");

    /**
	 * @generated
	 */
    public static final IElementType CombinedOut_2036 = getElementType("se.mdh.mrtc.saveccm.diagram.CombinedOut_2036");

    /**
	 * @generated
	 */
    public static final IElementType TriggerIn_2037 = getElementType("se.mdh.mrtc.saveccm.diagram.TriggerIn_2037");

    /**
	 * @generated
	 */
    public static final IElementType TriggerOut_2038 = getElementType("se.mdh.mrtc.saveccm.diagram.TriggerOut_2038");

    /**
	 * @generated
	 */
    public static final IElementType DataIn_2039 = getElementType("se.mdh.mrtc.saveccm.diagram.DataIn_2039");

    /**
	 * @generated
	 */
    public static final IElementType DataOut_2040 = getElementType("se.mdh.mrtc.saveccm.diagram.DataOut_2040");

    /**
	 * @generated
	 */
    public static final IElementType CombinedIn_2041 = getElementType("se.mdh.mrtc.saveccm.diagram.CombinedIn_2041");

    /**
	 * @generated
	 */
    public static final IElementType CombinedOut_2042 = getElementType("se.mdh.mrtc.saveccm.diagram.CombinedOut_2042");

    /**
	 * @generated
	 */
    public static final IElementType Model_2043 = getElementType("se.mdh.mrtc.saveccm.diagram.Model_2043");

    /**
	 * @generated
	 */
    public static final IElementType Attribut_2044 = getElementType("se.mdh.mrtc.saveccm.diagram.Attribut_2044");

    /**
	 * @generated
	 */
    public static final IElementType BindPort_2045 = getElementType("se.mdh.mrtc.saveccm.diagram.BindPort_2045");

    /**
	 * @generated
	 */
    public static final IElementType Model_2046 = getElementType("se.mdh.mrtc.saveccm.diagram.Model_2046");

    /**
	 * @generated
	 */
    public static final IElementType Connection_3001 = getElementType("se.mdh.mrtc.saveccm.diagram.Connection_3001");

    /**
	 * @generated
	 */
    public static final IElementType Delegation_3002 = getElementType("se.mdh.mrtc.saveccm.diagram.Delegation_3002");

    /**
	 * @generated
	 */
    public static final IElementType ConnectionComplexComplexTo_3003 = getElementType("se.mdh.mrtc.saveccm.diagram.ConnectionComplexComplexTo_3003");

    /**
	 * @generated
	 */
    public static final IElementType ConnectionComplexComplexFrom_3004 = getElementType("se.mdh.mrtc.saveccm.diagram.ConnectionComplexComplexFrom_3004");

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
                return SaveccmDiagramEditorPlugin.getInstance().getItemImageDescriptor(eClass.getEPackage().getEFactoryInstance().create(eClass));
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
            elements.put(System_79, SaveccmPackage.eINSTANCE.getSystem());
            elements.put(Delay_1001, SaveccmPackage.eINSTANCE.getDelay());
            elements.put(Switch_1002, SaveccmPackage.eINSTANCE.getSwitch());
            elements.put(Clock_1003, SaveccmPackage.eINSTANCE.getClock());
            elements.put(Composite_1004, SaveccmPackage.eINSTANCE.getComposite());
            elements.put(Assembly_1005, SaveccmPackage.eINSTANCE.getAssembly());
            elements.put(Component_1006, SaveccmPackage.eINSTANCE.getComponent());
            elements.put(ConnectionComplex_1007, SaveccmPackage.eINSTANCE.getConnectionComplex());
            elements.put(TriggerIn_1008, SaveccmPackage.eINSTANCE.getTriggerIn());
            elements.put(TriggerOut_1009, SaveccmPackage.eINSTANCE.getTriggerOut());
            elements.put(DataIn_1010, SaveccmPackage.eINSTANCE.getDataIn());
            elements.put(DataOut_1011, SaveccmPackage.eINSTANCE.getDataOut());
            elements.put(CombinedIn_1012, SaveccmPackage.eINSTANCE.getCombinedIn());
            elements.put(CombinedOut_1013, SaveccmPackage.eINSTANCE.getCombinedOut());
            elements.put(Model_1014, SaveccmPackage.eINSTANCE.getModel());
            elements.put(Attribut_1015, SaveccmPackage.eINSTANCE.getAttribut());
            elements.put(TriggerIn_2001, SaveccmPackage.eINSTANCE.getTriggerIn());
            elements.put(TriggerOut_2002, SaveccmPackage.eINSTANCE.getTriggerOut());
            elements.put(DataIn_2003, SaveccmPackage.eINSTANCE.getDataIn());
            elements.put(DataOut_2004, SaveccmPackage.eINSTANCE.getDataOut());
            elements.put(CombinedIn_2005, SaveccmPackage.eINSTANCE.getCombinedIn());
            elements.put(CombinedOut_2006, SaveccmPackage.eINSTANCE.getCombinedOut());
            elements.put(Model_2007, SaveccmPackage.eINSTANCE.getModel());
            elements.put(Attribut_2008, SaveccmPackage.eINSTANCE.getAttribut());
            elements.put(TriggerIn_2009, SaveccmPackage.eINSTANCE.getTriggerIn());
            elements.put(TriggerOut_2010, SaveccmPackage.eINSTANCE.getTriggerOut());
            elements.put(DataIn_2011, SaveccmPackage.eINSTANCE.getDataIn());
            elements.put(DataOut_2012, SaveccmPackage.eINSTANCE.getDataOut());
            elements.put(CombinedIn_2013, SaveccmPackage.eINSTANCE.getCombinedIn());
            elements.put(CombinedOut_2014, SaveccmPackage.eINSTANCE.getCombinedOut());
            elements.put(TriggerIn_2015, SaveccmPackage.eINSTANCE.getTriggerIn());
            elements.put(TriggerOut_2016, SaveccmPackage.eINSTANCE.getTriggerOut());
            elements.put(DataIn_2017, SaveccmPackage.eINSTANCE.getDataIn());
            elements.put(DataOut_2018, SaveccmPackage.eINSTANCE.getDataOut());
            elements.put(CombinedIn_2019, SaveccmPackage.eINSTANCE.getCombinedIn());
            elements.put(CombinedOut_2020, SaveccmPackage.eINSTANCE.getCombinedOut());
            elements.put(Model_2021, SaveccmPackage.eINSTANCE.getModel());
            elements.put(Attribut_2022, SaveccmPackage.eINSTANCE.getAttribut());
            elements.put(TriggerIn_2023, SaveccmPackage.eINSTANCE.getTriggerIn());
            elements.put(TriggerOut_2024, SaveccmPackage.eINSTANCE.getTriggerOut());
            elements.put(DataIn_2025, SaveccmPackage.eINSTANCE.getDataIn());
            elements.put(DataOut_2026, SaveccmPackage.eINSTANCE.getDataOut());
            elements.put(CombinedIn_2027, SaveccmPackage.eINSTANCE.getCombinedIn());
            elements.put(CombinedOut_2028, SaveccmPackage.eINSTANCE.getCombinedOut());
            elements.put(Model_2029, SaveccmPackage.eINSTANCE.getModel());
            elements.put(Attribut_2030, SaveccmPackage.eINSTANCE.getAttribut());
            elements.put(TriggerIn_2031, SaveccmPackage.eINSTANCE.getTriggerIn());
            elements.put(TriggerOut_2032, SaveccmPackage.eINSTANCE.getTriggerOut());
            elements.put(DataIn_2033, SaveccmPackage.eINSTANCE.getDataIn());
            elements.put(DataOut_2034, SaveccmPackage.eINSTANCE.getDataOut());
            elements.put(CombinedIn_2035, SaveccmPackage.eINSTANCE.getCombinedIn());
            elements.put(CombinedOut_2036, SaveccmPackage.eINSTANCE.getCombinedOut());
            elements.put(TriggerIn_2037, SaveccmPackage.eINSTANCE.getTriggerIn());
            elements.put(TriggerOut_2038, SaveccmPackage.eINSTANCE.getTriggerOut());
            elements.put(DataIn_2039, SaveccmPackage.eINSTANCE.getDataIn());
            elements.put(DataOut_2040, SaveccmPackage.eINSTANCE.getDataOut());
            elements.put(CombinedIn_2041, SaveccmPackage.eINSTANCE.getCombinedIn());
            elements.put(CombinedOut_2042, SaveccmPackage.eINSTANCE.getCombinedOut());
            elements.put(Model_2043, SaveccmPackage.eINSTANCE.getModel());
            elements.put(Attribut_2044, SaveccmPackage.eINSTANCE.getAttribut());
            elements.put(BindPort_2045, SaveccmPackage.eINSTANCE.getBindPort());
            elements.put(Model_2046, SaveccmPackage.eINSTANCE.getModel());
            elements.put(Connection_3001, SaveccmPackage.eINSTANCE.getConnection());
            elements.put(Delegation_3002, SaveccmPackage.eINSTANCE.getDelegation());
            elements.put(ConnectionComplexComplexTo_3003, SaveccmPackage.eINSTANCE.getConnectionComplex_ComplexTo());
            elements.put(ConnectionComplexComplexFrom_3004, SaveccmPackage.eINSTANCE.getConnectionComplex_ComplexFrom());
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
            KNOWN_ELEMENT_TYPES.add(System_79);
            KNOWN_ELEMENT_TYPES.add(Delay_1001);
            KNOWN_ELEMENT_TYPES.add(Switch_1002);
            KNOWN_ELEMENT_TYPES.add(Clock_1003);
            KNOWN_ELEMENT_TYPES.add(Composite_1004);
            KNOWN_ELEMENT_TYPES.add(Assembly_1005);
            KNOWN_ELEMENT_TYPES.add(Component_1006);
            KNOWN_ELEMENT_TYPES.add(ConnectionComplex_1007);
            KNOWN_ELEMENT_TYPES.add(TriggerIn_1008);
            KNOWN_ELEMENT_TYPES.add(TriggerOut_1009);
            KNOWN_ELEMENT_TYPES.add(DataIn_1010);
            KNOWN_ELEMENT_TYPES.add(DataOut_1011);
            KNOWN_ELEMENT_TYPES.add(CombinedIn_1012);
            KNOWN_ELEMENT_TYPES.add(CombinedOut_1013);
            KNOWN_ELEMENT_TYPES.add(Model_1014);
            KNOWN_ELEMENT_TYPES.add(Attribut_1015);
            KNOWN_ELEMENT_TYPES.add(TriggerIn_2001);
            KNOWN_ELEMENT_TYPES.add(TriggerOut_2002);
            KNOWN_ELEMENT_TYPES.add(DataIn_2003);
            KNOWN_ELEMENT_TYPES.add(DataOut_2004);
            KNOWN_ELEMENT_TYPES.add(CombinedIn_2005);
            KNOWN_ELEMENT_TYPES.add(CombinedOut_2006);
            KNOWN_ELEMENT_TYPES.add(Model_2007);
            KNOWN_ELEMENT_TYPES.add(Attribut_2008);
            KNOWN_ELEMENT_TYPES.add(TriggerIn_2009);
            KNOWN_ELEMENT_TYPES.add(TriggerOut_2010);
            KNOWN_ELEMENT_TYPES.add(DataIn_2011);
            KNOWN_ELEMENT_TYPES.add(DataOut_2012);
            KNOWN_ELEMENT_TYPES.add(CombinedIn_2013);
            KNOWN_ELEMENT_TYPES.add(CombinedOut_2014);
            KNOWN_ELEMENT_TYPES.add(TriggerIn_2015);
            KNOWN_ELEMENT_TYPES.add(TriggerOut_2016);
            KNOWN_ELEMENT_TYPES.add(DataIn_2017);
            KNOWN_ELEMENT_TYPES.add(DataOut_2018);
            KNOWN_ELEMENT_TYPES.add(CombinedIn_2019);
            KNOWN_ELEMENT_TYPES.add(CombinedOut_2020);
            KNOWN_ELEMENT_TYPES.add(Model_2021);
            KNOWN_ELEMENT_TYPES.add(Attribut_2022);
            KNOWN_ELEMENT_TYPES.add(TriggerIn_2023);
            KNOWN_ELEMENT_TYPES.add(TriggerOut_2024);
            KNOWN_ELEMENT_TYPES.add(DataIn_2025);
            KNOWN_ELEMENT_TYPES.add(DataOut_2026);
            KNOWN_ELEMENT_TYPES.add(CombinedIn_2027);
            KNOWN_ELEMENT_TYPES.add(CombinedOut_2028);
            KNOWN_ELEMENT_TYPES.add(Model_2029);
            KNOWN_ELEMENT_TYPES.add(Attribut_2030);
            KNOWN_ELEMENT_TYPES.add(TriggerIn_2031);
            KNOWN_ELEMENT_TYPES.add(TriggerOut_2032);
            KNOWN_ELEMENT_TYPES.add(DataIn_2033);
            KNOWN_ELEMENT_TYPES.add(DataOut_2034);
            KNOWN_ELEMENT_TYPES.add(CombinedIn_2035);
            KNOWN_ELEMENT_TYPES.add(CombinedOut_2036);
            KNOWN_ELEMENT_TYPES.add(TriggerIn_2037);
            KNOWN_ELEMENT_TYPES.add(TriggerOut_2038);
            KNOWN_ELEMENT_TYPES.add(DataIn_2039);
            KNOWN_ELEMENT_TYPES.add(DataOut_2040);
            KNOWN_ELEMENT_TYPES.add(CombinedIn_2041);
            KNOWN_ELEMENT_TYPES.add(CombinedOut_2042);
            KNOWN_ELEMENT_TYPES.add(Model_2043);
            KNOWN_ELEMENT_TYPES.add(Attribut_2044);
            KNOWN_ELEMENT_TYPES.add(BindPort_2045);
            KNOWN_ELEMENT_TYPES.add(Model_2046);
            KNOWN_ELEMENT_TYPES.add(Connection_3001);
            KNOWN_ELEMENT_TYPES.add(Delegation_3002);
            KNOWN_ELEMENT_TYPES.add(ConnectionComplexComplexTo_3003);
            KNOWN_ELEMENT_TYPES.add(ConnectionComplexComplexFrom_3004);
        }
        return KNOWN_ELEMENT_TYPES.contains(elementType);
    }
}
