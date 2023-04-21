package org.jactr.tools.async.message.command.login;

import java.io.Serializable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jactr.tools.async.credentials.ICredentials;
import org.jactr.tools.async.message.BaseMessage;
import org.jactr.tools.async.message.command.ICommand;

/**
 * @author developer
 *
 */
public class LoginCommand extends BaseMessage implements ICommand, Serializable {

    /**
   * 
   */
    private static final long serialVersionUID = -6888518069177191560L;

    /**
   logger definition
   */
    private static final Log LOGGER = LogFactory.getLog(LoginCommand.class);

    ICredentials _credentials;

    public LoginCommand(ICredentials credentials) {
        _credentials = credentials;
    }

    public ICredentials getCredentials() {
        return _credentials;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        sb.append(getClass().getSimpleName()).append(":").append(_credentials).append("]");
        return sb.toString();
    }
}
