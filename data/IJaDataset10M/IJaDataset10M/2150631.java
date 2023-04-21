package org.datanucleus.store.rdbms;

import java.sql.SQLException;
import java.util.Properties;
import javax.jdo.JDODataStoreException;
import javax.jdo.datastore.Sequence;
import org.datanucleus.ManagedConnection;
import org.datanucleus.ObjectManager;
import org.datanucleus.PersistenceConfiguration;
import org.datanucleus.exceptions.NucleusDataStoreException;
import org.datanucleus.exceptions.NucleusException;
import org.datanucleus.metadata.ExtensionMetaData;
import org.datanucleus.metadata.SequenceMetaData;
import org.datanucleus.plugin.ConfigurationElement;
import org.datanucleus.store.NucleusSequence;
import org.datanucleus.store.mapped.DatastoreAdapter;
import org.datanucleus.store.mapped.MappedStoreManager;
import org.datanucleus.store.valuegenerator.ValueGenerationConnectionProvider;
import org.datanucleus.store.valuegenerator.ValueGenerator;
import org.datanucleus.store.valuegenerator.ValueGenerationManager;
import org.datanucleus.transaction.TransactionUtils;
import org.datanucleus.util.NucleusLogger;
import org.datanucleus.util.Localiser;

/**
 * Basic implementation of a JDO 2 datastore sequence for RDBMS datastores.
 * Utilises the <B>org.datanucleus.store.poid</B> classes to generate
 * sequence values.
 * @version $Revision: 1.10 $
 */
public class JDOSequenceImpl implements Sequence, NucleusSequence {

    /** Localisation of messages */
    protected static final Localiser LOCALISER = Localiser.getInstance("org.datanucleus.Localisation", ObjectManager.class.getClassLoader());

    /** Store Manager where we obtain our sequence. */
    protected final MappedStoreManager storeManager;

    /** Name of the sequence. */
    protected final SequenceMetaData seqMetaData;

    /** The generator for the sequence. */
    protected final ValueGenerator generator;

    /** The controlling Object Manager. */
    protected final ObjectManager om;

    /**
     * Constructor.
     * @param objectMgr The Object Manager managing the sequence
     * @param storeMgr Manager of the store where we obtain the sequence
     * @param seqmd MetaData defining the sequence
     */
    public JDOSequenceImpl(ObjectManager objectMgr, MappedStoreManager storeMgr, SequenceMetaData seqmd) {
        this.om = objectMgr;
        this.storeManager = storeMgr;
        this.seqMetaData = seqmd;
        String poidGeneratorName = null;
        if (storeMgr.getDatastoreAdapter().supportsOption(DatastoreAdapter.SEQUENCES)) {
            poidGeneratorName = "sequence";
        } else {
            poidGeneratorName = "table-sequence";
        }
        Properties props = new Properties();
        ExtensionMetaData[] seqExtensions = seqmd.getExtensions();
        if (seqExtensions != null && seqExtensions.length > 0) {
            for (int i = 0; i < seqExtensions.length; i++) {
                props.put(seqExtensions[i].getKey(), seqExtensions[i].getValue());
            }
        }
        if (poidGeneratorName.equals("sequence")) {
            props.put("sequence-name", seqMetaData.getDatastoreSequence());
        } else if (poidGeneratorName.equals("table-sequence")) {
            props.put("sequence-name", seqMetaData.getDatastoreSequence());
        }
        ValueGenerationManager mgr = storeMgr.getPoidManager();
        ValueGenerationConnectionProvider connProvider = new ValueGenerationConnectionProvider() {

            ManagedConnection mconn;

            public ManagedConnection retrieveConnection() {
                try {
                    PersistenceConfiguration conf = om.getOMFContext().getPersistenceConfiguration();
                    int isolationLevel = TransactionUtils.getTransactionIsolationLevelForName(conf.getStringProperty("datanucleus.valuegeneration.transactionIsolation"));
                    this.mconn = ((RDBMSManager) storeManager).getConnection(isolationLevel);
                } catch (SQLException e) {
                    String msg = LOCALISER.msg("017006", e);
                    NucleusLogger.JDO.error(msg);
                    throw new JDODataStoreException(msg, e);
                }
                return mconn;
            }

            public void releaseConnection() {
                try {
                    mconn.close();
                } catch (NucleusException e) {
                    String msg = LOCALISER.msg("017007", e);
                    NucleusLogger.JDO.error(msg);
                    throw new JDODataStoreException(msg, e);
                }
            }
        };
        Class cls = null;
        ConfigurationElement elem = objectMgr.getOMFContext().getPluginManager().getConfigurationElementForExtension("org.datanucleus.store_valuegenerator", new String[] { "name", "datastore" }, new String[] { poidGeneratorName, storeManager.getStoreManagerKey() });
        if (elem != null) {
            cls = objectMgr.getOMFContext().getPluginManager().loadClass(elem.getExtension().getPlugin().getSymbolicName(), elem.getAttribute("class-name"));
        }
        if (cls == null) {
            throw new NucleusException("Cannot create Poid Generator for strategy " + poidGeneratorName);
        }
        generator = mgr.createValueGenerator(seqMetaData.getName(), cls, props, storeManager, connProvider);
        if (NucleusLogger.JDO.isDebugEnabled()) {
            NucleusLogger.JDO.debug(LOCALISER.msg("017003", seqMetaData.getName(), poidGeneratorName));
        }
    }

    /**
     * Accessor for the sequence name.
     * @return The sequence name
     */
    public String getName() {
        return seqMetaData.getName();
    }

    /**
     * Method to allocate a set of elements.
     * @param additional The number of additional elements to allocate
     */
    public void allocate(int additional) {
        try {
            generator.allocate(additional);
        } catch (NucleusException de) {
        }
    }

    /**
     * Accessor for the next element in the sequence.
     * @return The next element
     */
    public Object next() {
        try {
            return generator.next();
        } catch (NucleusDataStoreException dse) {
            if (dse.getFailedObject() != null) {
                throw new JDODataStoreException(dse.getMessage(), dse.getFailedObject());
            } else {
                throw new JDODataStoreException(dse.getMessage(), dse.getNestedExceptions());
            }
        }
    }

    /**
     * Accessor for the next element in the sequence as a long.
     * @return The next element
     * @throws JDODataStoreException Thrown if not numeric
     */
    public long nextValue() {
        try {
            return generator.nextValue();
        } catch (NucleusDataStoreException dse) {
            if (dse.getFailedObject() != null) {
                throw new JDODataStoreException(dse.getMessage(), dse.getFailedObject());
            } else {
                throw new JDODataStoreException(dse.getMessage(), dse.getNestedExceptions());
            }
        }
    }

    /**
     * Accessor for the current element.
     * @return The current element.
     */
    public Object current() {
        try {
            return generator.current();
        } catch (NucleusDataStoreException dse) {
            if (dse.getFailedObject() != null) {
                throw new JDODataStoreException(dse.getMessage(), dse.getFailedObject());
            } else {
                throw new JDODataStoreException(dse.getMessage(), dse.getNestedExceptions());
            }
        }
    }

    /**
     * Accessor for the current element in the sequence as a long.
     * @return The current element
     * @throws JDODataStoreException Thrown if not numeric
     */
    public long currentValue() {
        try {
            return generator.currentValue();
        } catch (NucleusDataStoreException dse) {
            if (dse.getFailedObject() != null) {
                throw new JDODataStoreException(dse.getMessage(), dse.getFailedObject());
            } else {
                throw new JDODataStoreException(dse.getMessage(), dse.getNestedExceptions());
            }
        }
    }
}
