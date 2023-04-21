package nakayo.gameserver.skillengine.effect.modifier;

import nakayo.gameserver.skillengine.model.Effect;
import nakayo.gameserver.utils.PositionUtil;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FrontDamageModifier")
public class FrontDamageModifier extends ActionModifier {

    @XmlAttribute(required = true)
    protected int delta;

    @XmlAttribute(required = true)
    protected int value;

    @Override
    public int analyze(Effect effect, int originalValue) {
        return originalValue + value + effect.getSkillLevel() * delta;
    }

    @Override
    public boolean check(Effect effect) {
        return PositionUtil.isInFrontOfTarget(effect.getEffector(), effect.getEffected());
    }
}
