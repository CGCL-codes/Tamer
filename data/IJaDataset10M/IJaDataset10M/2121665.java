package net.sf.orcc.ir.impl;

import net.sf.orcc.ir.IrPackage;
import net.sf.orcc.ir.TypeString;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * This class defines a String type.
 * 
 * @author Matthieu Wipliez
 * @author Jerome Gorin
 */
public class TypeStringImpl extends TypeImpl implements TypeString {

    /**
	 * The default value of the '{@link #getSize() <em>Size</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getSize()
	 * @generated
	 * @ordered
	 */
    protected static final int SIZE_EDEFAULT = 0;

    /**
	 * The cached value of the '{@link #getSize() <em>Size</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getSize()
	 * @generated
	 * @ordered
	 */
    protected int size = SIZE_EDEFAULT;

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    protected TypeStringImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case IrPackage.TYPE_STRING__SIZE:
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
            case IrPackage.TYPE_STRING__SIZE:
                return size != SIZE_EDEFAULT;
        }
        return super.eIsSet(featureID);
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof TypeString);
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch(featureID) {
            case IrPackage.TYPE_STRING__SIZE:
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
        return IrPackage.Literals.TYPE_STRING;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public void eUnset(int featureID) {
        switch(featureID) {
            case IrPackage.TYPE_STRING__SIZE:
                setSize(SIZE_EDEFAULT);
                return;
        }
        super.eUnset(featureID);
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
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
    public boolean isString() {
        return true;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public void setSize(int newSize) {
        int oldSize = size;
        size = newSize;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, IrPackage.TYPE_STRING__SIZE, oldSize, size));
    }

    @Override
    public String toString() {
        return "String";
    }
}
