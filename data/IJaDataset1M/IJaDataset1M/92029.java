package org.emftext.language.owl.impl;

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
import org.emftext.language.owl.Namespace;
import org.emftext.language.owl.Ontology;
import org.emftext.language.owl.OntologyDocument;
import org.emftext.language.owl.OwlPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Ontology Document</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.emftext.language.owl.impl.OntologyDocumentImpl#getOntology <em>Ontology</em>}</li>
 *   <li>{@link org.emftext.language.owl.impl.OntologyDocumentImpl#getNamespace <em>Namespace</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class OntologyDocumentImpl extends EObjectImpl implements OntologyDocument {

    /**
	 * The cached value of the '{@link #getOntology() <em>Ontology</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOntology()
	 * @generated
	 * @ordered
	 */
    protected Ontology ontology;

    /**
	 * The cached value of the '{@link #getNamespace() <em>Namespace</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNamespace()
	 * @generated
	 * @ordered
	 */
    protected EList<Namespace> namespace;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected OntologyDocumentImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return OwlPackage.Literals.ONTOLOGY_DOCUMENT;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Ontology getOntology() {
        return ontology;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetOntology(Ontology newOntology, NotificationChain msgs) {
        Ontology oldOntology = ontology;
        ontology = newOntology;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, OwlPackage.ONTOLOGY_DOCUMENT__ONTOLOGY, oldOntology, newOntology);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setOntology(Ontology newOntology) {
        if (newOntology != ontology) {
            NotificationChain msgs = null;
            if (ontology != null) msgs = ((InternalEObject) ontology).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - OwlPackage.ONTOLOGY_DOCUMENT__ONTOLOGY, null, msgs);
            if (newOntology != null) msgs = ((InternalEObject) newOntology).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - OwlPackage.ONTOLOGY_DOCUMENT__ONTOLOGY, null, msgs);
            msgs = basicSetOntology(newOntology, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, OwlPackage.ONTOLOGY_DOCUMENT__ONTOLOGY, newOntology, newOntology));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<Namespace> getNamespace() {
        if (namespace == null) {
            namespace = new EObjectContainmentEList<Namespace>(Namespace.class, this, OwlPackage.ONTOLOGY_DOCUMENT__NAMESPACE);
        }
        return namespace;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case OwlPackage.ONTOLOGY_DOCUMENT__ONTOLOGY:
                return basicSetOntology(null, msgs);
            case OwlPackage.ONTOLOGY_DOCUMENT__NAMESPACE:
                return ((InternalEList<?>) getNamespace()).basicRemove(otherEnd, msgs);
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
            case OwlPackage.ONTOLOGY_DOCUMENT__ONTOLOGY:
                return getOntology();
            case OwlPackage.ONTOLOGY_DOCUMENT__NAMESPACE:
                return getNamespace();
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
            case OwlPackage.ONTOLOGY_DOCUMENT__ONTOLOGY:
                setOntology((Ontology) newValue);
                return;
            case OwlPackage.ONTOLOGY_DOCUMENT__NAMESPACE:
                getNamespace().clear();
                getNamespace().addAll((Collection<? extends Namespace>) newValue);
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
            case OwlPackage.ONTOLOGY_DOCUMENT__ONTOLOGY:
                setOntology((Ontology) null);
                return;
            case OwlPackage.ONTOLOGY_DOCUMENT__NAMESPACE:
                getNamespace().clear();
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
            case OwlPackage.ONTOLOGY_DOCUMENT__ONTOLOGY:
                return ontology != null;
            case OwlPackage.ONTOLOGY_DOCUMENT__NAMESPACE:
                return namespace != null && !namespace.isEmpty();
        }
        return super.eIsSet(featureID);
    }
}
