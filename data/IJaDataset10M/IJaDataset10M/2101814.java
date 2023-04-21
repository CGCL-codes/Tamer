package hub.sam.lang.vcl.impl;

import hub.sam.lang.vcl.Action;
import hub.sam.lang.vcl.ActionContainer;
import hub.sam.lang.vcl.Block;
import hub.sam.lang.vcl.Conditional;
import hub.sam.lang.vcl.VclPackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Conditional</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link hub.sam.lang.vcl.impl.ConditionalImpl#isEntered <em>Entered</em>}</li>
 *   <li>{@link hub.sam.lang.vcl.impl.ConditionalImpl#isLeft <em>Left</em>}</li>
 *   <li>{@link hub.sam.lang.vcl.impl.ConditionalImpl#getBody <em>Body</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class ConditionalImpl extends ExpressionActionImpl implements Conditional {

    /**
     * The default value of the '{@link #isEntered() <em>Entered</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isEntered()
     * @generated
     * @ordered
     */
    protected static final boolean ENTERED_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isEntered() <em>Entered</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isEntered()
     * @generated
     * @ordered
     */
    protected boolean entered = ENTERED_EDEFAULT;

    /**
     * The default value of the '{@link #isLeft() <em>Left</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isLeft()
     * @generated
     * @ordered
     */
    protected static final boolean LEFT_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isLeft() <em>Left</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isLeft()
     * @generated
     * @ordered
     */
    protected boolean left = LEFT_EDEFAULT;

    /**
     * The cached value of the '{@link #getBody() <em>Body</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getBody()
     * @generated
     * @ordered
     */
    protected Block body;

    protected Action target;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ConditionalImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return VclPackage.Literals.CONDITIONAL;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isEntered() {
        return entered;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setEntered(boolean newEntered) {
        boolean oldEntered = entered;
        entered = newEntered;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, VclPackage.CONDITIONAL__ENTERED, oldEntered, entered));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isLeft() {
        return left;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setLeft(boolean newLeft) {
        boolean oldLeft = left;
        left = newLeft;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, VclPackage.CONDITIONAL__LEFT, oldLeft, left));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Block getBody() {
        return body;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetBody(Block newBody, NotificationChain msgs) {
        Block oldBody = body;
        body = newBody;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, VclPackage.CONDITIONAL__BODY, oldBody, newBody);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setBody(Block newBody) {
        if (newBody != body) {
            NotificationChain msgs = null;
            if (body != null) msgs = ((InternalEObject) body).eInverseRemove(this, VclPackage.BLOCK__COND, Block.class, msgs);
            if (newBody != null) msgs = ((InternalEObject) newBody).eInverseAdd(this, VclPackage.BLOCK__COND, Block.class, msgs);
            msgs = basicSetBody(newBody, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, VclPackage.CONDITIONAL__BODY, newBody, newBody));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    public void enterContainer() {
        getBody().enterContainer();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    public void leaveContainer() {
        getBody().leaveContainer();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case VclPackage.CONDITIONAL__BODY:
                if (body != null) msgs = ((InternalEObject) body).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - VclPackage.CONDITIONAL__BODY, null, msgs);
                return basicSetBody((Block) otherEnd, msgs);
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
            case VclPackage.CONDITIONAL__BODY:
                return basicSetBody(null, msgs);
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
            case VclPackage.CONDITIONAL__ENTERED:
                return isEntered() ? Boolean.TRUE : Boolean.FALSE;
            case VclPackage.CONDITIONAL__LEFT:
                return isLeft() ? Boolean.TRUE : Boolean.FALSE;
            case VclPackage.CONDITIONAL__BODY:
                return getBody();
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
            case VclPackage.CONDITIONAL__ENTERED:
                setEntered(((Boolean) newValue).booleanValue());
                return;
            case VclPackage.CONDITIONAL__LEFT:
                setLeft(((Boolean) newValue).booleanValue());
                return;
            case VclPackage.CONDITIONAL__BODY:
                setBody((Block) newValue);
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
            case VclPackage.CONDITIONAL__ENTERED:
                setEntered(ENTERED_EDEFAULT);
                return;
            case VclPackage.CONDITIONAL__LEFT:
                setLeft(LEFT_EDEFAULT);
                return;
            case VclPackage.CONDITIONAL__BODY:
                setBody((Block) null);
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
            case VclPackage.CONDITIONAL__ENTERED:
                return entered != ENTERED_EDEFAULT;
            case VclPackage.CONDITIONAL__LEFT:
                return left != LEFT_EDEFAULT;
            case VclPackage.CONDITIONAL__BODY:
                return body != null;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
        if (baseClass == ActionContainer.class) {
            switch(derivedFeatureID) {
                case VclPackage.CONDITIONAL__ENTERED:
                    return VclPackage.ACTION_CONTAINER__ENTERED;
                case VclPackage.CONDITIONAL__LEFT:
                    return VclPackage.ACTION_CONTAINER__LEFT;
                default:
                    return -1;
            }
        }
        return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
        if (baseClass == ActionContainer.class) {
            switch(baseFeatureID) {
                case VclPackage.ACTION_CONTAINER__ENTERED:
                    return VclPackage.CONDITIONAL__ENTERED;
                case VclPackage.ACTION_CONTAINER__LEFT:
                    return VclPackage.CONDITIONAL__LEFT;
                default:
                    return -1;
            }
        }
        return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
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
        result.append(" (entered: ");
        result.append(entered);
        result.append(", left: ");
        result.append(left);
        result.append(')');
        return result.toString();
    }
}
