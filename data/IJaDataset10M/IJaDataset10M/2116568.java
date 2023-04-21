package org.gamegineer.common.persistence.schemes.beans.services.persistencedelegateregistry;

/**
 * A fixture for testing the
 * {@link org.gamegineer.common.persistence.schemes.beans.services.persistencedelegateregistry.FakePersistenceDelegateRegistry}
 * class to ensure it does not violate the contract of the
 * {@link org.gamegineer.common.persistence.schemes.beans.services.persistencedelegateregistry.IPersistenceDelegateRegistry}
 * interface.
 */
public final class FakePersistenceDelegateRegistryAsPersistenceDelegateRegistryTest extends AbstractPersistenceDelegateRegistryTestCase {

    /**
     * Initializes a new instance of the {@code
     * FakePersistenceDelegateRegistryAsPersistenceDelegateRegistryTest} class.
     */
    public FakePersistenceDelegateRegistryAsPersistenceDelegateRegistryTest() {
        super();
    }

    @Override
    protected IPersistenceDelegateRegistry createPersistenceDelegateRegistry() {
        return new FakePersistenceDelegateRegistry();
    }
}
