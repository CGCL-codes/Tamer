package org.torweg.pulse.util.geolocation;

import java.math.BigDecimal;
import java.util.Locale;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * represents a {@code GeoLocation}, a place on earth.
 * 
 * @author Thomas Weber
 * @version $Revision: 1409 $
 */
@XmlAccessorType(XmlAccessType.NONE)
public class GeoLocation {

    /**
	 * the coordinates.
	 */
    @XmlElement(name = "coordinates")
    private Coordinates coordinates;

    /**
	 * the country-code.
	 */
    @XmlElement(name = "country-code")
    private String countryCode;

    /**
	 * the region.
	 */
    @XmlElement(name = "region")
    private String region;

    /**
	 * the city.
	 */
    @XmlElement(name = "city")
    private String city;

    /**
	 * the postal code.
	 */
    @XmlElement(name = "postal-code")
    private String postalCode;

    /**
	 * locale for display values.
	 */
    @XmlTransient
    private Locale locale = Locale.getDefault();

    /**
	 * for JAXB.
	 */
    @Deprecated
    protected GeoLocation() {
        super();
    }

    /**
	 * creates a new {@code GeoLocation}.
	 * 
	 * @param coords
	 *            the coordinates or {@code null}
	 * @param country
	 *            the country code or {@code null}
	 * @param reg
	 *            the region or {@code null}
	 * @param cit
	 *            the city or {@code null}
	 * @param pc
	 *            the postal code or {@code null}
	 */
    public GeoLocation(final Coordinates coords, final String country, final String reg, final String cit, final String pc) {
        super();
        this.coordinates = coords;
        this.countryCode = country;
        this.region = reg;
        this.city = cit;
        this.postalCode = pc;
    }

    /**
	 * sets the locale for the {@code GeoLocation}.
	 * 
	 * @param l
	 *            the {@code Locale}
	 */
    public final void setLocale(final Locale l) {
        this.locale = l;
    }

    /**
	 * @return the coordinates or {@code null}
	 */
    public final Coordinates getCoordinates() {
        return this.coordinates;
    }

    /**
	 * @return the countryCode or {@code null}
	 */
    public final String getCountryCode() {
        return this.countryCode;
    }

    /**
	 * used by JAXB.
	 * 
	 * @return the display name of the country
	 */
    @SuppressWarnings("unused")
    @XmlElement(name = "country-display-name")
    private String getCountryDisplayName() {
        if (this.countryCode == null) {
            return null;
        }
        Locale dummy = new Locale("en", this.countryCode);
        return dummy.getDisplayCountry(this.locale);
    }

    /**
	 * @return the region or {@code null}
	 */
    public final String getRegion() {
        return this.region;
    }

    /**
	 * @return the city or {@code null}
	 */
    public final String getCity() {
        return this.city;
    }

    /**
	 * @return the postal code {@code null}
	 */
    public final String getPostalCode() {
        return this.postalCode;
    }

    /**
	 * Coordinates.
	 */
    public static class Coordinates {

        /**
		 * the longitude.
		 */
        @XmlAttribute(name = "longitude")
        private final BigDecimal longitude;

        /**
		 * the latitude.
		 */
        @XmlAttribute(name = "latitude")
        private final BigDecimal latitude;

        /**
		 * for JAXB.
		 */
        @Deprecated
        protected Coordinates() {
            super();
            this.longitude = BigDecimal.ZERO;
            this.latitude = BigDecimal.ZERO;
        }

        /**
		 * constructs new coordinates.
		 * 
		 * @param lo
		 *            the longitude
		 * @param la
		 *            the latitude
		 */
        public Coordinates(final BigDecimal lo, final BigDecimal la) {
            super();
            if ((lo == null) || (la == null)) {
                throw new IllegalArgumentException("The ordinates must not be null.");
            }
            this.longitude = lo;
            this.latitude = la;
        }

        /**
		 * @return Returns the longitude.
		 */
        public final BigDecimal getLongitude() {
            return this.longitude;
        }

        /**
		 * @return Returns the latitude.
		 */
        public final BigDecimal getLatitude() {
            return this.latitude;
        }
    }
}
