package org.dfdaemon.il2.engine.domain;

import org.dfdaemon.il2.engine.GenericEntity;

/**
 * @author octo
 */
public class Player implements GenericEntity<Long> {

    private Long id;

    private String callsign;

    private String password;

    private AuthType authType;

    private String localeCode;

    public Player() {
    }

    public Player(final String callsign) {
        this.callsign = callsign;
    }

    public Player(final String callsign, final String password) {
        this.callsign = callsign;
        this.password = password;
    }

    public Player(final String callsign, final String password, final String localeCode) {
        this.callsign = callsign;
        this.localeCode = localeCode;
        this.password = password;
    }

    public Player(final String callsign, final String password, final AuthType authType, final String localeCode) {
        this.callsign = callsign;
        this.password = password;
        this.authType = authType;
        this.localeCode = localeCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getCallsign() {
        return callsign;
    }

    public void setCallsign(final String callsign) {
        this.callsign = callsign;
    }

    public String getLocaleCode() {
        return localeCode;
    }

    public void setLocaleCode(final String localeCode) {
        this.localeCode = localeCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public AuthType getAuthType() {
        return authType;
    }

    public void setAuthType(final AuthType authType) {
        this.authType = authType;
    }

    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Player");
        sb.append("{");
        sb.append("id=").append(id);
        sb.append(", callsign='").append(callsign).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", localeCode='").append(localeCode).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
