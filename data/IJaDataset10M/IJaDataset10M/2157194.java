package nakayo.gameserver.skillengine.effect;

import nakayo.gameserver.controllers.movement.ActionObserver;
import nakayo.gameserver.controllers.movement.ActionObserver.ObserverType;
import nakayo.gameserver.model.gameobjects.Creature;
import nakayo.gameserver.model.gameobjects.stats.StatEnum;
import nakayo.gameserver.skillengine.model.Effect;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SleepEffect")
public class SleepEffect extends EffectTemplate {

    @Override
    public void applyEffect(Effect effect) {
        effect.addToEffectedController();
    }

    @Override
    public void calculate(Effect effect) {
        if (calculateEffectResistRate(effect, StatEnum.MAGICAL_RESIST) && calculateEffectResistRate(effect, StatEnum.SLEEP_RESISTANCE)) effect.addSucessEffect(this);
    }

    @Override
    public void startEffect(final Effect effect) {
        final Creature effected = effect.getEffected();
        effected.getController().cancelCurrentSkill();
        effect.setAbnormal(EffectId.SLEEP.getEffectId());
        effected.getEffectController().setAbnormal(EffectId.SLEEP.getEffectId());
        effected.getObserveController().attach(new ActionObserver(ObserverType.ATTACKED) {

            @Override
            public void attacked(Creature creature) {
                effected.getEffectController().removeEffect(effect.getSkillId());
            }
        });
        effected.getObserveController().attach(new ActionObserver(ObserverType.DOT) {

            @Override
            public void onDot(Creature creature) {
                effected.getEffectController().removeEffect(effect.getSkillId());
            }
        });
    }

    @Override
    public void endEffect(Effect effect) {
        effect.getEffected().getEffectController().unsetAbnormal(EffectId.SLEEP.getEffectId());
    }
}
