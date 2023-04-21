package com.hofstetter.diplthesis.ctb.ctb.impl;

import com.hofstetter.diplthesis.ctb.ctb.CommunicationComponent;
import com.hofstetter.diplthesis.ctb.ctb.CtbPackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Communication Component</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.hofstetter.diplthesis.ctb.ctb.impl.CommunicationComponentImpl#getCommunicationComponent <em>Communication Component</em>}</li>
 *   <li>{@link com.hofstetter.diplthesis.ctb.ctb.impl.CommunicationComponentImpl#getTechnology_Artifacts <em>Technology Artifacts</em>}</li>
 *   <li>{@link com.hofstetter.diplthesis.ctb.ctb.impl.CommunicationComponentImpl#getMEX <em>MEX</em>}</li>
 *   <li>{@link com.hofstetter.diplthesis.ctb.ctb.impl.CommunicationComponentImpl#getSynchrony <em>Synchrony</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CommunicationComponentImpl extends RuntimeComponentImpl implements CommunicationComponent {

    /**
	 * The default value of the '{@link #getCommunicationComponent() <em>Communication Component</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCommunicationComponent()
	 * @generated
	 * @ordered
	 */
    protected static final String COMMUNICATION_COMPONENT_EDEFAULT = "<CommunicationComponent>";

    /**
	 * The cached value of the '{@link #getCommunicationComponent() <em>Communication Component</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCommunicationComponent()
	 * @generated
	 * @ordered
	 */
    protected String communicationComponent = COMMUNICATION_COMPONENT_EDEFAULT;

    /**
	 * The default value of the '{@link #getTechnology_Artifacts() <em>Technology Artifacts</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTechnology_Artifacts()
	 * @generated
	 * @ordered
	 */
    protected static final String TECHNOLOGY_ARTIFACTS_EDEFAULT = "<Technology-Artifacts>";

    /**
	 * The cached value of the '{@link #getTechnology_Artifacts() <em>Technology Artifacts</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTechnology_Artifacts()
	 * @generated
	 * @ordered
	 */
    protected String technology_Artifacts = TECHNOLOGY_ARTIFACTS_EDEFAULT;

    /**
	 * The default value of the '{@link #getMEX() <em>MEX</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMEX()
	 * @generated
	 * @ordered
	 */
    protected static final String MEX_EDEFAULT = "<MEX>";

    /**
	 * The cached value of the '{@link #getMEX() <em>MEX</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMEX()
	 * @generated
	 * @ordered
	 */
    protected String mex = MEX_EDEFAULT;

    /**
	 * The default value of the '{@link #getSynchrony() <em>Synchrony</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSynchrony()
	 * @generated
	 * @ordered
	 */
    protected static final String SYNCHRONY_EDEFAULT = "<Synchrony>";

    /**
	 * The cached value of the '{@link #getSynchrony() <em>Synchrony</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSynchrony()
	 * @generated
	 * @ordered
	 */
    protected String synchrony = SYNCHRONY_EDEFAULT;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected CommunicationComponentImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return CtbPackage.Literals.COMMUNICATION_COMPONENT;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getCommunicationComponent() {
        return communicationComponent;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setCommunicationComponent(String newCommunicationComponent) {
        String oldCommunicationComponent = communicationComponent;
        communicationComponent = newCommunicationComponent;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, CtbPackage.COMMUNICATION_COMPONENT__COMMUNICATION_COMPONENT, oldCommunicationComponent, communicationComponent));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getTechnology_Artifacts() {
        return technology_Artifacts;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setTechnology_Artifacts(String newTechnology_Artifacts) {
        String oldTechnology_Artifacts = technology_Artifacts;
        technology_Artifacts = newTechnology_Artifacts;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, CtbPackage.COMMUNICATION_COMPONENT__TECHNOLOGY_ARTIFACTS, oldTechnology_Artifacts, technology_Artifacts));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getMEX() {
        return mex;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setMEX(String newMEX) {
        String oldMEX = mex;
        mex = newMEX;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, CtbPackage.COMMUNICATION_COMPONENT__MEX, oldMEX, mex));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getSynchrony() {
        return synchrony;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setSynchrony(String newSynchrony) {
        String oldSynchrony = synchrony;
        synchrony = newSynchrony;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, CtbPackage.COMMUNICATION_COMPONENT__SYNCHRONY, oldSynchrony, synchrony));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case CtbPackage.COMMUNICATION_COMPONENT__COMMUNICATION_COMPONENT:
                return getCommunicationComponent();
            case CtbPackage.COMMUNICATION_COMPONENT__TECHNOLOGY_ARTIFACTS:
                return getTechnology_Artifacts();
            case CtbPackage.COMMUNICATION_COMPONENT__MEX:
                return getMEX();
            case CtbPackage.COMMUNICATION_COMPONENT__SYNCHRONY:
                return getSynchrony();
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
            case CtbPackage.COMMUNICATION_COMPONENT__COMMUNICATION_COMPONENT:
                setCommunicationComponent((String) newValue);
                return;
            case CtbPackage.COMMUNICATION_COMPONENT__TECHNOLOGY_ARTIFACTS:
                setTechnology_Artifacts((String) newValue);
                return;
            case CtbPackage.COMMUNICATION_COMPONENT__MEX:
                setMEX((String) newValue);
                return;
            case CtbPackage.COMMUNICATION_COMPONENT__SYNCHRONY:
                setSynchrony((String) newValue);
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
            case CtbPackage.COMMUNICATION_COMPONENT__COMMUNICATION_COMPONENT:
                setCommunicationComponent(COMMUNICATION_COMPONENT_EDEFAULT);
                return;
            case CtbPackage.COMMUNICATION_COMPONENT__TECHNOLOGY_ARTIFACTS:
                setTechnology_Artifacts(TECHNOLOGY_ARTIFACTS_EDEFAULT);
                return;
            case CtbPackage.COMMUNICATION_COMPONENT__MEX:
                setMEX(MEX_EDEFAULT);
                return;
            case CtbPackage.COMMUNICATION_COMPONENT__SYNCHRONY:
                setSynchrony(SYNCHRONY_EDEFAULT);
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
            case CtbPackage.COMMUNICATION_COMPONENT__COMMUNICATION_COMPONENT:
                return COMMUNICATION_COMPONENT_EDEFAULT == null ? communicationComponent != null : !COMMUNICATION_COMPONENT_EDEFAULT.equals(communicationComponent);
            case CtbPackage.COMMUNICATION_COMPONENT__TECHNOLOGY_ARTIFACTS:
                return TECHNOLOGY_ARTIFACTS_EDEFAULT == null ? technology_Artifacts != null : !TECHNOLOGY_ARTIFACTS_EDEFAULT.equals(technology_Artifacts);
            case CtbPackage.COMMUNICATION_COMPONENT__MEX:
                return MEX_EDEFAULT == null ? mex != null : !MEX_EDEFAULT.equals(mex);
            case CtbPackage.COMMUNICATION_COMPONENT__SYNCHRONY:
                return SYNCHRONY_EDEFAULT == null ? synchrony != null : !SYNCHRONY_EDEFAULT.equals(synchrony);
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
        result.append(" (CommunicationComponent: ");
        result.append(communicationComponent);
        result.append(", Technology_Artifacts: ");
        result.append(technology_Artifacts);
        result.append(", MEX: ");
        result.append(mex);
        result.append(", Synchrony: ");
        result.append(synchrony);
        result.append(')');
        return result.toString();
    }
}
