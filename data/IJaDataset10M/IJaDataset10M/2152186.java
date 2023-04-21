package org.specrunner.context;

import org.specrunner.runner.IRunner;
import org.specrunner.source.ISource;

/**
 * A context factory.
 * 
 * @author Thiago Santos
 * 
 */
public interface IContextFactory {

    /**
     * Creates a new context based on an source and a runner.
     * 
     * @param source
     *            The source context.
     * @param runner
     *            The context runner.
     * @return A new context.
     * @throws ContextException
     *             On creation errors.
     */
    IContext newContext(ISource source, IRunner runner) throws ContextException;
}
