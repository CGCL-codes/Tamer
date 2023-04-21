package net.sf.contrail.core.impl;

import java.util.List;
import java.util.Map;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyRange;

/**
 * Contrail works with any storage system that can store Entities by Key.
 * Contrail uses this interface to access basic storage. 
 */
public interface RawStorage {

    public Entity get(Key arg0) throws EntityNotFoundException;

    public Map<Key, Entity> get(Iterable<Key> arg0);

    public Key put(Entity arg0);

    public List<Key> put(Iterable<Entity> arg0);

    public void delete(Key... arg0);

    public void delete(Iterable<Key> arg0);

    public KeyRange allocateIds(String arg0, long arg1);

    public KeyRange allocateIds(Key arg0, java.lang.String arg1, long arg2);
}
