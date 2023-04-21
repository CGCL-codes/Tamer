package at.redcross.tacos.web.persistence;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import at.redcross.tacos.dbal.entity.listener.HistoryInterceptor;
import at.redcross.tacos.web.security.WebUserDetails;

public class WebHistoryInterceptor extends HistoryInterceptor {

    private static final long serialVersionUID = -3127982722416199008L;

    @Override
    protected String getAuthenticatedUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() instanceof String) {
            return "(TacosServer)";
        }
        WebUserDetails details = (WebUserDetails) auth.getPrincipal();
        return details.getUsername();
    }
}
