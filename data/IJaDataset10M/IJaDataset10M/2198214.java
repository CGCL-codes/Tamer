package org.openliberty.arisid.provider.opends;

import org.opends.server.protocols.internal.InternalClientConnection;
import org.opends.server.types.AuthenticationInfo;
import org.opends.server.types.DN;
import org.opends.server.types.DirectoryException;
import org.openliberty.arisid.provider.ldapMapper.ArisIdPrincipal;

public class DsPrincipal extends ArisIdPrincipal {

    AuthenticationInfo authInfo;

    InternalClientConnection con = null;

    public DsPrincipal() {
        InternalClientConnection con = DsManager.getRootConnection();
        this.authInfo = con.getAuthenticationInfo();
        this.con = con;
    }

    public DsPrincipal(AuthenticationInfo authInfo) {
        this.authInfo = authInfo;
        this.con = new InternalClientConnection(authInfo);
    }

    public DsPrincipal(String name) throws DirectoryException {
        DN userDN = DN.decode(name);
        this.con = new InternalClientConnection(userDN);
        this.authInfo = this.con.getAuthenticationInfo();
    }

    @Override
    public String getName() {
        return this.authInfo.getAuthenticationDN().toString();
    }

    @Override
    public String getPrimaryContext() {
        return getName();
    }

    public AuthenticationInfo getAuthenticationInfo() {
        return this.authInfo;
    }

    public InternalClientConnection getConnection() {
        return this.con;
    }
}
