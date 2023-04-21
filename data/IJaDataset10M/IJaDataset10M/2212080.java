package com.aionemu.gameserver.questEngine.handlers.models;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.aionemu.gameserver.model.templates.quest.QuestItems;
import com.aionemu.gameserver.questEngine.QuestEngine;
import com.aionemu.gameserver.questEngine.handlers.template.WorkOrders;

/**
 * @author Mr. Poke
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WorkOrdersData", propOrder = { "giveComponent" })
public class WorkOrdersData extends QuestScriptData {

    @XmlElement(name = "give_component", required = true)
    protected List<QuestItems> giveComponent;

    @XmlAttribute(name = "start_npc_id", required = true)
    protected int startNpcId;

    @XmlAttribute(name = "recipe_id", required = true)
    protected int recipeId;

    /**
	 * Gets the value of the giveComponent property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
	 * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
	 * the giveComponent property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getGiveComponent().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link QuestItems }
	 * 
	 * 
	 */
    public List<QuestItems> getGiveComponent() {
        if (giveComponent == null) {
            giveComponent = new ArrayList<QuestItems>();
        }
        return this.giveComponent;
    }

    /**
	 * Gets the value of the startNpcId property.
	 * 
	 */
    public int getStartNpcId() {
        return startNpcId;
    }

    /**
	 * Gets the value of the recipeId property.
	 * 
	 */
    public int getRecipeId() {
        return recipeId;
    }

    @Override
    public void register(QuestEngine questEngine) {
        questEngine.addQuestHandler(new WorkOrders(this));
    }
}
