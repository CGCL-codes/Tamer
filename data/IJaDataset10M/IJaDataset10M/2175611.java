package com.entelience.probe.itops;

import java.util.Date;

public class EventDaily {

    public Date date;

    public int assetId;

    public int publisherId;

    public Integer userId;

    public int level;

    public int businessHoursCounter;

    public int offHoursCounter;

    public String toString() {
        return date + "," + assetId + "," + publisherId + "," + userId + "," + level;
    }
}
