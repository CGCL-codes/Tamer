package org.hl7.v3.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.hl7.v3.IVLPQ;
import org.hl7.v3.IVXBPQ;
import org.hl7.v3.PQ;
import org.hl7.v3.V3Package;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>IVLPQ</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.hl7.v3.impl.IVLPQImpl#getLow <em>Low</em>}</li>
 *   <li>{@link org.hl7.v3.impl.IVLPQImpl#getWidth <em>Width</em>}</li>
 *   <li>{@link org.hl7.v3.impl.IVLPQImpl#getHigh <em>High</em>}</li>
 *   <li>{@link org.hl7.v3.impl.IVLPQImpl#getHigh1 <em>High1</em>}</li>
 *   <li>{@link org.hl7.v3.impl.IVLPQImpl#getWidth1 <em>Width1</em>}</li>
 *   <li>{@link org.hl7.v3.impl.IVLPQImpl#getHigh2 <em>High2</em>}</li>
 *   <li>{@link org.hl7.v3.impl.IVLPQImpl#getCenter <em>Center</em>}</li>
 *   <li>{@link org.hl7.v3.impl.IVLPQImpl#getWidth2 <em>Width2</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class IVLPQImpl extends SXCMPQImpl implements IVLPQ {

    /**
	 * The cached value of the '{@link #getLow() <em>Low</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLow()
	 * @generated
	 * @ordered
	 */
    protected IVXBPQ low;

    /**
	 * The cached value of the '{@link #getWidth() <em>Width</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWidth()
	 * @generated
	 * @ordered
	 */
    protected PQ width;

    /**
	 * The cached value of the '{@link #getHigh() <em>High</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHigh()
	 * @generated
	 * @ordered
	 */
    protected IVXBPQ high;

    /**
	 * The cached value of the '{@link #getHigh1() <em>High1</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHigh1()
	 * @generated
	 * @ordered
	 */
    protected IVXBPQ high1;

    /**
	 * The cached value of the '{@link #getWidth1() <em>Width1</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWidth1()
	 * @generated
	 * @ordered
	 */
    protected PQ width1;

    /**
	 * The cached value of the '{@link #getHigh2() <em>High2</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHigh2()
	 * @generated
	 * @ordered
	 */
    protected IVXBPQ high2;

    /**
	 * The cached value of the '{@link #getCenter() <em>Center</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCenter()
	 * @generated
	 * @ordered
	 */
    protected PQ center;

    /**
	 * The cached value of the '{@link #getWidth2() <em>Width2</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWidth2()
	 * @generated
	 * @ordered
	 */
    protected PQ width2;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected IVLPQImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return V3Package.eINSTANCE.getIVLPQ();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public IVXBPQ getLow() {
        return low;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetLow(IVXBPQ newLow, NotificationChain msgs) {
        IVXBPQ oldLow = low;
        low = newLow;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, V3Package.IVLPQ__LOW, oldLow, newLow);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setLow(IVXBPQ newLow) {
        if (newLow != low) {
            NotificationChain msgs = null;
            if (low != null) msgs = ((InternalEObject) low).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - V3Package.IVLPQ__LOW, null, msgs);
            if (newLow != null) msgs = ((InternalEObject) newLow).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - V3Package.IVLPQ__LOW, null, msgs);
            msgs = basicSetLow(newLow, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, V3Package.IVLPQ__LOW, newLow, newLow));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public PQ getWidth() {
        return width;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetWidth(PQ newWidth, NotificationChain msgs) {
        PQ oldWidth = width;
        width = newWidth;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, V3Package.IVLPQ__WIDTH, oldWidth, newWidth);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setWidth(PQ newWidth) {
        if (newWidth != width) {
            NotificationChain msgs = null;
            if (width != null) msgs = ((InternalEObject) width).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - V3Package.IVLPQ__WIDTH, null, msgs);
            if (newWidth != null) msgs = ((InternalEObject) newWidth).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - V3Package.IVLPQ__WIDTH, null, msgs);
            msgs = basicSetWidth(newWidth, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, V3Package.IVLPQ__WIDTH, newWidth, newWidth));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public IVXBPQ getHigh() {
        return high;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetHigh(IVXBPQ newHigh, NotificationChain msgs) {
        IVXBPQ oldHigh = high;
        high = newHigh;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, V3Package.IVLPQ__HIGH, oldHigh, newHigh);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setHigh(IVXBPQ newHigh) {
        if (newHigh != high) {
            NotificationChain msgs = null;
            if (high != null) msgs = ((InternalEObject) high).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - V3Package.IVLPQ__HIGH, null, msgs);
            if (newHigh != null) msgs = ((InternalEObject) newHigh).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - V3Package.IVLPQ__HIGH, null, msgs);
            msgs = basicSetHigh(newHigh, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, V3Package.IVLPQ__HIGH, newHigh, newHigh));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public IVXBPQ getHigh1() {
        return high1;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetHigh1(IVXBPQ newHigh1, NotificationChain msgs) {
        IVXBPQ oldHigh1 = high1;
        high1 = newHigh1;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, V3Package.IVLPQ__HIGH1, oldHigh1, newHigh1);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setHigh1(IVXBPQ newHigh1) {
        if (newHigh1 != high1) {
            NotificationChain msgs = null;
            if (high1 != null) msgs = ((InternalEObject) high1).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - V3Package.IVLPQ__HIGH1, null, msgs);
            if (newHigh1 != null) msgs = ((InternalEObject) newHigh1).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - V3Package.IVLPQ__HIGH1, null, msgs);
            msgs = basicSetHigh1(newHigh1, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, V3Package.IVLPQ__HIGH1, newHigh1, newHigh1));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public PQ getWidth1() {
        return width1;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetWidth1(PQ newWidth1, NotificationChain msgs) {
        PQ oldWidth1 = width1;
        width1 = newWidth1;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, V3Package.IVLPQ__WIDTH1, oldWidth1, newWidth1);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setWidth1(PQ newWidth1) {
        if (newWidth1 != width1) {
            NotificationChain msgs = null;
            if (width1 != null) msgs = ((InternalEObject) width1).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - V3Package.IVLPQ__WIDTH1, null, msgs);
            if (newWidth1 != null) msgs = ((InternalEObject) newWidth1).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - V3Package.IVLPQ__WIDTH1, null, msgs);
            msgs = basicSetWidth1(newWidth1, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, V3Package.IVLPQ__WIDTH1, newWidth1, newWidth1));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public IVXBPQ getHigh2() {
        return high2;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetHigh2(IVXBPQ newHigh2, NotificationChain msgs) {
        IVXBPQ oldHigh2 = high2;
        high2 = newHigh2;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, V3Package.IVLPQ__HIGH2, oldHigh2, newHigh2);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setHigh2(IVXBPQ newHigh2) {
        if (newHigh2 != high2) {
            NotificationChain msgs = null;
            if (high2 != null) msgs = ((InternalEObject) high2).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - V3Package.IVLPQ__HIGH2, null, msgs);
            if (newHigh2 != null) msgs = ((InternalEObject) newHigh2).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - V3Package.IVLPQ__HIGH2, null, msgs);
            msgs = basicSetHigh2(newHigh2, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, V3Package.IVLPQ__HIGH2, newHigh2, newHigh2));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public PQ getCenter() {
        return center;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetCenter(PQ newCenter, NotificationChain msgs) {
        PQ oldCenter = center;
        center = newCenter;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, V3Package.IVLPQ__CENTER, oldCenter, newCenter);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setCenter(PQ newCenter) {
        if (newCenter != center) {
            NotificationChain msgs = null;
            if (center != null) msgs = ((InternalEObject) center).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - V3Package.IVLPQ__CENTER, null, msgs);
            if (newCenter != null) msgs = ((InternalEObject) newCenter).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - V3Package.IVLPQ__CENTER, null, msgs);
            msgs = basicSetCenter(newCenter, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, V3Package.IVLPQ__CENTER, newCenter, newCenter));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public PQ getWidth2() {
        return width2;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetWidth2(PQ newWidth2, NotificationChain msgs) {
        PQ oldWidth2 = width2;
        width2 = newWidth2;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, V3Package.IVLPQ__WIDTH2, oldWidth2, newWidth2);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setWidth2(PQ newWidth2) {
        if (newWidth2 != width2) {
            NotificationChain msgs = null;
            if (width2 != null) msgs = ((InternalEObject) width2).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - V3Package.IVLPQ__WIDTH2, null, msgs);
            if (newWidth2 != null) msgs = ((InternalEObject) newWidth2).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - V3Package.IVLPQ__WIDTH2, null, msgs);
            msgs = basicSetWidth2(newWidth2, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, V3Package.IVLPQ__WIDTH2, newWidth2, newWidth2));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case V3Package.IVLPQ__LOW:
                return basicSetLow(null, msgs);
            case V3Package.IVLPQ__WIDTH:
                return basicSetWidth(null, msgs);
            case V3Package.IVLPQ__HIGH:
                return basicSetHigh(null, msgs);
            case V3Package.IVLPQ__HIGH1:
                return basicSetHigh1(null, msgs);
            case V3Package.IVLPQ__WIDTH1:
                return basicSetWidth1(null, msgs);
            case V3Package.IVLPQ__HIGH2:
                return basicSetHigh2(null, msgs);
            case V3Package.IVLPQ__CENTER:
                return basicSetCenter(null, msgs);
            case V3Package.IVLPQ__WIDTH2:
                return basicSetWidth2(null, msgs);
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
            case V3Package.IVLPQ__LOW:
                return getLow();
            case V3Package.IVLPQ__WIDTH:
                return getWidth();
            case V3Package.IVLPQ__HIGH:
                return getHigh();
            case V3Package.IVLPQ__HIGH1:
                return getHigh1();
            case V3Package.IVLPQ__WIDTH1:
                return getWidth1();
            case V3Package.IVLPQ__HIGH2:
                return getHigh2();
            case V3Package.IVLPQ__CENTER:
                return getCenter();
            case V3Package.IVLPQ__WIDTH2:
                return getWidth2();
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
            case V3Package.IVLPQ__LOW:
                setLow((IVXBPQ) newValue);
                return;
            case V3Package.IVLPQ__WIDTH:
                setWidth((PQ) newValue);
                return;
            case V3Package.IVLPQ__HIGH:
                setHigh((IVXBPQ) newValue);
                return;
            case V3Package.IVLPQ__HIGH1:
                setHigh1((IVXBPQ) newValue);
                return;
            case V3Package.IVLPQ__WIDTH1:
                setWidth1((PQ) newValue);
                return;
            case V3Package.IVLPQ__HIGH2:
                setHigh2((IVXBPQ) newValue);
                return;
            case V3Package.IVLPQ__CENTER:
                setCenter((PQ) newValue);
                return;
            case V3Package.IVLPQ__WIDTH2:
                setWidth2((PQ) newValue);
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
            case V3Package.IVLPQ__LOW:
                setLow((IVXBPQ) null);
                return;
            case V3Package.IVLPQ__WIDTH:
                setWidth((PQ) null);
                return;
            case V3Package.IVLPQ__HIGH:
                setHigh((IVXBPQ) null);
                return;
            case V3Package.IVLPQ__HIGH1:
                setHigh1((IVXBPQ) null);
                return;
            case V3Package.IVLPQ__WIDTH1:
                setWidth1((PQ) null);
                return;
            case V3Package.IVLPQ__HIGH2:
                setHigh2((IVXBPQ) null);
                return;
            case V3Package.IVLPQ__CENTER:
                setCenter((PQ) null);
                return;
            case V3Package.IVLPQ__WIDTH2:
                setWidth2((PQ) null);
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
            case V3Package.IVLPQ__LOW:
                return low != null;
            case V3Package.IVLPQ__WIDTH:
                return width != null;
            case V3Package.IVLPQ__HIGH:
                return high != null;
            case V3Package.IVLPQ__HIGH1:
                return high1 != null;
            case V3Package.IVLPQ__WIDTH1:
                return width1 != null;
            case V3Package.IVLPQ__HIGH2:
                return high2 != null;
            case V3Package.IVLPQ__CENTER:
                return center != null;
            case V3Package.IVLPQ__WIDTH2:
                return width2 != null;
        }
        return super.eIsSet(featureID);
    }
}
