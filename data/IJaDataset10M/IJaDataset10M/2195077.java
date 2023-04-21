package org.gamesroom.database.utils;

import java.io.Serializable;
import org.gamesroom.database.Customer;
import org.gamesroom.database.Lease;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

public class DeleteInterceptor extends EmptyInterceptor {

    /**
	 * 
	 */
    private static final long serialVersionUID = 7512746333005423977L;

    @Override
    public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        if (entity instanceof Customer) {
            Customer c = (Customer) entity;
            for (Lease lease : c.getLeases()) {
                lease.setCustomer(null);
            }
        } else {
            super.onDelete(entity, id, state, propertyNames, types);
        }
    }
}
