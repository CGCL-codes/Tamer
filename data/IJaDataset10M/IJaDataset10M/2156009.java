package org.xanadu.mud;

import java.util.Set;
import org.xanadu.mud.entity.MUDUser;
import org.xanadu.mud.security.RoleEnum;

public interface IUserAgent {

    boolean isRemote();

    boolean isLoggedIn();

    Set<RoleEnum> getRoles();

    MUDUser getUser();

    void write(String s);

    void loggedIn(MUDUser user);
}
