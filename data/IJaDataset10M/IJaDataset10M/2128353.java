package org.hardtokenmgmt.admin.control;

import java.io.IOException;
import java.util.List;
import org.hardtokenmgmt.admin.model.TokenDataVO;
import org.hardtokenmgmt.admin.model.UserDataVO;
import org.hardtokenmgmt.admin.model.UserQueryResultVO;
import org.hardtokenmgmt.common.vo.DateFilter;
import org.hardtokenmgmt.common.vo.OrganizationVO;
import org.hardtokenmgmt.ws.gen.AlreadyRevokedException_Exception;
import org.hardtokenmgmt.ws.gen.AuthorizationDeniedException_Exception;
import org.hardtokenmgmt.ws.gen.PublisherException_Exception;

/**
 * Interface containing methods for managing tokens (mainly soft ones).
 * 
 * 
 * @author Philip Vendil 17 Jan 2010
 *
 * @version $Id$
 */
public interface ITokenAdminManager {

    /**
     * Method used to list users of the given a set of search parameters.
     * 
     * @param orgIds a list of organization id's the user should belong to.
     * @param tokenTypes a list of DisplayTokenSelector.TOKENTYPE, null indicates all token types.
     * @param filterString a filter used to filter out data in DN, could be name, unique id or user id. null for no filter.
     * @param departments a list of departments to filter on. null returns all departments
     * @param statuses a list of statuses that should be returned
     * @param dateFilters a list of dateFilters, null of no date filter should be used.
     * @param tokenStatusFilterFlag a REVOCATION_FILTER_FLAG describing the revocation filter
     * @param resultBeginIndex the begin index of the matching result, used for paging.
     * @param maxReturnSize the maximum of returned users.
     * 
     * @return a UserQueryResult containing the list of users having tokens that fulfill the specified filters
     * @throws IOException if communication exception occurred.
     * @throws AuthorizationDeniedException_Exception if the administrator haven't got privileges to republish the specified token.
     * String tokenStatus,
     */
    UserQueryResultVO listUsers(List<String> orgIds, List<String> tokenTypes, String filterString, List<String> department, List<String> statuses, List<DateFilter> dateFilters, int resultBeginIndex, int maxReturnSize) throws IOException, AuthorizationDeniedException_Exception;

    /**
	 * Method used to revoke a user.
	 * 
	 *  @param orgId of the organization the token belongs to.
	 * @param username of the user related to token to revoke.
	 * @param revokeReason reason for revocation.
	 * @param deleteUser if the user also should be deleted.
	 * @throws IOException if communication exception occurred.
	 * @throws AuthorizationDeniedException_Exception if the administrator haven't got privileges to republish the specified token.
	 * @return updated user data
	 * @throws AlreadyRevokedException_Exception 
	 */
    UserDataVO revokeUser(String orgId, String username, int revokeReason, boolean deleteUser) throws IOException, AuthorizationDeniedException_Exception, AlreadyRevokedException_Exception;

    /**
	 * Method fetch a user given the user name.
	 * 
	 * @param orgId of user to fetch data for.
	 * @param username of the user to fetch
	 * @param fetchTokenData if tokenData should be fetched as well.
	 * @return the user data or null if user does't exist
	 * 
	 * @throws IOException if communication exception occurred.
	 * @throws AuthorizationDeniedException_Exception if the administrator haven't got privileges to republish the specified token.
	 */
    UserDataVO fetchUser(String orgId, String username, boolean fetchTokenData) throws IOException, AuthorizationDeniedException_Exception;

    /**
	 * Method used to update the given user data in database.
	 * 
	 * @param userData the userData containing the data to update.
	 * 
	 * @throws IOException if communication exception occurred.
	 * @throws AuthorizationDeniedException_Exception if the administrator haven't got privileges to republish the specified token.
	 */
    void editUser(UserDataVO userData) throws IOException, AuthorizationDeniedException_Exception;

    /**
	 * Method used to send a notification to a given recipient.
	 * 
	 * @param recipient the address to send to, use ';' so separate multiple recipient.
	 * @param carbonCopyRecipient the address to send CC mail to, null if not used.
	 * @param subject the subject of the message
	 * @param message the message to send to
	 * 
	 * @throws IOException if communication exception occurred.
	 * @throws AuthorizationDeniedException_Exception if the administrator haven't got privileges to republish the specified token.
	 */
    void sendNotification(OrganizationVO orgVO, String userName, String recipient, String carbonCopyRecipient, String subject, String message) throws IOException, AuthorizationDeniedException_Exception;

    /**
	 * Method used when generating the key store directly.
	 * 
	 * @param userName EJBCA user name of the user
	 * @param password the generated password. 
	 * @return the actual key store data.
	 * 
	 * @throws IOException if communication exception occurred.
	 * @throws AuthorizationDeniedException_Exception if the administrator haven't got privileges to republish the specified token.
	 */
    byte[] getKeyStoreData(String userName, String password, String keyAlg, String keySpec) throws IOException, AuthorizationDeniedException_Exception;

    /**
	 * Method used to fetch a certificate from the server.
	 * 
	 * @param userName EJBCA user name of the user
	 * @param password the generated password. 
	 * @param the acual request data.
	 * @return the certificate data in PEM formats.
	 * 
	 * @throws IOException if communication exception occurred.
	 * @throws AuthorizationDeniedException_Exception if the administrator haven't got privileges to republish the specified token.
	 */
    byte[] getCertificate(String userName, String password, byte[] requestData) throws IOException, AuthorizationDeniedException_Exception;

    /**
	 * Method used to revoke a token.
	 * 
	 * @param orgId of the organization the token belongs to.
	 * @param username of the user related to token to revoke.
	 * @param tokenData the token to revoke.
	 * @param revokeReason reason for revocation.
	 * @throws IOException if communication exception occurred.
	 * @throws AuthorizationDeniedException_Exception if the administrator haven't got privileges to republish the specified token.
	 * @throws AlreadyRevokedException_Exception 
	 * @return updated user data
	 * 
	 */
    UserDataVO revokeToken(String orgId, String username, TokenDataVO tokenData, int revokeReason) throws IOException, AuthorizationDeniedException_Exception, AlreadyRevokedException_Exception;

    /**
	 * Method used to republish a token.
	 * 
	 * @param orgId of the organization the token belongs to.
	 * @param username of the user related to token to republish.
	 * @param tokenData the token to republish
	 * @throws IOException if communication exception occurred.
	 * @throws AuthorizationDeniedException_Exception if the administrator haven't got privileges to republish the specified token.
	 * @return updated user data
	 * @throws PublisherException_Exception if publishing failed to underlying systems.
	 */
    UserDataVO republishToken(String orgId, String username, TokenDataVO tokenData) throws IOException, AuthorizationDeniedException_Exception, PublisherException_Exception;
}
