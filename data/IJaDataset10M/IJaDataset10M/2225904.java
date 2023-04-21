package net.sf.jguard.core.filters;

import net.sf.jguard.core.lifecycle.Request;
import net.sf.jguard.core.lifecycle.Response;

/**
 * Filter inspired by javax.servlet.Filter.
 *
 * @author <a href="mailto:diabolo512@users.sourceforge.net">Charles Gay</a>
 * @see net.sf.jguard.core.authentication.filters.AuthenticationFilter
 * @see net.sf.jguard.core.authorization.filters.AuthorizationFilter
 * @since 2.0
 */
public interface Filter<Req, Res> {

    void doFilter(Request<Req> request, Response<Res> response, FilterChain<Req, Res> chain);
}
