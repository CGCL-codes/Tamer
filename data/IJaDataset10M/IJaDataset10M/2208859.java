package org.eclipse.emf.test.models.ppo;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.emf.test.models.ppo.PPOPackage
 * @generated
 */
public interface PPOFactory extends EFactory {

    /**
   * The singleton instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    PPOFactory eINSTANCE = org.eclipse.emf.test.models.ppo.impl.PPOFactoryImpl.init();

    /**
   * Returns a new object of class '<em>Item</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Item</em>'.
   * @generated
   */
    Item createItem();

    /**
   * Returns a new object of class '<em>US Address</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>US Address</em>'.
   * @generated
   */
    USAddress createUSAddress();

    /**
   * Returns a new object of class '<em>Purchase Order</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Purchase Order</em>'.
   * @generated
   */
    PurchaseOrder createPurchaseOrder();

    /**
   * Returns the package supported by this factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the package supported by this factory.
   * @generated
   */
    PPOPackage getPPOPackage();
}
