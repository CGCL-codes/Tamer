package ecks.RPC;

import ecks.Configuration;
import ecks.Logging;
import ecks.services.Service;
import ecks.services.SrvAuth;
import ecks.services.SrvAuth_user;
import ecks.protocols.Generic;

/**
 * Class RPCHandler allows for functions that can be called remotely
 *
 * @author Jeff
 */
public class RPCHandler {

    /**
     * Method getUserCount returns the network wide usercount
     *
     * @return the userCount (type int).
     */
    public int getUserCount() {
        Logging.info("RPC", "Got UserCount Request!");
        return Generic.Users.size();
    }

    /**
     * Method getChanCount returns the network wide channel count.
     *
     * @return the chanCount (type int).
     */
    public int getChanCount() {
        Logging.info("RPC", "Got ChanCount Request!");
        return Generic.Channels.size();
    }

    /**
     * Method getRegUserCount returns the global registered users count (if auth service is used).
     *
     * @return the regUserCount (type int).
     */
    public int getRegUserCount() {
        Logging.info("RPC", "Got RegUserCount Request!");
        if (Configuration.getSvc().containsKey(Configuration.authservice)) return Configuration.getSvc().get(Configuration.authservice).getcount();
        return -1;
    }

    /**
     * Method getRegChanCount returns the global registered channels count (if chan service is used).
     *
     * @return the regChanCount (type int).
     */
    public int getRegChanCount() {
        Logging.info("RPC", "Got RegChanCount Request!");
        if (Configuration.getSvc().containsKey(Configuration.chanservice)) return Configuration.getSvc().get(Configuration.chanservice).getcount();
        return -1;
    }

    /**
     * Method getChanUserCount returns the number of users currently active in a channel
     *
     * @param whatchan of type String (channel
     * @return int (users in the channel)
     */
    public int getChanUserCount(String whatchan) {
        Logging.info("RPC", "Got ChanUserCount Request!");
        Logging.verbose("RPC", "Request was for channel: " + whatchan);
        if (Generic.Channels.containsKey(whatchan.toLowerCase())) return Generic.Channels.get(whatchan.toLowerCase()).clientmodes.size();
        return -1;
    }

    /**
     * Method checkUserAuth checks a username and password against the database
     *
     * @param username username is case insensitive
     * @param password of type String
     * @return boolean true if user is authenticated
     */
    public int checkUserAuth(String username, String password) {
        Logging.info("RPC", "Got checkUserAuth Request!");
        Logging.verbose("RPC", "Request was for user: " + username);
        if (!Configuration.getSvc().containsKey(Configuration.authservice)) return 0;
        SrvAuth auth = (SrvAuth) Configuration.getSvc().get(Configuration.authservice);
        if (auth.getUsers().containsKey(username.toLowerCase()) && auth.chkpass(password, username.toLowerCase())) return 1;
        return 0;
    }

    /**
     * Method getUserAccess returns ordinal of user access, or zero if no srvauth or user
     *
     * @param username of type String
     * @return int
     */
    public int getUserAccess(String username) {
        Logging.info("RPC", "Got getUserAccess Request!");
        Logging.verbose("RPC", "Request was for user: " + username);
        if (!Configuration.getSvc().containsKey(Configuration.authservice)) return 0;
        SrvAuth auth = (SrvAuth) Configuration.getSvc().get(Configuration.authservice);
        if (!auth.getUsers().containsKey(username.toLowerCase())) return 0;
        SrvAuth_user u = auth.getUsers().get(username.toLowerCase());
        return u.getAccess().ordinal();
    }
}
