package org.ufacekit.ui.examples.pim.core.model.upim.impl;

import org.eclipse.emf.ecore.EClass;
import org.ufacekit.ui.examples.pim.core.model.upim.AddressBook;
import org.ufacekit.ui.examples.pim.core.model.upim.AddressBookApplication;
import org.ufacekit.ui.examples.pim.core.model.upim.UpimPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Address Book Application</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.ufacekit.ui.examples.pim.core.model.upim.impl.AddressBookApplicationImpl#getAddressbooks <em>Addressbooks</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AddressBookApplicationImpl extends ApplicationImpl implements AddressBookApplication {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected AddressBookApplicationImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return UpimPackage.Literals.ADDRESS_BOOK_APPLICATION;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public AddressBook getAddressbooks() {
        return (AddressBook) eGet(UpimPackage.Literals.ADDRESS_BOOK_APPLICATION__ADDRESSBOOKS, true);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setAddressbooks(AddressBook newAddressbooks) {
        eSet(UpimPackage.Literals.ADDRESS_BOOK_APPLICATION__ADDRESSBOOKS, newAddressbooks);
    }
}
