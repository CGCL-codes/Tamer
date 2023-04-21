package com.cosmos.test.bl;

import static com.cosmos.acacia.gui.AcaciaPanel.getBean;
import java.util.UUID;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.junit.Before;
import com.cosmos.acacia.app.AcaciaSessionRemote;
import com.cosmos.acacia.crm.bl.users.UsersRemote;
import com.cosmos.acacia.crm.client.LocalSession;
import com.cosmos.acacia.crm.data.contacts.Address;
import com.cosmos.acacia.crm.data.contacts.Organization;
import com.cosmos.acacia.crm.data.users.User;
import com.cosmos.acacia.crm.gui.AcaciaApplication;
import java.util.UUID;

/**
 * Created	:	28.07.2008
 * @author	Petar Milev
 *
 */
public class BaseTest {

    private static Map<String, Object> sessionCache = new HashMap<String, Object>();

    protected UsersRemote usersRemote = getBean(UsersRemote.class, false);

    protected AcaciaSessionRemote session = LocalSession.instance();

    protected Random random = new Random();

    private boolean sessionCaching = true;

    @Before
    public void setUp() {
        login("admin", "admin");
    }

    public void login(String userName, String password) {
        throw new UnsupportedOperationException("ToDo");
    }

    public User getUser() {
        User result = (User) sessionCache.get("user");
        if (result == null) {
            result = session.getUser();
            sessionCache.put("user", result);
        }
        return result;
    }

    public Organization getOrganization() {
        Organization result = (Organization) sessionCache.get("organization");
        if (result == null) {
            result = session.getOrganization();
            sessionCache.put("organization", result);
        }
        return result;
    }

    public UUID getOrganizationId() {
        return getOrganization().getId();
    }

    public Address getBranch() {
        Address result = (Address) sessionCache.get("branch");
        if (result == null) {
            result = session.getBranch();
            sessionCache.put("branch", result);
        }
        return result;
    }

    public boolean isSessionCaching() {
        return sessionCaching;
    }

    public void clearSessionCache() {
        sessionCache.clear();
    }

    public void setSessionCaching(boolean sessionCaching) {
        this.sessionCaching = sessionCaching;
        if (!sessionCaching) sessionCache.clear();
    }

    public AcaciaSessionRemote getSession() {
        return session;
    }
}
