package com.alexmcchesney.poster;

/**
 * Exception thrown when we are unable to work out the user's home directory
 * @author Alex M
 *
 */
public class UserDirectoryNotFoundException extends PosterException {

    /**
	 * Constructor
	 */
    public UserDirectoryNotFoundException() {
        super(m_resources.getString("USER_DIR_NOT_FOUND_EXCEPTION"));
    }
}
