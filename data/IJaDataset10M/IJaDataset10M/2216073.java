package org.az.tb.services.client;

import java.util.List;
import javax.servlet.ServletException;
import org.az.tb.common.vo.client.ContactVo;
import org.az.tb.common.vo.client.UserProfileVo;
import org.az.tb.common.vo.client.exceptions.AuthenticationException;
import org.az.tb.common.vo.client.exceptions.InvalidUserException;
import org.az.tb.common.vo.client.exceptions.NoSuchUserException;
import org.az.tb.common.vo.client.exceptions.TechException;
import org.az.tb.common.vo.client.exceptions.ValidationException;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath(BeanNamesConst.USER_SERVICE_BEAN_NAME)
public interface UserService extends RemoteService {

    /**
     * Main method for user login process. Different combinations of login parameters are possible: username and password,
     * session id, invitation code, invitation code & session id. See functional specification for more info. 
     * @param username combination of username and passowrd can be used for login.
     * @param password combination of username and passowrd can be used for login.
     * @param sessionId persistent session id (can be used for login)
     * @param invitationCode invitation code (can be used for login in some cases)
     * @param rememberMe if the new session should be persistent.
     * @return contact vo with session id.
     * @throws AuthenticationException in case some of the provided arguments are not ok (pass doesn't match, ...)
     */
    public ContactVo login(String username, String password, String sessionId, String invitationCode, boolean rememberMe) throws AuthenticationException;

    public void logout() throws AuthenticationException;

    /**
     * sends the password of the user to his email
     */
    public void restoreAccount(String email) throws AuthenticationException, TechException;

    /**
     * Performing user registration
     * 
     * @param form
     * @throws ServletException
     */
    public void register(UserProfileVo form) throws AuthenticationException, TechException, ValidationException;

    public ContactVo confirmEmailAndLogin(String id, String key) throws AuthenticationException;

    public ContactVo changeUserData(UserProfileVo user) throws TechException, ValidationException;

    /**
     * Changes the password for the current user from session. The new password and its confirmation is stored
     * in the UserProfileVo, the old one is passed as an argument.
     * @param oldPassword old password to compare with data from db.
     * @param user profile with user data and new password.
     */
    public void changePassword(String oldPassword, UserProfileVo user) throws TechException, ValidationException;

    /**
     * Returns the user public profile. The same old user data, but without any passwords.
     * @param userId user id to search for profile
     * @return public user profile.
     */
    public ContactVo getUserPublicProfile(Long userId) throws InvalidUserException, NoSuchUserException, TechException;

    public List<ContactVo> getUsersByProperty(final String keyword, final int pageSize, final int startIndex);
}
