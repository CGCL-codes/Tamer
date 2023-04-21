package org.xteam.box2text.box.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.xteam.box2text.box.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class BoxFactoryImpl extends EFactoryImpl implements BoxFactory {

    /**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public static BoxFactory init() {
        try {
            BoxFactory theBoxFactory = (BoxFactory) EPackage.Registry.INSTANCE.getEFactory("http://www.xteam.org/box");
            if (theBoxFactory != null) {
                return theBoxFactory;
            }
        } catch (Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new BoxFactoryImpl();
    }

    /**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public BoxFactoryImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public EObject create(EClass eClass) {
        switch(eClass.getClassifierID()) {
            case BoxPackage.BOX:
                return createBox();
            case BoxPackage.OPERATOR:
                return createOperator();
            case BoxPackage.OPTION:
                return createOption();
            case BoxPackage.TEXT_BOX:
                return createTextBox();
            case BoxPackage.AOPERATOR:
                return createAOperator();
            case BoxPackage.ALIGNMENT_OPTION:
                return createAlignmentOption();
            case BoxPackage.IOPERATOR:
                return createIOperator();
            case BoxPackage.HOPERATOR:
                return createHOperator();
            case BoxPackage.VOPERATOR:
                return createVOperator();
            case BoxPackage.GOPERATOR:
                return createGOperator();
            case BoxPackage.SL_OPERATOR:
                return createSLOperator();
            case BoxPackage.HOV_OPERATOR:
                return createHOVOperator();
            case BoxPackage.HV_OPERATOR:
                return createHVOperator();
            case BoxPackage.ROPERATOR:
                return createROperator();
            case BoxPackage.WD_OPERATOR:
                return createWDOperator();
            default:
                throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
        }
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object createFromString(EDataType eDataType, String initialValue) {
        switch(eDataType.getClassifierID()) {
            case BoxPackage.ALIGN:
                return createAlignFromString(eDataType, initialValue);
            case BoxPackage.OPERATOR_KINDS:
                return createOperatorKindsFromString(eDataType, initialValue);
            default:
                throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
        }
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public String convertToString(EDataType eDataType, Object instanceValue) {
        switch(eDataType.getClassifierID()) {
            case BoxPackage.ALIGN:
                return convertAlignToString(eDataType, instanceValue);
            case BoxPackage.OPERATOR_KINDS:
                return convertOperatorKindsToString(eDataType, instanceValue);
            default:
                throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
        }
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Box createBox() {
        BoxImpl box = new BoxImpl();
        return box;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Operator createOperator() {
        OperatorImpl operator = new OperatorImpl();
        return operator;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Option createOption() {
        OptionImpl option = new OptionImpl();
        return option;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public TextBox createTextBox() {
        TextBoxImpl textBox = new TextBoxImpl();
        return textBox;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public AOperator createAOperator() {
        AOperatorImpl aOperator = new AOperatorImpl();
        return aOperator;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public AlignmentOption createAlignmentOption() {
        AlignmentOptionImpl alignmentOption = new AlignmentOptionImpl();
        return alignmentOption;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public IOperator createIOperator() {
        IOperatorImpl iOperator = new IOperatorImpl();
        return iOperator;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public HOperator createHOperator() {
        HOperatorImpl hOperator = new HOperatorImpl();
        return hOperator;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public VOperator createVOperator() {
        VOperatorImpl vOperator = new VOperatorImpl();
        return vOperator;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public GOperator createGOperator() {
        GOperatorImpl gOperator = new GOperatorImpl();
        return gOperator;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public SLOperator createSLOperator() {
        SLOperatorImpl slOperator = new SLOperatorImpl();
        return slOperator;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public HOVOperator createHOVOperator() {
        HOVOperatorImpl hovOperator = new HOVOperatorImpl();
        return hovOperator;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public HVOperator createHVOperator() {
        HVOperatorImpl hvOperator = new HVOperatorImpl();
        return hvOperator;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ROperator createROperator() {
        ROperatorImpl rOperator = new ROperatorImpl();
        return rOperator;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public WDOperator createWDOperator() {
        WDOperatorImpl wdOperator = new WDOperatorImpl();
        return wdOperator;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Align createAlignFromString(EDataType eDataType, String initialValue) {
        Align result = Align.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String convertAlignToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public OperatorKinds createOperatorKindsFromString(EDataType eDataType, String initialValue) {
        OperatorKinds result = OperatorKinds.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String convertOperatorKindsToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public BoxPackage getBoxPackage() {
        return (BoxPackage) getEPackage();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
    @Deprecated
    public static BoxPackage getPackage() {
        return BoxPackage.eINSTANCE;
    }
}
