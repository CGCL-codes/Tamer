package com.oz.lanslim.model;

import java.io.Serializable;
import com.oz.lanslim.Externalizer;
import com.oz.lanslim.SlimException;

public abstract class SlimContact implements Serializable {

    protected static final String HOST_SEPARATOR = "@";

    protected static final String PORT_SEPARATOR = ":";

    protected static final String FORMIDDEN_CHAR = "]";

    private String name = null;

    private SlimAvailabilityEnum availability = SlimAvailabilityEnum.OFFLINE;

    public SlimContact(String pName) throws SlimException {
        setName(pName);
    }

    public String getName() {
        return name;
    }

    public void setName(String pName) throws SlimException {
        if (pName == null || pName.trim().length() == 0) {
            throw new SlimException(Externalizer.getString("LANSLIM.167"));
        } else if (pName.indexOf(HOST_SEPARATOR) > 0) {
            throw new SlimException(Externalizer.getString("LANSLIM.168") + HOST_SEPARATOR);
        } else if (pName.indexOf(FORMIDDEN_CHAR) > 0) {
            throw new SlimException(Externalizer.getString("LANSLIM.168") + FORMIDDEN_CHAR);
        }
        name = pName;
    }

    public SlimAvailabilityEnum getAvailability() {
        return availability;
    }

    public void setAvailability(SlimAvailabilityEnum pAvail) {
        availability = pAvail;
    }

    public abstract boolean isGroup();

    public boolean equals(Object obj) {
        if (obj instanceof SlimContact) {
            SlimContact sc = (SlimContact) obj;
            return name.equalsIgnoreCase(sc.name) && isGroup() == sc.isGroup();
        }
        return false;
    }
}
