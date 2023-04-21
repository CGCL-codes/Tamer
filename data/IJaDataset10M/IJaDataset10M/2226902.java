package de.haumacher.timecollect.server.db;

import java.util.List;
import de.haumacher.timecollect.common.db.Context;
import de.haumacher.timecollect.common.db.DBException;
import de.haumacher.timecollect.server.User;

public interface StorageLookup extends Context {

    User getUser(String uid) throws DBException;

    List<User> getUsers() throws DBException;
}
