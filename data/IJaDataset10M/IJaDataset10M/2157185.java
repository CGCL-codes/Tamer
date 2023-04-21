package org.slaatsoi.coremodel.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;
import org.slaatsoi.coremodel.Area;
import org.slaatsoi.coremodel.BinaryLogicOperatorType;
import org.slaatsoi.coremodel.Constant;
import org.slaatsoi.coremodel.CoremodelPackage;
import org.slaatsoi.coremodel.DataRate;
import org.slaatsoi.coremodel.DataSize;
import org.slaatsoi.coremodel.DocumentRoot;
import org.slaatsoi.coremodel.Duration;
import org.slaatsoi.coremodel.Energy;
import org.slaatsoi.coremodel.Equal;
import org.slaatsoi.coremodel.Expression;
import org.slaatsoi.coremodel.False;
import org.slaatsoi.coremodel.Frequency;
import org.slaatsoi.coremodel.Greater;
import org.slaatsoi.coremodel.GreaterEqual;
import org.slaatsoi.coremodel.Length;
import org.slaatsoi.coremodel.Less;
import org.slaatsoi.coremodel.LessEqual;
import org.slaatsoi.coremodel.LogicExpressionType;
import org.slaatsoi.coremodel.Metric;
import org.slaatsoi.coremodel.NotEqual;
import org.slaatsoi.coremodel.Parameter;
import org.slaatsoi.coremodel.Percentage;
import org.slaatsoi.coremodel.Power;
import org.slaatsoi.coremodel.PredicateType;
import org.slaatsoi.coremodel.Price;
import org.slaatsoi.coremodel.Property;
import org.slaatsoi.coremodel.PropertyType;
import org.slaatsoi.coremodel.QualitativeType;
import org.slaatsoi.coremodel.QuantitativeType;
import org.slaatsoi.coremodel.True;
import org.slaatsoi.coremodel.TxRate;
import org.slaatsoi.coremodel.UnaryLogicOperatorType;
import org.slaatsoi.coremodel.Weight;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.slaatsoi.coremodel.CoremodelPackage
 * @generated
 */
public class CoremodelAdapterFactory extends AdapterFactoryImpl {

    /**
     * The cached model package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static CoremodelPackage modelPackage;

    /**
     * Creates an instance of the adapter factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CoremodelAdapterFactory() {
        if (modelPackage == null) {
            modelPackage = CoremodelPackage.eINSTANCE;
        }
    }

    /**
     * Returns whether this factory is applicable for the type of the object.
     * <!-- begin-user-doc -->
     * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
     * <!-- end-user-doc -->
     * @return whether this factory is applicable for the type of the object.
     * @generated
     */
    @Override
    public boolean isFactoryForType(Object object) {
        if (object == modelPackage) {
            return true;
        }
        if (object instanceof EObject) {
            return ((EObject) object).eClass().getEPackage() == modelPackage;
        }
        return false;
    }

