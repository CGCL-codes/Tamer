package ua.com.m1995.client;

import java.util.HashMap;
import java.util.Map;

public class RealEstateObjectMapper {

    static Map<String, String> toRequestPK(RealEstateObject object) {
        Map<String, String> map = new HashMap<String, String>();
        String locationID = object.getLocationID();
        if ("".equals(locationID)) {
            map.put(FormConstants.FORM_IDENTIFIER_LOCATION_TEXT, object.getLocationText());
        } else {
            map.put(FormConstants.FORM_IDENTIFIER_LOCATION_ID, locationID);
        }
        map.put(FormConstants.FORM_IDENTIFIER_OPERATION_TYPE, object.getOperationType());
        map.put(FormConstants.FORM_IDENTIFIER_REAL_ESTATE_TYPE, object.getRealEstateType());
        map.put(FormConstants.FORM_IDENTIFIER_ROOMS, object.getRooms());
        map.put(FormConstants.FORM_IDENTIFIER_ROOMS_TYPE, object.getRoomsType());
        map.put(FormConstants.FORM_IDENTIFIER_PRICE, object.getPrice());
        map.put(FormConstants.FORM_IDENTIFIER_CITY_DISTRICT, object.getCityDistrict());
        map.put(FormConstants.FORM_IDENTIFIER_CITY_SUB_DISTRICT, object.getCitySubDistrict());
        map.put(FormConstants.FORM_IDENTIFIER_STREET, object.getStreet());
        map.put(FormConstants.FORM_IDENTIFIER_HOUSE_NUMBER, object.getHouseNumber());
        map.put(FormConstants.FORM_IDENTIFIER_SQUARE_ALL, object.getSquareAll());
        map.put(FormConstants.FORM_IDENTIFIER_SQUARE_LIVE, object.getSquareLive());
        map.put(FormConstants.FORM_IDENTIFIER_SQUARE_KITCHEN, object.getSquareKitchen());
        map.put(FormConstants.FORM_IDENTIFIER_SQUARE_LAND, object.getSquareLand());
        map.put(FormConstants.FORM_IDENTIFIER_FLOOR, object.getFloor());
        map.put(FormConstants.FORM_IDENTIFIER_FLOORS, object.getFloors());
        map.put(FormConstants.FORM_IDENTIFIER_DESCRIPTION, object.getDescription());
        map.put(FormConstants.FORM_IDENTIFIER_RENT_PERIOD, object.getRentPeriod());
        map.put(FormConstants.FORM_IDENTIFIER_TELEPHONE_TYPE, "0");
        return map;
    }
}
