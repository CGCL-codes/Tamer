package com.acv.connector.ocean.command;

import java.util.LinkedList;
import java.util.List;
import com.acv.common.exception.ConnectorException;
import com.acv.common.exception.UnsuccessfulRequestException;
import com.acv.common.model.BusRequestObject;
import com.acv.common.model.BusResponseObject;
import com.acv.common.model.bus.SearchRequest;
import com.acv.common.model.bus.SearchResponse;
import com.acv.common.model.bus.BusErrorType;
import com.acv.common.model.criterion.impl.*;
import com.acv.common.model.entity.Passenger;
import com.acv.common.util.SearchCriterionUtil;
import com.acv.connector.ocean.exception.OceanUnsuccessfulSearchRequestException;
import com.acv.connector.ocean.model.OceanErrorType;
import com.acv.connector.ocean.command.request.CriterionOceanRequest;
import com.acv.dao.catalog.locations.airports.model.Airport;
import com.vacv.ocean.data.CRSFlightInfo;
import com.vacv.ocean.data.OceanConstants;
import com.vacv.ocean.data.ServiceSpecGroup;
import com.vacv.ocean.data.TravelerAgeList;
import com.vacv.ocean.protocol.OceanProtocolFactory;
import com.vacv.ocean.protocol.OceanRequest;
import com.vacv.ocean.protocol.OceanRequestFactory;
import com.vacv.ocean.protocol.OceanResponse;
import com.vacv.ocean.protocol.request.AvailabilityRQ;
import com.vacv.ocean.OceanLogger;

/**
 * Flexible search request command that handles all OPEN-JAW criterions and
 * any added components and hotels.
 */
public class SearchRequestCommand extends RoundTripSearchRequestCommand {

    private static List<String> notAllowedHotelOnlyDestinations = new LinkedList<String>();

    static {
        notAllowedHotelOnlyDestinations.add("LAS");
    }

    List<SearchCriterionUtil.SearchCriterionAndKey> flights = null;

