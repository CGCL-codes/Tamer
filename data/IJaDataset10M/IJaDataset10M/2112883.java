package org.flightreservationsserver.externalinterface;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import org.crappydbms.transactions.TransactionAbortedException;
import org.flightreservationsserver.database.NotFoundException;
import org.flightreservationsserver.database.TransactionManager;
import org.flightreservationsserver.model.Customer;
import org.flightreservationsserver.model.Flight;
import org.flightreservationsserver.model.FlightReservation;
import org.flightreservationsserver.model.FlightReservationsSystem;
import org.flightreservationsserver.model.ReservationAgency;
import org.flightreservationsserver.model.exceptions.FlightFullException;
import org.flightreservationsserver.model.exceptions.FlightReservationAlreadyCanceledException;
import org.flightreservationsserverinterface.FlightReservationsInterface;
import org.flightreservationsserverinterface.dtos.CustomerDTO;
import org.flightreservationsserverinterface.dtos.FlightDTO;
import org.flightreservationsserverinterface.dtos.FlightReservationDTO;
import org.flightreservationsserverinterface.dtos.ReservationAgencyDTO;
import org.flightreservationsserverinterface.exceptions.ErrorInServerException;
import org.flightreservationsserverinterface.exceptions.FlightReservationAlreadyPaidException;
import org.flightreservationsserverinterface.exceptions.InvalidReservationAgencyException;

public abstract class FlightReservationsServer extends UnicastRemoteObject implements FlightReservationsInterface, Serializable {

    protected transient FlightReservationsSystem flightReservationsSystem;

    public FlightReservationsServer(FlightReservationsSystem flightReservationsSystem) throws RemoteException {
        super();
        this.setFlightReservationsSystem(flightReservationsSystem);
    }

    @Override
    public void addCustomer(int dni, String name, int creditCardNumber) throws ErrorInServerException {
        try {
            TransactionManager.startTransaction();
            this.getFlightReservationsSystem().addCustomer(dni, name, creditCardNumber);
            TransactionManager.commitTransaction();
        } catch (TransactionAbortedException e) {
            TransactionManager.clearTransaction();
            this.synchronizedErrPrint("Transaction aborted in addCustomer");
            throw new ErrorInServerException();
        } catch (Exception e) {
            TransactionManager.abortTransaction();
            this.synchronizedErrPrint("Unknown error in addCustomer");
            throw new ErrorInServerException("Unknown Error");
        } finally {
            TransactionManager.clearTransaction();
        }
    }

    @Override
    public void bookPassenger(FlightDTO flightDTO, CustomerDTO customerDTO, ReservationAgencyDTO reservationAgencyDTO, String passengerName) throws ErrorInServerException, org.flightreservationsserverinterface.exceptions.NotFoundException, org.flightreservationsserverinterface.exceptions.FlightFullException {
        try {
            TransactionManager.startTransaction();
            Flight flight = this.getFlightFromSystem(flightDTO.getId());
            Customer customer = this.getCustomerFromSystem(customerDTO.getId());
            ReservationAgency reservationAgency = this.getReservationAgencyFromSystem(reservationAgencyDTO.getId());
            this.getFlightReservationsSystem().bookPassenger(flight, customer, reservationAgency, passengerName);
            TransactionManager.commitTransaction();
        } catch (TransactionAbortedException e) {
            TransactionManager.clearTransaction();
            this.synchronizedErrPrint("Transaction aborted in bookPassenger");
            throw new ErrorInServerException("Aborted Transaction");
        } catch (NotFoundException e) {
            TransactionManager.abortTransaction();
            throw new org.flightreservationsserverinterface.exceptions.NotFoundException();
        } catch (FlightFullException e) {
            TransactionManager.abortTransaction();
            throw new org.flightreservationsserverinterface.exceptions.FlightFullException();
        } catch (InvalidReservationAgencyException e) {
            TransactionManager.abortTransaction();
            this.synchronizedErrPrint("InvalidReservationAgencyException in cancelReservation");
            throw e;
        } catch (Exception e) {
            TransactionManager.clearTransaction();
            this.synchronizedErrPrint("Unknown error in bookPassenger");
            throw new ErrorInServerException("Unknown Error");
        } finally {
            TransactionManager.clearTransaction();
        }
    }

