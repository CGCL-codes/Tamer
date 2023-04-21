package edu.asu.vogon.embryo.embryomodel.impl;

import edu.asu.vogon.embryo.embryomodel.EmbryomodelPackage;
import edu.asu.vogon.embryo.embryomodel.HighlightedString;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Highlighted String</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link edu.asu.vogon.embryo.embryomodel.impl.HighlightedStringImpl#getHighlightedStr <em>Highlighted Str</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class HighlightedStringImpl extends StringMarkupImpl implements HighlightedString {

    /**
	 * The default value of the '{@link #getHighlightedStr() <em>Highlighted Str</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHighlightedStr()
	 * @generated
	 * @ordered
	 */
    protected static final String HIGHLIGHTED_STR_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getHighlightedStr() <em>Highlighted Str</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHighlightedStr()
	 * @generated
	 * @ordered
	 */
    protected String highlightedStr = HIGHLIGHTED_STR_EDEFAULT;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected HighlightedStringImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return EmbryomodelPackage.Literals.HIGHLIGHTED_STRING;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getHighlightedStr() {
        return highlightedStr;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setHighlightedStr(String newHighlightedStr) {
        String oldHighlightedStr = highlightedStr;
        highlightedStr = newHighlightedStr;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, EmbryomodelPackage.HIGHLIGHTED_STRING__HIGHLIGHTED_STR, oldHighlightedStr, highlightedStr));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case EmbryomodelPackage.HIGHLIGHTED_STRING__HIGHLIGHTED_STR:
                return getHighlightedStr();
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
            case EmbryomodelPackage.HIGHLIGHTED_STRING__HIGHLIGHTED_STR:
                setHighlightedStr((String) newValue);
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
            case EmbryomodelPackage.HIGHLIGHTED_STRING__HIGHLIGHTED_STR:
                setHighlightedStr(HIGHLIGHTED_STR_EDEFAULT);
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
            case EmbryomodelPackage.HIGHLIGHTED_STRING__HIGHLIGHTED_STR:
                return HIGHLIGHTED_STR_EDEFAULT == null ? highlightedStr != null : !HIGHLIGHTED_STR_EDEFAULT.equals(highlightedStr);
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
        result.append(" (highlightedStr: ");
        result.append(highlightedStr);
        result.append(')');
        return result.toString();
    }
}
