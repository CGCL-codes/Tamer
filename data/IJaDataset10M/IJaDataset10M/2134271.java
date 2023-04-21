package nakayo.gameserver.questEngine.handlers.models;

import nakayo.gameserver.questEngine.QuestEngine;
import javax.xml.bind.annotation.*;

/**
 * @author MrPoke
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QuestScriptData")
@XmlSeeAlso({ ReportToData.class, MonsterHuntData.class, ItemCollectingData.class, WorkOrdersData.class, XmlQuestData.class })
public abstract class QuestScriptData {

    @XmlAttribute(required = true)
    protected int id;

    /**
     * Gets the value of the id property.
     */
    public int getId() {
        return id;
    }

    public abstract void register(QuestEngine questEngine);
}
