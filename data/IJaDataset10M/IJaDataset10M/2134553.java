package com.sshtools.j2ssh.authentication;

import com.sshtools.j2ssh.io.ByteArrayReader;
import com.sshtools.j2ssh.io.ByteArrayWriter;
import com.sshtools.j2ssh.transport.InvalidMessageException;
import com.sshtools.j2ssh.transport.SshMessage;
import java.io.IOException;

public class SshMsgUserauthGssapiErrtok extends SshMessage {

    public SshMsgUserauthGssapiErrtok() {
        super(65);
        errorToken = "";
    }

    public String getMessageName() {
        return "SSH_MSG_USERAUTH_GSSAPI_ERRTOK";
    }

    protected void constructByteArray(ByteArrayWriter bytearraywriter) throws InvalidMessageException {
        try {
            bytearraywriter.writeString(errorToken);
        } catch (IOException ioexception) {
            throw new InvalidMessageException("Invalid message data");
        }
    }

    protected void constructMessage(ByteArrayReader bytearrayreader) throws InvalidMessageException {
        try {
            errorToken = bytearrayreader.readString();
        } catch (IOException ioexception) {
            throw new InvalidMessageException("Invalid message data");
        }
    }

    public String getErrorToken() {
        return errorToken;
    }

    public void setErrorToken(String s) {
        errorToken = s;
    }

    public static final int SSH_MSG_USERAUTH_GSSAPI_ERRTOK = 65;

    private String errorToken;
}
