package com.spartancoder.accommodation.beans;

import com.spartancoder.accommodation.models.Place;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author andrei
 */
public class UsageBean {

    private int placeId, used, available;

    public void fill(ResultSet rs) throws SQLException {
        setPlaceId(rs.getInt("place_id"));
        setUsed(rs.getInt("used"));
        setAvailable(rs.getInt("available"));
    }

    public float getProcent() {
        return (float) getUsed() / getAvailable() * 100;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public int getPlaceId() {
        return placeId;
    }

    public PlaceBean getPlace() {
        return Place.fetch(getPlaceId());
    }

    public void setPlaceId(int placeId) {
        this.placeId = placeId;
    }

    public int getUsed() {
        return used;
    }

    public void setUsed(int used) {
        this.used = used;
    }
}
