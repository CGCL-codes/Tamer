package dataobjects;

import prodata.DataObject;

public class StateDO extends DataObject {

    private String countryCodeNN;

    private String stateCode;

    private String utcDifferenceNN;

    private String nameNN;

    private String timeZoneNN;

    /**
	 * @return Returns the countryCodeNN.
	 */
    public String getCountryCode() {
        return countryCodeNN;
    }

    /**
	 * @param i_countryCode The countryCodeNN to set.
	 */
    public void setCountryCode(final String i_countryCode) {
        countryCodeNN = i_countryCode;
    }

    /**
	 * @return Returns the nameNN.
	 */
    public String getName() {
        return nameNN;
    }

    /**
	 * @param i_name The nameNN to set.
	 */
    public void setName(final String i_name) {
        nameNN = i_name;
    }

    /**
	 * @return Returns the stateCode.
	 */
    public String getStateCode() {
        return stateCode;
    }

    /**
	 * @param i_stateCode The stateCode to set.
	 */
    public void setStateCode(final String i_stateCode) {
        stateCode = i_stateCode;
    }

    /**
	 * @return Returns the timeZoneNN.
	 */
    public String getTimeZone() {
        return timeZoneNN;
    }

    /**
	 * @param i_timeZone The timeZoneNN to set.
	 */
    public void setTimeZone(final String i_timeZone) {
        timeZoneNN = i_timeZone;
    }

    /**
	 * @return Returns the utcDifferenceNN.
	 */
    public String getUtcDifference() {
        return utcDifferenceNN;
    }

    /**
	 * @param i_utcDifference The utcDifferenceNN to set.
	 */
    public void setUtcDifference(final String i_utcDifference) {
        utcDifferenceNN = i_utcDifference;
    }
}
