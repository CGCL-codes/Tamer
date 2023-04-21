package com.googlecode.jplurk.behavior;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.googlecode.jplurk.Constants;
import com.googlecode.jplurk.net.Request;
import com.googlecode.jplurk.utils.TimeUtil;

public class GetPlurks implements IBehavior {

    static Log logger = LogFactory.getLog(GetPlurks.class);

    @Override
    public boolean action(Request params, Object arg) {
        params.setEndPoint(Constants.GET_PLURK_URL);
        params.addParam("user_id", params.getUserUId());
        if (arg != null && arg instanceof String) {
            if (TimeUtil.isValidJsOutputFormat("" + arg)) {
                logger.info("apply offset: " + arg);
                params.addParam("offset", "\"" + arg + "\"");
            }
        }
        return true;
    }
}
