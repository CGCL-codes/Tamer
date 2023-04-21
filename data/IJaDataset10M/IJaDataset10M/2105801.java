package org.genos.gmf.form;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import org.genos.gmf.Configuration;
import org.genos.gmf.security.GroupMgmt;
import org.genos.gmf.security.UserMapping;

/**
 * Parameter to select a group of users.
 */
public class ParameterTypeSelectGroup extends ParameterTypeSelectExt {

    /**
	 * If true, the values of the select are short dns, not fdns
	 */
    protected boolean sdn = false;

    /**
     * Populated flag.
     */
    private boolean populated = false;

    /**
     * Default constructor.
     * @throws Exception
     */
    public ParameterTypeSelectGroup() throws Exception {
        super();
    }

    /**
     * Sets the flag sdn
     * @param f		New value for the flag
     */
    public void setShortDns(boolean f) {
        sdn = f;
    }

    /**
	 * Populate select field with a list of available groups.
	 */
    protected void populateSourceOptions(Connection conn) throws SQLException, Exception {
        if (populated) return;
        String dn;
        String caption;
        Map groups = GroupMgmt.getAllGroups();
        Iterator it = groups.keySet().iterator();
        while (it.hasNext()) {
            dn = (String) it.next();
            Map m = (Map) groups.get(dn);
            ArrayList values = (ArrayList) m.get(Configuration.ldapgroupdisplayattr);
            if (values != null && values.size() > 0) caption = (String) values.get(0); else caption = UserMapping.getShortDN(dn);
            if (sdn) addSelectOption(UserMapping.getShortDN(dn), caption); else addSelectOption(dn, caption);
        }
        populated = true;
    }
}
