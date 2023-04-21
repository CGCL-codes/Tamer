package org.campware.dream.modules.screens;

import org.apache.torque.util.Criteria;
import org.apache.velocity.context.Context;
import org.campware.dream.om.TurbineUser;
import org.campware.dream.om.TurbineUserPeer;
import org.campware.dream.om.TurbineRole;
import org.campware.dream.om.TurbineRolePeer;
import org.campware.dream.om.DreamUser;
import org.campware.dream.om.DreamUserPeer;

/**
 * To read comments for this class, please see
 * the ancestor class
 */
public class UserForm extends CreamForm {

    protected void initScreen() {
        setModuleType(LOOKUP);
        setModuleName("TURBINE_USER");
        setIdName(TurbineUserPeer.USER_ID);
        setFormIdName("userid");
    }

    protected boolean getEntry(Criteria criteria, Context context) {
        try {
            TurbineUser entry = (TurbineUser) TurbineUserPeer.doSelect(criteria).get(0);
            Criteria prefcrit = new Criteria();
            prefcrit.add(DreamUserPeer.LOGIN_NAME, entry.getLoginName());
            DreamUser prefs = (DreamUser) DreamUserPeer.doSelect(prefcrit).get(0);
            context.put("entry", entry);
            context.put("entryitems", entry.getTurbineUserGroupRoles());
            context.put("prefs", prefs);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    protected boolean getNew(Context context) {
        try {
            TurbineUser entry = new TurbineUser();
            DreamUser prefs = new DreamUser();
            context.put("entry", entry);
            context.put("prefs", prefs);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    protected boolean getLookups(Context context) {
        try {
            Criteria rolecrit = new Criteria();
            rolecrit.add(TurbineRolePeer.ROLE_ID, 999, Criteria.GREATER_THAN);
            rolecrit.addAscendingOrderByColumn(TurbineRolePeer.ROLE_NAME);
            context.put("roles", TurbineRolePeer.doSelect(rolecrit));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
