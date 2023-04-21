package net.sf.cclearly.logic.events;

import net.sf.cclearly.entities.User;

public interface UserChangeListener {

    public void changeUser(User oldUser, User newUser);
}
