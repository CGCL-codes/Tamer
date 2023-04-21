package it.hotel.controller.frontend.booking;

import it.hotel.model.CalendarUtils;
import it.hotel.model.booking.Booking;
import it.hotel.model.price.Price;
import it.hotel.model.room.Room;
import it.hotel.model.typology.Typology;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 *
 */
public class BookingFrontendDTO extends Booking {

    private String name;

    private String surname;

    private String mail;

    protected List<Room> roomsToBooking;

    private BigDecimal price;

    private Map<Typology, List<Room>> roomsToSelect;

    private Map<Typology, ArrayList<Price>> currentPrice;

    private BigDecimal total;

    private String begindate;

    private String finishdate;

    public String getBegindate() {
        return begindate;
    }

    public void setBegindate(String begindate) {
        this.begindate = begindate;
    }

    public String getFinishdate() {
        return finishdate;
    }

    public void setFinishdate(String finishdate) {
        this.finishdate = finishdate;
    }

    public BigDecimal getTotal() {
        return getPrice();
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    /**
	 * 
	 * @return
	 */
    public Map<Typology, ArrayList<Price>> getCurrentPrice() {
        GregorianCalendar startDate = getBeginDate();
        GregorianCalendar endDate = new GregorianCalendar();
        endDate = (GregorianCalendar) (this.getFinishDate()).clone();
        endDate = CalendarUtils.beforeDay(endDate);
        currentPrice = new HashMap<Typology, ArrayList<Price>>();
        int size = roomsToSelect.size();
        for (Map.Entry<Typology, List<Room>> valore : roomsToSelect.entrySet()) {
            Typology key = (Typology) valore.getKey();
            ArrayList<Price> price = null;
            price = key.getPriceListOnDataRange(startDate, endDate);
            price = orderPrice(price, startDate);
            currentPrice.put(key, price);
        }
        return currentPrice;
    }

    public List<Room> getRoomsToBooking() {
        return roomsToBooking;
    }

    public String getName() {
        return name;
    }

    public void setRoomsToBooking(List<Room> roomsToBooking) {
        this.roomsToBooking = roomsToBooking;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setRoomsToSelect(Map<Typology, List<Room>> roomsToSelect) {
        this.roomsToSelect = roomsToSelect;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
	 * 
	 * @return
	 */
    public BigDecimal getPrice() {
        GregorianCalendar startDate = getBeginDate();
        GregorianCalendar date = startDate;
        int day = CalendarUtils.GetNumberOfDays(getBeginDate(), getFinishDate());
        BigDecimal ret = new BigDecimal(0);
        for (int i = 0; i < roomsToBooking.size(); i++) {
            for (int j = 0; j < day; j++) {
                ret = ret.add(roomsToBooking.get(i).getTypology().getPrice(date));
                date = CalendarUtils.nexDay(date);
            }
        }
        return ret;
    }

    public void setPrice(BigDecimal price) {
        this.price = new BigDecimal(0);
    }

    public Map getRoomsToSelect() {
        return roomsToSelect;
    }

    private ArrayList<Price> orderPrice(ArrayList<Price> prices, GregorianCalendar firstDay) {
        ArrayList<Price> ret = new ArrayList<Price>();
        int j = 0;
        int i = 0;
        GregorianCalendar day = null;
        for (j = 0; j < (prices.size() + 2); j++) {
            for (i = 0; i < prices.size(); i++) {
                day = prices.get(i).getCalendarDate();
                if (CalendarUtils.daysBetween(firstDay, day) == j) {
                    ret.add(prices.get(i));
                }
            }
        }
        return ret;
    }
}
