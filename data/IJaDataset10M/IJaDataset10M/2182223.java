package de.uniwue.tm.textmarker.kernel.rule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.text.AnnotationFS;
import de.uniwue.tm.textmarker.action.AbstractTextMarkerAction;
import de.uniwue.tm.textmarker.condition.AbstractTextMarkerCondition;
import de.uniwue.tm.textmarker.kernel.TextMarkerBlock;
import de.uniwue.tm.textmarker.kernel.TextMarkerStream;
import de.uniwue.tm.textmarker.kernel.rule.quantifier.RuleElementQuantifier;
import de.uniwue.tm.textmarker.kernel.type.TextMarkerBasic;
import de.uniwue.tm.textmarker.kernel.visitor.InferenceCrowd;

public class TextMarkerRuleElement extends AbstractRuleElement {

    private TextMarkerMatcher matcher;

    private List<AbstractTextMarkerCondition> conditions;

    private List<AbstractTextMarkerAction> actions;

    private TextMarkerBlock parent;

    @SuppressWarnings("unchecked")
    protected final InferenceCrowd emptyCrowd = new InferenceCrowd(Collections.EMPTY_LIST);

    public TextMarkerRuleElement(TextMarkerMatcher matcher, RuleElementQuantifier quantifier, List<AbstractTextMarkerCondition> conditions, List<AbstractTextMarkerAction> actions, TextMarkerBlock parent) {
        super(quantifier);
        this.matcher = matcher;
        this.conditions = conditions;
        this.actions = actions;
        this.parent = parent;
        if (conditions == null) {
            this.conditions = new ArrayList<AbstractTextMarkerCondition>();
        }
        if (actions == null) {
            this.actions = new ArrayList<AbstractTextMarkerAction>();
        }
    }

    public List<TextMarkerBasic> getAnchors(TextMarkerStream symbolStream) {
        return matcher.getMatchingBasics(symbolStream, getParent());
    }

    public FSIterator<AnnotationFS> getAnchors2(TextMarkerStream symbolStream) {
        return matcher.getMatchingBasics2(symbolStream, getParent());
    }

    public RuleElementMatch match(TextMarkerBasic currentBasic, TextMarkerStream stream, InferenceCrowd crowd) {
        RuleElementMatch result = new RuleElementMatch(this);
        List<EvaluatedCondition> conditionResults = new ArrayList<EvaluatedCondition>(conditions.size());
        boolean matched = true;
        boolean base = matcher.match(currentBasic, stream, getParent());
        Type type = matcher.getType(getParent(), stream);
        List<AnnotationFS> textsMatched = new ArrayList<AnnotationFS>(1);
        if (base) {
            for (AbstractTextMarkerCondition condition : conditions) {
                crowd.beginVisit(condition, null);
                EvaluatedCondition eval = condition.eval(currentBasic, type, this, stream, crowd);
                crowd.endVisit(condition, null);
                matched &= eval.isValue();
                conditionResults.add(eval);
            }
        }
        if (currentBasic != null) {
            textsMatched.add(stream.expandAnchor(currentBasic, type));
        }
        result.setMatchInfo(base, textsMatched, conditionResults);
        return result;
    }

    public void apply(RuleMatch matchInfos, TextMarkerStream symbolStream, InferenceCrowd crowd) {
        for (AbstractTextMarkerAction action : actions) {
            crowd.beginVisit(action, null);
            action.execute(matchInfos, this, symbolStream, crowd);
            crowd.endVisit(action, null);
        }
    }

    @Override
    public String toString() {
        return matcher.toString() + " " + quantifier.getClass().getSimpleName() + (conditions.isEmpty() ? "" : "(" + conditions.toString() + ")" + "\\n") + (actions.isEmpty() ? "" : "{" + actions.toString() + "}");
    }

    @Override
    public RuleElementQuantifier getQuantifier() {
        return quantifier;
    }

    public TextMarkerMatcher getMatcher() {
        return matcher;
    }

    public List<AbstractTextMarkerCondition> getConditions() {
        return conditions;
    }

    public List<AbstractTextMarkerAction> getActions() {
        return actions;
    }

    public TextMarkerBlock getParent() {
        return parent;
    }
}
