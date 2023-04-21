package net.sf.mp.demo.conference.conference;

import java.sql.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import javax.persistence.*;
import net.sf.mp.demo.conference.conference.Speaker;
import net.sf.mp.demo.conference.conference.Address;
import net.sf.mp.demo.conference.conference.Conference;
import net.sf.mp.demo.conference.enumeration.conference.SponsorPrivilegeTypeEnum;
import net.sf.mp.demo.conference.enumeration.conference.SponsorStatusEnum;

/**
 *
 * <p>Title: Sponsor</p>
 *
 * <p>Description: Domain Object describing a Sponsor entity</p>
 *
 */
@Entity(name = "Sponsor")
@Table(name = "sponsor")
public class Sponsor {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", length = 45, nullable = false, unique = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private SponsorPrivilegeTypeEnum privilegeType;

    @Enumerated(EnumType.STRING)
    private SponsorStatusEnum status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "address_id", referencedColumnName = "id", nullable = false, unique = false)
    private Address addressId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "conference_id", referencedColumnName = "id", nullable = false, unique = false)
    private Conference conferenceId;

    @OneToMany(targetEntity = net.sf.mp.demo.conference.conference.Speaker.class, fetch = FetchType.LAZY, mappedBy = "sponsorId", cascade = CascadeType.REMOVE)
    private Set<Speaker> speakers = new HashSet<Speaker>();

    /**
    * Default constructor
    */
    public Sponsor() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SponsorPrivilegeTypeEnum getPrivilegeType() {
        return privilegeType;
    }

    public void setPrivilegeType(SponsorPrivilegeTypeEnum privilegeType) {
        this.privilegeType = privilegeType;
    }

    public SponsorStatusEnum getStatus() {
        return status;
    }

    public void setStatus(SponsorStatusEnum status) {
        this.status = status;
    }

    public Address getAddressId() {
        return addressId;
    }

    public void setAddressId(Address addressId) {
        this.addressId = addressId;
    }

    public Conference getConferenceId() {
        return conferenceId;
    }

    public void setConferenceId(Conference conferenceId) {
        this.conferenceId = conferenceId;
    }

    public Set<Speaker> getSpeakers() {
        if (speakers == null) {
            speakers = new HashSet<Speaker>();
        }
        return speakers;
    }

    public void setSpeakers(Set<Speaker> speakers) {
        this.speakers = speakers;
    }

    public void addSpeakers(Speaker speaker) {
        getSpeakers().add(speaker);
    }
}
