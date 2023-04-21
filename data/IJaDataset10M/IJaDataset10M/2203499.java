package org.lcelb.accounts.manager.data.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.lcelb.accounts.manager.data.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class DataFactoryImpl extends EFactoryImpl implements DataFactory {

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public static final String copyright = "Copyright (c) 2007, 2009 La Carotte Et Le Baton.\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v1.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v10.html\r\n \r\n  Contributors:\r\n      La Carotte Et Le Baton - initial API and implementation";

    /**
   * Creates the default factory implementation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public static DataFactory init() {
        try {
            DataFactory theDataFactory = (DataFactory) EPackage.Registry.INSTANCE.getEFactory("data");
            if (theDataFactory != null) {
                return theDataFactory;
            }
        } catch (Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new DataFactoryImpl();
    }

    /**
   * Creates an instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public DataFactoryImpl() {
        super();
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    public EObject create(EClass eClass) {
        switch(eClass.getClassifierID()) {
            case DataPackage.BANK:
                return createBank();
            case DataPackage.ACCOUNT:
                return createAccount();
            case DataPackage.COMPOUND_OWNER:
                return createCompoundOwner();
            case DataPackage.ACCOUNT_MANAGER:
                return createAccountManager();
            case DataPackage.MODEL_ELEMENT_WITH_ID:
                return createModelElementWithId();
            default:
                throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
        }
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public Bank createBank() {
        BankImpl bank = new BankImpl();
        return bank;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public Account createAccount() {
        AccountImpl account = new AccountImpl();
        return account;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public CompoundOwner createCompoundOwner() {
        CompoundOwnerImpl compoundOwner = new CompoundOwnerImpl();
        return compoundOwner;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public AccountManager createAccountManager() {
        AccountManagerImpl accountManager = new AccountManagerImpl();
        return accountManager;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public ModelElementWithId createModelElementWithId() {
        ModelElementWithIdImpl modelElementWithId = new ModelElementWithIdImpl();
        return modelElementWithId;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public DataPackage getDataPackage() {
        return (DataPackage) getEPackage();
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @deprecated
   * @generated
   */
    @Deprecated
    public static DataPackage getPackage() {
        return DataPackage.eINSTANCE;
    }
}
