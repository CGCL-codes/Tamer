package genomemap.cef.command;

import cef.command.Command;
import cef.command.CommandException;
import genomemap.cef.event.EstimateProgressCurveEvent;

/**
 * @since Sep 26, 2011
 * @author Susanta Tewari
 */
public class EstimateProgressCurveCommand extends Command {

    private boolean cmdRan;

    public EstimateProgressCurveCommand(EstimateProgressCurveEvent event) {
        super(event);
    }

    @Override
    public void executeImpl() throws CommandException {
        EstimateProgressCurveEvent evt = (EstimateProgressCurveEvent) event;
        cmdRan = true;
        fireOutputEvent(commandOutputEvent);
    }

    private void checkExecutionState() {
        if (!cmdRan) throw new IllegalStateException("Command has not finished computing yet.");
    }
}
