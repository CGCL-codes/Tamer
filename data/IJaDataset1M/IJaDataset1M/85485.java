package de.sonivis.tool.core.statistics;

/**
 * Exception for database errors in statistic calculation.
 * 
 * @author Benedikt, Anne
 */
public class StatisticCalculationSQLException extends AbstractStatisticException {

    private static final long serialVersionUID = -4200954116388127523L;

    /**
	 * @param statistic
	 *            Statistic to calculate
	 * @param e
	 *            Inner exception.
	 */
    public StatisticCalculationSQLException(final Statistic<?> statistic, final Exception e) {
        super("Catched a database exception while calculation statistic '" + statistic.getName() + "'.", e);
    }
}