    public void reservationPaid(FlightReservationDTO flightReservationDTO) throws FlightReservationAlreadyPaidException, ErrorInServerException, org.flightreservationsserverinterface.exceptions.NotFoundException, org.flightreservationsserverinterface.exceptions.FlightReservationAlreadyCanceledException {
        try {
            TransactionManager.startTransaction();
            FlightReservation flightReservation = this.getFlightReservationFromSystem(flightReservationDTO.getId());
            this.getFlightReservationsSystem().reservationPaid(flightReservation);
            TransactionManager.commitTransaction();
        } catch (TransactionAbortedException e) {
            TransactionManager.clearTransaction();
            this.synchronizedErrPrint("Transaction aborted in reservationPaid");
            throw new ErrorInServerException();
        } catch (FlightReservationAlreadyPaidException e) {
            TransactionManager.abortTransaction();
            throw new FlightReservationAlreadyPaidException();
        } catch (NotFoundException e) {
            TransactionManager.abortTransaction();
            throw new org.flightreservationsserverinterface.exceptions.NotFoundException();
        } catch (FlightReservationAlreadyCanceledException e) {
            TransactionManager.abortTransaction();
            throw new org.flightreservationsserverinterface.exceptions.FlightReservationAlreadyCanceledException();
        } catch (InvalidReservationAgencyException e) {
            TransactionManager.abortTransaction();
            this.synchronizedErrPrint("InvalidReservationAgencyException in cancelReservation");
            throw e;
        } catch (Exception e) {
            TransactionManager.abortTransaction();
            this.synchronizedErrPrint("Unknown error in reservationPaid");
            throw new ErrorInServerException("Unknown Error");
        } finally {
            TransactionManager.clearTransaction();
        }
    }

    @Override
    public void cancelReservation(FlightReservationDTO flightReservationDTO) throws org.flightreservationsserverinterface.exceptions.FlightReservationAlreadyCanceledException, org.flightreservationsserverinterface.exceptions.NotFoundException, ErrorInServerException {
        try {
            TransactionManager.startTransaction();
            FlightReservation flightReservation = this.getFlightReservationFromSystem(flightReservationDTO.getId());
            if (!this.checkPermission(flightReservation.getReservationAgency())) {
                throw new InvalidReservationAgencyException();
            }
            this.getFlightReservationsSystem().cancelReservation(flightReservation);
            TransactionManager.commitTransaction();
        } catch (TransactionAbortedException e) {
            TransactionManager.clearTransaction();
            this.synchronizedErrPrint("Transaction aborted in cancelReservation");
            throw new ErrorInServerException();
        } catch (NotFoundException e) {
            TransactionManager.abortTransaction();
            throw new org.flightreservationsserverinterface.exceptions.NotFoundException();
        } catch (FlightReservationAlreadyCanceledException e) {
            TransactionManager.abortTransaction();
            throw new org.flightreservationsserverinterface.exceptions.FlightReservationAlreadyCanceledException();
        } catch (InvalidReservationAgencyException e) {
            TransactionManager.abortTransaction();
            this.synchronizedErrPrint("InvalidReservationAgencyException in cancelReservation");
            throw e;
        } catch (Exception e) {
            TransactionManager.abortTransaction();
            this.synchronizedErrPrint("Unknown error in cancelReservation");
            throw new ErrorInServerException("Unknown Error");
        } finally {
            TransactionManager.clearTransaction();
        }
    }

    protected abstract boolean checkPermission(ReservationAgency reservationAgency);

