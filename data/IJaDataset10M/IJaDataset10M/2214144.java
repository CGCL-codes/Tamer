package com.safi.core.scripting.impl;

import com.safi.core.scripting.SafletScript;
import com.safi.core.scripting.SafletScriptFactory;
import com.safi.core.scripting.ScriptingPackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Saflet Script Factory</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.safi.core.scripting.impl.SafletScriptFactoryImpl#getSafletScript <em>Saflet Script</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class SafletScriptFactoryImpl extends EObjectImpl implements SafletScriptFactory {

    /**
	 * The cached value of the '{@link #getSafletScript() <em>Saflet Script</em>}' reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see #getSafletScript()
	 * @generated
	 * @ordered
	 */
    protected SafletScript safletScript;

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    protected SafletScriptFactoryImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return ScriptingPackage.Literals.SAFLET_SCRIPT_FACTORY;
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    public SafletScript getSafletScript() {
        if (safletScript != null && safletScript.eIsProxy()) {
            InternalEObject oldSafletScript = (InternalEObject) safletScript;
            safletScript = (SafletScript) eResolveProxy(oldSafletScript);
            if (safletScript != oldSafletScript) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, ScriptingPackage.SAFLET_SCRIPT_FACTORY__SAFLET_SCRIPT, oldSafletScript, safletScript));
            }
        }
        return safletScript;
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    public SafletScript basicGetSafletScript() {
        return safletScript;
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    public SafletScript getSafletScript(String name, String scriptText) {
        throw new UnsupportedOperationException();
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case ScriptingPackage.SAFLET_SCRIPT_FACTORY__SAFLET_SCRIPT:
                if (resolve) return getSafletScript();
                return basicGetSafletScript();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public boolean eIsSet(int featureID) {
        switch(featureID) {
            case ScriptingPackage.SAFLET_SCRIPT_FACTORY__SAFLET_SCRIPT:
                return safletScript != null;
        }
        return super.eIsSet(featureID);
    }
}
