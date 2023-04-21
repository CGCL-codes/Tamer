package eu.medeia.ecore.apmm.bm.impl;

import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import eu.medeia.ecore.apmm.bm.BehaviourImplementation;
import eu.medeia.ecore.apmm.bm.BmPackage;
import eu.medeia.ecore.apmm.bm.IBehaviourImplementationMethod;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Behaviour Implementation</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link eu.medeia.ecore.apmm.bm.impl.BehaviourImplementationImpl#getRevision <em>Revision</em>}</li>
 *   <li>{@link eu.medeia.ecore.apmm.bm.impl.BehaviourImplementationImpl#getOrganisation <em>Organisation</em>}</li>
 *   <li>{@link eu.medeia.ecore.apmm.bm.impl.BehaviourImplementationImpl#getAuthor <em>Author</em>}</li>
 *   <li>{@link eu.medeia.ecore.apmm.bm.impl.BehaviourImplementationImpl#getDate <em>Date</em>}</li>
 *   <li>{@link eu.medeia.ecore.apmm.bm.impl.BehaviourImplementationImpl#getImplementationMethods <em>Implementation Methods</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class BehaviourImplementationImpl extends INamedElementImpl implements BehaviourImplementation {

    /**
	 * The default value of the '{@link #getRevision() <em>Revision</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRevision()
	 * @generated
	 * @ordered
	 */
    protected static final String REVISION_EDEFAULT = "0";

    /**
	 * The cached value of the '{@link #getRevision() <em>Revision</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRevision()
	 * @generated
	 * @ordered
	 */
    protected String revision = REVISION_EDEFAULT;

    /**
	 * The default value of the '{@link #getOrganisation() <em>Organisation</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOrganisation()
	 * @generated
	 * @ordered
	 */
    protected static final String ORGANISATION_EDEFAULT = "organisation";

    /**
	 * The cached value of the '{@link #getOrganisation() <em>Organisation</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOrganisation()
	 * @generated
	 * @ordered
	 */
    protected String organisation = ORGANISATION_EDEFAULT;

    /**
	 * The default value of the '{@link #getAuthor() <em>Author</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAuthor()
	 * @generated
	 * @ordered
	 */
    protected static final String AUTHOR_EDEFAULT = "author";

    /**
	 * The cached value of the '{@link #getAuthor() <em>Author</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAuthor()
	 * @generated
	 * @ordered
	 */
    protected String author = AUTHOR_EDEFAULT;

    /**
	 * The default value of the '{@link #getDate() <em>Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDate()
	 * @generated
	 * @ordered
	 */
    protected static final String DATE_EDEFAULT = "";

    /**
	 * The cached value of the '{@link #getDate() <em>Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDate()
	 * @generated
	 * @ordered
	 */
    protected String date = DATE_EDEFAULT;

    /**
	 * The cached value of the '{@link #getImplementationMethods() <em>Implementation Methods</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getImplementationMethods()
	 * @generated
	 * @ordered
	 */
    protected EList<IBehaviourImplementationMethod> implementationMethods;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected BehaviourImplementationImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return BmPackage.Literals.BEHAVIOUR_IMPLEMENTATION;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getRevision() {
        return revision;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setRevision(String newRevision) {
        String oldRevision = revision;
        revision = newRevision;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, BmPackage.BEHAVIOUR_IMPLEMENTATION__REVISION, oldRevision, revision));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getOrganisation() {
        return organisation;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setOrganisation(String newOrganisation) {
        String oldOrganisation = organisation;
        organisation = newOrganisation;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, BmPackage.BEHAVIOUR_IMPLEMENTATION__ORGANISATION, oldOrganisation, organisation));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getAuthor() {
        return author;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setAuthor(String newAuthor) {
        String oldAuthor = author;
        author = newAuthor;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, BmPackage.BEHAVIOUR_IMPLEMENTATION__AUTHOR, oldAuthor, author));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getDate() {
        return date;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setDate(String newDate) {
        String oldDate = date;
        date = newDate;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, BmPackage.BEHAVIOUR_IMPLEMENTATION__DATE, oldDate, date));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<IBehaviourImplementationMethod> getImplementationMethods() {
        if (implementationMethods == null) {
            implementationMethods = new EObjectContainmentEList<IBehaviourImplementationMethod>(IBehaviourImplementationMethod.class, this, BmPackage.BEHAVIOUR_IMPLEMENTATION__IMPLEMENTATION_METHODS);
        }
        return implementationMethods;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case BmPackage.BEHAVIOUR_IMPLEMENTATION__IMPLEMENTATION_METHODS:
                return ((InternalEList<?>) getImplementationMethods()).basicRemove(otherEnd, msgs);
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
            case BmPackage.BEHAVIOUR_IMPLEMENTATION__REVISION:
                return getRevision();
            case BmPackage.BEHAVIOUR_IMPLEMENTATION__ORGANISATION:
                return getOrganisation();
            case BmPackage.BEHAVIOUR_IMPLEMENTATION__AUTHOR:
                return getAuthor();
            case BmPackage.BEHAVIOUR_IMPLEMENTATION__DATE:
                return getDate();
            case BmPackage.BEHAVIOUR_IMPLEMENTATION__IMPLEMENTATION_METHODS:
                return getImplementationMethods();
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
            case BmPackage.BEHAVIOUR_IMPLEMENTATION__REVISION:
                setRevision((String) newValue);
                return;
            case BmPackage.BEHAVIOUR_IMPLEMENTATION__ORGANISATION:
                setOrganisation((String) newValue);
                return;
            case BmPackage.BEHAVIOUR_IMPLEMENTATION__AUTHOR:
                setAuthor((String) newValue);
                return;
            case BmPackage.BEHAVIOUR_IMPLEMENTATION__DATE:
                setDate((String) newValue);
                return;
            case BmPackage.BEHAVIOUR_IMPLEMENTATION__IMPLEMENTATION_METHODS:
                getImplementationMethods().clear();
                getImplementationMethods().addAll((Collection<? extends IBehaviourImplementationMethod>) newValue);
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
            case BmPackage.BEHAVIOUR_IMPLEMENTATION__REVISION:
                setRevision(REVISION_EDEFAULT);
                return;
            case BmPackage.BEHAVIOUR_IMPLEMENTATION__ORGANISATION:
                setOrganisation(ORGANISATION_EDEFAULT);
                return;
            case BmPackage.BEHAVIOUR_IMPLEMENTATION__AUTHOR:
                setAuthor(AUTHOR_EDEFAULT);
                return;
            case BmPackage.BEHAVIOUR_IMPLEMENTATION__DATE:
                setDate(DATE_EDEFAULT);
                return;
            case BmPackage.BEHAVIOUR_IMPLEMENTATION__IMPLEMENTATION_METHODS:
                getImplementationMethods().clear();
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
            case BmPackage.BEHAVIOUR_IMPLEMENTATION__REVISION:
                return REVISION_EDEFAULT == null ? revision != null : !REVISION_EDEFAULT.equals(revision);
            case BmPackage.BEHAVIOUR_IMPLEMENTATION__ORGANISATION:
                return ORGANISATION_EDEFAULT == null ? organisation != null : !ORGANISATION_EDEFAULT.equals(organisation);
            case BmPackage.BEHAVIOUR_IMPLEMENTATION__AUTHOR:
                return AUTHOR_EDEFAULT == null ? author != null : !AUTHOR_EDEFAULT.equals(author);
            case BmPackage.BEHAVIOUR_IMPLEMENTATION__DATE:
                return DATE_EDEFAULT == null ? date != null : !DATE_EDEFAULT.equals(date);
            case BmPackage.BEHAVIOUR_IMPLEMENTATION__IMPLEMENTATION_METHODS:
                return implementationMethods != null && !implementationMethods.isEmpty();
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
        result.append(" (revision: ");
        result.append(revision);
        result.append(", organisation: ");
        result.append(organisation);
        result.append(", author: ");
        result.append(author);
        result.append(", date: ");
        result.append(date);
        result.append(')');
        return result.toString();
    }
}