    protected BusResponseObject doRequest(BusRequestObject busObjectRequest) throws ConnectorException, UnsuccessfulRequestException {
        try {
            hotels = SearchCriterionUtil.grabCriteriaByClass((SearchRequest) busObjectRequest, HotelCriterion.class);
            flights = SearchCriterionUtil.grabCriteriaByClass((SearchRequest) busObjectRequest, OpenJawFlightCriterion.class);
            cars = SearchCriterionUtil.grabCriteriaByClass((SearchRequest) busObjectRequest, CarCriterion.class);
            options = SearchCriterionUtil.grabCriteriaByClass((SearchRequest) busObjectRequest, TicketCriterion.class);
            insurances = SearchCriterionUtil.grabCriteriaByClass((SearchRequest) busObjectRequest, InsuranceCriterion.class);
            if (flights.isEmpty() && cars.isEmpty()) {
                for (SearchCriterionUtil.SearchCriterionAndKey criterion : hotels) {
                    if (notAllowedHotelOnlyDestinations.contains(((HotelCriterion) criterion.getCriterion()).getDestinationCode())) {
                        OceanUnsuccessfulSearchRequestException e = new OceanUnsuccessfulSearchRequestException(OceanErrorType.HOTEL_NOT_SELLABLE, criterion);
                        e.setAlternativeEnabled(false);
                        throw e;
                    }
                }
            }
            SearchResponse out = null;
            String brochureCode = null;
            if (flights != null) {
                for (SearchCriterionUtil.SearchCriterionAndKey criterion : flights) {
                    OceanRequest request = null;
                    OceanResponse response = null;
                    try {
                        request = convertToOceanOpenJawFlightRequest(criterion, busObjectRequest);
                        response = sendRequest(request);
                        SearchResponse flightResponse = convertFlightToModelResponse(response, busObjectRequest);
                        validateMandatoryCriterion(criterion, flightResponse, OceanErrorType.ACRSFLIGHT_NO_MATCH_FLIGHT_FILTER);
                        if (out == null) out = flightResponse; else out.appendResponse(flightResponse);
                        if (brochureCode == null) {
                            brochureCode = peekBrochureCode(flightResponse);
                        }
                    } catch (ConnectorException e) {
                        throw e;
                    } catch (UnsuccessfulRequestException e) {
                        if (exceptionCritical(criterion, e)) {
                            throw new OceanUnsuccessfulSearchRequestException(OceanErrorType.ACRSFLIGHT_NO_MATCH_FLIGHT_FILTER, "The mandatory criterion mapped to the key " + criterion.getKey() + " yielded no results", criterion);
                        }
                    }
                }
            }
            if (hotels != null && !hotels.isEmpty()) {
                OceanRequest request = null;
                OceanResponse response = null;
                try {
                    UnsuccessfulRequestException lastException = null;
                    int hotelRequestFailures = 0;
                    List<CriterionOceanRequest> oceanHotelRequests = convertToOceanHotelRequests(hotels, busObjectRequest, null, null);
                    for (CriterionOceanRequest hotelRequest : oceanHotelRequests) {
                        try {
                            response = sendRequest(hotelRequest.getRequest());
                            SearchResponse hotelResponse = convertHotelToModelResponse(response, busObjectRequest, null, null);
                            if (out == null) {
                                out = hotelResponse;
                            } else {
                                out.appendResponse(hotelResponse);
                            }
                            if (brochureCode == null) {
                                brochureCode = peekBrochureCode(hotelResponse);
                            }
                        } catch (UnsuccessfulRequestException e) {
                            if (e instanceof OceanUnsuccessfulSearchRequestException && e.getBusError() == BusErrorType.GENERIC_NOT_AVAILABLE) {
                                ((OceanUnsuccessfulSearchRequestException) e).setFailingCriterion(hotelRequest.getOriginalCriterion());
                                e.setBusError(BusErrorType.HOTEL_NOT_AVAILABLE);
                                e.setErrorMessage("The mandatory criterion mapped to the key " + hotelRequest.getOriginalCriterion().getKey() + " yielded no results");
                            }
                            hotelRequestFailures++;
                            lastException = e;
                        }
                    }
                    if (hotelRequestFailures == oceanHotelRequests.size()) {
                        throw lastException;
                    }
                } catch (ConnectorException e) {
                    throw e;
                }
                try {
                    for (SearchCriterionUtil.SearchCriterionAndKey criterion : hotels) {
                        validateMandatoryCriterion(criterion, out, OceanErrorType.HOTEL_VAKANZ_NOT_FOUND);
                    }
                } catch (UnsuccessfulRequestException e) {
                    if (exceptionCritical(hotels.get(0), e)) {
                        throw new OceanUnsuccessfulSearchRequestException(OceanErrorType.HOTEL_VAKANZ_NOT_FOUND, "The mandatory criterion mapped to the key " + hotels.get(0).getKey() + " yielded no results", hotels.get(0));
                    }
                }
            }
            if (cars != null && !cars.isEmpty()) {
                SearchResponse carResponse = processCarRequest(busObjectRequest, null, null);
                if (brochureCode == null) {
                    brochureCode = peekBrochureCode(carResponse);
                }
                if (out == null) out = carResponse; else out.appendResponse(carResponse);
            }
            if (options != null) {
                for (SearchCriterionUtil.SearchCriterionAndKey criterion : options) {
                    OceanRequest request = null;
                    OceanResponse response = null;
                    try {
                        request = convertToOceanOptionRequest(criterion, busObjectRequest, null, null);
                        response = sendRequest(request);
                        SearchResponse ticketResponse = convertOptionToModelResponse(response, busObjectRequest, criterion, null, null);
                        validateMandatoryCriterion(criterion, ticketResponse, OceanErrorType.TICKET_VAKANZ_NOT_FOUND);
                        if (out == null) out = ticketResponse; else out.appendResponse(ticketResponse);
                        if (brochureCode == null) {
                            brochureCode = peekBrochureCode(ticketResponse);
                        }
                    } catch (ConnectorException e) {
                        throw e;
                    } catch (UnsuccessfulRequestException e) {
                        if (exceptionCritical(criterion, e)) {
                            throw new OceanUnsuccessfulSearchRequestException(OceanErrorType.TICKET_VAKANZ_NOT_FOUND, "The mandatory criterion mapped to the key " + criterion.getKey() + " yielded no results", criterion);
                        }
                    }
                }
            }
            if (insurances != null && !insurances.isEmpty()) {
                for (SearchCriterionUtil.SearchCriterionAndKey criterion : insurances) {
                    if (brochureCode == null) {
                        throw new OceanUnsuccessfulSearchRequestException(OceanErrorType.INSURANCE_VAKANZ_NOT_FOUND, "Cannot independently search for insurances, must be paired with another criterion.", criterion);
                    }
                    try {
                        SearchResponse optionResponse = convertInsuranceToModelResponse(busObjectRequest, criterion, brochureCode, null, null);
                        validateMandatoryCriterion(criterion, optionResponse, OceanErrorType.INSURANCE_VAKANZ_NOT_FOUND);
                        out.appendResponse(optionResponse);
                    } catch (UnsuccessfulRequestException e) {
                        if (exceptionCritical(criterion, e)) {
                            throw new OceanUnsuccessfulSearchRequestException(OceanErrorType.INSURANCE_VAKANZ_NOT_FOUND, "The mandatory criterion mapped to the key " + criterion.getKey() + " yielded no results", criterion);
                        }
                    }
                }
            }
            return out;
        } catch (ConnectorException e) {
            OceanLogger.log.debug("ConnectorException while sending request: " + e.getMessage());
            throw e;
        } catch (UnsuccessfulRequestException e) {
            OceanLogger.log.debug("UnsuccessfulRequestException[" + (e.getBusError() != null ? e.getBusError() : "-") + ":" + (e.getErrorCode() != null ? e.getErrorCode() : "-") + "] while sending request: " + e.getMessage());
            throw e;
        }
    }

