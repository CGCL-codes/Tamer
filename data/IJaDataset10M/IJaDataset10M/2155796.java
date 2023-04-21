package org.perfectday.logicengine.combat.core.functions.damage;

import java.util.List;
import org.perfectday.logicengine.model.minis.support.modifiers.Modifier;
import org.perfectday.logicengine.combat.core.functions.DamageFunction;
import org.perfectday.logicengine.model.minis.Mini;
import org.perfectday.logicengine.model.minis.action.combat.CombatActionMini;

/**
 *
 * @author Miguel Angel Lopez Montellano ( alakat@gmail.com )
 */
public class FireMagicDamageFunction extends DamageFunction {

    @Override
    public double getDamageAtack(Mini atacker, Mini defensor, double modifiredDamage, CombatActionMini actionAtack, double lockyRoll) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isCriticAtack(double roll) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isWeighAtack(double roll) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
