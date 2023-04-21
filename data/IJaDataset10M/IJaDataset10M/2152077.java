package com.jeegallery.business.entities;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.apache.log4j.Logger;

/**
 *
 * @author marek
 */
@Entity
public class GFile implements Serializable, Comparable<GFile> {

    private static final long serialVersionUID = 3543563955237504269L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Column(unique = true)
    private String location;

    @ManyToOne(fetch = FetchType.EAGER)
    private Work work;

    private String type = "none";

    private long byteSize;

    @Temporal(TemporalType.DATE)
    private Date additionDate;

    @Temporal(TemporalType.TIME)
    private Date additionTime;

    public GFile(Work work, String name) {
        setAdditionDate(new Date());
        setAdditionTime(getAdditionDate());
        this.work = work;
        this.name = name;
    }

    /** Creates a new instance of File */
    public GFile() {
        setAdditionDate(new Date());
        setAdditionTime(getAdditionDate());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String toString() {
        return "File, " + "id=[" + this.id + "]" + "name=[" + name + "]" + "location=[" + location + "]";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String url) {
        this.location = url;
    }

    public boolean isLocal() {
        return !location.matches("^http://(.*)");
    }

    public Work getWork() {
        return work;
    }

    public void setWork(Work work) {
        this.work = work;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getAdditionDate() {
        return additionDate;
    }

    public void setAdditionDate(Date additionDate) {
        this.additionDate = additionDate;
    }

    public Date getAdditionTime() {
        return additionTime;
    }

    public void setAdditionTime(Date additionTime) {
        this.additionTime = additionTime;
    }

    /**
     * 
     * @return File name, but without extension!
     */
    public String getFileName() {
        return location.substring(location.lastIndexOf("/") + 1, location.lastIndexOf("."));
    }

    /**
     * 
     * @return Thumbnail jpg image relative path (looks in work's directory)
     */
    public String getThumbnailLocation() {
        return work.getLocation() + "/thumb_" + getFileName() + ".jpg";
    }

    public long getByteSize() {
        return byteSize;
    }

    public void setByteSize(long bsize) {
        byteSize = bsize;
    }

    public String getFileSize() {
        return byteSize + " B";
    }

    @Transient
    protected Logger log = Logger.getLogger(this.getClass().getSimpleName());

    public boolean hasThumbnail() {
        File t = new File(com.jeegallery.GlobalsFactory.getGlobals().getRootDirectory() + "/" + getThumbnailLocation());
        log.info(t.getAbsolutePath());
        return t.exists();
    }

    public boolean isYoungerThen(Date d) {
        return !additionDate.before(d);
    }

    public int compareTo(GFile o) {
        return this.getName().compareTo(o.getName());
    }
}
