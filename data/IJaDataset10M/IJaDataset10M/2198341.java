package org.jkoha.library;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import javax.jdo.InstanceCallbacks;

public class BiblioItem extends AbstractBiblioItem implements InstanceCallbacks {

    private String place;

    /**
     * 
     */
    public BiblioItem() {
    }

    /**
     * @return Returns the place.
     */
    public String getPlace() {
        return place;
    }

    /**
     * @param place The place to set.
     */
    public void setPlace(String place) {
        this.place = place;
    }

    public void jdoPreClear() {
    }

    public void jdoPreDelete() {
    }

    public void jdoPostLoad() {
    }

    public void jdoPreStore() {
        timestamp = new Timestamp(System.currentTimeMillis());
    }
}
