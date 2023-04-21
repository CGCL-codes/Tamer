package genomemap.cef.command.listener;

import cef.command.Command;
import cef.command.listener.CommandOutputEvent;
import cef.command.listener.CommandOutputListener;
import cef.command.listener.CommandOutputListenerException;
import cef.command.listener.CommandPrintWriter;
import genomemap.cef.command.EstimateModelCommand;

/**
 * @since Sep 27, 2011
 * @author Susanta Tewari
 */
public class EstimateModelCommandPrintWriter extends CommandPrintWriter {

    @Override
    public void receivedEvent(CommandOutputEvent cmdEvent) throws CommandOutputListenerException {
        EstimateModelCommand command = (EstimateModelCommand) cmdEvent.getSource();
        getPrintWriter().println("Likelihood: ");
        getPrintWriter().flush();
    }

    @Override
    public Class<? extends Command> getTargetCommandClass() {
        return EstimateModelCommand.class;
    }

    @Override
    public CommandOutputListener newInstance() {
        return new EstimateModelCommandPrintWriter();
    }
}
