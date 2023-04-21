package nakayo.gameserver.questEngine.handlers.models.xmlQuest.conditions;

import nakayo.gameserver.questEngine.model.ConditionUnionType;
import nakayo.gameserver.questEngine.model.QuestCookie;
import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * @author Mr. Poke
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QuestConditions", propOrder = { "conditions" })
public class QuestConditions {

    @XmlElements({ @XmlElement(name = "quest_status", type = QuestStatusCondition.class), @XmlElement(name = "npc_id", type = NpcIdCondition.class), @XmlElement(name = "pc_inventory", type = PcInventoryCondition.class), @XmlElement(name = "quest_var", type = QuestVarCondition.class), @XmlElement(name = "dialog_id", type = DialogIdCondition.class) })
    protected List<QuestCondition> conditions;

    @XmlAttribute(required = true)
    protected ConditionUnionType operate;

    public boolean checkConditionOfSet(QuestCookie env) {
        boolean inCondition = (operate == ConditionUnionType.AND);
        for (QuestCondition cond : conditions) {
            boolean bCond = cond.doCheck(env);
            switch(operate) {
                case AND:
                    if (!bCond) return false;
                    inCondition = inCondition && bCond;
                    break;
                case OR:
                    if (bCond) return true;
                    inCondition = inCondition || bCond;
                    break;
            }
        }
        return inCondition;
    }
}
