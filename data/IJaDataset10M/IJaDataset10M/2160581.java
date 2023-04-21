package com.agentfactory.astr.interpreter;

import com.agentfactory.clf.interpreter.AbstractLanguageAgent;
import com.agentfactory.clf.lang.IFormula;
import com.agentfactory.clf.lang.Predicate;
import com.agentfactory.clf.lang.Unifier;
import com.agentfactory.clf.plans.BlockStep;
import com.agentfactory.clf.plans.IPlanStep;
import com.agentfactory.clf.plans.PlanStack;
import com.agentfactory.clf.reasoner.Bindings;

public class LabelledPlanStep implements IPlanStep {

    protected IFormula step;

    public LabelledPlanStep(IFormula step) {
        this.step = step;
    }

    @Override
    public IPlanStep apply(Bindings set) {
        return new LabelledPlanStep(step.apply(set));
    }

    public IFormula formula() {
        return step;
    }

    public String toString() {
        return toString("");
    }

    public String toString(String tabs) {
        return tabs + "@" + Utilities.presenter.toString(step);
    }

    @Override
    public void handle(AbstractLanguageAgent agent, PlanStack planStack) {
        AbstractASTRAgent agt = (AbstractASTRAgent) agent;
        Predicate predicate = (Predicate) step.apply(planStack.bindings());
        PartialPlan plan = agt.getPartialPlan(predicate);
        if (plan == null) {
            planStack.fail();
            return;
        } else {
            Bindings bindings = Unifier.unifyFormulae(predicate, plan.getLabel());
            if (bindings == null) {
                planStack.fail();
            } else {
                planStack.addPlanStep(new BlockStep(plan.apply(bindings).getPlan()));
            }
        }
    }
}
