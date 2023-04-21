package fr.esrf.tango.pogo.pogoDsl.impl;

import fr.esrf.tango.pogo.pogoDsl.*;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class PogoDslFactoryImpl extends EFactoryImpl implements PogoDslFactory {

    /**
   * Creates the default factory implementation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public static PogoDslFactory init() {
        try {
            PogoDslFactory thePogoDslFactory = (PogoDslFactory) EPackage.Registry.INSTANCE.getEFactory("http://www.esrf.fr/tango/pogo/PogoDsl");
            if (thePogoDslFactory != null) {
                return thePogoDslFactory;
            }
        } catch (Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new PogoDslFactoryImpl();
    }

    /**
   * Creates an instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public PogoDslFactoryImpl() {
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
            case PogoDslPackage.POGO_SYSTEM:
                return createPogoSystem();
            case PogoDslPackage.IMPORT:
                return createImport();
            case PogoDslPackage.POGO_MULTI_CLASSES:
                return createPogoMultiClasses();
            case PogoDslPackage.ONE_CLASS_SIMPLE_DEF:
                return createOneClassSimpleDef();
            case PogoDslPackage.POGO_DEVICE_CLASS:
                return createPogoDeviceClass();
            case PogoDslPackage.CLASS_DESCRIPTION:
                return createClassDescription();
            case PogoDslPackage.INHERITANCE:
                return createInheritance();
            case PogoDslPackage.CLASS_IDENTIFICATION:
                return createClassIdentification();
            case PogoDslPackage.COMMENTS:
                return createComments();
            case PogoDslPackage.PREFERENCES:
                return createPreferences();
            case PogoDslPackage.STATE:
                return createState();
            case PogoDslPackage.PROPERTY:
                return createProperty();
            case PogoDslPackage.PROP_TYPE:
                return createPropType();
            case PogoDslPackage.SIMPLE_TYPE:
                return createSimpleType();
            case PogoDslPackage.VECTOR_TYPE:
                return createVectorType();
            case PogoDslPackage.INHERITANCE_STATUS:
                return createInheritanceStatus();
            case PogoDslPackage.COMMAND:
                return createCommand();
            case PogoDslPackage.ARGUMENT:
                return createArgument();
            case PogoDslPackage.ATTRIBUTE:
                return createAttribute();
            case PogoDslPackage.FIRE_EVENTS:
                return createFireEvents();
            case PogoDslPackage.ATTR_PROPERTIES:
                return createAttrProperties();
            case PogoDslPackage.ADDITIONAL_FILE:
                return createAdditionalFile();
            case PogoDslPackage.OVERLODED_POLL_PERIOD_OBJECT:
                return createOverlodedPollPeriodObject();
            case PogoDslPackage.TYPE:
                return createType();
            case PogoDslPackage.VOID_TYPE:
                return createVoidType();
            case PogoDslPackage.BOOLEAN_TYPE:
                return createBooleanType();
            case PogoDslPackage.SHORT_TYPE:
                return createShortType();
            case PogoDslPackage.USHORT_TYPE:
                return createUShortType();
            case PogoDslPackage.INT_TYPE:
                return createIntType();
            case PogoDslPackage.UINT_TYPE:
                return createUIntType();
            case PogoDslPackage.FLOAT_TYPE:
                return createFloatType();
            case PogoDslPackage.DOUBLE_TYPE:
                return createDoubleType();
            case PogoDslPackage.STRING_TYPE:
                return createStringType();
            case PogoDslPackage.CHAR_ARRAY_TYPE:
                return createCharArrayType();
            case PogoDslPackage.SHORT_ARRAY_TYPE:
                return createShortArrayType();
            case PogoDslPackage.USHORT_ARRAY_TYPE:
                return createUShortArrayType();
            case PogoDslPackage.INT_ARRAY_TYPE:
                return createIntArrayType();
            case PogoDslPackage.UINT_ARRAY_TYPE:
                return createUIntArrayType();
            case PogoDslPackage.FLOAT_ARRAY_TYPE:
                return createFloatArrayType();
            case PogoDslPackage.DOUBLE_ARRAY_TYPE:
                return createDoubleArrayType();
            case PogoDslPackage.STRING_ARRAY_TYPE:
                return createStringArrayType();
            case PogoDslPackage.LONG_STRING_ARRAY_TYPE:
                return createLongStringArrayType();
            case PogoDslPackage.DOUBLE_STRING_ARRAY_TYPE:
                return createDoubleStringArrayType();
            case PogoDslPackage.STATE_TYPE:
                return createStateType();
            case PogoDslPackage.CONST_STRING_TYPE:
                return createConstStringType();
            case PogoDslPackage.BOOLEAN_ARRAY_TYPE:
                return createBooleanArrayType();
            case PogoDslPackage.UCHAR_TYPE:
                return createUCharType();
            case PogoDslPackage.LONG_TYPE:
                return createLongType();
            case PogoDslPackage.ULONG_TYPE:
                return createULongType();
            case PogoDslPackage.LONG_ARRAY_TYPE:
                return createLongArrayType();
            case PogoDslPackage.ULONG_ARRAY_TYPE:
                return createULongArrayType();
            case PogoDslPackage.DEV_INT_TYPE:
                return createDevIntType();
            case PogoDslPackage.ENCODED_TYPE:
                return createEncodedType();
            case PogoDslPackage.SHORT_VECTOR_TYPE:
                return createShortVectorType();
            case PogoDslPackage.INT_VECTOR_TYPE:
                return createIntVectorType();
            case PogoDslPackage.FLOAT_VECTOR_TYPE:
                return createFloatVectorType();
            case PogoDslPackage.DOUBLE_VECTOR_TYPE:
                return createDoubleVectorType();
            case PogoDslPackage.STRING_VECTOR_TYPE:
                return createStringVectorType();
            default:
                throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
        }
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public PogoSystem createPogoSystem() {
        PogoSystemImpl pogoSystem = new PogoSystemImpl();
        return pogoSystem;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public Import createImport() {
        ImportImpl import_ = new ImportImpl();
        return import_;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public PogoMultiClasses createPogoMultiClasses() {
        PogoMultiClassesImpl pogoMultiClasses = new PogoMultiClassesImpl();
        return pogoMultiClasses;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public OneClassSimpleDef createOneClassSimpleDef() {
        OneClassSimpleDefImpl oneClassSimpleDef = new OneClassSimpleDefImpl();
        return oneClassSimpleDef;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public PogoDeviceClass createPogoDeviceClass() {
        PogoDeviceClassImpl pogoDeviceClass = new PogoDeviceClassImpl();
        return pogoDeviceClass;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public ClassDescription createClassDescription() {
        ClassDescriptionImpl classDescription = new ClassDescriptionImpl();
        return classDescription;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public Inheritance createInheritance() {
        InheritanceImpl inheritance = new InheritanceImpl();
        return inheritance;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public ClassIdentification createClassIdentification() {
        ClassIdentificationImpl classIdentification = new ClassIdentificationImpl();
        return classIdentification;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public Comments createComments() {
        CommentsImpl comments = new CommentsImpl();
        return comments;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public Preferences createPreferences() {
        PreferencesImpl preferences = new PreferencesImpl();
        return preferences;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public State createState() {
        StateImpl state = new StateImpl();
        return state;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public Property createProperty() {
        PropertyImpl property = new PropertyImpl();
        return property;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public PropType createPropType() {
        PropTypeImpl propType = new PropTypeImpl();
        return propType;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public SimpleType createSimpleType() {
        SimpleTypeImpl simpleType = new SimpleTypeImpl();
        return simpleType;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public VectorType createVectorType() {
        VectorTypeImpl vectorType = new VectorTypeImpl();
        return vectorType;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public InheritanceStatus createInheritanceStatus() {
        InheritanceStatusImpl inheritanceStatus = new InheritanceStatusImpl();
        return inheritanceStatus;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public Command createCommand() {
        CommandImpl command = new CommandImpl();
        return command;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public Argument createArgument() {
        ArgumentImpl argument = new ArgumentImpl();
        return argument;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public Attribute createAttribute() {
        AttributeImpl attribute = new AttributeImpl();
        return attribute;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public FireEvents createFireEvents() {
        FireEventsImpl fireEvents = new FireEventsImpl();
        return fireEvents;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public AttrProperties createAttrProperties() {
        AttrPropertiesImpl attrProperties = new AttrPropertiesImpl();
        return attrProperties;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public AdditionalFile createAdditionalFile() {
        AdditionalFileImpl additionalFile = new AdditionalFileImpl();
        return additionalFile;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public OverlodedPollPeriodObject createOverlodedPollPeriodObject() {
        OverlodedPollPeriodObjectImpl overlodedPollPeriodObject = new OverlodedPollPeriodObjectImpl();
        return overlodedPollPeriodObject;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public Type createType() {
        TypeImpl type = new TypeImpl();
        return type;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public VoidType createVoidType() {
        VoidTypeImpl voidType = new VoidTypeImpl();
        return voidType;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public BooleanType createBooleanType() {
        BooleanTypeImpl booleanType = new BooleanTypeImpl();
        return booleanType;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public ShortType createShortType() {
        ShortTypeImpl shortType = new ShortTypeImpl();
        return shortType;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public UShortType createUShortType() {
        UShortTypeImpl uShortType = new UShortTypeImpl();
        return uShortType;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public IntType createIntType() {
        IntTypeImpl intType = new IntTypeImpl();
        return intType;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public UIntType createUIntType() {
        UIntTypeImpl uIntType = new UIntTypeImpl();
        return uIntType;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public FloatType createFloatType() {
        FloatTypeImpl floatType = new FloatTypeImpl();
        return floatType;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public DoubleType createDoubleType() {
        DoubleTypeImpl doubleType = new DoubleTypeImpl();
        return doubleType;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public StringType createStringType() {
        StringTypeImpl stringType = new StringTypeImpl();
        return stringType;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public CharArrayType createCharArrayType() {
        CharArrayTypeImpl charArrayType = new CharArrayTypeImpl();
        return charArrayType;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public ShortArrayType createShortArrayType() {
        ShortArrayTypeImpl shortArrayType = new ShortArrayTypeImpl();
        return shortArrayType;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public UShortArrayType createUShortArrayType() {
        UShortArrayTypeImpl uShortArrayType = new UShortArrayTypeImpl();
        return uShortArrayType;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public IntArrayType createIntArrayType() {
        IntArrayTypeImpl intArrayType = new IntArrayTypeImpl();
        return intArrayType;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public UIntArrayType createUIntArrayType() {
        UIntArrayTypeImpl uIntArrayType = new UIntArrayTypeImpl();
        return uIntArrayType;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public FloatArrayType createFloatArrayType() {
        FloatArrayTypeImpl floatArrayType = new FloatArrayTypeImpl();
        return floatArrayType;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public DoubleArrayType createDoubleArrayType() {
        DoubleArrayTypeImpl doubleArrayType = new DoubleArrayTypeImpl();
        return doubleArrayType;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public StringArrayType createStringArrayType() {
        StringArrayTypeImpl stringArrayType = new StringArrayTypeImpl();
        return stringArrayType;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public LongStringArrayType createLongStringArrayType() {
        LongStringArrayTypeImpl longStringArrayType = new LongStringArrayTypeImpl();
        return longStringArrayType;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public DoubleStringArrayType createDoubleStringArrayType() {
        DoubleStringArrayTypeImpl doubleStringArrayType = new DoubleStringArrayTypeImpl();
        return doubleStringArrayType;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public StateType createStateType() {
        StateTypeImpl stateType = new StateTypeImpl();
        return stateType;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public ConstStringType createConstStringType() {
        ConstStringTypeImpl constStringType = new ConstStringTypeImpl();
        return constStringType;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public BooleanArrayType createBooleanArrayType() {
        BooleanArrayTypeImpl booleanArrayType = new BooleanArrayTypeImpl();
        return booleanArrayType;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public UCharType createUCharType() {
        UCharTypeImpl uCharType = new UCharTypeImpl();
        return uCharType;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public LongType createLongType() {
        LongTypeImpl longType = new LongTypeImpl();
        return longType;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public ULongType createULongType() {
        ULongTypeImpl uLongType = new ULongTypeImpl();
        return uLongType;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public LongArrayType createLongArrayType() {
        LongArrayTypeImpl longArrayType = new LongArrayTypeImpl();
        return longArrayType;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public ULongArrayType createULongArrayType() {
        ULongArrayTypeImpl uLongArrayType = new ULongArrayTypeImpl();
        return uLongArrayType;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public DevIntType createDevIntType() {
        DevIntTypeImpl devIntType = new DevIntTypeImpl();
        return devIntType;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public EncodedType createEncodedType() {
        EncodedTypeImpl encodedType = new EncodedTypeImpl();
        return encodedType;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public ShortVectorType createShortVectorType() {
        ShortVectorTypeImpl shortVectorType = new ShortVectorTypeImpl();
        return shortVectorType;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public IntVectorType createIntVectorType() {
        IntVectorTypeImpl intVectorType = new IntVectorTypeImpl();
        return intVectorType;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public FloatVectorType createFloatVectorType() {
        FloatVectorTypeImpl floatVectorType = new FloatVectorTypeImpl();
        return floatVectorType;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public DoubleVectorType createDoubleVectorType() {
        DoubleVectorTypeImpl doubleVectorType = new DoubleVectorTypeImpl();
        return doubleVectorType;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public StringVectorType createStringVectorType() {
        StringVectorTypeImpl stringVectorType = new StringVectorTypeImpl();
        return stringVectorType;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public PogoDslPackage getPogoDslPackage() {
        return (PogoDslPackage) getEPackage();
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @deprecated
   * @generated
   */
    @Deprecated
    public static PogoDslPackage getPackage() {
        return PogoDslPackage.eINSTANCE;
    }
}
