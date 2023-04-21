package de.uniwue.tm.textmarker.action;

import java.util.ArrayList;
import java.util.List;
import de.uniwue.tm.textmarker.kernel.TextMarkerStream;
import de.uniwue.tm.textmarker.kernel.expression.string.StringExpression;
import de.uniwue.tm.textmarker.kernel.rule.RuleMatch;
import de.uniwue.tm.textmarker.kernel.rule.TextMarkerRuleElement;
import de.uniwue.tm.textmarker.kernel.visitor.InferenceCrowd;

public class RetainMarkupAction extends AbstractTextMarkerAction {

    private List<StringExpression> markup;

    public RetainMarkupAction(List<StringExpression> markup) {
        super();
        this.markup = markup;
    }

    @Override
    public void execute(RuleMatch match, TextMarkerRuleElement element, TextMarkerStream stream, InferenceCrowd crowd) {
        List<String> list = new ArrayList<String>();
        for (StringExpression each : markup) {
            list.add(each.getStringValue(element.getParent()));
        }
        stream.retainTags(list);
    }

    public List<StringExpression> getMarkup() {
        return markup;
    }
}
