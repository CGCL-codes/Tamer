package com.kni.etl.ketl.writer;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import com.kni.etl.EngineConstants;
import com.kni.etl.dbutils.ResourcePool;
import com.kni.etl.ketl.ETLInPort;
import com.kni.etl.ketl.ETLStep;
import com.kni.etl.ketl.KETLJob;
import com.kni.etl.ketl.exceptions.KETLError;
import com.kni.etl.ketl.exceptions.KETLThreadException;
import com.kni.etl.ketl.exceptions.KETLWriteException;
import com.kni.etl.ketl.lookup.LookupCreatorImpl;
import com.kni.etl.ketl.lookup.PersistentMap;
import com.kni.etl.ketl.smp.DefaultWriterCore;
import com.kni.etl.ketl.smp.ETLThreadManager;
import com.kni.etl.stringtools.NumberFormatter;
import com.kni.etl.util.XMLHelper;

/**
 * <p>
 * Title: ETLWriter
 * </p>
 * <p>
 * Description: Abstract base class for ETL destination loading.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Kinetic Networks
 * </p>
 * 
 * @author Brian Sullivan
 * @version 0.1
 */
public class LookupWriter extends ETLWriter implements DefaultWriterCore, LookupCreatorImpl {

    @Override
    protected String getVersion() {
        return "$LastChangedRevision: 491 $";
    }

    /** The Constant KEY_ATTRIB. */
    private static final String KEY_ATTRIB = "KEY";

    /** The Constant VALUE_ATTRIB. */
    private static final String VALUE_ATTRIB = "VALUE";

    /** The values. */
    public int mKeys = 0, mValues = 0;

    /**
	 * The Class LookupWriterInPort.
	 */
    public class LookupWriterInPort extends ETLInPort {

        /** The key. */
        private int mKey;

        /** The value. */
        private int mValue;

        @Override
        public int initialize(Node xmlNode) throws ClassNotFoundException, KETLThreadException {
            super.initialize(xmlNode);
            NamedNodeMap attr = xmlNode.getAttributes();
            this.mKey = XMLHelper.getAttributeAsInt(attr, LookupWriter.KEY_ATTRIB, -1);
            this.mValue = XMLHelper.getAttributeAsInt(attr, LookupWriter.VALUE_ATTRIB, -1);
            if (this.mKey != -1) {
                if (this.mKey < 1) throw new KETLThreadException("Port " + this.mesStep.getName() + "." + this.getPortName() + " KEY order starts at 1, invalid value of " + this.mKey, this);
                LookupWriter.this.mKeys++;
                this.mKey--;
            }
            if (this.mValue != -1) {
                if (this.mValue < 1) throw new KETLThreadException("Port " + this.mesStep.getName() + "." + this.getPortName() + " VALUE order starts at 1, invalid value of " + this.mValue, this);
                LookupWriter.this.mValues++;
                this.mValue--;
            }
            return 0;
        }

        /**
		 * Instantiates a new lookup writer in port.
		 * 
		 * @param esOwningStep
		 *            the es owning step
		 * @param esSrcStep
		 *            the es src step
		 */
        public LookupWriterInPort(ETLStep esOwningStep, ETLStep esSrcStep) {
            super(esOwningStep, esSrcStep);
        }
    }

    @Override
    protected ETLInPort getNewInPort(ETLStep srcStep) {
        return new LookupWriterInPort(this, srcStep);
    }

    /**
	 * Instantiates a new lookup writer.
	 * 
	 * @param pXMLConfig
	 *            the XML config
	 * @param pPartitionID
	 *            the partition ID
	 * @param pPartition
	 *            the partition
	 * @param pThreadManager
	 *            the thread manager
	 * 
	 * @throws KETLThreadException
	 *             the KETL thread exception
	 */
    public LookupWriter(Node pXMLConfig, int pPartitionID, int pPartition, ETLThreadManager pThreadManager) throws KETLThreadException {
        super(pXMLConfig, pPartitionID, pPartition, pThreadManager);
    }

    /** The cache persistence ID. */
    private Integer mCachePersistenceID = -1;

    /** The cache size. */
    private int mCacheSize;

    /** The cache persistence. */
    private int cachePersistence;

