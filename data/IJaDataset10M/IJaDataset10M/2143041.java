package gameserver.skill.effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SubEffect")
public class SubEffect {

    @XmlAttribute(name = "skill_id")
    private int skillId;

    /**
	 * @return the skillId
	 */
    public int getSkillId() {
        return skillId;
    }
}
