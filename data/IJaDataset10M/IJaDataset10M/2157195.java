package com.acv.webapp.action.bus.search.utils;

import java.util.List;
import java.util.Locale;
import com.acv.common.exception.SessionExpiredException;
import com.acv.common.model.bus.PricingResponse;
import com.acv.common.model.bus.SearchRequest;
import com.acv.common.model.bus.SearchResponse;
import com.acv.common.model.display.AdaptedBusResponseObject;
import com.acv.common.model.display.CarAdapter;
import com.acv.common.model.display.HotelRoomAdapter;
import com.acv.common.model.display.OutboundFlightAdapter;
import com.acv.common.model.display.ReturnFlightAdapter;
import com.acv.common.model.display.TicketsAdapter;
import com.acv.common.model.display.TravelInsuranceAdapter;
import com.acv.common.model.display.sorting.AdaptorList;
import com.acv.dao.common.model.City;
import com.acv.service.bus.busToCms.BusToCmsAdapter;
import com.acv.webapp.action.bus.search.SearchAction;
import com.acv.webapp.action.bus.search.SearchRequestObject;
import com.acv.webapp.action.bus.search.alternate.AlternativeSearchResponse;

/**
 * Search Helper - helper methods for search process
 *
 */
public abstract class SearchHelper {

    private SearchAction searchAction;

    public SearchHelper(SearchAction searchAction) {
        this.searchAction = searchAction;
    }

    protected void setOutFlightsList(AdaptorList<OutboundFlightAdapter> outFlightsList) {
        getSearchAction().setOutFlightsList(outFlightsList);
    }

    protected void setReturnFlightsList(AdaptorList<ReturnFlightAdapter> returnFlightsList) {
        getSearchAction().setReturnFlightsList(returnFlightsList);
    }

    protected List GetPaginatedList(List source) {
        return getSearchAction().GetPaginatedList(source);
    }

    protected List<CarServices> getCarServiceList() {
        return getSearchAction().getCarServiceList();
    }

    protected List getSourceList() throws SessionExpiredException {
        return getSearchAction().getSourceList();
    }

    protected Locale getLocale() {
        return getSearchAction().getLocale();
    }

    protected AdaptorList<ReturnFlightAdapter> getReturnFlightsList() {
        return getSearchAction().getReturnFlightsList();
    }

    protected AdaptorList<OutboundFlightAdapter> getOutFlightsList() {
        return getSearchAction().getOutFlightsList();
    }

    protected String getText(String key, String args[]) {
        return getSearchAction().getText(key, args);
    }

    protected String getText(String aTextName) {
        return getSearchAction().getText(aTextName);
    }

    protected void addAlternativeString(String alternativeString) {
        getSearchAction().addAlternativeString(alternativeString);
    }

    protected void setAlternativeString(List<String> alternativeString) {
        getSearchAction().setAlternativeString(alternativeString);
    }

    protected SearchRequestObject getSeRequestObj() {
        return getSearchAction().getSeRequestObj();
    }

    protected AdaptorList<HotelRoomAdapter> getCheapestHotelRoomServices() {
        return getSearchAction().getCheapestHotelRoomServices();
    }

    public SearchAction getSearchAction() {
        return searchAction;
    }

    protected City getCitybyAirport(String code) {
        return getSearchAction().getCitybyAirport(code);
    }

    protected List<AlternativeSearchResponse> getAlternativeSearchResponses() {
        return getSearchAction().getAlternativeSearchResponses();
    }

    protected void setCheapestOutFlights(OutboundFlightAdapter outboundFlightAdapter) {
        getSearchAction().setCheapestOutFlights(outboundFlightAdapter);
    }

    protected void setCheapestReturnFlights(ReturnFlightAdapter returnFlightAdapter) {
        getSearchAction().setCheapestReturnFlights(returnFlightAdapter);
    }

    protected AdaptedBusResponseObject getAResponseObject() {
        return getSearchAction().getAResponseObject();
    }

    protected SearchRequest getSuccessfulAlternativeRequest() {
        return getSearchAction().getSuccessfulAlternativeRequest();
    }

    protected SearchResponse getSResponse() {
        return getSearchAction().getSResponse();
    }

    protected OutboundFlightAdapter getCheapestOutFlights() {
        return getSearchAction().getCheapestOutFlights();
    }

    protected ReturnFlightAdapter getCheapestReturnFlights() {
        return getSearchAction().getCheapestReturnFlights();
    }

    protected BusToCmsAdapter getBusToCmsAdapter() {
        return getSearchAction().getBusToCmsAdapter();
    }

