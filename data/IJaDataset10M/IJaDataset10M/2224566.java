package fighter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import fighter.checker.Visitor;
import fighter.condition.ConditionType;

@SuppressWarnings("serial")
public class Behaviour extends ArrayList<Rule> implements ASTNode {

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    private int generatRandomIndex(int maxIndex) {
        Random randomGenerator = new Random();
        return randomGenerator.nextInt(maxIndex);
    }

    public Rule getNextRule(List<ConditionType> acceptedConditions) {
        Rule selectedRule;
        int index;
        List<Rule> possibleRules;
        possibleRules = preselectPossibleMoves(acceptedConditions);
        index = generatRandomIndex(possibleRules.size());
        selectedRule = possibleRules.get(index);
        return selectedRule;
    }

    private List<Rule> preselectPossibleMoves(List<ConditionType> acceptedConditions) {
        List<Rule> possibleActions = new ArrayList<Rule>();
        for (Rule rule : this) {
            if (rule.checkCondition(acceptedConditions)) {
                possibleActions.add(rule);
            }
        }
        return possibleActions;
    }
}
