package org.objectwiz.entity;

import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.Type;

/**
 *
 * @author xym
 */
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "cardnumber" }) })
@PrimaryKeyJoinColumn(name = "MoralEntity_id")
public class Technician extends MoralEntity {

    private static final long serialVersionUID = 1L;

    private Company company;

    private Planning planning;

    private Polygon zone;

    private MultiLineString itinerary;

    private int cardNumber;

    ;

    private Set<Intervention> interventions = new HashSet<Intervention>();

    public Technician() {
    }

    public Technician(String address, String city, int cardNumber, Point location, Polygon zone) {
        super(address, city, location);
        this.zone = zone;
        this.cardNumber = cardNumber;
        interventions = new HashSet<Intervention>();
    }

    public void setInterventions(Set<Intervention> interventions) {
        this.interventions = interventions;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public void setPlanning(Planning planning) {
        this.planning = planning;
    }

    public void setZone(Polygon zone) {
        this.zone = zone;
    }

    public void setItinerary(MultiLineString itinerary) {
        this.itinerary = itinerary;
    }

    public void setCardNumber(int cardNumber) {
        this.cardNumber = cardNumber;
    }

    public int getCardNumber() {
        return cardNumber;
    }

    @Type(type = "org.hibernatespatial.GeometryUserType")
    public MultiLineString getItinerary() {
        return itinerary;
    }

    @Type(type = "org.hibernatespatial.GeometryUserType")
    @Column(name = "work_zone")
    public Polygon getZone() {
        return zone;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_fk")
    public Company getCompany() {
        return company;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "planning_fk")
    public Planning getPlanning() {
        return planning;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "technician", fetch = FetchType.EAGER)
    public Set<Intervention> getInterventions() {
        return interventions;
    }
}
