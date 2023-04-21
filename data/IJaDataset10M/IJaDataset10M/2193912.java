package mindbright.ssh;

import java.net.*;
import java.io.*;
import mindbright.security.RSAPublicKey;

public interface SSHAuthenticator {

    public String getUsername(SSHClientUser origin) throws IOException;

    public String getPassword(SSHClientUser origin) throws IOException;

    public String getChallengeResponse(SSHClientUser origin, String challenge) throws IOException;

    public int[] getAuthTypes(SSHClientUser origin);

    public int getCipher(SSHClientUser origin);

    public SSHRSAKeyFile getIdentityFile(SSHClientUser origin) throws IOException;

    public String getIdentityPassword(SSHClientUser origin) throws IOException;

    public boolean verifyKnownHosts(RSAPublicKey hostPub) throws IOException;
}
