package hub.metrik.lang.dtmfcontrol.impl;

import hub.metrik.lang.dtmfcontrol.Block;
import hub.metrik.lang.dtmfcontrol.DtmfcontrolPackage;
import hub.metrik.lang.dtmfcontrol.Goto;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Goto</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link hub.metrik.lang.dtmfcontrol.impl.GotoImpl#getTarget <em>Target</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class GotoImpl extends CommandImpl implements Goto {

    /**
	 * The cached value of the '{@link #getTarget() <em>Target</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTarget()
	 * @generated
	 * @ordered
	 */
    protected Block target;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected GotoImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return DtmfcontrolPackage.Literals.GOTO;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Block getTarget() {
        if (target != null && target.eIsProxy()) {
            InternalEObject oldTarget = (InternalEObject) target;
            target = (Block) eResolveProxy(oldTarget);
            if (target != oldTarget) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, DtmfcontrolPackage.GOTO__TARGET, oldTarget, target));
            }
        }
        return target;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Block basicGetTarget() {
        return target;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setTarget(Block newTarget) {
        Block oldTarget = target;
        target = newTarget;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, DtmfcontrolPackage.GOTO__TARGET, oldTarget, target));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case DtmfcontrolPackage.GOTO__TARGET:
                if (resolve) return getTarget();
                return basicGetTarget();
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
            case DtmfcontrolPackage.GOTO__TARGET:
                setTarget((Block) newValue);
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
            case DtmfcontrolPackage.GOTO__TARGET:
                setTarget((Block) null);
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
            case DtmfcontrolPackage.GOTO__TARGET:
                return target != null;
        }
        return super.eIsSet(featureID);
    }
}
