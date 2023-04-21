package br.ufes.xpflow.core.flow.execution.impl;

import br.ufes.xpflow.core.flow.execution.ExecutionContext;
import br.ufes.xpflow.core.flow.execution.TerminateFlowException;
import br.ufes.xpflow.core.main.XPFlowException;

/**
 * Interface para comandos que podem
 * compor um flow. Todas as classes que
 * herdem Command devem propagar a exceção
 * TerminateFlowException.
 *
 * @author Welton
 */
public class TerminateCmd extends AbstractCommand {

    public TerminateCmd() {
    }

    public void execute(ExecutionContext ctxt) throws XPFlowException, TerminateFlowException {
        throw new TerminateFlowException();
    }
}
