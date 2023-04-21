package com.model;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
public class Airport {

    @Id
    @GeneratedValue
    private Long id;

    private String code;

    private String name;

    private String city;

    private String country;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<FlightLeg> flightlegsForArrivalAirport = new HashSet<FlightLeg>(0);

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Flight> flightsForArrivalAirport = new HashSet<Flight>(0);

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Flight> flightsForDepartureAirport = new HashSet<Flight>(0);

    @OneToMany(cascade = CascadeType.ALL)
    private Set<FlightLeg> flightlegsForDepartureAirport = new HashSet<FlightLeg>(0);

    public Airport(String code, String name, String city, String country, Set<FlightLeg> flightlegsForArrivalAirport, Set<Flight> flightsForArrivalAirport, Set<Flight> flightsForDepartureAirport, Set<FlightLeg> flightlegsForDepartureAirport) {
        this.code = code;
        this.name = name;
        this.city = city;
        this.country = country;
        this.flightlegsForArrivalAirport = flightlegsForArrivalAirport;
        this.flightsForArrivalAirport = flightsForArrivalAirport;
        this.flightsForDepartureAirport = flightsForDepartureAirport;
        this.flightlegsForDepartureAirport = flightlegsForDepartureAirport;
    }

    @SuppressWarnings("unused")
    private Airport() {
    }

    ;

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
	 * @return the code
	 */
    public String getCode() {
        return code;
    }

    /**
	 * @param code the code to set
	 */
    public void setCode(String code) {
        this.code = code;
    }

    /**
	 * @return the name
	 */
    public String getName() {
        return name;
    }

    /**
	 * @param name the name to set
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * @return the city
	 */
    public String getCity() {
        return city;
    }

    /**
	 * @param city the city to set
	 */
    public void setCity(String city) {
        this.city = city;
    }

    /**
	 * @return the country
	 */
    public String getCountry() {
        return country;
    }

    /**
	 * @param country the country to set
	 */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
	 * @return the flightlegsForArrivalAirport
	 */
    public Set<FlightLeg> getFlightlegsForArrivalAirport() {
        return flightlegsForArrivalAirport;
    }

    /**
	 * @param flightlegsForArrivalAirport the flightlegsForArrivalAirport to set
	 */
    public void setFlightlegsForArrivalAirport(Set<FlightLeg> flightlegsForArrivalAirport) {
        this.flightlegsForArrivalAirport = flightlegsForArrivalAirport;
    }

    /**
	 * @return the flightsForArrivalAirport
	 */
    public Set<Flight> getFlightsForArrivalAirport() {
        return flightsForArrivalAirport;
    }

    /**
	 * @param flightsForArrivalAirport the flightsForArrivalAirport to set
	 */
    public void setFlightsForArrivalAirport(Set<Flight> flightsForArrivalAirport) {
        this.flightsForArrivalAirport = flightsForArrivalAirport;
    }

    /**
	 * @return the flightsForDepartureAirport
	 */
    public Set<Flight> getFlightsForDepartureAirport() {
        return flightsForDepartureAirport;
    }

    /**
	 * @param flightsForDepartureAirport the flightsForDepartureAirport to set
	 */
    public void setFlightsForDepartureAirport(Set<Flight> flightsForDepartureAirport) {
        this.flightsForDepartureAirport = flightsForDepartureAirport;
    }

    /**
	 * @return the flightlegsForDepartureAirport
	 */
    public Set<FlightLeg> getFlightlegsForDepartureAirport() {
        return flightlegsForDepartureAirport;
    }

    /**
	 * @param flightlegsForDepartureAirport the flightlegsForDepartureAirport to set
	 */
    public void setFlightlegsForDepartureAirport(Set<FlightLeg> flightlegsForDepartureAirport) {
        this.flightlegsForDepartureAirport = flightlegsForDepartureAirport;
    }
}
