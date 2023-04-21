package com.nexirius.framework.command;

import com.nexirius.util.assertion.Assert;
import javax.swing.*;

/**
 * This type represents a basic implementation of the Processor interface.
 *
 * @author Marcel Baumann
 */
public class SimpleProcessor implements Processor {

    /**
     * Execute the command
     *
     * @param command The command to be executed
     * @param param   The paramater to be passed to the command's
     *                initialiseAction() method.
     * @author Marcel Baumann
     */
    public synchronized void execute(Command command, Object param) {
        Assert.pre(command != null, "Parameter command is not null");
        SwingUtilities.invokeLater(new SwingExecutor(command, param));
    }

    class SwingExecutor implements Runnable {

        private Command command;

        private Object param;

        SwingExecutor(Command command, Object param) {
            this.command = command;
            this.param = param;
        }

        public void run() {
            try {
                command.initialiseAction(param);
                command.doAction();
                command.finaliseAction();
            } catch (Throwable e) {
                command.exceptionAction(e);
            }
        }
    }
}
