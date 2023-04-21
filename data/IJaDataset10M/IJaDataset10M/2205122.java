package genomemap.cef.command.listener;

import cef.command.Command;
import cef.command.listener.CommandOutputEvent;
import cef.command.listener.CommandOutputListener;
import cef.command.listener.CommandOutputListenerException;
import cef.command.listener.CommandPrintWriter;
import genomemap.cef.command.CreateRepeatsMapCommand;

/**
 * @since Sep 26, 2011
 * @author Susanta Tewari
 */
public class CreateRepeatsMapCommandPrintWriter extends CommandPrintWriter {

    @Override
    public void receivedEvent(CommandOutputEvent cmdEvent) throws CommandOutputListenerException {
        CreateRepeatsMapCommand command = (CreateRepeatsMapCommand) cmdEvent.getSource();
        getPrintWriter().println("Likelihood: ");
        getPrintWriter().flush();
    }

    @Override
    public Class<? extends Command> getTargetCommandClass() {
        return CreateRepeatsMapCommand.class;
    }

    @Override
    public CommandOutputListener newInstance() {
        return new CreateRepeatsMapCommandPrintWriter();
    }
}
