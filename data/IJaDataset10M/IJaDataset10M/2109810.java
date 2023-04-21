package de.objectcode.openk.soa.auth.store.api;

import java.io.Serializable;
import java.util.Set;

/**
 * Authentication token.
 * 
 * An authentication token is issued by the authenticator. All security relevant services require this token. A token
 * has a limited lifetime.
 * 
 * @author junglas
 */
public class AuthToken implements Serializable {

    private static final long serialVersionUID = 722921619119052733L;

    String userId;

    String mandantorId;

    Set<String> effectiveRoleIds;

    long expires;

    String token;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMandantorId() {
        return mandantorId;
    }

    public void setMandantorId(String mandantorId) {
        this.mandantorId = mandantorId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Set<String> getEffectiveRoleIds() {
        return effectiveRoleIds;
    }

    public void setEffectiveRoleIds(Set<String> effectiveRoleIds) {
        this.effectiveRoleIds = effectiveRoleIds;
    }

    public boolean isInRole(String roleId) {
        return effectiveRoleIds.contains(roleId);
    }

    public long getExpires() {
        return expires;
    }

    public void setExpires(long expires) {
        this.expires = expires;
    }
}
