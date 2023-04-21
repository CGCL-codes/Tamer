package com.agentfactory.teleoreactive.interpreter.mentalstate;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import com.agentfactory.clf.lang.IFormula;
import com.agentfactory.clf.lang.IMPLIES;
import com.agentfactory.clf.lang.Predicate;
import com.agentfactory.clf.reasoner.IQueryable;

public class RuleBase implements IQueryable {

    private Map<String, List<IFormula>> ruleMap;

    private List<IMPLIES> rules;

    public RuleBase() {
        ruleMap = new HashMap<String, List<IFormula>>();
        rules = new LinkedList<IMPLIES>();
    }

    public boolean hasType(String type) {
        return "raw-logic".equals(type);
    }

    public void addRule(IMPLIES rule) {
        rules.add(rule);
        String key = ((Predicate) rule.result()).functor();
        List<IFormula> list = ruleMap.get(key);
        if (list == null) {
            list = new LinkedList<IFormula>();
            list.add(rule);
            ruleMap.put(key, list);
        } else {
            for (IFormula i : list) {
                if (i.equals(rule)) return;
            }
            list.add(rule);
            ruleMap.put(key, list);
        }
    }

    public List<IFormula> query(IFormula query) {
        String key = ((Predicate) query).functor();
        List<IFormula> list = ruleMap.get(key);
        if (list != null) return list;
        return new LinkedList<IFormula>();
    }

    public List<IMPLIES> getRules() {
        return rules;
    }
}