    protected void setAResponseObject(AdaptedBusResponseObject aResponseObject) {
        getSearchAction().setAResponseObject(aResponseObject);
    }

    protected OutboundFlightAdapter getSelectedOutFlights() {
        return getSearchAction().getSelectedOutFlights();
    }

    protected void setInsuranceServiceList(AdaptorList<TravelInsuranceAdapter> insuranceServiceList) {
        getSearchAction().setInsuranceServiceList(insuranceServiceList);
    }

    protected AdaptorList<TravelInsuranceAdapter> getInsuranceServiceList() {
        return getSearchAction().getInsuranceServiceList();
    }

    protected void setCarServiceList(List<CarServices> carServiceList) {
        getSearchAction().setCarServiceList(carServiceList);
    }

    protected void setSelectedCars(AdaptorList<CarAdapter> selectedCars) {
        getSearchAction().setSelectedCars(selectedCars);
    }

    protected AdaptorList<CarAdapter> getSelectedCars() {
        return getSearchAction().getSelectedCars();
    }

    protected String getSelectedType() {
        return getSearchAction().getSelectedType();
    }

    protected String getReturnFlightsIdentifier() {
        return getSearchAction().getReturnFlightsIdentifier();
    }

    protected void setSelectedReturnFlights(ReturnFlightAdapter returnFlightAdapter) {
        getSearchAction().setSelectedReturnFlights(returnFlightAdapter);
    }

    protected void setSelectedType(String selectedType, boolean updateCurrentSelectedType) {
        getSearchAction().setSelectedType(selectedType, updateCurrentSelectedType);
    }

    protected void setActualpage(int actualpage) {
        getSearchAction().setActualpage(actualpage);
    }

    protected String getOutFlightsIdentifier() {
        return getSearchAction().getOutFlightsIdentifier();
    }

    protected void setSelectedOutFlights(OutboundFlightAdapter outboundFlightAdapter) {
        getSearchAction().setSelectedOutFlights(outboundFlightAdapter);
    }

    protected SearchRequest getSearchRequest() {
        return getSearchAction().getSearchRequest();
    }

    protected void setCheapestHotelRoomServices(AdaptorList<HotelRoomAdapter> hotelRoomServices) {
        getSearchAction().setCheapestHotelRoomServices(hotelRoomServices);
    }

    protected String getSortedType() {
        return getSearchAction().getSortedType();
    }

    protected void setSortedType(String sortedType) {
        getSearchAction().setSortedType(sortedType);
    }

    protected void setSortedOrder(String sortedOrder) {
        getSearchAction().setSortedOrder(sortedOrder);
    }

    protected List<HotelRoomServices> getHotelRoomList() {
        return getSearchAction().getHotelRoomList();
    }

    protected void setHotelRoomList(List<HotelRoomServices> hotelRoomList) {
        getSearchAction().setHotelRoomList(hotelRoomList);
    }

    protected ReturnFlightAdapter getSelectedReturnFlights() {
        return getSearchAction().getSelectedReturnFlights();
    }

    protected List<HotelRoomAdapter> getSelectedHotelRooms() {
        return getSearchAction().getSelectedHotelRooms();
    }

    protected void setSelectedInsurance(TravelInsuranceAdapter selectedInsurance) {
        getSearchAction().setSelectedInsurance(selectedInsurance);
    }

    protected void setSelectedTicket(TicketsAdapter selectedTicket) {
        getSearchAction().setSelectedTicket(selectedTicket);
    }

    protected List<HotelRoomServices> getRoomsServiceOfHotel() {
        return getSearchAction().getRoomsServiceOfHotel();
    }

    protected PricingResponse getPricingResponse() {
        return getSearchAction().getPricingResponse();
    }

    protected TravelInsuranceAdapter getSelectedInsurance() {
        return getSearchAction().getSelectedInsurance();
    }

    protected TicketsAdapter getSelectedTicket() {
        return getSearchAction().getSelectedTicket();
    }

    protected void setBeforePricing(boolean beforePricing) {
        getSearchAction().setBeforePricing(beforePricing);
    }

    protected void setSelectedHotelRooms(AdaptorList<HotelRoomAdapter> selectedHotelRooms) {
        getSearchAction().setSelectedHotelRooms(selectedHotelRooms);
    }

    public AdaptorList<TicketsAdapter> getTicketServiceList() {
        return getSearchAction().getTicketServiceList();
    }

    public void setTicketServiceList(AdaptorList<TicketsAdapter> ticketServiceList) {
        getSearchAction().setTicketServiceList(ticketServiceList);
    }

    public String getCurrentSelectedType() {
        return getSearchAction().getCurrentSelectedType();
    }
}
