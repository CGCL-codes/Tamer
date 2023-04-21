package de.fraunhofer.isst.vts.axlang.impl;

import de.fraunhofer.isst.vts.axlang.ApplicationModel;
import de.fraunhofer.isst.vts.axlang.AxlangPackage;
import de.fraunhofer.isst.vts.axlang.Model;
import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link de.fraunhofer.isst.vts.axlang.impl.ModelImpl#getName <em>Name</em>}</li>
 *   <li>{@link de.fraunhofer.isst.vts.axlang.impl.ModelImpl#getApplicationmodels <em>Applicationmodels</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ModelImpl extends MinimalEObjectImpl.Container implements Model {

    /**
   * The default value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
    protected static final String NAME_EDEFAULT = null;

    /**
   * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
    protected String name = NAME_EDEFAULT;

    /**
   * The cached value of the '{@link #getApplicationmodels() <em>Applicationmodels</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getApplicationmodels()
   * @generated
   * @ordered
   */
    protected EList<ApplicationModel> applicationmodels;

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    protected ModelImpl() {
        super();
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    protected EClass eStaticClass() {
        return AxlangPackage.Literals.MODEL;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public String getName() {
        return name;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setName(String newName) {
        String oldName = name;
        name = newName;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, AxlangPackage.MODEL__NAME, oldName, name));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public EList<ApplicationModel> getApplicationmodels() {
        if (applicationmodels == null) {
            applicationmodels = new EObjectContainmentEList<ApplicationModel>(ApplicationModel.class, this, AxlangPackage.MODEL__APPLICATIONMODELS);
        }
        return applicationmodels;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case AxlangPackage.MODEL__APPLICATIONMODELS:
                return ((InternalEList<?>) getApplicationmodels()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case AxlangPackage.MODEL__NAME:
                return getName();
            case AxlangPackage.MODEL__APPLICATIONMODELS:
                return getApplicationmodels();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch(featureID) {
            case AxlangPackage.MODEL__NAME:
                setName((String) newValue);
                return;
            case AxlangPackage.MODEL__APPLICATIONMODELS:
                getApplicationmodels().clear();
                getApplicationmodels().addAll((Collection<? extends ApplicationModel>) newValue);
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
            case AxlangPackage.MODEL__NAME:
                setName(NAME_EDEFAULT);
                return;
            case AxlangPackage.MODEL__APPLICATIONMODELS:
                getApplicationmodels().clear();
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
            case AxlangPackage.MODEL__NAME:
                return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
            case AxlangPackage.MODEL__APPLICATIONMODELS:
                return applicationmodels != null && !applicationmodels.isEmpty();
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
        result.append(" (name: ");
        result.append(name);
        result.append(')');
        return result.toString();
    }
}
