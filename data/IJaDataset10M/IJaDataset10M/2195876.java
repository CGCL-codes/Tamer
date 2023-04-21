package org.openaion.gameserver.skill.effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import org.openaion.gameserver.skill.action.DamageType;
import org.openaion.gameserver.skill.model.Effect;

/**
 * @author Sippolo
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MpAttackInstantEffect")
public class MpAttackInstantEffect extends DamageEffect {

    @XmlAttribute
    protected boolean percent;

    @Override
    public void applyEffect(Effect effect) {
        int maxMP = effect.getEffected().getLifeStats().getMaxMp();
        int newValue = value;
        if (percent) newValue = (int) ((maxMP * value) / 100);
        effect.getEffected().getLifeStats().reduceMp(newValue);
    }

    @Override
    public void calculate(Effect effect) {
        super.calculate(effect, DamageType.MAGICAL, false);
    }
}
