package barde.log.view;

import java.util.Iterator;
import java.util.TreeSet;

/**
 * Encapsulate a Channel.<br>
 * It provides different methods that allow two ChannelRef to be compared in most of the existing sorted {@link java.util.Collection}s.<br>
 * @author cbonar
 */
public class ChannelRef extends TreeSet implements Comparable {

    protected String name;

    public ChannelRef(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    /** a shortcut to {@link TreeSet#iterator()} */
    public Iterator avatars() {
        return iterator();
    }

    /** <tt>ChannelRef</tt> are compared using their name */
    public boolean equals(Object someOtherChannel) throws ClassCastException {
        return this.name.equals(((ChannelRef) someOtherChannel).getName());
    }

    public int compareTo(Object o) {
        return getName().compareTo(((ChannelRef) o).getName());
    }

    public String toString() {
        return getName();
    }

    public AvatarRef get(String avatar) {
        for (Iterator avit = this.iterator(); avit.hasNext(); ) {
            AvatarRef aref = (AvatarRef) avit.next();
            if (aref.getName().equals(avatar)) return aref;
        }
        return null;
    }
}
