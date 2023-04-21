package gameserver.model.templates;

import gameserver.model.Gender;
import gameserver.model.PlayerClass;
import gameserver.model.Race;
import gameserver.model.templates.quest.CollectItems;
import gameserver.model.templates.quest.QuestDrop;
import gameserver.model.templates.quest.QuestItems;
import gameserver.model.templates.quest.QuestWorkItems;
import gameserver.model.templates.quest.Rewards;
import gameserver.quest.model.QuestStartConditions;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Quest", propOrder = { "collectItems", "rewards", "extRewards", "questDrop", "finishedQuestConds", "unfinishedQuestConds", "acquiredQuestConds", "noacquiredQuestConds", "classPermitted", "genderPermitted", "questWorkItems", "fighterSelectableReward", "knightSelectableReward", "rangerSelectableReward", "assassinSelectableReward", "wizardSelectableReward", "elementalistSelectableReward", "priestSelectableReward", "chanterSelectableReward" })
public class QuestTemplate {

    @XmlElement(name = "collect_items")
    protected CollectItems collectItems;

    protected List<Rewards> rewards;

    @XmlElement(name = "extrewards")
    protected List<Rewards> extRewards;

    @XmlElement(name = "quest_drop")
    protected List<QuestDrop> questDrop;

    @XmlElement(name = "finished_quest_conds")
    protected QuestStartConditions finishedQuestConds;

    @XmlElement(name = "unfinished_quest_conds")
    protected QuestStartConditions unfinishedQuestConds;

    @XmlElement(name = "acquired_quest_conds")
    protected QuestStartConditions acquiredQuestConds;

    @XmlElement(name = "noacquired_quest_conds")
    protected QuestStartConditions noacquiredQuestConds;

    @XmlList
    @XmlElement(name = "class_permitted")
    protected List<PlayerClass> classPermitted;

    @XmlElement(name = "gender_permitted")
    protected Gender genderPermitted;

    @XmlElement(name = "quest_work_items")
    protected QuestWorkItems questWorkItems;

    @XmlElement(name = "fighter_selectable_reward")
    protected List<QuestItems> fighterSelectableReward;

    @XmlElement(name = "knight_selectable_reward")
    protected List<QuestItems> knightSelectableReward;

    @XmlElement(name = "ranger_selectable_reward")
    protected List<QuestItems> rangerSelectableReward;

    @XmlElement(name = "assassin_selectable_reward")
    protected List<QuestItems> assassinSelectableReward;

    @XmlElement(name = "wizard_selectable_reward")
    protected List<QuestItems> wizardSelectableReward;

    @XmlElement(name = "elementalist_selectable_reward")
    protected List<QuestItems> elementalistSelectableReward;

    @XmlElement(name = "priest_selectable_reward")
    protected List<QuestItems> priestSelectableReward;

    @XmlElement(name = "chanter_selectable_reward")
    protected List<QuestItems> chanterSelectableReward;

    @XmlAttribute(required = true)
    protected int id;

    @XmlAttribute
    protected String name;

    @XmlAttribute
    protected Integer nameId;

    @XmlAttribute(name = "minlevel_permitted")
    protected Integer minlevelPermitted;

    @XmlAttribute(name = "maxlevel_permitted")
    protected Integer maxlevelPermitted = 0;

    @XmlAttribute(name = "max_repeat_count")
    protected Integer maxRepeatCount;

    @XmlAttribute(name = "repeat_day")
    protected Integer repeatDay = 0;

    @XmlAttribute(name = "cannot_share")
    protected Boolean cannotShare;

    @XmlAttribute(name = "cannot_giveup")
    protected Boolean cannotGiveup;

    @XmlAttribute(name = "use_class_reward")
    protected Integer useClassReward;

    @XmlAttribute(name = "race_permitted")
    protected Race racePermitted;

    @XmlAttribute
    protected Integer combineskill;

    @XmlAttribute(name = "combine_skillpoint")
    protected Integer combineSkillpoint;

    @XmlAttribute(name = "timer")
    protected Boolean timer;

    /**
	 * Gets the value of the collectItems property.
	 * 
	 * @return possible object is {@link CollectItems }
	 * 
	 */
    public CollectItems getCollectItems() {
        return collectItems;
    }

