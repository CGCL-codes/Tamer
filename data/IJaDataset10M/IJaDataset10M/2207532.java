package org.hsnr.didac.modules.screens.preferences;

import org.hsnr.didac.modules.screens.DidacSecureScreen;
import org.apache.turbine.util.RunData;
import org.apache.velocity.context.Context;
import org.apache.torque.util.Criteria;
import org.hsnr.didac.om.TurbineUser;
import org.hsnr.didac.om.TurbineUserPeer;

public class AddressPhoneForm extends DidacSecureScreen {

    public void doBuildTemplate(RunData data, Context context) {
        super.doBuildTemplate(data, context);
        try {
            Criteria crit = new Criteria();
            crit.add(TurbineUserPeer.USER_ID, data.getUser().getId());
            TurbineUser user = (TurbineUser) TurbineUserPeer.doSelect(crit).get(0);
            context.put("user", user);
        } catch (Exception ex) {
            data.setMessage(ex.toString());
        }
    }
}
