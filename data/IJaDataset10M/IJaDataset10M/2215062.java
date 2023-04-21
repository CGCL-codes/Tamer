package com.acv.connector.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.acv.common.model.constants.PassengerType;
import com.acv.common.model.entity.*;
import com.acv.common.model.entity.impl.AbstractFlightShellService;
import com.acv.common.model.selector.BookableServiceSelector;
import com.acv.connector.ocean.model.OceanOutboundFlightShellServiceImpl;
import com.acv.connector.ocean.model.OceanReturnFlightShellServiceImpl;

public class TestDisplayHelper {

    public static String summarizePassengerGroup(PassengerGroup roomGroup) {
        StringBuffer roomGroupSummary = new StringBuffer();
        roomGroupSummary.append("[ adults: ");
        roomGroupSummary.append(roomGroup.getNbOfType(PassengerType.ADULT));
        roomGroupSummary.append(", children: ");
        roomGroupSummary.append(roomGroup.getNbOfType(PassengerType.CHILD));
        roomGroupSummary.append(", infants: ");
        roomGroupSummary.append(roomGroup.getNbOfType(PassengerType.INFANT));
        roomGroupSummary.append(", seniors: ");
        roomGroupSummary.append(roomGroup.getNbOfType(PassengerType.SENIOR));
        roomGroupSummary.append("]");
        return roomGroupSummary.toString();
    }

    public static void displayInsurance(BookableService insurance) {
        if (insurance != null) {
            List<BookableService> insurances = new ArrayList<BookableService>();
            insurances.add(insurance);
            displayInsurances(insurances);
        }
    }

    public static void displayInsurances(List<BookableService> insurances) {
        if (insurances == null) {
            return;
        }
        for (BookableService service : insurances) {
            InsuranceService insurance = (InsuranceService) service;
            StringBuffer summary = new StringBuffer();
            appendSummaryPrefix(summary, insurance);
            summary.append(insurance.getCode());
            summary.append(", ").append(insurance.getDescription());
            summary.append(", ").append(insurance.getStartDate()).append(" to ").append(insurance.getEndDate());
            appendSummaryPriceInfo(summary, insurance);
            System.out.println(summary.toString());
        }
    }

    public static void displayHotels(BookableService hotel) {
        List<BookableService> hotels = new ArrayList<BookableService>();
        hotels.add(hotel);
        displayHotels(hotels);
    }

    public static void displayHotels(List<BookableService> hotels) {
        if (hotels == null) {
            return;
        }
        for (BookableService service : hotels) {
            HotelRoomService hotelRoom = (HotelRoomService) service;
            Hotel hotel = hotelRoom.getParentHotel();
            SimpleDateFormat sdf = new SimpleDateFormat();
            StringBuffer summary = new StringBuffer();
            appendSummaryPrefix(summary, hotelRoom);
            summary.append(hotel.getDestinationCode()).append(hotel.getCode()).append(hotelRoom.getCode()).append(", ");
            summary.append(hotelRoom.getDescription()).append(", ");
            summary.append(hotelRoom.getMealCode()).append(", ");
            summary.append(sdf.format(hotel.getCheckInDate())).append(" to ").append(sdf.format(hotel.getCheckOutDate())).append(", Hints:" + hotel.getHints());
            appendSummaryPriceInfo(summary, hotelRoom);
            System.out.println(summary.toString());
        }
    }

    public static void displayFlights(BookableService flight) {
        List<BookableService> flights = new ArrayList<BookableService>();
        flights.add(flight);
        displayFlights(flights);
    }

