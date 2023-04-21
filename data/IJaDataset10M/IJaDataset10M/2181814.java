package org.apache.jetspeed.modules.actions;

import java.util.*;
import org.apache.turbine.modules.Action;
import org.apache.turbine.services.resources.TurbineResources;
import org.apache.turbine.util.RunData;
import org.apache.jetspeed.om.security.JetspeedUser;
import org.apache.jetspeed.services.JetspeedSecurity;

public class PrepareScreenEditAccount extends Action {

    public void doPerform(RunData rundata) throws Exception {
        if (!rundata.getUser().hasLoggedIn()) {
            rundata.setScreenTemplate(TurbineResources.getString("services.JspService.screen.error.NotLoggedIn", "Error"));
            return;
        }
        String username = rundata.getUser().getUserName();
        String firstname = null;
        String lastname = null;
        String email = null;
        try {
            JetspeedUser user = JetspeedSecurity.getUser(rundata.getUser().getUserName());
            firstname = (String) user.getFirstName();
            lastname = (String) user.getLastName();
            email = (String) user.getEmail();
            if (firstname == null) firstname = "";
            if (lastname == null) lastname = "";
            if (email == null) email = "";
            Hashtable screenData = new Hashtable();
            screenData.put("username", username);
            screenData.put("firstname", firstname);
            screenData.put("lastname", lastname);
            screenData.put("email", email);
            rundata.getRequest().setAttribute("ScreenDataEditAccount", screenData);
            return;
        } catch (Exception e) {
            rundata.setScreenTemplate(TurbineResources.getString("services.JspService.screen.error.NotLoggedIn", "Error"));
            return;
        }
    }
}
