package ee.webAppToolkit.storage.twigPersist;

import com.google.inject.AbstractModule;
import ee.webAppToolkit.storage.Store;

public class TwigPersistModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Store.class).to(TwigPersistStore.class).asEagerSingleton();
    }
}
