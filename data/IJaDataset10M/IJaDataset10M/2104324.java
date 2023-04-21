package com.acv.connector.ocean.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import com.acv.common.exception.ConnectorException;
import com.acv.common.exception.UnsuccessfulRequestException;
import com.acv.common.model.BusRequestObject;
import com.acv.common.model.BusResponseObject;
import com.acv.common.model.constants.StandardServiceMapKey;
import com.acv.common.model.bus.PricingRequest;
import com.acv.common.model.bus.impl.PricingResponseImpl;
import com.acv.common.model.entity.*;
import com.acv.common.model.entity.impl.HotelImpl;
import com.acv.common.model.entity.impl.ImmutablePassengerGroupImpl;
import com.acv.common.model.entity.impl.PassengerWithPriceTagImpl;
import com.acv.connector.ocean.exception.OceanUnsuccessfulRequestException;
import com.acv.connector.ocean.model.OceanHotelRoomServiceImpl;
import com.acv.connector.ocean.model.PriceUpdateable;
import com.acv.connector.ocean.model.conversion.OceanServiceFactory;
import com.acv.connector.ocean.price.PricingResponseData;
import com.acv.connector.ocean.price.PricingResponseDataFactory;
import com.acv.connector.ocean.price.TotalPriceTagInfo;
import com.acv.connector.ocean.selector.SelectorFactory;
import com.acv.connector.ocean.selector.ServiceSelector;
import com.acv.connector.ocean.util.OceanErrorHelper;
import com.acv.connector.ocean.util.OceanModelConverter;
import com.acv.connector.ocean.util.OceanServiceNormalizer;
import com.acv.connector.ocean.util.TravelerComparator;
import com.acv.connector.util.BookableServiceKeyPair;
import com.acv.connector.util.BookableServiceUtil;
import com.acv.dao.profiles.model.UserProfile;
import com.vacv.ocean.data.Hint;
import com.vacv.ocean.data.OceanConstants;
import com.vacv.ocean.data.PointOfService;
import com.vacv.ocean.data.SSR;
import com.vacv.ocean.data.Service;
import com.vacv.ocean.data.Traveler;
import com.vacv.ocean.protocol.OceanProtocolFactory;
import com.vacv.ocean.protocol.OceanRequest;
import com.vacv.ocean.protocol.OceanRequestFactory;
import com.vacv.ocean.protocol.OceanResponse;
import com.vacv.ocean.protocol.request.BookingRQ;
import com.vacv.ocean.protocol.response.BookingRS;

/**
 * Command to support <code>PricingRequest</code> processing for ocean.
 *
 *
 * @author pmartin
 *
 */
public class PricingRequestCommand extends AbstractOceanCommand {

    protected static final Logger log = Logger.getLogger(PricingRequestCommand.class);

    /** Map a service identifier to a BookableService (and an arbitrary reference key set by the caller) */
    protected Map<ServiceIdentifier, BookableServiceKeyPair<BookableService>> serviceLookupMap = null;

    /** Original <code>List</code> of all passengers */
    protected List<Passenger> allPassengers = null;

    /** Intermediate <code>PricingResponseData</code> object - contains extracted price information.  Generated from the BookingRS. */
    protected PricingResponseData pricingData = null;

    /** Flag to indicate whether we are processing a package or not.  Packages have no service prices. */
    protected boolean isPackage = false;

    /**
	 * Generate and set the <code>PricingResponseData</code> object based on extracted price information from the
	 * specified <code>BookingRS</code>.  This information is then more easily mapped to the original <code>BookableService</code> objects.
	 * @param bookingRs The ocean <code>BookingRS</code> object
	 */
    protected void initializePricingData(BookingRS bookingRs) {
        pricingData = PricingResponseDataFactory.create(bookingRs.getBookingData().getPaymentList(), bookingRs.getPriceData());
    }

