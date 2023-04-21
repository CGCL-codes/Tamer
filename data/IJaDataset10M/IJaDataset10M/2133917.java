package org.domain.tickethuntercrud.session;

import org.domain.tickethuntercrud.entity.*;
import java.util.ArrayList;
import java.util.List;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("placeHome")
public class PlaceHome extends EntityHome<Place> {

    public void setPlaceId(Integer id) {
        setId(id);
    }

    public Integer getPlaceId() {
        return (Integer) getId();
    }

    @Override
    protected Place createInstance() {
        Place place = new Place();
        return place;
    }

    public void load() {
        if (isIdDefined()) {
            wire();
        }
    }

    public void wire() {
        getInstance();
    }

    public boolean isWired() {
        return true;
    }

    public Place getDefinedInstance() {
        return isIdDefined() ? getInstance() : null;
    }

    public List<Seat> getSeats() {
        return getInstance() == null ? null : new ArrayList<Seat>(getInstance().getSeats());
    }

    public List<EventDescription> getEventDescriptions() {
        return getInstance() == null ? null : new ArrayList<EventDescription>(getInstance().getEventDescriptions());
    }
}
