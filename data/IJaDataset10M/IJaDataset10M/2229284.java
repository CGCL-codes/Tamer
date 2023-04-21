package org.joda.time.contrib.datanucleus;

import org.datanucleus.ClassLoaderResolver;
import org.datanucleus.store.mapped.expression.QueryExpression;
import org.datanucleus.store.mapped.expression.ScalarExpression;
import org.joda.time.LocalDate;
import java.sql.Timestamp;

/**
 * Maps a {@link org.joda.time.LocalDate} to a TIMESTAMP column.
 *
 * @author Richard Bolkey
 * @see org.joda.time.LocalDate#toDateTimeAtStartOfDay()
 */
public class LocalDateMapping extends ObjectAsTimestampMapping {

    private static final LocalDate sampleMappedValue = new LocalDate();

    public Object getSampleValue(ClassLoaderResolver clr) {
        return sampleMappedValue;
    }

    /**
     * Method to return the default length of this type in the datastore. {@link org.joda.time.LocalDate} requires 10
     * characters ("yyyy-MM-dd")
     *
     * @param index The index position
     * @return The default length
     */
    public int getDefaultLength(int index) {
        return 10;
    }

    public Class getJavaType() {
        return LocalDate.class;
    }

    public ScalarExpression newLiteral(QueryExpression qs, Object value) {
        return super.newLiteral(qs, new Timestamp(((LocalDate) value).toDateTimeAtStartOfDay().getMillis()));
    }

    protected Timestamp objectToTimestamp(Object object) {
        Timestamp time;
        if (object instanceof LocalDate) {
            time = new Timestamp(((LocalDate) object).toDateTimeAtStartOfDay().getMillis());
        } else {
            time = (Timestamp) object;
        }
        return time;
    }

    protected Object timestampToObject(Timestamp datastoreValue) {
        return new LocalDate(datastoreValue.getTime());
    }
}
