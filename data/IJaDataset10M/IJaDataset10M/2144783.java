package model.address.hibdb;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Sebastian Kuerten (sebastian.kuerten@fu-berlin.de)
 * 
 */
@Entity
@Table(name = "roads")
public class Road {

    @Id
    @GeneratedValue
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "lon")
    private double lon;

    @Column(name = "lat")
    private double lat;

    @ManyToOne
    private City city;

    @ManyToMany
    private Set<Borough> boroughs;

    @ManyToMany
    private Set<Postcode> postcodes;

    /**
	 * Default constructor for hibernate.
	 */
    public Road() {
    }

    /**
	 * Constructor.
	 * 
	 * @param name
	 *            the name
	 * @param lon
	 *            the longitude.
	 * @param lat
	 *            the latitude.
	 * @param city
	 *            the containing city.
	 * @param boroughs
	 *            the containing boroughs.
	 * @param postcodes
	 *            the containing postal codes.
	 */
    public Road(String name, double lon, double lat, City city, Collection<Borough> boroughs, Collection<Postcode> postcodes) {
        this.name = name;
        this.lon = lon;
        this.lat = lat;
        this.city = city;
        this.boroughs = new HashSet<Borough>();
        this.boroughs.addAll(boroughs);
        this.postcodes = new HashSet<Postcode>();
        this.postcodes.addAll(postcodes);
    }

    /**
	 * @return the internal id.
	 */
    public long getId() {
        return id;
    }

    /**
	 * @return the name.
	 */
    public String getName() {
        return name;
    }

    /**
	 * @return the longitude.
	 */
    public double getLon() {
        return lon;
    }

    /**
	 * @return the latitude.
	 */
    public double getLat() {
        return lat;
    }

    /**
	 * @return the city.
	 */
    public City getCity() {
        return city;
    }

    /**
	 * @return the boroughs.
	 */
    public Set<Borough> getBoroughs() {
        return boroughs;
    }

    /**
	 * @return the postal codes.
	 */
    public Set<Postcode> getPostcodes() {
        return postcodes;
    }

    @Override
    public String toString() {
        return String.format("%s: %f,%f", name, lon, lat);
    }

    /**
	 * @return a nicely formated string.
	 */
    public String toNiceString() {
        StringBuilder strb = new StringBuilder();
        strb.append(name);
        if (boroughs.size() != 0) {
            strb.append(" (");
            Iterator<Borough> iterator = boroughs.iterator();
            while (iterator.hasNext()) {
                Borough borough = iterator.next();
                strb.append(borough.getName());
                if (iterator.hasNext()) strb.append(", ");
            }
            strb.append(") ");
        }
        if (postcodes.size() != 0) {
            strb.append(" (");
            Iterator<Postcode> iterator = postcodes.iterator();
            while (iterator.hasNext()) {
                Postcode postcode = iterator.next();
                strb.append(postcode);
                if (iterator.hasNext()) strb.append(", ");
            }
            strb.append(")");
        }
        return strb.toString();
    }

    /**
	 * @return a pretty formated string representation
	 */
    public String toVerboseString() {
        StringBuilder strb = new StringBuilder();
        strb.append(toString());
        strb.append(" Boroughs: ");
        if (boroughs.size() == 0) {
            strb.append("none");
        } else {
            Iterator<Borough> iterator = boroughs.iterator();
            while (iterator.hasNext()) {
                Borough borough = iterator.next();
                strb.append(borough);
                if (iterator.hasNext()) strb.append(", ");
            }
        }
        strb.append(" Postcodes: ");
        if (postcodes.size() == 0) {
            strb.append("none");
        } else {
            Iterator<Postcode> iterator = postcodes.iterator();
            while (iterator.hasNext()) {
                Postcode postcode = iterator.next();
                strb.append(postcode);
                if (iterator.hasNext()) strb.append(", ");
            }
        }
        return strb.toString();
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Road)) {
            return false;
        }
        Road other = (Road) o;
        return id == other.id;
    }
}
