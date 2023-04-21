package net.sourceforge.plantuml.activitydiagram2.command;

import java.util.Map;
import net.sourceforge.plantuml.activitydiagram2.ActivityDiagram2;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexPartialMatch;

public class CommandBar2 extends SingleLineCommand2<ActivityDiagram2> {

    public CommandBar2(ActivityDiagram2 diagram) {
        super(diagram, getRegexConcat());
    }

    static RegexConcat getRegexConcat() {
        return new RegexConcat(new RegexLeaf("^"), new RegexLeaf("==+"), new RegexLeaf("BAR", "\\s*(.*?)\\s*"), new RegexLeaf("==+"), new RegexLeaf("$"));
    }

    @Override
    protected CommandExecutionResult executeArg(Map<String, RegexPartialMatch> arg) {
        getSystem().bar(arg.get("BAR").get(0));
        return CommandExecutionResult.ok();
    }
}
