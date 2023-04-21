package org.modss.facilitator.port.control.command;

import org.modss.facilitator.port.command.IFileCloseCommand;

/**
 * File.Close command.
 */
public class FileCloseCommand extends Command {

    IFileCloseCommand _handler = null;

    public FileCloseCommand(IFileCloseCommand handler) {
        _handler = handler;
    }

    /**
     * Invoke implementation
     */
    public void execute() {
        _handler.fileClose();
    }
}
