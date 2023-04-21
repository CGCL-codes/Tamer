package jolie.xtext.jolie;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see jolie.xtext.jolie.JoliePackage
 * @generated
 */
public interface JolieFactory extends EFactory {

    /**
   * The singleton instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    JolieFactory eINSTANCE = jolie.xtext.jolie.impl.JolieFactoryImpl.init();

    /**
   * Returns a new object of class '<em>Program</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Program</em>'.
   * @generated
   */
    Program createProgram();

    /**
   * Returns a new object of class '<em>Main</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Main</em>'.
   * @generated
   */
    Main createMain();

    /**
   * Returns a new object of class '<em>Main Process</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Main Process</em>'.
   * @generated
   */
    MainProcess createMainProcess();

    /**
   * Returns a new object of class '<em>Process</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Process</em>'.
   * @generated
   */
    Process createProcess();

    /**
   * Returns a new object of class '<em>Parallel Statement</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Parallel Statement</em>'.
   * @generated
   */
    ParallelStatement createParallelStatement();

    /**
   * Returns a new object of class '<em>Sequence Statement</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Sequence Statement</em>'.
   * @generated
   */
    SequenceStatement createSequenceStatement();

    /**
   * Returns a new object of class '<em>Basic Statement</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Basic Statement</em>'.
   * @generated
   */
    BasicStatement createBasicStatement();

    /**
   * Returns a new object of class '<em>Assign Statement</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Assign Statement</em>'.
   * @generated
   */
    AssignStatement createAssignStatement();

    /**
   * Returns a new object of class '<em>Post Increment Statement</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Post Increment Statement</em>'.
   * @generated
   */
    PostIncrementStatement createPostIncrementStatement();

    /**
   * Returns a new object of class '<em>Post Decrement Statement</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Post Decrement Statement</em>'.
   * @generated
   */
    PostDecrementStatement createPostDecrementStatement();

    /**
   * Returns a new object of class '<em>Expression</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Expression</em>'.
   * @generated
   */
    Expression createExpression();

    /**
   * Returns a new object of class '<em>OL Syntax Node</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>OL Syntax Node</em>'.
   * @generated
   */
    OLSyntaxNode createOLSyntaxNode();

    /**
   * Returns a new object of class '<em>Operation</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Operation</em>'.
   * @generated
   */
    Operation createOperation();

    /**
   * Returns a new object of class '<em>Int Literal</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Int Literal</em>'.
   * @generated
   */
    IntLiteral createIntLiteral();

    /**
   * Returns a new object of class '<em>Real Literal</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Real Literal</em>'.
   * @generated
   */
    RealLiteral createRealLiteral();

    /**
   * Returns a new object of class '<em>String</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>String</em>'.
   * @generated
   */
    String createString();

    /**
   * Returns the package supported by this factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the package supported by this factory.
   * @generated
   */
    JoliePackage getJoliePackage();
}
