package typingwar.rule;

import typingwar.entity.Enemy;
import typingwar.entity.Projectile;
import typingwar.entity.Soldier;
import typingwar.game.TypingWarGameUniverse;
import gameframework.base.ObservableValue;
import gameframework.game.GameUniverse;
import gameframework.game.OverlapRulesApplierDefaultImpl;

public class OverlapRules extends OverlapRulesApplierDefaultImpl {

    protected TypingWarGameUniverse universe;

    public OverlapRules() {
    }

    public void overlapRule(Enemy e, Projectile p) {
        if (p.getTarget() == e) {
            universe.removeGameEntity(e);
            universe.removeGameEntity(p);
            ObservableValue<Integer>[] score = universe.getGame().score();
            score[0].setValue(score[0].getValue() + e.score());
        }
    }

    @Override
    public void setUniverse(GameUniverse universe) {
        this.universe = (TypingWarGameUniverse) universe;
    }

    public void overlapRule(Soldier e, Projectile p) {
        universe.removeGameEntity(p);
        if (e.getHealth().getValue() > 10) e.setHealth(e.getHealth().getValue() - 5); else {
            e.setHealth(0);
            universe.removeGameEntity(e);
        }
    }
}
