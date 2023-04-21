package org.maestroframework.maestro.accessControl;

import java.util.Arrays;
import java.util.List;
import org.maestroframework.maestro.interfaces.AccessControlMethod;
import org.maestroframework.maestro.model.MaestroContext;
import org.maestroframework.maestro.model.MaestroUser;

public class GroupACL implements AccessControlMethod {

    @Override
    public List<String> getACLTags() {
        return Arrays.asList(new String[] { "g", "group", "role" });
    }

    @Override
    public boolean verify(MaestroContext ctx, String tag, String acl, Object testObject) {
        if ("public".equals(acl)) return true;
        MaestroUser user = ctx.getMaestroUser();
        if (user == null) return false;
        List<String> roles = user.getGroups();
        if (roles != null) {
            return roles.contains(acl);
        } else {
            return false;
        }
    }
}
