package edu.upc.lsi.kemlg.aws.knowledge;

import java.util.HashMap;
import java.util.Map.Entry;

/**
 *
 * @author biba
 */
public class RuntimeDataHolder<M extends Object> {

    HashMap<String, HashMap<String, M>> data = new HashMap<String, HashMap<String, M>>();

    public void putData(Class<? extends M> classUsedForData, String keyForTheseClasses, M dataToStore) {
        HashMap<String, M> classifiedData = data.get(classUsedForData.getName());
        if (classifiedData == null) {
            classifiedData = new HashMap<String, M>();
        }
        classifiedData.put(keyForTheseClasses, dataToStore);
        data.put(classUsedForData.getName(), classifiedData);
    }

    public void putAllData(Class<? extends M> classUsedForData, HashMap<String, M> allDataToStore) {
        data.put(classUsedForData.getName(), allDataToStore);
    }

    public void addAllData(Class<? extends M> classUsedForData, HashMap<String, M> allDataToStore) {
        HashMap<String, M> classifiedData = data.get(classUsedForData.getName());
        if (classifiedData == null) {
            classifiedData = new HashMap<String, M>();
            classifiedData.putAll(allDataToStore);
            data.put(classUsedForData.getName(), classifiedData);
        } else {
            for (Entry<String, M> entry : allDataToStore.entrySet()) {
                classifiedData.put(entry.getKey(), entry.getValue());
            }
        }
        data.put(classUsedForData.getName(), classifiedData);
    }

    public M getData(Class<? extends M> classUsedForData, String keyForTheseClasses) {
        M result = null;
        HashMap<String, M> classifiedData = data.get(classUsedForData.getName());
        if (classifiedData != null) {
            result = classifiedData.get(keyForTheseClasses);
        }
        return result;
    }

    public HashMap<String, M> getAllData(Class<? extends M> classUsedForData) {
        HashMap<String, M> result = data.get(classUsedForData.getName());
        return result;
    }

    public M deleteData(Class<? extends M> classUsedForData, String keyForTheseClasses) {
        M result = null;
        HashMap<String, M> classifiedData = data.get(classUsedForData.getName());
        if (classifiedData != null) {
            result = classifiedData.remove(keyForTheseClasses);
        }
        return result;
    }

    public HashMap<String, M> deleteAllData(Class<? extends M> classUsedForData) {
        HashMap<String, M> result = data.remove(classUsedForData.getName());
        return result;
    }
}
