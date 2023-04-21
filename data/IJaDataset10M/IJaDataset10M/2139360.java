package gameserver.model.legion;

import gameserver.dataholders.DataManager;
import gameserver.model.PlayerClass;
import gameserver.model.gameobjects.player.Player;
import java.sql.Timestamp;
import org.apache.log4j.Logger;

public class LegionMemberEx extends LegionMember {

    private static Logger log = Logger.getLogger(LegionMemberEx.class);

    private String name;

    private PlayerClass playerClass;

    private int level;

    private Timestamp lastOnline;

    private int worldId;

    private boolean online = false;

    /**
	 * If player is immediately after this constructor is called
	 */
    public LegionMemberEx(Player player, LegionMember legionMember, boolean online) {
        super(player.getObjectId(), legionMember.getLegion(), legionMember.getRank());
        this.nickname = legionMember.getNickname();
        this.selfIntro = legionMember.getSelfIntro();
        this.name = player.getName();
        this.playerClass = player.getPlayerClass();
        this.level = player.getLevel();
        this.lastOnline = player.getCommonData().getLastOnline();
        this.worldId = player.getPosition().getMapId();
        this.online = online;
    }

    /**
	 * If player is defined later on this constructor is called
	 */
    public LegionMemberEx(int playerObjId) {
        super(playerObjId);
    }

    /**
	 * If player is defined later on this constructor is called
	 */
    public LegionMemberEx(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PlayerClass getPlayerClass() {
        return playerClass;
    }

    public void setPlayerClass(PlayerClass playerClass) {
        this.playerClass = playerClass;
    }

    public int getLastOnline() {
        if (lastOnline == null || isOnline()) return 0;
        return (int) (lastOnline.getTime() / 1000);
    }

    public void setLastOnline(Timestamp timestamp) {
        lastOnline = timestamp;
    }

    public int getLevel() {
        return level;
    }

    /**
	 * sets the exp value
	 * 
	 * @param admin
	 *            : enable decrease level
	 */
    public void setExp(long exp) {
        int maxLevel = DataManager.PLAYER_EXPERIENCE_TABLE.getMaxLevel();
        if (getPlayerClass() != null && getPlayerClass().isStartingClass()) maxLevel = 10;
        long maxExp = DataManager.PLAYER_EXPERIENCE_TABLE.getStartExpForLevel(maxLevel);
        int level = 1;
        if (exp > maxExp) {
            exp = maxExp;
        }
        while ((level + 1) != maxLevel && exp >= DataManager.PLAYER_EXPERIENCE_TABLE.getStartExpForLevel(level + 1)) {
            level++;
        }
        this.level = level;
    }

    public int getWorldId() {
        return worldId;
    }

    public void setWorldId(int worldId) {
        this.worldId = worldId;
    }

    /**
	 * @param online
	 *            the online to set
	 */
    public void setOnline(boolean online) {
        this.online = online;
    }

    /**
	 * @return the online
	 */
    public boolean isOnline() {
        return online;
    }

    public boolean sameObjectId(int objectId) {
        return getObjectId() == objectId;
    }

    /**
	 * Checks if a LegionMemberEx is valid or not
	 * 
	 * @return true if LegionMemberEx is valid
	 */
    public boolean isValidLegionMemberEx() {
        if (getObjectId() < 1) {
            log.error("[LegionMemberEx] Player Object ID is empty.");
        } else if (getName() == null) {
            log.error("[LegionMemberEx] Player Name is empty." + getObjectId());
        } else if (getPlayerClass() == null) {
            log.error("[LegionMemberEx] Player Class is empty." + getObjectId());
        } else if (getLevel() < 1) {
            log.error("[LegionMemberEx] Player Level is empty." + getObjectId());
        } else if (getLastOnline() == 0) {
            log.error("[LegionMemberEx] Last Online is empty." + getObjectId());
        } else if (getWorldId() < 1) {
            log.error("[LegionMemberEx] World Id is empty." + getObjectId());
        } else if (getLegion() == null) {
            log.error("[LegionMemberEx] Legion is empty." + getObjectId());
        } else if (getRank() == null) {
            log.error("[LegionMemberEx] Rank is empty." + getObjectId());
        } else if (getNickname() == null) {
            log.error("[LegionMemberEx] Nickname is empty." + getObjectId());
        } else if (getSelfIntro() == null) {
            log.error("[LegionMemberEx] Self Intro is empty." + getObjectId());
        } else {
            return true;
        }
        return false;
    }
}
