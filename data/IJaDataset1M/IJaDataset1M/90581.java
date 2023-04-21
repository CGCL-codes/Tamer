package org.tapestrycomponents.tassel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.objectstyle.cayenne.ObjectId;
import org.objectstyle.cayenne.access.DataContext;
import org.objectstyle.cayenne.conf.Configuration;
import org.tapestrycomponents.tassel.domain.User;
import org.tapestrycomponents.tassel.domain.UserStatus;

public class Visit implements Serializable {

    private User user;

    private DataContext dContext;

    private Map ids;

    public Visit() {
        super();
        dContext = createContext();
        ids = new HashMap();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User u) {
        user = u;
    }

    public boolean isAuthenticated() {
        return user == null ? false : user.isAuthenticated();
    }

    public boolean isAdmin() {
        if (user == null) {
            return false;
        }
        if (user.getUserStatus().getStatusLevel().intValue() == UserStatus.LEVEL_ADMIN) {
            return true;
        }
        return false;
    }

    public DataContext getDataContext() {
        return dContext;
    }

    public String getStylesheetPath() {
        return user.getUserStyle().getStylesheetpath();
    }

    private DataContext createContext() {
        return Configuration.getSharedConfiguration().getDomain().createDataContext();
    }

    public void putObjectId(Object key, ObjectId value) {
        ids.put(key, value);
    }

    public ObjectId getObjectId(Object key) {
        return (ObjectId) ids.remove(key);
    }
}
