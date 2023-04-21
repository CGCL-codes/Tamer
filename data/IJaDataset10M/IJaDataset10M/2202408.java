package org.oauth4j.core;

import java.util.List;

public interface OAuth4JServiceProvider {

    public OAuth4JServiceProviderService getRequestTokenEndpoint();

    public OAuth4JServiceProviderService getAuthorizeTokenEndpoint();

    public OAuth4JServiceProviderService getAccessTokenEndpoint();

    public <S extends OAuth4JServiceProviderService> List<S> getServices();
}
