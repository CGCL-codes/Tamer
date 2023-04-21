package net.moep.irc.serverevents;

import sun.security.krb5.internal.crypto.b;
import net.moep.irc.ServerEventParameter;
import net.moep.irc.ServerEventTypes;
import static net.moep.util.Util.getIndex;

/**
 * @author schuppi
 * 
 */
public class NewUserEventParameter extends ServerEventParameter {

    private static final long serialVersionUID = 1L;

    public String nickname, username, hostname, realname, servername;

    @Override
    public ServerEventTypes getEventType() {
        return ServerEventTypes.NEWUSER;
    }

    /**
     * @param nickname
     * @param username
     * @param hostname
     * @param realname
     * @param servername
     */
    public NewUserEventParameter(String nickname, String username, String hostname, String realname, String servername) {
        super();
        this.nickname = nickname;
        this.username = username;
        this.hostname = hostname;
        this.realname = realname;
        this.servername = servername;
    }

    @Override
    public boolean testAll(StringBuilder... builders) {
        return testNickname(getIndex(0, builders)) & testUsername(getIndex(1, builders)) & testHostname(getIndex(2, builders)) & testRealname(getIndex(3, builders)) & testServername(getIndex(4, builders));
    }

    /**
     * @param obj
     * @return
     */
    private boolean testServername(StringBuilder obj) {
        return copy(obj, servername);
    }

    /**
     * @param obj
     * @return
     */
    private boolean testRealname(StringBuilder obj) {
        return copy(obj, realname);
    }

    /**
     * @param obj
     * @return
     */
    private boolean testHostname(StringBuilder obj) {
        return copy(obj, hostname);
    }

    /**
     * @param obj
     * @return
     */
    private boolean testUsername(StringBuilder obj) {
        return copy(obj, username);
    }

    /**
     * @param index
     * @return
     */
    private boolean testNickname(StringBuilder obj) {
        return copy(obj, nickname);
    }

    @Override
    public String toString() {
        return nickname + " " + username + "@" + hostname + " (" + realname + ") - " + servername;
    }
}
