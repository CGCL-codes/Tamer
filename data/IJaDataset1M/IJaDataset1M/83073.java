package de.uniwue.tm.textmarker.action;

import de.uniwue.tm.textmarker.kernel.TextMarkerElement;
import de.uniwue.tm.textmarker.kernel.TextMarkerStream;
import de.uniwue.tm.textmarker.kernel.rule.RuleMatch;
import de.uniwue.tm.textmarker.kernel.rule.TextMarkerRuleElement;
import de.uniwue.tm.textmarker.kernel.visitor.InferenceCrowd;

public abstract class AbstractTextMarkerAction extends TextMarkerElement {

    public abstract void execute(RuleMatch match, TextMarkerRuleElement element, TextMarkerStream stream, InferenceCrowd crowd);

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
