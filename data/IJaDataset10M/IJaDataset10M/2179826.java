package urban.urban.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import urban.urban.Link;
import urban.urban.Mrk;
import urban.urban.Site;
import urban.urban.UrbanPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Site</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link urban.urban.impl.SiteImpl#getId <em>Id</em>}</li>
 *   <li>{@link urban.urban.impl.SiteImpl#getState <em>State</em>}</li>
 *   <li>{@link urban.urban.impl.SiteImpl#getLink <em>Link</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SiteImpl extends MinimalEObjectImpl.Container implements Site {

    /**
   * The default value of the '{@link #getId() <em>Id</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getId()
   * @generated
   * @ordered
   */
    protected static final String ID_EDEFAULT = null;

    /**
   * The cached value of the '{@link #getId() <em>Id</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getId()
   * @generated
   * @ordered
   */
    protected String id = ID_EDEFAULT;

    /**
   * The cached value of the '{@link #getState() <em>State</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getState()
   * @generated
   * @ordered
   */
    protected Mrk state;

    /**
   * The cached value of the '{@link #getLink() <em>Link</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLink()
   * @generated
   * @ordered
   */
    protected Link link;

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    protected SiteImpl() {
        super();
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    protected EClass eStaticClass() {
        return UrbanPackage.Literals.SITE;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public String getId() {
        return id;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setId(String newId) {
        String oldId = id;
        id = newId;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, UrbanPackage.SITE__ID, oldId, id));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public Mrk getState() {
        return state;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public NotificationChain basicSetState(Mrk newState, NotificationChain msgs) {
        Mrk oldState = state;
        state = newState;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, UrbanPackage.SITE__STATE, oldState, newState);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setState(Mrk newState) {
        if (newState != state) {
            NotificationChain msgs = null;
            if (state != null) msgs = ((InternalEObject) state).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - UrbanPackage.SITE__STATE, null, msgs);
            if (newState != null) msgs = ((InternalEObject) newState).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - UrbanPackage.SITE__STATE, null, msgs);
            msgs = basicSetState(newState, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, UrbanPackage.SITE__STATE, newState, newState));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public Link getLink() {
        return link;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public NotificationChain basicSetLink(Link newLink, NotificationChain msgs) {
        Link oldLink = link;
        link = newLink;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, UrbanPackage.SITE__LINK, oldLink, newLink);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setLink(Link newLink) {
        if (newLink != link) {
            NotificationChain msgs = null;
            if (link != null) msgs = ((InternalEObject) link).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - UrbanPackage.SITE__LINK, null, msgs);
            if (newLink != null) msgs = ((InternalEObject) newLink).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - UrbanPackage.SITE__LINK, null, msgs);
            msgs = basicSetLink(newLink, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, UrbanPackage.SITE__LINK, newLink, newLink));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case UrbanPackage.SITE__STATE:
                return basicSetState(null, msgs);
            case UrbanPackage.SITE__LINK:
                return basicSetLink(null, msgs);
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
            case UrbanPackage.SITE__ID:
                return getId();
            case UrbanPackage.SITE__STATE:
                return getState();
            case UrbanPackage.SITE__LINK:
                return getLink();
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
            case UrbanPackage.SITE__ID:
                setId((String) newValue);
                return;
            case UrbanPackage.SITE__STATE:
                setState((Mrk) newValue);
                return;
            case UrbanPackage.SITE__LINK:
                setLink((Link) newValue);
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
            case UrbanPackage.SITE__ID:
                setId(ID_EDEFAULT);
                return;
            case UrbanPackage.SITE__STATE:
                setState((Mrk) null);
                return;
            case UrbanPackage.SITE__LINK:
                setLink((Link) null);
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
            case UrbanPackage.SITE__ID:
                return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
            case UrbanPackage.SITE__STATE:
                return state != null;
            case UrbanPackage.SITE__LINK:
                return link != null;
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
        result.append(" (id: ");
        result.append(id);
        result.append(')');
        return result.toString();
    }
}
