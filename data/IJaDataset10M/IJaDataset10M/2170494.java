package softwarekompetenz.workbench.command;

import softwarekompetenz.workbench.Workbench.Processor;

public class EndZoomOutCommand extends Command {

    public EndZoomOutCommand(Processor processor) {
        super(processor);
    }

    public void execute() {
        this.getProcessor().endZoomOut();
    }
}
