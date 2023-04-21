package com.webapp.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "company")
public class Company extends Identity {

    private static final long serialVersionUID = 4343746680021923363L;

    public Company() {
        super();
    }

    public Company(int id, String name, int owner_id, String description, int mark, Date date, List<Service> service) {
        super(id, name);
        this.owner_id = owner_id;
        this.description = description;
        this.mark = mark;
        this.date = date;
        this.setService(service);
    }

    public Company(ResultSet rs) throws SQLException {
        super(rs.getInt(1), rs.getString(2));
        this.owner_id = rs.getInt(3);
        this.description = rs.getString(4);
        this.mark = rs.getInt(5);
        this.date = rs.getDate(6);
    }

    @Column(name = "owner_id")
    private int owner_id;

    @Column(name = "description")
    private String description;

    @Column(name = "mark")
    private int mark;

    @Column(name = "date")
    private Date date;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "company")
    private List<Service> service;

    public int getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(int ownerId) {
        owner_id = ownerId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setService(List<Service> service) {
        this.service = service;
    }

    public List<Service> getService() {
        return service;
    }
}
