package hub.sam.lang.mf2b.impl;

import hub.metrik.lang.eprovide.debuggingstate.impl.MValueImpl;
import hub.sam.lang.mf2b.Mf2bPackage;
import hub.sam.lang.mf2b.Value;
import org.eclipse.emf.ecore.EClass;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Value</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public abstract class ValueImpl extends MValueImpl implements Value {

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
    protected ValueImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Mf2bPackage.Literals.VALUE;
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
    public String asString() {
        throw new UnsupportedOperationException();
    }
}