    /**
	 * Gets the value of the rewards property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
	 * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
	 * the rewards property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getRewards().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link Rewards }
	 * 
	 * 
	 */
    public List<Rewards> getRewards() {
        if (rewards == null) {
            rewards = new ArrayList<Rewards>();
        }
        return this.rewards;
    }

    public List<Rewards> getExtRewards() {
        return this.extRewards;
    }

    /**
	 * Gets the value of the questDrop property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
	 * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
	 * the questDrop property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getQuestDrop().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link QuestDrop }
	 * 
	 * 
	 */
    public List<QuestDrop> getQuestDrop() {
        if (questDrop == null) {
            questDrop = new ArrayList<QuestDrop>();
        }
        return this.questDrop;
    }

    /**
	 * Gets the value of the finishedQuestConds property.
	 * 
	 */
    public QuestStartConditions getFinishedQuestConds() {
        return finishedQuestConds;
    }

    /**
	 * Gets the value of the unfinishedQuestConds property.
	 * 
	 */
    public QuestStartConditions getUnfinishedQuestConds() {
        return unfinishedQuestConds;
    }

    /**
	 * Gets the value of the acquiredQuestConds property.
	 * 
	 */
    public QuestStartConditions getAcquiredQuestConds() {
        return acquiredQuestConds;
    }

    /**
	 * Gets the value of the finishedQuestConds property.
	 * 
	 */
    public QuestStartConditions getNoacquiredQuestConds() {
        return noacquiredQuestConds;
    }

    /**
	 * Gets the value of the classPermitted property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
	 * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
	 * the classPermitted property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getClassPermitted().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link PlayerClass }
	 * 
	 * 
	 */
    public List<PlayerClass> getClassPermitted() {
        if (classPermitted == null) {
            classPermitted = new ArrayList<PlayerClass>();
        }
        return this.classPermitted;
    }

    /**
	 * Gets the value of the genderPermitted property.
	 * 
	 * @return possible object is {@link Gender }
	 * 
	 */
    public Gender getGenderPermitted() {
        return genderPermitted;
    }

    /**
	 * Gets the value of the questWorkItems property.
	 * 
	 * @return possible object is {@link QuestWorkItems }
	 * 
	 */
    public QuestWorkItems getQuestWorkItems() {
        return questWorkItems;
    }

    /**
	 * Gets the value of the fighterSelectableReward property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
	 * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
	 * the fighterSelectableReward property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getFighterSelectableReward().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link QuestItems }
	 * 
	 * 
	 */
    public List<QuestItems> getFighterSelectableReward() {
        if (fighterSelectableReward == null) {
            fighterSelectableReward = new ArrayList<QuestItems>();
        }
        return this.fighterSelectableReward;
    }

    /**
	 * Gets the value of the knightSelectableReward property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
	 * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
	 * the knightSelectableReward property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getKnightSelectableReward().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link QuestItems }
	 * 
	 * 
	 */
    public List<QuestItems> getKnightSelectableReward() {
        if (knightSelectableReward == null) {
            knightSelectableReward = new ArrayList<QuestItems>();
        }
        return this.knightSelectableReward;
    }

    /**
	 * Gets the value of the rangerSelectableReward property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
	 * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
	 * the rangerSelectableReward property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getRangerSelectableReward().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link QuestItems }
	 * 
	 * 
	 */
    public List<QuestItems> getRangerSelectableReward() {
        if (rangerSelectableReward == null) {
            rangerSelectableReward = new ArrayList<QuestItems>();
        }
        return this.rangerSelectableReward;
    }

    /**
	 * Gets the value of the assassinSelectableReward property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
	 * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
	 * the assassinSelectableReward property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getAssassinSelectableReward().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link QuestItems }
	 * 
	 * 
	 */
    public List<QuestItems> getAssassinSelectableReward() {
        if (assassinSelectableReward == null) {
            assassinSelectableReward = new ArrayList<QuestItems>();
        }
        return this.assassinSelectableReward;
    }

    /**
	 * Gets the value of the wizardSelectableReward property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
	 * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
	 * the wizardSelectableReward property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getWizardSelectableReward().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link QuestItems }
	 * 
	 * 
	 */
    public List<QuestItems> getWizardSelectableReward() {
        if (wizardSelectableReward == null) {
            wizardSelectableReward = new ArrayList<QuestItems>();
        }
        return this.wizardSelectableReward;
    }

