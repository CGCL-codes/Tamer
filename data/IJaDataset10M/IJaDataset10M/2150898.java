package org.fao.geonet.services.user;

import jeeves.constants.Jeeves;
import jeeves.exceptions.UserNotFoundEx;
import jeeves.interfaces.Service;
import jeeves.resources.dbms.Dbms;
import jeeves.server.ServiceConfig;
import jeeves.server.UserSession;
import jeeves.server.context.ServiceContext;
import jeeves.utils.Util;
import org.fao.geonet.constants.Geonet;
import org.fao.geonet.constants.Params;
import org.jdom.Element;

/** Update the password of logged user
  */
public class PwUpdate implements Service {

    public void init(String appPath, ServiceConfig params) throws Exception {
    }

    public Element exec(Element params, ServiceContext context) throws Exception {
        String password = Util.scramble(Util.getParam(params, Params.PASSWORD));
        String newPassword = Util.scramble(Util.getParam(params, Params.NEW_PASSWORD));
        Dbms dbms = (Dbms) context.getResourceManager().open(Geonet.Res.MAIN_DB);
        UserSession session = context.getUserSession();
        String userId = session.getUserId();
        if (userId == null) throw new UserNotFoundEx(null);
        Element elUser = dbms.select("SELECT * FROM Users " + "WHERE id=" + userId + " AND password='" + password + "'");
        if (elUser.getChildren().size() == 0) throw new UserNotFoundEx(userId);
        dbms.execute("UPDATE Users SET password=? WHERE id=?", newPassword, new Integer(userId));
        return new Element(Jeeves.Elem.RESPONSE);
    }
}
