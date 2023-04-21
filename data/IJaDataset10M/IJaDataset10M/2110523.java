package org.atlantal.api.cms.definition;

import org.atlantal.api.cms.util.ListOrders;

/**
 * <p>Titre : Ewaloo</p>
 * <p>Description : Moteur de recherche structur�</p>
 * <p>Copyright : Copyright (c) 2002</p>
 * <p>Soci�t� : Mably Multim�dia</p>
 * @author Fran�ois MASUREL
 * @version 1.0
 */
public interface List extends AggregationContentDefinition {

    /**
     * @return group by
     */
    boolean getGroupBy();

    /**
     * @param groupby groupby
     */
    void setGroupBy(boolean groupby);

    /**
     * @return orders
     */
    ListOrders getOrders();

    /**
     * @param list list
     * @param orders orders
     * @return orders
     */
    ListOrders getOrders(List list, ListOrders orders);

    /**
     * @param itemid itemid
     * @param order order
     */
    void addOrder(Integer itemid, int order);
}
