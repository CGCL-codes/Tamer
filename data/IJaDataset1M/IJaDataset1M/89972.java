package nakayo.gameserver.skillengine.condition;

import nakayo.gameserver.model.gameobjects.Npc;
import nakayo.gameserver.model.gameobjects.player.Player;
import nakayo.gameserver.skillengine.model.Skill;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TargetCondition")
public class TargetCondition extends Condition {

    @XmlAttribute(required = true)
    protected TargetAttribute value;

    /**
     * Gets the value of the value property.
     *
     * @return possible object is
     *         {@link TargetAttribute }
     */
    public TargetAttribute getValue() {
        return value;
    }

    @Override
    public boolean verify(Skill skill) {
        if (value != TargetAttribute.NONE && skill.getFirstTarget() == null) {
            return false;
        }
        switch(value) {
            case NPC:
                return skill.getFirstTarget() instanceof Npc;
            case PC:
                return skill.getFirstTarget() instanceof Player;
            default:
                return false;
        }
    }
}
