package com.inet.qlcbcc.security;

import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import com.inet.qlcbcc.domain.AuthenticationMode;

/**
 * AuthenticationInfo.
 *
 * @author Duyen Tang
 * @version $Id: AuthenticationInfo.java 2011-07-07 11:10:18z tttduyen $
 *
 * @since 1.0
 */
public class AuthenticationInfo extends SimpleAuthenticationInfo {

    private AuthenticationMode authenticationMode;

    /**
   * The serial version UID
   */
    private static final long serialVersionUID = -8765530070192035165L;

    public AuthenticationInfo() {
    }

    public AuthenticationInfo(Object principal, Object hashedCredentials, ByteSource credentialsSalt, String realmName) {
        super(principal, hashedCredentials, credentialsSalt, realmName);
    }

    public AuthenticationInfo(Object principal, Object credentials, String realmName) {
        super(principal, credentials, realmName);
    }

    public AuthenticationInfo(PrincipalCollection principals, Object hashedCredentials, ByteSource credentialsSalt) {
        super(principals, hashedCredentials, credentialsSalt);
    }

    public AuthenticationInfo(PrincipalCollection principals, Object credentials) {
        super(principals, credentials);
    }

    /**
   * Return the authentication mode
   * 
   * @return the authenticationMode
   */
    public AuthenticationMode getAuthenticationMode() {
        return authenticationMode;
    }

    /**
   * Set authentication mode
   * 
   * @param authenticationMode the authenticationMode to set
   */
    public void setAuthenticationMode(AuthenticationMode authenticationMode) {
        this.authenticationMode = authenticationMode;
    }
}
