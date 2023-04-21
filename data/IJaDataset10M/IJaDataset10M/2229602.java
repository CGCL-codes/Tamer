package pl.ehotelik.portal.dao.hotel;

import pl.ehotelik.portal.dao.EntityPortalDao;
import pl.ehotelik.portal.domain.hotel.HotelBreak;
import pl.ehotelik.portal.exception.ServiceException;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: mkr
 * Date: Aug 25, 2010
 * Time: 9:24:34 PM
 * This is s representation of HotelBreak object.
 */
public interface HotelBreakDao extends EntityPortalDao<HotelBreak> {

    public HotelBreak load(final Date date) throws ServiceException;

    public List<HotelBreak> loadAllAvailableBreaks(final Date date) throws ServiceException;
}
