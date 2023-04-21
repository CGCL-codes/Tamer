package ar.com.ktulu.dict.kernel.command;

import java.util.Iterator;
import ar.com.ktulu.util.command.Command;
import ar.com.ktulu.util.command.Context;
import ar.com.ktulu.util.command.CommandException;
import ar.com.ktulu.dict.Database;
import ar.com.ktulu.dict.ServerThread;
import ar.com.ktulu.dict.Server;
import ar.com.ktulu.dict.Database;
import ar.com.ktulu.dict.Definition;
import ar.com.ktulu.dict.Matches;
import ar.com.ktulu.dict.DatabaseException;
import ar.com.ktulu.dict.strategies.Strategy;

/**
 *
 * @author  Luis Parravicini
 */
public class CmdOption implements Command {

    public void execute(Context ctx, String[] args) throws CommandException {
        ServerThread connHandler = (ServerThread) ctx.get("server");
        if (args.length == 0 || !args[0].equalsIgnoreCase("MIME")) {
            connHandler.println("501 Syntax error, illegal parameter");
            return;
        }
        connHandler.setMimeEnabled(true);
        connHandler.println("250 ok");
    }

    public void execute(Context ctx) throws CommandException {
    }

    public void execute() throws CommandException {
    }

    public void execute(String[] args) throws CommandException {
    }

    public String getDescription() {
        return "OPTION MIME                  -- use MIME headers";
    }
}
