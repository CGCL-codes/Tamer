package fr.inria.papyrus.uml4tst.emftext.alf.impl;

import fr.inria.papyrus.uml4tst.emftext.alf.AlfPackage;
import fr.inria.papyrus.uml4tst.emftext.alf.Block;
import fr.inria.papyrus.uml4tst.emftext.alf.StatementSequence;
import org.eclipse.emf.common.notify.Notification;
import fr.inria.papyrus.uml4tst.emftext.alf.Statement;
import java.util.Collection;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Block</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link fr.inria.papyrus.uml4tst.emftext.alf.impl.BlockImpl#getStatementSequence <em>Statement Sequence</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class BlockImpl extends EObjectImpl implements Block {

    /**
	 * The cached value of the '{@link #getStatementSequence() <em>Statement Sequence</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStatementSequence()
	 * @generated
	 * @ordered
	 */
    protected StatementSequence statementSequence;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected BlockImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return AlfPackage.Literals.BLOCK;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public StatementSequence getStatementSequence() {
        return statementSequence;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetStatementSequence(StatementSequence newStatementSequence, NotificationChain msgs) {
        StatementSequence oldStatementSequence = statementSequence;
        statementSequence = newStatementSequence;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AlfPackage.BLOCK__STATEMENT_SEQUENCE, oldStatementSequence, newStatementSequence);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setStatementSequence(StatementSequence newStatementSequence) {
        if (newStatementSequence != statementSequence) {
            NotificationChain msgs = null;
            if (statementSequence != null) msgs = ((InternalEObject) statementSequence).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AlfPackage.BLOCK__STATEMENT_SEQUENCE, null, msgs);
            if (newStatementSequence != null) msgs = ((InternalEObject) newStatementSequence).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AlfPackage.BLOCK__STATEMENT_SEQUENCE, null, msgs);
            msgs = basicSetStatementSequence(newStatementSequence, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, AlfPackage.BLOCK__STATEMENT_SEQUENCE, newStatementSequence, newStatementSequence));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case AlfPackage.BLOCK__STATEMENT_SEQUENCE:
                return basicSetStatementSequence(null, msgs);
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
            case AlfPackage.BLOCK__STATEMENT_SEQUENCE:
                return getStatementSequence();
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
            case AlfPackage.BLOCK__STATEMENT_SEQUENCE:
                setStatementSequence((StatementSequence) newValue);
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
            case AlfPackage.BLOCK__STATEMENT_SEQUENCE:
                setStatementSequence((StatementSequence) null);
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
            case AlfPackage.BLOCK__STATEMENT_SEQUENCE:
                return statementSequence != null;
        }
        return super.eIsSet(featureID);
    }
}
