package org.exos;

import org.exos.exchange.ttp.UserDataField;

/**
 * Interface to implement if an account should be created by EXOS.
 * @author ada
 */
public interface IAccountCreationListener {

    /**
	 * Informs the listener if the server (TTP) has requested
	 * additional data for a complete account creation. <b>Needs
	 * to return immediately.</b> After gathering the user data
	 * the listener must call
	 * {@link org.exos.Exos#setUserDataForAccountCreation(org.exos.ExosTaskIdentifier, org.exos.exchange.ttp.UserDataField[]) }
	 * <p>
	 * If the clients phone number was not passed (by the use of <code>createAccount(listener, ttpUrl)</code>)
	 * this will ask for that too (key: "mytel").
	 * </p>
	 * @param fields the array of user data fields specified by the server
	 */
    void requestAdditionalData(UserDataField fields[]);

    /**
	 * Informs the listener if the data received from the TTP could not be
	 * verified (eg. the certificate).
	 */
    void ttpVerificationFailed();

    /**
	 * Informs the listener if the account creation failed.
	 * To see the reason check the exception in e
	 * @param e error event object
	 */
    void accountCreationFailed(ExosTaskErrorEvent e);

    /**
	 * Informs the receiver about a necessary manual verification
	 * on the TTP side. This means that the account creation can
	 * only be completed as soon the owner of the TTP manually accepts the
	 * request for identification/authentication.<p>
	 * Nevertheless the account has been created but can just not be used
	 * yet to transfer data.
	 * @param e event object containing the account
	 * id of the created account
	 */
    void accountCreationDelayed(ExosTaskResultEvent e);

    /**
	 * Informs the receiver on successful account creation.
	 * The receiver shall store the account id for later use.
	 * @param e event object containing the account
	 * id of the created account
	 */
    void accountCreated(ExosTaskResultEvent e);
}
