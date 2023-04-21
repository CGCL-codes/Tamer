package com.psm.core.plugin;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.AbstractUrlHandlerMapping;

public class PluginUrlMapping extends AbstractUrlHandlerMapping {

    protected Logger logger = Logger.getLogger(this.getClass());

    private Map<String, String> pagineStandard = new HashMap<String, String>();

    public Map<String, String> getPagineStandard() {
        return pagineStandard;
    }

    public void setPagineStandard(Map<String, String> pagineStandard) {
        this.pagineStandard = pagineStandard;
    }

    public void registraUrl(String url, String idController) {
        logger.info("Added url : " + url);
        registerHandler(url, idController);
    }

    public void init() {
        for (String key : pagineStandard.keySet()) {
            registraUrl(key, pagineStandard.get(key));
        }
    }
}
