package com.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
public class FlightLeg {

    @Id
    @GeneratedValue
    private Long id;

    private String legNumber;

    @ManyToOne
    private Airport depAirport;

    @ManyToOne
    private Airport arrAirport;

    private Date departureTime;

    private Date arrivalTime;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<LegInstance> leginstances = new HashSet<LegInstance>(0);

    @OneToMany(cascade = CascadeType.ALL)
    private Set<ConsistOf> consistofs = new HashSet<ConsistOf>(0);

    private int maximumCapacity;

    private int seatsBooked;

    public FlightLeg(String legNumber, Airport airportByDepartureAirport, Airport airportByArrivalAirport, Date departureTime, Date arrivalTime, Set<LegInstance> leginstances, Set<ConsistOf> consistofs, int maximumCapacity, int seatsBooked) {
        super();
        this.legNumber = legNumber;
        this.depAirport = airportByDepartureAirport;
        this.arrAirport = airportByArrivalAirport;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.leginstances = leginstances;
        this.consistofs = consistofs;
        this.maximumCapacity = maximumCapacity;
        this.seatsBooked = seatsBooked;
    }

    @SuppressWarnings("unused")
    private FlightLeg() {
    }

    public int getMaximumCapacity() {
        return maximumCapacity;
    }

    public void setMaximumCapacity(int maximumCapacity) {
        this.maximumCapacity = maximumCapacity;
    }

    public int getSeatsBooked() {
        return seatsBooked;
    }

    public void setSeatsBooked(int seatsBooked) {
        this.seatsBooked = seatsBooked;
    }

    /**
	 * @return the id
	 */
    public Long getId() {
        return id;
    }

    /**
	 * This one should be private!
	 * @param id the id to set
	 */
    @SuppressWarnings("unused")
    private void setId(Long id) {
        this.id = id;
    }

    /**
	 * @return the legNumber
	 */
    public String getLegNumber() {
        return legNumber;
    }

    /**
	 * @param legNumber the legNumber to set
	 */
    public void setLegNumber(String legNumber) {
        this.legNumber = legNumber;
    }

    /**
	 * @return the airportByDepartureAirport
	 */
    public Airport getDepAirport() {
        return depAirport;
    }

    /**
	 * @param airportByDepartureAirport the airportByDepartureAirport to set
	 */
    public void setDepAirport(Airport depAirport) {
        this.depAirport = depAirport;
    }

    /**
	 * @return the airportByArrivalAirport
	 */
    public Airport getArrAirport() {
        return arrAirport;
    }

    /**
	 * @param airportByArrivalAirport the airportByArrivalAirport to set
	 */
    public void setArrAirport(Airport arrivalAirport) {
        this.arrAirport = arrivalAirport;
    }

    /**
	 * @return the departureTime
	 */
    public Date getDepartureTime() {
        return departureTime;
    }

    /**
	 * @param departureTime the departureTime to set
	 */
    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }

    /**
	 * @return the arrivalTime
	 */
    public Date getArrivalTime() {
        return arrivalTime;
    }

    /**
	 * @param arrivalTime the arrivalTime to set
	 */
    public void setArrivalTime(Date arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    /**
	 * @return the leginstances
	 */
    public Set<LegInstance> getLeginstances() {
        return leginstances;
    }

    /**
	 * @param leginstances the leginstances to set
	 */
    public void setLeginstances(Set<LegInstance> leginstances) {
        this.leginstances = leginstances;
    }

    /**
	 * @return the consistofs
	 */
    public Set<ConsistOf> getConsistofs() {
        return consistofs;
    }

    /**
	 * @param consistofs the consistofs to set
	 */
    public void setConsistofs(Set<ConsistOf> consistofs) {
        this.consistofs = consistofs;
    }
}
