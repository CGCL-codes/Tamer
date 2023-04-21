package de.suse.swamp.core.container;

import java.lang.reflect.*;
import java.util.*;
import de.suse.swamp.core.data.*;
import de.suse.swamp.util.*;

/**
 * Manages access to datasets and databits, 
 * uses the DatasetCache where possible. 
 */
public class DataManager {

    private static HashMap databitContructors = new HashMap();

    private DataManager() {
    }

    /**
     * create a Databit of the given type.
     */
    public static Databit createDatabit(String name, String desc, String type, String value, int state) {
        Databit databitInstance = null;
        try {
            Constructor constructor = null;
            if (databitContructors.containsKey(type)) {
                constructor = (Constructor) databitContructors.get(type);
            } else {
                Class databitClass = Class.forName("de.suse.swamp.core.data.datatypes." + type + "Databit");
                Class[] parameterType = new Class[] { String.class, String.class, String.class, Integer.class };
                constructor = databitClass.getConstructor(parameterType);
                databitContructors.put(type, constructor);
            }
            Object[] intArgs = new Object[] { name, desc, value, new Integer(state) };
            databitInstance = (Databit) constructor.newInstance(intArgs);
        } catch (ClassCastException e) {
            Logger.ERROR("Class " + type + "Databit must extend Databit.");
        } catch (Exception e) {
            Logger.ERROR("Cannot create databit " + name + " of type: " + type + ". Error: " + e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
        }
        return databitInstance;
    }

    public static void addDatasetToWorkflow(int dSetId, int wfId) {
        StorageManager.addDatasetToWorkflow(dSetId, wfId);
    }

    public static void storeDataBit(Databit databit, String uname) {
        StorageManager.storeDataBit(databit);
    }

    public static List getDatabitsForSet(int id) {
        List bits = StorageManager.loadAllDatabits(id);
        return bits;
    }

    public static void storeDataSet(Dataset set) {
        StorageManager.storeDataSet(set);
        if (!DatasetCache.getInstance().contains(set.getId())) {
            DatasetCache.getInstance().add(set);
        }
    }

    public static List loadDatasetsforWorkflow(int id) {
        List sets = StorageManager.loadDatasetsforWorkflow(id);
        List cachedSets = new ArrayList();
        DatasetCache cache = DatasetCache.getInstance();
        for (Iterator it = sets.iterator(); it.hasNext(); ) {
            Dataset dSet = (Dataset) it.next();
            if (cache.contains(dSet.getId())) {
                dSet = cache.getDataset(dSet.getId());
                cachedSets.add(dSet);
            } else {
                cachedSets.add(dSet);
                cache.add(dSet);
            }
        }
        return cachedSets;
    }

    public static Databit loadDatabit(int dSetId, String databitName) {
        return StorageManager.loadDatabit(dSetId, databitName);
    }

    public static Dataset loadChildDataset(int id, String datasetName) {
        Dataset dSet = StorageManager.loadChildDataset(id, datasetName);
        if (dSet != null) {
            if (DatasetCache.getInstance().contains(dSet.getId())) {
                dSet = DatasetCache.getInstance().getDataset(dSet.getId());
            } else {
                DatasetCache.getInstance().add(dSet);
            }
        }
        return dSet;
    }

    public static List loadDatasetsforDataset(int id) {
        List sets = StorageManager.loadChildDatasetsforDataset(id);
        List dSets = new ArrayList();
        DatasetCache cache = DatasetCache.getInstance();
        for (Iterator it = sets.iterator(); it.hasNext(); ) {
            Dataset dSet = (Dataset) it.next();
            if (cache.contains(dSet.getId())) {
                dSet = cache.getDataset(dSet.getId());
                dSets.add(dSet);
            } else {
                dSets.add(dSet);
                cache.add(dSet);
            }
        }
        return dSets;
    }

    /**
     * Returns a list of Integers of wfids that attach this dataset in any way
     */
    public static List loadWorkflowIdsForDataset(int dsetId) {
        List wfIds = new ArrayList();
        wfIds = StorageManager.loadWorkflowIdsforDatasetTree(dsetId, wfIds);
        return wfIds;
    }

    public static void addDatasetToDataset(int dSetid, int childSetId) {
        StorageManager.addDatasetToDataset(dSetid, childSetId);
    }

    public static Dataset loadDataset(int setId) {
        Dataset dset = null;
        DatasetCache cache = DatasetCache.getInstance();
        if (cache.contains(setId)) {
            dset = cache.getDataset(setId);
        } else {
            dset = StorageManager.loadDataset(setId);
            cache.add(dset);
        }
        return dset;
    }

    /**
	 * Loads the dataset from db and adds it to the cache.
	 * @return : null if not found
	 */
    public static Dataset loadDatasetforWorkflow(int workflowId, String datasetName) {
        Dataset dset = StorageManager.loadDatasetforWorkflow(workflowId, datasetName);
        if (dset != null) {
            if (DatasetCache.getInstance().contains(dset.getId())) {
                dset = DatasetCache.getInstance().getDataset(dset.getId());
            } else {
                DatasetCache.getInstance().add(dset);
            }
        } else {
            Logger.ERROR("Requested dataset: " + datasetName + " does not exist for wf " + workflowId);
        }
        return dset;
    }

    public static void clearCache() {
        DatasetCache.getInstance().clearCache();
    }

    public static void removeFromCache(int dsetId) {
        DatasetCache.getInstance().remove(dsetId);
    }

    public void detachDatasetFromWorkflow(int dSetId, int wfId) {
        StorageManager.detachDatasetFromWorkflow(dSetId, wfId);
    }
}
