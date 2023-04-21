package fi.tuska.jalkametri.dao;

import java.util.Date;
import java.util.List;

/**
 * Interface for accessing the statistics.
 * 
 * @author Tuukka Haapasalo
 */
public interface Statistics {

    /**
     * Calculates and returns general statistics information.
     */
    GeneralStatistics getGeneralStatistics();

    GeneralStatistics getGeneralStatistics(Date start, Date end);

    /**
     * @return the number of drinks consumed for each day in the given period.
     */
    List<DailyDrinkStatistics> getDailyDrinkAmounts(Date start, Date end);

    Date getFirstDrinkEventTime();
}
