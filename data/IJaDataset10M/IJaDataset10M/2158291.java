package ru.yep.forum.core;

import java.util.HashMap;
import java.util.Map;

/**
 * it contains profile data related to user.
 * 
 * @author Oleg Orlov
 */
public class UserProfile {

    public static final String EMAIL = "email";

    private final int id;

    private final Map properties = new HashMap();

    public UserProfile(int id) {
        this.id = id;
    }
}
