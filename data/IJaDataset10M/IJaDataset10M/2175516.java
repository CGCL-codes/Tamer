package net.sourceforge.nattable.data;

import java.io.Serializable;
import java.util.List;

public interface IBulkUpdateSupport<T> {

    public void addUpdates(Serializable rowObjectId, List<Object> cellValues, List<String> properties, DataUpdateHelper<T> helper);

    public void removeUpdate(Serializable rowObjectId);

    public void commitUpdates(List<T> data, DataUpdateHelper<T> helper);
}
