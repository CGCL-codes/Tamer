package net.comensus.gh.core.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author faraya
 */
@Entity
@Table(name = "gh_hotel")
@NamedQueries({ @NamedQuery(name = "Hotel.findAll", query = "SELECT h FROM Hotel h"), @NamedQuery(name = "Hotel.findByCompanyId", query = "SELECT h FROM Hotel h WHERE h.company.companyId = :companyId"), @NamedQuery(name = "Hotel.findByHotelId", query = "SELECT h FROM Hotel h WHERE h.hotelId = :hotelId") })
public class Hotel extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "hotel_id", nullable = false)
    @SequenceGenerator(name = "hotel_gen", sequenceName = "hotel_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hotel_gen")
    private Long hotelId;

    @Basic(optional = false)
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Basic(optional = false)
    @Column(name = "address", nullable = false, length = 250)
    private String address;

    @Basic(optional = false)
    @Column(name = "business_name", nullable = false, length = 100)
    private String businessName;

    @Basic(optional = false)
    @Column(name = "identification", nullable = false)
    private String identification;

    @Basic(optional = false)
    @Column(name = "number_of_rooms", nullable = false)
    private int numberOfRooms;

    @Basic(optional = false)
    @Column(name = "fax_number", nullable = false, length = 50)
    private String faxNumber;

    @Basic(optional = false)
    @Column(name = "email", nullable = false, length = 50)
    private String email;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "hotel", fetch = FetchType.EAGER)
    private Set<Room> rooms;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "hotel", fetch = FetchType.EAGER)
    private Set<HotelParameter> hotelParameters;

    @JoinColumn(name = "company_id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Company company;

    @OneToMany(mappedBy = "hotel", fetch = FetchType.EAGER)
    private Set<Rate> rates;

    /**
     * 
     */
    public Hotel() {
    }

    public Hotel(Company company) {
        this.company = company;
    }

    /**
     *
     * @param company
     * @param name
     * @param address
     * @param businessName
     * @param identification
     * @param numberOfRooms
     * @param faxNumber
     * @param email
     */
    public Hotel(Company company, String name, String address, String businessName, String identification, int numberOfRooms, String faxNumber, String email) {
        this.company = company;
        this.name = name;
        this.businessName = businessName;
        this.identification = identification;
        this.numberOfRooms = numberOfRooms;
        this.faxNumber = faxNumber;
        this.email = email;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    /**
     * indica la direccion del hotel
     * @return
     */
    public String getAddress() {
        return address;
    }

    /**
     * 
     * @param address
     */
    public void setHotelAddress(String address) {
        this.address = address;
    }

    /**
     * indica la razon social a la cual se debe realizar la factura.
     * @return
     */
    public String getBusinessName() {
        return businessName;
    }

    /**
     *
     * @param businessName
     */
    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    /**
     * indica el correo electronico del hotel
     * @return
     */
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * indica el numero de fax del hotel
     * @return
     */
    public String getFaxNumber() {
        return faxNumber;
    }

    /**
     *
     * @param faxNumber
     */
    public void setFaxNumber(String faxNumber) {
        this.faxNumber = faxNumber;
    }

    /**
     * indica la cedula juridica del hotel
     * @return
     */
    public String getIdentification() {
        return identification;
    }

    /**
     * 
     * @param identification
     */
    public void setIdentification(String identification) {
        this.identification = identification;
    }

    /**
     * indica el nombre del hotel
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * indica el numero de habitaciones que contiene un hotel.
     * @return
     */
    public int getNumberOfRooms() {
        return numberOfRooms;
    }

    /**
     *
     * @param numberOfRooms
     */
    public void setNumberOfRooms(int numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    /**
     * 
     * @return
     */
    public Set<HotelParameter> getHotelParameters() {
        if (hotelParameters == null) {
            hotelParameters = new HashSet<HotelParameter>();
        }
        return hotelParameters;
    }

    /**
     *
     * @param hotelParameters
     */
    public void setHotelParameters(Set<HotelParameter> hotelParameters) {
        this.hotelParameters = hotelParameters;
    }

    /**
     *
     * @return
     */
    public Set<Room> getRooms() {
        if (rooms == null) {
            rooms = new HashSet<Room>();
        }
        return rooms;
    }

    /**
     *
     * @param rooms
     */
    public void setRooms(Set<Room> rooms) {
        this.rooms = rooms;
    }

    /**
     * 
     * @param room
     */
    public void addRoom(Room room) {
        getRooms().add(room);
        room.setHotel(this);
    }

    /**
     *
     * @return
     */
    public Company getCompany() {
        return company;
    }

    /**
     *
     * @param company
     */
    public void setCompany(Company company) {
        this.company = company;
    }

    public Set<Rate> getRates() {
        if (rates == null) {
            rates = new HashSet<Rate>();
        }
        return rates;
    }

    public void setRates(Set<Rate> rates) {
        this.rates = rates;
    }

    /**
     *
     * @param rate
     */
    public void addRate(Rate rate) {
        rate.setHotel(this);
        getRates().add(rate);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Hotel other = (Hotel) obj;
        if (this.hotelId != other.hotelId && (this.hotelId == null || !this.hotelId.equals(other.hotelId))) {
            return false;
        }
        if (this.company != other.company && (this.company == null || !this.company.equals(other.company))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (this.hotelId != null ? this.hotelId.hashCode() : 0);
        hash = 37 * hash + (this.company != null ? this.company.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return String.format("net.comensus.gh.core.entity.Hotel[hotelId=%d, name=%s]", hotelId, name);
    }

    @Override
    public boolean isTransient() {
        return (hotelId == null);
    }
}
