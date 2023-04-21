package org.azrul.epice.dao.factory;

import org.azrul.epice.dao.*;
import org.azrul.epice.db4o.daoimpl.DB4OItemDAO;

/**
 *
 * @author Azrul
 */
public class ItemDAOFactory {

    public static ItemDAO getInstance() {
        return new DB4OItemDAO();
    }
}
