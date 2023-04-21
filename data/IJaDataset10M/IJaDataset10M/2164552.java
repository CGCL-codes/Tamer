package org.slaatsoi.coremodel.util;

import java.util.List;
import org.eclipse.emf.ecore.EClass;
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
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see org.slaatsoi.coremodel.CoremodelPackage
 * @generated
 */
public class CoremodelSwitch<T> {

    /**
     * The cached model package
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static CoremodelPackage modelPackage;

    /**
     * Creates an instance of the switch.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CoremodelSwitch() {
        if (modelPackage == null) {
            modelPackage = CoremodelPackage.eINSTANCE;
        }
    }

    /**
     * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the first non-null result returned by a <code>caseXXX</code> call.
     * @generated
     */
    public T doSwitch(EObject theEObject) {
        return doSwitch(theEObject.eClass(), theEObject);
    }

    /**
     * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the first non-null result returned by a <code>caseXXX</code> call.
     * @generated
     */
    protected T doSwitch(EClass theEClass, EObject theEObject) {
        if (theEClass.eContainer() == modelPackage) {
            return doSwitch(theEClass.getClassifierID(), theEObject);
        } else {
            List<EClass> eSuperTypes = theEClass.getESuperTypes();
            return eSuperTypes.isEmpty() ? defaultCase(theEObject) : doSwitch(eSuperTypes.get(0), theEObject);
        }
    }

