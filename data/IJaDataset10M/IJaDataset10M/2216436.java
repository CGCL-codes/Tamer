package org.dspace.content.dao;

import org.dspace.content.dao.postgres.BundleDAOPostgres;
import org.dspace.core.Context;
import org.dspace.dao.StackableDAOFactory;

/**
 * @author James Rutherford
 */
public class BundleDAOFactory extends StackableDAOFactory {

    public static BundleDAO getInstance(Context context) {
        return StackableDAOFactory.prepareStack(context, BundleDAO.class, new BundleDAOCore(context), new BundleDAOPostgres(context), "dao.stack.bundle.enabled");
    }
}