    public static void displayFlights(List<BookableService> flights) {
        if (flights == null) {
            return;
        }
        for (BookableService service : flights) {
            AbstractFlightShellService flight = (AbstractFlightShellService) service;
            StringBuffer summary = new StringBuffer();
            appendSummaryPrefix(summary, flight);
            summary.append("CombID:").append(flight.getCombinationId()).append(" ");
            if (flight instanceof OceanOutboundFlightShellServiceImpl) {
                OceanOutboundFlightShellServiceImpl oceanFlight = (OceanOutboundFlightShellServiceImpl) flight;
                summary.append("compatId:");
                for (ReturnFlightShellService returnflight : oceanFlight.getCompatibleFlightShells()) summary.append(((OceanReturnFlightShellServiceImpl) returnflight).getCombinationId()).append(" ");
            } else if (flight instanceof OceanReturnFlightShellServiceImpl) {
                OceanReturnFlightShellServiceImpl oceanFlight = (OceanReturnFlightShellServiceImpl) flight;
                summary.append("compatId:");
                for (OutboundFlightShellService outboundflight : oceanFlight.getCompatibleFlightShells()) summary.append(((OceanOutboundFlightShellServiceImpl) outboundflight).getCombinationId()).append(" ");
            }
            List<FlightSegment> segments = flight.getSegments();
            FlightSegment lastSegment = null;
            for (FlightSegment segment : segments) {
                if (lastSegment == null) {
                    SimpleDateFormat sdf = new SimpleDateFormat();
                    summary.append(sdf.format(segment.getDepartureDate())).append(" ");
                }
                lastSegment = segment;
                summary.append(segment.getOriginCode()).append("[").append(segment.getFlightNumber()).append("]").append("->");
            }
            summary.append(lastSegment.getDestinationCode());
            appendSummaryPriceInfo(summary, flight);
            System.out.println(summary.toString());
        }
    }

    public static void appendSummaryPrefix(StringBuffer summary, BookableService service) {
        summary.append(service.getSourceEngine()).append("[").append(service.getIdentifier()).append("]: ");
    }

    public static void appendSummaryPriceInfo(StringBuffer summary, BookableService service) {
        summary.append(", price: ");
        if (service.getGrossPrice() != null) {
            summary.append(service.getGrossPrice().getAmount());
        }
        summary.append(", taxes: ");
        if (service.getTaxes() != null) {
            summary.append(service.getTaxes().getTaxTotal());
        } else {
            summary.append("null");
        }
        summary.append(", comm: ");
        summary.append(service.isCommissionable());
    }

    public static void displaySelections(Map<String, BookableServiceSelector> selection) {
        Set<String> keys = selection.keySet();
        for (String key : keys) {
            BookableServiceSelector selector = selection.get(key);
            BookableService service = selector.getService();
            List<BookableService> serviceListWrapper = new ArrayList<BookableService>();
            serviceListWrapper.add(service);
            System.out.println("key: " + key);
            displayServices(service.getType(), serviceListWrapper);
        }
    }

    public static void displayServices(BookableServiceType type, List<BookableService> services) {
        switch(type) {
            case OUTBOUND_FLIGHT:
            case RETURN_FLIGHT:
            case OPENJAW_FLIGHT:
                displayFlights(services);
                break;
            case HOTEL_ROOM:
                displayHotels(services);
                break;
            case INSURANCE:
                displayInsurances(services);
                break;
            case TICKET:
                displayTickets(services);
                break;
            case CAR:
                displayCars(services);
                break;
        }
    }

    private static void displayCars(List<BookableService> cars) {
        if (cars == null) {
            return;
        }
        for (BookableService service : cars) {
            CarService car = (CarService) service;
            StringBuffer summary = new StringBuffer();
            appendSummaryPrefix(summary, car);
            summary.append(car.getCode());
            summary.append(car.getCategoryCode());
            summary.append(", ").append(car.getDescription()).append(", ");
            appendSummaryPriceInfo(summary, car);
            System.out.println(summary.toString());
        }
    }

    private static void displayTickets(List<BookableService> tickets) {
        if (tickets == null) {
            return;
        }
        for (BookableService service : tickets) {
            TicketService ticket = (TicketService) service;
            StringBuffer summary = new StringBuffer();
            appendSummaryPrefix(summary, ticket);
            summary.append(ticket.getCode()).append(".");
            summary.append(ticket.getCategoryCode());
            summary.append(", ").append(ticket.getDescription()).append(", ");
            appendSummaryPriceInfo(summary, ticket);
            System.out.println(summary.toString());
        }
    }

    public static void displayService(BookableServiceType type, BookableService service) {
        if (service == null) {
            return;
        }
        List<BookableService> services = new ArrayList<BookableService>();
        services.add(service);
        displayServices(type, services);
    }
}
