package com.ixora.rms.agents.providerhost;

import com.ixora.common.typedproperties.exception.VetoException;
import com.ixora.common.utils.Utils;
import com.ixora.rms.agents.AgentCustomConfiguration;
import com.ixora.rms.agents.messages.Msg;
import com.ixora.rms.providers.impl.process.ProcessExecutionMode;

/**
 * Configuration for host availability agent.
 * @author Daniel Moraru
 */
public final class ProcessConfiguration extends AgentCustomConfiguration {

    private static final long serialVersionUID = 6667532240986984708L;

    public static final int PORT_TELNET = 23;

    public static final int PORT_SSH2 = 22;

    public static final int PORT_DEFAULT = 0;

    public static final String EXECUTION_MODE = Msg.EXECUTION_MODE;

    public static final String USERNAME = Msg.USERNAME;

    public static final String PASSWORD = Msg.PASSWORD;

    public static final String USERNAME_PROMPT = Msg.USERNAME_PROMPT;

    public static final String PASSWORD_PROMPT = Msg.PASSWORD_PROMPT;

    public static final String SHELL_PROMPT = Msg.SHELL_PROPMT;

    public static final String PORT = Msg.PORT;

    /**
	 * Empty constructor. Required to allow the associated panel
	 * to create an empty configuration instance for editing.
	 */
    public ProcessConfiguration() {
        super();
        setProperty(EXECUTION_MODE, TYPE_STRING, true, true);
        setProperty(USERNAME, TYPE_STRING, true, true);
        setProperty(PASSWORD, TYPE_SECURE_STRING, true, true);
        setProperty(USERNAME_PROMPT, TYPE_STRING, true, true);
        setProperty(PASSWORD_PROMPT, TYPE_STRING, true, true);
        setProperty(SHELL_PROMPT, TYPE_STRING, true, true);
        setProperty(PORT, TYPE_STRING, true, false);
        setString(EXECUTION_MODE, ProcessExecutionMode.NORMAL.getStringKey());
        setString(USERNAME, "-");
        setString(PASSWORD, "-");
        setString(USERNAME_PROMPT, "login:");
        setString(PASSWORD_PROMPT, "Password:");
        setString(SHELL_PROMPT, "]$");
        setString(PORT, String.valueOf(PORT_DEFAULT));
    }

    /**
	 * @see com.ixora.common.typedproperties.TypedProperties#veto()
	 */
    public void veto() throws VetoException {
        ProcessExecutionMode execMode = ProcessExecutionMode.resolve(getString(Msg.EXECUTION_MODE));
        if (execMode == null) {
            throw new VetoException("Process execution mode is missing");
        }
        boolean usetelnet = (execMode == ProcessExecutionMode.TELNET);
        boolean ssh2 = (execMode == ProcessExecutionMode.SSH2);
        if (!usetelnet && !ssh2) {
            return;
        }
        Object username = getObject(USERNAME);
        if (username == null) {
            throw new VetoException("When telnet or ssh2 is enabled, the username must be specified");
        }
        if (usetelnet) {
            Object shell = getObject(SHELL_PROMPT);
            if (shell == null) {
                throw new VetoException("When telnet enabled, the shell prompt must be specified");
            }
        }
        String port = getString(PORT);
        if (!Utils.isEmptyString(port)) {
            try {
                Integer.parseInt(port.trim());
            } catch (NumberFormatException e) {
                throw new VetoException("Port number must be an integer value.");
            }
        }
    }
}
