package org.neodatis.odb.test.update.nullobject;

import java.util.Date;

/**
 * SensorMeteo
 * 
 * 
 */
public class SensorMeteo {

    private String name;

    private int state;

    private int way;

    private Float km;

    private Meteo meteo;

    private boolean deleted;

    private boolean status;

    private Date creationDate;

    private Date updateDate;

    private User user;

    public String toString() {
        return "[" + name + "][" + state + "][" + status + "][" + way + "][" + km + "]";
    }

    public Float getKm() {
        return km;
    }

    public String getName() {
        return name;
    }

    public int getState() {
        return state;
    }

    public int getWay() {
        return way;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public boolean getDeleted() {
        return deleted;
    }

    public Meteo getMeteo() {
        return meteo;
    }

    public boolean getStatus() {
        return status;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public User getUser() {
        return user;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void setMeteo(Meteo meteo) {
        this.meteo = meteo;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setWay(int way) {
        this.way = way;
    }

    public void setKm(Float km) {
        this.km = km;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setState(int state) {
        this.state = state;
    }
}