    /**
     * The switch that delegates to the <code>createXXX</code> methods.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected CoremodelSwitch<Adapter> modelSwitch = new CoremodelSwitch<Adapter>() {

        @Override
        public Adapter caseArea(Area object) {
            return createAreaAdapter();
        }

        @Override
        public Adapter caseBinaryLogicOperatorType(BinaryLogicOperatorType object) {
            return createBinaryLogicOperatorTypeAdapter();
        }

        @Override
        public Adapter caseConstant(Constant object) {
            return createConstantAdapter();
        }

        @Override
        public Adapter caseDataRate(DataRate object) {
            return createDataRateAdapter();
        }

        @Override
        public Adapter caseDataSize(DataSize object) {
            return createDataSizeAdapter();
        }

        @Override
        public Adapter caseDuration(Duration object) {
            return createDurationAdapter();
        }

        @Override
        public Adapter caseEnergy(Energy object) {
            return createEnergyAdapter();
        }

        @Override
        public Adapter caseEqual(Equal object) {
            return createEqualAdapter();
        }

        @Override
        public Adapter caseExpression(Expression object) {
            return createExpressionAdapter();
        }

        @Override
        public Adapter caseFalse(False object) {
            return createFalseAdapter();
        }

        @Override
        public Adapter caseFrequency(Frequency object) {
            return createFrequencyAdapter();
        }

        @Override
        public Adapter caseGreater(Greater object) {
            return createGreaterAdapter();
        }

        @Override
        public Adapter caseGreaterEqual(GreaterEqual object) {
            return createGreaterEqualAdapter();
        }

        @Override
        public Adapter caseLength(Length object) {
            return createLengthAdapter();
        }

        @Override
        public Adapter caseLess(Less object) {
            return createLessAdapter();
        }

        @Override
        public Adapter caseLessEqual(LessEqual object) {
            return createLessEqualAdapter();
        }

        @Override
        public Adapter caseLogicExpressionType(LogicExpressionType object) {
            return createLogicExpressionTypeAdapter();
        }

        @Override
        public Adapter caseMetric(Metric object) {
            return createMetricAdapter();
        }

        @Override
        public Adapter caseNotEqual(NotEqual object) {
            return createNotEqualAdapter();
        }

        @Override
        public Adapter caseNumber(org.slaatsoi.coremodel.Number object) {
            return createNumberAdapter();
        }

        @Override
        public Adapter caseParameter(Parameter object) {
            return createParameterAdapter();
        }

        @Override
        public Adapter casePercentage(Percentage object) {
            return createPercentageAdapter();
        }

        @Override
        public Adapter casePower(Power object) {
            return createPowerAdapter();
        }

        @Override
        public Adapter casePredicateType(PredicateType object) {
            return createPredicateTypeAdapter();
        }

        @Override
        public Adapter casePrice(Price object) {
            return createPriceAdapter();
        }

        @Override
        public Adapter caseProperty(Property object) {
            return createPropertyAdapter();
        }

        @Override
        public Adapter casePropertyType(PropertyType object) {
            return createPropertyTypeAdapter();
        }

        @Override
        public Adapter caseQualitativeType(QualitativeType object) {
            return createQualitativeTypeAdapter();
        }

        @Override
        public Adapter caseQuantitativeType(QuantitativeType object) {
            return createQuantitativeTypeAdapter();
        }

        @Override
        public Adapter caseDocumentRoot(DocumentRoot object) {
            return createDocumentRootAdapter();
        }

        @Override
        public Adapter caseTrue(True object) {
            return createTrueAdapter();
        }

        @Override
        public Adapter caseTxRate(TxRate object) {
            return createTxRateAdapter();
        }

        @Override
        public Adapter caseUnaryLogicOperatorType(UnaryLogicOperatorType object) {
            return createUnaryLogicOperatorTypeAdapter();
        }

        @Override
        public Adapter caseWeight(Weight object) {
            return createWeightAdapter();
        }

        @Override
        public Adapter defaultCase(EObject object) {
            return createEObjectAdapter();
        }
    };

    /**
     * Creates an adapter for the <code>target</code>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param target the object to adapt.
     * @return the adapter for the <code>target</code>.
     * @generated
     */
    @Override
    public Adapter createAdapter(Notifier target) {
        return modelSwitch.doSwitch((EObject) target);
    }

