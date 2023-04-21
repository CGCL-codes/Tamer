package com.acv.connector.test.ocean;

import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import com.acv.dao.profiles.model.UserProfile;
import com.vacv.ocean.protocol.request.BookingRQ;

/**
 * The booking request data.
 * @author Bin Chen
 *
 */
public class BookingRequest extends BookingRQ {

    /**
	 *
	 */
    private static final long serialVersionUID = -4097246382577971061L;

    private UserProfile userProfile;

    private String brochureCode;

    private int hotelServiceIndex = -1;

    private int outwardFlightServiceIndex = -1;

    private int returnFlightServiceIndex = -1;

    private float outWardFlightPrice;

    private float returnFlightPrice;

    private float hotelPrice;

    private float otherPrice;

    private String outWardFlightCurrency;

    private String returnFlightCurrency;

    private String hotelCurrency;

    private String otherCurrency;

    public boolean isAggregateRequest() {
        return false;
    }

    public String getHotelCurrency() {
        return hotelCurrency;
    }

    public void setHotelCurrency(String hotelCurrency) {
        this.hotelCurrency = hotelCurrency;
    }

    public String getOtherCurrency() {
        return otherCurrency;
    }

    public void setOtherCurrency(String otherCurrency) {
        this.otherCurrency = otherCurrency;
    }

    public String getOutWardFlightCurrency() {
        return outWardFlightCurrency;
    }

    public void setOutWardFlightCurrency(String outWardFlightCurrency) {
        this.outWardFlightCurrency = outWardFlightCurrency;
    }

    public String getReturnFlightCurrency() {
        return returnFlightCurrency;
    }

    public void setReturnFlightCurrency(String returnFlightCurrency) {
        this.returnFlightCurrency = returnFlightCurrency;
    }

    public float getOutWardFlightPrice() {
        return outWardFlightPrice;
    }

    public void setOutWardFlightPrice(float outWardFlightPrice) {
        this.outWardFlightPrice = outWardFlightPrice;
    }

    public float getReturnFlightPrice() {
        return returnFlightPrice;
    }

    public void setReturnFlightPrice(float returnFlightPrice) {
        this.returnFlightPrice = returnFlightPrice;
    }

    public float getHotelPrice() {
        return hotelPrice;
    }

    public void setHotelPrice(float hotelPrice) {
        this.hotelPrice = hotelPrice;
    }

    public float getOtherPrice() {
        return otherPrice;
    }

    public void setOtherPrice(float otherPrice) {
        this.otherPrice = otherPrice;
    }

    public void createPrice() {
    }

    public int getHotelServiceIndex() {
        return hotelServiceIndex;
    }

    public void setHotelServiceIndex(int hotelServiceIndex) {
        if (hotelServiceIndex < 0) {
            if (outwardFlightServiceIndex > this.hotelServiceIndex) outwardFlightServiceIndex--;
            if (returnFlightServiceIndex > this.hotelServiceIndex) returnFlightServiceIndex--;
        }
        this.hotelServiceIndex = hotelServiceIndex;
    }

    public int getOutwardFlightServiceIndex() {
        return outwardFlightServiceIndex;
    }

    public void setOutwardFlightServiceIndex(int outwardFlightServiceIndex) {
        if (outwardFlightServiceIndex < 0) {
            if (hotelServiceIndex > this.outwardFlightServiceIndex) hotelServiceIndex--;
            if (returnFlightServiceIndex > this.outwardFlightServiceIndex) returnFlightServiceIndex--;
        }
        this.outwardFlightServiceIndex = outwardFlightServiceIndex;
    }

    public int getReturnFlightServiceIndex() {
        return returnFlightServiceIndex;
    }

    public void setReturnFlightServiceIndex(int returnFlightServiceIndex) {
        if (returnFlightServiceIndex < 0) {
            if (hotelServiceIndex > this.returnFlightServiceIndex) hotelServiceIndex--;
            if (outwardFlightServiceIndex > this.returnFlightServiceIndex) outwardFlightServiceIndex--;
        }
        this.returnFlightServiceIndex = returnFlightServiceIndex;
    }

    public String getBrochureCode() {
        return brochureCode;
    }

    public void setBrochureCode(String brochureCode) {
        this.brochureCode = brochureCode;
    }

    /**
	 * @deprecated
	 */
    public Object parseResponse(String response) throws MarshalException, ValidationException, MappingException {
        return super.parseResponse(response);
    }

    public String getRequestName() {
        return "BookingRequest";
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }
}
