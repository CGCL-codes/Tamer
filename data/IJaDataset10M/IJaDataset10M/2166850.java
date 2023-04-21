package org.fao.geonet.services.user;

import jeeves.constants.Jeeves;
import jeeves.interfaces.Service;
import jeeves.resources.dbms.Dbms;
import jeeves.server.ServiceConfig;
import jeeves.server.context.ServiceContext;
import jeeves.utils.Util;
import org.fao.geonet.constants.Geonet;
import org.fao.geonet.constants.Params;
import org.fao.geonet.services.reusable.Reject;
import org.fao.geonet.services.reusable.ReusableTypes;
import org.jdom.Element;

/** Removes a user from the system. It removes the relationship to a group too
  */
public class Remove implements Service {

    public void init(String appPath, ServiceConfig params) throws Exception {
    }

    public Element exec(Element params, ServiceContext context) throws Exception {
        final String PROFILE = "profile";
        String id = Util.getParam(params, Params.ID);
        Dbms dbms = (Dbms) context.getResourceManager().open(Geonet.Res.MAIN_DB);
        Element profileQueryResult = dbms.select("SELECT " + PROFILE + " FROM users where id=?", Integer.parseInt(id));
        if (profileQueryResult.getChild("record") == null) {
            return new Element(Jeeves.Elem.RESPONSE);
        }
        final Element profile = profileQueryResult.getChild("record").getChild(PROFILE);
        SharedObjectSecurity.checkPermitted(profile, context.getUserSession().getProfile());
        if (profile == null || !profile.getTextTrim().equalsIgnoreCase(Geonet.Profile.SHARED)) {
            dbms.execute("DELETE FROM UserGroups WHERE userId=?", Integer.parseInt(id));
            dbms.execute("DELETE FROM Users      WHERE     id=?", Integer.parseInt(id));
        } else {
            new Reject().reject(context, ReusableTypes.contacts, new String[] { id }, "", null);
        }
        return new Element(Jeeves.Elem.RESPONSE);
    }
}
