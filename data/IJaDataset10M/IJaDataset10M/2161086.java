package net.solarnetwork.central.user.biz;

import net.solarnetwork.central.user.domain.User;
import net.solarnetwork.domain.NetworkAssociationDetails;
import net.solarnetwork.domain.RegistrationReceipt;

/**
 * API for user registration tasks.
 * 
 * @author matt
 * @version $Id: RegistrationBiz.java 1755 2011-09-08 23:09:22Z msqr $
 */
public interface RegistrationBiz {

    /** 
	 * Flag for a String value that should not change.
	 * 
	 * <p>For example, when updating a User, the password field can be 
	 * left unchanged when set to this value.</p>
	 */
    static final String DO_NOT_CHANGE_VALUE = "**DO_NOT_CHANGE**";

    /**
	 * Register a new user.
	 * 
	 * <p>Use this method to register a new user. After registration the user
	 * will be stored in the back end, but the user will require confirmation
	 * before they can officially log into the application (see 
	 * {@link #confirmRegisteredUser(RegistrationReceipt)}).</p>
	 * 
	 * @param user the new user to register
	 * @return a confirmation string suitable to pass to 
	 * {@link #confirmRegisteredUser(String, String, BizContext)}
	 * @throws AuthorizationException if the desired login is taken already, 
	 * this exception will be thrown with the reason code 
	 * {@link AuthorizationException.Reason#DUPLICATE_LOGIN}
	 */
    RegistrationReceipt registerUser(User user) throws AuthorizationException;

    /**
	 * Helper method for creating a RegistrationReceipt instnace from a
	 * username and code.
	 * 
	 * <p>This has been added to support web flows.</p>
	 * 
	 * @param username the username
	 * @param confirmationCode the confirmation code
	 * @return the receipt instance
	 */
    public RegistrationReceipt createReceipt(String username, String confirmationCode);

    /**
	 * Confirm a registered user.
	 * 
	 * <p>After a user has registered (see {@link #registerUser(User)}) they
	 * must confirm the registration via this method. After confirmation the
	 * user can login via {@link UserBiz#logonUser(String, String)} as a normal
	 * user.</p>
	 * 
	 * @param receipt the registration receipt to confirm
	 * @return the confirmed user
	 * @throws AuthorizationException if the receipt details do not match those
	 * returned from a previous call to {@link #registerUser(User)} then the
	 * reason code will be set to
	 * {@link AuthorizationException.Reason#REGISTRATION_NOT_CONFIRMED};
	 * if the login is not found then
	 * {@link AuthorizationException.Reason#UNKNOWN_LOGIN}; if the account has
	 * already been confirmed then 
	 * {@link AuthorizationException.Reason#REGISTRATION_ALREADY_CONFIRMED}
	 */
    User confirmRegisteredUser(RegistrationReceipt receipt) throws AuthorizationException;

    /**
	 * Generate a new {@link NetworkAssociationDetails} entity.
	 * 
	 * <p>This will return a new {@link NetworkAssociationDetails} with a new,
	 * unique node ID and the system details associated with specified User.
	 * The node will still need to associate with the system before it will
	 * be recognized.</p>
	 * 
	 * @param user the user to associate a new node with
	 * @return new NodeAssociationDetails entity
	 */
    NetworkAssociationDetails createNodeAssociation(User user);

    /**
	 * Confirm a node association previously created via {@link #createNodeAssociation(User)}.
	 * 
	 * <p>This method must be called after a call to {@link #createNodeAssociation(User)}
	 * to confirm the node association.</p>
	 * 
	 * @param user the user to associate the node with
	 * @param nodeId the node ID from {@link NetworkAssociationDetails#getNodeId()}
	 * @param confirmationKey the confirmation code from 
	 * {@link NetworkAssociationDetails#getConfirmationKey()}
	 * @return new RegistrationReceipt object
	 * @throws AuthorizationException if the details do not match those
	 * returned from a previous call to {@link #createNodeAssociation(User)} then the
	 * reason code will be set to
	 * {@link AuthorizationException.Reason#REGISTRATION_NOT_CONFIRMED};
	 * if the node has already been confirmed then 
	 * {@link AuthorizationException.Reason#REGISTRATION_ALREADY_CONFIRMED}
	 */
    RegistrationReceipt confirmNodeAssociation(User user, Long nodeId, String confirmationKey) throws AuthorizationException;
}
