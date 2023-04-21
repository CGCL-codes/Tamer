package org.torweg.pulse.component.statistics.model.aggregation;

import javax.persistence.Basic;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.hibernate.LazyInitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.torweg.pulse.component.statistics.model.RegexVersion;
import org.torweg.pulse.util.entity.AbstractBasicEntity;

/**
 * An abstract base class to derive a statistical data aggregation entity
 * aggregating {@code RegexVersion}s from. This would basically be the actual
 * counter, counting the {@code RegexVersion}s of the
 * {@code AbstractAggregation} the {@code AbstractRegexVersionedCounter} belongs
 * to.
 * 
 * @param <T>
 *            the type of the {@code AbstractAggregation} the
 *            {@code AbstractRegexVersionedCounter} belongs to
 * 
 * @author Daniel Dietz
 * @version $Revision: 2105 $
 * 
 */
@XmlRootElement(name = "abstract-regex-versioned-counter")
@XmlAccessorOrder(XmlAccessOrder.UNDEFINED)
@XmlAccessorType(XmlAccessType.FIELD)
@MappedSuperclass
public abstract class AbstractRegexVersionedCounter<T extends AbstractAggregation<?>> extends AbstractBasicEntity implements ICount {

    /**
	 * The serialVersionUID.
	 */
    private static final long serialVersionUID = 172778008120627703L;

    /**
	 * The logger.
	 */
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRegexVersionedCounter.class);

    /**
	 * The "parent-"{@code AbstractAggregation} the
	 * {@code AbstractRegexVersionedCounter} belongs to.
	 */
    @ManyToOne(optional = false)
    @XmlTransient
    private T abstractAggregation;

    /**
	 * The underlying {@code Version}.
	 */
    @ManyToOne(optional = false)
    @XmlTransient
    private RegexVersion version;

    /**
	 * The actual counter.
	 */
    @Basic
    @XmlElement(name = "counter")
    private int counter;

    /**
	 * Returns the "parent-"{@code AbstractAggregation} the
	 * {@code AbstractRegexVersionedCounter} belongs to.
	 * 
	 * @return the <tt>abstractAggregation</tt>
	 */
    @XmlTransient
    public final T getAggreation() {
        return this.abstractAggregation;
    }

    /**
	 * For JAXB only.
	 * 
	 * @return this.getAggreation().getId()
	 */
    @XmlElement(name = "aggregation-id")
    @SuppressWarnings("unused")
    @Deprecated
    private Long getAggreationIdJAXB() {
        try {
            return ((AbstractBasicEntity) getAggreation()).getId();
        } catch (LazyInitializationException e) {
            LOGGER.debug("igonred: " + e.getLocalizedMessage(), e);
            return null;
        }
    }

    /**
	 * Sets the given {@code AbstractAggregation} for the
	 * {@code AbstractRegexVersionedCounter}.
	 * 
	 * @param aggregation
	 *            the {@code AbstractAggregation}
	 * 
	 * @throws NullPointerException
	 *             if
	 *             <ul>
	 *             <li>given aggregation is {@code null}</li>
	 *             </ul>
	 */
    protected final void setAggreation(final T aggregation) {
        if (aggregation == null) {
            throw new NullPointerException("Given AbstractAggregation must not be null");
        }
        this.abstractAggregation = aggregation;
    }

    /**
	 * Returns the value of the underlying counter.
	 * 
	 * @return the count.
	 */
    public final int getCount() {
        return this.counter;
    }

    /**
	 * Sets the internal counter to the given value.
	 * 
	 * @param count
	 *            the value to set
	 * 
	 * @throws IllegalArgumentException
	 *             if
	 *             <ul>
	 *             <li>count &lt; 0</li>
	 *             </ul>
	 */
    protected final void setCounter(final int count) {
        if (count < 0) {
            throw new IllegalArgumentException("Given count must not be < 0.");
        }
        this.counter = count;
    }

    /**
	 * Returns the underlying {@code Version}.
	 * 
	 * @return the <tt>version</tt>
	 */
    @XmlTransient
    public final RegexVersion getVersion() {
        return this.version;
    }

    /**
	 * For JAXB only.
	 * 
	 * @return this.getVersion()
	 */
    @XmlElement(name = "version")
    @SuppressWarnings("unused")
    @Deprecated
    private RegexVersion getVersionJAXB() {
        try {
            return getVersion();
        } catch (LazyInitializationException e) {
            LOGGER.debug("ignored: {}", e.getLocalizedMessage());
            return null;
        }
    }

    /**
	 * Sets the given {@code Version}.
	 * 
	 * @param v
	 *            the {@code Version}
	 * 
	 * @throws NullPointerException
	 *             if
	 *             <ul>
	 *             <li>given version is {@code null}</li>
	 *             </ul>
	 */
    protected final void setVersion(final RegexVersion v) {
        if (v == null) {
            throw new NullPointerException("Given Version must not be null");
        }
        this.version = v;
    }

    /**
	 * Adds 1 to the internal counter.
	 */
    public final void increase() {
        this.counter++;
    }

    /**
	 * Adds the given value to the internal counter.
	 * 
	 * @param i
	 *            the value to add
	 */
    public final void increaseBy(final int i) {
        this.counter += i;
    }

    /**
	 * Checks the given string against the underlying version.
	 * 
	 * @param s
	 *            the string to check
	 * 
	 * @return {@code true} if the given match matches against the underlying
	 *         version, {@code false} otherwise
	 */
    public final boolean isMatch(final String s) {
        return s.matches(getVersion().getRegex());
    }

    /**
	 * Returns a string representation of the
	 * {@code AbstractRegexVersionedCounter}.
	 * 
	 * @return a string representation
	 */
    @Override
    public String toString() {
        return "{" + super.toString() + "@[" + getId() + "], count: " + getCount() + ", aggr: " + getAggreation().getClass().getCanonicalName() + ", version: " + getVersion() + "}";
    }
}
