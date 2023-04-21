package net.sf.rcer.rom.ddic.impl;

import net.sf.rcer.rom.ddic.*;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
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
public class DDICFactoryImpl extends EFactoryImpl implements DDICFactory {

    /**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public static DDICFactory init() {
        try {
            DDICFactory theDDICFactory = (DDICFactory) EPackage.Registry.INSTANCE.getEFactory("http://rcer.sf.net/rom/ddic");
            if (theDDICFactory != null) {
                return theDDICFactory;
            }
        } catch (Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new DDICFactoryImpl();
    }

    /**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public DDICFactoryImpl() {
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
            case DDICPackage.DOMAIN:
                return createDomain();
            case DDICPackage.DOMAIN_VALUE_SINGLE:
                return createDomainValueSingle();
            case DDICPackage.DOMAIN_VALUE_RANGE:
                return createDomainValueRange();
            case DDICPackage.DATA_ELEMENT:
                return createDataElement();
            case DDICPackage.STRUCTURE:
                return createStructure();
            case DDICPackage.TABLE:
                return createTable();
            case DDICPackage.STRUCTURE_INCLUSION:
                return createStructureInclusion();
            case DDICPackage.DATA_ELEMENT_FIELD:
                return createDataElementField();
            case DDICPackage.DIRECT_FIELD:
                return createDirectField();
            case DDICPackage.STRUCTURED_FIELD:
                return createStructuredField();
            case DDICPackage.TABULAR_FIELD:
                return createTabularField();
            case DDICPackage.TABLE_TYPE:
                return createTableType();
            case DDICPackage.VIEW:
                return createView();
            case DDICPackage.SEARCH_HELP:
                return createSearchHelp();
            case DDICPackage.ENQUEUE_OBJECT:
                return createEnqueueObject();
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
            case DDICPackage.DICTIONARY_DATA_TYPE:
                return createDictionaryDataTypeFromString(eDataType, initialValue);
            case DDICPackage.REFERRED_OBJECT_TYPE:
                return createReferredObjectTypeFromString(eDataType, initialValue);
            case DDICPackage.TYPE_KIND:
                return createTypeKindFromString(eDataType, initialValue);
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
            case DDICPackage.DICTIONARY_DATA_TYPE:
                return convertDictionaryDataTypeToString(eDataType, instanceValue);
            case DDICPackage.REFERRED_OBJECT_TYPE:
                return convertReferredObjectTypeToString(eDataType, instanceValue);
            case DDICPackage.TYPE_KIND:
                return convertTypeKindToString(eDataType, instanceValue);
            default:
                throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
        }
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Domain createDomain() {
        DomainImpl domain = new DomainImpl();
        return domain;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public DomainValueSingle createDomainValueSingle() {
        DomainValueSingleImpl domainValueSingle = new DomainValueSingleImpl();
        return domainValueSingle;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public DomainValueRange createDomainValueRange() {
        DomainValueRangeImpl domainValueRange = new DomainValueRangeImpl();
        return domainValueRange;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public DataElement createDataElement() {
        DataElementImpl dataElement = new DataElementImpl();
        return dataElement;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Structure createStructure() {
        StructureImpl structure = new StructureImpl();
        return structure;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Table createTable() {
        TableImpl table = new TableImpl();
        return table;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public StructureInclusion createStructureInclusion() {
        StructureInclusionImpl structureInclusion = new StructureInclusionImpl();
        return structureInclusion;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public DataElementField createDataElementField() {
        DataElementFieldImpl dataElementField = new DataElementFieldImpl();
        return dataElementField;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public DirectField createDirectField() {
        DirectFieldImpl directField = new DirectFieldImpl();
        return directField;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public StructuredField createStructuredField() {
        StructuredFieldImpl structuredField = new StructuredFieldImpl();
        return structuredField;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public TabularField createTabularField() {
        TabularFieldImpl tabularField = new TabularFieldImpl();
        return tabularField;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public TableType createTableType() {
        TableTypeImpl tableType = new TableTypeImpl();
        return tableType;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public View createView() {
        ViewImpl view = new ViewImpl();
        return view;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public SearchHelp createSearchHelp() {
        SearchHelpImpl searchHelp = new SearchHelpImpl();
        return searchHelp;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EnqueueObject createEnqueueObject() {
        EnqueueObjectImpl enqueueObject = new EnqueueObjectImpl();
        return enqueueObject;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public DictionaryDataType createDictionaryDataTypeFromString(EDataType eDataType, String initialValue) {
        DictionaryDataType result = DictionaryDataType.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String convertDictionaryDataTypeToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ReferredObjectType createReferredObjectTypeFromString(EDataType eDataType, String initialValue) {
        ReferredObjectType result = ReferredObjectType.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String convertReferredObjectTypeToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public TypeKind createTypeKindFromString(EDataType eDataType, String initialValue) {
        TypeKind result = TypeKind.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String convertTypeKindToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public DDICPackage getDDICPackage() {
        return (DDICPackage) getEPackage();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
    @Deprecated
    public static DDICPackage getPackage() {
        return DDICPackage.eINSTANCE;
    }
}
