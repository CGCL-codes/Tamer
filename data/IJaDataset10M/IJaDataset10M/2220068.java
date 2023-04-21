package hu.cubussapiens.modembed.notation.implementation.atomicImplementationNotation.impl;

import hu.cubussapiens.modembed.notation.implementation.atomicImplementationNotation.AtomicImplementationNotationPackage;
import hu.cubussapiens.modembed.notation.implementation.atomicImplementationNotation.NImplStep;
import hu.cubussapiens.modembed.notation.implementation.atomicImplementationNotation.NInterfaceImpl;
import hu.cubussapiens.modembed.notation.implementation.atomicImplementationNotation.NVariable;
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
 * An implementation of the model object '<em><b>NInterface Impl</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link hu.cubussapiens.modembed.notation.implementation.atomicImplementationNotation.impl.NInterfaceImplImpl#getRole <em>Role</em>}</li>
 *   <li>{@link hu.cubussapiens.modembed.notation.implementation.atomicImplementationNotation.impl.NInterfaceImplImpl#getParameters <em>Parameters</em>}</li>
 *   <li>{@link hu.cubussapiens.modembed.notation.implementation.atomicImplementationNotation.impl.NInterfaceImplImpl#getSteps <em>Steps</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class NInterfaceImplImpl extends MinimalEObjectImpl.Container implements NInterfaceImpl {

    /**
   * The default value of the '{@link #getRole() <em>Role</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRole()
   * @generated
   * @ordered
   */
    protected static final String ROLE_EDEFAULT = null;

    /**
   * The cached value of the '{@link #getRole() <em>Role</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRole()
   * @generated
   * @ordered
   */
    protected String role = ROLE_EDEFAULT;

    /**
   * The cached value of the '{@link #getParameters() <em>Parameters</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getParameters()
   * @generated
   * @ordered
   */
    protected EList<NVariable> parameters;

    /**
   * The cached value of the '{@link #getSteps() <em>Steps</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSteps()
   * @generated
   * @ordered
   */
    protected EList<NImplStep> steps;

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    protected NInterfaceImplImpl() {
        super();
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    protected EClass eStaticClass() {
        return AtomicImplementationNotationPackage.Literals.NINTERFACE_IMPL;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public String getRole() {
        return role;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setRole(String newRole) {
        String oldRole = role;
        role = newRole;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, AtomicImplementationNotationPackage.NINTERFACE_IMPL__ROLE, oldRole, role));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public EList<NVariable> getParameters() {
        if (parameters == null) {
            parameters = new EObjectContainmentEList<NVariable>(NVariable.class, this, AtomicImplementationNotationPackage.NINTERFACE_IMPL__PARAMETERS);
        }
        return parameters;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public EList<NImplStep> getSteps() {
        if (steps == null) {
            steps = new EObjectContainmentEList<NImplStep>(NImplStep.class, this, AtomicImplementationNotationPackage.NINTERFACE_IMPL__STEPS);
        }
        return steps;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case AtomicImplementationNotationPackage.NINTERFACE_IMPL__PARAMETERS:
                return ((InternalEList<?>) getParameters()).basicRemove(otherEnd, msgs);
            case AtomicImplementationNotationPackage.NINTERFACE_IMPL__STEPS:
                return ((InternalEList<?>) getSteps()).basicRemove(otherEnd, msgs);
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
            case AtomicImplementationNotationPackage.NINTERFACE_IMPL__ROLE:
                return getRole();
            case AtomicImplementationNotationPackage.NINTERFACE_IMPL__PARAMETERS:
                return getParameters();
            case AtomicImplementationNotationPackage.NINTERFACE_IMPL__STEPS:
                return getSteps();
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
            case AtomicImplementationNotationPackage.NINTERFACE_IMPL__ROLE:
                setRole((String) newValue);
                return;
            case AtomicImplementationNotationPackage.NINTERFACE_IMPL__PARAMETERS:
                getParameters().clear();
                getParameters().addAll((Collection<? extends NVariable>) newValue);
                return;
            case AtomicImplementationNotationPackage.NINTERFACE_IMPL__STEPS:
                getSteps().clear();
                getSteps().addAll((Collection<? extends NImplStep>) newValue);
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
            case AtomicImplementationNotationPackage.NINTERFACE_IMPL__ROLE:
                setRole(ROLE_EDEFAULT);
                return;
            case AtomicImplementationNotationPackage.NINTERFACE_IMPL__PARAMETERS:
                getParameters().clear();
                return;
            case AtomicImplementationNotationPackage.NINTERFACE_IMPL__STEPS:
                getSteps().clear();
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
            case AtomicImplementationNotationPackage.NINTERFACE_IMPL__ROLE:
                return ROLE_EDEFAULT == null ? role != null : !ROLE_EDEFAULT.equals(role);
            case AtomicImplementationNotationPackage.NINTERFACE_IMPL__PARAMETERS:
                return parameters != null && !parameters.isEmpty();
            case AtomicImplementationNotationPackage.NINTERFACE_IMPL__STEPS:
                return steps != null && !steps.isEmpty();
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
        result.append(" (role: ");
        result.append(role);
        result.append(')');
        return result.toString();
    }
}
