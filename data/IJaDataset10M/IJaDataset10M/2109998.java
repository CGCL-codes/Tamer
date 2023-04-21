package de.domainframework.common.configuration;

public class Configuration {

    private static Configuration instance = new Configuration();

    private PersistenceConfiguration persistenceConfiguration = new PersistenceConfiguration();

    private TestKeyStore testKeyStore = new TestKeyStore();

    public static Configuration getInstance() {
        return instance;
    }

    public PersistenceConfiguration getPersistanceConfiguration() {
        return persistenceConfiguration;
    }

    public TestKeyStore getTestKeyStore() {
        return testKeyStore;
    }
}
