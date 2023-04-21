package org.domain.tickethuntercrud.session;

import org.domain.tickethuntercrud.entity.*;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("eventDiscountHome")
public class EventDiscountHome extends EntityHome<EventDiscount> {

    @In(create = true)
    EventDescriptionHome eventDescriptionHome;

    @In(create = true)
    DiscountHome discountHome;

    public void setEventDiscountId(Integer id) {
        setId(id);
    }

    public Integer getEventDiscountId() {
        return (Integer) getId();
    }

    @Override
    protected EventDiscount createInstance() {
        EventDiscount eventDiscount = new EventDiscount();
        return eventDiscount;
    }

    public void load() {
        if (isIdDefined()) {
            wire();
        }
    }

    public void wire() {
        getInstance();
        EventDescription eventDescription = eventDescriptionHome.getDefinedInstance();
        if (eventDescription != null) {
            getInstance().setEventDescription(eventDescription);
        }
        Discount discount = discountHome.getDefinedInstance();
        if (discount != null) {
            getInstance().setDiscount(discount);
        }
    }

    public boolean isWired() {
        if (getInstance().getEventDescription() == null) return false;
        if (getInstance().getDiscount() == null) return false;
        return true;
    }

    public EventDiscount getDefinedInstance() {
        return isIdDefined() ? getInstance() : null;
    }
}
