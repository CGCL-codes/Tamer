package com.acm.sigapp;

import com.google.appengine.api.images.*;
import java.util.Date;

public class Event {

    public String name;

    public Date date;

    public String description;

    public String location;

    public Image picture;

    public Event(String n, Date da, String de, String l, Image p) {
        name = n;
        date = da;
        description = de;
        location = l;
        picture = p;
    }

    public Event(String n, Date da, String de, String l) {
        name = n;
        date = da;
        description = de;
        location = l;
    }
}
