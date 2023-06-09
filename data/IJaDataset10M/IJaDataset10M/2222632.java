package iec61970.wires.impl;

import iec61970.wires.DCLineSegment;
import iec61970.wires.WiresPackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>DC Line Segment</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link iec61970.wires.impl.DCLineSegmentImpl#getDcSegmentInductance <em>Dc Segment Inductance</em>}</li>
 *   <li>{@link iec61970.wires.impl.DCLineSegmentImpl#getDcSegmentResistance <em>Dc Segment Resistance</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DCLineSegmentImpl extends ConductorImpl implements DCLineSegment {

    /**
	 * The default value of the '{@link #getDcSegmentInductance() <em>Dc Segment Inductance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDcSegmentInductance()
	 * @generated
	 * @ordered
	 */
    protected static final String DC_SEGMENT_INDUCTANCE_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getDcSegmentInductance() <em>Dc Segment Inductance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDcSegmentInductance()
	 * @generated
	 * @ordered
	 */
    protected String dcSegmentInductance = DC_SEGMENT_INDUCTANCE_EDEFAULT;

    /**
	 * The default value of the '{@link #getDcSegmentResistance() <em>Dc Segment Resistance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDcSegmentResistance()
	 * @generated
	 * @ordered
	 */
    protected static final String DC_SEGMENT_RESISTANCE_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getDcSegmentResistance() <em>Dc Segment Resistance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDcSegmentResistance()
	 * @generated
	 * @ordered
	 */
    protected String dcSegmentResistance = DC_SEGMENT_RESISTANCE_EDEFAULT;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected DCLineSegmentImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return WiresPackage.Literals.DC_LINE_SEGMENT;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getDcSegmentInductance() {
        return dcSegmentInductance;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setDcSegmentInductance(String newDcSegmentInductance) {
        String oldDcSegmentInductance = dcSegmentInductance;
        dcSegmentInductance = newDcSegmentInductance;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, WiresPackage.DC_LINE_SEGMENT__DC_SEGMENT_INDUCTANCE, oldDcSegmentInductance, dcSegmentInductance));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getDcSegmentResistance() {
        return dcSegmentResistance;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setDcSegmentResistance(String newDcSegmentResistance) {
        String oldDcSegmentResistance = dcSegmentResistance;
        dcSegmentResistance = newDcSegmentResistance;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, WiresPackage.DC_LINE_SEGMENT__DC_SEGMENT_RESISTANCE, oldDcSegmentResistance, dcSegmentResistance));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case WiresPackage.DC_LINE_SEGMENT__DC_SEGMENT_INDUCTANCE:
                return getDcSegmentInductance();
            case WiresPackage.DC_LINE_SEGMENT__DC_SEGMENT_RESISTANCE:
                return getDcSegmentResistance();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch(featureID) {
            case WiresPackage.DC_LINE_SEGMENT__DC_SEGMENT_INDUCTANCE:
                setDcSegmentInductance((String) newValue);
                return;
            case WiresPackage.DC_LINE_SEGMENT__DC_SEGMENT_RESISTANCE:
                setDcSegmentResistance((String) newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public void eUnset(int featureID) {
        switch(featureID) {
            case WiresPackage.DC_LINE_SEGMENT__DC_SEGMENT_INDUCTANCE:
                setDcSegmentInductance(DC_SEGMENT_INDUCTANCE_EDEFAULT);
                return;
            case WiresPackage.DC_LINE_SEGMENT__DC_SEGMENT_RESISTANCE:
                setDcSegmentResistance(DC_SEGMENT_RESISTANCE_EDEFAULT);
                return;
        }
        super.eUnset(featureID);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public boolean eIsSet(int featureID) {
        switch(featureID) {
            case WiresPackage.DC_LINE_SEGMENT__DC_SEGMENT_INDUCTANCE:
                return DC_SEGMENT_INDUCTANCE_EDEFAULT == null ? dcSegmentInductance != null : !DC_SEGMENT_INDUCTANCE_EDEFAULT.equals(dcSegmentInductance);
            case WiresPackage.DC_LINE_SEGMENT__DC_SEGMENT_RESISTANCE:
                return DC_SEGMENT_RESISTANCE_EDEFAULT == null ? dcSegmentResistance != null : !DC_SEGMENT_RESISTANCE_EDEFAULT.equals(dcSegmentResistance);
        }
        return super.eIsSet(featureID);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public String toString() {
        if (eIsProxy()) return super.toString();
        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (dcSegmentInductance: ");
        result.append(dcSegmentInductance);
        result.append(", dcSegmentResistance: ");
        result.append(dcSegmentResistance);
        result.append(')');
        return result.toString();
    }
}
