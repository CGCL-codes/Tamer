package rs.observers;

import rs.items.*;
import javax.ejb.*;
import java.util.Date;
import java.util.Iterator;
import java.util.Collection;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.jboss.annotation.ejb.Service;
import org.jboss.annotation.ejb.Management;

@Service
@Remote(OrderService.class)
@Management(OrderServiceManagement.class)
public class OrderServiceImpl implements OrderService, OrderServiceManagement {

    private int attribute;

    private int DAY = 60 * 60 * 1000;

    @Resource
    private SessionContext ctx;

    @PersistenceContext
    protected EntityManager em;

    public void setAttribute(int attribute) {
        this.attribute = attribute;
    }

    public int getAttribute() {
        return this.attribute;
    }

    public void callback(Timer t) {
        Collection c = em.createQuery("from Item i").getResultList();
        Iterator i = c.iterator();
        while (i.hasNext()) {
            Item it = (Item) i.next();
            if (it.totalItems() < it.getOrderLimit()) {
            }
        }
    }

    public void create() throws Exception {
    }

    public void start() throws Exception {
        Date now = new Date();
    }

    public void stop() {
    }

    public void destroy() {
    }
}
