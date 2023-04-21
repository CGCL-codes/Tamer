package com.acv.webapp.action.bus.search;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;
import com.acv.common.exception.SessionExpiredException;
import com.acv.common.model.BusRequestObject;
import com.acv.common.model.bus.SearchRequest;
import com.acv.common.model.bus.SearchResponse;
import com.acv.common.model.bus.factory.SearchRequestFactory;
import com.acv.common.model.display.CarAdapter;
import com.acv.common.model.display.HotelRoomAdapter;
import com.acv.common.model.display.OutboundFlightAdapter;
import com.acv.common.model.display.ReturnFlightAdapter;
import com.acv.common.model.display.sorting.AdaptorList;
import com.acv.common.model.entity.PassengerGroup;
import com.acv.webapp.action.bus.booking.ChooseOptionAction;
import com.acv.webapp.action.bus.search.alternate.AlternativeSearchType;
import com.acv.webapp.action.bus.search.utils.HotelRoomServices;
import com.acv.webapp.action.bus.search.utils.SearchConstants;

/**
 * Action class to customVacation search
 *              to include chooseOption steps.
 * @author      Minxia Han
 * @version     %I%, %G%
 * @since       1.0
 */
public class SearchCustomVacationAction extends ChooseOptionAction {

    private static final long serialVersionUID = 1L;

    private static final Logger log = Logger.getLogger(SearchCustomVacationAction.class);

    private Integer engineFlag = -1;

    public Integer getSearchType() {
        if (getSeRequestObj() != null) return getSeRequestObj().getSearchType();
        return null;
    }

    public void setSearchType(Integer searchType) {
        if (getSeRequestObj() != null) getSeRequestObj().setSearchType(searchType);
    }

    public Integer getEngineFlag() {
        return engineFlag;
    }

    public void setEngineFlag(Integer engineFlag) {
        this.engineFlag = engineFlag;
    }

    /**
	 * To get the data sourse of types of Cars
	 *
	 * @return List<String>
	 */
    public List<String> getTypesOfCar() {
        List<String> typesCar = new ArrayList<String>();
        typesCar.add(getText("search.economyCar"));
        typesCar.add(getText("search.familyVan"));
        typesCar.add(getText("search.luxuryCar"));
        return typesCar;
    }

    public float getPackageTax() {
        return getSearchGetter().getPackageTax();
    }

    /************* i think that we don't use this method (Minxia)*/
    public String start() throws SessionExpiredException {
        initObj();
        if (getSeRequestObj().getType() != null) {
            if (!SearchConstants.SEARCH_TYPE_CUSTOMVACATION.equals(getSeRequestObj().getType().toUpperCase())) {
                deleteAllfromSession();
            }
        }
        initSearchResponse();
        getSelector();
        getOptions();
        if (log.isDebugEnabled()) log.debug("search Custom vacation start, return success");
        return SUCCESS;
    }

