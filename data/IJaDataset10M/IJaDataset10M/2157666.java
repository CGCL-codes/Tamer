package cwsexamples.wscoord.hotelservice;

import java.util.Date;

public class BookingInfo {

    private String bookingID;

    private Date startDate;

    private Date endDate;

    public BookingInfo(String bookingID, Date startDate, Date endDate) {
        this.bookingID = bookingID;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getBookingID() {
        return bookingID;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }
}
