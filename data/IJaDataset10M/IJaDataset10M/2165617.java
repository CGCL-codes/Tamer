package Domain;

import java.util.ArrayList;

public class Booking {

    private int bookingID;

    private Trip trip;

    private PaymentInfo payInfo;

    private ArrayList<Passenger> passengers;

    public Booking(Trip trip, ArrayList<Passenger> passengers, PaymentInfo payInfo) {
        this.trip = trip;
        this.passengers = passengers;
        this.payInfo = payInfo;
    }

    public int getID() {
        return bookingID;
    }

    public void setID(int ID) {
        bookingID = ID;
    }

    public ArrayList<Passenger> getPassengers() {
        return passengers;
    }

    public Trip getTrip() {
        return trip;
    }

    public PaymentInfo getPaymentInfo() {
        return payInfo;
    }
}
