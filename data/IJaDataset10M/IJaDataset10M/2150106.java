package net.sourceforge.olympos.dsl.domain.domain.impl;

import net.sourceforge.olympos.dsl.domain.domain.AbstractAttribute;
import net.sourceforge.olympos.dsl.domain.domain.AbstractAttributeReference;
import net.sourceforge.olympos.dsl.domain.domain.AggregationKind;
import net.sourceforge.olympos.dsl.domain.domain.Association;
import net.sourceforge.olympos.dsl.domain.domain.AssociationEndConfiguration;
import net.sourceforge.olympos.dsl.domain.domain.AssociationOrderConfiguration;
import net.sourceforge.olympos.dsl.domain.domain.Attribute;
import net.sourceforge.olympos.dsl.domain.domain.AttributeConfiguration;
import net.sourceforge.olympos.dsl.domain.domain.AttributeReference;
import net.sourceforge.olympos.dsl.domain.domain.BidirectionalAssociation;
import net.sourceforge.olympos.dsl.domain.domain.BidirectionalAssociationConfiguration;
import net.sourceforge.olympos.dsl.domain.domain.ClassConfiguration;
import net.sourceforge.olympos.dsl.domain.domain.ColumNameConfiguration;
import net.sourceforge.olympos.dsl.domain.domain.DataType;
import net.sourceforge.olympos.dsl.domain.domain.DataTypeConfiguration;
import net.sourceforge.olympos.dsl.domain.domain.DatabaseTypeConfiguration;
import net.sourceforge.olympos.dsl.domain.domain.DirectedAssociation;
import net.sourceforge.olympos.dsl.domain.domain.DomainFactory;
import net.sourceforge.olympos.dsl.domain.domain.DomainModel;
import net.sourceforge.olympos.dsl.domain.domain.DomainPackage;
import net.sourceforge.olympos.dsl.domain.domain.EditableConfiguration;
import net.sourceforge.olympos.dsl.domain.domain.Elements;
import net.sourceforge.olympos.dsl.domain.domain.Feature;
import net.sourceforge.olympos.dsl.domain.domain.ForeignKeyConfiguration;
import net.sourceforge.olympos.dsl.domain.domain.LabelConfiguration;
import net.sourceforge.olympos.dsl.domain.domain.LabelLiteral;
import net.sourceforge.olympos.dsl.domain.domain.LabelValue;
import net.sourceforge.olympos.dsl.domain.domain.Multiplicity;
import net.sourceforge.olympos.dsl.domain.domain.Operation;
import net.sourceforge.olympos.dsl.domain.domain.OrderByConfiguration;
import net.sourceforge.olympos.dsl.domain.domain.OrderByLiteralKind;
import net.sourceforge.olympos.dsl.domain.domain.OrderByLiterals;
import net.sourceforge.olympos.dsl.domain.domain.OrderByValue;
import net.sourceforge.olympos.dsl.domain.domain.PrimaryKeyConfiguration;
import net.sourceforge.olympos.dsl.domain.domain.SearchableConfiguration;
import net.sourceforge.olympos.dsl.domain.domain.StringLiteralXX;
import net.sourceforge.olympos.dsl.domain.domain.TableConfiguration;
import net.sourceforge.olympos.dsl.domain.domain.TagsConfiguration;
import net.sourceforge.olympos.dsl.domain.domain.Type;
import net.sourceforge.olympos.dsl.domain.domain.ValidationConfiguration;
import net.sourceforge.olympos.dsl.domain.domain.VersionableConfiguration;
import net.sourceforge.olympos.dsl.domain.domain.VisibilityConfiguration;
import net.sourceforge.olympos.dsl.domain.domain.VisibilityKind;
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
public class DomainFactoryImpl extends EFactoryImpl implements DomainFactory {

