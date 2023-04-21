package org.aspectbrains.contractj.paramprecondition;

/**
 * Handler for parameter-precondition checking.
 * 
 * @author Heiko Seeberger
 */
public interface IParamPreconditionHandler {

    /**
     * Handles parameter-precondition checking.
     * 
     * @param paramPreconditionContext
     *            The runtime context for the parameter-precondition to be
     *            handled.
     * @throws ParamPreconditionViolationException
     *             if the parameter-precondition is violated.
     */
    void handle(IParamPreconditionContext paramPreconditionContext) throws ParamPreconditionViolationException;
}
