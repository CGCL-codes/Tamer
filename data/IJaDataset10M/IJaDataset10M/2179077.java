package de.schlund.pfixcore.auth.conditions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import de.schlund.pfixcore.auth.Condition;
import de.schlund.pfixcore.workflow.Context;

/**
 * 
 * @author mleidig@schlund.de
 * 
 */
public abstract class ConditionGroup implements Condition {

    protected List<Condition> conditions;

    public ConditionGroup() {
        conditions = new ArrayList<Condition>();
    }

    public ConditionGroup(Condition... conditions) {
        this();
        for (Condition condition : conditions) this.conditions.add(condition);
    }

    public void add(Condition condition) {
        conditions.add(condition);
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public abstract boolean evaluate(Context context);

    public abstract String getOperatorString();

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("( ");
        Iterator<Condition> it = conditions.iterator();
        while (it.hasNext()) {
            Condition condition = it.next();
            sb.append(condition);
            if (it.hasNext()) sb.append(" " + getOperatorString() + " ");
        }
        sb.append(" )");
        return sb.toString();
    }
}
