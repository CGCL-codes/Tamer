package pl.org.minions.stigma.network.messaging.auth;

import pl.org.minions.stigma.network.messaging.NetworkMessageType;
import pl.org.minions.stigma.network.messaging.base.SimpleMessage;

/**
 * Class representing message informing that client is ready
 * to receive more data from server.
 */
public class LoginProceed extends SimpleMessage {

    /** Constructor. */
    public LoginProceed() {
        super(NetworkMessageType.LOGIN_PROCEED);
    }
}
