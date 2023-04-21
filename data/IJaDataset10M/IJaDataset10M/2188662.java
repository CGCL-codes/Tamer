package com.acv.common.deals.display;

import java.util.Locale;
import com.acv.common.model.display.Adaptable;
import com.acv.common.model.display.HotelRoomAdapter;
import com.acv.common.model.display.OutboundFlightAdapter;
import com.acv.common.model.display.ReturnFlightAdapter;
import com.acv.common.model.display.sorting.SortableByName;
import com.acv.common.model.display.sorting.SortableByPrice;
import com.acv.common.model.display.sorting.SortableByRating;
import com.acv.dao.catalog.locations.resorts.model.Resort;

/**
 * @author bdasilva
 *
 */
public class DealPackageAdapter implements Adaptable, SortableByName, SortableByPrice, SortableByRating {

    private static final long serialVersionUID = 1l;

    private HotelRoomAdapter adaptedHotelRoom = null;

    private OutboundFlightAdapter AdaptedOutboundFlight = null;

    private ReturnFlightAdapter AdaptedReturnFlight = null;

    private float price = -1f;

    private boolean valid = true;

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getName(Locale locale) {
        if (adaptedHotelRoom.getParentHotel() != null && adaptedHotelRoom.getParentHotel().getName(locale) != null) {
            return adaptedHotelRoom.getParentHotel().getName(locale);
        }
        return "";
    }

    public String getHotelId() {
        if (adaptedHotelRoom.getParentHotel() != null) {
            return adaptedHotelRoom.getParentHotel().getId();
        }
        return "";
    }

    public Resort getHotel() {
        if (adaptedHotelRoom.getParentHotel() != null) {
            return adaptedHotelRoom.getParentHotel().getCmsResort();
        }
        return null;
    }

    public float getPrice() {
        return price;
    }

    public Integer getRating() {
        if (adaptedHotelRoom.getParentHotel() != null) {
            if (adaptedHotelRoom.getParentHotel().getRating() == null) {
                return -1;
            }
            return adaptedHotelRoom.getParentHotel().getRating();
        }
        return 0;
    }

    public String getWarningMessage() {
        return null;
    }

    public Boolean isValid() {
        return valid;
    }

    public HotelRoomAdapter getAdaptedHotelRoom() {
        return adaptedHotelRoom;
    }

    public void setAdaptedHotelRoom(HotelRoomAdapter adaptedHotelRoom) {
        this.adaptedHotelRoom = adaptedHotelRoom;
    }

    public OutboundFlightAdapter getAdaptedOutboundFlight() {
        return AdaptedOutboundFlight;
    }

    public void setAdaptedOutboundFlight(OutboundFlightAdapter adaptedOutboundFlight) {
        AdaptedOutboundFlight = adaptedOutboundFlight;
    }

    public ReturnFlightAdapter getAdaptedReturnFlight() {
        return AdaptedReturnFlight;
    }

    public void setAdaptedReturnFlight(ReturnFlightAdapter adaptedReturnFlight) {
        AdaptedReturnFlight = adaptedReturnFlight;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    /**
	 * Calculate the number of days in a package travel.
	 * @return int the number of days
	 */
    public int getPackageDaysDuration() {
        int result = 0;
        if (AdaptedOutboundFlight != null && this.AdaptedReturnFlight != null) {
            java.util.Date departure = AdaptedOutboundFlight.getDepartureDate();
            java.util.Date arrival = AdaptedReturnFlight.getArrivalDate();
            java.text.SimpleDateFormat spd = new java.text.SimpleDateFormat("dd");
            result = Integer.parseInt(spd.format(new java.util.Date(arrival.getTime() - departure.getTime())));
        }
        return result;
    }

    public String getHotelCode() {
        if (adaptedHotelRoom.getParentHotel() != null) {
            return adaptedHotelRoom.getParentHotel().getCode();
        }
        return "";
    }
}
