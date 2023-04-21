package com.acv.common.model.entity.impl;

import java.io.Serializable;
import java.util.Date;
import com.acv.common.model.constants.Engine;
import com.acv.common.model.entity.BookableService;
import com.acv.common.model.entity.BookableServiceType;
import com.acv.common.model.entity.Hotel;
import com.acv.common.model.entity.HotelRoomService;
import com.acv.common.model.entity.ImmutablePassengerGroup;

/**
 * <p>HotelRoomServiceImpl contains the details of a bookable hotel room product. These
 * products are returned as a response to the HotelCriterions. It contains all
 * the necessary details of a room that can be purchased by a client.
 */
public class HotelRoomServiceImpl extends AbstractBookableService implements BookableService, Serializable, HotelRoomService {

    private static final long serialVersionUID = 5505461229976795190L;

    private Hotel parentHotel;

    private String code;

    private Date startDate, endDate;

    private String mealCode;

    /** Room occupancy parameters */
    private int occupancyMax, occupancyMin, minimumAge, maximumNonAdults, maximumChildAge, maximumInfantAge;

    public HotelRoomServiceImpl() {
        super();
    }

    public HotelRoomServiceImpl(Engine sourceEngine, String code, String mealCode, Hotel hotel, ImmutablePassengerGroup passengerGroup) {
        super(sourceEngine, passengerGroup);
        this.code = code;
        this.parentHotel = hotel;
        this.mealCode = mealCode;
    }

    public HotelRoomServiceImpl(Engine sourceEngine, String code, String mealCode, ImmutablePassengerGroup passengerGroup) {
        super(sourceEngine, passengerGroup);
        this.code = code;
        this.mealCode = mealCode;
    }

    /**
     * Returns the owning Hotel of this room.
     * @return the owning Hotel of this room.
     */
    public Hotel getParentHotel() {
        return parentHotel;
    }

    public void setParentHotel(Hotel parentHotel) {
        this.parentHotel = parentHotel;
    }

    /**
     * Returns the hotel room code.
     * @return the hotel room code.
     */
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public BookableServiceType getType() {
        return BookableServiceType.HOTEL_ROOM;
    }

    public String getMealCode() {
        return mealCode;
    }

    public void setMealCode(String mealCode) {
        this.mealCode = mealCode;
    }

    public int getOccupancyMax() {
        return occupancyMax;
    }

    public void setOccupancyMax(int occupancyMax) {
        this.occupancyMax = occupancyMax;
    }

    public int getOccupancyMin() {
        return occupancyMin;
    }

    public void setOccupancyMin(int occupancyMin) {
        this.occupancyMin = occupancyMin;
    }

    public int getMinimumAge() {
        return minimumAge;
    }

    public void setMinimumAge(int minimumAge) {
        this.minimumAge = minimumAge;
    }

    public int getMaximumNonAdults() {
        return maximumNonAdults;
    }

    public void setMaximumNonAdults(int maximumNonAdults) {
        this.maximumNonAdults = maximumNonAdults;
    }

    public int getMaximumChildAge() {
        return maximumChildAge;
    }

    public void setMaximumChildAge(int maximumChildAge) {
        this.maximumChildAge = maximumChildAge;
    }

    public int getMaximumInfantAge() {
        return maximumInfantAge;
    }

    public void setMaximumInfantAge(int maximumInfantAge) {
        this.maximumInfantAge = maximumInfantAge;
    }
}
