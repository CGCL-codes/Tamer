package com.sshtools.j2ssh.agent;

import com.sshtools.j2ssh.io.ByteArrayReader;
import com.sshtools.j2ssh.io.ByteArrayWriter;
import com.sshtools.j2ssh.subsystem.SubsystemMessage;
import com.sshtools.j2ssh.transport.InvalidMessageException;
import java.io.IOException;

class SshAgentUnlock extends SubsystemMessage {

    /**  */
    public static final int SSH_AGENT_UNLOCK = 209;

    String password;

    /**
     * Creates a new SshAgentUnlock object.
     */
    public SshAgentUnlock() {
        super(SSH_AGENT_UNLOCK);
    }

    /**
     * Creates a new SshAgentUnlock object.
     *
     * @param password
     */
    public SshAgentUnlock(String password) {
        super(SSH_AGENT_UNLOCK);
        this.password = password;
    }

    /**
     *
     *
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     *
     *
     * @return
     */
    public String getMessageName() {
        return "SSH_AGENT_UNLOCK";
    }

    /**
     *
     *
     * @param baw
     *
     * @throws java.io.IOException
     * @throws com.sshtools.j2ssh.transport.InvalidMessageException DOCUMENT
     *         ME!
     * @throws InvalidMessageException
     */
    public void constructByteArray(ByteArrayWriter baw) throws java.io.IOException, com.sshtools.j2ssh.transport.InvalidMessageException {
        try {
            baw.writeString(password);
        } catch (IOException ioe) {
            throw new InvalidMessageException(ioe.getMessage());
        }
    }

    /**
     *
     *
     * @param bar
     *
     * @throws java.io.IOException
     * @throws com.sshtools.j2ssh.transport.InvalidMessageException DOCUMENT
     *         ME!
     * @throws InvalidMessageException
     */
    public void constructMessage(ByteArrayReader bar) throws java.io.IOException, com.sshtools.j2ssh.transport.InvalidMessageException {
        try {
            password = bar.readString();
        } catch (IOException ioe) {
            throw new InvalidMessageException(ioe.getMessage());
        }
    }
}
