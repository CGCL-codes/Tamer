package genomemap.cef.command;

import cef.command.Command;
import cef.command.CommandException;
import genomemap.cef.event.SummarizeOrganismDataEvent;

/**
 * @since Sep 29, 2011
 * @author Susanta Tewari
 */
public class SummarizeOrganismDataCommand extends Command {

    private boolean cmdRan;

    public SummarizeOrganismDataCommand(SummarizeOrganismDataEvent event) {
        super(event);
    }

    @Override
    public void executeImpl() throws CommandException {
        SummarizeOrganismDataEvent evt = (SummarizeOrganismDataEvent) event;
        cmdRan = true;
        fireOutputEvent(commandOutputEvent);
    }

    private void checkExecutionState() {
        if (!cmdRan) throw new IllegalStateException("Command has not finished computing yet.");
    }
}
