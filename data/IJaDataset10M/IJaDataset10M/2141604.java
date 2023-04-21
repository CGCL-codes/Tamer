package org.openaion.gameserver.skill.effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.model.gameobjects.stats.StatEnum;
import org.openaion.gameserver.skill.model.Effect;
import org.openaion.gameserver.skill.model.HealType;

/**
 * @author ATracer
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ItemHealMpEffect")
public class ItemHealMpEffect extends AbstractHealEffect {

    @Override
    public void applyEffect(Effect effect) {
        super.applyEffect(effect, HealType.MP);
    }

    @Override
    public void calculate(Effect effect) {
        super.calculate(effect, HealType.MP, false);
    }

    @Override
    protected int getCurrentStatValue(Effect effect) {
        return ((Player) effect.getEffected()).getLifeStats().getCurrentMp();
    }

    @Override
    protected int getMaxCurStatValue(Effect effect) {
        return effect.getEffected().getGameStats().getCurrentStat(StatEnum.MAXMP);
    }
}
