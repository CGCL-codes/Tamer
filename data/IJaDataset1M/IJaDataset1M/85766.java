package com.acv.common.model.entity.impl;

import java.io.Serializable;
import java.util.Date;
import com.acv.common.model.constants.Engine;
import com.acv.common.model.entity.BookableServiceType;
import com.acv.common.model.entity.ImmutablePassengerGroup;
import com.acv.common.model.entity.TicketService;

/**
 * Represents any Ticket product that can be purchased by the client.
 */
public class TicketServiceImpl extends AbstractBookableService implements Serializable, TicketService {

    private static final long serialVersionUID = 1419915285985867064L;

    private String code;

    private String categoryCode;

    private String destinationCode;

    private Date startDate;

    private Date endDate;

    public TicketServiceImpl() {
        super();
    }

    public TicketServiceImpl(Engine sourceEngine, String code, ImmutablePassengerGroup passengerGroup) {
        super(sourceEngine, passengerGroup);
        this.code = code;
    }

    /**
     * Returns the code for this ticket option.
     * @return the code for this ticket option.
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

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getDestinationCode() {
        return destinationCode;
    }

    public void setDestinationCode(String destinationCode) {
        this.destinationCode = destinationCode;
    }

    public BookableServiceType getType() {
        return BookableServiceType.TICKET;
    }
}
