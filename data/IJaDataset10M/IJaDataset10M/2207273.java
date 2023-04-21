package de.ah7.imp.auth07;

import de.ah7.lib.authorization.AuthorizationException;
import de.ah7.lib.authorization.User;
import de.ah7.lib.authorization.UserGroup;
import de.ah7.lib.authorization.GroupExpansionService;
import de.ah7.lib.authorization.UserGroupMember;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Andreas Huber <dev@ah7.de>
 */
public class StandardGroupExpander extends AbstractGroupExpander implements GroupExpansionService {

    public static final String PREFIX_ALL = "all";

    public static final String PREFIX_ANON = "anon";

    public static final String PREFIX_AUTH = "auth";

    public static final String PREFIX_ID = "id";

    public static final String PREFIX_NONE = "none";

    public static final String[] PREFICES = { PREFIX_ALL, PREFIX_ANON, PREFIX_AUTH, PREFIX_ID, PREFIX_NONE };

    public static final String PSEUDO_USER_ALL = "@ALL";

    public static final String PSEUDO_USER_AUTH = "@AUTHORIZED";

    private static Log log = LogFactory.getLog(StandardGroupExpander.class);

    public StandardGroupExpander() {
    }

    public Collection<String> getSupportetPrefices() {
        Collection<String> result = new HashSet<String>();
        for (int i = 0; i < PREFICES.length; i++) {
            result.add(PREFICES[i]);
        }
        return result;
    }

    public Collection<? extends UserGroupMember> expandGroup(UserGroup group) throws AuthorizationException {
        Set<User> result = new HashSet<User>();
        String prefix = group.getGroupPrefix();
        if (PREFIX_ALL.equals(prefix)) {
            result.add(new UserBean(PSEUDO_USER_ALL));
        } else if (PREFIX_AUTH.equals(prefix)) {
            result.add(new UserBean(PSEUDO_USER_AUTH));
        } else if (PREFIX_ID.equals(prefix)) {
            result.add(new UserBean(group.getGroupName()));
        } else if (PREFIX_NONE.equals(prefix)) {
        } else {
            throw new AuthorizationException("can't resolve group for unknown prefix \'" + group.getGroupPrefix() + "\'!");
        }
        return result;
    }
}
