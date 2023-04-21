package net.sf.brightside.travelsystem.metamodel.beans;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import net.sf.brightside.travelsystem.core.beans.BaseBean;
import net.sf.brightside.travelsystem.core.beans.Identifiable;
import net.sf.brightside.travelsystem.metamodel.Airline;
import net.sf.brightside.travelsystem.metamodel.Airport;
import net.sf.brightside.travelsystem.metamodel.Flight;
import net.sf.brightside.travelsystem.metamodel.Plain;
import net.sf.brightside.travelsystem.metamodel.Reservation;

@Entity
public class FlightBean extends BaseBean implements Flight, Serializable, Identifiable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private Integer number;

    private Date arrivalTime;

    private Date departureTime;

    private Airport departureAirport;

    private Airport arrivalAirport;

    private Airline airline;

    private Plain plain;

    private List<Reservation> reservations;

    @Transient
    public String getFormatedArrivalTime() {
        DateFormat d = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.MEDIUM, Locale.UK);
        return d.format(arrivalTime).toString();
    }

    @Transient
    public String getFormatedDepartureTime() {
        DateFormat d = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.MEDIUM, Locale.UK);
        return d.format(departureTime).toString();
    }

    @ManyToOne(targetEntity = AirlineBean.class)
    public Airline getAirline() {
        return airline;
    }

    public void setAirline(Airline airline) {
        this.airline = airline;
    }

    @ManyToOne(targetEntity = AirportBean.class)
    public Airport getArrivalAirport() {
        return arrivalAirport;
    }

    public void setArrivalAirport(Airport arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    public Date getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Date arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    @ManyToOne(targetEntity = AirportBean.class)
    public Airport getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(Airport departureAirport) {
        this.departureAirport = departureAirport;
    }

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    public Date getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    @ManyToOne(targetEntity = PlainBean.class)
    public Plain getPlain() {
        return plain;
    }

    public void setPlain(Plain plain) {
        this.plain = plain;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "flight", targetEntity = ReservationBean.class)
    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    @Override
    public String toString() {
        return "Flight no. " + this.getNumber() + " , " + this.getAirline();
    }
}
