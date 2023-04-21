package com.aionemu.gameserver.skillengine.effect;

import java.util.concurrent.Future;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.stats.StatEnum;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.utils.stats.StatFunctions;

/**
 * @author ATracer
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BleedEffect")
public class BleedEffect extends EffectTemplate {

    @XmlAttribute(required = true)
    protected int checktime;

    @XmlAttribute
    protected int value;

    @XmlAttribute
    protected int delta;

    @Override
    public void applyEffect(Effect effect) {
        effect.addToEffectedController();
    }

    @Override
    public void calculate(Effect effect) {
        if (calculateEffectResistRate(effect, StatEnum.BLEED_RESISTANCE)) effect.addSucessEffect(this);
    }

    @Override
    public void endEffect(Effect effect) {
        Creature effected = effect.getEffected();
        effected.getEffectController().unsetAbnormal(EffectId.BLEED.getEffectId());
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
        final Creature effected = effect.getEffected();
        effected.getEffectController().setAbnormal(EffectId.BLEED.getEffectId());
        Future<?> task = ThreadPoolManager.getInstance().scheduleEffectAtFixedRate(new Runnable() {

            @Override
            public void run() {
                onPeriodicAction(effect);
            }
        }, checktime, checktime);
        effect.setPeriodicTask(task, position);
    }
}
