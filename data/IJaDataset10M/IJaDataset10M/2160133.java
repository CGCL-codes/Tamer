package com.acv.common.model.bus;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import com.acv.common.model.BusRequestObject;
import com.acv.common.model.criterion.SearchCriterion;
import com.acv.common.model.entity.Passenger;

/**
 * <p>The SearchRequest consists in the structure representing the client's product
 * search parameters. Every specific client need is represented by a SearchCriterion
 * instance which is added to the request using a unique user-defined key.
 * <p>The handling of the SearchCriterion is simplified by the use of unique
 * keys which are created by the request user and can be reused to retrieve the
 * SearchCriterion at any given time.
 * <p><b>Note:</b> the user defined key should be explicit in order to simplify the
 * retrieval of the SearchCriterion and also to facilitate the analysis of the subsequent
 * response which keeps the same keys to map the BookableServices.
 */
public interface SearchRequest extends BusRequestObject {

    /**
     * Add a SearchCriterion to the current SearchRequestImpl.
     * @param key the user-defined unique key of this criterion.
     * @param searchCriterion the SearchCriterion representing the new client requirement.
     */
    void addSearchCriterion(String key, SearchCriterion searchCriterion);

    /**
     * Returns the Set of currently contained user-defined unique key.
     * @return the Set of currently contained user-defined unique key.
     */
    Set<String> getKeys();

    /**
     * Returns the SearchCriterion attached to the given user-defined unique key.
     * @param key the user-defined unique key attached to the desired criterion.
     * @return the SearchCriterion attached to the given user-defined unique key.
     */
    SearchCriterion getCriterion(String key);

    /**
     * Returns the Collection of currently contained SearchCriterions.
     * @return the Collection of currently contained SearchCriterions.
     */
    Collection<SearchCriterion> getCriterions();

    /**
     * Returns the list of Passenger that take part into this SearchRequest's criterion.
     * @return the list of Passenger that take part into this SearchRequest's criterion.
     */
    List<Passenger> getPassengers();

    /**
     * Returns the number of days of variance that all this SearchRequest's
     * criterion are ready to accept for the potential BookableService date
     * of applicability. It should <b>return 0</b> for a non-flexibility
     * on dates of service.
     * @return the number of days of variance that all this SearchRequest's
     * criterion are ready to accept for the potential BookableService date
     * of applicability.
     */
    int getDaysVariance();

    /**
     * Sets the number of days of variance that all this SearchRequest's
     * criterion are ready to accept for the potential BookableService date
     * of applicability.
     * @param daysVariance the number of days of variance that all this
     * SearchRequest's criterion are ready to accept for the potential
     * BookableService date of applicability.
     */
    void setDaysVariance(int daysVariance);
}
