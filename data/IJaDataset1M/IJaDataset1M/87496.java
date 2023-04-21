package org.genos.gmf.resources.generic.ldap;

import org.genos.gmf.Configuration;
import org.genos.gmf.resources.LDAPResourceContainer;
import org.genos.gmf.resources.formatters.RFormatter;
import org.genos.gmf.resources.formatters.VParam;
import org.genos.gmf.resources.formatters.VParamSource.VPARAMSOURCE;

/**
 * LDAPGroupsContainer stores LDAPGroup resources and it is an interface
 * to the system groups (in the LDAP) to retrieve information about them.
 */
public class LDAPGroupsContainer extends LDAPResourceContainer {

    /**
     * Constructor. Basically it's used to define contained LDAPResources.
     * @throws Exception
     */
    public LDAPGroupsContainer() throws Exception {
        super("LDAPGroup");
    }

    /**
	 * Returns a resource description
	 */
    public String getResourceDescription() throws Exception {
        return Configuration.getLocaleString(uid, "s_res_ldapgroupscontainer");
    }

    /**
     * Add virtual params to default formatter.
     * @param f                     Formatter.
     * @throws Exception
     */
    protected void defFormatterParams(RFormatter f) throws Exception {
        super.defFormatterParams(f);
        VParam vp = new VParam();
        vp.defComponent(VPARAMSOURCE.COMPONENT, null, "description");
        f.defVParam("desc", vp);
    }
}
