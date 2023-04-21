package org.azrul.epice.dao.query.factory;

import org.azrul.epice.dao.query.SupervisedItemsSingleRootQuery;
import org.azrul.epice.db4o.daoimpl.queryimpl.DB4OSupervisedItemsSingleRootQuery;

/**
 *
 * @author Azrul
 */
public class SupervisedItemsSingleRootQueryFactory {

    public static SupervisedItemsSingleRootQuery getInstance() {
        return new DB4OSupervisedItemsSingleRootQuery();
    }
}
