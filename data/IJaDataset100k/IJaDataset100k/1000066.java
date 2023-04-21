package com.ericdaugherty.mail.server.configuration.cbc;

import java.util.*;
import com.ericdaugherty.mail.server.configuration.backEnd.PersistExecutor;

/**
 * A single user to add to a number of realms
 * 
 * @author Andreas Kyrmegalos
 */
public final class AddUserToRealmPLL1 extends CBCExecutor {

    private final NewUser user = new NewUser();

    public AddUserToRealmPLL1(ListIterator<String> iter) {
        super(iter);
    }

    public void processLines() {
        String line;
        for (; iter.hasNext(); ) {
            line = iter.next();
            if (line.startsWith(USERNAME)) {
                line = line.substring(USERNAME.length()).trim();
                user.username = line;
            } else if (line.startsWith(USER_ID)) {
                line = line.substring(USER_ID.length()).trim();
                user.userId = Integer.valueOf(line);
            } else if (line.startsWith(DOMAIN_ID)) {
                line = line.substring(DOMAIN_ID.length()).trim();
                user.domainId = Integer.valueOf(line);
            } else if (line.startsWith(PASSWORD)) {
                line = line.substring(PASSWORD.length()).trim();
                user.password = line;
            } else if (line.startsWith(REALM)) {
                user.realms = Arrays.asList(line.substring(REALM.length()).trim().split(","));
            }
        }
    }

    public void execute(PersistExecutor pe) {
        pe.addUserToRealm(user);
    }
}
