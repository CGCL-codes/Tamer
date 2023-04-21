package com.acv.service.catalog;

import java.util.List;
import com.acv.dao.catalog.deals.model.AdaptedDeal;
import com.acv.dao.catalog.locations.airports.model.Airport;
import com.acv.service.common.exception.InvalidObjectException;
import com.acv.service.common.exception.ObjectAlreadyExistsException;
import com.acv.service.common.exception.ObjectNotFoundException;

public interface AdaptedDealManager {

    /**
	 * This method returns all the deal objects which are applied with CMS contents.
	 * @param id
	 * @return <code>AdaptedDeal</code>
	 * @throws ObjectNotFoundException
	 */
    public AdaptedDeal getAdaptedDeal(Long id) throws ObjectNotFoundException;

    /**
	 * According to the audience group in which the current user is and the origin city from
	 * which the current user travels, this method returns all available deals associate with
	 * the audience and origin city.
	 * @param originCityId
	 * @param audience
	 * @return <code>List<AdaptedDeal></code>
	 */
    public List<AdaptedDeal> getAdaptedDealsByOriginAndAudience(String originCityId, String audience);

    /**
	 * According to the audience group in which the current user is, this method returns all available
	 * deals associate with the audience.
	 * @param audience
	 * @return <code>List<AdaptedDeal></code>
	 */
    public List<AdaptedDeal> getAdaptedDealsByAudience(String audience);

    /**
	 * According to the audience group in which the current user is, this method returns all available
	 * departure cities associated with the deals.
	 * @param audience
	 * @return <code>List<Airport></code>
	 */
    public List<Airport> getDepartureAirportAvalaibleForDealsByAudience(String audience);

    /**
	 * This method.
	 *
	 * @return the adapted deals
	 */
    public List<AdaptedDeal> getAdaptedDeals();

    /**
	 *
	 */
    public void removeAll();

    /**
	 * @param id
	 * @throws ObjectNotFoundException
	 */
    public void removeAdaptedDeal(Long id) throws ObjectNotFoundException;

    /**
	 * @param adaptedDeal
	 * @throws ObjectAlreadyExistsException
	 * @throws InvalidObjectException
	 */
    public void saveAdaptedDeal(AdaptedDeal adaptedDeal) throws ObjectAlreadyExistsException, InvalidObjectException;
}
