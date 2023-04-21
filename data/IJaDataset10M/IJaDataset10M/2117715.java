package jmemento.api.service.user;

import jmemento.api.domain.user.IUser;
import jmemento.web.controller.user.dto.UserDto;

/**
 * @author Rusty Wright
 * 
 */
public interface IUserSignOnService {

    /**
     * @param user
     */
    void initUserSession(final IUser user);

    /**
     * @param user
     */
    void addNewUser(UserDto user, String serverName, int serverPort);

    /**
     * @param userId
     * @return true or false.
     */
    boolean isUserVerified(String userId);

    /**
     * @param emailId
     * @return true or false.
     */
    boolean isEmailIdInUnverified(String emailId);

    /**
     * @param emailId
     */
    void emailVerified(String emailId);

    /**
     * @param userId
     * @return
     */
    String getPassword(String userId);

    /**
     * @param userName
     * @return
     */
    boolean isNameInUse(String userName);
}
