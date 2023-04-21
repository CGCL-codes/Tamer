package tefkat.model.impl;

import java.util.Collection;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.InternalEList;
import tefkat.model.CompoundTerm;
import tefkat.model.TefkatPackage;
import tefkat.model.Term;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Compound Term</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link tefkat.model.impl.CompoundTermImpl#getTerm <em>Term</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class CompoundTermImpl extends SourceTermImpl implements CompoundTerm {

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final String copyright = "Copyright michael lawley Pty Ltd 2003-2007";

    /**
     * The cached value of the '{@link #getTerm() <em>Term</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTerm()
     * @generated
     * @ordered
     */
    protected EList term = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected CompoundTermImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return TefkatPackage.Literals.COMPOUND_TERM;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList getTerm() {
        if (term == null) {
            term = new EObjectContainmentWithInverseEList(Term.class, this, TefkatPackage.COMPOUND_TERM__TERM, TefkatPackage.TERM__COMPOUND_TERM);
        }
        return term;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case TefkatPackage.COMPOUND_TERM__TERM:
                return ((InternalEList) getTerm()).basicAdd(otherEnd, msgs);
        }
        return super.eInverseAdd(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case TefkatPackage.COMPOUND_TERM__TERM:
                return ((InternalEList) getTerm()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case TefkatPackage.COMPOUND_TERM__TERM:
                return getTerm();
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
            case TefkatPackage.COMPOUND_TERM__TERM:
                getTerm().clear();
                getTerm().addAll((Collection) newValue);
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
            case TefkatPackage.COMPOUND_TERM__TERM:
                getTerm().clear();
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
            case TefkatPackage.COMPOUND_TERM__TERM:
                return term != null && !term.isEmpty();
        }
        return super.eIsSet(featureID);
    }
}
