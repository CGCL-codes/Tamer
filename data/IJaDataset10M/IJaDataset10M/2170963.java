package org.openaion.gameserver.skill.effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import org.openaion.gameserver.controllers.attack.AttackUtil;
import org.openaion.gameserver.model.gameobjects.Creature;
import org.openaion.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import org.openaion.gameserver.skill.model.Effect;

/**
 * @author ATracer
 * @rework kecimis
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SpellAttackEffect")
public class SpellAttackEffect extends AbstractOverTimeEffect {

    @Override
    public void calculate(Effect effect) {
        int valueWithDelta = value + delta * effect.getSkillLevel();
        int damage = AttackUtil.calculateMagicalOverTimeResult(effect, valueWithDelta, element, this.position, false);
        effect.setReserved4(damage);
        super.calculate(effect, null, null);
    }

    @Override
    public void onPeriodicAction(Effect effect) {
        Creature effected = effect.getEffected();
        Creature effector = effect.getEffector();
        effected.getController().onAttack(effector, effect.getSkillId(), TYPE.HP, effect.getReserved4(), 1, effect.getAttackStatus(), false, true);
        effected.getObserveController().notifyDotObservers(effected);
    }
}
