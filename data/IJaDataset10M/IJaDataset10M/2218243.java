package iec61970.core;

import iec61970.domain.UnitMultiplier;
import iec61970.domain.UnitSymbol;
import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Curve</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Relationship between an independent variable (X-axis) and one or two dependent 
 * variables (Y1-axis and Y2-axis). Curves can also serve as schedules.
 * 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link iec61970.core.Curve#getCurveStyle <em>Curve Style</em>}</li>
 *   <li>{@link iec61970.core.Curve#getXUnit <em>XUnit</em>}</li>
 *   <li>{@link iec61970.core.Curve#getXMultiplier <em>XMultiplier</em>}</li>
 *   <li>{@link iec61970.core.Curve#getY1Unit <em>Y1 Unit</em>}</li>
 *   <li>{@link iec61970.core.Curve#getY1Multiplier <em>Y1 Multiplier</em>}</li>
 *   <li>{@link iec61970.core.Curve#getY2Unit <em>Y2 Unit</em>}</li>
 *   <li>{@link iec61970.core.Curve#getY2Multiplier <em>Y2 Multiplier</em>}</li>
 *   <li>{@link iec61970.core.Curve#getCurveScheduleDatas <em>Curve Schedule Datas</em>}</li>
 * </ul>
 * </p>
 *
 * @see iec61970.core.CorePackage#getCurve()
 * @model
 * @generated
 */
public interface Curve extends IdentifiedObject {

    /**
	 * Returns the value of the '<em><b>Curve Style</b></em>' attribute.
	 * The literals are from the enumeration {@link iec61970.core.CurveStyle}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The style or shape of the curve.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Curve Style</em>' attribute.
	 * @see iec61970.core.CurveStyle
	 * @see #setCurveStyle(CurveStyle)
	 * @see iec61970.core.CorePackage#getCurve_CurveStyle()
	 * @model
	 * @generated
	 */
    CurveStyle getCurveStyle();

    /**
	 * Sets the value of the '{@link iec61970.core.Curve#getCurveStyle <em>Curve Style</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Curve Style</em>' attribute.
	 * @see iec61970.core.CurveStyle
	 * @see #getCurveStyle()
	 * @generated
	 */
    void setCurveStyle(CurveStyle value);

    /**
	 * Returns the value of the '<em><b>XUnit</b></em>' attribute.
	 * The literals are from the enumeration {@link iec61970.domain.UnitSymbol}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>XUnit</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>XUnit</em>' attribute.
	 * @see iec61970.domain.UnitSymbol
	 * @see #setXUnit(UnitSymbol)
	 * @see iec61970.core.CorePackage#getCurve_XUnit()
	 * @model
	 * @generated
	 */
    UnitSymbol getXUnit();

    /**
	 * Sets the value of the '{@link iec61970.core.Curve#getXUnit <em>XUnit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>XUnit</em>' attribute.
	 * @see iec61970.domain.UnitSymbol
	 * @see #getXUnit()
	 * @generated
	 */
    void setXUnit(UnitSymbol value);

    /**
	 * Returns the value of the '<em><b>XMultiplier</b></em>' attribute.
	 * The literals are from the enumeration {@link iec61970.domain.UnitMultiplier}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>XMultiplier</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>XMultiplier</em>' attribute.
	 * @see iec61970.domain.UnitMultiplier
	 * @see #setXMultiplier(UnitMultiplier)
	 * @see iec61970.core.CorePackage#getCurve_XMultiplier()
	 * @model
	 * @generated
	 */
    UnitMultiplier getXMultiplier();

    /**
	 * Sets the value of the '{@link iec61970.core.Curve#getXMultiplier <em>XMultiplier</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>XMultiplier</em>' attribute.
	 * @see iec61970.domain.UnitMultiplier
	 * @see #getXMultiplier()
	 * @generated
	 */
    void setXMultiplier(UnitMultiplier value);

    /**
	 * Returns the value of the '<em><b>Y1 Unit</b></em>' attribute.
	 * The literals are from the enumeration {@link iec61970.domain.UnitSymbol}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The Y1-axis units of measure.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Y1 Unit</em>' attribute.
	 * @see iec61970.domain.UnitSymbol
	 * @see #setY1Unit(UnitSymbol)
	 * @see iec61970.core.CorePackage#getCurve_Y1Unit()
	 * @model
	 * @generated
	 */
    UnitSymbol getY1Unit();

    /**
	 * Sets the value of the '{@link iec61970.core.Curve#getY1Unit <em>Y1 Unit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Y1 Unit</em>' attribute.
	 * @see iec61970.domain.UnitSymbol
	 * @see #getY1Unit()
	 * @generated
	 */
    void setY1Unit(UnitSymbol value);

    /**
	 * Returns the value of the '<em><b>Y1 Multiplier</b></em>' attribute.
	 * The literals are from the enumeration {@link iec61970.domain.UnitMultiplier}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Y1 Multiplier</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Y1 Multiplier</em>' attribute.
	 * @see iec61970.domain.UnitMultiplier
	 * @see #setY1Multiplier(UnitMultiplier)
	 * @see iec61970.core.CorePackage#getCurve_Y1Multiplier()
	 * @model
	 * @generated
	 */
    UnitMultiplier getY1Multiplier();

    /**
	 * Sets the value of the '{@link iec61970.core.Curve#getY1Multiplier <em>Y1 Multiplier</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Y1 Multiplier</em>' attribute.
	 * @see iec61970.domain.UnitMultiplier
	 * @see #getY1Multiplier()
	 * @generated
	 */
    void setY1Multiplier(UnitMultiplier value);

    /**
	 * Returns the value of the '<em><b>Y2 Unit</b></em>' attribute.
	 * The literals are from the enumeration {@link iec61970.domain.UnitSymbol}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The Y2-axis units of measure.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Y2 Unit</em>' attribute.
	 * @see iec61970.domain.UnitSymbol
	 * @see #setY2Unit(UnitSymbol)
	 * @see iec61970.core.CorePackage#getCurve_Y2Unit()
	 * @model
	 * @generated
	 */
    UnitSymbol getY2Unit();

    /**
	 * Sets the value of the '{@link iec61970.core.Curve#getY2Unit <em>Y2 Unit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Y2 Unit</em>' attribute.
	 * @see iec61970.domain.UnitSymbol
	 * @see #getY2Unit()
	 * @generated
	 */
    void setY2Unit(UnitSymbol value);

    /**
	 * Returns the value of the '<em><b>Y2 Multiplier</b></em>' attribute.
	 * The literals are from the enumeration {@link iec61970.domain.UnitMultiplier}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Y2 Multiplier</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Y2 Multiplier</em>' attribute.
	 * @see iec61970.domain.UnitMultiplier
	 * @see #setY2Multiplier(UnitMultiplier)
	 * @see iec61970.core.CorePackage#getCurve_Y2Multiplier()
	 * @model
	 * @generated
	 */
    UnitMultiplier getY2Multiplier();

    /**
	 * Sets the value of the '{@link iec61970.core.Curve#getY2Multiplier <em>Y2 Multiplier</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Y2 Multiplier</em>' attribute.
	 * @see iec61970.domain.UnitMultiplier
	 * @see #getY2Multiplier()
	 * @generated
	 */
    void setY2Multiplier(UnitMultiplier value);

    /**
	 * Returns the value of the '<em><b>Curve Schedule Datas</b></em>' reference list.
	 * The list contents are of type {@link iec61970.core.CurveData}.
	 * It is bidirectional and its opposite is '{@link iec61970.core.CurveData#getCurveSchedule <em>Curve Schedule</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The point data values that define a curve
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Curve Schedule Datas</em>' reference list.
	 * @see iec61970.core.CorePackage#getCurve_CurveScheduleDatas()
	 * @see iec61970.core.CurveData#getCurveSchedule
	 * @model opposite="CurveSchedule"
	 * @generated
	 */
    EList<CurveData> getCurveScheduleDatas();
}
