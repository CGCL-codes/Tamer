package org.isurf.gdssu.datapool.db.entities;

import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author nodari
 */
@Local
public interface SynchronizationitemFacadeLocal {

    void create(Synchronizationitem synchronizationitem);

    void edit(Synchronizationitem synchronizationitem);

    void remove(Synchronizationitem synchronizationitem);

    Synchronizationitem find(Object id);

    List<Synchronizationitem> findAll();
}
