package com.aionemu.gameserver.questEngine.handlers.models;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javolution.util.FastMap;
import com.aionemu.gameserver.questEngine.QuestEngine;
import com.aionemu.gameserver.questEngine.handlers.template.MonsterHunt;

/**
 * @author MrPoke
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MonsterHuntData", propOrder = { "monsterInfos" })
public class MonsterHuntData extends QuestScriptData {

    @XmlElement(name = "monster_infos", required = true)
    protected List<MonsterInfo> monsterInfos;

    @XmlAttribute(name = "start_npc_id")
    protected int startNpcId;

    @XmlAttribute(name = "end_npc_id")
    protected int endNpcId;

    @Override
    public void register(QuestEngine questEngine) {
        FastMap<Integer, MonsterInfo> monsterInfo = new FastMap<Integer, MonsterInfo>();
        for (MonsterInfo mi : monsterInfos) monsterInfo.put(mi.getNpcId(), mi);
        MonsterHunt template = new MonsterHunt(id, startNpcId, endNpcId, monsterInfo);
        questEngine.addQuestHandler(template);
    }
}
