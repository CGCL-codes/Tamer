package com.aimluck.eip.modules.screens;

import net.sf.json.JSONArray;
import org.apache.jetspeed.services.logging.JetspeedLogFactoryService;
import org.apache.jetspeed.services.logging.JetspeedLogger;
import org.apache.turbine.util.RunData;
import org.apache.velocity.context.Context;
import com.aimluck.eip.addressbookuser.util.AddressBookUserUtils;
import com.aimluck.eip.util.ALEipUtils;

/**
 * 社外アドレスユーザーの情報をJSONデータとして出力するクラスです。 <br />
 * 
 */
public class AddressBookUserLiteJSONScreen extends ALJSONScreen {

    /** logger */
    private static final JetspeedLogger logger = JetspeedLogFactoryService.getLogger(AddressBookUserLiteJSONScreen.class.getName());

    @Override
    protected String getJSONString(RunData rundata, Context context) throws Exception {
        String result = "";
        JSONArray json;
        try {
            String mode = rundata.getParameters().getString("mode");
            if ("group".equals(mode)) {
                String groupname = rundata.getParameters().getString("groupname");
                json = JSONArray.fromObject(AddressBookUserUtils.getAddressBookUserLiteBeansFromGroup(groupname, ALEipUtils.getUserId(rundata)));
            } else {
                json = new JSONArray();
            }
            result = json.toString();
        } catch (Exception e) {
            logger.error("[UserLiteJSONScreen]", e);
        }
        return result;
    }
}
