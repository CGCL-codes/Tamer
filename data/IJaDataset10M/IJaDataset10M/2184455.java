package net.solarnetwork.node;

/**
 * Information about a specific price location.
 * 
 * @author matt
 * @version $Revision: 1819 $
 */
public class PriceLocation {

    private Long locationId;

    private String currency;

    private String unit;

    @Override
    public String toString() {
        return "PriceDatum{locationId=" + this.locationId + ",currency=" + this.currency + ",unit=" + this.unit + '}';
    }

    /**
	 * @return the locationId
	 */
    public Long getLocationId() {
        return locationId;
    }

    /**
	 * @param locationId the locationId to set
	 */
    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    /**
	 * @return the currency
	 */
    public String getCurrency() {
        return currency;
    }

    /**
	 * @param currency the currency to set
	 */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
	 * @return the unit
	 */
    public String getUnit() {
        return unit;
    }

    /**
	 * @param unit the unit to set
	 */
    public void setUnit(String unit) {
        this.unit = unit;
    }
}
