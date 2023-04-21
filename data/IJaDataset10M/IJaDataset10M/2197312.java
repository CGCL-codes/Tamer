package org.datanucleus.store.hbase;

import org.datanucleus.exceptions.NucleusUserException;
import org.datanucleus.metadata.AbstractClassMetaData;
import org.datanucleus.metadata.AbstractMemberMetaData;
import org.datanucleus.metadata.IdentityStrategy;
import org.datanucleus.metadata.IdentityType;
import org.datanucleus.metadata.MetaDataListener;
import org.datanucleus.util.Localiser;

/**
 * Listener for the load of metadata for classes.
 * Allows us to reject metadata when it isn't supported by this datastore.
 */
public class HBaseMetaDataListener implements MetaDataListener {

    /** Localiser for messages. */
    protected static final Localiser LOCALISER = Localiser.getInstance("org.datanucleus.store.hbase.Localisation", HBaseStoreManager.class.getClassLoader());

    private HBaseStoreManager storeManager;

    HBaseMetaDataListener(HBaseStoreManager storeManager) {
        this.storeManager = storeManager;
    }

    public void loaded(AbstractClassMetaData cmd) {
        if (cmd.getIdentityType() == IdentityType.DATASTORE) {
            if (cmd.getIdentityMetaData() != null && cmd.getIdentityMetaData().getValueStrategy() == IdentityStrategy.IDENTITY) {
                throw new NucleusUserException("Class " + cmd.getFullClassName() + " has been specified to use datastore-identity with IDENTITY value generation, but not supported on HBase");
            }
        } else if (cmd.getIdentityType() == IdentityType.APPLICATION) {
            int[] pkFieldNumbers = cmd.getPKMemberPositions();
            for (int i = 0; i < pkFieldNumbers.length; i++) {
                AbstractMemberMetaData mmd = cmd.getMetaDataForManagedMemberAtAbsolutePosition(pkFieldNumbers[i]);
                if (mmd.getValueStrategy() == IdentityStrategy.IDENTITY) {
                    throw new NucleusUserException("Field " + mmd.getFullFieldName() + " has been specified to use IDENTITY value generation, but not supported on HBase");
                }
            }
        }
        if (storeManager.isAutoCreateTables() || storeManager.isAutoCreateColumns()) {
            HBaseUtils.createSchemaForClass(storeManager, cmd, false);
        }
    }
}