    /**
     * Creates a new adapter for an object of class '{@link org.slaatsoi.coremodel.Area <em>Area</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.slaatsoi.coremodel.Area
     * @generated
     */
    public Adapter createAreaAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.slaatsoi.coremodel.BinaryLogicOperatorType <em>Binary Logic Operator Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.slaatsoi.coremodel.BinaryLogicOperatorType
     * @generated
     */
    public Adapter createBinaryLogicOperatorTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.slaatsoi.coremodel.Constant <em>Constant</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.slaatsoi.coremodel.Constant
     * @generated
     */
    public Adapter createConstantAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.slaatsoi.coremodel.DataRate <em>Data Rate</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.slaatsoi.coremodel.DataRate
     * @generated
     */
    public Adapter createDataRateAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.slaatsoi.coremodel.DataSize <em>Data Size</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.slaatsoi.coremodel.DataSize
     * @generated
     */
    public Adapter createDataSizeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.slaatsoi.coremodel.Duration <em>Duration</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.slaatsoi.coremodel.Duration
     * @generated
     */
    public Adapter createDurationAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.slaatsoi.coremodel.Energy <em>Energy</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.slaatsoi.coremodel.Energy
     * @generated
     */
    public Adapter createEnergyAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.slaatsoi.coremodel.Equal <em>Equal</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.slaatsoi.coremodel.Equal
     * @generated
     */
    public Adapter createEqualAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.slaatsoi.coremodel.Expression <em>Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.slaatsoi.coremodel.Expression
     * @generated
     */
    public Adapter createExpressionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.slaatsoi.coremodel.False <em>False</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.slaatsoi.coremodel.False
     * @generated
     */
    public Adapter createFalseAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.slaatsoi.coremodel.Frequency <em>Frequency</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.slaatsoi.coremodel.Frequency
     * @generated
     */
    public Adapter createFrequencyAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.slaatsoi.coremodel.Greater <em>Greater</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.slaatsoi.coremodel.Greater
     * @generated
     */
    public Adapter createGreaterAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.slaatsoi.coremodel.GreaterEqual <em>Greater Equal</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.slaatsoi.coremodel.GreaterEqual
     * @generated
     */
    public Adapter createGreaterEqualAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.slaatsoi.coremodel.Length <em>Length</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.slaatsoi.coremodel.Length
     * @generated
     */
    public Adapter createLengthAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.slaatsoi.coremodel.Less <em>Less</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.slaatsoi.coremodel.Less
     * @generated
     */
    public Adapter createLessAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.slaatsoi.coremodel.LessEqual <em>Less Equal</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.slaatsoi.coremodel.LessEqual
     * @generated
     */
    public Adapter createLessEqualAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.slaatsoi.coremodel.LogicExpressionType <em>Logic Expression Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.slaatsoi.coremodel.LogicExpressionType
     * @generated
     */
    public Adapter createLogicExpressionTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.slaatsoi.coremodel.Metric <em>Metric</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.slaatsoi.coremodel.Metric
     * @generated
     */
    public Adapter createMetricAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.slaatsoi.coremodel.NotEqual <em>Not Equal</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.slaatsoi.coremodel.NotEqual
     * @generated
     */
    public Adapter createNotEqualAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.slaatsoi.coremodel.Number <em>Number</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.slaatsoi.coremodel.Number
     * @generated
     */
    public Adapter createNumberAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.slaatsoi.coremodel.Parameter <em>Parameter</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.slaatsoi.coremodel.Parameter
     * @generated
     */
    public Adapter createParameterAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.slaatsoi.coremodel.Percentage <em>Percentage</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.slaatsoi.coremodel.Percentage
     * @generated
     */
    public Adapter createPercentageAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.slaatsoi.coremodel.Power <em>Power</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.slaatsoi.coremodel.Power
     * @generated
     */
    public Adapter createPowerAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.slaatsoi.coremodel.PredicateType <em>Predicate Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.slaatsoi.coremodel.PredicateType
     * @generated
     */
    public Adapter createPredicateTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.slaatsoi.coremodel.Price <em>Price</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.slaatsoi.coremodel.Price
     * @generated
     */
    public Adapter createPriceAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.slaatsoi.coremodel.Property <em>Property</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.slaatsoi.coremodel.Property
     * @generated
     */
    public Adapter createPropertyAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.slaatsoi.coremodel.PropertyType <em>Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.slaatsoi.coremodel.PropertyType
     * @generated
     */
    public Adapter createPropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.slaatsoi.coremodel.QualitativeType <em>Qualitative Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.slaatsoi.coremodel.QualitativeType
     * @generated
     */
    public Adapter createQualitativeTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.slaatsoi.coremodel.QuantitativeType <em>Quantitative Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.slaatsoi.coremodel.QuantitativeType
     * @generated
     */
    public Adapter createQuantitativeTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.slaatsoi.coremodel.DocumentRoot <em>Document Root</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.slaatsoi.coremodel.DocumentRoot
     * @generated
     */
    public Adapter createDocumentRootAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.slaatsoi.coremodel.True <em>True</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.slaatsoi.coremodel.True
     * @generated
     */
    public Adapter createTrueAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.slaatsoi.coremodel.TxRate <em>Tx Rate</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.slaatsoi.coremodel.TxRate
     * @generated
     */
    public Adapter createTxRateAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.slaatsoi.coremodel.UnaryLogicOperatorType <em>Unary Logic Operator Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.slaatsoi.coremodel.UnaryLogicOperatorType
     * @generated
     */
    public Adapter createUnaryLogicOperatorTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.slaatsoi.coremodel.Weight <em>Weight</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.slaatsoi.coremodel.Weight
     * @generated
     */
    public Adapter createWeightAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for the default case.
     * <!-- begin-user-doc -->
     * This default implementation returns null.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @generated
     */
    public Adapter createEObjectAdapter() {
        return null;
    }
}
