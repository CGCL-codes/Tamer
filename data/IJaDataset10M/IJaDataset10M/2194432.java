package gameserver.questEngine.handlers.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author MrPoke
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MonsterInfo")
public class MonsterInfo {

    @XmlAttribute(name = "var_id", required = true)
    protected int varId;

    @XmlAttribute(name = "min_var_value")
    protected Integer minVarValue;

    @XmlAttribute(name = "max_kill", required = true)
    protected int maxKill;

    @XmlAttribute(name = "npc_id", required = true)
    protected int npcId;

    /**
     * Gets the value of the varId property.
     */
    public int getVarId() {
        return varId;
    }

    /**
     * Gets the value of the minVarValue property.
     *
     * @return possible object is {@link Integer }
     */
    public Integer getMinVarValue() {
        return minVarValue;
    }

    /**
     * Gets the value of the maxKill property.
     */
    public int getMaxKill() {
        return maxKill;
    }

    /**
     * Gets the value of the npcId property.
     */
    public int getNpcId() {
        return npcId;
    }
}
