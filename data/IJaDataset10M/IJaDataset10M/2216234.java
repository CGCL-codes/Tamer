package net.sourceforge.xconf.toolbox.hibernate3;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import org.hibernate.HibernateException;
import org.joda.time.LocalDateTime;

/**
 * Hibernate UserType to persist <code>org.joda.time.LocalDateTime</code>
 * instances as SQL timestamps.
 *
 * @author Tom Czarniecki
 */
public class LocalDateTimeType extends ImmutableUserType {

    public int[] sqlTypes() {
        return new int[] { Types.TIMESTAMP };
    }

    public Class returnedClass() {
        return LocalDateTime.class;
    }

    public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws HibernateException, SQLException {
        Timestamp ts = rs.getTimestamp(names[0]);
        if (ts == null) {
            return null;
        }
        return new LocalDateTime(ts);
    }

    public void nullSafeSet(PreparedStatement ps, Object value, int index) throws HibernateException, SQLException {
        if (value == null) {
            ps.setNull(index, Types.TIMESTAMP);
        } else {
            LocalDateTime dt = (LocalDateTime) value;
            ps.setTimestamp(index, new Timestamp(dt.toDateTime().getMillis()));
        }
    }
}
