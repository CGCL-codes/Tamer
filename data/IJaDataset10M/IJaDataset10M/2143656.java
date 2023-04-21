package uk.ac.ncl.cs.instantsoap.wsapi;

import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A universally unique ID, used to identify jobs.
 *
 * @author Matthew Pocock
 * @author Cheng-Yang(Louis) Tang
 */
@XmlRootElement(name = "UUID")
public class UUID {

    private String uuid;

    public UUID() {
        uuid = java.util.UUID.randomUUID().toString();
    }

    public String getUuid() {
        return uuid;
    }

    @XmlValue()
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public boolean equals(Object obj) {
        return ((UUID) obj).uuid.equals(this.uuid);
    }

    public int hashCode() {
        return uuid.hashCode();
    }

    public String toString() {
        return getClass() + " { uuid: " + uuid + " }";
    }
}
