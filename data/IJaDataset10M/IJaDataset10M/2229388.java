package com.fh.auge.quote.update;

import com.fh.auge.core.Market;
import com.fh.auge.core.Security;

public class PropertyProviderIdentifierResolver extends ProviderIdentifierResolver {

    private String propertyKey;

    public PropertyProviderIdentifierResolver(String propertyKey) {
        super();
        this.propertyKey = propertyKey;
    }

    public String getPropertyKey() {
        return propertyKey;
    }

    @Override
    protected String findId(Market market) {
        return market.getProperties().getProperty(propertyKey);
    }

    @Override
    protected String findId(Security security) {
        return security.getProperties().getProperty(propertyKey);
    }

    @Override
    public boolean hasIdentifier(Security security) {
        return security.getProperties().containsKey(propertyKey);
    }

    @Override
    public boolean hasIdentifier(Market market) {
        return market.getProperties().containsKey(propertyKey);
    }
}
