package org.jowidgets.impl.command;

import org.jowidgets.api.command.IAction;
import org.jowidgets.api.command.IExecutionContext;
import org.jowidgets.api.command.IExecutionContextValues;
import org.jowidgets.api.widgets.IWidget;
import org.jowidgets.util.ITypedKey;

public final class ExecutionContext implements IExecutionContext {

    private final IAction action;

    private final IWidget source;

    private final IExecutionContextValues executionContextValues;

    public ExecutionContext(final IAction action, final IWidget source) {
        this(action, source, null);
    }

    public ExecutionContext(final IAction action, final IWidget source, final IExecutionContextValues executionContextValues) {
        super();
        this.executionContextValues = executionContextValues;
        this.action = action;
        this.source = source;
    }

    @Override
    public IAction getAction() {
        return action;
    }

    @Override
    public IWidget getSource() {
        return source;
    }

    @Override
    public <VALUE_TYPE> VALUE_TYPE getValue(final ITypedKey<VALUE_TYPE> key) {
        if (executionContextValues != null) {
            return executionContextValues.getValue(key);
        }
        return null;
    }
}
