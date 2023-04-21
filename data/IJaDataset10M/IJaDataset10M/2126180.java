package org.kisst.cordys.caas;

import org.kisst.cordys.caas.support.LdapObject;
import org.kisst.cordys.caas.support.LdapObjectBase;

public class AuthenticatedUser extends LdapObjectBase {

    public final RefProperty<Organization> defaultOrg = new RefProperty<Organization>("defaultcontext");

    public final StringList osidentity = new StringList("osidentity");

    protected AuthenticatedUser(LdapObject parent, String dn) {
        super(parent, dn);
    }

    @Override
    protected String prefix() {
        return "au";
    }
}
