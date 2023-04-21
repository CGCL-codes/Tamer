package iec61970.generation.production;

import iec61970.core.Curve;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Penstock Loss Curve</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Relationship between penstock head loss (in meters) and  total discharge through the penstock (in cubic meters per second). One or more turbines may be connected to the same penstock.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link iec61970.generation.production.PenstockLossCurve#getHydroGeneratingUnit <em>Hydro Generating Unit</em>}</li>
 * </ul>
 * </p>
 *
 * @see iec61970.generation.production.ProductionPackage#getPenstockLossCurve()
 * @model
 * @generated
 */
public interface PenstockLossCurve extends Curve {

    /**
	 * Returns the value of the '<em><b>Hydro Generating Unit</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link iec61970.generation.production.HydroGeneratingUnit#getPenstockLossCurve <em>Penstock Loss Curve</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * A hydro generating unit has a penstock loss curve
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Hydro Generating Unit</em>' reference.
	 * @see #setHydroGeneratingUnit(HydroGeneratingUnit)
	 * @see iec61970.generation.production.ProductionPackage#getPenstockLossCurve_HydroGeneratingUnit()
	 * @see iec61970.generation.production.HydroGeneratingUnit#getPenstockLossCurve
	 * @model opposite="PenstockLossCurve" required="true"
	 * @generated
	 */
    HydroGeneratingUnit getHydroGeneratingUnit();

    /**
	 * Sets the value of the '{@link iec61970.generation.production.PenstockLossCurve#getHydroGeneratingUnit <em>Hydro Generating Unit</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Hydro Generating Unit</em>' reference.
	 * @see #getHydroGeneratingUnit()
	 * @generated
	 */
    void setHydroGeneratingUnit(HydroGeneratingUnit value);
}
