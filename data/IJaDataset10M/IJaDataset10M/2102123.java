package org.flightreservationsserver.externalinterface;

import java.util.ArrayList;
import org.flightreservationsserver.model.Customer;
import org.flightreservationsserver.model.Flight;
import org.flightreservationsserver.model.FlightReservation;
import org.flightreservationsserver.model.ReservationAgency;
import org.flightreservationsserverinterface.dtos.CustomerDTO;
import org.flightreservationsserverinterface.dtos.FlightDTO;
import org.flightreservationsserverinterface.dtos.FlightReservationDTO;
import org.flightreservationsserverinterface.dtos.ReservationAgencyDTO;

/**
 * @author Facundo Manuel Quiroga
 * Feb 1, 2009
 * 
 */
public class POJOToDTO {

    public static FlightDTO flightToFlightDTO(Flight flight) {
        return new FlightDTO(flight.getId(), flight.getDate(), flight.getSource(), flight.getDestination(), flight.getMaximumSeats(), flight.getOccupiedSeats(), flight.isFull());
    }

    public static ReservationAgencyDTO reservationAgencyToReservationAgencyDTO(ReservationAgency reservationAgency) {
        return new ReservationAgencyDTO(reservationAgency.getId(), reservationAgency.getName(), reservationAgency.getPassword());
    }

    public static CustomerDTO customerToCustomerDTO(Customer customer) {
        return new CustomerDTO(customer.getId(), customer.getName(), customer.getDni(), customer.getCreditCardNumber());
    }

    public static FlightReservationDTO flightReservationToFlightReservationDTO(FlightReservation flightReservation) {
        FlightDTO flightDTO = flightToFlightDTO(flightReservation.getFlight());
        ReservationAgencyDTO reservationAgencyDTO = reservationAgencyToReservationAgencyDTO(flightReservation.getReservationAgency());
        CustomerDTO customerDTO = customerToCustomerDTO(flightReservation.getCustomer());
        return new FlightReservationDTO(flightReservation.getId(), flightDTO, customerDTO, reservationAgencyDTO, flightReservation.isPaid(), flightReservation.isCancelled(), flightReservation.getPassengerName());
    }

    public static ArrayList<CustomerDTO> customersToCustomerDTOs(ArrayList<Customer> customers) {
        ArrayList<CustomerDTO> customerDTOs = new ArrayList<CustomerDTO>();
        for (Customer customer : customers) {
            customerDTOs.add(customerToCustomerDTO(customer));
        }
        return customerDTOs;
    }

    public static ArrayList<FlightDTO> flightsToFlightDTOs(ArrayList<Flight> flights) {
        ArrayList<FlightDTO> flightDTOs = new ArrayList<FlightDTO>();
        for (Flight flight : flights) {
            flightDTOs.add(flightToFlightDTO(flight));
        }
        return flightDTOs;
    }

    /**
	 * @param flightReservationsBy
	 * @return
	 */
    public static ArrayList<FlightReservationDTO> flightReservationsToFlightReservationDTOs(ArrayList<FlightReservation> flightReservations) {
        ArrayList<FlightReservationDTO> flightReservationsDTOs = new ArrayList<FlightReservationDTO>();
        for (FlightReservation flightReservation : flightReservations) {
            flightReservationsDTOs.add(flightReservationToFlightReservationDTO(flightReservation));
        }
        return flightReservationsDTOs;
    }
}
