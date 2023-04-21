package com.ma_la.myRunning.domain;

import com.ma_la.myRunning.*;
import com.ma_la.myRunning.domain.EventRoute;
import java.util.Date;
import java.io.Serializable;

/**
 * PoJo fuer einen Staffelpreis
 *
 * @author <a href="mailto:mail@myrunning.de">Martin Lang</a>
 */
public class ScaledPrice implements Serializable {

    private static final long serialVersionUID = 1834213442420342850L;

    private Long id;

    private Double fee;

    private Double feeYouth;

    private Date toDate;

    private EventRoute eventRoute;

    public ScaledPrice() {
    }

    public Long getId() {
        return id;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public Double getFeeYouth() {
        return feeYouth;
    }

    public void setFeeYouth(Double feeYouth) {
        this.feeYouth = feeYouth;
    }

    public String getDatum() {
        return RunningSystemBean.formatToDateGerman(toDate);
    }

    public Date getToDate() {
        return toDate;
    }

    public void setStaffelPreisID(int staffelPreisID) {
        setId((long) staffelPreisID);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setToDate(Date date) {
        this.toDate = date;
    }

    public int getStaffelPreisID() {
        return getId().intValue();
    }

    public EventRoute getEventRoute() {
        return eventRoute;
    }

    public void setEventRoute(EventRoute eventRoute) {
        this.eventRoute = eventRoute;
    }
}
