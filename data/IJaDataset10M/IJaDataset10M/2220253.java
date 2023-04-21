package au.edu.archer.metadata.msf.mss.impl;

import org.eclipse.emf.ecore.EClass;
import au.edu.archer.metadata.msf.mss.Expression;
import au.edu.archer.metadata.msf.mss.MSSPackage;
import au.edu.archer.metadata.msf.mss.parser.ExpressionParserFactory;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Expression</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link au.edu.archer.metadata.msf.mss.impl.ExpressionImpl#getUnparsed <em>Unparsed</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class ExpressionImpl extends NodeImpl implements Expression {

    /**
     * The default value of the '{@link #getUnparsed() <em>Unparsed</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getUnparsed()
     * @generated
     * @ordered
     */
    protected static final String UNPARSED_EDEFAULT = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ExpressionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return MSSPackage.Literals.EXPRESSION;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    public final String getUnparsed() {
        return ExpressionParserFactory.instance().createUnparser().unparse(this);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case MSSPackage.EXPRESSION__UNPARSED:
                return getUnparsed();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch(featureID) {
            case MSSPackage.EXPRESSION__UNPARSED:
                return UNPARSED_EDEFAULT == null ? getUnparsed() != null : !UNPARSED_EDEFAULT.equals(getUnparsed());
        }
        return super.eIsSet(featureID);
    }
}
