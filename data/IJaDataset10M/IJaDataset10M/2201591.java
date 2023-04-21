package org.atlantal.impl.cms.content;

import org.atlantal.api.cms.content.ListContent;
import org.atlantal.api.cms.id.ListContentId;
import org.atlantal.api.cms.util.ListOrders;

/**
 * <p>Titre : Ewaloo</p>
 * <p>Description : Moteur de recherche structur�</p>
 * <p>Copyright : Copyright (c) 2002</p>
 * <p>Soci�t� : Mably Multim�dia</p>
 * @author Fran�ois MASUREL
 * @version 1.0
 */
public abstract class ListContentInstance extends CollectionContentInstance implements ListContent {

    /**
     * Constructor
     */
    public ListContentInstance() {
    }

    /**
     * {@inheritDoc}
     */
    public ListOrders getOrders() {
        return ((ListContentId) getId()).getOrders();
    }
}
