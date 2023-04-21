package prisms.util.persisters;

import prisms.arch.ds.User;

/**
 * A key to an object that is shared between multiple user with varying edit permissions
 */
public interface ShareKey extends Cloneable {

    /** @return Whether this item should be shared between sessions */
    boolean isShared();

    /**
	 * Determines whether a user has permission to view this key's object
	 * 
	 * @param user The user
	 * @return Whether the user can view this key's object
	 */
    boolean canView(User user);

    /**
	 * Determines whether a user has permission to edit this key's object
	 * 
	 * @param user The user
	 * @return Whether the user can edit this key's object
	 */
    boolean canEdit(User user);

    /**
	 * Determines whether a user can administrate this key's object, i.e. change who can view and
	 * edit it
	 * 
	 * @param user The user
	 * @return Whether the user can change who can view/edit this key's object
	 */
    boolean canAdministrate(User user);

    /** @return A copy of this object */
    ShareKey clone();
}
