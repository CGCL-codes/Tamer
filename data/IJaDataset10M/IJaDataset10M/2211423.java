package tefkat.engine.runtime;

import java.util.List;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Expression</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link tefkat.engine.runtime.Expression#getExpr <em>Expr</em>}</li>
 * </ul>
 * </p>
 *
 * @see tefkat.engine.runtime.RuntimePackage#getExpression()
 * @model abstract="true"
 * @generated
 */
public interface Expression extends EObject {

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String copyright = "Copyright michael lawley 2004";

    /**
     * Returns the value of the '<em><b>Expr</b></em>' container reference.
     * It is bidirectional and its opposite is '{@link tefkat.engine.runtime.CompoundExpr#getArg <em>Arg</em>}'.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Expr</em>' container reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Expr</em>' container reference.
     * @see #setExpr(CompoundExpr)
     * @see tefkat.engine.runtime.RuntimePackage#getExpression_Expr()
     * @see tefkat.engine.runtime.CompoundExpr#getArg
     * @model opposite="arg"
     * @generated
     */
    CompoundExpr getExpr();

    /**
     * Sets the value of the '{@link tefkat.engine.runtime.Expression#getExpr <em>Expr</em>}' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Expr</em>' container reference.
     * @see #getExpr()
     * @generated
     */
    void setExpr(CompoundExpr value);

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @model dataType="tefkat.engine.runtime.List" many="false" exceptions="tefkat.engine.runtime.ResolutionException tefkat.engine.runtime.NotGroundException" contextDataType="tefkat.engine.runtime.Context"
     * @generated
     */
    List eval(Context context) throws ResolutionException, NotGroundException;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @model dataType="tefkat.engine.runtime.List" many="false" exceptions="tefkat.engine.runtime.ResolutionException tefkat.engine.runtime.NotGroundException" contextDataType="tefkat.engine.runtime.Context" bindingDataType="tefkat.engine.runtime.Binding"
     * @generated
     */
    List eval(Context context, Binding binding) throws ResolutionException, NotGroundException;
}
