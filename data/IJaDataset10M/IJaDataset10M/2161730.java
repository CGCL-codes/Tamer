package com.bluemarsh.jswat.breakpoint;

import com.bluemarsh.jswat.breakpoint.ui.ConditionUI;
import com.bluemarsh.jswat.breakpoint.ui.ExprConditionUI;
import com.bluemarsh.jswat.expr.EvaluationException;
import com.bluemarsh.jswat.expr.Evaluator;
import com.sun.jdi.BooleanValue;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.LocatableEvent;
import java.util.prefs.Preferences;

/**
 * Class ExprCondition implements a breakpoint conditional that is
 * satisfied when a given expression evaluates to true.
 *
 * @author  Nathan Fiedler
 */
public class ExprCondition implements Condition {

    /** Expression to test. */
    private String expression;

    /**
     * Default constructor for deserialization.
     */
    ExprCondition() {
    }

    /**
     * Constructs a ExprCondition that is satisfied when the given
     * expression evaluates to true.
     *
     * @param  expr  boolean expression to evaluate.
     */
    public ExprCondition(String expr) {
        expression = expr;
    }

    /**
     * Retrieves the expression this condition tests for.
     *
     * @return  expression this condition tests for.
     */
    public String getExprString() {
        return expression;
    }

    /**
     * Returns the user interface widget for customizing this condition.
     *
     * @return  Condition user interface adapter.
     */
    public ConditionUI getUIAdapter() {
        return new ExprConditionUI(this);
    }

    /**
     * Returns true if this condition is satisfied.
     *
     * @param  event  JDI Event that brought us here.
     * @return  true if satisfied, false otherwise.
     * @throws  Exception
     *          because anything can happen.
     */
    public boolean isSatisfied(Event event) throws Exception {
        if (!(event instanceof LocatableEvent)) {
            return false;
        }
        ThreadReference thread = ((LocatableEvent) event).thread();
        if (thread == null || !thread.isSuspended() || !thread.isAtBreakpoint()) {
            return false;
        }
        Evaluator eval = new Evaluator(expression);
        Object o = eval.evaluate(thread, 0);
        if (o instanceof BooleanValue) {
            return ((BooleanValue) o).value();
        } else if (o instanceof Boolean) {
            return ((Boolean) o).booleanValue();
        } else {
            throw new EvaluationException(Bundle.getString("condition.notBoolean"));
        }
    }

    /**
     * Reads the condition properties from the given preferences node.
     *
     * @param  prefs  Preferences node from which to initialize this
     *                condition.
     * @return  true if successful, false otherwise.
     */
    public boolean readObject(Preferences prefs) {
        expression = prefs.get("expression", null);
        return expression != null;
    }

    /**
     * Sets the expression this condition tests for.
     *
     * @param  expr  new expression to test for.
     */
    public void setExprString(String expr) {
        expression = expr;
    }

    /**
     * Returns a string representation of this.
     *
     * @return  String representing this.
     */
    public String toString() {
        return "ExprCondition=[" + expression + "]";
    }

    /**
     * Writes the condition properties to the given preferences node.
     * It is assumed that the preferences node is completely empty.
     *
     * @param  prefs  Preferences node to which to serialize this
     *                condition.
     * @return  true if successful, false otherwise.
     */
    public boolean writeObject(Preferences prefs) {
        prefs.put("class", this.getClass().getName());
        prefs.put("expression", expression);
        return true;
    }
}
