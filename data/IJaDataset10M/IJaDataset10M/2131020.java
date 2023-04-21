package gameserver.skill.effect;

import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.stats.StatEnum;
import gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import gameserver.skill.model.Effect;
import gameserver.skill.model.HealType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HealOverTimeEffect")
public abstract class HealOverTimeEffect extends AbstractOverTimeEffect {

    public void calculate(Effect effect, HealType healtype) {
        int valueWithDelta = value + delta * effect.getSkillLevel();
        int maxCurValue = getMaxCurStatValue(effect);
        int possibleHealValue = 0;
        if (percent) {
            possibleHealValue = maxCurValue * valueWithDelta / 100;
        } else {
            float boostHeal = 0;
            float healRate = 1.0f;
            if (healtype == HealType.HP && effect.getEffector() instanceof Player) {
                boostHeal = ((float) (effect.getEffector().getGameStats().getCurrentStat(StatEnum.BOOST_HEAL) - 100) / 1000f);
                healRate = effect.getEffector().getController().getHealRate();
            }
            possibleHealValue = (int) (valueWithDelta * (float) (healRate + boostHeal));
        }
        switch(healtype) {
            case MP:
                effect.setmotValue(possibleHealValue);
                break;
            default:
                effect.sethotValue(possibleHealValue);
                break;
        }
        super.calculate(effect);
    }

    public void onPeriodicAction(Effect effect, HealType healtype) {
        Creature effected = effect.getEffected();
        int currentValue = getCurrentStatValue(effect);
        int maxCurValue = getMaxCurStatValue(effect);
        int possibleHealValue = 0;
        switch(healtype) {
            case MP:
                possibleHealValue = effect.getmotValue();
                break;
            default:
                possibleHealValue = effect.gethotValue();
                break;
        }
        int healValue = maxCurValue - currentValue < possibleHealValue ? (maxCurValue - currentValue) : possibleHealValue;
        if (healtype == HealType.HP && effect.getEffected().getEffectController().isAbnormalSet(EffectId.DISEASE)) return;
        if (healValue == 0) return;
        switch(healtype) {
            case FP:
                effected.getLifeStats().increaseFp(TYPE.FP, healValue);
                break;
            case HP:
                effected.getLifeStats().increaseHp(TYPE.HP, healValue, effect.getSkillId(), 3);
                break;
            case MP:
                effected.getLifeStats().increaseMp(TYPE.MP, healValue, effect.getSkillId(), 4);
                break;
            case DP:
                ((Player) effect.getEffected()).getCommonData().addDp(healValue);
                break;
        }
    }

    protected abstract int getCurrentStatValue(Effect effect);

    protected abstract int getMaxCurStatValue(Effect effect);
}
