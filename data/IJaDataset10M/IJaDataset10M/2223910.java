package org.nexopenframework.security.acegi.ui.webapp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.acegisecurity.AuthenticationException;
import org.acegisecurity.ui.webapp.AuthenticationProcessingFilterEntryPoint;
import org.nexopenframework.security.acegi.ui.ModuleResolver;
import org.springframework.util.StringUtils;

/**
 * <p>NexOpen Framework</p>
 * 
 * <p>Comment here</p>
 * 
 * @author Francesc Xavier Magdaleno
 * @version 1.0
 * @since 1.0
 */
public class MultiModuleAuthenticationProcessingFilterEntryPoint extends AuthenticationProcessingFilterEntryPoint {

    /**
	 * <p>Allows custom login form urls associated to the modules 
	 * configured in your application</p>
	 * 
	 * @see ModuleResolver
	 * @see org.acegisecurity.ui.webapp.AuthenticationProcessingFilterEntryPoint#determineUrlToUseForThisRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.acegisecurity.AuthenticationException)
	 */
    protected String determineUrlToUseForThisRequest(final HttpServletRequest request, final HttpServletResponse response, final AuthenticationException exception) {
        if (ModuleResolver.needsResolve(request)) {
            String module = ModuleResolver.resolveModule(request);
            if (StringUtils.hasLength(module)) {
                module = new StringBuffer("/").append(module).toString();
            }
            final StringBuffer url = new StringBuffer(module);
            url.append(getLoginFormUrl());
            return url.toString();
        }
        return super.determineUrlToUseForThisRequest(request, response, exception);
    }
}
