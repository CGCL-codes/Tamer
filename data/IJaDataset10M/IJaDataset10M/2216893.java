package pl.org.minions.stigma.network.messaging.auth;

import pl.org.minions.stigma.network.messaging.NetworkMessageType;
import pl.org.minions.stigma.network.messaging.base.SimpleMessage;

/**
 * Message carrying information about bad version of client
 * in authorization data.
 */
public class LoginBadVersion extends SimpleMessage {

    /**
     * Constructor - creates empty message.
     */
    public LoginBadVersion() {
        super(NetworkMessageType.LOGIN_BAD_VERSION);
    }
}
