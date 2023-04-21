package pl.ehotelik.portal.dao.hotel;

import pl.ehotelik.portal.dao.EntityPortalDao;
import pl.ehotelik.portal.domain.hotel.ClientBookingOrder;
import pl.ehotelik.portal.exception.ServiceException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: mkr
 * Date: Aug 25, 2010
 * Time: 9:24:34 PM
 * This is s representation of ClientBookingOrderDao object.
 */
public interface ClientBookingOrderDao extends EntityPortalDao<ClientBookingOrder> {

    public List<ClientBookingOrder> load(final String date) throws ServiceException;
}