    protected OceanRequest convertToOceanOpenJawFlightRequest(SearchCriterionUtil.SearchCriterionAndKey openJawCriterion, BusRequestObject busObjRequest) throws ConnectorException {
        SearchRequest request = (SearchRequest) busObjRequest;
        AvailabilityRQ availabilityRQ = OceanRequestFactory.createAvailabilityRQ();
        initializeRequest(availabilityRQ, request.getUserProfile());
        String originCode = ((OpenJawFlightCriterion) openJawCriterion.getCriterion()).getOriginCode();
        String destinationCode = ((OpenJawFlightCriterion) openJawCriterion.getCriterion()).getDestinationCode();
        Airport destinationAirport = getAirport(destinationCode);
        if (destinationAirport == null) throw new ConnectorException("There are no Airport with the code of the desired destination: '" + destinationCode + "'");
        availabilityRQ.setActionCode(AvailabilityRQ.ACTION_CRSFLIGHT);
        ServiceSpecGroup specs = OceanProtocolFactory.createServiceSpecGroup(destinationAirport.getBrochureCode());
        specs.setServiceCategory("%");
        specs.setServiceCategoryDescription("%");
        specs.setServiceType(OceanConstants.SERVICE_TYPE_FLIGHT);
        specs.setServiceCode(originCode + "%");
        CRSFlightInfo info = new CRSFlightInfo();
        info.setDestination(destinationCode);
        info.setOrigin(originCode);
        info.setOutwardReturn(OceanConstants.CRS_FLIGHT_INFO_DIRECTION_OUTWARD);
        info.setRequestedCRS(OceanConstants.DEFAULT_REQUESTED_CRS);
        availabilityRQ.setFlightInfo(info);
        availabilityRQ.setService(specs);
        availabilityRQ.setFromDate(((OpenJawFlightCriterion) openJawCriterion.getCriterion()).getFlightDate());
        availabilityRQ.setEndDate(((OpenJawFlightCriterion) openJawCriterion.getCriterion()).getFlightDate());
        TravelerAgeList travelerAgeList = OceanProtocolFactory.createTravelerAgeList();
        int nbChildren = 0;
        int nbInfants = 0;
        List ageList = new LinkedList();
        for (Passenger pax : request.getPassengers()) {
            ageList.add(pax.getAge());
            switch(pax.getType()) {
                case CHILD:
                    nbChildren++;
                    break;
                case INFANT:
                    nbInfants++;
                    break;
            }
        }
        travelerAgeList.setList(ageList);
        availabilityRQ.setTravelerNb(request.getPassengers().size());
        availabilityRQ.setChildrenNb(nbChildren);
        availabilityRQ.setInfantsNb(nbInfants);
        return availabilityRQ;
    }
}
