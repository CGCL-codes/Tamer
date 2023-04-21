package pl.ehotelik.portal.service.spa;

import pl.ehotelik.portal.domain.spa.TreatmentDay;
import pl.ehotelik.portal.domain.spa.TreatmentPath;
import pl.ehotelik.portal.exception.ObjectNotFoundException;
import pl.ehotelik.portal.exception.ServiceException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: mkr
 * Date: Aug 25, 2010
 * Time: 9:21:57 PM
 * This is a representation of TreatmentPathService object.
 */
public interface TreatmentPathService {

    public Long saveTreatmentPath(final TreatmentPath treatmentPath) throws ServiceException;

    public TreatmentPath loadTreatmentPath(final long id) throws ServiceException;

    public List<TreatmentPath> loadAllTreatmentPaths() throws ServiceException;

    public void deleteTreatmentPackage(final TreatmentPath treatmentPath) throws ServiceException;

    public TreatmentDay findTreatmentDay(final long dayNumber, final TreatmentPath treatmentPath) throws ObjectNotFoundException;
}
