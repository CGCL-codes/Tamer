package com.lolcode.runtime.action.builder;

import java.util.ArrayList;
import java.util.List;
import com.lolcode.runtime.LOLRuntimeExpression;
import com.lolcode.runtime.RuntimeAction;
import com.lolcode.runtime.action.ExitScope;
import com.lolcode.runtime.action.IfAction;
import com.lolcode.runtime.expression.AccessorExpression;

public class IfActionBuilder {

    private LOLRuntimeExpression expression = new AccessorExpression();

    private List<RuntimeAction> trueActions = new ArrayList<RuntimeAction>();

    private List<LOLRuntimeExpression> maybeExpressions = new ArrayList<LOLRuntimeExpression>();

    private List<List<RuntimeAction>> maybeActions = new ArrayList<List<RuntimeAction>>();

    private List<RuntimeAction> falseActions = new ArrayList<RuntimeAction>();

    private int action_status = IF_ACTIONS;

    private static final int IF_ACTIONS = 0;

    private static final int MAYBE_ACTIONS = 1;

    private static final int FALSE_ACTIONS = 2;

    public IfActionBuilder() {
    }

    public void setIfExpression(LOLRuntimeExpression expression) {
        this.expression = expression;
    }

    public void addAction(RuntimeAction action) {
        switch(action_status) {
            case IF_ACTIONS:
                trueActions.add(action);
                break;
            case FALSE_ACTIONS:
                falseActions.add(action);
                break;
            case MAYBE_ACTIONS:
            default:
                maybeActions.get(maybeExpressions.size() - 1).add(action);
                break;
        }
    }

    public void addMaybeExpression(LOLRuntimeExpression expression) {
        this.action_status = MAYBE_ACTIONS;
        maybeExpressions.add(expression);
        maybeActions.add(new ArrayList<RuntimeAction>());
    }

    public void atFalse() {
        this.action_status = FALSE_ACTIONS;
    }

    public IfAction getFinalAction() {
        trueActions.add(new ExitScope());
        falseActions.add(new ExitScope());
        for (List<RuntimeAction> actionList : maybeActions) {
            actionList.add(new ExitScope());
        }
        return new IfAction(expression, trueActions, falseActions, maybeExpressions, maybeActions);
    }
}
