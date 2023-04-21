package org.tolven.shiro.authc;

import org.apache.shiro.authc.AuthenticationToken;

public interface RealmAuthenticationToken extends AuthenticationToken {

    public String getRealm();
}
