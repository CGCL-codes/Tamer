package org.enml.net.impl;

import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.enml.net.Line;
import org.enml.net.NetPackage;
import org.enml.net.Point;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Line</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.enml.net.impl.LineImpl#isClosed <em>Closed</em>}</li>
 *   <li>{@link org.enml.net.impl.LineImpl#getPoints <em>Points</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class LineImpl extends EObjectImpl implements Line {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public static final String copyright = "enml.org (C) 2007";

    /**
	 * The default value of the '{@link #isClosed() <em>Closed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isClosed()
	 * @generated
	 * @ordered
	 */
    protected static final boolean CLOSED_EDEFAULT = false;

    /**
	 * The cached value of the '{@link #isClosed() <em>Closed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isClosed()
	 * @generated
	 * @ordered
	 */
    protected boolean closed = CLOSED_EDEFAULT;

    /**
	 * The cached value of the '{@link #getPoints() <em>Points</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPoints()
	 * @generated
	 * @ordered
	 */
    protected EList<Point> points;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected LineImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return NetPackage.Literals.LINE;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean isClosed() {
        return closed;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setClosed(boolean newClosed) {
        boolean oldClosed = closed;
        closed = newClosed;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, NetPackage.LINE__CLOSED, oldClosed, closed));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<Point> getPoints() {
        if (points == null) {
            points = new EObjectContainmentEList<Point>(Point.class, this, NetPackage.LINE__POINTS);
        }
        return points;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case NetPackage.LINE__POINTS:
                return ((InternalEList<?>) getPoints()).basicRemove(otherEnd, msgs);
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
            case NetPackage.LINE__CLOSED:
                return isClosed();
            case NetPackage.LINE__POINTS:
                return getPoints();
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
            case NetPackage.LINE__CLOSED:
                setClosed((Boolean) newValue);
                return;
            case NetPackage.LINE__POINTS:
                getPoints().clear();
                getPoints().addAll((Collection<? extends Point>) newValue);
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
            case NetPackage.LINE__CLOSED:
                setClosed(CLOSED_EDEFAULT);
                return;
            case NetPackage.LINE__POINTS:
                getPoints().clear();
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
            case NetPackage.LINE__CLOSED:
                return closed != CLOSED_EDEFAULT;
            case NetPackage.LINE__POINTS:
                return points != null && !points.isEmpty();
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
        result.append(" (closed: ");
        result.append(closed);
        result.append(')');
        return result.toString();
    }
}
