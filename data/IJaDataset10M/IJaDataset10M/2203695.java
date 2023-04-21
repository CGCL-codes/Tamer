package freeguide.common.plugininterfaces;

import freeguide.plugins.program.freeguide.lib.general.ICommandRunner;

public interface IExecutionController {

    public void activate(final IApplication controller, final ICommandRunner runner, final boolean dialogVisible);
}
