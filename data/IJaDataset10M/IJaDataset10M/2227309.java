package org.tamacat.httpd.auth;

import org.apache.http.protocol.HttpContext;

/**
 * The interface of Authentication component.
 */
public interface AuthComponent<T extends AuthUser> {

    /**
	 * Remote user key used for HttpContext.
	 */
    String REMOTE_USER_KEY = AuthComponent.class.getClass().getName() + ".REMOTE_USER";

    /**
	 * Initialized this instance.
	 * Execute from {@link HttpHandler#setRequestFilter}.
	 */
    void init();

    /**
	 * Release all object used by this instance.
	 */
    void release();

    /**
	 * The login account is verified. 
	 * @param id
	 * @param pass
	 * @param context
	 * @throws {@link org.tamacat.server.http.UnauthorizedException}
	 */
    boolean check(String id, String pass, HttpContext context);

    /**
	 * Get the {@link AuthUser}.
	 * @param id
	 * @param context
	 * @return Implements of {@code AuthUser}
	 */
    T getAuthUser(String id, HttpContext context);
}
