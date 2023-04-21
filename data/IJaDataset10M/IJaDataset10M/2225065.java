package fighter.checker;

import fighter.Behaviour;
import fighter.Fighter;
import fighter.Personality;
import fighter.Rule;
import fighter.action.Actions;
import fighter.condition.AndCondition;
import fighter.condition.OrCondition;
import fighter.condition.SimpleCondition;

public interface Visitor {

    public void visit(Fighter fighter);

    public void visit(Personality personality);

    public void visit(Behaviour behaviour);

    public void visit(Rule rule);

    public void visit(AndCondition andCondition);

    public void visit(OrCondition orCondition);

    public void visit(SimpleCondition simpleCondition);

    public void visit(Actions<fighter.action.ActionType> actions);
}
