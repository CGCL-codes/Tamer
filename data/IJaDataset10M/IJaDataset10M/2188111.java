package nl.tamasja.uva.saf.fighter.action;

import nl.tamasja.uva.saf.fighter.action.ActionOptions.Type;
import nl.tamasja.uva.saf.fighter.condition.IBehaviourCondition;
import nl.tamasja.uva.saf.fighter.conditions.NearCondition;

public abstract class ActionClassKick extends ActionClass implements IFightAction {

    @Override
    public Type getStrikeType() {
        return Type.KICK;
    }

    @Override
    public IBehaviourCondition getCondition() {
        return new NearCondition();
    }
}
