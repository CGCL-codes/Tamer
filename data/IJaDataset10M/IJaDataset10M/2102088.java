package isp.apps.example.shoppingcart.persistence.hibernate;

import isp.apps.example.shoppingcart.domain.CartItem;
import isp.apps.example.shoppingcart.domain.OrderStatus;
import isp.apps.example.shoppingcart.persistence.OrderStatusDirectory;
import isp.apps.example.shoppingcart.persistence.PersistenceManagerFactory;
import isp.utils.cjis.persistence.PersistenceException;
import isp.utils.cjis.persistence.PersistenceManager;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HibernateOrderStatusDirectory implements OrderStatusDirectory {

    private PersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();

    private static final Log log = LogFactory.getLog(HibernateOrderStatusDirectory.class);

    public List getMaxValidStatus() {
        try {
            return persistenceManager.find("get.maxValidStatus", Collections.EMPTY_LIST);
        } catch (PersistenceException pe) {
            log.error(pe, pe);
            return null;
        }
    }

    public List getMinValidStatus() {
        try {
            return persistenceManager.find("get.minValidStatus", Collections.EMPTY_LIST);
        } catch (PersistenceException pe) {
            log.error(pe, pe);
            return null;
        }
    }

    public OrderStatus getStatusById(Integer id) {
        try {
            return (OrderStatus) persistenceManager.load(OrderStatus.class, id);
        } catch (PersistenceException pe) {
            log.error(pe, pe);
            return null;
        }
    }

    public List getAllStatuses() {
        try {
            return persistenceManager.find("get.allStatuses", Collections.EMPTY_LIST);
        } catch (PersistenceException pe) {
            log.error(pe, pe);
            return null;
        }
    }

    public List getValidStatuses() {
        try {
            return persistenceManager.find("get.allValidStatuses", Collections.EMPTY_LIST);
        } catch (PersistenceException pe) {
            log.error(pe, pe);
            return null;
        }
    }
}
