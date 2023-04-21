package com.aionemu.gameserver.skillengine.effect;

import java.util.concurrent.Future;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.utils.stats.StatFunctions;

/**
 * @author ATracer
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DamageOverTimeEffect")
public class DamageOverTimeEffect extends DamageEffect {

    @XmlAttribute(required = true)
    protected int checktime;

    @Override
    public void calculate(Effect effect) {
        if (calculateEffectResistRate(effect, null)) effect.addSucessEffect(this);
    }

    @Override
    public void applyEffect(Effect effect) {
        effect.addToEffectedController();
    }

    @Override
    public void endEffect(Effect effect) {
    }

    @Override
    public void onPeriodicAction(Effect effect) {
        Creature effected = effect.getEffected();
        Creature effector = effect.getEffector();
        int valueWithDelta = value + delta * effect.getSkillLevel();
        int damage = StatFunctions.calculateMagicDamageToTarget(effector, effected, valueWithDelta, getElement());
        effected.getController().onAttack(effector, effect.getSkillId(), TYPE.DAMAGE, damage);
    }

    @Override
    public void startEffect(final Effect effect) {
        Future<?> task = ThreadPoolManager.getInstance().scheduleEffectAtFixedRate(new Runnable() {

            @Override
            public void run() {
                onPeriodicAction(effect);
            }
        }, checktime, checktime);
        effect.setPeriodicTask(task, position);
    }
}