    @Override
    protected int initialize(Node xmlConfig) throws KETLThreadException {
        int res = super.initialize(xmlConfig);
        if (res != 0) return res;
        int minSize = NumberFormatter.convertToBytes(EngineConstants.getDefaultCacheSize());
        this.cachePersistence = EngineConstants.JOB_PERSISTENCE;
        String tmp = XMLHelper.getAttributeAsString(xmlConfig.getAttributes(), "PERSISTENCE", null);
        if (tmp == null || tmp.equalsIgnoreCase("JOB")) {
            this.mCachePersistenceID = ((Long) this.getJobExecutionID()).intValue();
            this.cachePersistence = EngineConstants.JOB_PERSISTENCE;
        } else if (tmp.equalsIgnoreCase("LOAD")) {
            this.mCachePersistenceID = this.mkjExecutor.getCurrentETLJob().getLoadID();
            this.cachePersistence = EngineConstants.LOAD_PERSISTENCE;
        } else if (tmp.equalsIgnoreCase("STATIC")) {
            this.cachePersistence = EngineConstants.STATIC_PERSISTENCE;
            this.mCachePersistenceID = null;
        } else throw new KETLThreadException("PERSISTENCE has to be either JOB,LOAD or STATIC", this);
        this.mCacheSize = NumberFormatter.convertToBytes(XMLHelper.getAttributeAsString(xmlConfig.getAttributes(), "CACHESIZE", null));
        if (this.mCacheSize == -1) this.mCacheSize = minSize;
        if (this.mCacheSize < minSize) {
            ResourcePool.LogMessage(this, ResourcePool.INFO_MESSAGE, "Cache cannot be less than 64kb, defaulting to 64kb");
            this.mCacheSize = minSize;
        }
        if (this.mKeys == 0) {
            throw new KETLThreadException("No keys have been specified", this);
        }
        if (this.mValues == 0) {
            throw new KETLThreadException("No return values have been specified", this);
        }
        if (this.mKeys > 4) {
            ResourcePool.LogMessage(this, ResourcePool.INFO_MESSAGE, "Currently lookups are limited to no more than 4 keys, unless you use a an array object to represent the compound key");
        }
        this.mLookup = ((KETLJob) this.getJobExecutor().getCurrentETLJob()).registerLookupWriteLock(this.getName(), this, this.cachePersistence);
        this.lookupLocked = true;
        return 0;
    }

    public PersistentMap getLookup() {
        Class[] types = new Class[this.mKeys];
        Class[] values = new Class[this.mValues];
        String[] valueFields = new String[this.mValues];
        for (ETLInPort element : this.mInPorts) {
            LookupWriterInPort port = (LookupWriterInPort) element;
            if (port.mKey != -1) types[port.mKey] = port.getPortClass();
            if (port.mValue != -1) {
                values[port.mValue] = port.getPortClass();
                valueFields[port.mValue] = port.mstrName;
            }
        }
        String lookupClass = XMLHelper.getAttributeAsString(this.getXMLConfig().getAttributes(), "LOOKUPCLASS", EngineConstants.getDefaultLookupClass());
        try {
            return EngineConstants.getInstanceOfPersistantMap(lookupClass, this.getName(), this.mCacheSize, this.mCachePersistenceID, EngineConstants.CACHE_PATH, types, values, valueFields, this.cachePersistence == EngineConstants.JOB_PERSISTENCE ? true : false);
        } catch (Throwable e) {
            ResourcePool.LogMessage(Thread.currentThread(), ResourcePool.WARNING_MESSAGE, "Lookup cache creation failed, trying again, check stack trace");
            e.printStackTrace();
            try {
                return EngineConstants.getInstanceOfPersistantMap(lookupClass, this.getName(), this.mCacheSize, this.mCachePersistenceID, EngineConstants.CACHE_PATH, types, values, valueFields, this.cachePersistence == EngineConstants.JOB_PERSISTENCE ? true : false);
            } catch (Throwable e1) {
                e1.printStackTrace();
                throw new KETLError("LOOKUPCLASS " + lookupClass + " could not be found: " + e.getMessage(), e);
            }
        }
    }

    /** The lookup. */
    private PersistentMap mLookup;

    public int putNextRecord(Object[] o, Class[] pExpectedDataTypes, int pRecordWidth) throws KETLWriteException {
        try {
            this.putKeyArrayDataArray(o);
        } catch (Error e) {
            throw new KETLWriteException(e);
        }
        return 1;
    }

    /**
	 * Put key array data array.
	 * 
	 * @param o
	 *            the o
	 */
    private void putKeyArrayDataArray(Object[] o) {
        Object[] elements = new Object[this.mKeys];
        Object[] values = new Object[this.mValues];
        for (ETLInPort element : this.mInPorts) {
            LookupWriterInPort port = (LookupWriterInPort) element;
            if (port.mKey != -1) {
                elements[port.mKey] = o[port.getSourcePortIndex()];
            } else if (port.mValue != -1) {
                values[port.mValue] = o[port.getSourcePortIndex()];
            }
        }
        this.mLookup.put(elements, values);
    }

    /** The lookup locked. */
    boolean lookupLocked = false;

    @Override
    public int complete() throws KETLThreadException {
        int res = super.complete();
        if (res != 0) return res;
        if (this.lookupLocked) {
            this.mLookup.commit(true);
            ((KETLJob) this.getJobExecutor().getCurrentETLJob()).releaseLookupWriteLock(this.getName(), this);
        }
        return 0;
    }

    public PersistentMap swichToReadOnlyMode() {
        this.mLookup.switchToReadOnlyMode();
        return this.mLookup;
    }

    @Override
    protected void close(boolean success, boolean jobFailed) {
        if (this.lookupLocked) ((KETLJob) this.getJobExecutor().getCurrentETLJob()).releaseLookupWriteLock(this.getName(), this);
        if (this.cachePersistence == EngineConstants.JOB_PERSISTENCE) {
            ((KETLJob) this.getJobExecutor().getCurrentETLJob()).deleteLookup(this.getName());
        }
    }
}
