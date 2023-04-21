package org.openaion.gameserver.model.gameobjects.player;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.openaion.gameserver.network.aion.serverpackets.SM_FRIEND_NOTIFY;
import org.openaion.gameserver.network.aion.serverpackets.SM_FRIEND_UPDATE;

/**
 * Represents a player's Friend list
 * 
 * @author Ben
 *
 */
public class FriendList implements Iterable<Friend> {

    public static final int MAX_FRIENDS = 10;

    private Status status = Status.OFFLINE;

    private Queue<Friend> friends;

    private Player player;

    /**
	 * Constructs an empty friend list for the given player
	 * @param player Player who has this friendlist
	 */
    public FriendList(Player player) {
        this(player, new ConcurrentLinkedQueue<Friend>());
    }

    /**
	 * Constructs a friend list for the given player, with the given friends
	 * @param player Player who has this friend list
	 * @param friends Friends on the list
	 */
    public FriendList(Player owner, Collection<Friend> newFriends) {
        this.friends = new ConcurrentLinkedQueue<Friend>(newFriends);
        this.player = owner;
    }

    /**
	 * Gets the friend with this objId<br />
	 * Returns null if it is not our friend
	 * @param objId objId of friend
	 * @return Friend
	 */
    public Friend getFriend(int objId) {
        for (Friend friend : friends) {
            if (friend.getOid() == objId) return friend;
        }
        return null;
    }

    /**
	 * Returns number of friends in list
	 * @return Num Friends in list
	 */
    public int getSize() {
        return friends.size();
    }

    /**
	 * Adds the given friend to the list<br />
	 * To add a friend in the database, see <tt>PlayerService</tt>
	 * @param friend
	 */
    public void addFriend(Friend friend) {
        friends.add(friend);
    }

    /**
	 * Gets the Friend by this name
	 * @param name Name of friend
	 * @return Friend matching name
	 */
    public Friend getFriend(String name) {
        for (Friend friend : friends) if (friend.getName().equalsIgnoreCase(name)) return friend;
        return null;
    }

    /**
	 * Deletes given friend from this friends list<br />
	 * <ul><li>Note: This will only affect this player, not the friend.</li>
	 * <li>Note: Sends the packet to update the client automatically</li>
	 * <li>Note: You should use requestDel to delete from both lists</li></ul>
	 * @param friend
	 */
    public void delFriend(int friendOid) {
        Iterator<Friend> it = iterator();
        while (it.hasNext()) if (it.next().getOid() == friendOid) it.remove();
    }

    public boolean isFull() {
        return getSize() >= MAX_FRIENDS;
    }

    /**
	 * Gets players status
	 * @return Status
	 */
    public Status getStatus() {
        return status;
    }

    /**
	 * Sets the status of the player<br />
	 * <ul><li>Note: Does not update friends</li></ul>
	 * @param status
	 */
    public void setStatus(Status status) {
        Status previousStatus = this.status;
        this.status = status;
        for (Friend friend : friends) {
            if (friend.isOnline()) {
                Player friendPlayer = friend.getPlayer();
                if (friendPlayer == null) continue;
                friendPlayer.getClientConnection().sendPacket(new SM_FRIEND_UPDATE(player.getObjectId()));
                if (previousStatus == Status.OFFLINE) {
                    friendPlayer.getClientConnection().sendPacket(new SM_FRIEND_NOTIFY(SM_FRIEND_NOTIFY.LOGIN, player.getName()));
                } else if (status == Status.OFFLINE) {
                    friendPlayer.getClientConnection().sendPacket(new SM_FRIEND_NOTIFY(SM_FRIEND_NOTIFY.LOGOUT, player.getName()));
                }
            }
        }
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public Iterator<Friend> iterator() {
        return friends.iterator();
    }

    public enum Status {

        /**
		 * User is offline or invisible
		 */
        OFFLINE(0), /**
		 * User is online
		 */
        ONLINE(1), /**
		 * User is away or busy
		 */
        AWAY(2);

        int value;

        private Status(int value) {
            this.value = value;
        }

        public int getIntValue() {
            return value;
        }

        /**
		 * Gets the Status from its int value<br />
		 * Returns null if out of range
		 * @param value range 0-2
		 * @return Status
		 */
        public static Status getByValue(int value) {
            for (Status stat : values()) if (stat.getIntValue() == value) return stat;
            return null;
        }
    }
}
