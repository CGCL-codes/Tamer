package com.agentfactory.agentspeak.interpreter.mentalstate;

import java.util.LinkedList;
import java.util.List;
import com.agentfactory.agentspeak.interpreter.Utilities;
import com.agentfactory.clf.lang.Predicate;
import com.agentfactory.clf.plans.IPlanStep;
import com.agentfactory.clf.reasoner.Bindings;

public class PartialPlan {

    private List<IPlanStep> plan;

    private Predicate label;

    public PartialPlan(Predicate label, List<IPlanStep> plan) {
        this.label = label;
        this.plan = plan;
    }

    public Predicate getLabel() {
        return label;
    }

    public List<IPlanStep> getPlan() {
        return plan;
    }

    public PartialPlan apply(Bindings bindings) {
        List<IPlanStep> list = new LinkedList<IPlanStep>();
        for (IPlanStep step : plan) {
            list.add(step.apply(bindings));
        }
        return new PartialPlan((Predicate) label.apply(bindings), list);
    }

    public String toString() {
        String output = "@" + Utilities.presenter.toString(label) + " <-";
        boolean first = true;
        for (IPlanStep step : plan) {
            if (first) first = false; else output += ",";
            output += "\n" + step.toString(IPlanStep.TAB);
        }
        return output + ";";
    }
}
