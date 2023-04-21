package ucm;

import model.TicketModel;
import model.DatabaseModel;
import view.BookingView;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @authors LianMing, FengChu
 * This usecase is for system to calculat the price of ticket that user booked
 */
public class UCCalculatePrice implements UCController {

    private TicketModel ticketModel;

    private Connection con = null;

    private Statement smt = null;

    public UCCalculatePrice() {
        ticketModel = new TicketModel();
    }

    public UCCalculatePrice(TicketModel ticketModel) {
        this.ticketModel = ticketModel;
    }

    public UCCalculatePrice(BookingView bookView) {
        this.ticketModel = bookView.getTicketModel();
    }

    public boolean run() {
        return true;
    }

    public String toString() {
        return calculate();
    }

    protected String calculate() {
        String flightID = ticketModel.getFlightID();
        String flightClass = ticketModel.getFlightClass();
        double price = 0.0;
        int count = ticketModel.getTicketsCount();
        double discount = ticketModel.getDiscount();
        double tax = ticketModel.getTax();
        double gst = ticketModel.getGst();
        try {
            if (con == null) {
                con = DatabaseModel.createConnection();
            }
            if (smt == null) {
                smt = con.createStatement();
            }
            smt = con.createStatement(ResultSet.CONCUR_UPDATABLE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = smt.executeQuery("SELECT * FROM ticket_info WHERE id = '" + flightID + "'");
            rs.first();
            price = Double.parseDouble(rs.getString(flightClass));
        } catch (SQLException e) {
        }
        double totalPrice = (gst + tax + 1) * (price * count);
        if (count > 1) {
            totalPrice = totalPrice * discount;
        }
        return String.valueOf(totalPrice);
    }
}
