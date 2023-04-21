package net.sf.doolin.cs.auth.annotation;

import java.lang.reflect.Method;
import net.sf.doolin.cs.auth.AuthenticationLevel;
import net.sf.doolin.cs.auth.AuthenticationMode;
import net.sf.doolin.cs.auth.AuthenticationPolicy;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.remoting.support.RemoteInvocation;

/**
 * Authentication policy based on annotations.
 * 
 * @author Damien Coraboeuf
 * 
 * @see Forbidden
 * @see Authenticated
 */
public class AnnotationAuthenticationPolicy implements AuthenticationPolicy {

    private final AuthenticationLevel defaultLevel;

    /**
	 * Constructor with a default authorisation level set to
	 * {@link AuthenticationLevel#FORBIDDEN}.
	 */
    public AnnotationAuthenticationPolicy() {
        this(AuthenticationLevel.FORBIDDEN);
    }

    /**
	 * Constructor with a given default authentication level.
	 * 
	 * @param defaultLevel
	 *            Default level of authentication.
	 */
    public AnnotationAuthenticationPolicy(AuthenticationLevel defaultLevel) {
        this.defaultLevel = defaultLevel;
    }

    @Override
    public AuthenticationMode getAuthenticationMode(Object target, RemoteInvocation invocation) {
        try {
            Method method = target.getClass().getMethod(invocation.getMethodName(), invocation.getParameterTypes());
            Forbidden forbidden = AnnotationUtils.findAnnotation(method, Forbidden.class);
            if (forbidden != null) {
                return AuthenticationMode.FORBIDDEN;
            }
            Authenticated authenticated = AnnotationUtils.findAnnotation(method, Authenticated.class);
            if (authenticated != null) {
                return new AuthenticationMode(AuthenticationLevel.AUTHORISATION, authenticated.value());
            }
            return new AuthenticationMode(this.defaultLevel, null);
        } catch (NoSuchMethodException ex) {
            throw new RuntimeException("No such method exception", ex);
        }
    }
}
