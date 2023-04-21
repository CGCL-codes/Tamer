package net.sf.woko.usermgt;

import net.sf.woko.controller.stripes.WokoStripesConfiguration;
import net.sourceforge.stripes.config.Configuration;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

public class DefaultWokoUserResolutionStrategy implements WokoUserResolutionStrategy {

    private WokoStripesConfiguration configuration;

    public WokoUser getCurrentUser(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        if (principal == null) {
            return null;
        }
        String userName = principal.getName();
        return configuration.getUserManager().findUser(userName);
    }

    public void init(Configuration configuration) throws Exception {
        this.configuration = (WokoStripesConfiguration) configuration;
    }

    public WokoStripesConfiguration getConfiguration() {
        return configuration;
    }
}
