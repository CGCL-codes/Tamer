package jsynoptic.plugins.merge;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Logger;
import simtools.data.CollectiveDataSource;
import simtools.data.DataInfo;
import simtools.data.DataSource;
import simtools.data.DataSourceCollection;
import simtools.data.DataSourcePool;
import simtools.data.DuplicateIdException;
import simtools.data.async.TimeStampedDataSource;
import simtools.data.async.TimeStampedDataSourceCollection;
import simtools.data.buffer.BufferedDataSource;
import simtools.data.merge.MergeDataException;
import simtools.data.merge.SynchronousMergeDSCollection;
import simtools.ui.MenuResourceBundle;
import simtools.ui.ResourceFinder;

/**
 * extends MergedDataSourceCollection for saving, contextual actions requirements
 * @author zxpletran007
 *
 */
public class JSSynchronousMergeDSCollection extends SynchronousMergeDSCollection {

    static Logger _logger = simtools.util.LogConfigurator.getLogger(JSSynchronousMergeDSCollection.class.getName());

    protected static MenuResourceBundle resources = ResourceFinder.getMenu(JSMergeCollectionPlugin.class);

    public static final String ID_MARKER = "SynchronousMergeDSCollection:";

    /** List of merged data information */
    protected SynchronousMergeDSCollectionInformation information;

    public SynchronousMergeDSCollectionInformation getMergeInformation() {
        return information;
    }

    /**
	 * Create a merge collection
	 * @param collectionName
	 * @param interpolationOrder
	 * @param timeRefIsRelative
	 * @param mergedTimeReferenceDs
	 * @param mergedTimeReferenceDsIsRelative
	 * @param mergedTimeReferenceDsOffset
	 * @param mergedTimeReferenceDsInitData
	 * @throws MergeDataException
	 */
    public JSSynchronousMergeDSCollection(String collectionName, int interpolationOrder, boolean timeRefIsRelative, DataSource mergedTimeReferenceDs, boolean mergedTimeReferenceDsIsRelative, double mergedTimeReferenceDsOffset, double mergedTimeReferenceDsInitData) throws MergeDataException {
        super(collectionName, interpolationOrder, timeRefIsRelative, mergedTimeReferenceDs, mergedTimeReferenceDsIsRelative, mergedTimeReferenceDsOffset, mergedTimeReferenceDsInitData);
        information = new SynchronousMergeDSCollectionInformation(collectionName, interpolationOrder, timeRefIsRelative, mergedTimeReferenceDs, mergedTimeReferenceDsIsRelative, mergedTimeReferenceDsOffset, mergedTimeReferenceDsInitData);
        JSMergeCollectionPlugin.numberOfMergeCollection++;
    }

    /**
	 * Create a merge collection from serialized information
	 * @param mergeInformation
	 * @throws MergeDataException
	 */
    public JSSynchronousMergeDSCollection(SynchronousMergeDSCollectionInformation information) throws MergeDataException {
        this(information.collectionName, information.interpolationOrder, information.collectionTimeReferenceIsRelative, information.mergedTimeReferenceDs, information.mergedTimeReferenceDsIsRelative, information.mergedTimeReferenceDsOffset, information.mergedTimeReferenceDsInitData);
        for (int i = 0; i < information.mergedData.size(); i++) {
            try {
                SynchronousMergeDataSourceInformation dataInfo = (SynchronousMergeDataSourceInformation) information.mergedData.get(i);
                DataSource ds = dataInfo.data;
                if (!dataInfo.mergeAllRelatedCollection) {
                    if (ds instanceof TimeStampedDataSource) add((TimeStampedDataSource) ds, dataInfo.offset, dataInfo.initialDate); else {
                        add(ds, dataInfo.timeReference, dataInfo.isRelative, dataInfo.offset, dataInfo.initialDate);
                    }
                } else {
                    DataSourceCollection dsc = SynchronousMergeDataSourceInformation.creationCollectionFromDataSource(ds);
                    if (dsc != null) {
                        if (dsc instanceof TimeStampedDataSourceCollection) {
                            add((TimeStampedDataSourceCollection) dsc, dataInfo.offset, dataInfo.initialDate);
                        } else {
                            add(dsc, dataInfo.timeReference, dataInfo.isRelative, dataInfo.offset, dataInfo.initialDate);
                        }
                    }
                }
            } catch (MergeDataException e) {
            }
        }
        try {
            mergeData();
        } catch (MergeDataException e) {
        }
    }

    public void add(TimeStampedDataSourceCollection collection, double offset, double initialDate) throws MergeDataException {
        super.add(collection, offset, initialDate);
        information.mergedData.add(new SynchronousMergeDataSourceInformation(collection, offset));
    }

    public void add(TimeStampedDataSource data, double offset, double initialDate) throws MergeDataException {
        super.add(data, offset, initialDate);
        information.mergedData.add(new SynchronousMergeDataSourceInformation(data, offset));
    }

    public void add(DataSourceCollection collection, DataSource timeRef, boolean isRelative, double offset, double initialDate) throws MergeDataException {
        super.add(collection, timeRef, isRelative, offset, initialDate);
        information.mergedData.add(new SynchronousMergeDataSourceInformation(collection, timeRef, isRelative, offset, initialDate));
    }

