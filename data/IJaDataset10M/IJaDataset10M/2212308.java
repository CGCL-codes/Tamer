package org.lcelb.accounts.manager.data.transaction.payment;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.lcelb.accounts.manager.data.DataPackage;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.lcelb.accounts.manager.data.transaction.payment.PaymentFactory
 * @model kind="package"
 * @generated
 */
public interface PaymentPackage extends EPackage {

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    String copyright = "Copyright (c) 2007, 2009 La Carotte Et Le Baton.\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v1.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v10.html\r\n \r\n  Contributors:\r\n      La Carotte Et Le Baton - initial API and implementation";

    /**
   * The package name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    String eNAME = "payment";

    /**
   * The package namespace URI.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    String eNS_URI = "payment";

    /**
   * The package namespace name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    String eNS_PREFIX = "payment";

    /**
   * The singleton instance of the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    PaymentPackage eINSTANCE = org.lcelb.accounts.manager.data.transaction.payment.impl.PaymentPackageImpl.init();

    /**
   * The meta object id for the '{@link org.lcelb.accounts.manager.data.transaction.payment.impl.AbstractPaymentImpl <em>Abstract Payment</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.lcelb.accounts.manager.data.transaction.payment.impl.AbstractPaymentImpl
   * @see org.lcelb.accounts.manager.data.transaction.payment.impl.PaymentPackageImpl#getAbstractPayment()
   * @generated
   */
    int ABSTRACT_PAYMENT = 0;

    /**
   * The feature id for the '<em><b>Id</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
    int ABSTRACT_PAYMENT__ID = DataPackage.MODEL_ELEMENT_WITH_ID__ID;

    /**
   * The number of structural features of the '<em>Abstract Payment</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
    int ABSTRACT_PAYMENT_FEATURE_COUNT = DataPackage.MODEL_ELEMENT_WITH_ID_FEATURE_COUNT + 0;

    /**
   * Returns the meta object for class '{@link org.lcelb.accounts.manager.data.transaction.payment.AbstractPayment <em>Abstract Payment</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Abstract Payment</em>'.
   * @see org.lcelb.accounts.manager.data.transaction.payment.AbstractPayment
   * @generated
   */
    EClass getAbstractPayment();

    /**
   * Returns the factory that creates the instances of the model.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the factory that creates the instances of the model.
   * @generated
   */
    PaymentFactory getPaymentFactory();

    /**
   * <!-- begin-user-doc -->
   * Defines literals for the meta objects that represent
   * <ul>
   *   <li>each class,</li>
   *   <li>each feature of each class,</li>
   *   <li>each enum,</li>
   *   <li>and each data type</li>
   * </ul>
   * <!-- end-user-doc -->
   * @generated
   */
    interface Literals {

        /**
     * The meta object literal for the '{@link org.lcelb.accounts.manager.data.transaction.payment.impl.AbstractPaymentImpl <em>Abstract Payment</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.lcelb.accounts.manager.data.transaction.payment.impl.AbstractPaymentImpl
     * @see org.lcelb.accounts.manager.data.transaction.payment.impl.PaymentPackageImpl#getAbstractPayment()
     * @generated
     */
        EClass ABSTRACT_PAYMENT = eINSTANCE.getAbstractPayment();
    }
}
