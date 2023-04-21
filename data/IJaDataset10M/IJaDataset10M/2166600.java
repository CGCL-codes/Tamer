package org.nakedobjects.application.tracker.resources;

import java.util.List;
import org.nakedobjects.applib.AbstractFactoryAndRepository;
import org.nakedobjects.application.tracker.User;

public class Users extends AbstractFactoryAndRepository {

    public List<User> allUsers() {
        return allInstances(User.class);
    }

    public List<User> findUsersByName(String text) {
        return allMatches(User.class, text);
    }
}
