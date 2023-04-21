package net.sourceforge.jrcom.gss;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.PrivilegedActionException;
import org.ietf.jgss.GSSContext;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.GSSManager;
import org.ietf.jgss.GSSName;
import org.ietf.jgss.Oid;

/**
 * @author Artem Glebov
 *
 */
public class GSSInitiatorSocket extends GSSSocket {

    private final String serverPrinc;

    public GSSInitiatorSocket(final String host, final int port, final String serverPrinc) throws IOException, GSSException, PrivilegedActionException {
        super(host, port);
        this.serverPrinc = serverPrinc;
        establishContext();
    }

    public GSSName getPeerName() throws GSSException {
        return context.getTargName();
    }

    private void establishContext() throws GSSException, IOException {
        final Oid krb5Oid = new Oid("1.2.840.113554.1.2.2");
        final GSSManager manager = GSSManager.getInstance();
        final GSSName serverName = manager.createName(serverPrinc, null);
        context = manager.createContext(serverName, krb5Oid, null, GSSContext.DEFAULT_LIFETIME);
        context.requestConf(true);
        context.requestInteg(true);
        final DataInputStream inStream = new DataInputStream(getBasicInputStream());
        final DataOutputStream outStream = new DataOutputStream(getBasicOutputStream());
        byte[] token = new byte[0];
        while (!context.isEstablished()) {
            token = context.initSecContext(token, 0, token.length);
            if (token != null) {
                outStream.writeInt(token.length);
                outStream.write(token);
                outStream.flush();
            }
            if (!context.isEstablished()) {
                token = new byte[inStream.readInt()];
                inStream.readFully(token);
            }
        }
    }
}
