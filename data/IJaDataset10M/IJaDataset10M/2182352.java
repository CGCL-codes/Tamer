package org.maestroframework.maestro.accessControl;

import java.util.Arrays;
import java.util.List;
import org.maestroframework.maestro.interfaces.AccessControlMethod;
import org.maestroframework.maestro.model.MaestroContext;
import org.maestroframework.maestro.model.MaestroUser;

public class UsernameACL implements AccessControlMethod {

    @Override
    public List<String> getACLTags() {
        return Arrays.asList(new String[] { "u", "user" });
    }

    @Override
    public boolean verify(MaestroContext ctx, String tag, String acl, Object testObject) {
        MaestroUser user = ctx.getMaestroUser();
        if ("nobody".equals(acl)) {
            return !ctx.isUserLoggedIn();
        }
        if (user != null) {
            return acl.equals(user.getName());
        } else {
            return false;
        }
    }
}
