package net.sourceforge.plantuml.command;

import java.util.List;
import net.sourceforge.plantuml.classdiagram.AbstractEntityDiagram;

public class CommandPage extends SingleLineCommand<AbstractEntityDiagram> {

    public CommandPage(AbstractEntityDiagram classDiagram) {
        super(classDiagram, "(?i)^page\\s+(\\d+)\\s*x\\s*(\\d+)$");
    }

    @Override
    protected CommandExecutionResult executeArg(List<String> arg) {
        final int horizontal = Integer.parseInt(arg.get(0));
        final int vertical = Integer.parseInt(arg.get(1));
        if (horizontal <= 0 || vertical <= 0) {
            return CommandExecutionResult.error("Argument must be positive");
        }
        getSystem().setHorizontalPages(horizontal);
        getSystem().setVerticalPages(vertical);
        return CommandExecutionResult.ok();
    }
}
