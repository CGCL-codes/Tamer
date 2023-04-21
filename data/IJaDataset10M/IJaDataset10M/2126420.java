package fr.free.online.sun.frozen.model.dto.quest;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import fr.free.online.sun.frozen.model.dto.abstractDTO.AbstractExternationnalizableDTO;
import fr.free.online.sun.frozen.model.dto.player.IPlayerNeededObject;
import fr.free.online.sun.frozen.model.dto.player.Player;
import fr.free.online.sun.frozen.model.dto.player.PlayerQuest;
import fr.free.online.sun.frozen.model.dto.quest.questInterruptor.IQuestInterruptorNeededObject;
import fr.free.online.sun.frozen.model.dto.quest.questInterruptor.QuestInterruptor;
import fr.free.online.sun.frozen.model.dto.quest.questInterruptor.QuestInterruptorQuest;
import fr.free.online.sun.frozen.model.dto.quest.type.QuestType;

/**
 * Quest is composed of {@link QuestInterruptor}.
 * Quest has a {@link QuestType}.
 * {@link Player} can process Quest.
 * @author $Author: JBGIRAUD $
 * @version $Rev: 43 $
 * @since $Date: 2010-03-12 10:14:24 +0100 (ven., 12 mars 2010) $
 * @see AbstractExternationnalizableDTO
 * @see Player
 * @see QuestInterruptor
 * @see QuestType
 */
@Entity
@Table(name = "FS_QUEST")
public class Quest extends AbstractExternationnalizableDTO implements IPlayerNeededObject, Serializable, IQuestInterruptorNeededObject {

    /**
	 * 
	 */
    private static final long serialVersionUID = 4269888029757626818L;

    /**
	 * Code used to identity internationalization Strings.
	 */
    @Column(name = "CODE", unique = true, nullable = false)
    protected String code;

    /**
	 * ID of the Quest.
	 */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", unique = true, nullable = false)
    protected Integer id;

    /**
	 * {@link Player} who are processing this Quest.
	 */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "neededObject", cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
    private Set<PlayerQuest> playerQuests;

    /**
	 * {@link QuestInterruptor} used by this Quest.
	 */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "neededObject", cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
    private Set<QuestInterruptorQuest> questInterruptorQuests;

    /**
	 * {@link QuestType} of this Quest.
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "QUEST_TYPE_ID", nullable = false)
    private QuestType questType;

    /**
	 * Return true if a Quest has the same ID of another.
	 */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Quest) {
            Quest _obj = (Quest) obj;
            boolean result = _obj.getId().equals(this.getId());
            return result;
        }
        return super.equals(obj);
    }

    /**
	 * Retrieve {@link Player} who are processing this Quest.
	 * @return {@link Player} who are processing this Quest.
	 */
    public Set<PlayerQuest> getQuestPlayers() {
        return this.playerQuests;
    }

    /**
	 * Retrieve {@link QuestInterruptor} used by this Quest.
	 * @return {@link QuestInterruptor} used by this Quest.
	 */
    public Set<QuestInterruptorQuest> getQuestQuestInterruptors() {
        return this.questInterruptorQuests;
    }

    /**
	 * Retrieve {@link QuestType} of this Quest.
	 * @return {@link QuestType} of this Quest.
	 */
    public QuestType getQuestType() {
        return this.questType;
    }

    /**
	 * Set {@link Player} who are processing this Quest.
	 * @param playerQuests {@link Player} who are processing this Quest.
	 */
    public void setQuestPlayers(Set<PlayerQuest> playerQuests) {
        this.playerQuests = playerQuests;
    }

    /**
	 * Set {@link QuestInterruptor} used by this Quest.
	 * @param questInterruptorQuests {@link QuestInterruptor} used by this Quest.
	 */
    public void setQuestQuestInterruptors(Set<QuestInterruptorQuest> questInterruptorQuests) {
        this.questInterruptorQuests = questInterruptorQuests;
    }

    /**
	 * Set {@link QuestType} of this Quest.
	 * @param questType {@link QuestType} of this Quest.
	 */
    public void setQuestType(QuestType questType) {
        this.questType = questType;
    }
}
