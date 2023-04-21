package org.openaion.gameserver.model.gameobjects.player;

import org.openaion.gameserver.model.PlayerClass;
import org.openaion.gameserver.model.gameobjects.player.FriendList.Status;

/**
 * @author Ben
 *
 */
public class Friend {

    private final PlayerCommonData pcd;

    public Friend(PlayerCommonData pcd) {
        this.pcd = pcd;
    }

    /**
	 * Returns the status of this player 
	 * @return Friend's status
	 */
    public Status getStatus() {
        if (pcd.getPlayer() == null || !pcd.isOnline()) {
            return FriendList.Status.OFFLINE;
        }
        return pcd.getPlayer().getFriendList().getStatus();
    }

    /**
	 * Returns this friend's name
	 * @return Friend's name
	 */
    public String getName() {
        return pcd.getName();
    }

    public int getLevel() {
        return pcd.getLevel();
    }

    public String getNote() {
        return pcd.getNote();
    }

    public PlayerClass getPlayerClass() {
        return pcd.getPlayerClass();
    }

    public int getMapId() {
        return pcd.getPosition().getMapId();
    }

    /**
	 * Gets the last time this player was online as a unix timestamp<br />
	 * Returns 0 if the player is online now
	 * @return Unix timestamp the player was last online
	 */
    public int getLastOnlineTime() {
        if (pcd.getLastOnline() == null || isOnline()) return 0;
        return (int) (pcd.getLastOnline().getTime() / 1000);
    }

    public int getOid() {
        return pcd.getPlayerObjId();
    }

    public Player getPlayer() {
        return pcd.getPlayer();
    }

    public boolean isOnline() {
        return pcd.isOnline();
    }
}
