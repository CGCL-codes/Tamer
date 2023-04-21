package org.flightreservationsserver.model;

import org.flightreservationsserver.model.exceptions.FlightFullException;

/**
 * @author Facundo Manuel Quiroga
 * Jan 23, 2009
 * 
 */
public class Flight {

    protected int id;

    protected String date;

    protected String source;

    protected String destination;

    protected int maximumSeats;

    protected int occupiedSeats;

    public Flight(int id, String date, String source, String destination, int maximumSeats, int occupiedSeats) {
        super();
        this.id = id;
        this.date = date;
        this.source = source;
        this.destination = destination;
        this.maximumSeats = maximumSeats;
        this.occupiedSeats = occupiedSeats;
    }

    public Flight(int id, String date, String source, String destination, int maximumSeats) {
        this(id, date, source, destination, maximumSeats, 0);
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return this.date;
    }

    protected void setDate(String date) {
        this.date = date;
    }

    public String getSource() {
        return this.source;
    }

    protected void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return this.destination;
    }

    protected void setDestination(String destination) {
        this.destination = destination;
    }

    public int getMaximumSeats() {
        return this.maximumSeats;
    }

    protected void setMaximumSeats(int maximumSeats) {
        this.maximumSeats = maximumSeats;
    }

    public int getOccupiedSeats() {
        return this.occupiedSeats;
    }

    protected void setOccupiedSeats(int occupiedSeats) {
        this.occupiedSeats = occupiedSeats;
    }

    /**
	 * @return
	 */
    public boolean isFull() {
        return this.getOccupiedSeats() == this.getMaximumSeats();
    }

    /**
	 * @param customer
	 * @param reservation
	 * @param passengerName
	 * @return
	 * @throws FlightFullException 
	 */
    public FlightReservation bookPassenger(Customer customer, ReservationAgency reservationAgency, String passengerName) throws FlightFullException {
        if (this.isFull()) {
            throw new FlightFullException("Cant book passenger " + passengerName + " for customer " + customer.getName() + " in flight " + this.getId() + " from " + this.getSource() + " to " + this.getDestination() + " is full");
        }
        this.setOccupiedSeats(this.getOccupiedSeats() + 1);
        FlightReservation flightReservation = new FlightReservation(-1, this, customer, reservationAgency, passengerName);
        return flightReservation;
    }

    public void cancelReservation(FlightReservation flightReservation) {
        assert (flightReservation.getFlight() == this);
        this.setOccupiedSeats(this.getOccupiedSeats() - 1);
    }
}
