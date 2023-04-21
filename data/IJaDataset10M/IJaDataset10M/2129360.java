package net.sf.xqz.engine.script.orchestror.impl;

import java.util.Collection;
import net.sf.xqz.engine.script.orchestror.DSLEngineApp;
import net.sf.xqz.engine.script.orchestror.DSLEngineClient;
import net.sf.xqz.engine.script.orchestror.OrchestrorPackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>DSL Engine App</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.sf.xqz.engine.script.orchestror.impl.DSLEngineAppImpl#getName <em>Name</em>}</li>
 *   <li>{@link net.sf.xqz.engine.script.orchestror.impl.DSLEngineAppImpl#getRefClients <em>Ref Clients</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DSLEngineAppImpl extends MinimalEObjectImpl.Container implements DSLEngineApp {

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
   * The cached value of the '{@link #getRefClients() <em>Ref Clients</em>}' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRefClients()
   * @generated
   * @ordered
   */
    protected EList<DSLEngineClient> refClients;

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    protected DSLEngineAppImpl() {
        super();
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    protected EClass eStaticClass() {
        return OrchestrorPackage.Literals.DSL_ENGINE_APP;
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
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, OrchestrorPackage.DSL_ENGINE_APP__NAME, oldName, name));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public EList<DSLEngineClient> getRefClients() {
        if (refClients == null) {
            refClients = new EObjectResolvingEList<DSLEngineClient>(DSLEngineClient.class, this, OrchestrorPackage.DSL_ENGINE_APP__REF_CLIENTS);
        }
        return refClients;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case OrchestrorPackage.DSL_ENGINE_APP__NAME:
                return getName();
            case OrchestrorPackage.DSL_ENGINE_APP__REF_CLIENTS:
                return getRefClients();
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
            case OrchestrorPackage.DSL_ENGINE_APP__NAME:
                setName((String) newValue);
                return;
            case OrchestrorPackage.DSL_ENGINE_APP__REF_CLIENTS:
                getRefClients().clear();
                getRefClients().addAll((Collection<? extends DSLEngineClient>) newValue);
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
            case OrchestrorPackage.DSL_ENGINE_APP__NAME:
                setName(NAME_EDEFAULT);
                return;
            case OrchestrorPackage.DSL_ENGINE_APP__REF_CLIENTS:
                getRefClients().clear();
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
            case OrchestrorPackage.DSL_ENGINE_APP__NAME:
                return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
            case OrchestrorPackage.DSL_ENGINE_APP__REF_CLIENTS:
                return refClients != null && !refClients.isEmpty();
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