    /**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public static DomainFactory init() {
        try {
            DomainFactory theDomainFactory = (DomainFactory) EPackage.Registry.INSTANCE.getEFactory("http://www.sourceforge.net/olympos/dsl/domain/Domain");
            if (theDomainFactory != null) {
                return theDomainFactory;
            }
        } catch (Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new DomainFactoryImpl();
    }

    /**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public DomainFactoryImpl() {
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
            case DomainPackage.DOMAIN_MODEL:
                return createDomainModel();
            case DomainPackage.ELEMENTS:
                return createElements();
            case DomainPackage.TYPE:
                return createType();
            case DomainPackage.DATA_TYPE:
                return createDataType();
            case DomainPackage.DATA_TYPE_CONFIGURATION:
                return createDataTypeConfiguration();
            case DomainPackage.DATABASE_TYPE_CONFIGURATION:
                return createDatabaseTypeConfiguration();
            case DomainPackage.VALIDATION_CONFIGURATION:
                return createValidationConfiguration();
            case DomainPackage.CLASS:
                return createClass();
            case DomainPackage.FEATURE:
                return createFeature();
            case DomainPackage.ABSTRACT_ATTRIBUTE:
                return createAbstractAttribute();
            case DomainPackage.ATTRIBUTE:
                return createAttribute();
            case DomainPackage.ATTRIBUTE_REFERENCE:
                return createAttributeReference();
            case DomainPackage.OPERATION:
                return createOperation();
            case DomainPackage.CLASS_CONFIGURATION:
                return createClassConfiguration();
            case DomainPackage.TABLE_CONFIGURATION:
                return createTableConfiguration();
            case DomainPackage.PRIMARY_KEY_CONFIGURATION:
                return createPrimaryKeyConfiguration();
            case DomainPackage.ORDER_BY_CONFIGURATION:
                return createOrderByConfiguration();
            case DomainPackage.ORDER_BY_VALUE:
                return createOrderByValue();
            case DomainPackage.ORDER_BY_LITERALS:
                return createOrderByLiterals();
            case DomainPackage.ABSTRACT_ATTRIBUTE_REFERENCE:
                return createAbstractAttributeReference();
            case DomainPackage.ASSOCIATION_ORDER_CONFIGURATION:
                return createAssociationOrderConfiguration();
            case DomainPackage.LABEL_CONFIGURATION:
                return createLabelConfiguration();
            case DomainPackage.LABEL_VALUE:
                return createLabelValue();
            case DomainPackage.LABEL_LITERAL:
                return createLabelLiteral();
            case DomainPackage.STRING_LITERAL_XX:
                return createStringLiteralXX();
            case DomainPackage.VISIBILITY_CONFIGURATION:
                return createVisibilityConfiguration();
            case DomainPackage.SEARCHABLE_CONFIGURATION:
                return createSearchableConfiguration();
            case DomainPackage.ATTRIBUTE_CONFIGURATION:
                return createAttributeConfiguration();
            case DomainPackage.COLUM_NAME_CONFIGURATION:
                return createColumNameConfiguration();
            case DomainPackage.TAGS_CONFIGURATION:
                return createTagsConfiguration();
            case DomainPackage.EDITABLE_CONFIGURATION:
                return createEditableConfiguration();
            case DomainPackage.VERSIONABLE_CONFIGURATION:
                return createVersionableConfiguration();
            case DomainPackage.ASSOCIATION:
                return createAssociation();
            case DomainPackage.BIDIRECTIONAL_ASSOCIATION:
                return createBidirectionalAssociation();
            case DomainPackage.DIRECTED_ASSOCIATION:
                return createDirectedAssociation();
            case DomainPackage.MULTIPLICITY:
                return createMultiplicity();
            case DomainPackage.BIDIRECTIONAL_ASSOCIATION_CONFIGURATION:
                return createBidirectionalAssociationConfiguration();
            case DomainPackage.ASSOCIATION_END_CONFIGURATION:
                return createAssociationEndConfiguration();
            case DomainPackage.FOREIGN_KEY_CONFIGURATION:
                return createForeignKeyConfiguration();
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
            case DomainPackage.ORDER_BY_LITERAL_KIND:
                return createOrderByLiteralKindFromString(eDataType, initialValue);
            case DomainPackage.VISIBILITY_KIND:
                return createVisibilityKindFromString(eDataType, initialValue);
            case DomainPackage.AGGREGATION_KIND:
                return createAggregationKindFromString(eDataType, initialValue);
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
            case DomainPackage.ORDER_BY_LITERAL_KIND:
                return convertOrderByLiteralKindToString(eDataType, instanceValue);
            case DomainPackage.VISIBILITY_KIND:
                return convertVisibilityKindToString(eDataType, instanceValue);
            case DomainPackage.AGGREGATION_KIND:
                return convertAggregationKindToString(eDataType, instanceValue);
            default:
                throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
        }
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public DomainModel createDomainModel() {
        DomainModelImpl domainModel = new DomainModelImpl();
        return domainModel;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Elements createElements() {
        ElementsImpl elements = new ElementsImpl();
        return elements;
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
    public DataType createDataType() {
        DataTypeImpl dataType = new DataTypeImpl();
        return dataType;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public DataTypeConfiguration createDataTypeConfiguration() {
        DataTypeConfigurationImpl dataTypeConfiguration = new DataTypeConfigurationImpl();
        return dataTypeConfiguration;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public DatabaseTypeConfiguration createDatabaseTypeConfiguration() {
        DatabaseTypeConfigurationImpl databaseTypeConfiguration = new DatabaseTypeConfigurationImpl();
        return databaseTypeConfiguration;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ValidationConfiguration createValidationConfiguration() {
        ValidationConfigurationImpl validationConfiguration = new ValidationConfigurationImpl();
        return validationConfiguration;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public net.sourceforge.olympos.dsl.domain.domain.Class createClass() {
        ClassImpl class_ = new ClassImpl();
        return class_;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Feature createFeature() {
        FeatureImpl feature = new FeatureImpl();
        return feature;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public AbstractAttribute createAbstractAttribute() {
        AbstractAttributeImpl abstractAttribute = new AbstractAttributeImpl();
        return abstractAttribute;
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
    public AttributeReference createAttributeReference() {
        AttributeReferenceImpl attributeReference = new AttributeReferenceImpl();
        return attributeReference;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Operation createOperation() {
        OperationImpl operation = new OperationImpl();
        return operation;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ClassConfiguration createClassConfiguration() {
        ClassConfigurationImpl classConfiguration = new ClassConfigurationImpl();
        return classConfiguration;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public TableConfiguration createTableConfiguration() {
        TableConfigurationImpl tableConfiguration = new TableConfigurationImpl();
        return tableConfiguration;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public PrimaryKeyConfiguration createPrimaryKeyConfiguration() {
        PrimaryKeyConfigurationImpl primaryKeyConfiguration = new PrimaryKeyConfigurationImpl();
        return primaryKeyConfiguration;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public OrderByConfiguration createOrderByConfiguration() {
        OrderByConfigurationImpl orderByConfiguration = new OrderByConfigurationImpl();
        return orderByConfiguration;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public OrderByValue createOrderByValue() {
        OrderByValueImpl orderByValue = new OrderByValueImpl();
        return orderByValue;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public OrderByLiterals createOrderByLiterals() {
        OrderByLiteralsImpl orderByLiterals = new OrderByLiteralsImpl();
        return orderByLiterals;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public AbstractAttributeReference createAbstractAttributeReference() {
        AbstractAttributeReferenceImpl abstractAttributeReference = new AbstractAttributeReferenceImpl();
        return abstractAttributeReference;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public AssociationOrderConfiguration createAssociationOrderConfiguration() {
        AssociationOrderConfigurationImpl associationOrderConfiguration = new AssociationOrderConfigurationImpl();
        return associationOrderConfiguration;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public LabelConfiguration createLabelConfiguration() {
        LabelConfigurationImpl labelConfiguration = new LabelConfigurationImpl();
        return labelConfiguration;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public LabelValue createLabelValue() {
        LabelValueImpl labelValue = new LabelValueImpl();
        return labelValue;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public LabelLiteral createLabelLiteral() {
        LabelLiteralImpl labelLiteral = new LabelLiteralImpl();
        return labelLiteral;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public StringLiteralXX createStringLiteralXX() {
        StringLiteralXXImpl stringLiteralXX = new StringLiteralXXImpl();
        return stringLiteralXX;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public VisibilityConfiguration createVisibilityConfiguration() {
        VisibilityConfigurationImpl visibilityConfiguration = new VisibilityConfigurationImpl();
        return visibilityConfiguration;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public SearchableConfiguration createSearchableConfiguration() {
        SearchableConfigurationImpl searchableConfiguration = new SearchableConfigurationImpl();
        return searchableConfiguration;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public AttributeConfiguration createAttributeConfiguration() {
        AttributeConfigurationImpl attributeConfiguration = new AttributeConfigurationImpl();
        return attributeConfiguration;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ColumNameConfiguration createColumNameConfiguration() {
        ColumNameConfigurationImpl columNameConfiguration = new ColumNameConfigurationImpl();
        return columNameConfiguration;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public TagsConfiguration createTagsConfiguration() {
        TagsConfigurationImpl tagsConfiguration = new TagsConfigurationImpl();
        return tagsConfiguration;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EditableConfiguration createEditableConfiguration() {
        EditableConfigurationImpl editableConfiguration = new EditableConfigurationImpl();
        return editableConfiguration;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public VersionableConfiguration createVersionableConfiguration() {
        VersionableConfigurationImpl versionableConfiguration = new VersionableConfigurationImpl();
        return versionableConfiguration;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Association createAssociation() {
        AssociationImpl association = new AssociationImpl();
        return association;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public BidirectionalAssociation createBidirectionalAssociation() {
        BidirectionalAssociationImpl bidirectionalAssociation = new BidirectionalAssociationImpl();
        return bidirectionalAssociation;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public DirectedAssociation createDirectedAssociation() {
        DirectedAssociationImpl directedAssociation = new DirectedAssociationImpl();
        return directedAssociation;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Multiplicity createMultiplicity() {
        MultiplicityImpl multiplicity = new MultiplicityImpl();
        return multiplicity;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public BidirectionalAssociationConfiguration createBidirectionalAssociationConfiguration() {
        BidirectionalAssociationConfigurationImpl bidirectionalAssociationConfiguration = new BidirectionalAssociationConfigurationImpl();
        return bidirectionalAssociationConfiguration;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public AssociationEndConfiguration createAssociationEndConfiguration() {
        AssociationEndConfigurationImpl associationEndConfiguration = new AssociationEndConfigurationImpl();
        return associationEndConfiguration;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ForeignKeyConfiguration createForeignKeyConfiguration() {
        ForeignKeyConfigurationImpl foreignKeyConfiguration = new ForeignKeyConfigurationImpl();
        return foreignKeyConfiguration;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public OrderByLiteralKind createOrderByLiteralKindFromString(EDataType eDataType, String initialValue) {
        OrderByLiteralKind result = OrderByLiteralKind.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String convertOrderByLiteralKindToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public VisibilityKind createVisibilityKindFromString(EDataType eDataType, String initialValue) {
        VisibilityKind result = VisibilityKind.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String convertVisibilityKindToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public AggregationKind createAggregationKindFromString(EDataType eDataType, String initialValue) {
        AggregationKind result = AggregationKind.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String convertAggregationKindToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public DomainPackage getDomainPackage() {
        return (DomainPackage) getEPackage();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
    @Deprecated
    public static DomainPackage getPackage() {
        return DomainPackage.eINSTANCE;
    }
}
