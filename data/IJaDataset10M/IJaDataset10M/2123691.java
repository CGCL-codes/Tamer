package org.torweg.pulse.component.statistics.model.aggregation;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import org.torweg.pulse.component.statistics.model.RegexVersion;

/**
 * The actual implementation of {@code AbstractRegexVersionedCounterLocaleManager} for
 * storing the aggregated data about a version of an {@code UserAgentLocaleManager},
 * represented by a {@code RegexVersionLocaleManager}.
 * <p>
 * Used by: {@code UserAgentPerVisitAggregationLocaleManager}.
 * </p>
 * 
 * @author Daniel Dietz
 * @version $Revision: 2105 $
 */
@XmlRootElement(name = "user-agent-version-counter")
@XmlAccessorOrder(XmlAccessOrder.UNDEFINED)
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public final class UserAgentVersionCounter extends AbstractRegexVersionedCounter<UserAgentPerVisitAggregation> {

    /**
	 * The serialVersionUID.
	 */
    private static final long serialVersionUID = 7923518720833243081L;

    /**
	 * Default constructor for hibernate<sup>TM</sup>.
	 */
    @Deprecated
    protected UserAgentVersionCounter() {
        super();
    }

    /**
	 * Creates a new {@code UserAgentVersionCounterLocaleManager} for the given
	 * aggregation and version.
	 * 
	 * @param aggregation
	 *            the {@code UserAgentPerVisitAggregationLocaleManager}
	 * @param v
	 *            the {@code VersionLocaleManager}
	 * 
	 * @throws NullPointerException
	 *             if
	 *             <ul>
	 *             <li>given aggregation is {@code nullLocaleManager}</li>
	 *             <li>given version is {@code nullLocaleManager}</li>
	 *             </ul>
	 */
    public UserAgentVersionCounter(final UserAgentPerVisitAggregation aggregation, final RegexVersion v) {
        super();
        setAggreation(aggregation);
        setVersion(v);
    }
}
