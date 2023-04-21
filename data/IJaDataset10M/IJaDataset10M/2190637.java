package net.hawk.digiextractor.util;

/**
 * This class encapsulates a modified julian date as used in DVB Transmissions.
 * 
 * It can be used to calculate the UTC representation of a given modified
 * julian date.
 * 
 * Note: Calculation is valid for dates between March, 1st 1900 and 
 *       February 28th, 2100.
 * @see EN 300 368 Annex C
 * 
 * @author Hawk
 *
 */
public class ModifiedJulianDate {

    /**
	 * 
	 */
    private static final int MONTHS_PER_YEAR = 12;

    /**
	 * 
	 */
    private static final int BASEYEAR = 1900;

    /**
	 * 
	 */
    private static final int THIRTEEN = 13;

    /**
	 * 
	 */
    private static final int DAYS_OFFSET = 14956;

    /**
	 * 
	 */
    private static final double MONTHS_OFFSET = 14956.1;

    /**
	 * 
	 */
    private static final double YEAR1900 = 15078.2;

    /**
	 * 
	 */
    private static final double DAYS_PER_MONTH = 30.6001;

    /**
	 * 
	 */
    private static final double DAYS_PER_YEAR = 365.25;

    /** The year. */
    private final transient int year;

    /** The month. */
    private final transient int month;

    /** The day. */
    private final transient int day;

    /**
	 * Instantiates a new modified julian date.
	 * 
	 * @param mjd the mjd
	 */
    public ModifiedJulianDate(final int mjd) {
        final int yrTick = (int) ((mjd - YEAR1900) / DAYS_PER_YEAR);
        final int moTick = (int) ((mjd - MONTHS_OFFSET - (int) (yrTick * DAYS_PER_YEAR)) / DAYS_PER_MONTH);
        day = mjd - DAYS_OFFSET - (int) (yrTick * DAYS_PER_YEAR) - (int) (moTick * DAYS_PER_MONTH);
        int k;
        if (moTick > THIRTEEN) {
            k = 1;
        } else {
            k = 0;
        }
        year = BASEYEAR + yrTick + k;
        month = moTick - 1 - k * MONTHS_PER_YEAR;
    }

    /**
	 * Gets the year.
	 * 
	 * @return the year
	 */
    public final int getYear() {
        return year;
    }

    /**
	 * Gets the month.
	 * 
	 * @return the month
	 */
    public final int getMonth() {
        return month;
    }

    /**
	 * Gets the day.
	 * 
	 * @return the day
	 */
    public final int getDay() {
        return day;
    }
}
