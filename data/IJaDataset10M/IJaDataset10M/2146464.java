package com.bluestone;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import com.bluestone.config.ActionConfig;
import com.bluestone.scripts.ActionScript;

public class ObjectFactory implements Serializable {

    public ObjectFactory() {
    }

    public Object buildAction(String actionName, ActionScript action, ActionConfig config) throws Exception {
        return null;
    }
}
