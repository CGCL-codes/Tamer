package pl.ehotelik.portal.service.security;

import pl.ehotelik.portal.domain.security.PortalUser;
import pl.ehotelik.portal.exception.security.AuthenticationException;

/**
 * User: mkr <a href="mailto:michal.krawczak@blstream.com">Micha? Krawczak</code>
 * Date: Nov 22, 2010 10:09:25 PM
 * Class <code>pl.ehotelik.portal.service.security.LoginService</code> is representation
 * of login service object.
 */
public interface LoginService {

    public PortalUser login(String loginName, String password) throws AuthenticationException;

    public void logout() throws AuthenticationException;
}
