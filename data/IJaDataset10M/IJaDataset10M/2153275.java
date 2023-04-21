package org.openaion.gameserver.skill.effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import org.openaion.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import org.openaion.gameserver.skill.action.DamageType;
import org.openaion.gameserver.skill.model.Effect;

/**
 * @author ATracer
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SkillAtkDrainInstantEffect")
public class SkillAtkDrainInstantEffect extends DamageEffect {

    @XmlAttribute(name = "hp")
    protected int HPpercent;

    @XmlAttribute(name = "mp")
    protected int MPpercent;

    @Override
    public void applyEffect(Effect effect) {
        super.applyEffect(effect);
        int value = 0;
        if (HPpercent > 0) {
            value = effect.getReserved1() * HPpercent / 100;
            effect.getEffector().getLifeStats().increaseHp(TYPE.HP, value, effect.getSkillId(), 170);
        }
        if (MPpercent > 0) {
            value = effect.getReserved1() * MPpercent / 100;
            effect.getEffector().getLifeStats().increaseMp(TYPE.MP, value, effect.getSkillId(), 170);
        }
    }

    @Override
    public void calculate(Effect effect) {
        super.calculate(effect, DamageType.PHYSICAL, true);
    }
}
