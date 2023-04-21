package org.ufacekit.ui.examples.pim.core.model.upim;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Mail Application</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.ufacekit.ui.examples.pim.core.model.upim.MailApplication#getAccounts <em>Accounts</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.ufacekit.ui.examples.pim.core.model.upim.UpimPackage#getMailApplication()
 * @model
 * @generated
 */
public interface MailApplication extends Application {

    /**
	 * Returns the value of the '<em><b>Accounts</b></em>' containment reference list.
	 * The list contents are of type {@link org.ufacekit.ui.examples.pim.core.model.upim.MailAccount}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Accounts</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Accounts</em>' containment reference list.
	 * @see org.ufacekit.ui.examples.pim.core.model.upim.UpimPackage#getMailApplication_Accounts()
	 * @model containment="true"
	 * @generated
	 */
    EList<MailAccount> getAccounts();
}