    /**
	 * Convert the given <code>OceanResponse</code> to the appropriate <code>BusResponseObject</code> (using some information from the
	 * original <code>BusRequestObject</code> if necessary).
	 *
	 * In this command, the return value is invariably a <code>PricingResponse</code>.
	 *
	 * @param oceanResponse The <code>BookingRS</code> returned by ocean.
	 * @param requestSent The <code>PricingRequest</code> originally sent by the caller (prior to being transformed to a <code>BookingRQ</code>).
	 * @return The fully converted <code>PricingResponse</code> object.
	 * @throws UnsuccessfulRequestException Thrown if there is a functional error
	 * @throws ConnectorException Thrown if there is a system error
	 */
    @Override
    protected BusResponseObject convertToModelResponse(OceanResponse oceanResponse, BusRequestObject requestSent) throws UnsuccessfulRequestException, ConnectorException {
        OceanErrorHelper.validateResponse(oceanResponse);
        if (!(oceanResponse instanceof BookingRS)) {
            throw new ConnectorException("Ocean returned an invalid response. PriceRequestCommand can only process BookingRS responses from Ocean");
        }
        BookingRS bookingRs = (BookingRS) oceanResponse;
        initializePricingData(bookingRs);
        Set<BookableServiceKeyPair> unprocessedBookableServices = new HashSet<BookableServiceKeyPair>();
        unprocessedBookableServices.addAll(serviceLookupMap.values());
        Set<BookableServiceKeyPair> processedBookableServices = new HashSet<BookableServiceKeyPair>();
        List<Service> oceanServices = OceanServiceNormalizer.normalizePricingServices(bookingRs.getBookingData().getServiceList());
        for (Service oceanService : oceanServices) {
            List<BookableServiceKeyPair> selectedServices = null;
            ServiceSelector selector = SelectorFactory.createIdentifierSelector(bookingRs.getBookingData().getTravelerList(), oceanService);
            selectedServices = selector.select(unprocessedBookableServices);
            if (selectedServices.size() > 1) {
                throw new ConnectorException("The ocean service data matched more than one BookableService, aborting. [Echo token: " + bookingRs.getEchoToken() + ", serviceRPH: " + oceanService.getServiceRPH() + "]");
            }
            for (BookableServiceKeyPair keyPair : selectedServices) {
                BookableService bookableService = keyPair.getService();
                if (bookableService == null) continue;
                int serviceRPH = 1;
                if (!(isPackage && (bookableService instanceof OutboundFlightShellService))) serviceRPH = oceanService.getServiceRPH();
                updateServicePricing(serviceRPH, bookableService);
            }
            if (selectedServices != null) {
                processedBookableServices.addAll(selectedServices);
                unprocessedBookableServices.removeAll(selectedServices);
            }
        }
        if (unprocessedBookableServices.size() > 0) {
            log.warn("Couldn't price all bookable services!  processed: " + processedBookableServices.size() + ", unprocessed: " + unprocessedBookableServices.size());
            if (log.isDebugEnabled()) {
                for (BookableServiceKeyPair keyPair : unprocessedBookableServices) {
                    log.debug("UNMATCHED SERVICE: " + keyPair.getService().getIdentifier());
                }
            }
            throw new ConnectorException("The ocean response did not give us pricing for all requested services!");
        }
        PricingResponseImpl pricingResponse = createModelResponse();
        pricingResponse.setUserProfile(requestSent.getUserProfile());
        for (BookableServiceKeyPair serviceKeyPair : processedBookableServices) {
            pricingResponse.addService(serviceKeyPair.getKey(), serviceKeyPair.getService());
        }
        if (bookingRs.getHints() != null && bookingRs.getHints().getList().size() > 0) {
            for (BookableService service : pricingResponse.getServices().values()) {
                if (service.getType() == BookableServiceType.HOTEL_ROOM) {
                    Hotel hotel = ((HotelRoomService) service).getParentHotel();
                    if (hotel != null && (hotel instanceof HotelImpl)) {
                        for (Object hint : bookingRs.getHints().getList()) {
                            ((HotelImpl) hotel).addHint(((Hint) hint).getHint());
                        }
                    }
                }
            }
            for (Object hint : bookingRs.getHints().getList()) {
                pricingResponse.addHint(((Hint) hint).getHint());
            }
        }
        ImmutablePassengerGroupImpl passengerGroup = new ImmutablePassengerGroupImpl();
        for (Passenger passenger : allPassengers) {
            PassengerWithPriceTag passengerWithPrice = createPassengerWithPrice(passenger);
            passengerGroup.add(passengerWithPrice);
        }
        pricingResponse.setPassengers(passengerGroup);
        updateTotalPricing(pricingResponse);
        if (isPackage) {
            pricingResponse.setServicePriceBreakdown(false);
            pricingResponse.setTravelerPriceBreakdown(true);
        } else {
            pricingResponse.setServicePriceBreakdown(true);
            pricingResponse.setTravelerPriceBreakdown(true);
        }
        return pricingResponse;
    }

