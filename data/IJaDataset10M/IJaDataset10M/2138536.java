package com.bap.ele.workbench.model.Course.impl;

import com.bap.ele.workbench.model.Course.COURSE;
import com.bap.ele.workbench.model.Course.COURSEstatus;
import com.bap.ele.workbench.model.Course.CoursePackage;
import com.bap.ele.workbench.model.Course.LANGUAGE;
import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>COURSE</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.bap.ele.workbench.model.Course.impl.COURSEImpl#getStatus <em>Status</em>}</li>
 *   <li>{@link com.bap.ele.workbench.model.Course.impl.COURSEImpl#getKeywords <em>Keywords</em>}</li>
 *   <li>{@link com.bap.ele.workbench.model.Course.impl.COURSEImpl#getLanguages <em>Languages</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class COURSEImpl extends COURSEobjectImpl implements COURSE {

    /**
	 * The default value of the '{@link #getStatus() <em>Status</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStatus()
	 * @generated
	 * @ordered
	 */
    protected static final COURSEstatus STATUS_EDEFAULT = COURSEstatus.NEW;

    /**
	 * The cached value of the '{@link #getStatus() <em>Status</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStatus()
	 * @generated
	 * @ordered
	 */
    protected COURSEstatus status = STATUS_EDEFAULT;

    /**
	 * The cached value of the '{@link #getKeywords() <em>Keywords</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKeywords()
	 * @generated
	 * @ordered
	 */
    protected EList<String> keywords;

    /**
	 * The cached value of the '{@link #getLanguages() <em>Languages</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLanguages()
	 * @generated
	 * @ordered
	 */
    protected EList<LANGUAGE> languages;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected COURSEImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return CoursePackage.Literals.COURSE;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public COURSEstatus getStatus() {
        return status;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setStatus(COURSEstatus newStatus) {
        COURSEstatus oldStatus = status;
        status = newStatus == null ? STATUS_EDEFAULT : newStatus;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, CoursePackage.COURSE__STATUS, oldStatus, status));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<String> getKeywords() {
        if (keywords == null) {
            keywords = new EDataTypeUniqueEList<String>(String.class, this, CoursePackage.COURSE__KEYWORDS);
        }
        return keywords;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<LANGUAGE> getLanguages() {
        if (languages == null) {
            languages = new EObjectResolvingEList<LANGUAGE>(LANGUAGE.class, this, CoursePackage.COURSE__LANGUAGES);
        }
        return languages;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case CoursePackage.COURSE__STATUS:
                return getStatus();
            case CoursePackage.COURSE__KEYWORDS:
                return getKeywords();
            case CoursePackage.COURSE__LANGUAGES:
                return getLanguages();
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
            case CoursePackage.COURSE__STATUS:
                setStatus((COURSEstatus) newValue);
                return;
            case CoursePackage.COURSE__KEYWORDS:
                getKeywords().clear();
                getKeywords().addAll((Collection<? extends String>) newValue);
                return;
            case CoursePackage.COURSE__LANGUAGES:
                getLanguages().clear();
                getLanguages().addAll((Collection<? extends LANGUAGE>) newValue);
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
            case CoursePackage.COURSE__STATUS:
                setStatus(STATUS_EDEFAULT);
                return;
            case CoursePackage.COURSE__KEYWORDS:
                getKeywords().clear();
                return;
            case CoursePackage.COURSE__LANGUAGES:
                getLanguages().clear();
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
            case CoursePackage.COURSE__STATUS:
                return status != STATUS_EDEFAULT;
            case CoursePackage.COURSE__KEYWORDS:
                return keywords != null && !keywords.isEmpty();
            case CoursePackage.COURSE__LANGUAGES:
                return languages != null && !languages.isEmpty();
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
        result.append(" (status: ");
        result.append(status);
        result.append(", keywords: ");
        result.append(keywords);
        result.append(')');
        return result.toString();
    }
}
