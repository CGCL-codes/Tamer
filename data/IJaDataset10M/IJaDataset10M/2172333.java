package odm.impl;

import odm.DataComplementOf;
import odm.DataRange;
import odm.OdmPackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Data Complement Of</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link odm.impl.DataComplementOfImpl#getDataComplementOfLabel <em>Data Complement Of Label</em>}</li>
 *   <li>{@link odm.impl.DataComplementOfImpl#getDatarange <em>Datarange</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DataComplementOfImpl extends DataRangeImpl implements DataComplementOf {

    /**
	 * The default value of the '{@link #getDataComplementOfLabel() <em>Data Complement Of Label</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDataComplementOfLabel()
	 * @generated
	 * @ordered
	 */
    protected static final String DATA_COMPLEMENT_OF_LABEL_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getDataComplementOfLabel() <em>Data Complement Of Label</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDataComplementOfLabel()
	 * @generated
	 * @ordered
	 */
    protected String dataComplementOfLabel = DATA_COMPLEMENT_OF_LABEL_EDEFAULT;

    /**
	 * The cached value of the '{@link #getDatarange() <em>Datarange</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDatarange()
	 * @generated
	 * @ordered
	 */
    protected DataRange datarange = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected DataComplementOfImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected EClass eStaticClass() {
        return OdmPackage.Literals.DATA_COMPLEMENT_OF;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getDataComplementOfLabel() {
        return dataComplementOfLabel;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setDataComplementOfLabel(String newDataComplementOfLabel) {
        String oldDataComplementOfLabel = dataComplementOfLabel;
        dataComplementOfLabel = newDataComplementOfLabel;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, OdmPackage.DATA_COMPLEMENT_OF__DATA_COMPLEMENT_OF_LABEL, oldDataComplementOfLabel, dataComplementOfLabel));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public DataRange getDatarange() {
        if (datarange != null && datarange.eIsProxy()) {
            InternalEObject oldDatarange = (InternalEObject) datarange;
            datarange = (DataRange) eResolveProxy(oldDatarange);
            if (datarange != oldDatarange) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, OdmPackage.DATA_COMPLEMENT_OF__DATARANGE, oldDatarange, datarange));
            }
        }
        return datarange;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public DataRange basicGetDatarange() {
        return datarange;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setDatarange(DataRange newDatarange) {
        DataRange oldDatarange = datarange;
        datarange = newDatarange;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, OdmPackage.DATA_COMPLEMENT_OF__DATARANGE, oldDatarange, datarange));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case OdmPackage.DATA_COMPLEMENT_OF__DATA_COMPLEMENT_OF_LABEL:
                return getDataComplementOfLabel();
            case OdmPackage.DATA_COMPLEMENT_OF__DATARANGE:
                if (resolve) return getDatarange();
                return basicGetDatarange();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void eSet(int featureID, Object newValue) {
        switch(featureID) {
            case OdmPackage.DATA_COMPLEMENT_OF__DATA_COMPLEMENT_OF_LABEL:
                setDataComplementOfLabel((String) newValue);
                return;
            case OdmPackage.DATA_COMPLEMENT_OF__DATARANGE:
                setDatarange((DataRange) newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void eUnset(int featureID) {
        switch(featureID) {
            case OdmPackage.DATA_COMPLEMENT_OF__DATA_COMPLEMENT_OF_LABEL:
                setDataComplementOfLabel(DATA_COMPLEMENT_OF_LABEL_EDEFAULT);
                return;
            case OdmPackage.DATA_COMPLEMENT_OF__DATARANGE:
                setDatarange((DataRange) null);
                return;
        }
        super.eUnset(featureID);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean eIsSet(int featureID) {
        switch(featureID) {
            case OdmPackage.DATA_COMPLEMENT_OF__DATA_COMPLEMENT_OF_LABEL:
                return DATA_COMPLEMENT_OF_LABEL_EDEFAULT == null ? dataComplementOfLabel != null : !DATA_COMPLEMENT_OF_LABEL_EDEFAULT.equals(dataComplementOfLabel);
            case OdmPackage.DATA_COMPLEMENT_OF__DATARANGE:
                return datarange != null;
        }
        return super.eIsSet(featureID);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String toString() {
        if (eIsProxy()) return super.toString();
        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (dataComplementOfLabel: ");
        result.append(dataComplementOfLabel);
        result.append(')');
        return result.toString();
    }
}
