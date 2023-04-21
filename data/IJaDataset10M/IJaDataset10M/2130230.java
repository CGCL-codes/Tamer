package com.sshtools.j2ssh.authentication;

import com.sshtools.j2ssh.io.ByteArrayReader;
import com.sshtools.j2ssh.io.ByteArrayWriter;
import com.sshtools.j2ssh.transport.InvalidMessageException;
import com.sshtools.j2ssh.transport.SshMessage;

public class SshMsgUserauthGssapiExchangeComplete extends SshMessage {

    public SshMsgUserauthGssapiExchangeComplete() {
        super(63);
    }

    public String getMessageName() {
        return "SSH_MSG_USERAUTH_GSSAPI_EXCHANGE_COMPLETE";
    }

    protected void constructByteArray(ByteArrayWriter bytearraywriter) throws InvalidMessageException {
    }

    protected void constructMessage(ByteArrayReader bytearrayreader) throws InvalidMessageException {
    }

    public static final int SSH_MSG_USERAUTH_GSSAPI_EXCHANGE_COMPLETE = 63;
}
