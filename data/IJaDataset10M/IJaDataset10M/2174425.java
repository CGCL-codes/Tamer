package org.jowidgets.impl.widgets.basic.blueprint.convenience;

import org.jowidgets.api.command.IAction;
import org.jowidgets.api.command.IExecutionContext;
import org.jowidgets.api.widgets.blueprint.builder.ITitledWindowSetupBuilder;
import org.jowidgets.api.widgets.blueprint.builder.convenience.ITitledWindowSetupConvenience;
import org.jowidgets.tools.widgets.blueprint.convenience.AbstractSetupBuilderConvenience;
import org.jowidgets.util.Assert;

public class TitledWindowSetupConvenience extends AbstractSetupBuilderConvenience<ITitledWindowSetupBuilder<?>> implements ITitledWindowSetupConvenience<ITitledWindowSetupBuilder<?>> {

    @Override
    public ITitledWindowSetupBuilder<?> setExecutionContext(final IExecutionContext executionContext) {
        Assert.paramNotNull(executionContext, "executionContext");
        final IAction action = executionContext.getAction();
        return getBuilder().setTitle(action.getText()).setIcon(action.getIcon());
    }
}
