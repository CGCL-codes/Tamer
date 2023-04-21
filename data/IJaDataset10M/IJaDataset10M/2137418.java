package ru.dragon.bugzilla.api;

import ru.dragon.bugzilla.BugzillaException;

/**
 * @author <a href="mailto: NIzhikov@gmail.com">Izhikov Nikolay</a>
 */
public interface UserService {

    /**
     * Login to bugzilla.
     * Setup necessary cookies in XmlRpcWithCookieTransport
     *
     * @param bugzillaUrl
     * @param login
     * @param password
     * @return user id
     * @throws BugzillaException
     */
    Integer login(String bugzillaUrl, String login, String password) throws BugzillaException;

    void logout(String bugzillaUrl) throws BugzillaException;

    boolean logined();
}
