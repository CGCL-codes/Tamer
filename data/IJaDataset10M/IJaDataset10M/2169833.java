package de.flingelli.scrum.datastructure;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(name = "impedimentType")
@SuppressWarnings("serial")
public final class Impediment implements Serializable, Cloneable {

    private String mId = null;

    private String mTitle = null;

    private String mDescription = null;

    private Date mDate = null;

    private boolean mResolved;

    private String mReported = null;

    public Impediment() {
        this("");
    }

    public Impediment(String title) {
        mDescription = "";
        mId = UUID.randomUUID().toString();
        mResolved = false;
        mTitle = title;
    }

    @XmlElement(name = "id")
    public String getId() {
        return mId;
    }

    void setId(String id) {
        this.mId = id;
    }

    @XmlElement(name = "title")
    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    @XmlElement(name = "description")
    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    @XmlElement(name = "date")
    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    @XmlElement(name = "resolved")
    public boolean isResolved() {
        return mResolved;
    }

    public void setResolved(boolean resolved) {
        mResolved = resolved;
    }

    @XmlElement(name = "reported")
    public String getReported() {
        return mReported;
    }

    public void setReported(Member reported) {
        mReported = reported.getId();
    }

    public void setReported(String reported) {
        mReported = reported;
    }

    public int hashCode() {
        return getId().length() + getTitle().length() + getDescription().length();
    }

    public boolean equals(Object object) {
        boolean result = false;
        if (object instanceof Impediment) {
            Impediment obj = (Impediment) object;
            result = obj.getId().equalsIgnoreCase(this.getId());
        }
        return result;
    }

    public String toString() {
        return getTitle();
    }

    public Impediment clone() {
        Impediment clone = new Impediment(getTitle());
        clone.setDate(getDate());
        clone.setDescription(getDescription());
        clone.setId(getId());
        clone.setReported(getReported());
        clone.setResolved(isResolved());
        return clone;
    }

    public void update(Impediment impediment) {
        setDate(impediment.getDate());
        setDescription(impediment.getDescription());
        setReported(impediment.getReported());
        setResolved(impediment.isResolved());
        setTitle(impediment.getTitle());
    }
}
