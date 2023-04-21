package com.safi.db.server.config.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import com.safi.db.server.config.ConfigPackage;
import com.safi.db.server.config.Saflet;
import com.safi.db.server.config.SafletProject;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Saflet</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.safi.db.server.config.impl.SafletImpl#getCode <em>Code</em>}</li>
 *   <li>{@link com.safi.db.server.config.impl.SafletImpl#getProject <em>Project</em>}</li>
 *   <li>{@link com.safi.db.server.config.impl.SafletImpl#getSubsystemId <em>Subsystem Id</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SafletImpl extends ServerResourceImpl implements Saflet {

    /**
	 * The default value of the '{@link #getCode() <em>Code</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see #getCode()
	 * @generated
	 * @ordered
	 */
    protected static final byte[] CODE_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getCode() <em>Code</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see #getCode()
	 * @generated
	 * @ordered
	 */
    protected byte[] code = CODE_EDEFAULT;

    /**
	 * The default value of the '{@link #getSubsystemId() <em>Subsystem Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSubsystemId()
	 * @generated
	 * @ordered
	 */
    protected static final String SUBSYSTEM_ID_EDEFAULT = "";

    /**
	 * The cached value of the '{@link #getSubsystemId() <em>Subsystem Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSubsystemId()
	 * @generated
	 * @ordered
	 */
    protected String subsystemId = SUBSYSTEM_ID_EDEFAULT;

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    protected SafletImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return ConfigPackage.Literals.SAFLET;
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    public byte[] getCode() {
        return code;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setCode(byte[] newCode) {
        byte[] oldCode = code;
        code = newCode;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.SAFLET__CODE, oldCode, code));
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    public SafletProject getProject() {
        if (eContainerFeatureID() != ConfigPackage.SAFLET__PROJECT) return null;
        return (SafletProject) eContainer();
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetProject(SafletProject newProject, NotificationChain msgs) {
        msgs = eBasicSetContainer((InternalEObject) newProject, ConfigPackage.SAFLET__PROJECT, msgs);
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    public void setProject(SafletProject newProject) {
        if (newProject != eInternalContainer() || (eContainerFeatureID() != ConfigPackage.SAFLET__PROJECT && newProject != null)) {
            if (EcoreUtil.isAncestor(this, newProject)) throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
            NotificationChain msgs = null;
            if (eInternalContainer() != null) msgs = eBasicRemoveFromContainer(msgs);
            if (newProject != null) msgs = ((InternalEObject) newProject).eInverseAdd(this, ConfigPackage.SAFLET_PROJECT__SAFLETS, SafletProject.class, msgs);
            msgs = basicSetProject(newProject, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.SAFLET__PROJECT, newProject, newProject));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getSubsystemId() {
        return subsystemId;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setSubsystemId(String newSubsystemId) {
        String oldSubsystemId = subsystemId;
        subsystemId = newSubsystemId;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.SAFLET__SUBSYSTEM_ID, oldSubsystemId, subsystemId));
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case ConfigPackage.SAFLET__PROJECT:
                if (eInternalContainer() != null) msgs = eBasicRemoveFromContainer(msgs);
                return basicSetProject((SafletProject) otherEnd, msgs);
        }
        return super.eInverseAdd(otherEnd, featureID, msgs);
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case ConfigPackage.SAFLET__PROJECT:
                return basicSetProject(null, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
        switch(eContainerFeatureID()) {
            case ConfigPackage.SAFLET__PROJECT:
                return eInternalContainer().eInverseRemove(this, ConfigPackage.SAFLET_PROJECT__SAFLETS, SafletProject.class, msgs);
        }
        return super.eBasicRemoveFromContainerFeature(msgs);
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case ConfigPackage.SAFLET__CODE:
                return getCode();
            case ConfigPackage.SAFLET__PROJECT:
                return getProject();
            case ConfigPackage.SAFLET__SUBSYSTEM_ID:
                return getSubsystemId();
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
            case ConfigPackage.SAFLET__CODE:
                setCode((byte[]) newValue);
                return;
            case ConfigPackage.SAFLET__PROJECT:
                setProject((SafletProject) newValue);
                return;
            case ConfigPackage.SAFLET__SUBSYSTEM_ID:
                setSubsystemId((String) newValue);
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
            case ConfigPackage.SAFLET__CODE:
                setCode(CODE_EDEFAULT);
                return;
            case ConfigPackage.SAFLET__PROJECT:
                setProject((SafletProject) null);
                return;
            case ConfigPackage.SAFLET__SUBSYSTEM_ID:
                setSubsystemId(SUBSYSTEM_ID_EDEFAULT);
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
            case ConfigPackage.SAFLET__CODE:
                return CODE_EDEFAULT == null ? code != null : !CODE_EDEFAULT.equals(code);
            case ConfigPackage.SAFLET__PROJECT:
                return getProject() != null;
            case ConfigPackage.SAFLET__SUBSYSTEM_ID:
                return SUBSYSTEM_ID_EDEFAULT == null ? subsystemId != null : !SUBSYSTEM_ID_EDEFAULT.equals(subsystemId);
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
        result.append(" (code: ");
        result.append(code);
        result.append(", subsystemId: ");
        result.append(subsystemId);
        result.append(')');
        return result.toString();
    }
}
