package org.jabusuite.transaction.session;

import javax.ejb.Remote;
import org.jabusuite.core.companies.JbsCompany;
import org.jabusuite.core.users.JbsUser;
import org.jabusuite.core.utils.EJbsObject;
import org.jabusuite.core.utils.JbsManagementRemote;
import org.jabusuite.transaction.ShoppingCart;

/**
 *
 * @author hilwers
 */
@Remote
public interface ShoppingCartsRemote extends JbsManagementRemote {

    public void createDataset(ShoppingCart shoppingCart, JbsUser user, JbsCompany company);

    /**
	 * Updates the specified shoppingCart.
         * @param shoppingCart
         * @param changeUser
         * @throws EJbsObject 
	 */
    public void updateDataset(ShoppingCart shoppingCart, JbsUser changeUser) throws EJbsObject;

    /**
	 * Finds the shoppingCart with the specified id, but without it's positions.
     * Use <code>findDataset(ling id, boolean withPositions)</code> instead to
     * get the postions also.
	 * @param id
	 * @return
	 */
    public ShoppingCart findDataset(long id);

    /**
     * Finds a shopping-cart with the specified id.
     * @param id The id of the shopping-cart to look for
     * @param withPositions Set this to true, if the positions shall also be fetched
     * @return The ShoppingCart matching the given specifications
     */
    public ShoppingCart findDataset(long id, boolean withPositions);
}
