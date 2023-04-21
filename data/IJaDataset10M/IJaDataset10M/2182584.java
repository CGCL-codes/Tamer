package flex.contrib.messaging.security;

import java.security.Principal;
import java.util.Map;
import javax.servlet.ServletContext;
import org.springframework.context.ApplicationContext;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.ProviderManager;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;
import org.springframework.web.context.support.WebApplicationContextUtils;
import flex.messaging.FlexContext;
import flex.messaging.log.Log;
import flex.messaging.security.AppServerLoginCommand;

/**
 * @author see: http://histos.typepad.com/blog/2008/06/integrating-blaze-data-services-and-spring-security.html
 */
public class SpringSecurityLoginCommand extends AppServerLoginCommand {

    @SuppressWarnings("unchecked")
    public Principal doAuthentication(String username, Object credentials) {
        Log.getLogger("Security").debug("doAuthentication");
        Map<String, ProviderManager> map = getContext().getBeansOfType(ProviderManager.class);
        if (map.size() != 1) {
            throw new RuntimeException("Spring ApplicationContext must contain exactly one ProviderManager bean");
        }
        ProviderManager provider = map.get(map.keySet().iterator().next());
        String password = extractPassword(credentials);
        Authentication auth = provider.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(auth);
        return auth;
    }

    private ApplicationContext getContext() {
        ServletContext servletContext = FlexContext.getServletContext();
        return WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
    }

    public boolean logout(Principal principal) {
        Log.getLogger("Security").debug("logout");
        SecurityContextHolder.getContext().setAuthentication(null);
        FlexContext.getFlexSession().invalidate();
        return true;
    }
}
