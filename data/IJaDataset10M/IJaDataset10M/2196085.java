package org.hironico.dbtool2.querymanager;

/**
 * Cette interface décrit le fonctionnement d'un listener sur le query manager.
 * On va appeler les différentes méthodes en fonction de ce qui se passe sur le 
 * query manager.
 * @author hironico
 * @since 2.0.0
 */
public interface QueryManagerListener {

    public void queryManaged(QueryManagerEvent evt);
}
