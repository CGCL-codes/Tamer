package org.openaion.gameserver.skill.effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import org.openaion.commons.utils.Rnd;
import org.openaion.gameserver.controllers.attack.AttackStatus;
import org.openaion.gameserver.controllers.movement.AttackCalcObserver;
import org.openaion.gameserver.controllers.movement.AttackStatusObserver;
import org.openaion.gameserver.model.gameobjects.stats.StatEnum;
import org.openaion.gameserver.skill.model.Effect;

/**
 * @author ATracer
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BlindEffect")
public class BlindEffect extends EffectTemplate {

    @XmlAttribute
    private int value;

    @Override
    public void applyEffect(Effect effect) {
        effect.addToEffectedController();
    }

    @Override
    public void calculate(Effect effect) {
        super.calculate(effect, StatEnum.BLIND_RESISTANCE, null);
    }

    @Override
    public void startEffect(Effect effect) {
        AttackCalcObserver acObserver = new AttackStatusObserver(value, AttackStatus.DODGE) {

            @Override
            public boolean checkAttackerStatus(AttackStatus status) {
                return Rnd.get(100) <= value;
            }
        };
        effect.getEffected().getObserveController().addAttackCalcObserver(acObserver);
        effect.setAttackStatusObserver(acObserver, position);
        effect.getEffected().getEffectController().setAbnormal(EffectId.BLIND.getEffectId());
    }

    @Override
    public void endEffect(Effect effect) {
        effect.getEffected().getEffectController().unsetAbnormal(EffectId.BLIND.getEffectId());
        AttackCalcObserver acObserver = effect.getAttackStatusObserver(position);
        if (acObserver != null) effect.getEffected().getObserveController().removeAttackCalcObserver(acObserver);
    }
}
