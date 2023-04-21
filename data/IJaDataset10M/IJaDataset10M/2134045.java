package org.slaatsoi.coremodel;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Duration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.slaatsoi.coremodel.Duration#getPrecision <em>Precision</em>}</li>
 *   <li>{@link org.slaatsoi.coremodel.Duration#getUnit <em>Unit</em>}</li>
 *   <li>{@link org.slaatsoi.coremodel.Duration#getValue <em>Value</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.slaatsoi.coremodel.CoremodelPackage#getDuration()
 * @model extendedMetaData="name='Duration' kind='empty'"
 * @generated
 */
public interface Duration extends QuantitativeType {

    /**
     * Returns the value of the '<em><b>Precision</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Precision</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Precision</em>' attribute.
     * @see #isSetPrecision()
     * @see #unsetPrecision()
     * @see #setPrecision(double)
     * @see org.slaatsoi.coremodel.CoremodelPackage#getDuration_Precision()
     * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Double"
     *        extendedMetaData="kind='attribute' name='precision'"
     * @generated
     */
    double getPrecision();

    /**
     * Sets the value of the '{@link org.slaatsoi.coremodel.Duration#getPrecision <em>Precision</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Precision</em>' attribute.
     * @see #isSetPrecision()
     * @see #unsetPrecision()
     * @see #getPrecision()
     * @generated
     */
    void setPrecision(double value);

    /**
     * Unsets the value of the '{@link org.slaatsoi.coremodel.Duration#getPrecision <em>Precision</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetPrecision()
     * @see #getPrecision()
     * @see #setPrecision(double)
     * @generated
     */
    void unsetPrecision();

    /**
     * Returns whether the value of the '{@link org.slaatsoi.coremodel.Duration#getPrecision <em>Precision</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Precision</em>' attribute is set.
     * @see #unsetPrecision()
     * @see #getPrecision()
     * @see #setPrecision(double)
     * @generated
     */
    boolean isSetPrecision();

    /**
     * Returns the value of the '<em><b>Unit</b></em>' attribute.
     * The literals are from the enumeration {@link org.slaatsoi.coremodel.TimeUnitKind}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Unit</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Unit</em>' attribute.
     * @see org.slaatsoi.coremodel.TimeUnitKind
     * @see #isSetUnit()
     * @see #unsetUnit()
     * @see #setUnit(TimeUnitKind)
     * @see org.slaatsoi.coremodel.CoremodelPackage#getDuration_Unit()
     * @model unsettable="true"
     *        extendedMetaData="kind='attribute' name='unit'"
     * @generated
     */
    TimeUnitKind getUnit();

    /**
     * Sets the value of the '{@link org.slaatsoi.coremodel.Duration#getUnit <em>Unit</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Unit</em>' attribute.
     * @see org.slaatsoi.coremodel.TimeUnitKind
     * @see #isSetUnit()
     * @see #unsetUnit()
     * @see #getUnit()
     * @generated
     */
    void setUnit(TimeUnitKind value);

    /**
     * Unsets the value of the '{@link org.slaatsoi.coremodel.Duration#getUnit <em>Unit</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetUnit()
     * @see #getUnit()
     * @see #setUnit(TimeUnitKind)
     * @generated
     */
    void unsetUnit();

    /**
     * Returns whether the value of the '{@link org.slaatsoi.coremodel.Duration#getUnit <em>Unit</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Unit</em>' attribute is set.
     * @see #unsetUnit()
     * @see #getUnit()
     * @see #setUnit(TimeUnitKind)
     * @generated
     */
    boolean isSetUnit();

    /**
     * Returns the value of the '<em><b>Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Value</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Value</em>' attribute.
     * @see #isSetValue()
     * @see #unsetValue()
     * @see #setValue(double)
     * @see org.slaatsoi.coremodel.CoremodelPackage#getDuration_Value()
     * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Double"
     *        extendedMetaData="kind='attribute' name='value'"
     * @generated
     */
    double getValue();

    /**
     * Sets the value of the '{@link org.slaatsoi.coremodel.Duration#getValue <em>Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Value</em>' attribute.
     * @see #isSetValue()
     * @see #unsetValue()
     * @see #getValue()
     * @generated
     */
    void setValue(double value);

    /**
     * Unsets the value of the '{@link org.slaatsoi.coremodel.Duration#getValue <em>Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetValue()
     * @see #getValue()
     * @see #setValue(double)
     * @generated
     */
    void unsetValue();

    /**
     * Returns whether the value of the '{@link org.slaatsoi.coremodel.Duration#getValue <em>Value</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Value</em>' attribute is set.
     * @see #unsetValue()
     * @see #getValue()
     * @see #setValue(double)
     * @generated
     */
    boolean isSetValue();
}
