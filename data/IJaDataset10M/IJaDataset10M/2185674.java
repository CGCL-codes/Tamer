package org.objectwiz.remote;

import org.objectwiz.PersistenceUnit;

/**
 * Description of a {@link PersistenceUnit} that is aimed
 * to be sent over the network.
 *
 * @author Benoît Del Basso <benoit.delbasso at helmet.fr>
 */
public class PersistenceUnitDescription implements HessianSerializable {

    private String name;

    private String persistenceProviderDescription;

    private String databaseDriverDescription;

    public PersistenceUnitDescription(PersistenceUnit pu) {
        if (pu == null) return;
        this.name = pu.getName();
        this.persistenceProviderDescription = pu.getPersistenceProviderDescription();
        this.databaseDriverDescription = pu.getDatabaseDriverDescription();
    }

    public String getName() {
        return name;
    }

    public String getPersistenceProviderDescription() {
        return persistenceProviderDescription;
    }

    public String getDatabaseDriverDescription() {
        return databaseDriverDescription;
    }
}
