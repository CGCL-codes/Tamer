package net.sourceforge.nattable.data;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sourceforge.nattable.NatTable;
import net.sourceforge.nattable.typeconfig.content.ContentConfigRegistry;
import net.sourceforge.nattable.util.MethodNameGenerator;
import org.apache.log4j.Logger;

public class DefaultBulkUpdateSupport<T> implements IBulkUpdateSupport<T> {

    private Map<Serializable, String> errors;

    private ContentConfigRegistry contentConfigRegistry;

    private Map<Serializable, Map<String, Object>> updates;

    private Map<String, Method> methodsToUpdate;

    public DefaultBulkUpdateSupport(ContentConfigRegistry contentConfigRegistry) {
        this.contentConfigRegistry = contentConfigRegistry;
        errors = new HashMap<Serializable, String>();
        updates = new HashMap<Serializable, Map<String, Object>>();
        methodsToUpdate = new HashMap<String, Method>();
    }

    public void addUpdates(Serializable rowObjectId, List<Object> cellValues, List<String> properties, DataUpdateHelper<T> helper) {
        Map<String, Object> fieldToValues = new HashMap<String, Object>();
        for (int propertyIndex = 0; propertyIndex < properties.size(); propertyIndex++) {
            try {
                String fieldName = properties.get(propertyIndex);
                Object cellValue = (propertyIndex >= cellValues.size() ? cellValues.get(cellValues.size() - 1) : cellValues.get(propertyIndex));
                appendMapWithUpdate(fieldName, cellValue, fieldToValues, helper);
            } catch (Exception e) {
                errors.put(rowObjectId, e.getMessage());
                Logger.getLogger(NatTable.class).error("Error inserting update for row object " + rowObjectId, e);
            }
        }
        rowObjectId = rowObjectId == null ? helper.getRowIdResolver().resolveRowIdProperty(fieldToValues) : rowObjectId;
        updates.put(rowObjectId, fieldToValues);
    }

    private void appendMapWithUpdate(String fieldName, Object cellValue, Map<String, Object> fieldUpdates, DataUpdateHelper<T> helper) throws SecurityException, NoSuchMethodException {
        Class<T> updateTarget = helper.getRowObjectCreator().getRowClass();
        if (!methodsToUpdate.containsKey(fieldName)) {
            Method method = updateTarget.getDeclaredMethod(MethodNameGenerator.buildGetMethodName(fieldName), new Class[] {});
            method.setAccessible(true);
            methodsToUpdate.put(fieldName, method);
        }
        Class<?> propertyClass = helper.getPropertyResolver().getPropertyType(fieldName);
        Object value = null;
        if (!propertyClass.equals(String.class)) {
            value = helper.getPropertyInstanceCreator().getPropertyInstance(propertyClass, cellValue);
        } else {
            value = cellValue != null ? cellValue : "";
        }
        fieldUpdates.put(fieldName, value);
    }

    /**
	 * Callers of this method must lock the underlying list prior to committing
	 * updates.
	 */
    @SuppressWarnings("unchecked")
    public void commitUpdates(List<T> data, DataUpdateHelper<T> helper) {
        Method[] sortedMethods = null;
        T[] dataArray = data.toArray((T[]) new Object[0]);
        try {
            Comparator<T> comparator = helper.getRowComparator();
            Arrays.sort(dataArray, comparator);
            for (Serializable rowId : updates.keySet()) {
                IRowObjectCreator<T> dataHandler = helper.getRowObjectCreator();
                T rowObject = dataHandler.createRowObject(rowId);
                if (sortedMethods == null) {
                    sortedMethods = rowObject.getClass().getDeclaredMethods();
                    Arrays.sort(sortedMethods, new Comparator<Method>() {

                        public int compare(Method o1, Method o2) {
                            return o1.getName().compareTo(o2.getName());
                        }
                    });
                }
                int index = Arrays.binarySearch(dataArray, rowObject, comparator);
                boolean isUpdate = index >= 0;
                if (isUpdate) {
                    rowObject = dataArray[index];
                }
                Map<String, Object> updateValues = updates.get(rowId);
                for (String fieldName : updateValues.keySet()) {
                    Object updateValue = updateValues.get(fieldName);
                    boolean foundError = false;
                    try {
                        Method method = methodsToUpdate.get(fieldName);
                        IDataValidator validator = helper.getBeanConfigResolver() != null ? contentConfigRegistry.getDataValidator(helper.getBeanConfigResolver().getConfigType(rowObject, fieldName)) : null;
                        if (validator != null && !validator.validate(method.invoke(rowObject, new Object[] {}), updateValue)) {
                            errors.put(rowId, fieldName);
                            foundError = true;
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e.getMessage(), e);
                    }
                    if (!foundError) {
                        storeUpdate(rowObject, updateValue, fieldName, sortedMethods);
                    }
                }
                if (!isUpdate) {
                    data.add(rowObject);
                }
            }
        } finally {
            clearUpdates();
        }
    }

    private void storeUpdate(T rowObject, Object updateValue, String fieldName, Method[] sortedMethods) {
        try {
            String methodName = MethodNameGenerator.buildSetMethodName(fieldName);
            int lastIndex = sortedMethods.length;
            Method targetMethod = null;
            for (int index = 0; index < sortedMethods.length / 2; index++) {
                targetMethod = sortedMethods[index];
                if (targetMethod.getName().equals(methodName)) {
                    targetMethod.setAccessible(true);
                    break;
                } else if (--lastIndex >= sortedMethods.length / 2) {
                    targetMethod = sortedMethods[lastIndex];
                    targetMethod.setAccessible(true);
                    if (targetMethod.getName().equals(methodName)) {
                        break;
                    }
                }
            }
            if (targetMethod != null) {
                targetMethod.invoke(rowObject, new Object[] { updateValue });
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void clearUpdates() {
        updates.clear();
    }

    public void clearErrors() {
        errors.clear();
    }

    public Map<Serializable, String> getErrors() {
        return errors;
    }

    public boolean foundErrors() {
        return errors.size() > 0;
    }

    public void reset() {
        clearUpdates();
        clearErrors();
    }

    public void removeUpdate(Serializable rowObjectId) {
        assert (updates != null);
        updates.remove(rowObjectId);
    }
}
