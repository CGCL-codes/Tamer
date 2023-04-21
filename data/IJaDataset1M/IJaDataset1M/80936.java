package org.unitils.orm.jpa.util.provider.toplink;

import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
import org.springframework.instrument.classloading.LoadTimeWeaver;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.vendor.TopLinkJpaVendorAdapter;
import org.unitils.orm.jpa.util.JpaProviderSupport;
import javax.persistence.EntityManager;
import javax.persistence.spi.PersistenceProvider;

/**
 * Implementation of {@link JpaProviderSupport} for Oracle Toplink JPA
 *
 * @author Filip Neven
 * @author Tim Ducheyne
 */
public class ToplinkJpaProviderSupport implements JpaProviderSupport {

    public void assertMappingWithDatabaseConsistent(EntityManager entityManager, Object configurationObject) {
        throw new UnsupportedOperationException("The method assertMappingWithDatabaseConsistent is not implemented for toplink");
    }

    public Object getProviderSpecificConfigurationObject(PersistenceProvider persistenceProvider) {
        return null;
    }

    public JpaVendorAdapter getSpringJpaVendorAdaptor() {
        return new TopLinkJpaVendorAdapter();
    }

    public LoadTimeWeaver getLoadTimeWeaver() {
        return new InstrumentationLoadTimeWeaver();
    }
}
