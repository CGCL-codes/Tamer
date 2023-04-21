package com.spring66.training.dao;

import com.spring66.training.entity.Owner;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author TwinP
 */
public interface OwnerDao {

    public void create(Owner own);

    public void storeOwner(Owner own);

    public Owner loadOwner(Integer pk);

    public void update(Owner own);

    public void delete(Owner own);

    public List<Owner> readAll();

    public Collection<Owner> findOwner(String name);
}
