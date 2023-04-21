package gameserver.skill.effect;

import gameserver.skill.action.DamageType;
import gameserver.skill.model.Effect;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SkillAttackInstantEffect")
public class SkillAttackInstantEffect extends DamageEffect {

    @Override
    public void calculate(Effect effect) {
        super.calculate(effect, DamageType.PHYSICAL, true);
    }
}
