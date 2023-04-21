package com.dfruits.logging.scriptbindings;

import java.util.HashMap;
import java.util.Map;
import net.java.custos.scripting.ScriptEngineBindingsFactory;
import com.dfruits.logging.LogService;
import com.dfruits.logging.LogServicePlugin;

public class LogBindingsFactory implements ScriptEngineBindingsFactory {

    public boolean canHandle(String engineName) {
        return true;
    }

    public Map<String, Object> createBindings() {
        Map<String, Object> ret = new HashMap<String, Object>();
        LogService log = LogServicePlugin.getDefault().getLogService();
        ret.put("$log", log);
        return ret;
    }
}
