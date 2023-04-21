package com.acv.webapp.action.bus.search;

import org.apache.log4j.Logger;
import com.acv.common.exception.SessionExpiredException;
import com.acv.dao.common.model.City;
import com.acv.service.i18n.model.I18nCacheableList;
import com.acv.webapp.action.bus.search.utils.SearchConstants;

public class LastMinuteSearchResultAction extends SearchCustomVacationAction {

    private static final long serialVersionUID = 1L;

    private static final Logger log = Logger.getLogger(LastMinuteSearchResultAction.class);

    /**
	 * to get packageSearch result page
	 *
	 * @return String SUCCESS
	 */
    public String result() {
        if (getSeRequestObj() == null) {
            log.debug("seRequest is null. return input");
            return INPUT;
        } else {
            City departureCity = getCitybyAirport(getSeRequestObj().getDepartureAirportCode());
            I18nCacheableList OriginCityList = this.getI18nRetriever().getOriginCityList(getLang());
            getSeRequestObj().setDepartureCityName(OriginCityList.get(departureCity.getId()));
            getSeRequestObj().setDepartureCountryName(departureCity.getCountry().getName());
            City destinationCity = getCitybyAirport(getSeRequestObj().getDestinationAirportCode());
            I18nCacheableList destinationCityList = this.getI18nRetriever().getDestinationCityList(getLang());
            getSeRequestObj().setDestinationCountryName(destinationCity.getCountry().getName());
            getSeRequestObj().setDestinationCityName(destinationCityList.get(destinationCity.getId()));
            getSeRequestObj().flushRoomSetting();
            savetoSession();
            return getSeRequestObj().getType();
        }
    }

    public String getActionPageName() {
        return "/search/searchPaginate.do";
    }

    public String resultHotel() throws SessionExpiredException {
        if (getSeRequestObj().getSearchType().equals(SearchConstants.PACKAGE)) {
            super.resultPackage();
            return "package";
        } else {
            super.resultCustom();
            return "customVacation";
        }
    }
}
