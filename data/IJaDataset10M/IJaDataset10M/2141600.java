package bookez.model.service;

import java.util.List;
import bookez.model.businessobject.TripDailyRecord;
import bookez.model.exception.BusinessLogicLayerException;

public interface TripService {

    List<TripDailyRecord> findAll() throws BusinessLogicLayerException;

    TripDailyRecord findById(Long id) throws BusinessLogicLayerException;

    boolean updateTrip(TripDailyRecord trip) throws BusinessLogicLayerException;

    boolean saveTrip(TripDailyRecord trip) throws BusinessLogicLayerException;

    boolean suspendTrip(TripDailyRecord trip) throws BusinessLogicLayerException;

    boolean terminateTrip(TripDailyRecord trip) throws BusinessLogicLayerException;
}
