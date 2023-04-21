package org.redemptionhost.web;

import org.redemptionhost.GreetingQueries;
import org.redemptionhost.HostQueries;
import org.redemptionhost.Repository;
import org.redemptionhost.jdo.PersistenceManagerFilter;
import org.redemptionhost.model.Greeting;
import org.redemptionhost.model.Host;
import org.redemptionhost.service.JdoGreetingQueries;
import org.redemptionhost.service.JdoGreetingRepository;
import org.redemptionhost.service.JdoHostQueries;
import org.redemptionhost.service.JdoHostRepository;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;

/**
 * This Guice module sets up the bindings used in this Wicket application, including the
 * JDO PersistenceManager.
 */
public class GuiceModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new PersistenceManagerFilter.GuiceModule());
        bind(GreetingQueries.class).to(JdoGreetingQueries.class);
        bind(new TypeLiteral<Repository<Greeting>>() {
        }).to(JdoGreetingRepository.class);
        bind(HostQueries.class).to(JdoHostQueries.class);
        bind(new TypeLiteral<Repository<Host>>() {
        }).to(JdoHostRepository.class);
    }
}
