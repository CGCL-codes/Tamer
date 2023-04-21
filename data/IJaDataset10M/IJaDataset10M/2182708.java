package net.sf.orcc.ir.impl;

import net.sf.orcc.ir.IrPackage;
import net.sf.orcc.ir.TypeInt;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * This class defines an integer type.
 * 
 * @author Matthieu Wipliez
 * @author Jerome Gorin
 * 
 */
public class TypeIntImpl extends TypeImpl implements TypeInt {

    /**
	 * The default value of the '{@link #getSize() <em>Size</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getSize()
	 * @generated
	 * @ordered
	 */
    protected static final int SIZE_EDEFAULT = 0;

    private int size;

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    protected TypeIntImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case IrPackage.TYPE_INT__SIZE:
                return getSize();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public boolean eIsSet(int featureID) {
        switch(featureID) {
            case IrPackage.TYPE_INT__SIZE:
                return size != SIZE_EDEFAULT;
        }
        return super.eIsSet(featureID);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TypeInt) {
            return size == ((TypeInt) obj).getSize();
        } else {
            return false;
        }
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch(featureID) {
            case IrPackage.TYPE_INT__SIZE:
                setSize((Integer) newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return IrPackage.Literals.TYPE_INT;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public void eUnset(int featureID) {
        switch(featureID) {
            case IrPackage.TYPE_INT__SIZE:
                setSize(SIZE_EDEFAULT);
                return;
        }
        super.eUnset(featureID);
    }

    /**
	 * Returns the size of this integer type.
	 * 
	 * @return the size of this integer type
	 * @generated
	 */
    public int getSize() {
        return size;
    }

    @Override
    public int getSizeInBits() {
        return size;
    }

    @Override
    public boolean isInt() {
        return true;
    }

    @Override
    public boolean isLong() {
        return (this.getSizeInBits() > 32);
    }

    /**
	 * Sets the size of this integer type.
	 * 
	 * @param size
	 *            the size of this integer type
	 * @generated
	 */
    public void setSize(int newSize) {
        int oldSize = size;
        size = newSize;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, IrPackage.TYPE_INT__SIZE, oldSize, size));
    }

    @Override
    public String toString() {
        return "int(size=" + getSize() + ")";
    }
}
