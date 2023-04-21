package org.openaion.gameserver.quest.model;

import org.openaion.gameserver.model.gameobjects.Gatherable;
import org.openaion.gameserver.model.gameobjects.Npc;
import org.openaion.gameserver.model.gameobjects.StaticObject;
import org.openaion.gameserver.model.gameobjects.VisibleObject;
import org.openaion.gameserver.model.gameobjects.player.Player;

/**
 * @author MrPoke, modified Rolandas
 *
 */
public class QuestCookie {

    private VisibleObject visibleObject;

    private Player player;

    private Integer questId;

    private Integer dialogId;

    private int questVars;

    private int workVar;

    private int targetId;

    /**
	 * @param creature
	 * @param player
	 * @param questId
	 */
    public QuestCookie(VisibleObject visibleObject, Player player, int questId, int dialogId) {
        super();
        this.visibleObject = visibleObject;
        this.player = player;
        this.questId = questId;
        this.dialogId = dialogId;
        if (player.getQuestCookie() == null) this.player.setQuestCookie(this);
        if (visibleObject == null) {
            this.targetId = 0;
        } else if (visibleObject instanceof Npc) {
            this.targetId = ((Npc) visibleObject).getNpcId();
        } else if (visibleObject instanceof Gatherable) {
            this.targetId = ((Gatherable) visibleObject).getObjectTemplate().getTemplateId();
        } else if (visibleObject instanceof StaticObject) {
            this.targetId = ((StaticObject) visibleObject).getObjectTemplate().getTemplateId();
        }
        if (player.getQuestCookie().questId == this.questId) {
            this.questVars = player.getQuestCookie().questVars;
            this.workVar = player.getQuestCookie().workVar;
        } else if (this.questId == 0) {
            this.questVars = 0;
            this.workVar = 0;
        }
        this.player.setQuestCookie(this);
    }

    /**
	 * @return the visibleObject
	 */
    public VisibleObject getVisibleObject() {
        return visibleObject;
    }

    /**
	 * @param visibleObject the visibleObject to set
	 */
    public void setVisibleObject(VisibleObject visibleObject) {
        this.visibleObject = visibleObject;
    }

    /**
	 * @return the player
	 */
    public Player getPlayer() {
        return player;
    }

    /**
	 * @param player the player to set
	 */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
	 * @return the questId
	 */
    public Integer getQuestId() {
        return questId;
    }

    /**
	 * @param questId the questId to set
	 */
    public void setQuestId(Integer questId) {
        this.questId = questId;
    }

    /**
	 * @return the dialogId
	 */
    public Integer getDialogId() {
        return dialogId;
    }

    /**
	 * @param dialogId the dialogId to set
	 */
    public void setDialogId(Integer dialogId) {
        this.dialogId = dialogId;
    }

    /**
	 * @return the questVars
	 */
    public int getQuestVars() {
        return questVars;
    }

    /**
	 * @param questVars the questVars to set
	 */
    public void setQuestVars(int questVars) {
        this.questVars = questVars;
    }

    /**
	 * @return the workVar which is the number of active var
	 */
    public int getQuestVarNum() {
        return workVar;
    }

    /**
	 * @param questVarNum the workVar to set
	 */
    public void setQuestWorkVar(int questVarNum) {
        this.workVar = questVarNum;
    }

    /**
	 * @return the targetId
	 */
    public int getTargetId() {
        return targetId;
    }

    /**
	 * @param targetId the targetId to set
	 */
    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }
}
