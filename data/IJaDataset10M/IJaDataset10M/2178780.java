package gameserver.skillengine.effect;

import gameserver.controllers.movement.AttackCalcObserver;
import gameserver.skillengine.model.Effect;
import gameserver.skillengine.model.SkillType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OneTimeBoostSkillAttackEffect")
public class OneTimeBoostSkillAttackEffect extends BufEffect {

    @XmlAttribute
    private int count;

    @XmlAttribute
    private SkillType type;

    @XmlAttribute
    private int value;

    @Override
    public void startEffect(final Effect effect) {
        super.startEffect(effect);
        final int stopCount = count;
        final float percent = 1.0f + value / 100.0f;
        AttackCalcObserver observer = null;
        switch(type) {
            case MAGICAL:
                observer = new AttackCalcObserver() {

                    private int count = 0;

                    @Override
                    public float getBaseMagicalDamageMultiplier() {
                        if (count++ < stopCount) {
                            return percent;
                        } else effect.getEffected().getEffectController().removeEffect(effect.getSkillId());
                        return 1.0f;
                    }
                };
                break;
            case PHYSICAL:
                observer = new AttackCalcObserver() {

                    private int count = 0;

                    @Override
                    public float getBasePhysicalDamageMultiplier() {
                        if (count++ < stopCount) {
                            return percent;
                        } else effect.getEffected().getEffectController().removeEffect(effect.getSkillId());
                        return 1.0f;
                    }
                };
                break;
        }
        effect.getEffected().getObserveController().addAttackCalcObserver(observer);
        effect.setAttackStatusObserver(observer, position);
    }

    @Override
    public void endEffect(Effect effect) {
        super.endEffect(effect);
        AttackCalcObserver observer = effect.getAttackStatusObserver(position);
        effect.getEffected().getObserveController().removeAttackCalcObserver(observer);
    }

    @Override
    public void calculate(Effect effect) {
        super.calculate(effect);
    }
}
