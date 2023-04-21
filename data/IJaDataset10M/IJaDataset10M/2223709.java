package eu.medeia.caex.model.CAEXClassModelV215.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import eu.medeia.caex.model.CAEXClassModelV215.CAEXClassModelV215Package;
import eu.medeia.caex.model.CAEXClassModelV215.RefSemanticType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Ref Semantic Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link eu.medeia.caex.model.CAEXClassModelV215.impl.RefSemanticTypeImpl#getCorrespondingAttributePath <em>Corresponding Attribute Path</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class RefSemanticTypeImpl extends CAEXBasicObjectImpl implements RefSemanticType {

    /**
	 * The default value of the '{@link #getCorrespondingAttributePath() <em>Corresponding Attribute Path</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCorrespondingAttributePath()
	 * @generated
	 * @ordered
	 */
    protected static final String CORRESPONDING_ATTRIBUTE_PATH_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getCorrespondingAttributePath() <em>Corresponding Attribute Path</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCorrespondingAttributePath()
	 * @generated
	 * @ordered
	 */
    protected String correspondingAttributePath = CORRESPONDING_ATTRIBUTE_PATH_EDEFAULT;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected RefSemanticTypeImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return CAEXClassModelV215Package.Literals.REF_SEMANTIC_TYPE;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getCorrespondingAttributePath() {
        return correspondingAttributePath;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setCorrespondingAttributePath(String newCorrespondingAttributePath) {
        String oldCorrespondingAttributePath = correspondingAttributePath;
        correspondingAttributePath = newCorrespondingAttributePath;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, CAEXClassModelV215Package.REF_SEMANTIC_TYPE__CORRESPONDING_ATTRIBUTE_PATH, oldCorrespondingAttributePath, correspondingAttributePath));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case CAEXClassModelV215Package.REF_SEMANTIC_TYPE__CORRESPONDING_ATTRIBUTE_PATH:
                return getCorrespondingAttributePath();
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
            case CAEXClassModelV215Package.REF_SEMANTIC_TYPE__CORRESPONDING_ATTRIBUTE_PATH:
                setCorrespondingAttributePath((String) newValue);
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
            case CAEXClassModelV215Package.REF_SEMANTIC_TYPE__CORRESPONDING_ATTRIBUTE_PATH:
                setCorrespondingAttributePath(CORRESPONDING_ATTRIBUTE_PATH_EDEFAULT);
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
            case CAEXClassModelV215Package.REF_SEMANTIC_TYPE__CORRESPONDING_ATTRIBUTE_PATH:
                return CORRESPONDING_ATTRIBUTE_PATH_EDEFAULT == null ? correspondingAttributePath != null : !CORRESPONDING_ATTRIBUTE_PATH_EDEFAULT.equals(correspondingAttributePath);
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
        result.append(" (correspondingAttributePath: ");
        result.append(correspondingAttributePath);
        result.append(')');
        return result.toString();
    }
}
