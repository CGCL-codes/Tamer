package com.trohko.jfsim.core.data;

public interface DataProvider<D extends DataObject> {

    public void setDataListener(DataListener<D> listener);

    public DataListener<D> getDataListener();
}