    /**
	 *
	 * to get customVacation Search result page from ocean
	 *
	 * @return String SUCCESS or INPUT
	 * @throws SessionExpiredException : exception of session expired
	 */
    public String resultCustom() throws SessionExpiredException {
        this.setActualpage(0);
        List<PassengerGroup> rooms = new LinkedList<PassengerGroup>();
        this.initObject();
        if (getSeRequestObj() == null || getSeRequestObj().getDestinationAirportCode() == null) {
            if (log.isDebugEnabled()) log.debug("seRequest is null. return input");
            return INPUT;
        }
        if (getSeRequestObj().getAdultsNum() + getSeRequestObj().getSeniorsNum() > 9) {
            if (log.isDebugEnabled()) log.debug("trsanfer to group Booking");
            return "groupBooking";
        }
        rooms = super.initPassengers(getSeRequestObj());
        PassengerGroup passengers = SearchRequestFactory.createPassengerGroup();
        for (PassengerGroup pg : rooms) {
            passengers.append(pg);
        }
        List<PassengerGroup> cars = new ArrayList<PassengerGroup>();
        cars.add(passengers);
        SearchRequestObject seRequestObj = getSeRequestObj();
        switch(seRequestObj.getSearchType()) {
            case SearchConstants.FLIGHT_ONLY:
                setSearchRequest(SearchRequestFactory.createRoundTripFlightSearchRequest(getUserProfileManager().getCurrentUser(), seRequestObj.getDepartureAirportCode(), seRequestObj.getDestinationAirportCode(), seRequestObj.getDepartureDate(), seRequestObj.getReturnDate(), passengers));
                break;
            case SearchConstants.FLIGHT_HOTEL:
                if (seRequestObj.getHotelCode() == null || "".equals(seRequestObj.getHotelCode())) setSearchRequest(SearchRequestFactory.createFlightHotelSearchRequest(getUserProfileManager().getCurrentUser(), seRequestObj.getDepartureAirportCode(), seRequestObj.getDestinationAirportCode(), seRequestObj.getDepartureDate(), seRequestObj.getReturnDate(), rooms)); else setSearchRequest(SearchRequestFactory.createFlightHotelSearchRequest(getUserProfileManager().getCurrentUser(), seRequestObj.getDepartureAirportCode(), seRequestObj.getDestinationAirportCode(), seRequestObj.getDepartureDate(), seRequestObj.getReturnDate(), rooms, seRequestObj.getHotelCode()));
                break;
            case SearchConstants.FLIGHT_CAR:
                setSearchRequest(SearchRequestFactory.createFlightAndCarSearchRequest(getUserProfileManager().getCurrentUser(), seRequestObj.getDepartureAirportCode(), seRequestObj.getDestinationAirportCode(), seRequestObj.getDepartureDate(), seRequestObj.getReturnDate(), cars));
                break;
            case SearchConstants.FLIGHT_HOTEL_CAR:
                if (seRequestObj.getHotelCode() == null || "".equals(seRequestObj.getHotelCode())) setSearchRequest(SearchRequestFactory.createFlightHotelCarSearchRequest(getUserProfileManager().getCurrentUser(), seRequestObj.getDepartureAirportCode(), seRequestObj.getDestinationAirportCode(), seRequestObj.getDepartureDate(), seRequestObj.getReturnDate(), rooms, cars)); else setSearchRequest(SearchRequestFactory.createFlightHotelCarSearchRequest(getUserProfileManager().getCurrentUser(), seRequestObj.getDepartureAirportCode(), seRequestObj.getDestinationAirportCode(), seRequestObj.getDepartureDate(), seRequestObj.getReturnDate(), rooms, cars, seRequestObj.getHotelCode()));
                break;
            case SearchConstants.HOTEL_ONLY:
                if (seRequestObj.getHotelCode() == null || "".equals(seRequestObj.getHotelCode())) setSearchRequest(SearchRequestFactory.createHotelSearchRequest(getUserProfileManager().getCurrentUser(), seRequestObj.getDestinationAirportCode(), seRequestObj.getDepartureDate(), seRequestObj.getReturnDate(), rooms, null)); else setSearchRequest(SearchRequestFactory.createHotelSearchRequest(getUserProfileManager().getCurrentUser(), seRequestObj.getDestinationAirportCode(), seRequestObj.getDepartureDate(), seRequestObj.getReturnDate(), rooms, seRequestObj.getHotelCode()));
                break;
            case SearchConstants.HOTEL_CAR:
                if (seRequestObj.getHotelCode() == null || "".equals(seRequestObj.getHotelCode())) setSearchRequest(SearchRequestFactory.createHotelAndCarSearchRequest(getUserProfileManager().getCurrentUser(), seRequestObj.getDestinationAirportCode(), seRequestObj.getDepartureDate(), seRequestObj.getReturnDate(), rooms, cars, null)); else setSearchRequest(SearchRequestFactory.createHotelAndCarSearchRequest(getUserProfileManager().getCurrentUser(), seRequestObj.getDestinationAirportCode(), seRequestObj.getDepartureDate(), seRequestObj.getReturnDate(), rooms, cars, seRequestObj.getHotelCode()));
                break;
            case SearchConstants.CAR_ONLY:
                setSearchRequest(SearchRequestFactory.createCarSearchRequest(getUserProfileManager().getCurrentUser(), seRequestObj.getDestinationAirportCode(), seRequestObj.getDepartureDate(), seRequestObj.getDepartureDate(), cars));
            default:
                break;
        }
        SearchRequest searchRequest = getSearchRequest();
        deleteAllfromSession();
        setSearchRequest(searchRequest);
        getSearchRequest().addMetadata(BusRequestObject.SEARCH_REQUEST_TYPE_METADATA_KEY, BusRequestObject.CUSTOM_PACKAGE_SEARCH);
        setSResponse(super.doSearch(seRequestObj, getSearchRequest()));
        SearchResponse sResponse = getSResponse();
        if (log.isDebugEnabled()) log.debug("seRequestObj.getSearchType:" + seRequestObj.getSearchType());
        if (sResponse != null) {
            String str = null;
            if (log.isDebugEnabled()) log.debug("sResponse is not null in customVacation Search");
            for (String key : sResponse.getKeys()) {
                str = "searchResponse key:" + key + "response size:" + sResponse.getServices(key).size();
                if (log.isDebugEnabled()) log.debug("searchResponse key:" + key + "response size:" + sResponse.getServices(key).size());
            }
        }
        if (sResponse != null) switch(seRequestObj.getSearchType()) {
            case SearchConstants.FLIGHT_ONLY:
            case SearchConstants.FLIGHT_HOTEL:
            case SearchConstants.FLIGHT_CAR:
            case SearchConstants.FLIGHT_HOTEL_CAR:
                getReturnFlightses();
                getOutFlightses();
                setSelectedType(Integer.toBinaryString(SearchConstants.TAB_DEPARTUREFLIGHT), true);
                break;
            case SearchConstants.HOTEL_ONLY:
            case SearchConstants.HOTEL_CAR:
                getHotelList();
                if (log.isDebugEnabled()) log.debug("---------size:" + getCheapestHotelRoomServices().size());
                setSelectedType(Integer.toString(SearchConstants.TAB_HOTEL), true);
                break;
            case SearchConstants.CAR_ONLY:
            default:
                try {
                    initSelectedACVPackage();
                } catch (SessionExpiredException e) {
                    e.printStackTrace();
                }
                setSelectedType(Integer.toString(SearchConstants.TAB_OPTIONS), true);
                break;
        }
        if (sResponse != null) {
            super.savetoSession();
            if (log.isDebugEnabled()) log.debug("seRequest is not null. return success.");
        }
        if (getErrorMessage() != null) {
            super.addActionError(getErrorMessage());
            return INPUT;
        }
        alternativeProcess();
        if (getAlternativeString() != null && getAlternativeString().size() > 1) {
            if (log.isDebugEnabled()) log.debug("alternativeString size:" + getAlternativeString().size());
            if (!super.type.name().equalsIgnoreCase(AlternativeSearchType.ALTERNATIVE_ATTEMPTED.name())) {
                if (log.isDebugEnabled()) log.debug("alternative ------------search;");
                return "alternative";
            }
            return INPUT;
        }
        return SUCCESS;
    }

