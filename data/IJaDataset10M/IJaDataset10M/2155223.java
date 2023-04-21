package de.uniwue.tm.textmarker.action;

import de.uniwue.tm.textmarker.kernel.TextMarkerStream;
import de.uniwue.tm.textmarker.kernel.rule.RuleMatch;
import de.uniwue.tm.textmarker.kernel.rule.TextMarkerRuleElement;
import de.uniwue.tm.textmarker.kernel.visitor.InferenceCrowd;

public class VariableAction extends AbstractTextMarkerAction {

    private final String var;

    public VariableAction(String var) {
        super();
        this.var = var;
    }

    @Override
    public void execute(RuleMatch match, TextMarkerRuleElement element, TextMarkerStream stream, InferenceCrowd crowd) {
    }

    public String getVar() {
        return var;
    }
}
