package failure.core.business.core;

import failure.core.business.unit.Unit;
import failure.core.game.GameEngine;

public class Fight {

    public static final EfficiencyCalculator efficiencyCalculator = new EfficiencyCalculator();

    public static final BonusCalculator bonusCalculator = new BonusCalculator();

    public Fight() {
    }

    public static FightUnitsResult fightUnits(Unit atk, Unit def) {
        FightUnitsResult result = new FightUnitsResult(atk, def);
        int dmgAtk = atk.getAttack().getValue() * ((int) (Math.random() * 3) - def.getDefence().getValue() * (int) (Math.random() * 4));
        int dmgDef = def.getAttack().getValue() * ((int) (Math.random() * 3) - atk.getDefence().getValue() * (int) (Math.random() * 4));
        int distance = GameEngine.unitManager.getBlockDistance(atk, def);
        if (distance > atk.getAttackRange()) {
            dmgAtk = (int) (dmgAtk / (atk.getDistanceAtkAttenuation() * (distance - atk.getAttackRange())));
        }
        if (distance > def.getAttackRange()) {
            dmgDef = (int) (dmgDef / (def.getDistanceAtkAttenuation() * (distance - def.getAttackRange())));
        }
        result.setDmgAtk(dmgAtk);
        result.setDmgDef(dmgDef);
        if (dmgDef > 0) atk.setLife(atk.getLife() - dmgDef);
        if (dmgAtk > 0) def.setLife(def.getLife() - dmgAtk);
        return result;
    }
}
