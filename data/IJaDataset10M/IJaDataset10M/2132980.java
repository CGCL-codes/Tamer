package org.rjam.net.command.server.api;

import java.io.Serializable;

public interface ICommandFactory extends Serializable {

    public ICommand getCommand(IRequestContext context);
}
