package org.helianto.web.action;

import org.helianto.core.UserGroup;

/**
 * User group resolver interface.
 * 
 * @author mauriciofernandesdecastro
 */
public interface UserGroupResolver {

    /**
	 * Return an <code>UserGroup</code> given a clue.
	 * 
	 * @param clue
	 */
    public UserGroup resolveUserGroup(String clue);
}
