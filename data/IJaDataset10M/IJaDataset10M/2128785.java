package com.googlecode.formomatic;

import com.googlecode.formomatic.bean.Config;
import com.googlecode.formomatic.bean.Rule;
import com.googlecode.formomatic.exception.MalformedConfException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ConfigParser {

    private Config config = new Config();

    public ConfigParser(Object yamlObject) throws MalformedConfException {
        List<Rule> rulesList = new ArrayList();
        Map configMap = (Map) yamlObject;
        if (configMap.containsKey("rules") && (configMap.get("rules") instanceof List)) {
            Iterator it = ((List) configMap.get("rules")).iterator();
            while (it.hasNext()) {
                Object ruleMap = it.next();
                rulesList.add(validateRuleMap(ruleMap));
            }
        } else {
            throw new MalformedConfException("The 'rule' key is wrong or " + "not set in Config File");
        }
    }

    private Rule validateRuleMap(Object obj) throws MalformedConfException {
        Rule rule = null;
        if (obj instanceof Map) {
            Map ruleMap = (Map) obj;
            if (ruleMap.containsKey("name") && ruleMap.containsKey("resource")) {
                rule = new Rule((String) ruleMap.get("name"), (String) ruleMap.get("resource"));
            } else {
                throw new MalformedConfException("The keys name and resource " + "are mandatory for rule definition");
            }
        } else {
            throw new MalformedConfException("Expected java.util.Map but get " + obj.getClass().getName() + " in rule definition");
        }
        return rule;
    }

    public Config getConfig() {
        return this.config;
    }
}
