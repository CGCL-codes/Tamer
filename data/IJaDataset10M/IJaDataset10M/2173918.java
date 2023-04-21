package nakayo.gameserver.model.templates.siege;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * @author xitanium
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ArtifactEffectTemplate {

    @XmlAttribute(name = "skillid")
    protected int skillId;

    @XmlAttribute(name = "range")
    protected String range;

    public int getSkillId() {
        return skillId;
    }

    public String getRange() {
        return range;
    }
}
