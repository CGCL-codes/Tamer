package gov.lanl.Authenticate;

/**The Authenticator interface provides support for checking the authentication
 * for a single user or a list of users.  It can either make the decision
 * or defer to a service, such as "SecureID" service; it may also keep
 * a time dependent transient list to support one time sign on via 
 * secure cards or time restricted passwords.  It expects to see
 * data as GSSUP's InitialContextToken which is:
 *    struct InitialContextToken {
 *        CSI::UTF8String          username;
 *        CSI::UTF8String          password;
 *        CSI::GSS_NT_ExportedName target_name;
 *
 *    typedef sequence <octet> GSS_NT_ExportedName;
 *
 * @author James George
 * @version $Revision: 1017 $ $Date: 2001-11-08 18:26:55 -0500 (Thu, 08 Nov 2001) $
 **/
public interface AuthenticatorOperations {

    /**Check to see if a user is or can be authenticated; authenticate the user
     * if possible.
     * @param userToken is the user information to use for authentication,
     * and consists of name, password and target_name.
     * @return true if the user is authenticated.
     **/
    boolean isUserOk(org.omg.GSSUP.InitialContextToken userToken);

    /**Check to see if a user is or can be authenticated; authenticate the user
     * if possible.
     * User information to use for authentication is assumed to be in the
     * security credentials of the connection.
     * @return true if the user is authenticated.
     **/
    boolean isUserOkFromCredentials();

    /**Check to see if the users are or can be authenticated; authenticate each
     * user as necessary.
     * @param userTokenSeq is a sequence of user information to use for 
     * authentication, and consists of name, password and target_name for each user.
     * @return a sequence of booleans for the user list, each element 
     * specifiying if the corresponding element in the userTokenSeq is 
     * authenticated.
     **/
    boolean[] areUsersOk(org.omg.GSSUP.InitialContextToken[] userTokenSeq);

    /**Logoff the user; i.e. forget that the user had been previously authenticated.
     * @param userToken is the user data uniquely identifying the user to be 
     * logged off.
     **/
    void logoffUser(org.omg.GSSUP.InitialContextToken userToken);

    /**Logoff the user; i.e. forget that the user had been previously authenticated.
     * User information to identify the user to log off is assumed to be in the
     * security credentials of the connection.
     **/
    void logoffUserFromCredentials();
}
