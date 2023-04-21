package nakayo.gameserver.questEngine.handlers.models.xmlQuest.operations;

import nakayo.gameserver.model.gameobjects.player.Player;
import nakayo.gameserver.network.aion.serverpackets.SM_QUEST_ACCEPTED;
import nakayo.gameserver.questEngine.model.QuestCookie;
import nakayo.gameserver.questEngine.model.QuestState;
import nakayo.gameserver.utils.PacketSendUtility;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Mr. Poke
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SetQuestVarOperation")
public class SetQuestVarOperation extends QuestOperation {

    @XmlAttribute(name = "var_id", required = true)
    protected int varId;

    @XmlAttribute(required = true)
    protected int value;

    @Override
    public void doOperate(QuestCookie env) {
        Player player = env.getPlayer();
        int questId = env.getQuestId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null) {
            qs.getQuestVars().setVarById(varId, value);
            PacketSendUtility.sendPacket(player, new SM_QUEST_ACCEPTED(questId, qs.getStatus(), qs.getQuestVars().getQuestVars()));
        }
    }
}
