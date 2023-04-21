package org.atlantal.api.cms.content;

import org.atlantal.api.cms.util.ListOrders;

/**
 * <p>Titre : Ewaloo</p>
 * <p>Description : Moteur de recherche structur�</p>
 * <p>Copyright : Copyright (c) 2002</p>
 * <p>Soci�t� : Mably Multim�dia</p>
 * @author Fran�ois MASUREL
 * @version 1.0
 */
public interface ListContent extends CollectionContent {

    /**
     * @return orders
     */
    ListOrders getOrders();
}
