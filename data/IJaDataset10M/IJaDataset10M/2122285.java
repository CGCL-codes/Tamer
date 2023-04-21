package org.torweg.pulse.component.statistics.util;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import org.torweg.pulse.util.time.Duration;
import org.torweg.pulse.util.time.IHasDuration;

/**
 * Represents a row within the {@code ResultIHasDurationDataMatrix}.
 * 
 * @param <T>
 *            the {@code IHasDuration} implementation the
 *            {@code ResultIHasDurationDataMatrix} aggregates
 * @param <U>
 *            the {@code IRowDataBuilder}
 * @param <V>
 *            the underlying {@code AbstractRowData} row data
 *            implementation
 * 
 * @author Daniel Dietz
 * @version $Revision: 1562 $
 */
public final class ResultIHasDurationDataMatrixRow<T extends IHasDuration, U extends IRowDataBuilder<T, V>, V extends AbstractRowData<T>> implements IHasDuration, Serializable {

    /**
	 * The serialVersionUID.
	 */
    private static final long serialVersionUID = -425986691105614919L;

    /**
	 * The duration of the row.
	 */
    @XmlElement(name = "duration")
    private Duration duration;

    /**
	 * The data.
	 */
    @XmlElement(name = "data")
    private V data;

    /**
	 * The row builder.
	 */
    @XmlTransient
    private U rowBuilder;

    /**
	 * Default constructor.
	 */
    @Deprecated
    protected ResultIHasDurationDataMatrixRow() {
        super();
    }

    /**
	 * Creates a new {@code Row} with the given duration and the given
	 * builder.
	 * 
	 * @param dur
	 *            the {@code Duration}
	 * @param builder
	 *            the {@code IRowDataBuilder}
	 */
    public ResultIHasDurationDataMatrixRow(final Duration dur, final U builder) {
        super();
        setDuration(dur);
        setRowBuilder(builder);
    }

    /**
	 * Sets the duration.
	 * 
	 * @param dur
	 *            the {@code Duration}
	 */
    private void setDuration(final Duration dur) {
        this.duration = dur;
    }

    /**
	 * Returns the duration.
	 * 
	 * @return the duration.
	 * 
	 * @see org.torweg.pulse.util.time.IHasDuration#getDuration()
	 */
    public Duration getDuration() {
        return this.duration;
    }

    /**
	 * Sets the row builder.
	 * 
	 * @param builder
	 *            the {@code IRowDataBuilder}
	 */
    private void setRowBuilder(final U builder) {
        this.rowBuilder = builder;
    }

    /**
	 * Adds the given dataObject if the duration of the dataObject is within or
	 * equal the internal duration.
	 * 
	 * @param dataObject
	 *            the {@code IHasDuration}
	 * 
	 * @throws IllegalArgumentException
	 *             if the duration of the given dataObject is NOT within or
	 *             equal the internal duration
	 */
    public void add(final T dataObject) {
        if (!this.duration.contains(dataObject.getDuration())) {
            throw new IllegalArgumentException("Given " + dataObject.getDuration() + " is not within " + getDuration() + ".");
        }
        if (this.data == null) {
            this.data = this.rowBuilder.create(dataObject);
            return;
        }
        this.data.aggregate(dataObject);
    }

    /**
	 * Returns the row data object.
	 * 
	 * @return the {@code AbstractRowData} row data object
	 */
    public V getData() {
        return this.data;
    }
}
