package gameserver.dao;

import gameserver.model.gameobjects.player.FriendList;
import gameserver.model.gameobjects.player.Player;
import commons.database.dao.DAO;

public abstract class FriendListDAO implements DAO {

    /**
	 * {@inheritDoc}
	 */
    @Override
    public String getClassName() {
        return FriendListDAO.class.getName();
    }

    /**
	 * Loads the friend list for the given player
	 * @param player Player to get friend list of
	 * @return FriendList for player
	 */
    public abstract FriendList load(final Player player);

    /**
	 * Makes the given players friends
	 * <ul><li>Note: Adds for both players</li></ul>
	 * @param player Player who is adding
	 * @param friend Friend to add to the friend list
	 * @return Success
	 */
    public abstract boolean addFriends(final Player player, final Player friend);

    /**
	 * Deletes the friends from eachothers lists
	 * @param player Player whos is deleting
	 * @param friendName Name of friend to delete
	 * @return Success
	 */
    public abstract boolean delFriends(final int playerOid, final int friendOid);
}
