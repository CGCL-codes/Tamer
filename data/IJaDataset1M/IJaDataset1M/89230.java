package com.privilege.displayable;

import org.directwebremoting.util.Logger;

/**
 * A POJO that represents a typed user
 * @author Ankh MAMBI
 */
public class User {

    private long id = System.currentTimeMillis();

    private String userName;

    private static final Logger logger = Logger.getLogger(User.class);

    private int USERNAME_LENGTH_MAX = 20;

    /**
     * @param newtext the new message text
     */
    public User(String userName) {
        logger.debug("Method: DisplayableUser(" + userName + ")");
        this.userName = userName;
        if (userName != null) if (userName.length() > USERNAME_LENGTH_MAX) this.userName = userName.substring(0, USERNAME_LENGTH_MAX);
    }

    /**
     * @return the message id
     */
    public long getId() {
        logger.debug("Method: getId(" + ")");
        return id;
    }

    /**
     * @return the message itself
     */
    public String getText() {
        logger.debug("Method: getText(" + ")");
        return userName;
    }

    public boolean equals(Object o) {
        logger.debug("Method: equals(" + o + ")");
        if (o instanceof User) {
            if (((User) o).getText().equals(userName)) return true;
        }
        return false;
    }
}
