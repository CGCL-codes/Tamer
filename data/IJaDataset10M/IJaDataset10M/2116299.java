package org.lcelb.accounts.manager.data.extensions.transaction.payment.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;
import org.lcelb.accounts.manager.data.ModelElementWithId;
import org.lcelb.accounts.manager.data.extensions.transaction.payment.*;
import org.lcelb.accounts.manager.data.transaction.payment.AbstractPayment;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.lcelb.accounts.manager.data.extensions.transaction.payment.PaymentPackage
 * @generated
 */
public class PaymentAdapterFactory extends AdapterFactoryImpl {

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public static final String copyright = "Copyright (c) 2007, 2009 La Carotte Et Le Baton.\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v1.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v10.html\r\n \r\n  Contributors:\r\n      La Carotte Et Le Baton - initial API and implementation";

    /**
   * The cached model package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    protected static PaymentPackage modelPackage;

    /**
   * Creates an instance of the adapter factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public PaymentAdapterFactory() {
        if (modelPackage == null) {
            modelPackage = PaymentPackage.eINSTANCE;
        }
    }

    /**
   * Returns whether this factory is applicable for the type of the object.
   * <!-- begin-user-doc -->
   * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
   * <!-- end-user-doc -->
   * @return whether this factory is applicable for the type of the object.
   * @generated
   */
    @Override
    public boolean isFactoryForType(Object object) {
        if (object == modelPackage) {
            return true;
        }
        if (object instanceof EObject) {
            return ((EObject) object).eClass().getEPackage() == modelPackage;
        }
        return false;
    }

    /**
   * The switch that delegates to the <code>createXXX</code> methods.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    protected PaymentSwitch<Adapter> modelSwitch = new PaymentSwitch<Adapter>() {

        @Override
        public Adapter caseDefaultPayment(DefaultPayment object) {
            return createDefaultPaymentAdapter();
        }

        @Override
        public Adapter caseModelElementWithId(ModelElementWithId object) {
            return createModelElementWithIdAdapter();
        }

        @Override
        public Adapter caseAbstractPayment(AbstractPayment object) {
            return createAbstractPaymentAdapter();
        }

        @Override
        public Adapter defaultCase(EObject object) {
            return createEObjectAdapter();
        }
    };

    /**
   * Creates an adapter for the <code>target</code>.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param target the object to adapt.
   * @return the adapter for the <code>target</code>.
   * @generated
   */
    @Override
    public Adapter createAdapter(Notifier target) {
        return modelSwitch.doSwitch((EObject) target);
    }

    /**
   * Creates a new adapter for an object of class '{@link org.lcelb.accounts.manager.data.extensions.transaction.payment.DefaultPayment <em>Default Payment</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.lcelb.accounts.manager.data.extensions.transaction.payment.DefaultPayment
   * @generated
   */
    public Adapter createDefaultPaymentAdapter() {
        return null;
    }

    /**
   * Creates a new adapter for an object of class '{@link org.lcelb.accounts.manager.data.ModelElementWithId <em>Model Element With Id</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.lcelb.accounts.manager.data.ModelElementWithId
   * @generated
   */
    public Adapter createModelElementWithIdAdapter() {
        return null;
    }

    /**
   * Creates a new adapter for an object of class '{@link org.lcelb.accounts.manager.data.transaction.payment.AbstractPayment <em>Abstract Payment</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.lcelb.accounts.manager.data.transaction.payment.AbstractPayment
   * @generated
   */
    public Adapter createAbstractPaymentAdapter() {
        return null;
    }

    /**
   * Creates a new adapter for the default case.
   * <!-- begin-user-doc -->
   * This default implementation returns null.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @generated
   */
    public Adapter createEObjectAdapter() {
        return null;
    }
}
