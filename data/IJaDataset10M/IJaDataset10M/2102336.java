package linker.buddy;

import linker.message.MessageItem;
import linker.status.StatusIcons;
import org.jivesoftware.smack.RosterEntry;

/**
 * The ChatCellProxy class.
 * 
 * @version 2008-05-30
 * @author Jianfeng tujf.cn@gmail.com
 * 
 */
public class BuddyItem extends MessageItem {

    /**
	 * The buddy's roster entry .
	 */
    private RosterEntry entry;

    /**
	 * The buddyList.
	 */
    private BuddyList buddyList;

    /**
	 * The constructor of chatCellProxy.
	 * 
	 * @param buddyEntry
	 *            The buddy's rosterentry.
	 * @param list
	 *            BuddyList
	 */
    public BuddyItem(final RosterEntry buddyEntry, final BuddyList list) {
        super(null, buddyEntry.getUser());
        this.entry = buddyEntry;
        this.buddyList = list;
        String title = buddyEntry.getName();
        if (title == null) {
            title = buddyEntry.getUser();
        }
        this.setTitle(title);
        this.setIcon(StatusIcons.getOffIcon());
    }

    /**
	 * Get rosterentry.
	 * 
	 * @return The rosterEntry.
	 */
    public final RosterEntry getEntry() {
        return entry;
    }

    /**
	 * Get the chatCell.
	 * 
	 * @return The chatCell.
	 */
    public final BuddyChat getBuddyChat() {
        return buddyList.getBuddyChatByItem(this);
    }

    /**
	 * Get the flag whether it is offline or not.
	 * 
	 * @return The offline flag.
	 */
    public final boolean isOffline() {
        return (this.getIcon() == StatusIcons.getOffIcon());
    }

    /**
	 * Catch the double click action.
	 */
    public final void doubleClick() {
        getBuddyChat().doubleClick();
    }
}
