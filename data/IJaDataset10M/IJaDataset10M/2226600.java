package com.monad.homerun.rule;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.monad.homerun.base.Value;
import com.monad.homerun.core.GlobalProps;
import com.monad.homerun.core.Trace;
import com.monad.homerun.model.time.TimeModel;
import com.monad.homerun.event.Event;

/**
 * Task class represents a single action part, i.e. the smallest
 * unit of work we can perform. Effectively, it means the exercise
 * of a single control on a single object.
 */
public class Task implements Serializable {

    private static final long serialVersionUID = -8955463131256912636L;

    public static final String CTRL_TYPE = "control";

    public static final String INFORM_TYPE = "inform";

    public static final String RULE_TYPE = "rule";

    private boolean alternate = false;

    private String note = null;

    private String domain = null;

    private String target = null;

    private String type = null;

    private String control = null;

    private String verb = null;

    private Map<String, String> modifiers = null;

    public Task() {
    }

    public Task(String domain, String target, String type, String control, String verb, Map<String, String> modifiers) {
        this.domain = domain;
        this.target = target;
        this.type = type;
        this.control = control;
        this.verb = verb;
        this.modifiers = modifiers;
    }

    public Task(Task task) {
        this.domain = task.domain;
        this.target = task.target;
        this.type = task.type;
        this.control = task.control;
        this.verb = task.verb;
        this.modifiers = task.modifiers;
    }

    public static Task clone(Task task) {
        return new Task(task);
    }

    public boolean isAlternate() {
        return alternate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDomain() {
        return domain;
    }

    public String getTarget() {
        return target;
    }

    public String getType() {
        return type;
    }

    public String getControl() {
        return control;
    }

    public String getVerb() {
        return verb;
    }

    public Iterator<String> getModifierNames() {
        return modifiers == null ? null : modifiers.keySet().iterator();
    }

    public String getModifier(String name) {
        return modifiers != null ? modifiers.get(name) : null;
    }

    public String toString() {
        return type + ":" + control + ":" + verb + " " + domain + ":" + target;
    }

    public boolean validate(Map<String, Object> context) {
        boolean valid = false;
        Invoker invoker = (Invoker) context.get("invoker");
        Trace trace = (Trace) context.get("trace");
        if ("rule".equals(type)) {
            valid = invoker.ruleExists(target);
        } else {
            valid = invoker.objectExists(domain, target);
        }
        if (trace != null) {
            if (!valid) {
                trace.setDesc("invalid target: '" + target + "'");
            } else {
                trace.setDesc("ok");
            }
        }
        return valid;
    }

    public boolean perform(Map<String, Object> context) {
        boolean ret = false;
        Invoker invoker = (Invoker) context.get("invoker");
        Trace trace = (Trace) context.get("trace");
        if (invoker == null) {
            if (trace != null) {
                trace.setDesc(ret + ": no invoker!");
            }
            return ret;
        }
        if ("rule".equals(type)) {
            Rule embedRule = invoker.getRule(domain, target);
            if (trace == null) {
                context.put("mode", verb);
                ret = invoker.applyRule(embedRule, context);
            } else {
                RuleTrace rtrace = (RuleTrace) trace;
                RuleTrace subTrace = invoker.traceRule(embedRule, rtrace);
                ret = subTrace.getResult();
                rtrace.addSubTrace(subTrace);
            }
        } else if (type.startsWith("inform")) {
            String modelType = type.substring(type.indexOf(":") + 1);
            Event informEvent = null;
            if ("state".equals(modelType)) {
                Value state = new Value(modifiers.get("state"), System.currentTimeMillis());
                informEvent = new Event(state, "rule");
            } else if ("number".equals(modelType)) {
                informEvent = new Event(verb, "rule");
            } else if ("time".equals(modelType)) {
                if (TimeModel.STAMP.equals(verb)) {
                    Value now = new Value(System.currentTimeMillis());
                    informEvent = new Event(now, "rule");
                }
            } else if ("value".equals(modelType)) {
            }
            invoker.informModel(domain, target, control, informEvent);
            ret = true;
            if (trace != null) {
                trace.setDesc(ret + ": " + "informing " + target + " model " + control);
            }
        } else {
            context.put("agent", "task");
            if (trace != null && RuleTrace.INTERACTIVE_MODE == ((RuleTrace) trace).getMode()) {
                context.put("itrace", "true");
            }
            Map<String, String> varMap = (Map<String, String>) context.get("bindingProps");
            if (varMap != null && modifiers != null) {
                filterModifiers(varMap);
            }
            ret = invoker.controlObject(domain, target, control, verb, modifiers, context);
            if (trace != null) {
                trace.setDesc(ret + ": [" + control + "] " + verb + " " + target);
            }
        }
        return ret;
    }

    public void addTemplateVars(Map<String, Object> context) {
        if ("rule".equals(type)) {
            Invoker invoker = (Invoker) context.get("invoker");
            if (invoker != null) {
                Rule embedRule = invoker.getRule(domain, target);
                if (embedRule != null) {
                    embedRule.getBindingVariables(context);
                }
            }
        } else if (!type.startsWith("inform")) {
            List<String> varList = (List<String>) context.get("varList");
            if (varList != null) {
                for (String name : modifiers.keySet()) {
                    String val = modifiers.get(name);
                    if (val.startsWith("$")) {
                        val = val.substring(1);
                        if (!varList.contains(val)) {
                            varList.add(val);
                        }
                    }
                }
            }
        }
    }

    private void filterModifiers(Map<String, String> varMap) {
        for (String name : modifiers.keySet()) {
            String val = modifiers.get(name);
            if (GlobalProps.DEBUG) {
                System.out.println("task filterMode - name: " + name + " val: " + val);
                for (String key : varMap.keySet()) {
                    System.out.println("task FM - key: " + key + " val: " + varMap.get(key));
                }
            }
            if (val.startsWith("$")) {
                String sval = val.substring(1);
                if (varMap.containsKey(sval)) {
                    if (GlobalProps.DEBUG) {
                        System.out.println("task filterMode - adding key: " + name + " val: " + varMap.get(sval));
                    }
                    modifiers.put(name, varMap.get(sval));
                }
            }
        }
    }
}
