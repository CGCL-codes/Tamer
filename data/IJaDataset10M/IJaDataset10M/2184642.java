package net.sourceforge.javo2.example.business.entity;

/**
 * Continent
 * Created on 06/04/2008.
 * @author Nicolás L. Di Benedetto {@link mailto:nikodb@users.sourceforge.net}.<br>
 * @version ($Revision$)
 * @date 06/04/2008
 */
public class Continent {

    /**
	 * The name of this continent instance.
	 */
    private String name = null;

    /**
	 * The region for this continent.
	 */
    private String region = null;

    /**
	 * Default constructor.
	 */
    public Continent() {
    }

    /**
	 * Getter method for the name attribute.
	 * @return the name attribute.
	 */
    public String getName() {
        return (null == this.name) ? this.name = "N/A" : this.name;
    }

    /**
	 * Getter method for the region attribute.
	 * @return the region attribute.
	 */
    public String getRegion() {
        return (null == this.region) ? this.region = "N/A" : this.region;
    }

    /**
	 * Setter method for the name attribute.
	 * @param name the name attribute to set.
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * Setter method for the region attribute.
	 * @param region the region attribute to set.
	 */
    public void setRegion(String region) {
        this.region = region;
    }
}
