package org.databene.commons.depend;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class that provides partial featur implementation of the Dependent interface.
 * @author Volker Bergmann
 * @since 0.3.04
 * @param <E>
 */
public abstract class AbstractDependent<E extends Dependent<E>> implements Dependent<E> {

    protected List<ProviderInfo<E>> providers;

    public AbstractDependent(E... requiredProviders) {
        this.providers = new ArrayList<ProviderInfo<E>>();
        for (E requiredProvider : requiredProviders) addRequiredProvider(requiredProvider);
    }

    public void addRequiredProvider(E provider) {
        providers.add(new ProviderInfo<E>(provider, true));
    }

    public void addOptionalProvider(E provider) {
        providers.add(new ProviderInfo<E>(provider, false));
    }

    public int countProviders() {
        return providers.size();
    }

    public E getProvider(int index) {
        return providers.get(index).getProvider();
    }

    public boolean requiresProvider(int index) {
        return providers.get(index).isRequired();
    }
}
