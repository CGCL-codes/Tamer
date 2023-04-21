package org.osmius.service;

import org.osmius.model.OsmService;
import org.osmius.model.OsmServiceSla;
import org.osmius.service.exceptions.OsmServiceExistsException;
import java.util.HashMap;
import java.util.List;

/**
 * Business Service Interface to handle communication between web and persistence layer.
 * Exposes the neccessary methods to handle a service manager
 */
public interface OsmServiceManager extends Manager {

    /**
    * Gets the services from the database
    *
    * @param osmService The service to be retrieve, can be null
    * @return A list with the services
    */
    public List getOsmServices(OsmService osmService);

    /**
    * Gets an instance from the database
    *
    * @param idnService The Service ID
    * @return The service information
    */
    public OsmService getOsmService(final String idnService);

    /**
    * Gets a partial list of services
    * @param osmService A parametrized object to filter the result data. Can be null
    * @param startPosition The start position
    * @param pageSize The number of items to be retrieved
    * @param orderBy The order
    * @return A list of services
    */
    public List getOsmServices(OsmService osmService, final int startPosition, final int pageSize, final String orderBy);

    /**
    * Gets the size of a list of services
    * @param osmService A parametrized objedt to filter the result data. Can be null
    * @return The size
    */
    public Long getSizeOsmServices(OsmService osmService);

    /**
    * Deletes an array of services
    * @param services The array of services to be deleted
    */
    public void removeOmsServices(String[] services);

    /**
    * Saves a service
    * @param osmService The service to be saved
    * @throws OsmServiceExistsException An exception to be thrown if the service already exists
    */
    public void saveOsmService(OsmService osmService) throws OsmServiceExistsException;

    /**
    * Updats a service
    * @param osmService The service to be updated
    */
    public void updateOsmService(OsmService osmService);

    public List getAvaOsmNServicesSubscription(String idnUser, String typNot, int working);

    public List getOsmServicesAtLeastOneInstance();

    public List getOsmServicesUsersByClient(String idnClient);

    public boolean checkServiceSchedules(String[] schedules);

    public List getOsmServicesGroupByCriticity();

    public List getOsmServicesGroupByAvailability();

    public List getOsmServicesWithoutInstances();

    public List getOsmInstancesByServiceWithDependencies(String idnService);

    public List getOsmInstancesByServiceWithDependencies(String idnService, String aggr, String tag);

    public void saveOsmServiceAndSLA(OsmService osmService, OsmServiceSla osmServiceSla, Long aggr) throws OsmServiceExistsException;

    public void updateOsmServiceAndSLA(OsmService service, OsmServiceSla osmServiceSla, Long aggr);

    public List getTopFiveNonAvailableServices(String idnSla, String start_date, String end_date);

    public List getDWStats(String idnSla, String idnService, String start_date, String end_date);

    public int getServiceStatus(String idnService);

    public List getLastServicesIdnsByAvailability(int indAvailability, int limit);

    public List getIdAndDes(OsmService osmService);

    public List getIdAndDesWithEmptyServices(OsmService osmService);

    public void updateResponsible(String userId);

    public List getOsmServicesUsers();

    HashMap getHashServiceInstances();
}
