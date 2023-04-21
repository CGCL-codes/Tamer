package com.hack23.cia.model.api.sweden.configuration;

/**
 * The Interface OperationalStatementData.
 */
public interface OperationalStatementData {

    /**
	 * The Enum Month.
	 */
    public enum Month {

        /** The JANUARY. */
        JANUARY, /** The FEBRUARY. */
        FEBRUARY, /** The MARCH. */
        MARCH, /** The APRIL. */
        APRIL, /** The MAY. */
        MAY, /** The JUNE. */
        JUNE, /** The JULY. */
        JULY, /** The AUGUST. */
        AUGUST, /** The SEPTEMBER. */
        SEPTEMBER, /** The OCTOBER. */
        OCTOBER, /** The NOVEMBER. */
        NOVEMBER, /** The DECEMBER. */
        DECEMBER
    }

    /**
	 * Gets the year.
	 * 
	 * @return the year
	 */
    Integer getYear();

    /**
	 * Gets the month.
	 * 
	 * @return the month
	 */
    Month getMonth();

    /**
	 * Gets the state subsidy result.
	 * 
	 * @return the state subsidy result
	 */
    double getStateSubsidyResult();

    /**
	 * Gets the state subsidy id.
	 * 
	 * @return the state subsidy id
	 */
    String getStateSubsidyId();

    /**
	 * Gets the name.
	 * 
	 * @return the name
	 */
    String getName();
}
