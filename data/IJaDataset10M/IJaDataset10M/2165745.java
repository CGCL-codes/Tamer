package org.datanucleus.store;

import java.util.Properties;
import org.datanucleus.exceptions.NucleusException;
import org.datanucleus.metadata.ExtensionMetaData;
import org.datanucleus.metadata.SequenceMetaData;
import org.datanucleus.plugin.ConfigurationElement;
import org.datanucleus.store.connection.ManagedConnection;
import org.datanucleus.store.valuegenerator.ValueGenerationConnectionProvider;
import org.datanucleus.store.valuegenerator.ValueGenerationManager;
import org.datanucleus.store.valuegenerator.ValueGenerator;
import org.datanucleus.util.Localiser;
import org.datanucleus.util.NucleusLogger;

/**
 * Basic generic implementation of a datastore sequence.
 * Utilises the "org.datanucleus.store_valuegenerator" extensions.
 */
public class NucleusSequenceImpl implements NucleusSequence {

    /** Localisation of messages */
    protected static final Localiser LOCALISER = Localiser.getInstance("org.datanucleus.Localisation", org.datanucleus.ClassConstants.NUCLEUS_CONTEXT_LOADER);

    /** Store Manager where we obtain our sequence. */
    protected final StoreManager storeManager;

    /** Name of the sequence. */
    protected final SequenceMetaData seqMetaData;

    /** The generator for the sequence. */
    protected ValueGenerator generator;

    /** execution context. */
    protected final ExecutionContext ec;

    /**
     * Constructor.
     * @param objectMgr The Object Manager managing the sequence
     * @param storeMgr Manager of the store where we obtain the sequence
     * @param seqmd MetaData defining the sequence
     */
    public NucleusSequenceImpl(ExecutionContext objectMgr, final StoreManager storeMgr, SequenceMetaData seqmd) {
        this.ec = objectMgr;
        this.storeManager = storeMgr;
        this.seqMetaData = seqmd;
        setGenerator();
    }

    /**
     * Method to set the value generator to use.
     */
    protected void setGenerator() {
        String valueGeneratorName = "sequence";
        Properties props = new Properties();
        ExtensionMetaData[] seqExtensions = seqMetaData.getExtensions();
        if (seqExtensions != null && seqExtensions.length > 0) {
            for (int i = 0; i < seqExtensions.length; i++) {
                props.put(seqExtensions[i].getKey(), seqExtensions[i].getValue());
            }
        }
        props.put("sequence-name", seqMetaData.getDatastoreSequence());
        ValueGenerationManager mgr = storeManager.getValueGenerationManager();
        ValueGenerationConnectionProvider connProvider = new ValueGenerationConnectionProvider() {

            ManagedConnection mconn;

            public ManagedConnection retrieveConnection() {
                mconn = storeManager.getConnection(ec);
                return mconn;
            }

            public void releaseConnection() {
                this.mconn.release();
                this.mconn = null;
            }
        };
        Class cls = null;
        ConfigurationElement elem = ec.getNucleusContext().getPluginManager().getConfigurationElementForExtension("org.datanucleus.store_valuegenerator", new String[] { "name", "datastore" }, new String[] { valueGeneratorName, storeManager.getStoreManagerKey() });
        if (elem != null) {
            cls = ec.getNucleusContext().getPluginManager().loadClass(elem.getExtension().getPlugin().getSymbolicName(), elem.getAttribute("class-name"));
        }
        if (cls == null) {
            throw new NucleusException("Cannot create ValueGenerator for strategy " + valueGeneratorName);
        }
        generator = mgr.createValueGenerator(seqMetaData.getName(), cls, props, storeManager, connProvider);
        if (NucleusLogger.DATASTORE.isDebugEnabled()) {
            NucleusLogger.DATASTORE.debug(LOCALISER.msg("017003", seqMetaData.getName(), valueGeneratorName));
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
        generator.allocate(additional);
    }

    /**
     * Accessor for the next element in the sequence.
     * @return The next element
     */
    public Object next() {
        return generator.next();
    }

    /**
     * Accessor for the next element in the sequence as a long.
     * @return The next element
     */
    public long nextValue() {
        return generator.nextValue();
    }

    /**
     * Accessor for the current element.
     * @return The current element.
     */
    public Object current() {
        return generator.current();
    }

    /**
     * Accessor for the current element in the sequence as a long.
     * @return The current element
     */
    public long currentValue() {
        return generator.currentValue();
    }
}
