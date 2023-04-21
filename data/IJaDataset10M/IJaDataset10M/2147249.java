package pl.org.minions.stigma.network.messaging.auth;

import pl.org.minions.stigma.network.messaging.NetworkMessageType;
import pl.org.minions.stigma.network.messaging.base.SimpleMessage;

/**
 * Class representing message sent after accepting login and
 * password.
 */
public class LoginPasswordAccepted extends SimpleMessage {

    /**
     * Constructor.
     */
    public LoginPasswordAccepted() {
        super(NetworkMessageType.LOGIN_PASSWORD_ACCEPTED);
    }
}