    /**
	 * Create a model response of the appropriate type.  Handy for subclassing (Booking is an extension of pricing, but the response
	 * objects are also subclasses)
	 * @return A <code>PricingResponseImpl</code> instance
	 */
    protected PricingResponseImpl createModelResponse() {
        return new PricingResponseImpl();
    }

    /**
	 * Update the specified <code>PricingResponseImpl</code>'s object *total* pricing information.  Pricing by service and traveler
	 * are not updated.
	 * @param pricingResponse The <code>PricingResponseImpl</code> to update
	 */
    protected void updateTotalPricing(PricingResponseImpl pricingResponse) {
        TotalPriceTagInfo totalPricing = pricingData.getTotalPricing();
        pricingResponse.setBalanceDue(totalPricing.getBalanceDue());
        pricingResponse.setCommission(totalPricing.getCommission());
        pricingResponse.setDiscount(totalPricing.getDiscount());
        pricingResponse.setDueDate(totalPricing.getDueDate());
        pricingResponse.setGrossPrice(totalPricing.getGrossPrice());
        pricingResponse.setMinimumDeposit(totalPricing.getMinimumDeposit());
        pricingResponse.setTaxes(totalPricing.getTaxes());
        pricingResponse.setCommissionable(totalPricing.isCommissionable());
    }

    /**
	 * Convert the given <code>BusRequestObject</code> to the appropriate <code>OceanRequest</code>
	 *
	 * In this command, the return value is invariably a <code>BookingRQ</code>.
	 *
	 * @param busObjRequest The <code>PricingRequest</code> to process
	 * @return The <code>BookingRQ</code> needed to generate the appropriate <code>BookingRS</code>
	 * @throws UnsuccessfulRequestException Thrown if there is a functional error
	 * @throws ConnectorException Thrown if there is a system error
	 */
    @Override
    protected OceanRequest convertToOceanRequest(BusRequestObject busObjRequest) throws ConnectorException, UnsuccessfulRequestException {
        PricingRequest pricingRequest = (PricingRequest) busObjRequest;
        serviceLookupMap = BookableServiceUtil.generateIdentifierToKeyMap(pricingRequest);
        allPassengers = pricingRequest.getPassengers().getPassengers();
        BookingRQ bookingRQ = OceanRequestFactory.createBookingRQ();
        initializeRequest(bookingRQ, pricingRequest.getUserProfile());
        bookingRQ.setActionCode(BookingRQ.ACTION_BOOK_QUERY);
        UserProfile userProfile = pricingRequest.getUserProfile();
        if (userProfile == null || userProfile.getAudience() == null) {
            throw new ConnectorException("Invalid UserProfile");
        }
        bookingRQ.setDistributionChannel(userProfile.getAudience().getDistributionChannel());
        PointOfService pos = OceanProtocolFactory.createPointOfService();
        pos.setAgencyKey(userProfile.getAudience().getAgencyKey());
        bookingRQ.setPos(pos);
        List<Traveler> travelersWithSeatRequests = new ArrayList<Traveler>();
        StringBuffer bookingRemarks = new StringBuffer();
        if (pricingRequest != null && pricingRequest.getPassengers() != null) {
            List<Traveler> travelerList = new ArrayList<Traveler>();
            ImmutablePassengerGroup passengerGroup = pricingRequest.getPassengers();
            for (Passenger passenger : passengerGroup.getPassengers()) {
                Traveler traveler = OceanModelConverter.convertPassenger(passenger);
                travelerList.add(traveler);
            }
            Collections.sort(travelerList, new TravelerComparator());
            bookingRQ.setTravelerList(travelerList);
            bookingRQ.setNumberOfTravelers(travelerList.size());
        }
        List<SSR> ssrList = new ArrayList<SSR>();
        OceanHotelRoomServiceImpl lastHotelRoomService = null;
        List<Service> oceanServices = new LinkedList<Service>();
        Set<String> keys = pricingRequest.getKeys();
        for (String key : keys) {
            BookableService service = pricingRequest.getService(key);
            if (service instanceof OceanHotelRoomServiceImpl) {
                lastHotelRoomService = (OceanHotelRoomServiceImpl) service;
            }
            oceanServices.add(OceanServiceFactory.create(service));
        }
        if (lastHotelRoomService != null && lastHotelRoomService.getPackageInformation() != null) {
            OutboundFlightShellService outboudFlight = (OutboundFlightShellService) pricingRequest.getService(StandardServiceMapKey.FLIGHTS_OUTBOUND.getKey());
            ReturnFlightShellService returnFlight = (ReturnFlightShellService) pricingRequest.getService(StandardServiceMapKey.FLIGHTS_RETURN.getKey());
            oceanServices.add(0, OceanServiceFactory.createPackageService(pricingRequest.getPassengers(), lastHotelRoomService, outboudFlight, returnFlight));
            isPackage = true;
        } else {
            isPackage = false;
        }
        int serviceRPH = 0;
        for (Service service : oceanServices) {
            service.setServiceRPH(++serviceRPH);
            if (OceanConstants.SERVICE_TYPE_FLIGHT.equals(service.getServiceType())) {
                ssrList.addAll(OceanModelConverter.createSeatRequestSSR(serviceRPH, travelersWithSeatRequests));
            }
            bookingRQ.addService(service);
        }
        bookingRQ.setRemarks(bookingRemarks.toString());
        return bookingRQ;
    }

