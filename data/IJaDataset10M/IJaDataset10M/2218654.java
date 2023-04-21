package org.campware.cream.modules.screens;

import org.apache.torque.util.Criteria;
import org.apache.velocity.context.Context;
import org.campware.cream.om.TurbineRole;
import org.campware.cream.om.TurbineRolePeer;

/**
 * To read comments for this class, please see
 * the ancestor class
 */
public class RoleForm extends CreamForm {

    protected void initScreen() {
        setModuleType(LOOKUP);
        setModuleName("TURBINE_ROLE");
        setIdName(TurbineRolePeer.ROLE_ID);
        setFormIdName("roleid");
    }

    protected boolean getEntry(Criteria criteria, Context context) {
        try {
            TurbineRole entry = (TurbineRole) TurbineRolePeer.doSelect(criteria).get(0);
            context.put("entry", entry);
            context.put("entryitems", entry.getTurbineRolePermissions());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    protected boolean getNew(Context context) {
        try {
            TurbineRole entry = new TurbineRole();
            context.put("entry", entry);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
