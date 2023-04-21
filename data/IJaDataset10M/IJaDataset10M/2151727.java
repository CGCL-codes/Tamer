package org.openjef.business.domain;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.openjef.business.persistence.Business2PersistenceLayer.WriteResult;

public class ModelManager {

    Hashtable<IModelKey, IModel> models = new Hashtable<IModelKey, IModel>();

    Hashtable<IModelCollectionKey, Collection<IModel>> collections = new Hashtable<IModelCollectionKey, Collection<IModel>>();

    public ModelManager() {
    }

    public IModel lookup(IModelKey key) {
        return this.models.get(key);
    }

    public void put(IModel model) {
        this.models.put(model.ojGetModelKey(), model);
    }

    public void put(Collection<IModel> models) {
        for (Iterator<IModel> it = models.iterator(); it.hasNext(); ) {
            put(it.next());
        }
    }

    public Collection<IModel> lookup(IModelCollectionKey key) {
        return this.collections.get(key);
    }

    public void put(IModelCollectionKey key, Collection<IModel> collection) {
        this.collections.put(key, collection);
    }

    /**
	 * liefert alle ge�nderten Modelle
	 * @return
	 */
    public Collection<IModel> getNewOrChanged() {
        return models.values();
    }

    /**
	 * Aktualisierung der Modelle anhand der R�ckgabe aus der Persistenzschicht.
	 * Es werden die Schl�sselwerte gesetzt und Modelle �ber ihren transienten Teil herausgesucht
	 * Dabei �ndern sich die Hashcodes der TMModelKey-Instanzen und alle relevanten Hashtables m�ssen
	 * neu aufgebaut werden
	 * @param writeResult
	 */
    public void updateAfterWritePersistence(WriteResult writeResult) {
        List<IModel> updatedModels = new LinkedList<IModel>();
        for (Enumeration<IModel> itModels = models.elements(); itModels.hasMoreElements(); ) {
            IModel nextModel = itModels.nextElement();
            if (nextModel.ojGetModelKey().isNew()) {
                updatedModels.add(nextModel);
            }
        }
        for (Iterator<IModel> itUpdated = updatedModels.iterator(); itUpdated.hasNext(); ) {
            IModel nextModel = itUpdated.next();
            this.models.remove(nextModel.ojGetModelKey());
            boolean found = false;
            for (Iterator<IModelKey> itKey = writeResult.newKeys.iterator(); itKey.hasNext() && !found; ) {
                IModelKey nextKey = itKey.next();
                if (nextModel.ojGetModelKey().getTransientData().equals(nextKey.getTransientData())) {
                    nextModel.ojGetModelKey().update(nextKey);
                    found = true;
                }
            }
            if (!found) {
                System.err.println("Warning: no key found in updateAfterWritePersistence");
            }
            this.models.put(nextModel.ojGetModelKey(), nextModel);
        }
    }
}