    /**
	 * Create a new "priced" passenger from the request's passenger object.  The passenger's index value will be used
	 * to determing the traveler RPH
	 * @param passenger The original <code>Passenger</code> object
	 * @return A new <code>PassengerWithPriceTag</code> object containing the pricing information specified by the
	 * ocean backend
	 */
    protected PassengerWithPriceTag createPassengerWithPrice(Passenger passenger) {
        PassengerWithPriceTagImpl passengerWithPrice = new PassengerWithPriceTagImpl(passenger);
        PriceTag priceTag = pricingData.getTravelerPricing(passenger.getIndex());
        passengerWithPrice.setCommissionable(priceTag.isCommissionable());
        passengerWithPrice.setGrossPrice(priceTag.getGrossPrice());
        passengerWithPrice.setTaxes(priceTag.getTaxes());
        return passengerWithPrice;
    }

    /**
	 * Update the given <code>BookableService</code> with the pricing information returned by ocean at the given serviceRPH.
	 *
	 * @param serviceRPH The serviceRPH mapped to the ocean pricing data
	 * @param service The <code>BookableService</code> to update
	 * @throws OceanUnsuccessfulRequestException If there is a serious problem with the pricing.
	 */
    protected void updateServicePricing(int serviceRPH, BookableService service) throws OceanUnsuccessfulRequestException, ConnectorException {
        if (pricingData == null) {
            throw new ConnectorException("The ocean response did not contain any pricing information!");
        }
        if (!(service instanceof PriceUpdateable)) {
            throw new ConnectorException("The service price is not updateable!");
        }
        PriceUpdateable originalService = (PriceUpdateable) service;
        PriceTag servicePriceTag = pricingData.getServicePricing(serviceRPH);
        originalService.setCommissionable(servicePriceTag.isCommissionable());
        originalService.setGrossPrice(servicePriceTag.getGrossPrice());
        originalService.setTaxes(servicePriceTag.getTaxes());
        ImmutablePassengerGroup passengerGroup = service.getPassengerGroup();
        List<PassengerWithPriceTag> originalPassengers = passengerGroup.getPassengerPrices();
        for (PassengerWithPriceTag passenger : originalPassengers) {
            PriceTag travelerPriceTag = pricingData.getServicePricingByTraveler(serviceRPH, passenger.getIndex());
            PassengerWithPriceTagImpl paxPrice = (PassengerWithPriceTagImpl) passenger;
            paxPrice.setCommissionable(travelerPriceTag.isCommissionable());
            paxPrice.setGrossPrice(travelerPriceTag.getGrossPrice());
            paxPrice.setTaxes(travelerPriceTag.getTaxes());
        }
    }

    /**
	 * Returns whether or not the specified <code>BusRequestObject</code> is compatible with this command.
	 *
	 * @param obj The <code>BusRequestObject</code> to check for compatibility
	 * @return <code>true</code> if the specified object is of type <code>PricingRequest</code>, <code>false</code> otherwise.
	 */
    @Override
    protected boolean isCompatible(BusRequestObject obj) {
        if (obj instanceof PricingRequest) {
            return true;
        }
        return false;
    }
}
