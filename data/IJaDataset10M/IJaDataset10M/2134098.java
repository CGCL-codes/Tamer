package net.sourceforge.plantuml.command;

import java.util.List;
import net.sourceforge.plantuml.UmlDiagram;

public class CommandRotate extends SingleLineCommand<UmlDiagram> {

    public CommandRotate(UmlDiagram diagram) {
        super(diagram, "(?i)^rotate$");
    }

    @Override
    protected CommandExecutionResult executeArg(List<String> arg) {
        getSystem().setRotation(true);
        return CommandExecutionResult.ok();
    }
}
