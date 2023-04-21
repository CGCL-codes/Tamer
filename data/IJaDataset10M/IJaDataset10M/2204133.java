package org.zeroexchange.dao.resource.money.fixed;

import java.util.Collection;
import org.zeroexchange.dao.IntegerPKDAO;
import org.zeroexchange.model.resource.money.che.CHEPayment;

/**
 * @author black
 *
 */
public interface CHEPaymentDAO extends IntegerPKDAO<CHEPayment> {

    /**
     * Returns actual amount of money user have.
     */
    Collection<CHEPayment> getPayments(Integer userId);
}
