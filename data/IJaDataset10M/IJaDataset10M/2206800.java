package org.torweg.pulse.component.statistics.model.aggregation;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.Session;
import org.torweg.pulse.component.statistics.model.StatisticsServer;
import org.torweg.pulse.component.statistics.model.Visit;
import org.torweg.pulse.util.time.Duration;

/**
 * Aggregates {@code Visit}s and stores information about the average time spent
 * on a "per {@code Visit}" basis for the set {@code Duration}.
 * 
 * @author Daniel Dietz
 * @version $Revision: 2105 $
 */
@XmlRootElement(name = "average-time-per-visit-aggregation")
@XmlAccessorOrder(XmlAccessOrder.UNDEFINED)
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class AverageTimePerVisitAggregation extends AbstractCountAggregation<AverageTimePerVisitAggregation> {

    /**
	 * The serialVersionUID.
	 */
    private static final long serialVersionUID = -8593340599059224392L;

    /**
	 * The average time per {@code Visit} in milliseconds.
	 */
    @XmlElement(name = "time")
    @Basic
    private double time = 0;

    /**
	 * Default constructor for Hibernate<sup>TM</sup> and JAXB.
	 */
    @Deprecated
    protected AverageTimePerVisitAggregation() {
        super();
    }

    /**
	 * Creates a new {@code AverageTimePerVisitAggregation} with the given
	 * {@code StatisticsServer} and the given {@code Duration}.
	 * 
	 * @param server
	 *            the {@code StatisticsServer}
	 * @param duration
	 *            the {@code Duration}
	 */
    public AverageTimePerVisitAggregation(final StatisticsServer server, final Duration duration) {
        super();
        setStatisticsServer(server);
        setDuration(duration);
    }

    /**
	 * Returns the average time in milliseconds per {@code Visit}.
	 * 
	 * @return the <tt>time</tt>
	 */
    public final double getTime() {
        return this.time;
    }

    /**
	 * Test if the given {@code Visit} is aggregate-able by this
	 * {@code CountryPerVisitAggregation}.
	 * 
	 * @param visit
	 *            the {@code Visit}
	 * 
	 * @return {@code true} if and only if this {@code AbstractAggregation} can
	 *         process the given {@code Visit}, {@code false} otherwise
	 * 
	 * @see org.torweg.pulse.component.statistics.model.aggregation.AbstractAggregation
	 *      #isAggregateable(org.torweg.pulse.component.statistics.model.Visit)
	 */
    @Override
    public final boolean isAggregateable(final Visit visit) {
        if (visit == null || !getStatisticsServer().equals(visit.getLastRecord().getStatisticsServer())) {
            return false;
        }
        return true;
    }

    /**
	 * Aggregates the given {@code Visit}.
	 * 
	 * @param visit
	 *            the {@code Visit}
	 * @param s
	 *            the {@code Session} UNUSED
	 * 
	 * @return {@code true} if and only if the {@code Visit} has been processed,
	 *         {@code false} otherwise
	 * 
	 * @see org.torweg.pulse.component.statistics.model.aggregation.AbstractAggregation
	 *      #aggregate(org.torweg.pulse.component.statistics.model.Visit)
	 */
    @Override
    public final boolean aggregate(final Visit visit, final Session s) {
        if (!isAggregateable(visit)) {
            LOGGER.warn("{} CANNOT AGGREGATE{}", this, visit);
            return false;
        }
        int currentCount = getCount();
        this.time = ((currentCount * this.time) + visit.getTimeSpan(true).getMilliseconds()) / (currentCount + 1);
        increase();
        return true;
    }

    /**
	 * {@inheritDoc}
	 * 
	 * @throws NullPointerException
	 *             if the given {@code AbstractAggregation<?>} is {@code null}
	 * @throws IllegalStateException
	 *             if the accumulation is being performed on a loaded
	 *             {@code AbstractAggregation<?>}
	 * @throws IllegalArgumentException
	 *             if the {@code Duration} of the {@code AbstractAggregation<?>}
	 *             the accumulation is being performed on does not contain the
	 *             {@code Duration} of the given {@code AbstractAggregation<?>}
	 */
    public final void accumulate(final AverageTimePerVisitAggregation aggr) {
        if (aggr == null) {
            throw new NullPointerException("The given AverageTimePerVisitAggregation aggr must not be null.");
        }
        if (getId() != null) {
            throw new IllegalStateException("A loaded aggregation cannot be used for accumulation.");
        }
        if (!getDuration().contains(aggr.getDuration())) {
            throw new IllegalArgumentException("The duration [" + aggr.getDuration() + "] of the given AverageTimePerVisitAggregation does not lie within [" + getDuration() + "].");
        }
        int currentCount = getCount();
        this.time = ((currentCount * this.time) + (aggr.getTime() * aggr.getCount())) / (currentCount + aggr.getCount());
        increaseBy(aggr.getCount());
    }
}
