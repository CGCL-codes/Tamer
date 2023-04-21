package com.acv.webapp.action.bus.search.utils;

import java.io.Serializable;
import com.acv.common.model.display.HotelRoomAdapter;
import com.acv.common.model.display.sorting.AdaptorList;

/**
 * class hotelRoomServices
 */
public class HotelRoomServices implements Serializable {

    private static final long serialVersionUID = 1L;

    private AdaptorList<HotelRoomAdapter> hotelRoomServices;

    /**
	 * Constructor for HotelRoomServices.
	 * @param tmpL AdaptorList<HotelRoomAdapter>
	 */
    public HotelRoomServices(AdaptorList<HotelRoomAdapter> tmpL) {
        hotelRoomServices = new AdaptorList<HotelRoomAdapter>();
        hotelRoomServices.addAll(tmpL);
        hotelRoomServices.sortByPrice(true);
    }

    /**
	 * Method getHotelRoomServices.
	 * @return AdaptorList<HotelRoomAdapter>
	 */
    public AdaptorList<HotelRoomAdapter> getHotelRoomServices() {
        return hotelRoomServices;
    }

    /**
	 * Method setHotelRoomServices.
	 * @param hotelRoomServices AdaptorList<HotelRoomAdapter>
	 */
    public void setHotelRoomServices(AdaptorList<HotelRoomAdapter> hotelRoomServices) {
        this.hotelRoomServices = hotelRoomServices;
    }

    /**
	 * Method sortByPrice.
	 * @param descendFlag boolean
	 */
    public void sortByPrice(boolean descendFlag) {
        hotelRoomServices.sortByPrice(descendFlag);
    }

    /**
	 * Method sortByRating.
	 * @param descendFlag boolean
	 */
    public void sortByRating(boolean descendFlag) {
        hotelRoomServices.sortByRating(descendFlag);
    }
}
