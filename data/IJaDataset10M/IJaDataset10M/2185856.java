package nakayo.gameserver.skillengine.effect.modifier;

import nakayo.gameserver.skillengine.model.Effect;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ActionModifier")
public abstract class ActionModifier {

    /**
     * Applies modifier to original value
     *
     * @param effect
     * @param originalValue
     * @return int
     */
    public abstract int analyze(Effect effect, int originalValue);

    /**
     * Performs check of condition
     *
     * @param effect
     * @return true or false
     */
    public abstract boolean check(Effect effect);
}