    /**
	 * to search Car result from Ocean
	 * to work with CustomVacationOption select car.
	 * @return INPUT or SUCCESS
	 *
	 */
    public String searchCar() throws SessionExpiredException {
        SearchResponse seResponseCars = null;
        this.setActualpage(0);
        List<PassengerGroup> rooms = new LinkedList<PassengerGroup>();
        this.initObject();
        if (getSeRequestObj() == null) {
            if (log.isDebugEnabled()) log.debug("seRequest is null. return input");
            return INPUT;
        }
        rooms = super.initPassengers(getSeRequestObj());
        PassengerGroup passengers = SearchRequestFactory.createPassengerGroup();
        for (PassengerGroup pg : rooms) {
            passengers.append(pg);
        }
        List<PassengerGroup> cars = new ArrayList<PassengerGroup>();
        cars.add(passengers);
        setSearchRequest(SearchRequestFactory.createCarSearchRequest(getUserProfileManager().getCurrentUser(), getSeRequestObj().getDestinationAirportCode(), getSeRequestObj().getDepartureDate(), getSeRequestObj().getDepartureDate(), cars));
        getSearchRequest().addMetadata(BusRequestObject.SEARCH_REQUEST_TYPE_METADATA_KEY, BusRequestObject.CUSTOM_PACKAGE_SEARCH);
        seResponseCars = super.doSearch(getSeRequestObj(), getSearchRequest());
        if (log.isDebugEnabled()) log.debug("seRequestObj.getSearchType:" + getSeRequestObj().getSearchType());
        initSearchResponse();
        getSelector();
        if (getErrorMessage() != null || seResponseCars == null) {
            if (getErrorMessage() == null) {
                setErrorMessage(getText("searchResponse.noresults"));
            }
            addActionError(getErrorMessage());
            setSelectedType(Integer.toString(SearchConstants.TAB_OPTIONS), true);
            return INPUT;
        } else {
            if (log.isDebugEnabled()) for (String serviceKey : seResponseCars.getKeys()) {
                log.debug("car only search result,serviceKey" + serviceKey);
                log.debug("service size:" + seResponseCars.getServices(serviceKey).size());
            }
            if (getSResponse() != null) {
                getSResponse().appendResponse(seResponseCars);
                setAResponseObject(getBusToCmsAdapter().adaptBusResponseObject(getSResponse()));
            }
        }
        if (getSResponse() != null) {
            Integer searchType = getSeRequestObj().getSearchType();
            switch(searchType) {
                case SearchConstants.FLIGHT_ONLY:
                    getSeRequestObj().setSearchType(SearchConstants.FLIGHT_CAR);
                    break;
                case SearchConstants.FLIGHT_HOTEL:
                    getSeRequestObj().setSearchType(SearchConstants.FLIGHT_HOTEL_CAR);
                    break;
                case SearchConstants.HOTEL_ONLY:
                    getSeRequestObj().setSearchType(SearchConstants.HOTEL_CAR);
                    break;
            }
            setSelectedType(Integer.toString(SearchConstants.TAB_CAR), true);
            super.getCars();
            super.savetoSession();
            if (log.isDebugEnabled()) log.debug("seRequest is not null. return success.");
        }
        return SUCCESS;
    }

