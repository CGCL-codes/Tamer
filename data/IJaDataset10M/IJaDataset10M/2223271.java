package org.nakedobjects.system;

import org.nakedobjects.object.repository.NakedObjectsClient;
import org.nakedobjects.system.install.core.DefaultConfigurationLoader;
import org.nakedobjects.system.install.core.DefaultObjectLoader;
import org.nakedobjects.system.install.core.DefaultSpecificationLoader;
import org.nakedobjects.system.install.fixture.FixturesFromProperties;
import org.nakedobjects.system.install.persistence.SqlPersistor;

/**
 * Setups the Sql object store with the objects created by the fixtures specified in the properties.
 */
public final class SetupSqlStore {

    public static void main(final String[] args) {
        NakedObjectsSystem e = new NakedObjectsSystem();
        DefaultConfigurationLoader configurationLoader = new DefaultConfigurationLoader();
        configurationLoader.addConfigurationFile("sqlos.properties", false);
        e.setConfigurationLoader(configurationLoader);
        NakedObjectsClient nakedObjects = new NakedObjectsClient();
        e.setRepository(nakedObjects);
        e.setSpecificationLoader(new DefaultSpecificationLoader());
        e.setObjectLoader(new DefaultObjectLoader());
        e.setObjectPersistor(new SqlPersistor());
        e.setFixture(new FixturesFromProperties());
        e.init();
        nakedObjects.shutdown();
    }
}