    public void add(DataSource data, DataSource timeRef, boolean isRelative, double offset, double initialDate) throws MergeDataException {
        super.add(data, timeRef, isRelative, offset, initialDate);
        information.mergedData.add(new SynchronousMergeDataSourceInformation(data, timeRef, isRelative, offset, initialDate));
    }

    /**
	 * A nested class dedicated to serialize all information about a merge collection
	 * @author zxpletran007
	 *
	 */
    public static class SynchronousMergeDSCollectionInformation implements Serializable {

        private static final long serialVersionUID = -6094396172567695399L;

        public String collectionName;

        public ArrayList mergedData;

        public int interpolationOrder;

        public boolean collectionTimeReferenceIsRelative;

        public transient DataSource mergedTimeReferenceDs;

        public boolean mergedTimeReferenceDsIsRelative;

        public double mergedTimeReferenceDsOffset;

        public double mergedTimeReferenceDsInitData;

        public SynchronousMergeDSCollectionInformation(String collectionName, int interpolationOrder, boolean collectionTimeReferenceIsRelative, DataSource mergedTimeReferenceDs, boolean mergedTimeReferenceDsIsRelative, double mergedTimeReferenceDsOffset, double mergedTimeReferenceDsInitData) {
            this.collectionName = collectionName;
            this.mergedData = new ArrayList();
            this.interpolationOrder = interpolationOrder;
            this.collectionTimeReferenceIsRelative = collectionTimeReferenceIsRelative;
            this.mergedTimeReferenceDs = mergedTimeReferenceDs;
            this.mergedTimeReferenceDsIsRelative = mergedTimeReferenceDsIsRelative;
            this.mergedTimeReferenceDsOffset = mergedTimeReferenceDsOffset;
            this.mergedTimeReferenceDsInitData = mergedTimeReferenceDsInitData;
        }

        private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
            out.defaultWriteObject();
            DataSourcePool.global.writeDataSource(out, mergedTimeReferenceDs);
        }

        private void readObject(java.io.ObjectInputStream in) throws java.lang.ClassNotFoundException, java.io.IOException {
            in.defaultReadObject();
            mergedTimeReferenceDs = (DataSource) DataSourcePool.global.readDataSource(in);
        }
    }

    /**
	 * A nested class dedicated to serialize information about a merged data.
	 * @author zxpletran007
	 *
	 */
    public static class SynchronousMergeDataSourceInformation implements Serializable {

        private static final long serialVersionUID = 3993909610809844941L;

        /**
		 * A data source to merge
		 */
        public transient DataSource data;

        /**
		 * If true, all collection that hold data source has to be merged
		 */
        public boolean mergeAllRelatedCollection;

        /**
		 * When data is synchronous: data sous related to time reference
		 */
        public transient DataSource timeReference;

        /**
		 * Time reference is expressed in relative format (number of seconds)
		 */
        public boolean isRelative;

        /**
		 * Offset applied to time reference values
		 */
        public double offset;

        /**
		 * An optional initial date used in case a relative time to absolute time conversion has to be performed
		 */
        public double initialDate;

        public SynchronousMergeDataSourceInformation(TimeStampedDataSourceCollection collection, double offset) {
            data = (DataSource) collection.get(0);
            mergeAllRelatedCollection = true;
            timeReference = null;
            this.offset = offset;
            isRelative = false;
        }

        public SynchronousMergeDataSourceInformation(TimeStampedDataSource dataSource, double offset) {
            data = dataSource;
            mergeAllRelatedCollection = false;
            timeReference = null;
            this.offset = offset;
            isRelative = false;
        }

        public SynchronousMergeDataSourceInformation(DataSourceCollection collection, DataSource refTime, boolean isRelative, double offset, double initialDate) {
            data = (DataSource) collection.get(0);
            mergeAllRelatedCollection = true;
            timeReference = refTime;
            this.offset = offset;
            this.isRelative = isRelative;
            this.initialDate = initialDate;
        }

        public SynchronousMergeDataSourceInformation(DataSource dataSource, DataSource refTime, boolean isRelative, double offset, double initialDate) {
            data = dataSource;
            mergeAllRelatedCollection = false;
            timeReference = refTime;
            this.offset = offset;
            this.isRelative = isRelative;
            this.initialDate = initialDate;
        }

        private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
            out.defaultWriteObject();
            DataSourcePool.global.writeDataSource(out, data);
            DataSourcePool.global.writeDataSource(out, timeReference);
        }

        private void readObject(java.io.ObjectInputStream in) throws java.lang.ClassNotFoundException, java.io.IOException {
            in.defaultReadObject();
            data = (DataSource) DataSourcePool.global.readDataSource(in);
            timeReference = (DataSource) DataSourcePool.global.readDataSource(in);
        }

        public static DataSourceCollection creationCollectionFromDataSource(DataSource ds) {
            if (ds instanceof TimeStampedDataSource) return ((TimeStampedDataSource) ds).getCollection();
            if (ds instanceof CollectiveDataSource) return ((CollectiveDataSource) ds).getCollection();
            if (ds instanceof BufferedDataSource) return creationCollectionFromDataSource(((BufferedDataSource) ds).dataSource);
            try {
                return DataSourcePool.global.getCollectionForDataSourceId(DataInfo.getId(ds));
            } catch (DuplicateIdException e) {
                _logger.fine("Cannot find collection related to " + DataInfo.getId(ds));
            }
            return null;
        }
    }
}
