package net.ad.adsp.jsf;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.context.FacesContext;
import org.ajax4jsf.model.DataVisitor;
import org.ajax4jsf.model.ExtendedDataModel;
import org.ajax4jsf.model.Range;
import org.ajax4jsf.model.SequenceRange;
import org.jboss.seam.framework.EntityQuery;

public abstract class EntityDataModel<T, Integer> extends ExtendedDataModel {

    private String ID_METHOD_NAME = "";

    private boolean isWalked = false;

    private EntityQuery entityQuery;

    private String currentId;

    private Map<String, T> wrappedData = new HashMap<String, T>();

    private List<String> wrappedKeys;

    public void setEntityQuery(EntityQuery entityQuery) {
        this.entityQuery = entityQuery;
    }

    @Override
    public Object getRowKey() {
        return currentId;
    }

    @Override
    public void setRowKey(Object key) {
        currentId = String.valueOf(key);
    }

    @Override
    public void walk(FacesContext fCtx, DataVisitor visitor, Range range, Object argument) throws IOException {
        int firstResult = ((SequenceRange) range).getFirstRow();
        int maxResults = ((SequenceRange) range).getRows();
        entityQuery.setFirstResult(firstResult);
        entityQuery.setMaxResults(maxResults);
        List<T> list = entityQuery.getResultList();
        wrappedKeys = new ArrayList<String>();
        wrappedData = new HashMap<String, T>();
        for (T row : list) {
            String keyValue = getKeyValue(row);
            wrappedKeys.add(keyValue);
            wrappedData.put(keyValue, row);
            visitor.process(fCtx, keyValue, argument);
        }
    }

    @Override
    public int getRowCount() {
        return entityQuery.getResultCount().intValue();
    }

    @Override
    public Object getRowData() {
        if (currentId == null || currentId.equals("null")) {
            return null;
        } else {
            T ret = wrappedData.get(currentId);
            if (ret == null) {
                ret = (T) findById(currentId);
                wrappedData.put(currentId, ret);
                return ret;
            } else {
                return ret;
            }
        }
    }

    @Override
    public int getRowIndex() {
        return 0;
    }

    /**
	 * Unused rudiment from old JSF staff.
	 */
    @Override
    public Object getWrappedData() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isRowAvailable() {
        if (currentId == null || currentId.equals("null")) {
            return false;
        } else {
            for (T row : (List<T>) entityQuery.getResultList()) {
                String keyValue = getKeyValue(row);
                if (keyValue.equals(currentId)) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
	 * Unused rudiment from old JSF staff.
	 */
    @Override
    public void setRowIndex(int newRowIndex) {
    }

    /**
	 * Unused rudiment from old JSF staff.
	 */
    @Override
    public void setWrappedData(Object data) {
        throw new UnsupportedOperationException();
    }

    private String getId(T row) {
        String id;
        try {
            Method idMethod = row.getClass().getMethod(ID_METHOD_NAME, new Class[0]);
            Class<?> idType = idMethod.getReturnType();
            id = String.valueOf(idMethod.invoke(row, new Object[0]));
        } catch (Exception e) {
            throw new javax.faces.FacesException("Failed to obtain row id", e);
        }
        return id;
    }

    private T findById(String currentId) {
        for (T row : (List<T>) entityQuery.getResultList()) {
            String keyValue = getKeyValue(row);
            if (keyValue.equals(currentId)) {
                return row;
            }
        }
        ;
        return null;
    }

    private String getKeyValue(T row) {
        StringBuilder keyValue = new StringBuilder();
        if (row instanceof Object[]) {
            for (Object obj : (Object[]) row) {
                if (obj != null) {
                    keyValue.append(String.valueOf(obj.hashCode()));
                } else {
                    keyValue.append("0");
                }
            }
        } else {
            keyValue.append(String.valueOf(row.hashCode()));
        }
        return keyValue.toString();
    }

    private void settingEQParameters(int firstRes, int maxRes) {
        entityQuery.setFirstResult(firstRes);
        entityQuery.setMaxResults(maxRes);
    }
}
