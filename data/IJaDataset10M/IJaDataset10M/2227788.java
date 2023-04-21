package com.aipo.orm.query;

import java.util.List;
import org.apache.cayenne.access.DataContext;

/**
 * 
 * 
 * @param <M>
 */
public interface Query<M> {

    public DataContext getDataContext();

    public List<M> fetchList();

    public M fetchSingle();

    public void deleteAll();
}
