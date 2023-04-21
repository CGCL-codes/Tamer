package org.eclipse.emf.test.models.ref;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.emf.test.models.ref.RefPackage
 * @generated
 */
public interface RefFactory extends EFactory {

    /**
   * The singleton instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    RefFactory eINSTANCE = org.eclipse.emf.test.models.ref.impl.RefFactoryImpl.init();

    /**
   * Returns a new object of class '<em>A</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>A</em>'.
   * @generated
   */
    A createA();

    /**
   * Returns a new object of class '<em>B</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>B</em>'.
   * @generated
   */
    B createB();

    /**
   * Returns a new object of class '<em>C1</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>C1</em>'.
   * @generated
   */
    C1 createC1();

    /**
   * Returns a new object of class '<em>C2</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>C2</em>'.
   * @generated
   */
    C2 createC2();

    /**
   * Returns a new object of class '<em>C</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>C</em>'.
   * @generated
   */
    C createC();

    /**
   * Returns a new object of class '<em>D</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>D</em>'.
   * @generated
   */
    D createD();

    /**
   * Returns a new object of class '<em>E</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>E</em>'.
   * @generated
   */
    E createE();

    /**
   * Returns a new object of class '<em>C4</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>C4</em>'.
   * @generated
   */
    C4 createC4();

    /**
   * Returns a new object of class '<em>C3</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>C3</em>'.
   * @generated
   */
    C3 createC3();

    /**
   * Returns the package supported by this factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the package supported by this factory.
   * @generated
   */
    RefPackage getRefPackage();
}
