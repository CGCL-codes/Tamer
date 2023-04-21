package fi.jab.esb.config.xml.impl;

import fi.jab.esb.config.xml.FtpBusType;
import fi.jab.esb.config.xml.FtpMessageFilterType;
import fi.jab.esb.config.xml.XmlPackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Ftp Bus Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link fi.jab.esb.config.xml.impl.FtpBusTypeImpl#getFtpMessageFilter <em>Ftp Message Filter</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class FtpBusTypeImpl extends BusImpl implements FtpBusType {

    /**
   * The cached value of the '{@link #getFtpMessageFilter() <em>Ftp Message Filter</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getFtpMessageFilter()
   * @generated
   * @ordered
   */
    protected FtpMessageFilterType ftpMessageFilter;

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    protected FtpBusTypeImpl() {
        super();
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    protected EClass eStaticClass() {
        return XmlPackage.Literals.FTP_BUS_TYPE;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public FtpMessageFilterType getFtpMessageFilter() {
        return ftpMessageFilter;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public NotificationChain basicSetFtpMessageFilter(FtpMessageFilterType newFtpMessageFilter, NotificationChain msgs) {
        FtpMessageFilterType oldFtpMessageFilter = ftpMessageFilter;
        ftpMessageFilter = newFtpMessageFilter;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, XmlPackage.FTP_BUS_TYPE__FTP_MESSAGE_FILTER, oldFtpMessageFilter, newFtpMessageFilter);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setFtpMessageFilter(FtpMessageFilterType newFtpMessageFilter) {
        if (newFtpMessageFilter != ftpMessageFilter) {
            NotificationChain msgs = null;
            if (ftpMessageFilter != null) msgs = ((InternalEObject) ftpMessageFilter).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - XmlPackage.FTP_BUS_TYPE__FTP_MESSAGE_FILTER, null, msgs);
            if (newFtpMessageFilter != null) msgs = ((InternalEObject) newFtpMessageFilter).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - XmlPackage.FTP_BUS_TYPE__FTP_MESSAGE_FILTER, null, msgs);
            msgs = basicSetFtpMessageFilter(newFtpMessageFilter, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, XmlPackage.FTP_BUS_TYPE__FTP_MESSAGE_FILTER, newFtpMessageFilter, newFtpMessageFilter));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case XmlPackage.FTP_BUS_TYPE__FTP_MESSAGE_FILTER:
                return basicSetFtpMessageFilter(null, msgs);
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
            case XmlPackage.FTP_BUS_TYPE__FTP_MESSAGE_FILTER:
                return getFtpMessageFilter();
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
            case XmlPackage.FTP_BUS_TYPE__FTP_MESSAGE_FILTER:
                setFtpMessageFilter((FtpMessageFilterType) newValue);
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
            case XmlPackage.FTP_BUS_TYPE__FTP_MESSAGE_FILTER:
                setFtpMessageFilter((FtpMessageFilterType) null);
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
            case XmlPackage.FTP_BUS_TYPE__FTP_MESSAGE_FILTER:
                return ftpMessageFilter != null;
        }
        return super.eIsSet(featureID);
    }
}
