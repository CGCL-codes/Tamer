package gameserver.skill.condition;

import gameserver.skill.model.Skill;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Condition")
public abstract class Condition {

    /**
	 *  Verify condition specified in template
	 * @param env
	 * @return true or false
	 */
    public abstract boolean verify(Skill env);
}
