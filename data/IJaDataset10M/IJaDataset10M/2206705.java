package edu.lehigh.mab305.swproj.ConferenceModel;

import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;

public class Location {

    protected String parentURI = null, locationURI = null, typeURI = null;

    protected String parentTypeURI = null, displayName = null;

    public Location(String parentURI, String parentTypeURI, String locationURI) {
        super();
        this.parentURI = parentURI;
        this.locationURI = locationURI;
        this.parentTypeURI = parentTypeURI;
        if (this.parentTypeURI != null) {
            this.typeURI = this.realizeType();
        }
    }

    /**
	 * Determine the location type URI for this newly-created location
	 * @return This will only return the URI for Confernce.CITY, as this is the only logical
	 * choice for any given new location (as no further information can be easily discerned.
	 * It will, however, return null if a location type not equal to COUNTRY, STATE, PROVINCE.
	 */
    protected String realizeType() {
        String ret = null;
        if (this.parentTypeURI.equals(Conference.COUNTRY) || this.parentTypeURI.equals(Conference.PROVINCE) || this.parentTypeURI.equals(Conference.STATE)) {
            ret = Conference.CITY;
        }
        return ret;
    }

    /**
	 * @return the displayName
	 */
    public String getDisplayName() {
        return displayName;
    }

    /**
	 * @param displayName the displayName to set
	 */
    public void setDisplayName(String displayName) {
        if (this.locationURI == null || this.locationURI.length() <= 0) {
            String s = displayName;
            s = s.replaceAll(" ", "_");
            try {
                this.locationURI = URLEncoder.encode(s, "UTF-8");
                this.locationURI = "#" + this.locationURI;
            } catch (UnsupportedEncodingException ie) {
                this.locationURI = s;
                this.locationURI = this.displayName.replaceAll("\"", "");
                this.locationURI = this.displayName.replaceAll("(", "");
                this.locationURI = this.displayName.replaceAll(")", "");
                this.locationURI = this.displayName.replaceAll("<", "");
                this.locationURI = this.displayName.replaceAll(">", "");
                this.locationURI = this.displayName.replaceAll(",", "");
                this.locationURI = this.displayName.replaceAll("#", "");
                this.locationURI = "#" + this.displayName;
            }
        }
        this.displayName = displayName;
    }

    /**
	 * @return the parentURI
	 */
    public String getParentURI() {
        return parentURI;
    }

    /**
	 * @param parentURI the parentURI to set
	 */
    public void setParentURI(String parentURI) {
        this.parentURI = parentURI;
    }

    /**
	 * @return the locationURI
	 */
    public String getLocationURI() {
        return locationURI;
    }

    /**
	 * @param locationURI the locationURI to set
	 */
    public void setLocationURI(String locationURI) {
        this.locationURI = locationURI;
    }

    /**
	 * @return the typeURI
	 */
    public String getTypeURI() {
        return typeURI;
    }

    /**
	 * @param typeURI the typeURI to set
	 */
    public void setTypeURI(String typeURI) {
        this.typeURI = typeURI;
    }

    /**
	 * @return the parentTypeURI
	 */
    public String getParentTypeURI() {
        return parentTypeURI;
    }

    /**
	 * @param parentTypeURI the parentTypeURI to set
	 */
    public void setParentTypeURI(String parentTypeURI) {
        this.parentTypeURI = parentTypeURI;
        this.typeURI = this.realizeType();
    }
}
