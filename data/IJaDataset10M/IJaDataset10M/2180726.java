package org.qsari.effectopedia.core.embeddedobjects;

import java.util.ArrayList;
import java.util.StringTokenizer;
import org.qsari.effectopedia.core.Effectopedia;
import org.qsari.effectopedia.search.SearchableItem;

public class DataValue_List<T extends DataValue<?>> extends DataValue<ArrayList<T>> {

    public DataValue_List(SearchableItem searchableItem, Class<T> valueClass) {
        super(searchableItem);
        this.valueClass = valueClass;
        value = new ArrayList<T>();
    }

    @SuppressWarnings("unchecked")
    public DataValue_List<T> clone() {
        DataValue_List<T> clone = new DataValue_List<T>(searchItem, valueClass);
        clone.value = new ArrayList<T>();
        for (T dataValue : value) clone.value.add((T) dataValue.clone());
        return clone;
    }

    public String toString() {
        StringBuilder val = new StringBuilder();
        for (T dataValue : value) {
            val.append(dataValue.toString());
            val.append(DELIMITER);
        }
        return val.toString();
    }

    public void parseString(String fromString) {
        DataValue_List<T> oldValue = this.clone();
        StringTokenizer dataValueString = new StringTokenizer(fromString, DELIMITER);
        value.clear();
        value.ensureCapacity(dataValueString.countTokens());
        while (dataValueString.hasMoreTokens()) {
            T dataValue = null;
            try {
                dataValue = (T) valueClass.newInstance();
                dataValue.setSearchItem(this.searchItem);
                dataValue.parseString(dataValueString.nextToken());
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            value.add(dataValue);
        }
        Effectopedia.getEffectopedia().getData().updateSearchIndices(oldValue, this);
    }

    public static final boolean isValidList(String string) {
        return string.matches(REGEX);
    }

    protected Class<T> valueClass;

    public static final String DELIMITER = ",";

    public static final String REGEX = ",+";
}
