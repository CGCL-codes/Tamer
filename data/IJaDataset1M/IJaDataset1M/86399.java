package net.deytan.wofee.gae.persistence.action;

import net.deytan.wofee.exception.PersistenceException;
import com.google.appengine.api.datastore.Key;

public interface StoreAction extends Action {

    Object store(Object instance, Key parentKey) throws PersistenceException;
}
