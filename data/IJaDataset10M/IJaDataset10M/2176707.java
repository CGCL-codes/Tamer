package net.sf.sqlking.api.client;

import java.util.List;

public interface ChangeTrackerList<E> extends List<E> {

    public void setListChanges(ListChanges listChanges);

    public ListChanges getListChanges();
}
