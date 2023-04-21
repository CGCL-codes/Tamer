package com.peterhi.server.commands;

import com.peterhi.StatusCode;
import com.peterhi.server.Server;

/**
 *
 * @author YUN TAO
 */
public class ServerHealthCommand implements Command {

    public CommandResult execute(Server server, String command) {
        return new CommandResult(server.toString(), StatusCode.OK);
    }
}
