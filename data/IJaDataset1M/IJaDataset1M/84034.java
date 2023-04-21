package net.sf.ubq.script.ubqt.impl;

import java.util.Collection;
import net.sf.ubq.script.ubqt.UbqBounds;
import net.sf.ubq.script.ubqt.UbqComposite;
import net.sf.ubq.script.ubqt.UbqEmmitedActions;
import net.sf.ubq.script.ubqt.UbqHandledReactions;
import net.sf.ubq.script.ubqt.UbqWidget;
import net.sf.ubq.script.ubqt.UbqWidgetStyle;
import net.sf.ubq.script.ubqt.UbqtPackage;
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
 * An implementation of the model object '<em><b>Ubq Widget</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.sf.ubq.script.ubqt.impl.UbqWidgetImpl#getParent <em>Parent</em>}</li>
 *   <li>{@link net.sf.ubq.script.ubqt.impl.UbqWidgetImpl#getBounds <em>Bounds</em>}</li>
 *   <li>{@link net.sf.ubq.script.ubqt.impl.UbqWidgetImpl#getAngle <em>Angle</em>}</li>
 *   <li>{@link net.sf.ubq.script.ubqt.impl.UbqWidgetImpl#getStyle <em>Style</em>}</li>
 *   <li>{@link net.sf.ubq.script.ubqt.impl.UbqWidgetImpl#getEmitted <em>Emitted</em>}</li>
 *   <li>{@link net.sf.ubq.script.ubqt.impl.UbqWidgetImpl#getReacted <em>Reacted</em>}</li>
 *   <li>{@link net.sf.ubq.script.ubqt.impl.UbqWidgetImpl#getName <em>Name</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class UbqWidgetImpl extends MinimalEObjectImpl.Container implements UbqWidget {

    /**
   * The cached value of the '{@link #getParent() <em>Parent</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getParent()
   * @generated
   * @ordered
   */
    protected UbqComposite parent;

    /**
   * The cached value of the '{@link #getBounds() <em>Bounds</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getBounds()
   * @generated
   * @ordered
   */
    protected UbqBounds bounds;

    /**
   * The default value of the '{@link #getAngle() <em>Angle</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getAngle()
   * @generated
   * @ordered
   */
    protected static final int ANGLE_EDEFAULT = 0;

    /**
   * The cached value of the '{@link #getAngle() <em>Angle</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getAngle()
   * @generated
   * @ordered
   */
    protected int angle = ANGLE_EDEFAULT;

    /**
   * The cached value of the '{@link #getStyle() <em>Style</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getStyle()
   * @generated
   * @ordered
   */
    protected UbqWidgetStyle style;

    /**
   * The cached value of the '{@link #getEmitted() <em>Emitted</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getEmitted()
   * @generated
   * @ordered
   */
    protected EList<UbqEmmitedActions> emitted;

    /**
   * The cached value of the '{@link #getReacted() <em>Reacted</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getReacted()
   * @generated
   * @ordered
   */
    protected UbqHandledReactions reacted;

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
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    protected UbqWidgetImpl() {
        super();
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    protected EClass eStaticClass() {
        return UbqtPackage.Literals.UBQ_WIDGET;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public UbqComposite getParent() {
        if (parent != null && parent.eIsProxy()) {
            InternalEObject oldParent = (InternalEObject) parent;
            parent = (UbqComposite) eResolveProxy(oldParent);
            if (parent != oldParent) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, UbqtPackage.UBQ_WIDGET__PARENT, oldParent, parent));
            }
        }
        return parent;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public UbqComposite basicGetParent() {
        return parent;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setParent(UbqComposite newParent) {
        UbqComposite oldParent = parent;
        parent = newParent;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, UbqtPackage.UBQ_WIDGET__PARENT, oldParent, parent));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public UbqBounds getBounds() {
        return bounds;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public NotificationChain basicSetBounds(UbqBounds newBounds, NotificationChain msgs) {
        UbqBounds oldBounds = bounds;
        bounds = newBounds;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, UbqtPackage.UBQ_WIDGET__BOUNDS, oldBounds, newBounds);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setBounds(UbqBounds newBounds) {
        if (newBounds != bounds) {
            NotificationChain msgs = null;
            if (bounds != null) msgs = ((InternalEObject) bounds).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - UbqtPackage.UBQ_WIDGET__BOUNDS, null, msgs);
            if (newBounds != null) msgs = ((InternalEObject) newBounds).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - UbqtPackage.UBQ_WIDGET__BOUNDS, null, msgs);
            msgs = basicSetBounds(newBounds, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, UbqtPackage.UBQ_WIDGET__BOUNDS, newBounds, newBounds));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public int getAngle() {
        return angle;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setAngle(int newAngle) {
        int oldAngle = angle;
        angle = newAngle;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, UbqtPackage.UBQ_WIDGET__ANGLE, oldAngle, angle));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public UbqWidgetStyle getStyle() {
        return style;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public NotificationChain basicSetStyle(UbqWidgetStyle newStyle, NotificationChain msgs) {
        UbqWidgetStyle oldStyle = style;
        style = newStyle;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, UbqtPackage.UBQ_WIDGET__STYLE, oldStyle, newStyle);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setStyle(UbqWidgetStyle newStyle) {
        if (newStyle != style) {
            NotificationChain msgs = null;
            if (style != null) msgs = ((InternalEObject) style).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - UbqtPackage.UBQ_WIDGET__STYLE, null, msgs);
            if (newStyle != null) msgs = ((InternalEObject) newStyle).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - UbqtPackage.UBQ_WIDGET__STYLE, null, msgs);
            msgs = basicSetStyle(newStyle, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, UbqtPackage.UBQ_WIDGET__STYLE, newStyle, newStyle));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public EList<UbqEmmitedActions> getEmitted() {
        if (emitted == null) {
            emitted = new EObjectContainmentEList<UbqEmmitedActions>(UbqEmmitedActions.class, this, UbqtPackage.UBQ_WIDGET__EMITTED);
        }
        return emitted;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public UbqHandledReactions getReacted() {
        return reacted;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public NotificationChain basicSetReacted(UbqHandledReactions newReacted, NotificationChain msgs) {
        UbqHandledReactions oldReacted = reacted;
        reacted = newReacted;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, UbqtPackage.UBQ_WIDGET__REACTED, oldReacted, newReacted);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setReacted(UbqHandledReactions newReacted) {
        if (newReacted != reacted) {
            NotificationChain msgs = null;
            if (reacted != null) msgs = ((InternalEObject) reacted).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - UbqtPackage.UBQ_WIDGET__REACTED, null, msgs);
            if (newReacted != null) msgs = ((InternalEObject) newReacted).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - UbqtPackage.UBQ_WIDGET__REACTED, null, msgs);
            msgs = basicSetReacted(newReacted, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, UbqtPackage.UBQ_WIDGET__REACTED, newReacted, newReacted));
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
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, UbqtPackage.UBQ_WIDGET__NAME, oldName, name));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case UbqtPackage.UBQ_WIDGET__BOUNDS:
                return basicSetBounds(null, msgs);
            case UbqtPackage.UBQ_WIDGET__STYLE:
                return basicSetStyle(null, msgs);
            case UbqtPackage.UBQ_WIDGET__EMITTED:
                return ((InternalEList<?>) getEmitted()).basicRemove(otherEnd, msgs);
            case UbqtPackage.UBQ_WIDGET__REACTED:
                return basicSetReacted(null, msgs);
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
            case UbqtPackage.UBQ_WIDGET__PARENT:
                if (resolve) return getParent();
                return basicGetParent();
            case UbqtPackage.UBQ_WIDGET__BOUNDS:
                return getBounds();
            case UbqtPackage.UBQ_WIDGET__ANGLE:
                return getAngle();
            case UbqtPackage.UBQ_WIDGET__STYLE:
                return getStyle();
            case UbqtPackage.UBQ_WIDGET__EMITTED:
                return getEmitted();
            case UbqtPackage.UBQ_WIDGET__REACTED:
                return getReacted();
            case UbqtPackage.UBQ_WIDGET__NAME:
                return getName();
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
            case UbqtPackage.UBQ_WIDGET__PARENT:
                setParent((UbqComposite) newValue);
                return;
            case UbqtPackage.UBQ_WIDGET__BOUNDS:
                setBounds((UbqBounds) newValue);
                return;
            case UbqtPackage.UBQ_WIDGET__ANGLE:
                setAngle((Integer) newValue);
                return;
            case UbqtPackage.UBQ_WIDGET__STYLE:
                setStyle((UbqWidgetStyle) newValue);
                return;
            case UbqtPackage.UBQ_WIDGET__EMITTED:
                getEmitted().clear();
                getEmitted().addAll((Collection<? extends UbqEmmitedActions>) newValue);
                return;
            case UbqtPackage.UBQ_WIDGET__REACTED:
                setReacted((UbqHandledReactions) newValue);
                return;
            case UbqtPackage.UBQ_WIDGET__NAME:
                setName((String) newValue);
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
            case UbqtPackage.UBQ_WIDGET__PARENT:
                setParent((UbqComposite) null);
                return;
            case UbqtPackage.UBQ_WIDGET__BOUNDS:
                setBounds((UbqBounds) null);
                return;
            case UbqtPackage.UBQ_WIDGET__ANGLE:
                setAngle(ANGLE_EDEFAULT);
                return;
            case UbqtPackage.UBQ_WIDGET__STYLE:
                setStyle((UbqWidgetStyle) null);
                return;
            case UbqtPackage.UBQ_WIDGET__EMITTED:
                getEmitted().clear();
                return;
            case UbqtPackage.UBQ_WIDGET__REACTED:
                setReacted((UbqHandledReactions) null);
                return;
            case UbqtPackage.UBQ_WIDGET__NAME:
                setName(NAME_EDEFAULT);
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
            case UbqtPackage.UBQ_WIDGET__PARENT:
                return parent != null;
            case UbqtPackage.UBQ_WIDGET__BOUNDS:
                return bounds != null;
            case UbqtPackage.UBQ_WIDGET__ANGLE:
                return angle != ANGLE_EDEFAULT;
            case UbqtPackage.UBQ_WIDGET__STYLE:
                return style != null;
            case UbqtPackage.UBQ_WIDGET__EMITTED:
                return emitted != null && !emitted.isEmpty();
            case UbqtPackage.UBQ_WIDGET__REACTED:
                return reacted != null;
            case UbqtPackage.UBQ_WIDGET__NAME:
                return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
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
        result.append(" (angle: ");
        result.append(angle);
        result.append(", name: ");
        result.append(name);
        result.append(')');
        return result.toString();
    }
}
