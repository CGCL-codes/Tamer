package com.fh.auge.core.provider;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.fh.auge.core.security.Security;

public class PricesProviderService implements IPricesProviderService {

    private List<ILatestPricesProvider> latestPricesProviders = new ArrayList<ILatestPricesProvider>();

    private List<IHistoricalPricesProvider> historicalPricesProviders = new ArrayList<IHistoricalPricesProvider>();

    private Set<IDataProvider> providers = new HashSet<IDataProvider>();

    public List<ILatestPricesProvider> getLatestPricesProviders() {
        return latestPricesProviders;
    }

    public List<IHistoricalPricesProvider> getHistoricalPricesProviders() {
        return historicalPricesProviders;
    }

    public ILatestPricesProvider findLatestPricesProvider(String id) {
        for (ILatestPricesProvider provider : latestPricesProviders) {
            if (provider.getId().equals(id)) return provider;
        }
        return null;
    }

    public IHistoricalPricesProvider findHistoricalPricesProvider(String id) {
        for (IHistoricalPricesProvider provider : historicalPricesProviders) {
            if (provider.getId().equals(id)) return provider;
        }
        return null;
    }

    public void add(PricesProviderWrapper wrapper) {
        providers.add(wrapper);
        if (wrapper.isHistoricalProvider()) {
            getHistoricalPricesProviders().add(wrapper);
        }
        if (wrapper.isLatestProvider()) {
            getLatestPricesProviders().add(wrapper);
        }
    }

    public Set<IDataProvider> findAvailableProviders(Security security) {
        Set<IDataProvider> items = new HashSet<IDataProvider>();
        for (IDataProvider dataProvider : providers) {
            if (security.getIdentifiers().containsKey(dataProvider.getRequiredIdType())) {
                items.add(dataProvider);
            }
        }
        return items;
    }

    public Set<IDataProvider> getProviders() {
        return providers;
    }
}
