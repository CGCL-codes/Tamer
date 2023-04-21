package org.marcont.rulegenerator.mobile;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import org.marcont.rulegenerator.model.Call;
import org.marcont.rulegenerator.model.Consequent;
import org.marcont.rulegenerator.model.NodeType;
import org.marcont.rulegenerator.model.Parameter;
import org.marcont.rulegenerator.model.Premise;
import org.marcont.rulegenerator.model.Rule;
import org.marcont.rulegenerator.model.TranslationRules;

/**
 *
 * @author Piotr Piotrowski
 */
public class MobileModel {

    private TranslationRules translation;

    /**
     * Creates a new instance of MobileModel 
     */
    public MobileModel() {
        translation = new TranslationRules();
    }

    public String[] getRules() {
        List<Rule> rules = translation.getRules();
        String[] ret = new String[rules.size()];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = rules.get(i).getName();
        }
        return ret;
    }

    public String addRule() {
        String name = getUniqueRuleName();
        translation.addRule(new Rule(name, false));
        return name;
    }

    public boolean delRule(int index) {
        return translation.removeRule(translation.getRules().get(index).getName());
    }

    public byte[] selRule(int index) {
        Rule rule = translation.getRules().get(index);
        try {
            ByteArrayOutputStream ba = new ByteArrayOutputStream();
            DataOutputStream os = new DataOutputStream(ba);
            os.writeUTF(rule.getName());
            os.writeBoolean(rule.isTerminate());
            os.writeInt(rule.getPremises().size());
            for (int i = 0; i < rule.getPremises().size(); i++) {
                Premise p = rule.getPremises().get(i);
                os.writeUTF(p.getSubject());
                os.writeUTF(p.getPredicate());
                os.writeUTF(p.getObject());
                os.writeUTF(p.getObjectDatatype());
                os.writeUTF(p.getObjectXmlLang());
                os.writeUTF(p.getObjectType().toString());
                os.writeBoolean(p.isSubjectRegexp());
                os.writeBoolean(p.isPredicateRegexp());
                os.writeBoolean(p.isObjectRegexp());
            }
            os.writeInt(rule.getConsequents().size());
            for (int i = 0; i < rule.getConsequents().size(); i++) {
                Consequent c = rule.getConsequents().get(i);
                os.writeUTF(c.getSubject());
                os.writeUTF(c.getPredicate());
                os.writeUTF(c.getObject());
                os.writeUTF(c.getObjectDatatype());
                os.writeUTF(c.getObjectXmlLang());
                os.writeUTF(c.getObjectType().toString());
            }
            os.writeInt(rule.getCalls().size());
            for (int i = 0; i < rule.getCalls().size(); i++) {
                Call c = rule.getCalls().get(i);
                os.writeUTF(c.getRule().getName());
                os.writeInt(c.getParams().size());
                for (int j = 0; j < c.getParams().size(); j++) {
                    Parameter p = c.getParams().get(j);
                    os.writeUTF(p.getName());
                    os.writeUTF(p.getValue());
                }
            }
            os.flush();
            return ba.toByteArray();
        } catch (IOException ex) {
            return null;
        }
    }

    public boolean modRule(String oldName, String newName, boolean isTerminate) {
        if (translation.containsRule(newName) && !newName.equals(oldName)) {
            return false;
        } else {
            translation.changeRule(translation.getRule(oldName), new Rule(newName, isTerminate));
            return true;
        }
    }

    public boolean addPremise(String ruleName) {
        Rule rule = translation.getRule(ruleName);
        rule.addPremise(new Premise());
        return true;
    }

    public boolean delPremise(String ruleName, int index) {
        Rule rule = translation.getRule(ruleName);
        return rule.removePremise(rule.getPremises().get(index));
    }

    public boolean modPremise(String ruleName, int index, String subject, String predicate, String object, boolean[] regexp, String datatype, String lang, String objectType) {
        Rule rule = translation.getRule(ruleName);
        boolean ret = true;
        ret &= rule.validatePremise(rule.getPremises().get(index), subject);
        ret &= rule.validatePremise(rule.getPremises().get(index), predicate);
        ret &= rule.validatePremise(rule.getPremises().get(index), object);
        if (!ret) {
            return false;
        }
        Premise p = new Premise();
        p.setSubject(subject);
        p.setPredicate(predicate);
        p.setObject(object);
        p.setSubjectRegexp(regexp[0]);
        p.setPredicateRegexp(regexp[1]);
        p.setObjectRegexp(regexp[2]);
        p.setObjectDatatype(datatype);
        p.setObjectXmlLang(lang);
        p.setObjectType(NodeType.get(objectType));
        rule.changePremise(rule.getPremises().get(index), p);
        return true;
    }

    public boolean addConsequent(String ruleName) {
        Rule rule = translation.getRule(ruleName);
        rule.addConsequent(new Consequent());
        return true;
    }

    public boolean delConsequent(String ruleName, int index) {
        Rule rule = translation.getRule(ruleName);
        return rule.removeConsequent(rule.getConsequents().get(index));
    }

    public boolean modConsequent(String ruleName, int index, String subject, String predicate, String object, String datatype, String lang, String objectType) {
        Rule rule = translation.getRule(ruleName);
        boolean ret = true;
        ret &= rule.validateConsequent(rule.getConsequents().get(index), subject);
        ret &= rule.validateConsequent(rule.getConsequents().get(index), predicate);
        ret &= rule.validateConsequent(rule.getConsequents().get(index), object);
        if (!ret) {
            return false;
        }
        Consequent c = new Consequent();
        c.setSubject(subject);
        c.setPredicate(predicate);
        c.setObject(object);
        c.setObjectDatatype(datatype);
        c.setObjectXmlLang(lang);
        c.setObjectType(NodeType.get(objectType));
        rule.changeConsequent(rule.getConsequents().get(index), c);
        return true;
    }

    public boolean addCall(String ruleName) {
        Rule rule = translation.getRule(ruleName);
        Call call = new Call();
        call.setRule(rule);
        rule.addCall(call);
        return true;
    }

    public boolean delCall(String ruleName, int index) {
        Rule rule = translation.getRule(ruleName);
        return rule.removeCall(rule.getCalls().get(index));
    }

    public boolean modCall(String ruleName, int index, String calledRule) {
        Rule rule = translation.getRule(ruleName);
        return rule.changeCall(rule.getCalls().get(index), translation.getRule(calledRule));
    }

    public String addParam(String ruleName, int callIndex) {
        Call call = translation.getRule(ruleName).getCalls().get(callIndex);
        Parameter param = new Parameter();
        param.setName(getUniqueParamName(call));
        call.addParameter(param);
        return param.getName();
    }

    public boolean delParam(String ruleName, int callIndex, int index) {
        Call call = translation.getRule(ruleName).getCalls().get(callIndex);
        return call.removeParameter(call.getParams().get(index));
    }

    public boolean modParam(String ruleName, int callIndex, int index, String name, String value) {
        Call call = translation.getRule(ruleName).getCalls().get(callIndex);
        Parameter p = new Parameter();
        p.setName(name);
        p.setValue(value);
        return call.changeParameter(call.getParams().get(index), p);
    }

    /**
     * Generates a unique rule name
     * @return unique rule name
     */
    private String getUniqueRuleName() {
        String base = "New rule";
        String newName = base;
        for (int i = 1; translation.containsRule(newName); i++) {
            newName = base + i;
        }
        return newName;
    }

    /**
     * Generates a unique param name in a call
     * @return unique param name
     */
    private String getUniqueParamName(Call call) {
        String base = "New parameter";
        String newName = base;
        for (int i = 1; call.containsParam(newName); i++) {
            newName = base + i;
        }
        return newName;
    }
}
