package default_.testpackage.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import default_.testpackage.BooleanValue;
import default_.testpackage.RealValue;
import default_.testpackage.TestpackagePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Real Value</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class RealValueImpl extends IntValueImpl implements RealValue {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected RealValueImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return TestpackagePackage.Literals.REAL_VALUE;
    }

    public float getFloatValue() {
        return Float.intBitsToFloat(getValue().intValue());
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
    public void setValue(float newValue) {
        super.setValue(Float.floatToIntBits(newValue) & 0x00000000FFFFFFFFL);
        setInitialized(true);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
    @Override
    public String toString() {
        return Float.toString(getFloatValue());
    }

    @Override
    public boolean equals(Object arg0) {
        if (arg0 == null) return false;
        if (!(arg0 instanceof RealValue)) return false;
        if (initialized ^ ((RealValue) arg0).isInitialized()) return false;
        if (value == ((RealValue) arg0).getValue().longValue()) return true;
        return false;
    }

    @Override
    public int hashCode() {
        int hc = 17;
        int m = 43;
        hc = hc * m + ((getValue() == 0.0F) ? 0 : Float.floatToIntBits(getValue()));
        hc = hc * m + valueType.hashCode();
        hc = hc * m + (initialized ? 0 : 1);
        return hc;
    }

    @Override
    public void setValue(String value) {
        float f;
        try {
            f = Float.parseFloat(value);
        } catch (Exception e) {
            if (value.equals("")) {
                setValue(0.0f);
                setInitialized(false);
            }
            return;
        }
        setValue(f);
    }
}
