package com.acv.connector.ocean.identifier.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import com.acv.common.model.constants.Engine;
import com.acv.common.model.entity.BookableService;
import com.acv.common.model.entity.FlightSegment;
import com.acv.common.model.entity.ServiceIdentifier;
import com.acv.connector.ocean.identifier.InitializationException;
import com.acv.connector.ocean.identifier.ServiceIdentifierGenerator;
import com.acv.connector.ocean.identifier.TravelerProfile;
import com.acv.connector.ocean.model.OceanFlightShellServiceImpl;
import com.acv.util.DateUtil;
import com.vacv.ocean.data.BookingClass;
import com.vacv.ocean.data.CRSTransportSegment;
import com.vacv.ocean.data.OceanConstants;
import com.vacv.ocean.data.Service;

/**
 * <code>ServiceIdentifierGenerator</code> to create a <code>ServiceIdentifer</code>
 * for flight services.
 *
 * @author pmartin
 *
 */
public class FlightServiceIdentifierGenerator implements ServiceIdentifierGenerator {

    public static final String DATE_FORMAT = "yyyy-MM-dd@HHmm";

    protected class SegmentInfo {

        protected String origin;

        protected String destination;

        protected Date departureDate;

        protected Date arrivalDate;

        protected String carrier;

        protected String flightNumber;

        protected String bookingClass;

        protected boolean isMainSegment = false;
    }

    protected String brochureCode;

    protected String flightShellServiceCode;

    protected List<SegmentInfo> segmentInfos = new LinkedList<SegmentInfo>();

    protected TravelerProfile travelerProfile;

    /**
	 * Initialize the generator for a later call of the <code>generate()</code> method.
	 *
	 * WARNING:  The oceanService specified in the parameter must have EXACTLY ONE booking class associated to it.  It is the caller's responsibility to make sure
	 * that all services have at least one, and no more than one booking class.
	 *
	 * @param travelerProfile The <code>TravelerProfile</code> representing the group of passengers
	 * @param oceanService The <code>com.vacv.ocean.data.Service</code> instance returned by Ocean.
	 * @throws InitializationException if there is a problem in initializing the generator (missing fields, etc)
	 */
    public void initialize(TravelerProfile travelerProfile, Service oceanService) throws InitializationException {
        if (!OceanConstants.SERVICE_TYPE_FLIGHT.equals(oceanService.getServiceType())) {
            throw new IllegalArgumentException("FlightServiceKeyGenerator can only be used to generate keys for ocean flight services");
        }
        brochureCode = oceanService.getBrochureCode();
        flightShellServiceCode = oceanService.getServiceCode();
        if (oceanService.getCrsTransportDetails() != null && oceanService.getCrsTransportDetails().getTransportSegmentList().size() > 0) {
            List<CRSTransportSegment> segments = oceanService.getCrsTransportDetails().getTransportSegmentList();
            CRSTransportSegment firstSegment = null;
            boolean mainSegmentFound = false;
            for (CRSTransportSegment segment : segments) {
                if (firstSegment == null) {
                    firstSegment = segment;
                }
                SegmentInfo segmentInfo = new SegmentInfo();
                segmentInfo.origin = segment.getDeparturePoint();
                segmentInfo.destination = segment.getArrivalPoint();
                segmentInfo.departureDate = DateUtil.mergeDateAndTime(segment.getDepartureDate(), segment.getDepartureTime());
                segmentInfo.arrivalDate = DateUtil.mergeDateAndTime(segment.getArrivalDate(), segment.getArrivalTime());
                segmentInfo.carrier = segment.getMarketingCarrier();
                segmentInfo.flightNumber = segment.getFlightNumber();
                if (segment.getBookingClassAvailability() == null || segment.getBookingClassAvailability().size() != 1) {
                    throw new InitializationException("Ambiguous booking class");
                }
                segmentInfo.bookingClass = ((BookingClass) segment.getBookingClassAvailability().get(0)).getExtClassCode();
                if (Boolean.parseBoolean(segment.getMainSegment()) == true) {
                    segmentInfo.isMainSegment = true;
                    mainSegmentFound = true;
                }
                segmentInfos.add(segmentInfo);
            }
            if (!mainSegmentFound) {
                throw new InitializationException("No main segment found!");
            }
        } else {
            throw new InitializationException("No flight details or flight segments, cannot process!");
        }
        this.travelerProfile = travelerProfile;
    }

    /**
	 * Generate the <code>ServiceIdentifier</code> given the parameters set in the initialization.
	 * @return The <code>ServiceIdentifier</code> object that can uniquely identify the initialized data.
	 */
    public ServiceIdentifier generate() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        StringBuffer keyBuf = new StringBuffer();
        keyBuf.append(Engine.OCEAN).append(".FLIGHT.");
        keyBuf.append(brochureCode).append("&");
        keyBuf.append(flightShellServiceCode).append("&");
        for (SegmentInfo segment : segmentInfos) {
            keyBuf.append("[SEGMENT").append((segment.isMainSegment ? "-MAIN." : "."));
            keyBuf.append(segment.origin).append("->").append(segment.destination).append(".");
            keyBuf.append(segment.carrier).append(segment.flightNumber).append(".");
            keyBuf.append(segment.bookingClass).append(".");
            keyBuf.append(sdf.format(segment.departureDate)).append("to");
            keyBuf.append(sdf.format(segment.arrivalDate)).append("]");
        }
        keyBuf.append("&");
        keyBuf.append((travelerProfile == null ? "" : travelerProfile.toString()));
        return new ServiceIdentifier(keyBuf.toString());
    }

    /**
	 * Initialize the generator for a later call of the <code>generate()</code> method.
	 *
	 * @param travelerProfile The <code>TravelerProfile</code> representing the group of passengers
	 * @param bookableService The <code>BookableService</code> instance
	 * @throws InitializationException if there is a problem in initializing the generator (missing fields, etc)
	 */
    public void initialize(TravelerProfile travelerProfile, BookableService bookableService) throws InitializationException {
        if (!(bookableService instanceof OceanFlightShellServiceImpl)) {
            throw new IllegalArgumentException("FlightServiceKeyGenerator can only be used to generate keys for ocean flight services");
        }
        OceanFlightShellServiceImpl flightService = (OceanFlightShellServiceImpl) bookableService;
        brochureCode = flightService.getBrochureCode();
        List<FlightSegment> segments = flightService.getSegments();
        FlightSegment firstSegment = null;
        FlightSegment lastSegment = null;
        for (FlightSegment segment : segments) {
            if (firstSegment == null) {
                firstSegment = segment;
            }
            lastSegment = segment;
            SegmentInfo segmentInfo = new SegmentInfo();
            segmentInfo.origin = segment.getOriginCode();
            segmentInfo.destination = segment.getDestinationCode();
            segmentInfo.departureDate = segment.getDepartureDate();
            segmentInfo.arrivalDate = segment.getArrivalDate();
            segmentInfo.carrier = segment.getCarrier();
            segmentInfo.flightNumber = segment.getFlightNumber();
            segmentInfo.bookingClass = segment.getBookingClassCode();
            segmentInfo.isMainSegment = segment.isMain();
            segmentInfos.add(segmentInfo);
        }
        flightShellServiceCode = firstSegment.getOriginCode() + lastSegment.getDestinationCode();
        this.travelerProfile = travelerProfile;
    }
}
