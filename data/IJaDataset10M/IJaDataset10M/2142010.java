package org.jowidgets.impl.command;

import org.jowidgets.api.command.IAction;
import org.jowidgets.api.command.IExceptionHandler;
import org.jowidgets.api.command.IExecutionContext;
import org.jowidgets.tools.command.DefaultExceptionHandler;

public class ActionExecuter {

    private final IAction action;

    private final IActionWidget actionWidget;

    private final IExceptionHandler defaultExceptionHandler;

    public ActionExecuter(final IAction action, final IActionWidget actionWidget) {
        super();
        this.action = action;
        this.actionWidget = actionWidget;
        this.defaultExceptionHandler = new DefaultExceptionHandler();
    }

    public void execute() {
        if (action.isEnabled()) {
            try {
                executeCommand();
            } catch (final Exception exception) {
                Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), exception);
            }
        }
    }

    private void executeCommand() throws Exception {
        final IExecutionContext executionContext = new ExecutionContext(action, actionWidget);
        try {
            action.execute(executionContext);
        } catch (final Exception exception) {
            if (action.getExceptionHandler() != null) {
                action.getExceptionHandler().handleException(executionContext, exception);
            } else {
                defaultExceptionHandler.handleException(executionContext, exception);
            }
        }
    }
}
