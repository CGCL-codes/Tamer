package org.jtools.server.config;

import org.jtools.server.Listener;
import org.jtools.server.Service;
import org.jtools.server.auth.Authenticator;

public interface ServiceConfiguration<S extends Service<S>, L extends Listener, A> {

    S getService();

    L getListener();

    Authenticator<A> getAuthenticator();
}
