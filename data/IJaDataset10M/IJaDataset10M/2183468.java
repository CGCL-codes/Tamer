package at.ofai.aaa.agent.jam;

import java.io.Serializable;
import at.ofai.aaa.agent.jam.types.Binding;
import at.ofai.aaa.agent.jam.types.DList;

/**
 * Represents the conditions under which a plan is applicable.
 * @author Marc Huber
 * @author Jaeho Lee
 * @author Stefan Rank
 */
class PlanContext implements Serializable, Condition {

    private DList<Condition> conditions;

    /**  */
    PlanContext() {
        this.conditions = null;
    }

    /**  */
    PlanContext(DList<Condition> cList) {
        this.conditions = cList;
    }

    /**  */
    DList<Condition> addConditions(DList<Condition> cList) {
        for (Condition c : cList) {
            this.conditions.addFirst(c);
        }
        return this.conditions;
    }

    /** Establish contexts (generate the binding list) */
    public boolean check(DList<Binding> bindingList) {
        if (this.conditions == null) {
            return true;
        }
        for (Condition cond : this.conditions) {
            if (!cond.check(bindingList)) break;
        }
        return (bindingList.size() == 0) ? false : true;
    }

    /** Confirm the validity of the current context with the current binding */
    public boolean confirm(Binding b) {
        if (this.conditions == null) {
            return true;
        }
        for (Condition cond : this.conditions) {
            if (!cond.confirm(b)) {
                return false;
            }
        }
        return true;
    }
}
