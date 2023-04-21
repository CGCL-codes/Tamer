package gameserver.skill.action;

import gameserver.model.gameobjects.Creature;
import gameserver.skill.model.Skill;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HpUseAction")
public class HpUseAction extends Action {

    @Override
    public void act(Skill skill) {
        Creature effector = skill.getEffector();
        int valueWithDelta = 0;
        if (percent) {
            valueWithDelta = effector.getLifeStats().getMaxHp() * value / 100;
        } else valueWithDelta = value + delta * skill.getSkillLevel();
        effector.getLifeStats().reduceHp(valueWithDelta, null);
    }
}