    /**
	 * to change result list from one to another (departureFlight,returnFlight,Hotel,Cars)
	 *
	 * @return String SUCCESS
	 * @throws SessionExpiredException :exception of session expired
	 */
    public String change() throws SessionExpiredException {
        initObject();
        initSearchResponse();
        this.getSelector();
        switch(Integer.parseInt(getSelectedType())) {
            case SearchConstants.TAB_CAR:
                getCars();
                break;
            case SearchConstants.TAB_DEPARTUREFLIGHT:
                getOutFlightses();
                break;
            case SearchConstants.TAB_RETURNFLIGHT:
                getReturnFlightses();
                break;
            case SearchConstants.TAB_HOTEL:
                getHotelList();
                break;
            case SearchConstants.TAB_OPTIONS:
                getOptions();
                break;
            default:
                break;
        }
        return SUCCESS;
    }

    /**
	 *
	 * to set items selected from departureflight, returnFlight,hotel and Cars
	 * to change to display next page
	 * @return String SUCCESS
	 * @throws SessionExpiredException : exception of session expired
	 */
    public String select() throws SessionExpiredException {
        HotelRoomAdapter hotel = null;
        if (getSeRequestObj() == null) {
            initObject();
        }
        if (getSResponse() == null) initSearchResponse();
        if (log.isDebugEnabled()) log.debug("selectedtype." + getSelectedType());
        getSelector();
        int tabIndex = Integer.parseInt(getSelectedType());
        if (getSourceList() != null && getSourceList().size() > 0) {
            Object so = getSourceList().get(0);
            if (so instanceof CarAdapter && SearchConstants.TAB_CAR != tabIndex) {
                tabIndex = SearchConstants.TAB_CAR;
            } else if (so instanceof OutboundFlightAdapter && SearchConstants.TAB_DEPARTUREFLIGHT != tabIndex) {
                tabIndex = SearchConstants.TAB_DEPARTUREFLIGHT;
            } else if (so instanceof ReturnFlightAdapter && SearchConstants.TAB_RETURNFLIGHT != tabIndex) {
                tabIndex = SearchConstants.TAB_RETURNFLIGHT;
            } else if (so instanceof HotelRoomAdapter && SearchConstants.TAB_HOTEL != tabIndex) {
                tabIndex = SearchConstants.TAB_HOTEL;
            }
        }
        switch(tabIndex) {
            case SearchConstants.TAB_CAR:
                if (getCarServiceList() != null && getCarServiceList().get(0) != null) {
                    getCarServiceList().get(0).setCarServices((AdaptorList<CarAdapter>) this.getSourceList());
                    for (CarAdapter hr : getCarServiceList().get(0).getCarServices()) {
                        if (getCarIdentifier() != null && getCarIdentifier().equals(hr.getBookableService().getIdentifier().toString())) if (getSelectedCars() != null) getSelectedCars().set(0, hr); else {
                            setSelectedCars(new AdaptorList<CarAdapter>());
                            getSelectedCars().add(hr);
                        }
                    }
                }
                break;
            case SearchConstants.TAB_DEPARTUREFLIGHT:
                setOutFlightsList((AdaptorList<OutboundFlightAdapter>) getSourceList());
                for (OutboundFlightAdapter ls : getOutFlightsList()) {
                    if (getOutFlightsIdentifier() != null && getOutFlightsIdentifier().equals(ls.getBookableService().getIdentifier().toString())) {
                        setSelectedOutFlights(ls);
                        break;
                    }
                }
                break;
            case SearchConstants.TAB_RETURNFLIGHT:
                setReturnFlightsList((AdaptorList<ReturnFlightAdapter>) getSourceList());
                for (ReturnFlightAdapter ls : getReturnFlightsList()) {
                    if (getReturnFlightsIdentifier() != null && getReturnFlightsIdentifier().equals(ls.getBookableService().getIdentifier().toString())) {
                        setSelectedReturnFlights(ls);
                        break;
                    }
                }
                break;
            case SearchConstants.TAB_HOTEL:
                setCheapestHotelRoomServices((AdaptorList<HotelRoomAdapter>) getSourceList());
                for (HotelRoomAdapter hr : getCheapestHotelRoomServices()) {
                    if (getHotelRoomIdentifier() != null && getHotelRoomIdentifier().equals(hr.getBookableService().getIdentifier().toString())) {
                        hotel = hr;
                    }
                }
                setRoomsServiceOfHotel(new ArrayList<HotelRoomServices>());
                setSelectedHotelRooms(new AdaptorList<HotelRoomAdapter>());
                AdaptorList<HotelRoomAdapter> hotelRooms = new AdaptorList<HotelRoomAdapter>();
                if (getHotelRoomList() == null) super.getHotelList();
                for (HotelRoomServices hrs : getHotelRoomList()) {
                    if (log.isDebugEnabled()) log.debug("------------5------this.hotelRoomIdentifier:" + getHotelRoomIdentifier());
                    if (hrs != null) {
                        if (log.isDebugEnabled()) log.debug("hotelServices:" + hrs.getHotelRoomServices().size());
                        if (hotel != null && hotel.getParentHotel() != null) {
                            HotelRoomAdapter ht = getCheapestHotelRoom(hrs.getHotelRoomServices(), hotel.getParentHotel().getCode());
                            if (ht != null) {
                                getSelectedHotelRooms().add(ht);
                                if (log.isDebugEnabled()) log.debug("selectedHotelRooms is ok");
                            }
                        }
                    } else {
                        if (log.isDebugEnabled()) log.debug("hrs is null error");
                    }
                }
                for (HotelRoomServices ls : getRoomsServiceOfHotel()) {
                    hotelRooms.addAll(ls.getHotelRoomServices());
                }
                for (HotelRoomServices ls : getRoomsServiceOfHotel()) {
                    for (HotelRoomAdapter hra : ls.getHotelRoomServices()) {
                        hra.getParentHotel().setRooms(hotelRooms);
                    }
                }
                break;
            default:
                break;
        }
        if (SearchConstants.TAB_DEPARTUREFLIGHT == tabIndex) switch(getSeRequestObj().getSearchType()) {
            case SearchConstants.FLIGHT_ONLY:
            case SearchConstants.FLIGHT_HOTEL:
            case SearchConstants.FLIGHT_CAR:
            case SearchConstants.FLIGHT_HOTEL_CAR:
                super.getReturnFlightses();
                setSelectedType(Integer.toString(SearchConstants.TAB_RETURNFLIGHT), true);
                break;
        } else if (SearchConstants.TAB_RETURNFLIGHT == tabIndex) switch(getSeRequestObj().getSearchType()) {
            case SearchConstants.FLIGHT_ONLY:
                getOptions();
                setSelectedType(Integer.toString(SearchConstants.TAB_OPTIONS), true);
                break;
            case SearchConstants.FLIGHT_HOTEL:
            case SearchConstants.FLIGHT_HOTEL_CAR:
                getHotelList();
                setSelectedType(Integer.toString(SearchConstants.TAB_HOTEL), true);
                break;
            case SearchConstants.FLIGHT_CAR:
                getCars();
                setSelectedType(Integer.toString(SearchConstants.TAB_CAR), true);
                break;
        } else if (SearchConstants.TAB_HOTEL == tabIndex) switch(getSeRequestObj().getSearchType()) {
            case SearchConstants.FLIGHT_HOTEL:
            case SearchConstants.HOTEL_ONLY:
                getOptions();
                setSelectedType(Integer.toString(SearchConstants.TAB_OPTIONS), true);
                break;
            case SearchConstants.FLIGHT_HOTEL_CAR:
            case SearchConstants.HOTEL_CAR:
                getCars();
                setSelectedType(Integer.toString(SearchConstants.TAB_CAR), true);
                break;
        } else if (SearchConstants.TAB_CAR == tabIndex) switch(getSeRequestObj().getSearchType()) {
            case SearchConstants.HOTEL_CAR:
            case SearchConstants.FLIGHT_CAR:
            case SearchConstants.FLIGHT_HOTEL_CAR:
            case SearchConstants.CAR_ONLY:
                getOptions();
                setSelectedType(Integer.toString(SearchConstants.TAB_OPTIONS), true);
                break;
        }
        savetoSession();
        return SUCCESS;
    }
}