    /**
	 * Gets the value of the elementalistSelectableReward property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
	 * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
	 * the elementalistSelectableReward property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getElementalistSelectableReward().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link QuestItems }
	 * 
	 * 
	 */
    public List<QuestItems> getElementalistSelectableReward() {
        if (elementalistSelectableReward == null) {
            elementalistSelectableReward = new ArrayList<QuestItems>();
        }
        return this.elementalistSelectableReward;
    }

    /**
	 * Gets the value of the priestSelectableReward property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
	 * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
	 * the priestSelectableReward property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getPriestSelectableReward().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link QuestItems }
	 * 
	 * 
	 */
    public List<QuestItems> getPriestSelectableReward() {
        if (priestSelectableReward == null) {
            priestSelectableReward = new ArrayList<QuestItems>();
        }
        return this.priestSelectableReward;
    }

    /**
	 * Gets the value of the chanterSelectableReward property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
	 * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
	 * the chanterSelectableReward property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getChanterSelectableReward().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link QuestItems }
	 * 
	 * 
	 */
    public List<QuestItems> getChanterSelectableReward() {
        if (chanterSelectableReward == null) {
            chanterSelectableReward = new ArrayList<QuestItems>();
        }
        return this.chanterSelectableReward;
    }

    /**
	 * Gets the value of the id property.
	 * 
	 */
    public int getId() {
        return id;
    }

    /**
	 * Gets the value of the name property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
    public String getName() {
        return name;
    }

    /**
	 * Gets the value of the nameId property.
	 * 
	 * @return possible object is {@link Integer }
	 * 
	 */
    public Integer getNameId() {
        return nameId;
    }

    /**
	 * Gets the value of the minlevelPermitted property.
	 * 
	 * @return possible object is {@link Integer }
	 * 
	 */
    public Integer getMinlevelPermitted() {
        return minlevelPermitted == 99 ? 1 : minlevelPermitted;
    }

    public Integer getMaxlevelPermitted() {
        return maxlevelPermitted;
    }

    /**
	 * Gets the value of the maxRepeatCount property.
	 * 
	 * @return possible object is {@link Integer }
	 * 
	 */
    public Integer getMaxRepeatCount() {
        return maxRepeatCount;
    }

    public Integer getRepeatDay() {
        return repeatDay;
    }

    public boolean isWeeklyActive() {
        if (repeatDay == 0) return true;
        Calendar now = Calendar.getInstance();
        int dayValue = now.get(Calendar.DAY_OF_WEEK);
        if (dayValue == 1) dayValue = 7; else dayValue--;
        return dayValue == repeatDay;
    }

    /**
	 * Gets the value of the cannotShare property.
	 * 
	 * @return possible object is {@link Boolean }
	 * 
	 */
    public boolean isCannotShare() {
        if (cannotShare == null) {
            return false;
        } else {
            return cannotShare;
        }
    }

    /**
	 * Gets the value of the cannotGiveup property.
	 * 
	 * @return possible object is {@link Boolean }
	 * 
	 */
    public boolean isCannotGiveup() {
        if (cannotGiveup == null) {
            return false;
        } else {
            return cannotGiveup;
        }
    }

    /**
	 * Gets the value of the useClassReward property.
	 * 
	 * @return possible object is {@link Boolean }
	 * 
	 */
    public int isUseClassReward() {
        if (useClassReward == null) {
            return 0;
        } else {
            return useClassReward;
        }
    }

    /**
	 * Gets the value of the racePermitted property.
	 * 
	 * @return possible object is {@link Race }
	 * 
	 */
    public Race getRacePermitted() {
        return racePermitted;
    }

    /**
	 * Gets the value of the combineskill property.
	 * 
	 * @return possible object is {@link Integer }
	 * 
	 */
    public Integer getCombineSkill() {
        return combineskill;
    }

    /**
	 * Gets the value of the combineSkillpoint property.
	 * 
	 * @return possible object is {@link Integer }
	 * 
	 */
    public Integer getCombineSkillPoint() {
        return combineSkillpoint;
    }

    /**
	 * Gets the value of the timer property.
	 * 
	 * @return possible object is {@link Integer }
	 * 
	 */
    public boolean isTimer() {
        if (timer == null) {
            return false;
        } else {
            return timer;
        }
    }
}
