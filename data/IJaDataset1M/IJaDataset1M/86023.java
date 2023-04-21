package com.hyd.issues.auth;

import com.hyd.issues.AppConfiguration;
import com.hyd.issues.BaseAction;
import com.hyd.issues.json.JsonResult;

/**
 * (description)
 *
 * @author yiding.he
 */
public class ApplicationAction extends BaseAction {

    public JsonResult applicationInitialized() {
        boolean initialized = AppConfiguration.getBoolean("app.configured");
        return new JsonResult("initialized", initialized);
    }
}
