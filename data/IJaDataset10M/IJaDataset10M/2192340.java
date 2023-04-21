package bd.com.escenic.flexilunch.dao;

import bd.com.escenic.flexilunch.model.Item;
import bd.com.escenic.flexilunch.model.ItemImpl;

/**
 * $Id: ItemDAOTest.java 15 2009-06-09 04:04:09Z shihab.uddin@gmail.com $.
 *
 * @author <a href="mailto:shihab.uddin@gmail.com">Shihab Uddin</a>
 * @version $Revision: 15 $
 */
public class ItemDAOTest extends EntityDAOTest<Item> {

    public ItemDAOTest() {
        super(ItemImpl.class, ItemDAO.class);
    }
}
