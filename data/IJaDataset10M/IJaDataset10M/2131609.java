package org.bpaul.rtalk.protocol;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Represents the response for the login request.
 * The error code should be zero in case of successful login.
 * @author BPaul
 *
 */
public class CSLoginResponse extends Response {

    private int errorcode;

    private String[] names;

    private List<String> roster;

    private String session;

    private String visibleIP;

    private Map<String, List<String>> groups;

    private String id;

    private Map<String, String> options;

    private Set<String> blocked;

    public CSLoginResponse() {
        super(Response.RESPONSE_ID_LOGIN);
    }

    public String[] getNames() {
        return names;
    }

    public void setNames(String[] names) {
        this.names = names;
    }

    public List<String> getRoster() {
        return roster;
    }

    public void setRoster(List<String> roster) {
        this.roster = roster;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getVisibleIP() {
        return visibleIP;
    }

    public void setVisibleIP(String visibleIP) {
        this.visibleIP = visibleIP;
    }

    public Map<String, List<String>> getGroups() {
        return groups;
    }

    public void setGroups(Map<String, List<String>> groups) {
        this.groups = groups;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(int errorcode) {
        this.errorcode = errorcode;
    }

    public Map<String, String> getOptions() {
        return options;
    }

    public void setOptions(Map<String, String> options) {
        this.options = options;
    }

    public Set<String> getBlocked() {
        return blocked;
    }

    public void setBlocked(Set<String> blocked) {
        this.blocked = blocked;
    }
}
