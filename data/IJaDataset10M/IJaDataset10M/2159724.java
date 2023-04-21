package com.google.code.linkedinapi.schema.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import com.google.code.linkedinapi.schema.Adapter1;
import com.google.code.linkedinapi.schema.Location;
import com.google.code.linkedinapi.schema.Locations;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "locationList" })
@XmlRootElement(name = "locations")
public class LocationsImpl implements Serializable, Locations {

    private static final long serialVersionUID = 2461660169443089969L;

    @XmlElement(name = "location", required = true, type = LocationImpl.class)
    protected List<Location> locationList;

    @XmlAttribute(required = true)
    @XmlJavaTypeAdapter(Adapter1.class)
    protected Long total;

    public List<Location> getLocationList() {
        if (locationList == null) {
            locationList = new ArrayList<Location>();
        }
        return this.locationList;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long value) {
        this.total = value;
    }
}
