package it.unisannio.rcost.callgraphanalyzer.impl;

import it.unisannio.rcost.callgraphanalyzer.CallGraphPackage;
import it.unisannio.rcost.callgraphanalyzer.NodeContainer;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Node Container</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link it.unisannio.rcost.callgraphanalyzer.impl.NodeContainerImpl#getPackageName <em>Package Name</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class NodeContainerImpl extends NodeImpl implements NodeContainer {

    /**
	 * The default value of the '{@link #getPackageName() <em>Package Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPackageName()
	 * @generated
	 * @ordered
	 */
    protected static final String PACKAGE_NAME_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getPackageName() <em>Package Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPackageName()
	 * @generated
	 * @ordered
	 */
    protected String packageName = PACKAGE_NAME_EDEFAULT;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected NodeContainerImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return CallGraphPackage.Literals.NODE_CONTAINER;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getPackageName() {
        return packageName;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setPackageName(String newPackageName) {
        String oldPackageName = packageName;
        packageName = newPackageName;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, CallGraphPackage.NODE_CONTAINER__PACKAGE_NAME, oldPackageName, packageName));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case CallGraphPackage.NODE_CONTAINER__PACKAGE_NAME:
                return getPackageName();
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
            case CallGraphPackage.NODE_CONTAINER__PACKAGE_NAME:
                setPackageName((String) newValue);
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
            case CallGraphPackage.NODE_CONTAINER__PACKAGE_NAME:
                setPackageName(PACKAGE_NAME_EDEFAULT);
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
            case CallGraphPackage.NODE_CONTAINER__PACKAGE_NAME:
                return PACKAGE_NAME_EDEFAULT == null ? packageName != null : !PACKAGE_NAME_EDEFAULT.equals(packageName);
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
        result.append(" (packageName: ");
        result.append(packageName);
        result.append(')');
        return result.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((packageName == null) ? 0 : packageName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (!(obj instanceof NodeContainer)) return false;
        final NodeContainerImpl other = (NodeContainerImpl) obj;
        if (packageName == null) {
            if (other.packageName != null) return false;
        } else if (!packageName.equals(other.packageName)) return false;
        return true;
    }

    @Override
    boolean isValidModifiers() {
        return false;
    }

    @Override
    public it.unisannio.rcost.callgraphanalyzer.Node clone() {
        NodeContainer nodeClone = (NodeContainer) super.clone();
        nodeClone.setPackageName(this.packageName);
        return nodeClone;
    }
}
