package com.objectcode.time4u.server.ejb.entities;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "PERSONS_HISTORY")
public class PersonHistoryEntity {

    private long m_id;

    private PersonEntity m_person;

    private PersonEntity m_performedBy;

    private Date m_performedAt;

    private String m_name;

    private String m_email;

    public PersonHistoryEntity() {
    }

    public PersonHistoryEntity(PersonEntity person, PersonEntity performedBy) {
        m_person = person;
        m_performedBy = performedBy;
        m_performedAt = new Date();
        m_name = person.getName();
        m_email = person.getEmail();
    }

    @Id
    @GeneratedValue
    public long getId() {
        return m_id;
    }

    public void setId(long id) {
        m_id = id;
    }

    @Column(length = 50, nullable = false)
    public String getName() {
        return m_name;
    }

    public void setName(String name) {
        m_name = name;
    }

    @Column(length = 200, nullable = true)
    public String getEmail() {
        return m_email;
    }

    public void setEmail(String email) {
        m_email = email;
    }

    @JoinColumn(name = "person_id", nullable = false)
    @ManyToOne
    public PersonEntity getPerson() {
        return m_person;
    }

    public void setPerson(PersonEntity person) {
        m_person = person;
    }

    @JoinColumn(name = "performedBy_id", nullable = false)
    @ManyToOne
    public PersonEntity getPerformedBy() {
        return m_performedBy;
    }

    public void setPerformedBy(PersonEntity performedBy) {
        this.m_performedBy = performedBy;
    }

    @Column(nullable = false)
    public Date getPerformedAt() {
        return m_performedAt;
    }

    public void setPerformedAt(Date performedAt) {
        m_performedAt = performedAt;
    }
}
