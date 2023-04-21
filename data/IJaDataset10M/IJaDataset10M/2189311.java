package es.ua.qallme;

/**
 * Auxiliar class that contains some elements to handle temporal expressions
 * 
 * @author Alicante Team
 * @version SVN $Rev$ by $Author$
 */
public class TemporalExpression {

    /**
	 * String instance for the attribute value
	 */
    private String value;

    /**
	 * String instance for the attribute token
	 */
    private String token;

    /**
	 * String instance for the attribute currentDate
	 */
    private String currentDate;

    /**
	 * String instance for the attribute startPeriod
	 */
    private String startPeriod;

    /**
	 * String instance for the attribute endPeriod
	 */
    private String endPeriod;

    /**
	 * Creates a new instance of {@link TemporalExpression}
	 */
    public TemporalExpression() {
        value = new String();
        token = new String();
        currentDate = new String();
        startPeriod = new String();
        endPeriod = new String();
    }

    /**
	 * Get method for attribute value
	 * 
	 * @return String value of the attribute value
	 */
    public String getValue() {
        return value;
    }

    /**
	 * Get method for attribute token
	 * 
	 * @return String value of the attribute token
	 */
    public String getToken() {
        return token;
    }

    /**
	 * Get method for attribute currentDate
	 * 
	 * @return String value of the attribute currentDate
	 */
    public String getCurrentDate() {
        return currentDate;
    }

    /**
	 * Get method for attribute startPeriod
	 * 
	 * @return String value of the attribute startPeriod
	 */
    public String getStartPeriod() {
        return startPeriod;
    }

    /**
	 * Get method for attribute endPeriod
	 * 
	 * @return String value of the attribute endPeriod
	 */
    public String getEndPeriod() {
        return endPeriod;
    }

    /**
	 * Set method for attribute value
	 * 
	 * @param _in 
	 *            new String value for the attribute value
	 */
    public void setValue(String _in) {
        value = _in;
    }

    /**
	 * Set method for attribute token
	 * 
	 * @param _in 
	 *            new String value for the attribute token
	 */
    public void setToken(String _in) {
        token = _in;
    }

    /**
	 * Set method for attribute currentDate
	 * 
	 * @param _in 
	 *            new String value for the attribute currentDate
	 */
    public void setCurrentDate(String _in) {
        currentDate = _in;
    }

    /**
	 * Set method for attribute startPeriod
	 * 
	 * @param _in 
	 *            new String value for the attribute startPeriod
	 */
    public void setStartPeriod(String _in) {
        startPeriod = _in;
    }

    /**
	 * Set method for attribute endPeriod
	 * 
	 * @param _in 
	 *            new String value for the attribute endPeriod
	 */
    public void setEndPeriod(String _in) {
        endPeriod = _in;
    }
}
