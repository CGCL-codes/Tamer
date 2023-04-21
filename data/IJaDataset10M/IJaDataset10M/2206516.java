package DeftDataModel.impl;

import DeftDataModel.Chapter;
import DeftDataModel.DeftDataModelPackage;
import DeftDataModel.Referable;
import DeftDataModel.Reference;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Reference</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link DeftDataModel.impl.ReferenceImpl#getChapter <em>Chapter</em>}</li>
 *   <li>{@link DeftDataModel.impl.ReferenceImpl#getReferable <em>Referable</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ReferenceImpl extends EObjectImpl implements Reference {

    /**
	 * The cached value of the '{@link #getChapter() <em>Chapter</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChapter()
	 * @generated
	 * @ordered
	 */
    protected Chapter chapter;

    /**
	 * The cached value of the '{@link #getReferable() <em>Referable</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReferable()
	 * @generated
	 * @ordered
	 */
    protected Referable referable;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected ReferenceImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return DeftDataModelPackage.Literals.REFERENCE;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Chapter getChapter() {
        if (chapter != null && chapter.eIsProxy()) {
            InternalEObject oldChapter = (InternalEObject) chapter;
            chapter = (Chapter) eResolveProxy(oldChapter);
            if (chapter != oldChapter) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, DeftDataModelPackage.REFERENCE__CHAPTER, oldChapter, chapter));
            }
        }
        return chapter;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Chapter basicGetChapter() {
        return chapter;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setChapter(Chapter newChapter) {
        Chapter oldChapter = chapter;
        chapter = newChapter;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, DeftDataModelPackage.REFERENCE__CHAPTER, oldChapter, chapter));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Referable getReferable() {
        if (referable != null && referable.eIsProxy()) {
            InternalEObject oldReferable = (InternalEObject) referable;
            referable = (Referable) eResolveProxy(oldReferable);
            if (referable != oldReferable) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, DeftDataModelPackage.REFERENCE__REFERABLE, oldReferable, referable));
            }
        }
        return referable;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Referable basicGetReferable() {
        return referable;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setReferable(Referable newReferable) {
        Referable oldReferable = referable;
        referable = newReferable;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, DeftDataModelPackage.REFERENCE__REFERABLE, oldReferable, referable));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case DeftDataModelPackage.REFERENCE__CHAPTER:
                if (resolve) return getChapter();
                return basicGetChapter();
            case DeftDataModelPackage.REFERENCE__REFERABLE:
                if (resolve) return getReferable();
                return basicGetReferable();
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
            case DeftDataModelPackage.REFERENCE__CHAPTER:
                setChapter((Chapter) newValue);
                return;
            case DeftDataModelPackage.REFERENCE__REFERABLE:
                setReferable((Referable) newValue);
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
            case DeftDataModelPackage.REFERENCE__CHAPTER:
                setChapter((Chapter) null);
                return;
            case DeftDataModelPackage.REFERENCE__REFERABLE:
                setReferable((Referable) null);
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
            case DeftDataModelPackage.REFERENCE__CHAPTER:
                return chapter != null;
            case DeftDataModelPackage.REFERENCE__REFERABLE:
                return referable != null;
        }
        return super.eIsSet(featureID);
    }
}
