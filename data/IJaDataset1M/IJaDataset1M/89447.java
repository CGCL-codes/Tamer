package org.redpin.server.standalone.db.homes;

import java.util.List;

/**
 * Interface for all Entity Homes. An Entity Home provides all necessary 
 * function to add, get, update and remove entities to/from the database 
 * 
 * @author Pascal Brogle (broglep@student.ethz.ch)
 *
 * @param <T> Element type
 * @param <ID> Primary key type (depending on database)
 */
public interface IEntityHome<T, ID> {

    public T add(T e);

    public List<T> add(List<T> l);

    public T get(T e);

    public T getById(ID id);

    public List<T> get(List<T> list);

    public List<T> getById(List<ID> ids);

    public List<T> getAll();

    public boolean update(T e);

    public boolean update(List<T> list);

    public boolean remove(T e);

    public boolean remove(List<T> list);

    public boolean removeAll();
}