    /**
     * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the first non-null result returned by a <code>caseXXX</code> call.
     * @generated
     */
    protected T doSwitch(int classifierID, EObject theEObject) {
        switch(classifierID) {
            case CoremodelPackage.AREA:
                {
                    Area area = (Area) theEObject;
                    T result = caseArea(area);
                    if (result == null) result = caseQuantitativeType(area);
                    if (result == null) result = casePropertyType(area);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CoremodelPackage.BINARY_LOGIC_OPERATOR_TYPE:
                {
                    BinaryLogicOperatorType binaryLogicOperatorType = (BinaryLogicOperatorType) theEObject;
                    T result = caseBinaryLogicOperatorType(binaryLogicOperatorType);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CoremodelPackage.CONSTANT:
                {
                    Constant constant = (Constant) theEObject;
                    T result = caseConstant(constant);
                    if (result == null) result = caseProperty(constant);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CoremodelPackage.DATA_RATE:
                {
                    DataRate dataRate = (DataRate) theEObject;
                    T result = caseDataRate(dataRate);
                    if (result == null) result = caseQuantitativeType(dataRate);
                    if (result == null) result = casePropertyType(dataRate);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CoremodelPackage.DATA_SIZE:
                {
                    DataSize dataSize = (DataSize) theEObject;
                    T result = caseDataSize(dataSize);
                    if (result == null) result = caseQuantitativeType(dataSize);
                    if (result == null) result = casePropertyType(dataSize);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CoremodelPackage.DURATION:
                {
                    Duration duration = (Duration) theEObject;
                    T result = caseDuration(duration);
                    if (result == null) result = caseQuantitativeType(duration);
                    if (result == null) result = casePropertyType(duration);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CoremodelPackage.ENERGY:
                {
                    Energy energy = (Energy) theEObject;
                    T result = caseEnergy(energy);
                    if (result == null) result = caseQuantitativeType(energy);
                    if (result == null) result = casePropertyType(energy);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CoremodelPackage.EQUAL:
                {
                    Equal equal = (Equal) theEObject;
                    T result = caseEqual(equal);
                    if (result == null) result = casePredicateType(equal);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CoremodelPackage.EXPRESSION:
                {
                    Expression expression = (Expression) theEObject;
                    T result = caseExpression(expression);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CoremodelPackage.FALSE:
                {
                    False false_ = (False) theEObject;
                    T result = caseFalse(false_);
                    if (result == null) result = casePredicateType(false_);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CoremodelPackage.FREQUENCY:
                {
                    Frequency frequency = (Frequency) theEObject;
                    T result = caseFrequency(frequency);
                    if (result == null) result = caseQuantitativeType(frequency);
                    if (result == null) result = casePropertyType(frequency);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CoremodelPackage.GREATER:
                {
                    Greater greater = (Greater) theEObject;
                    T result = caseGreater(greater);
                    if (result == null) result = casePredicateType(greater);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CoremodelPackage.GREATER_EQUAL:
                {
                    GreaterEqual greaterEqual = (GreaterEqual) theEObject;
                    T result = caseGreaterEqual(greaterEqual);
                    if (result == null) result = casePredicateType(greaterEqual);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CoremodelPackage.LENGTH:
                {
                    Length length = (Length) theEObject;
                    T result = caseLength(length);
                    if (result == null) result = caseQuantitativeType(length);
                    if (result == null) result = casePropertyType(length);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CoremodelPackage.LESS:
                {
                    Less less = (Less) theEObject;
                    T result = caseLess(less);
                    if (result == null) result = casePredicateType(less);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CoremodelPackage.LESS_EQUAL:
                {
                    LessEqual lessEqual = (LessEqual) theEObject;
                    T result = caseLessEqual(lessEqual);
                    if (result == null) result = casePredicateType(lessEqual);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CoremodelPackage.LOGIC_EXPRESSION_TYPE:
                {
                    LogicExpressionType logicExpressionType = (LogicExpressionType) theEObject;
                    T result = caseLogicExpressionType(logicExpressionType);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CoremodelPackage.METRIC:
                {
                    Metric metric = (Metric) theEObject;
                    T result = caseMetric(metric);
                    if (result == null) result = caseProperty(metric);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CoremodelPackage.NOT_EQUAL:
                {
                    NotEqual notEqual = (NotEqual) theEObject;
                    T result = caseNotEqual(notEqual);
                    if (result == null) result = casePredicateType(notEqual);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CoremodelPackage.NUMBER:
                {
                    org.slaatsoi.coremodel.Number number = (org.slaatsoi.coremodel.Number) theEObject;
                    T result = caseNumber(number);
                    if (result == null) result = caseQuantitativeType(number);
                    if (result == null) result = casePropertyType(number);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CoremodelPackage.PARAMETER:
                {
                    Parameter parameter = (Parameter) theEObject;
                    T result = caseParameter(parameter);
                    if (result == null) result = caseProperty(parameter);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CoremodelPackage.PERCENTAGE:
                {
                    Percentage percentage = (Percentage) theEObject;
                    T result = casePercentage(percentage);
                    if (result == null) result = caseQuantitativeType(percentage);
                    if (result == null) result = casePropertyType(percentage);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CoremodelPackage.POWER:
                {
                    Power power = (Power) theEObject;
                    T result = casePower(power);
                    if (result == null) result = caseQuantitativeType(power);
                    if (result == null) result = casePropertyType(power);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CoremodelPackage.PREDICATE_TYPE:
                {
                    PredicateType predicateType = (PredicateType) theEObject;
                    T result = casePredicateType(predicateType);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CoremodelPackage.PRICE:
                {
                    Price price = (Price) theEObject;
                    T result = casePrice(price);
                    if (result == null) result = caseQuantitativeType(price);
                    if (result == null) result = casePropertyType(price);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CoremodelPackage.PROPERTY:
                {
                    Property property = (Property) theEObject;
                    T result = caseProperty(property);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CoremodelPackage.PROPERTY_TYPE:
                {
                    PropertyType propertyType = (PropertyType) theEObject;
                    T result = casePropertyType(propertyType);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CoremodelPackage.QUALITATIVE_TYPE:
                {
                    QualitativeType qualitativeType = (QualitativeType) theEObject;
                    T result = caseQualitativeType(qualitativeType);
                    if (result == null) result = casePropertyType(qualitativeType);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CoremodelPackage.QUANTITATIVE_TYPE:
                {
                    QuantitativeType quantitativeType = (QuantitativeType) theEObject;
                    T result = caseQuantitativeType(quantitativeType);
                    if (result == null) result = casePropertyType(quantitativeType);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CoremodelPackage.DOCUMENT_ROOT:
                {
                    DocumentRoot documentRoot = (DocumentRoot) theEObject;
                    T result = caseDocumentRoot(documentRoot);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CoremodelPackage.TRUE:
                {
                    True true_ = (True) theEObject;
                    T result = caseTrue(true_);
                    if (result == null) result = casePredicateType(true_);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CoremodelPackage.TX_RATE:
                {
                    TxRate txRate = (TxRate) theEObject;
                    T result = caseTxRate(txRate);
                    if (result == null) result = caseQuantitativeType(txRate);
                    if (result == null) result = casePropertyType(txRate);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CoremodelPackage.UNARY_LOGIC_OPERATOR_TYPE:
                {
                    UnaryLogicOperatorType unaryLogicOperatorType = (UnaryLogicOperatorType) theEObject;
                    T result = caseUnaryLogicOperatorType(unaryLogicOperatorType);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CoremodelPackage.WEIGHT:
                {
                    Weight weight = (Weight) theEObject;
                    T result = caseWeight(weight);
                    if (result == null) result = caseQuantitativeType(weight);
                    if (result == null) result = casePropertyType(weight);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            default:
                return defaultCase(theEObject);
        }
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Area</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Area</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseArea(Area object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Binary Logic Operator Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Binary Logic Operator Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseBinaryLogicOperatorType(BinaryLogicOperatorType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Constant</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Constant</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseConstant(Constant object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Data Rate</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Data Rate</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDataRate(DataRate object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Data Size</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Data Size</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDataSize(DataSize object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Duration</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Duration</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDuration(Duration object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Energy</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Energy</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseEnergy(Energy object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Equal</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Equal</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseEqual(Equal object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseExpression(Expression object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>False</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>False</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseFalse(False object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Frequency</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Frequency</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseFrequency(Frequency object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Greater</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Greater</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseGreater(Greater object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Greater Equal</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Greater Equal</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseGreaterEqual(GreaterEqual object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Length</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Length</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseLength(Length object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Less</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Less</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseLess(Less object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Less Equal</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Less Equal</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseLessEqual(LessEqual object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Logic Expression Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Logic Expression Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseLogicExpressionType(LogicExpressionType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Metric</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Metric</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMetric(Metric object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Not Equal</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Not Equal</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseNotEqual(NotEqual object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Number</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Number</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseNumber(org.slaatsoi.coremodel.Number object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Parameter</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Parameter</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseParameter(Parameter object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Percentage</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Percentage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T casePercentage(Percentage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Power</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Power</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T casePower(Power object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Predicate Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Predicate Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T casePredicateType(PredicateType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Price</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Price</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T casePrice(Price object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Property</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Property</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseProperty(Property object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Property Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Property Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T casePropertyType(PropertyType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Qualitative Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Qualitative Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseQualitativeType(QualitativeType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Quantitative Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Quantitative Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseQuantitativeType(QuantitativeType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Document Root</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Document Root</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDocumentRoot(DocumentRoot object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>True</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>True</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTrue(True object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Tx Rate</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Tx Rate</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTxRate(TxRate object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Unary Logic Operator Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Unary Logic Operator Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseUnaryLogicOperatorType(UnaryLogicOperatorType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Weight</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Weight</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseWeight(Weight object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch, but this is the last case anyway.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject)
     * @generated
     */
    public T defaultCase(EObject object) {
        return null;
    }
}
