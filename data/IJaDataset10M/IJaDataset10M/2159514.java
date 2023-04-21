package nl.tamasja.uva.saf.fighter.conditions;

import nl.tamasja.uva.saf.fighter.FighterBot;
import nl.tamasja.uva.saf.fighter.condition.IBehaviourCondition;

public class StrongerCondition implements IBehaviourCondition {

    @Override
    public boolean evaluate(FighterBot self) {
        return evaluate(self, self.getEnemyFighter().getPower());
    }

    @Override
    public boolean evaluate(FighterBot self, int value) {
        return self.getPower() > value;
    }
}
