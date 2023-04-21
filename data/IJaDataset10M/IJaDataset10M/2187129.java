package nl.tamasja.uva.saf.fighter.action;

import nl.tamasja.uva.saf.fighter.condition.IBehaviourCondition;
import nl.tamasja.uva.saf.fighter.action.ActionOptions.*;

public interface IAction {

    IBehaviourCondition getCondition();

    Height getStrikeHeight();

    Type getStrikeType();

    Height getMovementHeight();

    int getMovement();
}
