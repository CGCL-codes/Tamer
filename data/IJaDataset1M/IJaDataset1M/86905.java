package net.sourceforge.plantuml.command;

import java.util.List;
import net.sourceforge.plantuml.UmlDiagram;

public class CommandMinwidth extends SingleLineCommand<UmlDiagram> {

    public CommandMinwidth(UmlDiagram system) {
        super(system, "(?i)^minwidth\\s+(\\d+)$");
    }

    @Override
    protected CommandExecutionResult executeArg(List<String> arg) {
        final int minwidth = Integer.parseInt(arg.get(0));
        ((UmlDiagram) getSystem()).setMinwidth(minwidth);
        return CommandExecutionResult.ok();
    }
}
