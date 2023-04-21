package net.sourceforge.plantuml.sequencediagram.command;

import java.util.List;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;

public class CommandNewpage extends SingleLineCommand<SequenceDiagram> {

    public CommandNewpage(SequenceDiagram sequenceDiagram) {
        super(sequenceDiagram, "(?i)^@?newpage(?:(?:\\s*:\\s*|\\s+)(.*[\\p{L}0-9_.].*))?$");
    }

    @Override
    protected CommandExecutionResult executeArg(List<String> arg) {
        final List<String> strings = arg.get(0) == null ? null : StringUtils.getWithNewlines(arg.get(0));
        getSystem().newpage(strings);
        return CommandExecutionResult.ok();
    }
}
