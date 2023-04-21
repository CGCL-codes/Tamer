package nakayo.gameserver.skillengine.effect;

import nakayo.gameserver.model.gameobjects.Creature;
import nakayo.gameserver.model.gameobjects.stats.StatEnum;
import nakayo.gameserver.skillengine.model.Effect;
import nakayo.gameserver.utils.ThreadPoolManager;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import java.util.concurrent.Future;

/**
 * @author ATracer, ZeroSignal
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HpUseOverTimeEffect")
public class HpUseOverTimeEffect extends EffectTemplate {

    @XmlAttribute(required = true)
    protected int checktime;

    @XmlAttribute
    protected int value;

    @XmlAttribute
    protected int delta;

    @Override
    public void applyEffect(final Effect effect) {
        Creature effected = effect.getEffected();
        final int requiredHp = value + (delta * effect.getSkillLevel());
        Future<?> task = ThreadPoolManager.getInstance().scheduleEffectAtFixedRate(new Runnable() {

            @Override
            public void run() {
                onPeriodicAction(effect, requiredHp);
            }
        }, 0, checktime);
        effect.setHpUseTask(task);
    }

    public void onPeriodicAction(Effect effect, int value) {
        Creature effected = effect.getEffected();
        if (effected.getLifeStats().getCurrentHp() < value) effect.endEffect();
        effected.getLifeStats().reduceHp(value, effected);
    }

    @Override
    public void calculate(Effect effect) {
        Creature effected = effect.getEffected();
        effect.addSucessEffect(this);
    }
}
