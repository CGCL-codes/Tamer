package app.services;

import com.google.inject.AbstractModule;

/**
 * @author Igor Polevoy
 */
public class GreeterModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Greeter.class).to(GreeterImpl.class).asEagerSingleton();
    }
}