    @Override
    public ArrayList<CustomerDTO> getCustomerByDNI(int dni) throws ErrorInServerException {
        try {
            TransactionManager.startTransaction();
            ArrayList<CustomerDTO> customerDTOs = POJOToDTO.customersToCustomerDTOs(this.getFlightReservationsSystem().getCustomerByDNI(dni));
            TransactionManager.commitTransaction();
            return customerDTOs;
        } catch (TransactionAbortedException e) {
            this.synchronizedErrPrint("Transaction aborted in getCustomerByDNI");
            TransactionManager.clearTransaction();
            throw new ErrorInServerException();
        } catch (Exception e) {
            TransactionManager.abortTransaction();
            this.synchronizedErrPrint("Unknown error in getCustomerByDNI");
            throw new ErrorInServerException("Unknown Error");
        } finally {
            TransactionManager.clearTransaction();
        }
    }

    @Override
    public CustomerDTO getCustomerByID(int id) throws org.flightreservationsserverinterface.exceptions.NotFoundException, ErrorInServerException {
        try {
            TransactionManager.startTransaction();
            CustomerDTO customerDTO = POJOToDTO.customerToCustomerDTO(this.getCustomerFromSystem(id));
            TransactionManager.commitTransaction();
            return customerDTO;
        } catch (TransactionAbortedException e) {
            TransactionManager.clearTransaction();
            this.synchronizedErrPrint("Transaction aborted in getCustomerByID");
            throw new ErrorInServerException();
        } catch (NotFoundException e) {
            TransactionManager.abortTransaction();
            throw new org.flightreservationsserverinterface.exceptions.NotFoundException();
        } catch (Exception e) {
            TransactionManager.abortTransaction();
            this.synchronizedErrPrint("Unknown error in getCustomerByID");
            throw new ErrorInServerException("Unknown Error");
        } finally {
            TransactionManager.clearTransaction();
        }
    }

    @Override
    public ArrayList<CustomerDTO> getCustomersByName(String name) throws ErrorInServerException {
        try {
            TransactionManager.startTransaction();
            ArrayList<CustomerDTO> customerDTOs = POJOToDTO.customersToCustomerDTOs(this.getFlightReservationsSystem().getCustomersByName(name));
            TransactionManager.commitTransaction();
            return customerDTOs;
        } catch (TransactionAbortedException e) {
            TransactionManager.clearTransaction();
            this.synchronizedErrPrint("Transaction aborted in getCustomersByName");
            throw new ErrorInServerException();
        } catch (Exception e) {
            TransactionManager.abortTransaction();
            this.synchronizedErrPrint("Unknown error in getCustomersByName");
            throw new ErrorInServerException("Unknown Error");
        } finally {
            TransactionManager.clearTransaction();
        }
    }

    @Override
    public FlightDTO getFlight(int id) throws org.flightreservationsserverinterface.exceptions.NotFoundException, ErrorInServerException {
        try {
            TransactionManager.startTransaction();
            FlightDTO flightDTO = POJOToDTO.flightToFlightDTO(this.getFlightFromSystem(id));
            TransactionManager.commitTransaction();
            return flightDTO;
        } catch (TransactionAbortedException e) {
            TransactionManager.clearTransaction();
            this.synchronizedErrPrint("Transaction aborted in getFlight");
            throw new ErrorInServerException();
        } catch (NotFoundException e) {
            TransactionManager.abortTransaction();
            throw new org.flightreservationsserverinterface.exceptions.NotFoundException();
        } catch (Exception e) {
            TransactionManager.abortTransaction();
            this.synchronizedErrPrint("Unknown error in getFlight");
            throw new ErrorInServerException("Unknown Error");
        } finally {
            TransactionManager.clearTransaction();
        }
    }

