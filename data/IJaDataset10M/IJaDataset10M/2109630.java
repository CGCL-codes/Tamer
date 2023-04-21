package org.openaion.gameserver.skill.effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import org.openaion.gameserver.skill.action.DamageType;
import org.openaion.gameserver.skill.model.Effect;

/**
 * @author ATracer
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SkillAttackInstantEffect")
public class SkillAttackInstantEffect extends DamageEffect {

    @Override
    public void calculate(Effect effect) {
        super.calculate(effect, DamageType.PHYSICAL, true);
    }
}
