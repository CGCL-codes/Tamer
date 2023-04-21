package com.acv.webapp.action.bus.search;

import org.apache.log4j.Logger;
import com.acv.dao.common.model.City;
import com.acv.service.i18n.model.I18nCacheableList;

/**
 * Action Class SearchResultAction to validator as well as work with changing language
 * @author Minxia Han
 *
 */
public class SearchResultAction extends SearchCustomVacationAction {

    private static final long serialVersionUID = 1L;

    private static final Logger log = Logger.getLogger(SearchResultAction.class);

    /**
	 * to get packageSearch result page
	 *
	 * @return String customvation or package
	 */
    public String result() {
        if (getSeRequestObj() == null) {
            if (log.isDebugEnabled()) log.debug("seRequest is null. return input");
            return INPUT;
        } else {
            City departureCity = getCitybyAirport(getSeRequestObj().getDepartureAirportCode());
            I18nCacheableList OriginAirportList = getI18nRetriever().getOriginAirportList(getLang());
            getSeRequestObj().setDepartureCityName(OriginAirportList.get(getSeRequestObj().getDepartureAirportCode()));
            getSeRequestObj().setDepartureCountryName(departureCity.getCountry().getName());
            City destinationCity = getCitybyAirport(getSeRequestObj().getDestinationAirportCode());
            I18nCacheableList destinationAirportList = getI18nRetriever().getDestinationAirportList(getLang());
            getSeRequestObj().setDestinationCountryName(destinationCity.getCountry().getName());
            getSeRequestObj().setDestinationCityName(destinationAirportList.get(getSeRequestObj().getDestinationAirportCode()));
            getSeRequestObj().flushRoomSetting();
            savetoSession();
            return getSeRequestObj().getType();
        }
    }

    public String getActionPageName() {
        return "/search/searchPaginate.do";
    }
}
