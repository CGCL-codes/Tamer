package architecture.ee.spring.security.authentication;

import org.springframework.security.core.context.SecurityContext;
import architecture.ee.security.authentication.AuthenticationProvider;

public interface ExtendedAuthenticationProvider extends AuthenticationProvider {

    public abstract ExtendedAuthentication getAuthentication();

    public abstract SecurityContext getSecurityContext();
}
