package gameserver.questEngine.handlers.models.xmlQuest;

import gameserver.questEngine.model.QuestCookie;
import gameserver.questEngine.model.QuestState;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * @author Mr. Poke
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QuestVar", propOrder = { "npc" })
public class QuestVar {

    protected List<QuestNpc> npc;

    @XmlAttribute(required = true)
    protected int value;

    public boolean operate(QuestCookie env, QuestState qs) {
        int var = -1;
        if (qs != null) var = qs.getQuestVars().getQuestVars();
        if (var != value) return false;
        for (QuestNpc questNpc : npc) {
            if (questNpc.operate(env, qs)) return true;
        }
        return false;
    }
}
