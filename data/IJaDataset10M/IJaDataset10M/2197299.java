package at.gp.web.jsf.codi.service;

import java.io.Serializable;

/**
 * @author Gerhard Petracek
 */
public interface UserService extends Serializable {

    void registerUser(String userName, String password);

    boolean checkCredentials(String userName, String password);
}