    @Override
    public FlightReservationDTO getFlightReservation(int id) throws org.flightreservationsserverinterface.exceptions.NotFoundException, ErrorInServerException {
        try {
            TransactionManager.startTransaction();
            FlightReservationDTO flightReservationDTO = POJOToDTO.flightReservationToFlightReservationDTO(this.getFlightReservationFromSystem(id));
            TransactionManager.commitTransaction();
            return flightReservationDTO;
        } catch (TransactionAbortedException e) {
            TransactionManager.clearTransaction();
            this.synchronizedErrPrint("Transaction aborted in getFlightReservation");
            throw new ErrorInServerException();
        } catch (NotFoundException e) {
            TransactionManager.abortTransaction();
            throw new org.flightreservationsserverinterface.exceptions.NotFoundException();
        } catch (Exception e) {
            TransactionManager.abortTransaction();
            this.synchronizedErrPrint("Unknown error in getFlightReservation");
            throw new ErrorInServerException("Unknown Error");
        } finally {
            TransactionManager.clearTransaction();
        }
    }

    @Override
    public ArrayList<FlightDTO> getFlights(String source, String destination, String fromDate, String toDate) throws ErrorInServerException {
        try {
            TransactionManager.startTransaction();
            ArrayList<FlightDTO> flightDTOs = POJOToDTO.flightsToFlightDTOs(this.getFlightReservationsSystem().getFlights(source, destination, fromDate, toDate));
            TransactionManager.commitTransaction();
            return flightDTOs;
        } catch (TransactionAbortedException e) {
            TransactionManager.clearTransaction();
            this.synchronizedErrPrint("Transaction aborted in getFlights");
            throw new ErrorInServerException();
        } catch (Exception e) {
            TransactionManager.abortTransaction();
            this.synchronizedErrPrint("Unknown error in getFlights");
            throw new ErrorInServerException("Unknown Error");
        } finally {
            TransactionManager.clearTransaction();
        }
    }

    protected ArrayList<FlightReservationDTO> getFlightReservationsBy(CustomerDTO customerDTO, ReservationAgency reservationAgency) throws NotFoundException, TransactionAbortedException {
        Customer customer = this.getCustomerFromSystem(customerDTO.getId());
        return POJOToDTO.flightReservationsToFlightReservationDTOs(this.getFlightReservationsSystem().getFlightReservationsBy(customer, reservationAgency));
    }

    protected ArrayList<FlightReservationDTO> getFlightReservationsFor(FlightDTO flightDTO, ReservationAgency reservationAgency) throws NotFoundException, TransactionAbortedException {
        Flight flight = this.getFlightFromSystem(flightDTO.getId());
        return POJOToDTO.flightReservationsToFlightReservationDTOs(this.getFlightReservationsSystem().getFlightReservationsFor(flight, reservationAgency));
    }

    protected ReservationAgency getReservationAgencyFromSystem(int id) throws NotFoundException, TransactionAbortedException {
        return this.getFlightReservationsSystem().getReservationAgency(id);
    }

    protected Customer getCustomerFromSystem(int id) throws NotFoundException, TransactionAbortedException {
        return this.getFlightReservationsSystem().getCustomerByID(id);
    }

    protected Flight getFlightFromSystem(int id) throws NotFoundException, TransactionAbortedException {
        return this.getFlightReservationsSystem().getFlight(id);
    }

    protected FlightReservation getFlightReservationFromSystem(int id) throws NotFoundException, TransactionAbortedException {
        return this.getFlightReservationsSystem().getFlightReservation(id);
    }

    protected FlightReservationsSystem getFlightReservationsSystem() {
        return this.flightReservationsSystem;
    }

    protected void setFlightReservationsSystem(FlightReservationsSystem flightReservationsSystem) {
        this.flightReservationsSystem = flightReservationsSystem;
    }

    protected void synchronizedErrPrint(String message) {
        synchronized (this) {
            System.err.println(message);
        }
    }

    protected void synchronizedOutPrint(String message) {
        synchronized (this) {
            System.err.println(message);
        }
    }
}
